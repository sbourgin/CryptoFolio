package data;

import market.cryptocompare.CryptoCompareApiHelper;
import market.cryptocompare.CryptoCompareRateLimiter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sylvain on 2/26/18.
 */
public class DatabaseSessionFactory {

    /**
     * The session factory instance.
     */
    private static volatile SessionFactory instance = null;

    /***
     * The class logger.
     */
    private static Logger logger = LoggerFactory.getLogger(DatabaseSessionFactory.class);

    /**
     * Gets the SessionFactory instance.
     * @return The instance of the SessionFactory.
     */
    public final static SessionFactory getInstance() {

        // Double checked implementation of the singleton pattern
        if (DatabaseSessionFactory.instance == null) {
            synchronized(DatabaseSessionFactory.class) {
                if (DatabaseSessionFactory.instance == null) {
                    try {
                        DatabaseSessionFactory.instance = new Configuration().configure().buildSessionFactory();
                    } catch (Throwable ex) {
                        logger.error("Failed to create sessionFactory object.", ex);
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return DatabaseSessionFactory.instance;
    }
}