package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author saniaky
 * @since 6/9/17
 */
public class ForumDailyCom implements Fetcher {

    @Override
    public String url() {
        return "forumdaily.com";
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

        String article = doc.select(".post-content").html();
        model.setText(Utils.formatHTMLToText(article));

        return model;
    }

}

