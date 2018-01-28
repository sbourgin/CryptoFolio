package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.fluent.Request;
import response.GetCoinResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by sylvain on 1/27/18.
 */
public class CryptoCompareApiHelper implements IExchangeApiHelper {  // TODO Make sure this can be mocked for the tests

    // TODO implement request limiter

    private final String API_BASE_URL = "https://min-api.cryptocompare.com/data/";

    private final int CONNECT_TIMEOUT = 1000; // TODO check number

    private final int SOCKET_TIMEOUT = 1000;  // TODO check number


    public CryptoCompareApiHelper() {

    }

    public GetCoinResponse GetAllCoins() {

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

        // Parse and return the response
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(responseFromApi, GetCoinResponse.class);
    }

}
/*
*  For tests
*  String plouf = null; // Hardcoded response for performance "{\"Response\":\"Success\",\"Message\":\"Coin list succesfully returned! This api is moving to https://min-api.cryptocompare.com/data/all/coinlist, please change the path.\",\"BaseImageUrl\":\"https://www.cryptocompare.com\",\"BaseLinkUrl\":\"https://www.cryptocompare.com\",\"DefaultWatchlist\":{\"CoinIs\":\"1182,7605,5038,24854,3807,3808,202330,5324,5031,20131\",\"Sponsored\":\"\"},\"Data\":{\"42\":{\"Id\":\"4321\",\"Url\":\"/coins/42/overview\",\"ImageUrl\":\"/media/12318415/42.png\",\"Name\":\"42\",\"Symbol\":\"42\",\"CoinName\":\"42 Coin\",\"FullName\":\"42 Coin (42)\",\"Algorithm\":\"Scrypt\",\"ProofType\":\"PoW/PoS\",\"FullyPremined\":\"0\",\"TotalCoinSupply\":\"42\",\"PreMinedValue\":\"N/A\",\"TotalCoinsFreeFloat\":\"N/A\",\"SortOrder\":\"34\",\"Sponsored\":false},\"365\":{\"Id\":\"33639\",\"Url\":\"/coins/365/overview\",\"ImageUrl\":\"/media/352070/365.png\",\"Name\":\"365\",\"Symbol\":\"365\",\"CoinName\":\"365Coin\",\"FullName\":\"365Coin (365)\",\"Algorithm\":\"X11\",\"ProofType\":\"PoW/PoS\",\"FullyPremined\":\"0\",\"TotalCoinSupply\":\"2300000000\",\"PreMinedValue\":\"N/A\",\"TotalCoinsFreeFloat\":\"N/A\",\"SortOrder\":\"916\",\"Sponsored\":false}}}";
*/
