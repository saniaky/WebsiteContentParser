package com.saniaky.crawler;

import com.saniaky.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Fetch all urls from website: http://startapy.ru/
 *
 * @author saniaky
 * @since 5/10/17
 */
public class CreateArticlesUrls {

    private static final String ARTICLES_TXT = "articles.txt";

    public static void main(String[] args) throws IOException {
        new CreateArticlesUrls().parse();
    }

    private void parse() throws IOException {
        List<String> allUrls = new LinkedList<>();
        List<String> articlesURLs = Utils.getLines(ARTICLES_TXT);

        for (String articlesURL : articlesURLs) {
            do {
                Document page = Utils.getDocument(articlesURL);
                if (is404(page)) {
                    break;
                }

                allUrls.addAll(findArticlesOnPage(page));
                System.out.println(articlesURL);

                articlesURL = getNextURL(articlesURL);
            } while (true);
        }

        Utils.writeToFile("allUrls.txt", allUrls);
        System.out.println(allUrls);
    }

    private boolean is404(Document page) {
        return page == null || page.select("body").hasClass("error404");
    }

    private String getNextURL(String articlesURL) {
        articlesURL = articlesURL.substring(0, articlesURL.length() - 1);

        int beginIndex = articlesURL.lastIndexOf("/") + 1;
        int pageNum = Integer.valueOf(articlesURL.substring(beginIndex));
        pageNum++;
        articlesURL = articlesURL.substring(0, beginIndex) + pageNum + "/";

        return articlesURL;
    }


    private List<String> findArticlesOnPage(Document page) {
        List<String> urls = new LinkedList<>();
        Elements elements = page.select("a.preview");
        for (Element element : elements) {
            urls.add(element.attr("href"));
        }
        return urls;
    }

}
