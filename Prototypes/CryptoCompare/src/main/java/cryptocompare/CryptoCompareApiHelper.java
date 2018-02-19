package cryptocompare;

import Core.PreconditionsValidation;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import cryptocompare.request.PriceHistoricalRequest;
import cryptocompare.request.PriceRequest;
import manager.IExchangeApiHelper;
import model.Coin;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import cryptocompare.reponse.GetCoinResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
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
    private final String API_BASE_URL = "https://min-api.cryptocompare.com";

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
    @Override
    public Map<String, Coin> getAllCoins() {
        // Create the URI
        URI apiURI = this.tryCreateURI("data/all/coinlist", null);

        // Acquire the authorization of executing this API call (this call blocks the thread)
        CryptoCompareRateLimiter.getInstance().getPriceApiCallAuthorization();

        // Execute the request
        String responseFromApi = this.executesRequest(apiURI);

        // Parse the list of coins
        Gson gson = new GsonBuilder().create();
        GetCoinResponse getCoinResponse = gson.fromJson(responseFromApi, GetCoinResponse.class);
        Map<String, Coin> coinMap = new HashMap<String, Coin>();

        // Return early if the response doesn't contain any coin
        if (getCoinResponse.coinMap == null)
            return coinMap;

        // Convert the CryptoCompareCoin to Coin and return the expected map
        getCoinResponse.coinMap.forEach((k,v) -> coinMap.put(k, new Coin(v)));
        return coinMap;
    }

    @Override
    public Map<String, BigDecimal> getCoinsCurrentValue(List<String> coinShortNameList, String currencyShortName) {
        // Check preconditions
        Preconditions.checkNotNull(coinShortNameList);
        Preconditions.checkArgument(coinShortNameList.size() > 0);
        PreconditionsValidation.checkStringNotEmpty(currencyShortName);

        // Create a Price Request
        PriceRequest priceRequest = new PriceRequest(coinShortNameList, currencyShortName);

        // Create the URI
        URI apiURI = this.tryCreateURI("data/pricemulti", priceRequest.generateParameters());

        // Acquire the authorization of executing this API call (this call blocks the thread)
        CryptoCompareRateLimiter.getInstance().getPriceApiCallAuthorization();

        // Execute the request
        String responseFromApi = this.executesRequest(apiURI);

        // Parse the list of coins
        Gson gson = new GsonBuilder().create();
        LinkedTreeMap<String, LinkedTreeMap<String, BigDecimal>> coinPriceApiResponseMap = gson.fromJson(responseFromApi, new LinkedTreeMap<String, LinkedTreeMap<String, BigDecimal>>().getClass());
        Map<String, BigDecimal> coinPriceResult = new HashMap<String, BigDecimal>();

        // Return early if the response doesn't contain any coin
        if (coinPriceApiResponseMap == null || coinPriceApiResponseMap.size() == 0)
            return coinPriceResult;

        // Prepare the response
        for (String coinShortName : coinPriceApiResponseMap.keySet()) {
            // Try to get the price, if we can't skip the coin
            if (false == coinPriceApiResponseMap.get(coinShortName).containsKey(currencyShortName))
                continue;

            // Add the price to the result map
            coinPriceResult.put(coinShortName, coinPriceApiResponseMap.get(coinShortName).get(currencyShortName));
        }
        return coinPriceResult;
    }

    @Override
    public BigDecimal getCoinHistoricalValue(String coinShortName, String currencyShortName, ZonedDateTime zonedDateTime) {
        // Check preconditions
        PreconditionsValidation.checkStringNotEmpty(coinShortName);
        PreconditionsValidation.checkStringNotEmpty(currencyShortName);

        // Create a price historical request
        PriceHistoricalRequest priceHistoricalRequest = new PriceHistoricalRequest(coinShortName, currencyShortName, zonedDateTime);

        // Create the URI
        URI apiURI = this.tryCreateURI("data/pricehistorical", priceHistoricalRequest.generateParameters());

        // Acquire the authorization of executing this API call (this call blocks the thread)
        CryptoCompareRateLimiter.getInstance().getHistoricalApiCallAuthorization();

        // Execute the request
        String responseFromApi = this.executesRequest(apiURI);

        // Parse the response
        Gson gson = new GsonBuilder().create();
        LinkedTreeMap<String, LinkedTreeMap<String, Double>> coinPriceApiResponseMap = gson.fromJson(responseFromApi, new LinkedTreeMap<String, LinkedTreeMap<String, Double>>().getClass());

        // Return early if the response doesn't contain any coin
        if (coinPriceApiResponseMap == null || coinPriceApiResponseMap.size() == 0 || false == coinPriceApiResponseMap.get(coinShortName).containsKey(currencyShortName))
            return null;

        // Return the price
        return BigDecimal.valueOf(coinPriceApiResponseMap.get(coinShortName).get(currencyShortName));

    }

    /**
     * Tries to create the URI from the API base url and the address of the endpoint.
     * @param endpointAddress The endpoint address. E.g: pricemulti or all/coinlist
     * @param requestParameterList The request parameters (optional parameter)
     * @return The URI of the endpoint.
     */
    private URI tryCreateURI(String endpointAddress, List<NameValuePair> requestParameterList) {
        // Check preconditions
        Preconditions.checkNotNull(endpointAddress);
        Preconditions.checkArgument(endpointAddress.length() > 0);

        // Try create the URI
        URIBuilder apiURIBuilder;
        try {
            apiURIBuilder = new URIBuilder(this.API_BASE_URL).setPath(endpointAddress);

            // Add the parameters if specified
            if (requestParameterList != null)
                apiURIBuilder.addParameters(requestParameterList);

            // Build and return the URI
            return apiURIBuilder.build();
        } catch (URISyntaxException e) {
            logger.error("An exception occurred when trying to create the URI", e);
            throw new RuntimeException("Failed to create the URI", e);
        }
    }

    /**
     * Executes the request and return the response.
     * @param apiURI The request URI to execute
     * @return The response of the request
     */
    private String executesRequest(URI apiURI) {
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
        return responseFromApi;
    }
}