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


        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        Coin entity = null;

        // Try to retrieve the entity
        try {
            tx = session.beginTransaction();

            // Add restriction.
            Criteria cr = session.createCriteria(Coin.class);
            cr.add(Restrictions.eq("shortName", shortName));
            entity = (Coin) cr.uniqueResult();
            tx.commit();

            // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while finding an entity by its ID", e);

            // Rollback the transaction if possible
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return entity;


    }
}