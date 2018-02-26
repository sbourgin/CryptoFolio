package market.cryptocompare.request;

import com.google.common.base.Preconditions;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 *  * The request to get the current price of a coin in a specified currency.
 */
public class PriceRequest {

    /**
     * The coins short name to include in the price request. The value is a comma separated list.
     */
    private List<String> coinShortNameList;

    /**
     * The currency short name for the coin's price.
     */
    private String currencyShortName;

    /**
     * Constructor.
     * @param coinShortNameList The coins short name to include in the price request
     * @param currencyShortName The currency short name for the coin's price
     */
    public PriceRequest(List<String> coinShortNameList, String currencyShortName) {

        // Check the preconditions
        Preconditions.checkNotNull(coinShortNameList);
        Preconditions.checkArgument(coinShortNameList.size() > 0);
        Preconditions.checkNotNull(currencyShortName);
        Preconditions.checkArgument(currencyShortName.length() > 0);

        // Set members
        this.coinShortNameList = coinShortNameList;
        this.currencyShortName = currencyShortName;
    }

    /**
     * Generates the parameters for the http request.
     * @return The list of pair parameters.
     */
    public List<NameValuePair> generateParameters() {
        List<NameValuePair> parameterList = new ArrayList<>();
        parameterList.add(new BasicNameValuePair("fsyms",  String.join(",", this.coinShortNameList)));
        parameterList.add(new BasicNameValuePair("tsyms",  this.currencyShortName));
        return parameterList;
    }
}