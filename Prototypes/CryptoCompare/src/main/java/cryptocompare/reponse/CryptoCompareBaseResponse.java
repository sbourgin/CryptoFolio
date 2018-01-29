package cryptocompare.reponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sylvain on 1/15/18.
 */
public class CryptoCompareBaseResponse {

    /**
     * The type of resonse (success or failure).
     */
    @SerializedName("Response")
    public String response;

    /**
     * The message for the cryptocompare.
     */
    @SerializedName("Message")
    public String message;
}