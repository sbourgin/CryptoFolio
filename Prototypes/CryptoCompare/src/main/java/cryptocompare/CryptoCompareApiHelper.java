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

    // TODO implement request limiter
    // TODO find a retry manager library

    /***
     * The application logger.
     */
    private static Logger logger = LoggerFactory.getLogger(CryptoCompareApiHelper.class);

    private final String API_BASE_URL = "https://min-api.cryptocompare.com/data/";

    private final int CONNECT_TIMEOUT = 1000; // TODO check number

    private final int SOCKET_TIMEOUT = 1000;  // TODO check number


    public CryptoCompareApiHelper() {

    }

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

        // Execute the request
        String responseFromApi = null;
        try {
            responseFromApi = Request.Get(apiURI)
                    .connectTimeout(this.CONNECT_TIMEOUT)
                    .socketTimeout(this.SOCKET_TIMEOUT)
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
