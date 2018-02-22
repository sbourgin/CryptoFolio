package cryptocompare.model;

import com.google.gson.annotations.SerializedName;

/**
 * The coin's model.
 */
public class CryptoCompareCoin {

    /**
     * The Id of the coin.
     */
    @SerializedName("Id")
    public int id;

    /**
     * The URL for the coin.
     * @Example /coins/ltc/overview
     */
    @SerializedName("Url")
    public String url;

    /**
     * The URL for the coin's image.
     * @Example: /media/19782/ltc.png
     */
    @SerializedName("ImageUrl")
    public String imageUrl;

    /**
     * The coin's short name.
     * @Example: LTC for Litecoin.
     */
    @SerializedName("Name")
    public String shortName;

    /**
     * The coin's name.
     * @Example: Litecoin.
     */
    @SerializedName("CoinName")
    public String coinName;
}