package com.saniaky.parser.custom;

import com.saniaky.util.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 21.04.2017
 */
public class AinUa implements Fetcher {

    @Override
    public String url() {
        return "ain.ua";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        BasicModel model = new BasicModel();

        model.setUrl(url);
        model.setTitle(doc.select("meta[property=og:title]").attr("content"));
        model.setImageUrl(doc.select("meta[property=og:image]").attr("content"));

        String article = "";

        if (url.contains("/special/")) {
            article = doc.select("[field=text]").html();
        } else {
            article = doc.select(".post_height > p, .post_height > h1, .post_height > h4").html();
        }

        model.setText(Utils.formatHTMLToText(article));

        return model;
    }

}
