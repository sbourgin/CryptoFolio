import manager.CryptoCompareApiHelper;
import manager.MarketApiManager;
import model.Coin;

import java.util.Map;

/**
 * Created by sylvain on 12/28/17.
 */
public class Hello {

    public static void main(String[] args) {

        System.out.println("Hello world");

        // Try get the list of coins
        MarketApiManager marketApiManager = new MarketApiManager(new CryptoCompareApiHelper());

        Map<String, Coin> coinMap = marketApiManager.GetCoinDictionary();

        System.out.print("done");

    }

}
