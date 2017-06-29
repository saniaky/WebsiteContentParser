package com.saniaky.parser.custom;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import com.saniaky.model.custom.InvestGo24Model;
import com.saniaky.parser.Fetcher;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author saniaky
 * @since 2/6/17
 */
public class InvestGo24 implements Fetcher {

    @Override
    public String url() {
        return "investgo24.com";
    }

    @Override
    public BasicModel parse(String url) {
        Document doc = Utils.getDocument(url);

        if (doc == null) {
            return null;
        }

        InvestGo24Model model = new InvestGo24Model();

        model.setUrl(url);
        model.setTitle(doc.select("title").text().replace("InvestGo24", ""));
        model.setImageUrl(doc.select("meta[property=og:image]").attr("content"));

        Elements h3 = doc.select("h3");
        for (Element element : h3) {

            if (element.text().equals("Описание бизнес идеи")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                model.setDescription(text);
            }

            if (element.text().equals("Полное описание инвестиционного проекта")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                model.setFullDescription(text);
            }

            if (element.text().equals("Инициатор проекта, контакты")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                model.setContact(text);
            }

            if (element.text().equals("Команда, компетенция и опыт")) {
                String text = element.nextElementSibling().nextElementSibling().text();
                model.setTeam(text);
            }

        }

        return model;
    }

}
