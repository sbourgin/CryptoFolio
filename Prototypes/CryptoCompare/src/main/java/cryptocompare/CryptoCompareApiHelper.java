package cryptocompare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.IExchangeApiHelper;
import model.Coin;
import org.apache.http.client.fluent.Request;
import cryptocompare.reponse.GetCoinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sylvain on 1/27/18.
 */
public class CryptoCompareApiHelper implements IExchangeApiHelper {

    /***
     * The application logger.
     */
    private static Logger logger = LoggerFactory.getLogger(CryptoCompareApiHelper.class);

    /**
     * Base url for the Crypto Compare API.
     */
    private final String API_BASE_URL = "https://min-api.cryptocompare.com/data/";

    /**
     * Timeout for the connection.
     */
    private final int CONNECT_TIMEOUT_MILLISECONDS = 5 * 1000; /* 5 seconds */

    /**
     * Timeout for the socket.
     */
    private final int SOCKET_TIMEOUT_MILLISECONDS = 5 * 1000;  /* 5 seconds */

    /**
     * Default constructor.
     */
    public CryptoCompareApiHelper() {
    }

    /***
     * Gets a map of all the existing coins
     * @return A map with all the existing coins where the key is the coin's short name.
     */
    public Map<String, Coin> getAllCoins() {
        // Create the URI
        URI apiURI = null;
        try {
            apiURI = new URI(this.API_BASE_URL);
            apiURI = apiURI.resolve("all/coinlist");
        } catch (URISyntaxException e) {
            logger.error("An exception occurred when trying to create the URI", e);
            throw new RuntimeException("Failed to create the URI", e);
        }

        // Acquire the authorization of executing this API call (this call blocks the thread)
        CryptoCompareRateLimiter.getInstance().GetPriceApiCallAuthorization();

        // Execute the request
        String responseFromApi = null;
        try {
            responseFromApi = Request.Get(apiURI)
                    .connectTimeout(this.CONNECT_TIMEOUT_MILLISECONDS)
                    .socketTimeout(this.SOCKET_TIMEOUT_MILLISECONDS)
                    .execute().returnContent().asString();

        } catch (IOException e) {
            logger.error("An exception occurred when trying to execute the request ", e);
            throw new RuntimeException("Failed to execute the API request", e);
        }

        // Parse the list of coins
        Gson gson = new GsonBuilder().create();
        GetCoinResponse getCoinResponse = gson.fromJson(responseFromApi, GetCoinResponse.class);

        // Convert the CryptoCompareCoin to Coin and return the expected map
        Map<String, Coin> coinMap = new HashMap<String, Coin>();
        getCoinResponse.coinMap.forEach((k,v) -> coinMap.put(k, new Coin(v)));
        return coinMap;
    }
}