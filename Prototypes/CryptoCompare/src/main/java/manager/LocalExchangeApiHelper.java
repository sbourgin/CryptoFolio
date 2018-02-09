package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cryptocompare.reponse.GetCoinResponse;
import model.Coin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sylvain on 1/28/18.
 */
public class LocalExchangeApiHelper implements IExchangeApiHelper {


    public Map<String, Coin> getAllCoins() {

        // Use an hardcoded list of coins
        String hardcodedListOfCoins = "{\"Response\":\"Success\",\"Message\":\"Coin list succesfully returned! This api is moving to https://min-api.cryptocompare.com/data/all/coinlist, please change the path.\",\"BaseImageUrl\":\"https://www.cryptocompare.com\",\"BaseLinkUrl\":\"https://www.cryptocompare.com\",\"DefaultWatchlist\":{\"CoinIs\":\"1182,7605,5038,24854,3807,3808,202330,5324,5031,20131\",\"Sponsored\":\"\"},\"Data\":{\"42\":{\"Id\":\"4321\",\"Url\":\"/coins/42/overview\",\"ImageUrl\":\"/media/12318415/42.png\",\"Name\":\"42\",\"Symbol\":\"42\",\"CoinName\":\"42 Coin\",\"FullName\":\"42 Coin (42)\",\"Algorithm\":\"Scrypt\",\"ProofType\":\"PoW/PoS\",\"FullyPremined\":\"0\",\"TotalCoinSupply\":\"42\",\"PreMinedValue\":\"N/A\",\"TotalCoinsFreeFloat\":\"N/A\",\"SortOrder\":\"34\",\"Sponsored\":false},\"365\":{\"Id\":\"33639\",\"Url\":\"/coins/365/overview\",\"ImageUrl\":\"/media/352070/365.png\",\"Name\":\"365\",\"Symbol\":\"365\",\"CoinName\":\"365Coin\",\"FullName\":\"365Coin (365)\",\"Algorithm\":\"X11\",\"ProofType\":\"PoW/PoS\",\"FullyPremined\":\"0\",\"TotalCoinSupply\":\"2300000000\",\"PreMinedValue\":\"N/A\",\"TotalCoinsFreeFloat\":\"N/A\",\"SortOrder\":\"916\",\"Sponsored\":false}}}";

        // Parse the list of coins
        Gson gson = new GsonBuilder().create();
        GetCoinResponse getCoinResponse = gson.fromJson(hardcodedListOfCoins, GetCoinResponse.class);

        // Convert the CryptoCompareCoin to Coin and return the expected map
        Map<String, Coin> coinMap = new HashMap<String, Coin>();
        getCoinResponse.coinMap.forEach((k,v) -> coinMap.put(k, new Coin(v)));
        return coinMap;
    }
}
