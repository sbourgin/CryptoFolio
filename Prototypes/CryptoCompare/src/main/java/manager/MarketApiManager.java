package manager;

import cryptocompare.model.CryptoCompareCoin;
import cryptocompare.reponse.GetCoinResponse;
import model.Coin;

import java.util.Map;

/**
 * Created by sylvain on 1/15/18.
 */
public class MarketApiManager {

    private IExchangeApiHelper exchangeApiHelper;

    public MarketApiManager(IExchangeApiHelper exchangeApiHelper){
        this.exchangeApiHelper = exchangeApiHelper;
    }

    /**
     * Gets the list of coins.
     * The key is the coin's short name.
     */
    public Map<String, Coin> getCoinDictionary() {

        return this.exchangeApiHelper.getAllCoins();
    }

}
