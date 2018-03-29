package tasks;

import data.access.CoinDAO;
import market.manager.IExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * Tests the FetchCurrentCoinsPriceTask implementation.
 */
public class FetchCurrentCoinsPriceTaskTest {

    @Before
    public void setUp() throws Exception {

        // Delete the coins that happened to have the same external ID as the ones we will be using during the test
        CoinDAO coinDAO = new CoinDAO();
        for (Coin coin : this.getHardcodedCoinMap().values()) {
            data.model.Coin coinInDatabase = coinDAO.findOneByProperty("externalId", coin.id);
            if (null != coinInDatabase) {
                coinDAO.delete(coinInDatabase);
            }
        }
    }

    @After
    public void tearDown() throws Exception {

        // Delete the coins created during the test. Thanks to the cascade effect, the coinPrices will be deleted as well
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
        // Create the coins
        CoinDAO coinDAO = new CoinDAO();
        List<data.model.Coin> coinList = new ArrayList<>();
        for (Coin coin : this.getHardcodedCoinMap().values()) {
            data.model.Coin coinToCreate = new data.model.Coin(coin);
            coinDAO.create(coinToCreate);
            coinList.add(coinToCreate);
        }

        // Set up a mock for the exchange API to return expected prices
        IExchangeApiHelper exchangeApiHelper = Mockito.mock(IExchangeApiHelper.class);
        Map<String, BigDecimal> expectedCurrentValueMap = new HashMap<>();
        expectedCurrentValueMap.put("ABCT", BigDecimal.valueOf(0.567));
        expectedCurrentValueMap.put("DEFT", BigDecimal.valueOf(34567));
        when(exchangeApiHelper.getCoinsCurrentValue(anyList(), any(String.class))).thenReturn(expectedCurrentValueMap);
        MarketApiManager marketApiManager = new MarketApiManager(exchangeApiHelper);

        // Act
        FetchCurrentCoinsPriceTask fetchCurrentCoinsPriceTask = new FetchCurrentCoinsPriceTask(marketApiManager, coinList);
        try {
            Method myMethod = fetchCurrentCoinsPriceTask.getClass().getDeclaredMethod("runOneIteration", new Class[0]);
            myMethod.setAccessible(true);
            myMethod.invoke(fetchCurrentCoinsPriceTask, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            assertTrue("The runOneIteration method couldn't be called", false);
        }

        // Assert - make sure the task created the expected coinPrice entities
        data.model.Coin abctCoin = coinDAO.findByShortName("ABCT");
        assertNotNull(abctCoin);
        assertEquals(1, abctCoin.getCoinPrices().size());
        assertEquals(BigDecimal.valueOf(0.567).stripTrailingZeros(), abctCoin.getCoinPrices().iterator().next().getPrice().stripTrailingZeros());
        data.model.Coin deftCoin = coinDAO.findByShortName("DEFT");
        assertNotNull(deftCoin);
        assertEquals(1, deftCoin.getCoinPrices().size());
        assertEquals(BigDecimal.valueOf(34567).stripTrailingZeros(), deftCoin.getCoinPrices().iterator().next().getPrice().stripTrailingZeros());
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