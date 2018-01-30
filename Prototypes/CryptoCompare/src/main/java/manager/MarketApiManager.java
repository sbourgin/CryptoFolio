package manager;

import com.google.common.base.Preconditions;
import model.Coin;

import java.util.Map;

/**
 * Created by sylvain on 1/15/18.
 */
public class MarketApiManager {

    private IExchangeApiHelper exchangeApiHelper;

    public MarketApiManager(IExchangeApiHelper exchangeApiHelper) {

        // Check precondition
        Preconditions.checkNotNull(exchangeApiHelper);

        // Set members
        this.exchangeApiHelper = exchangeApiHelper;
    }

    /**
     * Gets the list of coins.
     * The key is the coin's short name.
     */
    public Map<String, Coin> getCoinDictionary() {

        // Delegate the call to the exchange API helper
        return this.exchangeApiHelper.getAllCoins();
    }

}