package com.saniaky.parser;

import com.saniaky.parser.custom.AinUa;
import com.saniaky.parser.custom.BankirRu;
import com.saniaky.parser.custom.CossaRu;
import com.saniaky.parser.custom.DeloUa;
import com.saniaky.parser.custom.FirrmaRu;
import com.saniaky.parser.custom.ForbesNetUa;
import com.saniaky.parser.custom.ForumDailyCom;
import com.saniaky.parser.custom.FuturebankingRu;
import com.saniaky.parser.custom.GeekTimesRu;
import com.saniaky.parser.custom.GrowRichSu;
import com.saniaky.parser.custom.HabrahabrRu;
import com.saniaky.parser.custom.IGateComUa;
import com.saniaky.parser.custom.ImenaUa;
import com.saniaky.parser.custom.InformationTechnologyRu;
import com.saniaky.parser.custom.InventureComUa;
import com.saniaky.parser.custom.InvestGo24;
import com.saniaky.parser.custom.ItcUa;
import com.saniaky.parser.custom.KorrespondentNet;
import com.saniaky.parser.custom.LifehackerRu;
import com.saniaky.parser.custom.NovistiitNet;
import com.saniaky.parser.custom.Psm7Com;
import com.saniaky.parser.custom.RbRu;
import com.saniaky.parser.custom.StartapyRu;
import com.saniaky.parser.custom.StartupUA;
import com.saniaky.parser.custom.TherunetCom;
import com.saniaky.parser.custom.ToWaveRu;
import com.saniaky.parser.custom.TutBy;
import com.saniaky.parser.custom.VCRu;
import com.saniaky.parser.custom.VedomostiRu;
import com.saniaky.parser.custom.WebPaymentRu;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FetcherFactory {

    private static Fetcher genericFetcher = new GenericFetcher();

    private static List<Fetcher> fetcherList = Arrays.asList(
            new StartapyRu(),
            new StartupUA(),
            new InvestGo24(),
            new InventureComUa(),
            new DeloUa(),
            new WebPaymentRu(),
            new GrowRichSu(),
            new ForbesNetUa(),
            new VCRu(),
            new LifehackerRu(),
            new ToWaveRu(),
            new AinUa(),
            new ImenaUa(),
            new FuturebankingRu(),
            new RbRu(),
            new HabrahabrRu(),
            new BankirRu(),
            new TherunetCom(),
            new GeekTimesRu(),
            new TutBy(),
            new CossaRu(),
            new VedomostiRu(),
            new ForumDailyCom(),
            new IGateComUa(),
            new NovistiitNet(),
            new KorrespondentNet(),
            new InformationTechnologyRu(),
            new Psm7Com(),
            new ItcUa(),
            new FirrmaRu()
    );


    public static Fetcher getFetcher(String url) {

        for (Fetcher fetcher : fetcherList) {
            if (containsIgnoreCase(url, fetcher.url())) {
                return fetcher;
            }
        }

        return genericFetcher;
    }

}
