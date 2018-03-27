package tasks;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractScheduledService;
import data.access.CoinDAO;
import data.access.CoinPriceDAO;
import data.model.Coin;
import data.model.CoinPrice;
import market.manager.MarketApiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This tasks fetches periodically the current value of a list of coins and store it into the database.
 */
public class FetchCurrentCoinsPriceTask extends AbstractScheduledService {

    /***
     * The class logger.
     */
    private static Logger logger = LoggerFactory.getLogger(FetchCurrentCoinsPriceTask.class);

    /**
     * The list of coins to lookup the price.
     */
    private List<Coin> coinList;

    /**
     * The market api manager.
     */
    private MarketApiManager marketApiManager;

    /**
     * Constructor.
     * @param marketApiManager The market api manager.
     * @param coinList The list of coins to lookup.
     */
    public FetchCurrentCoinsPriceTask(MarketApiManager marketApiManager, List<Coin> coinList){

        // Validate preconditions
        Preconditions.checkNotNull(marketApiManager);
        Preconditions.checkNotNull(coinList);
        Preconditions.checkArgument(coinList.size() > 0);

        // Set members
        this.marketApiManager = marketApiManager;
        this.coinList = coinList;
    }

    /**
     * Executes the task.
     * Retrieve the current value of the coins and store it in the database.
     */
    @Override
    protected void runOneIteration() {
        logger.info("Start executing FetchCurrentCoinsPriceTask");

        // Get the short name for the coins to lookup
        List<String> coinShortNameList = this.coinList.stream().map(Coin::getCoinName).collect(Collectors.toList());

        // Create the DAOs to access the database
        CoinPriceDAO coinPriceDAO = new CoinPriceDAO();
        CoinDAO coinDAO = new CoinDAO();

        // Get the latest price for all the coins
        Map<String, BigDecimal> coinsCurrentValueMap = this.marketApiManager.getCoinsCurrentValue(coinShortNameList);

        // Retrieve the current timestamp
        Timestamp currentTimestamp = Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());

        // Save each coin's price in the database
        for (Map.Entry<String, BigDecimal> coinAndValue : coinsCurrentValueMap.entrySet()) {

            // Retrieve the coin
            Coin coin = coinDAO.findByShortName(coinAndValue.getKey());
            CoinPrice coinPrice = new CoinPrice();
            coinPrice.setCoin(coin);
            coinPrice.setPrice(coinAndValue.getValue());
            coinPrice.setDate(currentTimestamp);
            coinPriceDAO.create(coinPrice);
        }
        logger.info("End FetchCurrentCoinsPriceTask");
    }

    /**
     * Runs the tasks every 15 seconds.
     * @return The task's schedule.
     */
    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 15, TimeUnit.SECONDS);
    }
}