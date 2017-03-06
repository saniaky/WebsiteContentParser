package com.saniaky.parser;

import com.saniaky.parser.custom.*;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FetcherFactory {

    private static GenericFetcher genericFetcher = new GenericFetcher();
    private static StartupUAFetcher startupUaFetcher = new StartupUAFetcher();
    private static InvestGo24Fetcher investGo24Fetcher = new InvestGo24Fetcher();
    private static InventureComUa inventureComUa = new InventureComUa();
    private static DeloUa deloUa = new DeloUa();
    private static WebPaymentRu webPaymentRu = new WebPaymentRu();
    private static GrowRichSu growRichSu = new GrowRichSu();

    public static Fetcher getFetcher(String url) {

        if (containsIgnoreCase(url, "startup.ua")) {
            return startupUaFetcher;
        }

        if (containsIgnoreCase(url, "investgo24.com")) {
            return investGo24Fetcher;
        }

        if (containsIgnoreCase(url, "inventure.com.ua")) {
            return inventureComUa;
        }

        if (containsIgnoreCase(url, "delo.ua")) {
            return deloUa;
        }

        if (containsIgnoreCase(url, "web-payment.ru")) {
            return webPaymentRu;
        }

        if (containsIgnoreCase(url, "grow-rich.su")) {
            return growRichSu;
        }

        return genericFetcher;
    }

}
