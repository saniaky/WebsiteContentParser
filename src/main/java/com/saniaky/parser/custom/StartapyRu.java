package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 21.04.2017
 */
public class StartapyRu implements Fetcher {

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        BasicModel model = new BasicModel();

        model.setUrl(url);

        String title = doc.select("title").text();
        title = title.substring(0, Math.max(0, title.length() - 14));
        model.setTitle(title);

        model.setImageUrl(doc.select(".alignnone").attr("src"));

        String article = doc.select(".posts.article p").html();
        model.setText(Utils.formatHTMLToText(article));

        return model;
    }

}
