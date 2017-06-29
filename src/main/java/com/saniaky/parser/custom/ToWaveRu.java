package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.parser.Fetcher;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Alexander Kohonovsky
 * @since 09.03.2017
 */
public class ToWaveRu implements Fetcher {

    @Override
    public String url() {
        return "towave.ru";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        BasicModel model = new BasicModel();

        model.setUrl(url);
        model.setTitle(doc.select(".node-top-frame > h1").text());
        if (StringUtils.isBlank(model.getTitle())) {
            model.setTitle(doc.select("title").text());
        }

        model.setImageUrl(doc.select(".content .field-item .imagecache").attr("src"));


        StringBuilder text = new StringBuilder();

        Element content = doc.select(".content").get(0);

        for (Element element : content.children()) {
            if (element.hasClass("field")) {
                continue;
            }

            if (element.hasClass("node-top-frame")) {
                model.setImageUrl(element.select("img").attr("src"));
            }

            if (element.is("h3") || element.is("p") || element.is("div")) {
                text.append("<p>").append(element.html()).append("</p>");
            }
        }
        model.setText(Utils.replaceParagraphWithNewLines(text.toString()));

        return model;
    }

}
