package market.manager;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import market.cryptocompare.reponse.GetCoinResponse;
import market.model.Coin;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The local implementation of the exchange interface.
 * @remark: This implementation doesn't make any network call and can be used for demo purposes.
 */
public class LocalExchangeApiHelper implements IExchangeApiHelper {

    /***
     * Gets a map of all the existing coins
     * @return A map with all the existing coins where the key is the coin's short name.
     */
    @Override
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

    /**
     * Gets the current price value of a list of coin.
     * @param coinShortNameList The list of coins to evaluate
     * @param currencyShortName The currency for the price request.
     * @return A map of coin's short name and its price.
     */
    @Override
    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList, String currencyShortName) {
        // Check preconditions
        Preconditions.checkNotNull(coinShortNameList);
        Preconditions.checkArgument(coinShortNameList.size() > 0);

        // Generate random prices for each coin
        Map<String, BigDecimal> coinPriceResult = new HashMap<String, BigDecimal>();
        for (String coinShortName : coinShortNameList) {
            coinPriceResult.put(coinShortName, BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 12000)));
        }
        return coinPriceResult;
    }

    /**
     * Gets the price of a coin in a specified currency at a specified time.
     * @param coinShortName The coin's short name to lookup
     * @param currencyShortName The currency to use
     * @param zonedDateTime The specified time for the lookup
     * @return The price of the coin
     */
    @Override
    public BigDecimal getCoinHistoricalValue(String coinShortName, String currencyShortName, ZonedDateTime zonedDateTime) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 12000));
    }
}
