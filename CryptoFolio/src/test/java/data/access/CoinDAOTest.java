package data.access;

import data.model.Coin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Tests the AHibernateDAO implementation for the entity Coin.
 */
public class CoinDAOTest {

    /**
     * The list to track the coins created during the tests.
     */
    private List<Coin> coinToDeleteList;

    @Before
    public void setUp() throws Exception {

        // Initialize the list of coins to delete at the end of the test
        this.coinToDeleteList = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {

        // Delete the coins created during the tests
        CoinDAO coinDAO = new CoinDAO();
        this.coinToDeleteList.forEach(coin -> coinDAO.delete(coin));
    }

    /**
     * Tests the findById method.
     */
    @Test
    public void findById() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);
        this.coinToDeleteList.add(coin);

        // Act
        Coin coinFromDatabase = coinDAO.findById(coin.getId());

        // Assert
        assertNotNull(coinFromDatabase);
        assertEquals(coin.getId(), coinFromDatabase.getId());
        assertEquals(coin.getShortName(), coinFromDatabase.getShortName());
    }

    /**
     * Tests the findAll method.
     */
    @Test
    public void findAll() {

        // Arrange
        CoinDAO coinDAO = new CoinDAO();
        Long initialCoinRows = coinDAO.countRowInDatabase();

        // Create 50 coins
        for (int i = 0; i < 50; i++) {
            Coin coin = this.initializeCoin();
            coinDAO.create(coin);
            this.coinToDeleteList.add(coin);
        }

        // Act
        List<Coin> coinList = coinDAO.findAll();

        // Assert
        assertNotNull(coinList);
        // Make sure the final count is the initial number of coins + the coins created
        assertEquals(this.coinToDeleteList.size() + initialCoinRows, coinList.size());
    }

    /**
     * Tests the create method.
     */
    @Test
    public void create() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();

        // Act
        coinDAO.create(coin);
        this.coinToDeleteList.add(coin);

        // Assert
        assertTrue(coin.getId() >= 1);
    }

    /**
     * Tests the update method.
     */
    @Test
    public void update() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);
        this.coinToDeleteList.add(coin);

        // Act
        coin.setImageUrl("www.test.com/img");
        coinDAO.update(coin);
        Coin coinFromDatabase = coinDAO.findById(coin.getId());

        // Assert
        assertNotNull(coinFromDatabase);
        assertEquals(coin.getId(), coinFromDatabase.getId());
        assertEquals("www.test.com/img", coinFromDatabase.getImageUrl());
    }

    /**
     * Tests the delete method.
     */
    @Test
    public void delete() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);
        int coinId = coin.getId();

        // Act
        coinDAO.delete(coin);
        Coin coinFromDatabase = coinDAO.findById(coinId);

        // Assert
        assertNull(coinFromDatabase);
    }

    /**
     * Tests the deleteById method.
     */
    @Test
    public void deleteById() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);
        int coinId = coin.getId();

        // Act
        coinDAO.deleteById(coinId);
        Coin coinFromDatabase = coinDAO.findById(coinId);

        // Assert
        assertNull(coinFromDatabase);
    }

    /**
     * Tests the findOneByProperty method.
     */
    @Test
    public void findOneByProperty() {

        // Arrange
        Coin coin = this.initializeCoin();
        CoinDAO coinDAO = new CoinDAO();
        coinDAO.create(coin);
        this.coinToDeleteList.add(coin);

        // Act
        Coin coinFromDatabase = coinDAO.findOneByProperty("shortName", coin.getShortName());

        // Assert
        assertNotNull(coinFromDatabase);
        assertEquals(coin.getId(), coinFromDatabase.getId());
        assertEquals(coin.getShortName(), coinFromDatabase.getShortName());
    }

    /**
     * Tests the countRowInDatabase method.
     */
    @Test
    public void countRowInDatabase() {

        // Arrange
        CoinDAO coinDAO = new CoinDAO();
        Long initialCoinRows = coinDAO.countRowInDatabase();

        // Create 50 coins
        for (int i = 0; i < 50; i++) {
            Coin coin = this.initializeCoin();
            coinDAO.create(coin);
            this.coinToDeleteList.add(coin);
        }
        Long expectedCoinRows = Long.valueOf(this.coinToDeleteList.size()) + initialCoinRows;

        // Act
        Long coinRows = coinDAO.countRowInDatabase();

        // Assert -- make sure the final count is the initial number of coins + the coins created
        assertEquals(expectedCoinRows, coinRows);
    }

    /**
     * Initializes a new coin entity.
     * @return A coin with its property initialized.
     */
    private Coin initializeCoin() {

        // Initialize the coin properties and return it
        int randomInt = ThreadLocalRandom.current().nextInt(1, 100000000);
        Coin coin = new Coin();
        coin.setExternalId(randomInt);
        coin.setImageUrl("www.image.com/myimage");
        coin.setUrl("www.great-coin.com");
        coin.setCoinName(String.format("ABCoin%d", randomInt));
        coin.setShortName(String.format("ABC%d", randomInt));
        return coin;
    }
}