package com.saniaky.parser;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FetcherFactory {

    public static GenericFetcher genericFetcher = new GenericFetcher();
    public static StartupUAFetcher startupUaFetcher = new StartupUAFetcher();
    public static Investgo24Fetcher investgo24Fetcher = new Investgo24Fetcher();

    public static Fetcher getFetcher(String url) {

        if (url.contains("startup.ua")) {
            return startupUaFetcher;
        }

        if (url.contains("investgo24.com")) {
            return investgo24Fetcher;
        }

        return genericFetcher;
    }

}
