import manager.IExchangeApiHelper;
import manager.LocalExchangeApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sylvain on 2/3/18.
 */
public class MarketApiManagerTest {

    private IExchangeApiHelper exchangeApiHelper;

    @Before
    public void beforeEachTest() {
        this.exchangeApiHelper = Mockito.mock(IExchangeApiHelper.class);
    }

    @Test
    public void test() throws Exception {

        // Arrange
        Coin coin1 = new Coin();
        coin1.shortName = "ABC";
        coin1.id = 123;
        coin1.coinName = "ABC coin";
        Coin coin2 = new Coin();
        coin2.shortName = "DEF";
        coin2.id = 456;
        coin2.coinName = "DEF coin";
        Map<String, Coin> allCoinsMap = new HashMap<String, Coin>();
        allCoinsMap.put(coin1.shortName, coin1);
        allCoinsMap.put(coin2.shortName, coin2);
        when(this.exchangeApiHelper.getAllCoins()).thenReturn(allCoinsMap);
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        Map<String, Coin> actualValue = marketApiManager.getCoinDictionary();

        // Assert
        assertEquals(allCoinsMap.size(), actualValue.size());
        assertTrue(allCoinsMap.equals(actualValue));
    }

    @Test
    public void testWithFirstChanceException() throws Exception {

        // Arrange
        Coin coin1 = new Coin();
        coin1.shortName = "ABC";
        coin1.id = 123;
        coin1.coinName = "ABC coin";
        Coin coin2 = new Coin();
        coin2.shortName = "DEF";
        coin2.id = 456;
        coin2.coinName = "DEF coin";
        Map<String, Coin> allCoinsMap = new HashMap<String, Coin>();
        allCoinsMap.put(coin1.shortName, coin1);
        allCoinsMap.put(coin2.shortName, coin2);
        when(this.exchangeApiHelper.getAllCoins()).thenAnswer( invocation -> { throw new SocketTimeoutException(); }).thenReturn(allCoinsMap);
        MarketApiManager marketApiManager = new MarketApiManager(this.exchangeApiHelper);

        // Act
        Map<String, Coin> actualValue = marketApiManager.getCoinDictionary();

        // Assert
        assertEquals(allCoinsMap.size(), actualValue.size());
        assertTrue(allCoinsMap.equals(actualValue));
        verify(this.exchangeApiHelper, times(2)).getAllCoins();
    }
}
