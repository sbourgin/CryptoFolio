package data.access;

import data.model.Coin;

/**
 * Created by sylvain on 3/5/18.
 */
public class CoinDAO extends AbstractHibernateDAO<Coin> {

    public CoinDAO(){
        setClazz(Coin.class );
    }
}