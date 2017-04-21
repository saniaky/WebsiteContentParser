package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 21.04.2017
 */
public class AinUa implements Fetcher {

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
            model.setText(Utils.replaceDivWithNewLines(article));
        } else {
            article = doc.select("div.post_height").html();
            model.setText(Utils.replaceParagraphWithNewLines(article));
        }

        return model;
    }

}
