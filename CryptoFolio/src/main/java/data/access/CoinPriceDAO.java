package data.access;

import data.model.CoinPrice;

/**
 * The coin price's DAO.
 */
public class CoinPriceDAO extends AHibernateDAO<CoinPrice> {

    /**
     * Constructor.
     */
    public CoinPriceDAO() {
        super(CoinPrice.class);
    }
}