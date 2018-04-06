package market.cryptocompare.reponse;

import com.google.gson.annotations.SerializedName;
import market.cryptocompare.model.CryptoCompareCoin;

import java.util.Map;

/**
 * The coin response as provided by the API.
 */
public class GetCoinResponse extends CryptoCompareBaseResponse {

    /**
     * The base image url for coins.
     */
    @SerializedName("BaseImageUrl")
    public String baseImageUrl;

    /**
     * The base link url for coins.
     */
    @SerializedName("BaseLinkUrl")
    public String baseLinkUrl;

    /**
     * The array of coins.
     * The key is the coin's short name.
     */
    @SerializedName("Data")
    public Map<String, CryptoCompareCoin> coinMap;
}