package market.manager;

import market.model.Coin;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface which defines the methods to implement for a given crypto exchange.
 */
public interface IExchangeApiHelper {

    /***
     * Gets a map of all the existing coins
     * @return A map with all the existing coins where the key is the coin's short name.
     */
    public Map<String, Coin> getAllCoins();

    /**
     * Gets the current price value of a list of coin.
     * @param coinShortNameList The list of coins to evaluate
     * @param currencyShortName The currency for the price request.
     * @return A map of coin's short name and its price.
     */
    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList, String currencyShortName);

    /**
     * Gets the price of a coin in a specified currency at a specified time.
     * @param coinShortName The coin's short name to lookup
     * @param currencyShortName The currency to use
     * @param zonedDateTime The specified time for the lookup
     * @return The price of the coin
     */
    public BigDecimal getCoinHistoricalValue(String coinShortName, String currencyShortName, ZonedDateTime zonedDateTime);
}
