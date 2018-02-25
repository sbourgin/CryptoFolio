import core.ApplicationConfiguration;
import cryptocompare.CryptoCompareApiHelper;
import manager.LocalExchangeApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sylvain on 12/28/17.
 */
public class Hello {

    /**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(Hello.class);

    /**
     * Application entry point method.
     * @param args Arguments
     */
    public static void main(String[] args) {

        // Log application start
        logger.info("Application starts");

        // Log whether we are in demo mode
        logger.info("Application mode: {}", (ApplicationConfiguration.IS_DEMO_MODE) ? "DEMO" : "Production");

        // Create the market api manager
        MarketApiManager marketApiManager = new MarketApiManager((ApplicationConfiguration.IS_DEMO_MODE) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());

        // Get the list of coins
        Map<String, Coin> coinMap = marketApiManager.getCoinDictionary();

        // Get the current prices
        List<String> coinShortNameList = new ArrayList<String>();
        coinShortNameList.add("ETH");
        coinShortNameList.add("BTC");
        coinShortNameList.add("ADA");
        Map<String, BigDecimal> coinsCurrentValueMap = marketApiManager.getCoinsCurrentValue(coinShortNameList);

        // Get historical prices
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0,0), ZoneId.of("UTC"));
        BigDecimal btcHistoricalValue = marketApiManager.getCoinHistoricalValue("BTC", zonedDateTime);

        // Print the price
        System.out.println(new StringBuilder().append("Bitcoin historical price: ").append(btcHistoricalValue).append(" at ").append(zonedDateTime.toString()).toString());

        // Log application end
        logger.info("Application stops");
    }
}
