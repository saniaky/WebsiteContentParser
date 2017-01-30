package com.saniaky.parser;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FetcherFactory {

    public static GenericFetcher genericFetcher = new GenericFetcher();
    public static StartupUAFetcher startupUaFetcher = new StartupUAFetcher();

    public static Fetcher getFetcher(String url) {

        if (url.contains("startup.ua")) {
            return startupUaFetcher;
        }

        return genericFetcher;
    }

}
