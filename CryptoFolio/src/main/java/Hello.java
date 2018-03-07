import core.ApplicationConfiguration;
import data.ManageEmployee;
import data.access.CoinDAO;
import market.cryptocompare.CryptoCompareApiHelper;
import market.manager.LocalExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Console;
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

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(10000);

        data.model.Coin coin = new data.model.Coin();
        coin.setShortName("ABC" + randomInt);
        coin.setCoinName("Stealite" + randomInt);

        CoinDAO coinDAO = new CoinDAO();
        coinDAO.save(coin);


        List<data.model.Coin> allCoins = coinDAO.findAll();


        coin.setShortName("DEF" + randomInt);

        coinDAO.update(coin);

        coinDAO.delete(coin);

  //      coinDAO.deleteById(coin.getId());

        data.model.Coin coin10 = coinDAO.findById(10);


        System.out.println("Choose which action to do: ");
        System.out.println("{0} Fetch the coin price periodically");

        System.out.println("{1} Refresh the coin stored in the database");
        System.out.println("{2} Save in the database the historical price for a given coin");


// TODO        http://www.baeldung.com/simplifying-the-data-access-layer-with-spring-and-java-generics
// or (easier?) http://www.baeldung.com/persistence-layer-with-spring-and-hibernate

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

            case 1:
                refreshCoinsInDatabase();
            case 2:
                System.out.println("Great");
        }

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


    // TODO move me elsewhere - different project?
    private static void refreshCoinsInDatabase () {

        // Create the market api manager
        MarketApiManager marketApiManager = new MarketApiManager((ApplicationConfiguration.IS_DEMO_MODE) ? new LocalExchangeApiHelper() : new CryptoCompareApiHelper());

        // Get the list of coins
        Map<String, Coin> coinMap = marketApiManager.getCoinDictionary();

        for (Coin coin : coinMap.values()) {

            // Check if the coin exists in the database

            // If yes -> Update properties

            // If no -> Create it

        }

        // Count how many coins are stored in the database and print it
    }



}
