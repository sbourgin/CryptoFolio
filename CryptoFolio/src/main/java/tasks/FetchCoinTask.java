package tasks;

import com.google.common.util.concurrent.AbstractScheduledService;
import core.ApplicationConfiguration;
import data.DatabaseSessionFactory;
import data.access.CoinDAO;
import market.cryptocompare.CryptoCompareApiHelper;
import market.manager.LocalExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This recurrent task aims to refresh the coins in the database.
 */
public class FetchCoinTask extends AbstractScheduledService {

    /***
     * The class logger.
     */
    private static Logger logger = LoggerFactory.getLogger(FetchCoinTask.class);

    /**
     * Executes the task.
     * Updates or creates all coins in the database.
     * If a coin already exists, then its properties are refreshed.
     */
    @Override
    protected void runOneIteration() {
        logger.info("Start executing FetchCoinTask");

        // Create the market api manager
        MarketApiManager marketApiManager = new MarketApiManager((ApplicationConfiguration.IS_DEMO_MODE) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());

        // Get the list of coins
        Map<String, Coin> coinMap = marketApiManager.getCoinDictionary();

        // Create the coin DAO
        CoinDAO coinDAO = new CoinDAO();

        // For each coin fetched from the API create or update the coin in the database
        for (Coin coin : coinMap.values()) {

            // Check if the coin exists in the database
            data.model.Coin coinFromDB = coinDAO.findOneByProperty("externalId", coin.id);

            // If no -> Create it
            if (coinFromDB == null) {
                coinFromDB =  new data.model.Coin(coin);
                coinDAO.create(coinFromDB);
            }
            else {
                // If yes -> Update properties
                int id = coinFromDB.getId();
                coinFromDB = new data.model.Coin(coin);
                coinFromDB.setId(id);
                coinDAO.update(coinFromDB);
            }
        }

        // Count how many coins are stored in the database and log it
        logger.info("Coins in the database: " + coinDAO.countRowInDatabase());
        logger.info("End FetchCoinTask");
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.DAYS);
    }

    @Override
    protected void startUp() { }


    @Override
    protected void shutDown() { }
}