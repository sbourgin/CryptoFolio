import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import core.ApplicationConfiguration;
import data.access.CoinDAO;
import data.access.CoinPriceDAO;
import market.cryptocompare.CryptoCompareApiHelper;
import market.manager.LocalExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.FetchCoinTask;
import tasks.FetchHistoricalCoinPriceTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static void main(String[] args)  {

        // Log application start
        logger.info("Application starts");

        // Log whether we are in demo mode
        logger.info("Application mode: {}", (ApplicationConfiguration.IS_DEMO_MODE) ? "DEMO" : "Production");

        // Create the market api manager
        MarketApiManager marketApiManager = new MarketApiManager((ApplicationConfiguration.IS_DEMO_MODE) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());


        // TODO add is tracked to coin for current prices?

        CoinDAO coinDAO = new CoinDAO();

        // Start recurring tasks
        List<Service> serviceList = new ArrayList<Service>();
        serviceList.add(new FetchCoinTask(marketApiManager));

        serviceList.add(new FetchHistoricalCoinPriceTask(marketApiManager, coinDAO.findByShortName("BTC"), true));

        ServiceManager serviceManager = new ServiceManager(serviceList);
        serviceManager.startAsync();


      /*  System.out.println("Choose which action to do: ");

        System.out.println("{0} Fetch coin prices periodically");

        System.out.println("{1} Refresh the coin stored in the database");

        System.out.println("{2} Save in the database the historical price for a given coin");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int actionSelected;
        try{
            actionSelected  = Integer.parseInt(br.readLine());
        }catch(IOException ioe) {
            System.err.println("IO Exception!");
            return;
        }
        catch(NumberFormatException nfe) {
            System.err.println("Invalid Format!");
            return;
        }

        switch (actionSelected) {
            case 0:

                break;

            case 1:
                refreshCoinsInDatabase();
                break;
            case 2:
                System.out.println("Great");
                break;
        }


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


*/
    }
}