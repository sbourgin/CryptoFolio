package data.access;

import data.model.Coin;

/**
 * The coin's DAO.
 */
public class CoinDAO extends AHibernateDAO<Coin> {

    /**
     * Constructor.
     */
    public CoinDAO() {
        super(Coin.class);
    }
}