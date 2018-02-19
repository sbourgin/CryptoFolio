package cryptocompare.request;

import Core.PreconditionsValidation;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvain on 2/15/18.
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