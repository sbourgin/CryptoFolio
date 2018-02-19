import Core.ApplicationConfiguration;
import cryptocompare.CryptoCompareApiHelper;
import manager.LocalExchangeApiHelper;
import manager.MarketApiManager;
import model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
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

    public static void main(String[] args) {

        // Log application start
        logger.info("Application starts");

        // Log whether we are in test mode
        logger.info("Application mode: {}", (ApplicationConfiguration.IS_TEST_MODE) ? "Test" : "Production");

        // Try get the list of coins
        MarketApiManager marketApiManager = new MarketApiManager((ApplicationConfiguration.IS_TEST_MODE) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());

        // Get all the coins
   //     Map<String, Coin> coinMap = marketApiManager.getCoinDictionary();

        // Get the current prices
        List<String> coinShortNameList = new ArrayList<String>();
        coinShortNameList.add("ETH");
        coinShortNameList.add("BTC");
        coinShortNameList.add("ADA");
        Map<String, BigDecimal> coinsCurrentValueMap = marketApiManager.getCoinsCurrentValue(coinShortNameList);

        // Get historical prices
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0,0), ZoneId.of("UTC"));
        BigDecimal coinsHistoricalValue = marketApiManager.getCoinHistoricalValue("BTC", zonedDateTime);

        // Log application end
        logger.info("Application stops");
    }
}
