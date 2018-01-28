package response;

import com.google.gson.annotations.SerializedName;
import model.Coin;

import java.util.Map;

/**
 * Created by sylvain on 1/27/18.
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
    public Map<String, Coin> coinMap;

}
