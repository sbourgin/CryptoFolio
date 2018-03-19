package data.access;

import data.DatabaseSessionFactory;
import data.model.Coin;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The coin's DAO.
 */
public class CoinDAO extends AHibernateDAO<Coin> {

    /**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(CoinDAO.class);

    /**
     * Constructor.
     */
    public CoinDAO() {
        super(Coin.class);
    }

    public Coin findByShortName(String shortName) {
        return super.findOneByProperty("shortName", shortName);
    }
}