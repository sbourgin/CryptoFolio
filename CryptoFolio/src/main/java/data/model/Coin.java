package data.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the coin's in the database.
 */
@Entity
public class Coin implements Serializable {

    private static final long serialVersionUID = -2009141494713526598L;

    /**
     * Database identifier of the record.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private int id;

    /**
     * The Id of the coin in an external system.
     */
    @Basic
    @Column(name = "externalId", nullable = true)
    public Integer getExternalId() {
        return externalId;
    }
    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }
    private Integer externalId;

    /**
     * The URL for the coin.
     */
    @Basic
    @Column(name = "url", nullable = true, length = 300)
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    private String url;

    /**
     * The URL for the coin's image.
     */
    @Basic
    @Column(name = "imageUrl", nullable = true, length = 300)
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    private String imageUrl;

    /**
     * The coin's short name.
     */
    @Basic
    @Column(name = "shortName", nullable = true, length = 50)
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    private String shortName;

    /**
     * The coin's name.
     */
    @Basic
    @Column(name = "coinName", nullable = true, length = 100)
    public String getCoinName() {
        return coinName;
    }
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
    private String coinName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return id == coin.id &&
                Objects.equal(externalId, coin.externalId) &&
                Objects.equal(url, coin.url) &&
                Objects.equal(imageUrl, coin.imageUrl) &&
                Objects.equal(shortName, coin.shortName) &&
                Objects.equal(coinName, coin.coinName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, externalId, url, imageUrl, shortName, coinName);
    }
}