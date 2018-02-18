package cryptocompare.request;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvain on 2/15/18.
 */
public class PriceRequest {
    /**
     * The coins short name to include in the price request. The value is a comma separated list.
     */
    public List<String> coinShortNameList;

    /**
     * The currency short name for the coin's price.
     */
    public String currencyShortName;

    /**
     * Constructor.
     * @param coinShortNameList The coins short name to include in the price request.
     * @param currencyShortName The currency short name for the coin's price..
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

    public List<NameValuePair> generateParameters() {
        List<NameValuePair> parameterList = new ArrayList<>();
        parameterList.add(new BasicNameValuePair("fsyms",  String.join(",", this.coinShortNameList)));
        parameterList.add(new BasicNameValuePair("tsyms",  String.join(",", this.currencyShortName)));
        return parameterList;
    }
}