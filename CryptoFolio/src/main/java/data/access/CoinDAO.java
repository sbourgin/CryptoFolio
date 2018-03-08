package data.access;

import data.model.Coin;

/**
 * Created by sylvain on 3/5/18.
 */
public class CoinDAO extends AHibernateDAO<Coin> {

    public CoinDAO() {
        super(Coin.class);
    }
}