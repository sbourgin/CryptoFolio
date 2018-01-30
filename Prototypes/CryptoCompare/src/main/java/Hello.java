import cryptocompare.CryptoCompareApiHelper;
import manager.LocalExchangeApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by sylvain on 12/28/17.
 */
public class Hello {

    private static Logger logger = LoggerFactory.getLogger(Hello.class);

    private static boolean isTestMode = true;

    public static void main(String[] args) {

        // Log application start
        logger.info("Application starts");

        // Log whether we are in test mode
        logger.info("Application mode: {}", (isTestMode) ? "Test" : "Production");

        // Try get the list of coins
        MarketApiManager marketApiManager = new MarketApiManager((isTestMode) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());

        // Get all the coins
        Map<String, Coin> coinMap = marketApiManager.getCoinDictionary();

        // Log application end
        logger.info("Application stops");
    }

}
