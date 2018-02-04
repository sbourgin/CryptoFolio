package cryptocompare;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by sylvain on 2/2/18.
 */
public final class CryptoCompareRateLimiter {


    private final static int HISTORICAL_API_CALL_PER_SECOND = 8000 / 60;
    private final static int PRICE_API_CALL_PER_SECOND = 150000 / 60;
    private final static int NEWS_API_CALL_PER_SECOND = 3000 / 60;

    private static volatile CryptoCompareRateLimiter instance = null;


    /**
     * The Rate limiter for the API CryptoCompare.
     */
    private RateLimiter historicalApiRateLimiter;

    private RateLimiter priceApiRateLimiter;
    private RateLimiter newsApiRateLimiter;


    /**
     * Constructor.
     * @Remark: intended to be private - singleton pattern
     */
    private CryptoCompareRateLimiter() {
        // Initialize members
        this.historicalApiRateLimiter = RateLimiter.create(CryptoCompareRateLimiter.HISTORICAL_API_CALL_PER_SECOND);
        this.priceApiRateLimiter = RateLimiter.create(PRICE_API_CALL_PER_SECOND);
        this.newsApiRateLimiter = RateLimiter.create(NEWS_API_CALL_PER_SECOND);
    }

    /**
     * Gets the CryptoCompareRateLimiter instance.
     * @return The instance of the CryptoCompareRateLimiter.
     */
    public final static CryptoCompareRateLimiter getInstance() {

        // Double checked implementation of the singleton pattern
        if (CryptoCompareRateLimiter.instance == null) {
            synchronized(CryptoCompareRateLimiter.class) {
                if (CryptoCompareRateLimiter.instance == null) {
                    CryptoCompareRateLimiter.instance = new CryptoCompareRateLimiter();
                }
            }
        }
        return CryptoCompareRateLimiter.instance;
    }

    public void GetHistoricalApiCallAuthorization() {
        this.historicalApiRateLimiter.acquire(1);

    }

    public void GetPriceApiCallAuthorization() {
        this.priceApiRateLimiter.acquire(1);
    }

    public void GetNewsApiCallAuthorization() {
        this.newsApiRateLimiter.acquire(1);
    }
}