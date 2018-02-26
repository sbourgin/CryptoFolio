package manager;

import core.ApplicationConfiguration;
import com.google.common.base.Preconditions;
import model.Coin;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * The market API manager which provides high level implementation of an exchange API.
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

    /**
     * Gets the current price value of a list of coin.
     * @param coinShortNameList The list of coins to evaluate
     * @return A map of coin's short name and its price.
     */
    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList) {

        // Delegate the call to the exchange API helper
        // Wrap it around a retry manager
        return Failsafe.with(this.retryPolicy).get(new Callable<Map<String, BigDecimal>>() {
            public Map<String, BigDecimal> call() {
                return exchangeApiHelper.getCoinsCurrentValue(coinShortNameList, ApplicationConfiguration.DEFAULT_CURRENCY);
            }
        });
    }

    /**
     * Gets the price of a coin in a specified currency at a specified time.
     * @param coinShortName The coin's short name to lookup
     * @param zonedDateTime The specified time for the lookup
     * @return The price of the coin
     */
    public BigDecimal getCoinHistoricalValue(String coinShortName, ZonedDateTime zonedDateTime) {

        // Delegate the call to the exchange API helper
        // Wrap it around a retry manager
        return Failsafe.with(this.retryPolicy).get(new Callable<BigDecimal>() {
            public BigDecimal call() {
                return exchangeApiHelper.getCoinHistoricalValue(coinShortName, ApplicationConfiguration.DEFAULT_CURRENCY, zonedDateTime);
            }
        });
    }
}