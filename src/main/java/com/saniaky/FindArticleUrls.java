package com.saniaky;

import com.saniaky.model.StartupMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class FindArticleUrls {

    public static void main(String[] args) throws IOException {
        FindArticleUrls findArticleUrls = new FindArticleUrls();

        List<String> urlsList = new Main().getLines("urls.txt");
        List<String> parsed = new Main().getLines("done.txt");
        urlsList.removeAll(parsed);

        List<String> notFound = new ArrayList<>();
        List<String> somethingWrong = new ArrayList<>();
        List<StartupMapper> successfulResults = new ArrayList<>();

        for (String id : urlsList) {
            String url = findArticleUrls.getDataFromGoogle("site:https://startup.ua+" + id);

            if ("notfound".equals(url)) {
                notFound.add(id);
            } else if (url == null) {
                somethingWrong.add(id);
            } else {
                StartupMapper page = new StartupMapper(id, url);
                System.out.println(page);
                successfulResults.add(page);
            }

            Utils.sleep(5000);
        }

        for (StartupMapper successfulResult : successfulResults) {
            System.out.println(successfulResult);
        }

        System.out.println("NOT FOUND:");
        for (String failedId : notFound) {
            System.out.println(failedId);
        }
    }

    private String getDataFromGoogle(String query) {

        String result = null;
        String request = "https://www.google.com/search?q=" + query + "&num=5";
        System.out.println("Sending request..." + request);

        try {

            // need http protocol, set this as a Google bot agent :)
            Document doc = Jsoup
                    .connect(request)
                    .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();

            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                String temp = link.attr("href");
                if (temp.startsWith("/url?q=")) {
                    if (temp.length() > 7 && temp.contains("startup.ua")) {
                        String url = temp.substring(7);
                        return url;
                }
            }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getDuckGo(String query) {
        String request = "https://duckduckgo.com/html/?q=" + query;

        try {
            Document doc = Jsoup
                    .connect(request)
                    .timeout(5000)
                    .get();

            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                if ("nofollow".equals(link.attr("rel"))) {
                    return link.attr("href");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return "notfound";
    }


}
