package tasks;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import core.PreconditionsValidation;
import data.access.CoinDAO;
import data.access.CoinPriceDAO;
import data.model.Coin;
import data.model.CoinPrice;
import market.manager.MarketApiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * This task can retrieve the historical price of a coin (from it's introduction on the market up to today's price).
 */
public class FetchHistoricalCoinPriceTask extends AbstractExecutionThreadService {

    /***
     * The class logger.
     */
    private static Logger logger = LoggerFactory.getLogger(FetchHistoricalCoinPriceTask.class);

    /**
     * The date when Bitcoin started to be traded.
     */
    private final ZonedDateTime bitcoinOriginZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2010, Month.JULY, 18, 0, 0,0), ZoneId.of("UTC"));

    /**
     * The coin to lookup.
     */
    private Coin coin;

    /**
     * Whether the task should delete the existing coin prices in the database for the given coin.
     */
    private boolean clearOldData;

    /**
     * The market api manager.
     */
    private MarketApiManager marketApiManager;

    /**
     * Constructor.
     * @param marketApiManager The market api manager.
     * @param coin The coin to lookup.
     * @param clearOldData Whether the task should delete the existing coin prices in the database for the given coin.
     */
    public FetchHistoricalCoinPriceTask(MarketApiManager marketApiManager, Coin coin, boolean clearOldData){

        // Validate preconditions
        Preconditions.checkNotNull(marketApiManager);
        Preconditions.checkNotNull(coin);
        PreconditionsValidation.checkStringNotEmpty(coin.getShortName());

        // Set members
        this.marketApiManager = marketApiManager;
        this.coin = coin;
        this.clearOldData = clearOldData;
    }

    /**
     * Runs the task. It will retrieve all the historical prices (value of the coin for each day since it started being traded on exchanges)
     * and store it in the database.
     */
    @Override
    protected void run() {

        logger.info("Start executing FetchHistoricalCoinPriceTask");

        // Delete all the old data if requested
        if (this.clearOldData)
            this.deleteOldCoinPriceData();

        // First step is to know when we should start fetching the price
        ZonedDateTime currentZoneDateTime = this.findCoinTradingStartDate();
        CoinPriceDAO coinPriceDAO = new CoinPriceDAO();

        // Loop from origin to now (except today's date)
        while (currentZoneDateTime.plusDays(1).isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
            BigDecimal coinValue = this.marketApiManager.getCoinHistoricalValue(this.coin.getShortName(), currentZoneDateTime);

            // Skip if the price is 0
            if (coinValue.equals(BigDecimal.valueOf(0.0)))
            {
                currentZoneDateTime = currentZoneDateTime.plusDays(1);
                continue;
            }

            // Store the price in the database
            CoinPrice coinPrice = new CoinPrice();
            coinPrice.setCoin(this.coin);
            coinPrice.setPrice(coinValue);
            coinPrice.setDate(java.sql.Timestamp.from(currentZoneDateTime.toInstant()));
            coinPriceDAO.create(coinPrice);

            // Increment the time to lookup
            currentZoneDateTime = currentZoneDateTime.plusDays(1);
        }

        // Count how many coin prices are stored in the database and log it
        logger.info("CoinPrices in the database: " + new CoinDAO().findById(this.coin.getId()).getCoinPrices().size() + " for the coin: " + this.coin.getShortName());
        logger.info("End FetchCoinTask");
    }

    /**
     * Deletes all the CoinPrice entity in the database that are older than yesterday.
     */
    private void deleteOldCoinPriceData() {

        // Create the coin price DAO
        CoinPriceDAO coinPriceDAO = new CoinPriceDAO();

        // Return early if there is nothing to delete
        if(null == this.coin.getCoinPrices()) {
            return;
        }

        // Delete all the coinPrice older than a day
        for (CoinPrice coinPrice : this.coin.getCoinPrices()) {
            if (coinPrice.getDate().after(new Timestamp(ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).toEpochSecond()))) {
                coinPriceDAO.delete(coinPrice);
            }
        }
    }

    /**
     * Finds the date when the coin started to be traded on exchanges.
     * @return the date when the coin started to be traded on exchanges
     */
    private ZonedDateTime findCoinTradingStartDate() {

        // We will use a bisection method to find the start trade date
        // We know that all the coins have started to be traded after Bitcoin, that's our low date
        ZonedDateTime lowDateTime = this.bitcoinOriginZonedDateTime;
        ZonedDateTime highDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // Check there is a solution (i.e: the coin has started to be traded
        if(this.marketApiManager.getCoinHistoricalValue(this.coin.getShortName(), highDateTime).equals(BigDecimal.valueOf(0)))
            return null;

        // Find the start date
        while (true) {

            // Compute the number of days between the low and high date
            long daysBetweenHighAndLow = ChronoUnit.DAYS.between(lowDateTime, highDateTime);

            // Return if the two dates are close enough
            if (daysBetweenHighAndLow <= 1)
                return highDateTime;

            // Get the value of the date between the low and high date
            ZonedDateTime midDateTime = lowDateTime.plusDays(daysBetweenHighAndLow/2);
            BigDecimal midDateCoinValue = this.marketApiManager.getCoinHistoricalValue(this.coin.getShortName(), midDateTime);

            // Increase the low date if the coin started to be traded after the mid date otherwise increase the high date
            if (midDateCoinValue.equals(BigDecimal.valueOf(0.0))) {
                lowDateTime = midDateTime.plusDays(1);
                continue;
            } else {
                highDateTime = midDateTime.minusDays(1);
            }
        }
    }
}