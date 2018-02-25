package cryptocompare.request;

import core.PreconditionsValidation;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The request to get the price of a coin in a specified currency at a specified time.
 */
public class PriceHistoricalRequest {

    /**
     * The coin short name to include in the price request.
     */
    private String coinShortName;

    /**
     * The currency short name for the coin's price.
     */
    private String currencyShortName;

    /**
     * The time of the price request.
     */
    protected ZonedDateTime zonedDateTime;

    /**
     * Constructor.
     * @param coinShortName The coin short name to include in the price request
     * @param currencyShortName The currency short name for the coin's price
     * @param zonedDateTime The time of the price request
     */
    public PriceHistoricalRequest(String coinShortName, String currencyShortName, ZonedDateTime zonedDateTime) {
        // Check preconditions
        PreconditionsValidation.checkStringNotEmpty(coinShortName);
        PreconditionsValidation.checkStringNotEmpty(currencyShortName);

        // Set members
        this.coinShortName = coinShortName;
        this.currencyShortName = currencyShortName;
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * Generates the parameters for the http request.
     * @return The list of pair parameters.
     */
    public List<NameValuePair> generateParameters() {
        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("fsym",  this.coinShortName));
        parameterList.add(new BasicNameValuePair("tsyms",  this.currencyShortName));
        parameterList.add(new BasicNameValuePair("ts", String.valueOf(zonedDateTime.toEpochSecond())));
        return parameterList;
    }
}