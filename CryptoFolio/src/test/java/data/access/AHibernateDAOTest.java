package data.access;

import data.DatabaseSessionFactory;
import data.model.Coin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by sylvain on 3/8/18.
 */
public class AHibernateDAOTest {

    @Before
    public void setUp() throws Exception {


        /*
        *       // setup the session factory
      AnnotationConfiguration configuration = new AnnotationConfiguration();
      configuration.addAnnotatedClass(SuperHero.class)
        .addAnnotatedClass(SuperPower.class)
        .addAnnotatedClass(SuperPowerType.class);
      configuration.setProperty("hibernate.dialect",
        "org.hibernate.dialect.H2Dialect");
      configuration.setProperty("hibernate.connection.driver_class",
        "org.h2.Driver");
      configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem");
      configuration.setProperty("hibernate.hbm2ddl.auto", "create");
      sessionFactory = configuration.buildSessionFactory();
      session = sessionFactory.openSession();
      */



/*
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(10000);

        data.model.Coin coin = new data.model.Coin();
        coin.setShortName("ABC" + randomInt);
        coin.setCoinName("Stealite" + randomInt);

        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);


        List<Coin> allCoins = coinDAO.findAll();


        coin.setShortName("DEF" + randomInt);

        coinDAO.update(coin);

        coinDAO.delete(coin);

        //      coinDAO.deleteById(coin.getId());

        data.model.Coin coin10 = coinDAO.findById(10);

        coinDAO.deleteById(34567890);


        CoinPriceDAO coinPriceDAO = new CoinPriceDAO();
        List<data.model.CoinPrice> allCoinPrices = coinPriceDAO.findAll();

*/

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findById() throws Exception {
    }

    @Test
    public void findAll() throws Exception {




    }

    @Test
    public void create() throws Exception {
/*
        Coin coin = new Coin();
        coin.setExternalId(12);
        coin.setUrl("Pliiif");

        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin); */

    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void deleteById() throws Exception {
    }

    // findOneByProperty

}