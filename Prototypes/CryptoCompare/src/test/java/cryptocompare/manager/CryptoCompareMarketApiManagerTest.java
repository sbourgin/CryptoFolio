package cryptocompare.manager;

import cryptocompare.CryptoCompareApiHelper;
import manager.IExchangeApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by sylvain on 2/25/18.
 */
public class CryptoCompareMarketApiManagerTest {

    private MarketApiManager marketApiManager;

    @Before
    public void beforeEachTest() {
        this.marketApiManager = new MarketApiManager(new CryptoCompareApiHelper());
    }

    @Test
    public void GetCoinDictionary() {

        // Act
        Map<String, Coin> allCoinsMap = this.marketApiManager.getCoinDictionary();

        // Assert
        assertTrue(allCoinsMap.size() > 100 );
        assertTrue(allCoinsMap.containsKey("BTC"));
        assertTrue(allCoinsMap.containsKey("ADA"));
        assertTrue(allCoinsMap.containsKey("ETH"));
    }



}
