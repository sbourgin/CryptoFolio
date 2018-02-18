package manager;

import model.Coin;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by sylvain on 1/27/18.
 */
public interface IExchangeApiHelper {

    /***
     * Gets a map of all the existing coins
     * @return A map with all the existing coins where the key is the coin's short name.
     */
    public Map<String, Coin> getAllCoins();

    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList, String currencyShortName);

    public Map<String, BigDecimal> getCoinsHistoricalValue(List<String> coinShortNameList, String currencyShortName, Timestamp timestamp);
}
