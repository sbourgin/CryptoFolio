package data.access;

import data.model.CoinPrice;

/**
 * Created by sylvain on 3/5/18.
 */
public class CoinPriceDAO extends AHibernateDAO<CoinPrice> {

    public CoinPriceDAO() {
        super(CoinPrice.class);
    }
}