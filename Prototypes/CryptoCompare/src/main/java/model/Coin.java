package model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
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
     * Default constructor.
     */
    public Coin () {}

    /**
     * Creates a coin using a CryptoCompareCoin.
     * @param cryptoCompareCoin
     */
    public Coin (CryptoCompareCoin cryptoCompareCoin) {

        // Check precondition
        Preconditions.checkNotNull(cryptoCompareCoin);

        // Set members
        this.id = cryptoCompareCoin.id;
        this.coinName = cryptoCompareCoin.coinName;
        this.imageUrl = cryptoCompareCoin.imageUrl;
        this.shortName = cryptoCompareCoin.shortName;
        this.url = cryptoCompareCoin.url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return id == coin.id &&
                Objects.equal(url, coin.url) &&
                Objects.equal(imageUrl, coin.imageUrl) &&
                Objects.equal(shortName, coin.shortName) &&
                Objects.equal(coinName, coin.coinName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, url, imageUrl, shortName, coinName);
    }
}