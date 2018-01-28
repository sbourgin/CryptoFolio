package manager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Coin;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import response.GetCoinResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by sylvain on 1/15/18.
 */
public class MarketApiManager {

    private IExchangeApiHelper exchangeApiHelper;

    public MarketApiManager(IExchangeApiHelper exchangeApiHelper){
        this.exchangeApiHelper = exchangeApiHelper;
    }

    /**
     * Gets the list of coins.
     * The key is the coin's short name.
     */
    public Map<String, Coin> GetCoinDictionary() {

        GetCoinResponse getCoinResponse = this.exchangeApiHelper.GetAllCoins(); // TODO Make GetCoinResponse generic (it's the response of a specific implementation)

        return getCoinResponse.coinMap;


    }

}
