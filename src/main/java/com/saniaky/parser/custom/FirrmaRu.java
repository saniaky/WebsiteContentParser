package com.saniaky.parser.custom;

import com.saniaky.util.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author saniaky
 * @since 6/29/17
 */
public class FirrmaRu implements Fetcher {

    @Override
    public String url() {
        return "firrma.ru";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);
        Utils.sleep(3000);

        if (doc == null) {
            return null;
        }

        BasicModel model = new BasicModel();

        model.setUrl(url);

        String title = doc.select("meta[property=og:title]").attr("content");
        title = title.replaceAll("- Firrma. Данные для стартапа", "");
        model.setTitle(title);

        model.setImageUrl(doc.select("meta[property=og:image]").attr("content"));

        String article = doc.select(".news-text").html();
        model.setText(Utils.formatHTMLToText(article));

        return model;
    }

}
