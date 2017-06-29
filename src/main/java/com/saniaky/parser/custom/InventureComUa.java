package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 28.02.2017
 */
public class InventureComUa implements Fetcher {

    @Override
    public String url() {
        return "inventure.com.ua";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        BasicModel model = new BasicModel();

        model.setUrl(url);
        model.setTitle(doc.select("meta[name=og:title]").attr("content"));
        model.setImageUrl(doc.select("meta[name=og:image]").attr("content"));

        String article = doc.select("div[itemprop=text]").html();
        model.setText(Utils.replaceParagraphWithNewLines(article));

        return model;
    }

}
