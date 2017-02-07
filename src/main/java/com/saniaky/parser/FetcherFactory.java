package com.saniaky.parser;

import com.saniaky.parser.custom.InvestGo24Fetcher;
import com.saniaky.parser.custom.StartupUAFetcher;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FetcherFactory {

    private static GenericFetcher genericFetcher = new GenericFetcher();
    private static StartupUAFetcher startupUaFetcher = new StartupUAFetcher();
    private static InvestGo24Fetcher investGo24Fetcher = new InvestGo24Fetcher();

    public static Fetcher getFetcher(String url) {

        if (url.contains("startup.ua")) {
            return startupUaFetcher;
        }

        if (url.contains("investgo24.com")) {
            return investGo24Fetcher;
        }

        return genericFetcher;
    }

}
