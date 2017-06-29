package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 21.04.2017
 */
public class TherunetCom implements Fetcher {

    @Override
    public String url() {
        return "therunet.com";
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

        String article = doc.select("div.article_content").html();
        model.setText(Utils.formatHTMLToText(article));

        return model;
    }

}
