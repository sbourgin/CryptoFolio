package tasks;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractScheduledService;
import data.access.CoinDAO;
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
     * The market api manager.
     */
    private MarketApiManager marketApiManager;

    /**
     * Constructor.
     * @param marketApiManager The market API manager.
     */
    public FetchCoinTask(MarketApiManager marketApiManager){

        // Validate preconditions
        Preconditions.checkNotNull(marketApiManager);

        // Set member
        this.marketApiManager = marketApiManager;
    }

    /**
     * Executes the task.
     * Updates or creates all coins in the database.
     * If a coin already exists, then its properties are refreshed.
     * @remark: If any invocation of this method throws an exception, the service will transition to the FAILED state and this method will no
     * longer be called.
     */
    @Override
    protected void runOneIteration()  throws Exception {
        logger.info("Start executing FetchCoinTask");

        // Get the list of coins
        Map<String, Coin> coinMap = this.marketApiManager.getCoinDictionary();

        // Create the coin DAO
        CoinDAO coinDAO = new CoinDAO();

        // For each coin fetched from the API create or update the coin in the database
        for (Coin coin : coinMap.values()) {

            // Check if the coin exists in the database
            data.model.Coin coinFromDB = coinDAO.findOneByProperty("externalId", coin.id);

            // If the coin doesn't exist then create it
            if (coinFromDB == null) {
                coinFromDB =  new data.model.Coin(coin);
                coinDAO.create(coinFromDB);
            }
            // If the coin already exists then updates its property (coin can be renamed for instance)
            else {
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

    /**
     * Runs the tasks every day.
     * @return The task's schedule.
     */
    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.DAYS);
    }

    /**
     * Shut down the task due to an interruption.
     * @throws Exception
     */
    protected void shutDown() throws Exception {
        logger.error("FetchCoinTask has been requested to shutdown");
    }
}