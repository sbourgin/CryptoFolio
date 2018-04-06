package market.cryptocompare.manager;

import market.manager.IExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests the marketApiManager implementation with a mock for the network calls.
 */
public class MarketApiManagerTest {

    /**
     * The market api manager.
     */
    private IExchangeApiHelper exchangeApiHelper;

    /**
     * Method executed before all the tests.
     */
    @Before
    public void beforeEachTest() {
        this.exchangeApiHelper = Mockito.mock(IExchangeApiHelper.class);
    }

    /**
     * Tests that we can retrieve all the coins.
     */
    @Test
    public void GetCoinDictionary() {

        // Arrange
        Map<String, Coin> allCoinsMap = this.getHardcodedCoinMap();
        when(this.exchangeApiHelper.getAllCoins()).thenReturn(allCoinsMap);
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        Map<String, Coin> actualValue = marketApiManager.getCoinDictionary();

        // Assert
        assertEquals(allCoinsMap.size(), actualValue.size());
        assertTrue(allCoinsMap.equals(actualValue));
    }

    /**
     * Tests that we can retrieve all the coins and the retry strategy works.
     */
    @Test
    public void GetCoinDictionaryWithFirstChanceException() {

        // Arrange
        Map<String, Coin> allCoinsMap = this.getHardcodedCoinMap();
        when(this.exchangeApiHelper.getAllCoins()).thenAnswer( invocation -> { throw new SocketTimeoutException(); }).thenReturn(allCoinsMap);
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        Map<String, Coin> actualValue = marketApiManager.getCoinDictionary();

        // Assert
        assertEquals(allCoinsMap.size(), actualValue.size());
        assertTrue(allCoinsMap.equals(actualValue));
        verify(this.exchangeApiHelper, times(2)).getAllCoins();
    }

    /**
     * Tests that we can retrieve the current price of coins.
     */
    @Test
    public void getCoinsCurrentValue() {

        // Arrange
        Map<String, BigDecimal> expectedCurrentValueMap = new HashMap<>();
        expectedCurrentValueMap.put("ABC", BigDecimal.valueOf(0.567));
        expectedCurrentValueMap.put("DEF", BigDecimal.valueOf(34567));

        // The list of coin to query
        List<String> coinShortNameList = new ArrayList<>();
        coinShortNameList.add("ABC");
        coinShortNameList.add("DEF");

        // Set up the market api manager with the mock
        when(this.exchangeApiHelper.getCoinsCurrentValue(anyList(), any(String.class))).thenReturn(expectedCurrentValueMap);
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        Map<String, BigDecimal> coinsCurrentValueMap = marketApiManager.getCoinsCurrentValue(coinShortNameList);

        // Assert
        assertNotNull(coinsCurrentValueMap);
        assertEquals(expectedCurrentValueMap.size(), coinsCurrentValueMap.size());
        assertTrue(coinsCurrentValueMap.keySet().containsAll(coinShortNameList));
        assertEquals(expectedCurrentValueMap.get("ABC"), coinsCurrentValueMap.get("ABC"));
        assertEquals(expectedCurrentValueMap.get("DEF"), coinsCurrentValueMap.get("DEF"));
    }

    /**
     * Tests that we can retrieve the historical price of a coin.
     */
    @Test
    public void getCoinsHistoricalValue() {

        // Arrange
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2014, Month.JANUARY, 1, 0, 0,0), ZoneId.of("UTC"));
        when(this.exchangeApiHelper.getCoinHistoricalValue(anyString(), anyString(), any(ZonedDateTime.class))).thenReturn(BigDecimal.valueOf(0.567));
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        BigDecimal coinCurrentValue = marketApiManager.getCoinHistoricalValue("ABC", zonedDateTime);

        // Assert
        assertNotNull(coinCurrentValue);
        assertEquals(BigDecimal.valueOf(0.567), coinCurrentValue);
    }

    /**
     * Gets a map of two hardcoded coins.
     * @return a map of two hardcoded coins
     */
    private Map<String, Coin> getHardcodedCoinMap()
    {
        Coin coin1 = new Coin();
        coin1.shortName = "ABC";
        coin1.id = 123;
        coin1.coinName = "ABC coin";
        Coin coin2 = new Coin();
        coin2.shortName = "DEF";
        coin2.id = 456;
        coin2.coinName = "DEF coin";
        Map<String, Coin> coinsMap = new HashMap<String, Coin>();
        coinsMap.put(coin1.shortName, coin1);
        coinsMap.put(coin2.shortName, coin2);
        return coinsMap;
    }
}