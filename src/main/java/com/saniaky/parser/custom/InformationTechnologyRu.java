package com.saniaky.parser.custom;

import com.saniaky.util.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import com.saniaky.parser.GenericFetcher;
import org.jsoup.nodes.Document;

/**
 * @author Alexander Kohonovsky
 * @since 21.04.2017
 */
public class InformationTechnologyRu implements Fetcher {

    @Override
    public String url() {
        return "information-technology.ru";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        GenericFetcher fetcher = new GenericFetcher();
        BasicModel model = fetcher.parse(url);

        model.setTitle(doc.select("meta[name=description]").attr("content"));
        String article = doc.select("#ja-content-main").html();
        model.setText(Utils.replaceParagraphWithNewLines(article));

        return model;
    }

}
