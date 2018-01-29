package model;

import cryptocompare.model.CryptoCompareCoin;

/**
 * Created by sylvain on 1/28/18.
 */
public class Coin {

    /**
     * The Id of the coin.
     */
    public int id;

    /**
     * The URL for the coin.
     */
    public String url;

    /**
     * The URL for the coin's image.
     */
    public String imageUrl;

    /**
     * The coin's short name.
     */
    public String shortName;

    /**
     * The coin's name.
     */
    public String coinName;

    /**
     * Creates a coin using a CryptoCompareCoin.
     * @param cryptoCompareCoin
     */
    public Coin (CryptoCompareCoin cryptoCompareCoin) {
        // Set members
        this.id = cryptoCompareCoin.id;
        this.coinName = cryptoCompareCoin.coinName;
        this.imageUrl = cryptoCompareCoin.imageUrl;
        this.shortName = cryptoCompareCoin.shortName;
        this.url = cryptoCompareCoin.url;
    }
}