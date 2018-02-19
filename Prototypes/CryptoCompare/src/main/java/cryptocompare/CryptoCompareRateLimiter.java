package cryptocompare;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by sylvain on 2/2/18.
 */
public final class CryptoCompareRateLimiter {

    /**
     * Number of calls to the historical API allowed per second.
     */
    private final static int HISTORICAL_API_CALL_PER_SECOND = 8000 / 60;

    /**
     * Number of calls to the price API allowed per second.
     */
    private final static int PRICE_API_CALL_PER_SECOND = 150000 / 60;

    /**
     * Number of calls to the news API allowed per second.
     */
    private final static int NEWS_API_CALL_PER_SECOND = 3000 / 60;

    /**
     * The instance of the singleton.
     */
    private static volatile CryptoCompareRateLimiter instance = null;

    /**
     * The rate limiter for the historical API.
     */
    private RateLimiter historicalApiRateLimiter;

    /**
     * The rate limiter for the price API.
     */
    private RateLimiter priceApiRateLimiter;

    /**
     * The rate limiter for the news API.
     */
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

    /**
     * Gets the authorization to call the historical API. This will block the thread if wait is needed.
     */
    public void getHistoricalApiCallAuthorization() {
        // Acquire the authorization for one call
        this.historicalApiRateLimiter.acquire(1);

    }

    /**
     * Gets the authorization to call the price API. This will block the thread if wait is needed.
     */
    public void getPriceApiCallAuthorization() {
        // Acquire the authorization for one call
        this.priceApiRateLimiter.acquire(1);
    }

    /**
     * Gets the authorization to call the news API. This will block the thread if wait is needed.
     */
    public void getNewsApiCallAuthorization() {
        // Acquire the authorization for one call
        this.newsApiRateLimiter.acquire(1);
    }
}