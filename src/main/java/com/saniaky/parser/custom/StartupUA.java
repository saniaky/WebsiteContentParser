package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.custom.StartupUAModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class StartupUA implements Fetcher {

    @Override
    public String url() {
        return "startup.ua";
    }

    @Override
    public StartupUAModel parse(String url) {

        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        String title = doc.select("title").text();
        //String description = doc.select("meta[name=description]").get(0).attr("content");
        String imageUrl = doc.select("#big_photo_view > img").attr("src");

        StartupUAModel model = new StartupUAModel(url, title, "", imageUrl, null);

        model.setIdea(doc.select("#IDEA > .i_d_c").text());
        model.setPresetStatus(doc.select("#PRESENT_S > .i_d_c").text());
        model.setMarket(doc.select("#MARKET > .i_d_c").text());
        model.setProblemsAndPossibilities(doc.select("#PROBLEMS_POSSIBILITY > .i_d_c").text());
        model.setProduct(doc.select("#PRODUCT > .i_d_c").text());
        model.setCompetitors(doc.select("#COMPETITION > .i_d_c").text());
        model.setBenefits(doc.select("#BENEFITS > .i_d_c").text());
        model.setFinance(doc.select("#FINANCE > .i_d_c").text());
        model.setBusinessModel(doc.select("#BUSINESS_MODEL > .i_d_c").text());
        model.setProposalGoal(doc.select("#PROPOSAL_GOAL > .i_d_c").text()); // Целевое назначение инвестиций
        model.setProposal(doc.select("#PROPOSAL > .i_d_c").text()); // Предложение инвестор

        return model;
    }

}
