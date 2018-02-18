package manager;

import Core.ApplicationConfiguration;
import com.google.common.base.Preconditions;
import model.Coin;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by sylvain on 1/15/18.
 */
public class MarketApiManager {
    /**
     * The exchange API helper.
     */
    private IExchangeApiHelper exchangeApiHelper;

    /**
     * The retry policy for the API calls.
     */
    private RetryPolicy retryPolicy;

    /**
     * Constructor.
     * @param exchangeApiHelper
     */
    public MarketApiManager(IExchangeApiHelper exchangeApiHelper) {
        // Check precondition
        Preconditions.checkNotNull(exchangeApiHelper);

        // Set members
        this.exchangeApiHelper = exchangeApiHelper;
        this.retryPolicy = new RetryPolicy()
                .retryOn(failure -> failure instanceof IOException)
                .withBackoff(5, 90, TimeUnit.SECONDS)
                .withMaxRetries(30);
    }

    /**
     * Gets the list of coins.
     * The key is the coin's short name.
     */
    public Map<String, Coin> getCoinDictionary() {
        // Delegate the call to the exchange API helper
        // Wrap it around a retry manager
        return Failsafe.with(this.retryPolicy).get(new Callable<Map<String, Coin>>() {
            public Map<String, Coin> call() {
                return exchangeApiHelper.getAllCoins();
            }
        });
    }

    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList) {

        // Delegate the call to the exchange API helper
        // Wrap it around a retry manager
        return Failsafe.with(this.retryPolicy).get(new Callable<Map<String, BigDecimal>>() {
            public Map<String, BigDecimal> call() {
                return exchangeApiHelper.getCoinsCurrentValue(coinShortNameList, ApplicationConfiguration.DEFAULT_CURRENCY);
            }
        });
    }

    public Map<String, BigDecimal> getCoinsHistoricalValue(List<String> coinShortNameList, Timestamp timestamp) {
        // Delegate the call to the exchange API helper
        // Wrap it around a retry manager
        return Failsafe.with(this.retryPolicy).get(new Callable<Map<String, BigDecimal>>() {
            public Map<String, BigDecimal> call() {
                return exchangeApiHelper.getCoinsHistoricalValue(coinShortNameList, ApplicationConfiguration.DEFAULT_CURRENCY, timestamp);
            }
        });
    }
}