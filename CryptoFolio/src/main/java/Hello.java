import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import core.ApplicationConfiguration;
import data.access.CoinDAO;
import market.cryptocompare.CryptoCompareApiHelper;
import market.manager.LocalExchangeApiHelper;
import market.manager.MarketApiManager;
import market.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.FetchCoinTask;
import tasks.FetchCurrentCoinsPriceTask;
import tasks.FetchHistoricalCoinPriceTask;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        CoinDAO coinDAO = new CoinDAO();

        // Refresh the coins in the database
        FetchCoinTask fetchCoinTask = new FetchCoinTask(marketApiManager);
        fetchCoinTask.startAsync();
        try {
            fetchCoinTask.wait();
        } catch (InterruptedException e) {
            logger.error("Coins cannot be retrieved");
        }

        // Start recurring tasks
        List<data.model.Coin> coinList = new ArrayList<data.model.Coin>();
        coinList.add(coinDAO.findByShortName("ETH"));
        coinList.add(coinDAO.findByShortName("BTC"));
        coinList.add(coinDAO.findByShortName("ADA"));
        coinList.add(coinDAO.findByShortName("XRP"));
        List<Service> serviceList = new ArrayList<>();
        serviceList.add(new FetchCurrentCoinsPriceTask(marketApiManager, coinList));
        ServiceManager serviceManager = new ServiceManager(serviceList);
        serviceManager.startAsync();
        try {
            serviceManager.wait();
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage());
        }

        // Log application end
        logger.info("Application stops");
    }
}