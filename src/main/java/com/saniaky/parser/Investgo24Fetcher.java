package com.saniaky.parser;

import com.saniaky.model.BasicModel;
import com.saniaky.model.InverstGo24Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author saniaky
 * @since 2/6/17
 */
public class Investgo24Fetcher implements Fetcher {

    private static final int TIMEOUT = 3000;

    @Override
    public BasicModel parse(String url) {
        Document doc = getDocument(url);

        if (doc == null) {
            return null;
        }

        InverstGo24Model model = new InverstGo24Model();
        model.setTitle(doc.select("title").text());
        model.setUrl(url);
        model.setImageUrl(doc.select("meta[property=og:image]").attr("content"));

        StringBuilder sb = new StringBuilder();
        Elements h3 = doc.select("h3");
        for (Element element : h3) {

            if (element.text().equals("Описание бизнес идеи")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                sb.append(text);
                sb.append("\n\n");
                model.setDescription(text);
            }

            if (element.text().equals("Полное описание инвестиционного проекта")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                sb.append(text);
                sb.append("\n\n");
                model.setFullDescription(text);
            }

            if (element.text().equals("Инициатор проекта, контакты")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                sb.append(text);
                sb.append("\n\n");
                model.setContact(text);
            }

            if (element.text().equals("Команда, компетенция и опыт")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                sb.append(text);
                sb.append("\n\n");
                model.setTeam(text);
            }

        }

        return model;
    }

    private Document getDocument(String url) {
        try {
            return Jsoup.connect(url).timeout(TIMEOUT).get();
        } catch (IOException e) {
        }
        return null;
    }

}
