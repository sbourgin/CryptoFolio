package data.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a coin's price in the database.
 */
@Entity
@Table(name = "CoinPrice")
public class CoinPrice implements Serializable {

    private static final long serialVersionUID = -1092133205944542753L;

    /**
     * Default constructor.
     */
    public CoinPrice() { }

    /**
     * Database identifier of the record.
     */
    @Id
    @Column(name = "coinprice_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private int id;

    /**
     * The coin associated with this price.
     * @return The coin entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coin_id")
    public Coin getCoin() {
        return this.coin;
    }
    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    private Coin coin;

    /**
     * The price of the coin (in USD).
     */
    @Basic
    @Column(name = "price", nullable = true, precision = 5)
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    private BigDecimal price;

    /**
     * The date when the coin was valued at the specified price.
     */
    @Basic
    @Column(name = "date", nullable = true)
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    private Timestamp date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinPrice coinPrice = (CoinPrice) o;
        return id == coinPrice.id &&
                Objects.equal(price, coinPrice.price) &&
                Objects.equal(date, coinPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, price, date);
    }
}
