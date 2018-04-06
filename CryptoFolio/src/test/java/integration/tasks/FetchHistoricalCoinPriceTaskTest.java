package integration.tasks;

import data.access.CoinDAO;
import data.model.Coin;
import data.model.CoinPrice;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import market.cryptocompare.CryptoCompareApiHelper;
import market.manager.MarketApiManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tasks.FetchHistoricalCoinPriceTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertTrue;

/**
 * Tests the FetchHistoricalCoinPriceTask implementation.
 */
@RunWith(JUnitParamsRunner.class)
public class FetchHistoricalCoinPriceTaskTest {

    @Before
    public void setUp() throws Exception {
        this.cleanUpDatabase();
    }

    @After
    public void tearDown() throws Exception {
        this.cleanUpDatabase();
    }

    /**
     * Cleans up the database.
     */
    private void cleanUpDatabase() {

        // Delete the coins created during the test. Thanks to the cascade effect, the coinPrices will be deleted as well
        CoinDAO coinDAO = new CoinDAO();
        Coin coinInDatabase = coinDAO.findOneByProperty("externalId", this.getInitializedCoin("test").getExternalId());
        if (null != coinInDatabase) {
            coinDAO.delete(coinInDatabase);
        }

        // Due to the unique index on the coinShortName, we need to delete those well known coins for the test
        List<String> coinShortNameList = new ArrayList<>();
        coinShortNameList.add("ADA");
        coinShortNameList.add("BTC");
        coinShortNameList.add("ETH");
        coinShortNameList.add("XRP");
        for(String coinShortName : coinShortNameList) {
            Coin coin = coinDAO.findByShortName(coinShortName);
            if (null != coin) {
                coinDAO.delete(coin);
            }
        }
    }

    /**
     * Tests the findOriginZonedDateTime method. This method finds the start date for a coin to be traded on exchanges.
     * @param coinShortName The name of the coin to process
     * @param year The expected year's result
     * @param month The expected month's result
     * @param day The expected day's result
     */
    @Test
    @Parameters({"ADA, 2017, 10, 01",
                 "BTC, 2010, 07, 18",
                 "ETH, 2015, 08, 7",
                 "XRP, 2015, 01, 29"
    })
    public void findOriginZonedDateTime(String coinShortName, int year, int month, int day) {

        // Arrange
        ZonedDateTime expectedResult = ZonedDateTime.of(year, month, day, 0,0,0,0, ZoneId.of("UTC"));
        MarketApiManager marketApiManager = new MarketApiManager(new CryptoCompareApiHelper());
        CoinDAO coinDAO = new CoinDAO();
        Coin coin = this.getInitializedCoin(coinShortName);
        coinDAO.create(coin);

        // Act
        ZonedDateTime result = null;
        FetchHistoricalCoinPriceTask fetchHistoricalCoinPriceTask = new FetchHistoricalCoinPriceTask(marketApiManager, coin, true);
        try {
            Method myMethod = fetchHistoricalCoinPriceTask.getClass().getDeclaredMethod("findCoinTradingStartDate", new Class[0]);
            myMethod.setAccessible(true);
            Object executionResult = myMethod.invoke(fetchHistoricalCoinPriceTask, null);
            result = (ZonedDateTime) executionResult;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            assertTrue("The findCoinTradingStartDate method couldn't be called or an exception occurred during the execution", false);
        }

        // Assert the result is within an acceptable time range
        assertTrue(DAYS.between(expectedResult, result) < 3);
    }

    /**
     * Tests the run method.
     */
    @Test
    public void run() {

        // Arrange
        MarketApiManager marketApiManager = new MarketApiManager(new CryptoCompareApiHelper());
        CoinDAO coinDAO = new CoinDAO();
        Coin coin = this.getInitializedCoin("ADA");
        coinDAO.create(coin);

        // Act
        FetchHistoricalCoinPriceTask fetchHistoricalCoinPriceTask = new FetchHistoricalCoinPriceTask(marketApiManager, coin, true);
        try {
            Method myMethod = fetchHistoricalCoinPriceTask.getClass().getDeclaredMethod("run", new Class[0]);
            myMethod.setAccessible(true);
            myMethod.invoke(fetchHistoricalCoinPriceTask, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            assertTrue("The run method couldn't be called or an exception occurred during the execution" , false);
        }

        // Assert
        Set<CoinPrice> coinPriceSet = coinDAO.findByShortName("ADA").getCoinPrices(); // force a refresh from the database
        assertTrue(coinPriceSet.size() > 100); // Make sure at least one hundred coin prices where created
    }

    /**
     * Gets an initialized coin.
     * @return an initialized coin
     */
    private Coin getInitializedCoin(String coinShortName)
    {
        Coin coin = new Coin();
        coin.setShortName(coinShortName);
        coin.setExternalId(123);
        coin.setCoinName("Test coin");
        coin.setUrl("www.test-coin.com");
        return coin;
    }
}