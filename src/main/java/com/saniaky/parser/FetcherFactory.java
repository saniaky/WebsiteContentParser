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
    private static ForbesNetUa forbesNetUa = new ForbesNetUa();
    private static VCRu vcRu = new VCRu();
    private static LifehackerRu lifehackerRu = new LifehackerRu();
    private static ToWaveRu toWaveRu = new ToWaveRu();
    private static AinUa ainUa = new AinUa();
    private static ImenaUa imenaUa = new ImenaUa();
    private static FuturebankingRu futurebankingRu = new FuturebankingRu();
    private static RbRu rbRu = new RbRu();
    private static HabrahabrRu habrahabrRu = new HabrahabrRu();
    private static BankirRu bankirRu = new BankirRu();
    private static TherunetCom therunetCom = new TherunetCom();


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

        if (containsIgnoreCase(url, "forbes.net.ua")) {
            return forbesNetUa;
        }

        if (containsIgnoreCase(url, "vc.ru")) {
            return vcRu;
        }

        if (containsIgnoreCase(url, "lifehacker.ru")) {
            return lifehackerRu;
        }

        if (containsIgnoreCase(url, "towave.ru")) {
            return toWaveRu;
        }

        if (containsIgnoreCase(url, "ain.ua")) {
            return ainUa;
        }

        if (containsIgnoreCase(url, "imena.ua")) {
            return imenaUa;
        }

        if (containsIgnoreCase(url, "futurebanking.ru")) {
            return futurebankingRu;
        }

        if (containsIgnoreCase(url, "rb.ru")) {
            return rbRu;
        }

        if (containsIgnoreCase(url, "habrahabr.ru")) {
            return habrahabrRu;
        }

        if (containsIgnoreCase(url, "bankir.ru")) {
            return bankirRu;
        }

        if (containsIgnoreCase(url, "therunet.com")) {
            return therunetCom;
        }

        return genericFetcher;
    }

}
