package market.cryptocompare.reponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sylvain on 1/15/18.
 */
public class CryptoCompareBaseResponse {

    /**
     * The type of response (success or failure).
     */
    @SerializedName("Response")
    public String response;

    /**
     * The message from the API.
     */
    @SerializedName("Message")
    public String message;
}