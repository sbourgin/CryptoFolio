package tasks;

import data.access.CoinDAO;
import data.access.CoinPriceDAO;
import market.manager.IExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the FetchCoinTask implementation.
 */
public class FetchCoinTaskTest {

    @Before
    public void setUp() throws Exception {

        // Delete the coins that happened to have the same external ID as the ones we will be using during the test
        CoinDAO coinDAO = new CoinDAO();
        CoinPriceDAO coinPriceDAO = new CoinPriceDAO();
        for (Coin coin : this.getHardcodedCoinMap().values()) {
            data.model.Coin coinInDatabase = coinDAO.findOneByProperty("externalId", coin.id);
            if (null != coinInDatabase) {
                coinDAO.delete(coinInDatabase);
            }
        }
    }

    @After
    public void tearDown() throws Exception {

        // Delete the coins created during the tests
        CoinDAO coinDAO = new CoinDAO();
        for (Coin coin : this.getHardcodedCoinMap().values()) {
            data.model.Coin coinInDatabase = coinDAO.findOneByProperty("externalId", coin.id);
            if (null != coinInDatabase) {
                coinDAO.delete(coinInDatabase);
            }
        }
    }

    /**
     * Tests the runOneIteration method.
     */
    @Test
    public void runOneIteration() {

        // Arrange
        IExchangeApiHelper exchangeApiHelper = Mockito.mock(IExchangeApiHelper.class);
        Map<String, Coin> allCoinsMap = this.getHardcodedCoinMap();
        when(exchangeApiHelper.getAllCoins()).thenReturn(allCoinsMap);
        MarketApiManager marketApiManager = new MarketApiManager(exchangeApiHelper);

        // Pre-create the ABC coin so we can make sure its properties get updated by the task
        CoinDAO coinDAO = new CoinDAO();
        data.model.Coin abcCoin = new data.model.Coin();
        abcCoin.setShortName("ABCCT");
        abcCoin.setExternalId(123);
        abcCoin.setImageUrl("www.dummyurl.com");
        abcCoin.setCoinName("AAAAA BCT");
        coinDAO.create(abcCoin);

        // Act
        FetchCoinTask fetchCoinTask = new FetchCoinTask(marketApiManager);
        try {
            Method myMethod = fetchCoinTask.getClass().getDeclaredMethod("runOneIteration", new Class[0]);
            myMethod.setAccessible(true);
            myMethod.invoke(fetchCoinTask, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            assertTrue("The runOneIteration method couldn't be called", false);
        }

        // Assert
        // Make sure the ABC coin still exist in the database and its properties where updated
        abcCoin = coinDAO.findById(abcCoin.getId());
        assertNotNull(abcCoin);
        assertEquals("ABCT", abcCoin.getShortName());
        assertEquals("ABCT coin", abcCoin.getCoinName());
        assertEquals("www.greatUrl.com", abcCoin.getUrl());
        assertEquals(Integer.valueOf("123"), abcCoin.getExternalId());

        // Make sure the DEFT coin has been created
        data.model.Coin deftCoin = coinDAO.findOneByProperty("externalId", 456);
        assertNotNull(deftCoin);
        assertEquals("DEFT", deftCoin.getShortName());
        assertEquals("DEFT coin", deftCoin.getCoinName());
        assertEquals("www.greatgreatUrl.com", deftCoin.getUrl());
    }

    /**
     * Gets a map of two hardcoded coins.
     * @return a map of two hardcoded coins
     */
    private Map<String, Coin> getHardcodedCoinMap()
    {
        Coin coin1 = new Coin();
        coin1.shortName = "ABCT";
        coin1.id = 123;
        coin1.coinName = "ABCT coin";
        coin1.url = "www.greatUrl.com";
        Coin coin2 = new Coin();
        coin2.shortName = "DEFT";
        coin2.id = 456;
        coin2.coinName = "DEFT coin";
        coin2.url = "www.greatgreatUrl.com";
        Map<String, Coin> coinsMap = new HashMap<String, Coin>();
        coinsMap.put(coin1.shortName, coin1);
        coinsMap.put(coin2.shortName, coin2);
        return coinsMap;
    }
}