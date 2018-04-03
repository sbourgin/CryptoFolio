package integration;

import cryptocompare.CryptoCompareApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests the MarketApiManager with the CryptoCompare implementation.
 */
public class CryptoCompareMarketApiManagerTest {

    /**
     * The market api manager.
     */
    private MarketApiManager marketApiManager;

    /**
     * Method executed before all the tests.
     */
    @Before
    public void beforeTest() {
        this.marketApiManager = new MarketApiManager(new CryptoCompareApiHelper());
    }

    /**
     * Tests that we can retrieve all the coins.
     */
    @Test
    public void getCoinDictionary() {

        // Act
        Map<String, Coin> allCoinsMap = this.marketApiManager.getCoinDictionary();

        // Assert
        assertTrue(allCoinsMap.size() > 100 ); // Make sure that there is at least 100 coins (should be thousands)
        assertTrue(allCoinsMap.containsKey("BTC")); // Make sure that Bitcoin is in the list
        assertTrue(allCoinsMap.containsKey("ADA")); // Make sure that Cardano is in the list
        assertTrue(allCoinsMap.containsKey("ETH")); // Make sure that Ethereum is in the list
    }

    /**
     * Tests that we can retrieve the current price of 3 known coins.
     */
    @Test
    public void getCoinsCurrentValue() {

        // Arrange
        List<String> coinShortNameList = new ArrayList<>();
        coinShortNameList.add("ETH");
        coinShortNameList.add("BTC");
        coinShortNameList.add("ADA");

        // Act
        Map<String, BigDecimal> coinsCurrentValueMap = this.marketApiManager.getCoinsCurrentValue(coinShortNameList);

        // Assert
        assertNotNull(coinsCurrentValueMap);
        assertEquals(coinShortNameList.size(), coinsCurrentValueMap.size());
        assertTrue(coinsCurrentValueMap.keySet().containsAll(coinShortNameList));
        assertEquals(-1, BigDecimal.valueOf(5000).compareTo(coinsCurrentValueMap.get("BTC")));
        assertEquals(-1, BigDecimal.valueOf(200).compareTo(coinsCurrentValueMap.get("ETH")));
        assertEquals(-1, BigDecimal.valueOf(0.10).compareTo(coinsCurrentValueMap.get("ADA")));
    }

    /**
     * Tests that we retrieve the bitcoin price back in January 2014.
     */
    @Test
    public void getCoinHistoricalValue() {

        // Arrange
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2014, Month.JANUARY, 1, 0, 0,0), ZoneId.of("UTC"));

        // Act
        BigDecimal btcHistoricalValue = marketApiManager.getCoinHistoricalValue("BTC", zonedDateTime);

        // Assert
        assertNotNull(btcHistoricalValue);
        assertEquals(BigDecimal.valueOf(815.94), btcHistoricalValue);
    }
}