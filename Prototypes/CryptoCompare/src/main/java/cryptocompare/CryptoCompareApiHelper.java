package cryptocompare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.IExchangeApiHelper;
import model.Coin;
import org.apache.http.client.fluent.Request;
import cryptocompare.reponse.GetCoinResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sylvain on 1/27/18.
 */
public class CryptoCompareApiHelper implements IExchangeApiHelper {

    // TODO implement request limiter

    private final String API_BASE_URL = "https://min-api.cryptocompare.com/data/";

    private final int CONNECT_TIMEOUT = 1000; // TODO check number

    private final int SOCKET_TIMEOUT = 1000;  // TODO check number


    public CryptoCompareApiHelper() {

    }

    public Map<String, Coin> getAllCoins() {

       // "https://min-api.cryptocompare.com/data/all/coinlist"

        // Create the URI
        URI apiURI = null;
        try {
            apiURI = new URI(this.API_BASE_URL);
            apiURI = apiURI.resolve("all/coinlist");
        } catch (URISyntaxException e) {
            e.printStackTrace(); // TODO do something
        }

        // Execute the request
        String responseFromApi = null;
        try {
            responseFromApi = Request.Get(apiURI)
                    .connectTimeout(this.CONNECT_TIMEOUT)
                    .socketTimeout(this.SOCKET_TIMEOUT)
                    .execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse and return the cryptocompare
        Gson gson = new GsonBuilder().create();
        GetCoinResponse getCoinResponse = gson.fromJson(responseFromApi, GetCoinResponse.class);

        Map<String, Coin> coinMap = new HashMap<String, Coin>();

      //  getCoinResponse.coinMap.forEach((k,v) -> coinMap.put(k, new Coin(v)));

        return null;
    }

}
