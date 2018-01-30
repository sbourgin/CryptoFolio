package manager;

import model.Coin;

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


}
