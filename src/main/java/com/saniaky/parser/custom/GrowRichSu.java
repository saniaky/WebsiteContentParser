package com.saniaky.parser.custom;

import com.saniaky.util.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 06.03.2017
 */
public class GrowRichSu implements Fetcher {

    @Override
    public String url() {
        return "grow-rich.su";
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

        String article = doc.select("div.hentry-content").html();
        model.setText(Utils.replaceParagraphWithNewLines(article));

        return model;
    }

}
