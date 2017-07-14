package com.saniaky.util;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public final class Utils {

    private static final int TIMEOUT = 5000;
    private static final int WAIT_TIMEOUT = 10000;
    private static final int MAX_TRY_COUNT = 30;

    public static String formatHTMLToText(String str) {
        // b ->
        // </br> -> \n

        str = str.replaceAll("</br>", "\n");
        str = str.replaceAll("<br>", "\n");
        str = str.replaceAll("</p>", "\n");
        str = str.replaceAll("</div>", "\n");
        str = str.replaceAll("<hr>", "\n");
        str = str.replaceAll("</h1>", "\n");
        str = str.replaceAll("</h2>", "\n");
        str = str.replaceAll("</h3>", "\n");
        str = str.replaceAll("</h4>", "\n");
        str = str.replaceAll("</h5>", "\n");
        str = str.replaceAll("</h5>", "\n");
        str = removeHTMLTags(str);
        return removeDuplicatedNewlines(str);
    }

    private static String removeDuplicatedNewlines(String str) {
        return str.replaceAll("[\r\n]+", "\n").replaceAll("\n *", "\n").replaceAll("[\n]+", "\n");
    }

    private static String removeHTMLTags(String html) {
        // Jsoup.clean(article, Whitelist.none());
        return html.replaceAll("<[^>]*>", "")       // html tags
                .replaceAll("&nbsp;", "")           // &nbsp chars
                .replaceAll("\\[[^\\]]*\\]", "");   // evertying inside []
    }

    public static String replaceParagraphWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("p")) {
            b.append(p.text());
            b.append(System.getProperty("line.separator"));
        }
        return b.toString();
    }

    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public static Document getDocument(String url) {
        int tryCount = 0;
        String lastErrorMessage = "";

        while (tryCount < MAX_TRY_COUNT) {
            Document document = null;

            try {
                url = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
                document = Jsoup.connect(url).timeout(TIMEOUT).userAgent(HttpConnection.DEFAULT_UA).get();
            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 503) {
                    System.err.println(String.format("%d/%d, Error 503. Trying to load again.", tryCount, MAX_TRY_COUNT));
                    Utils.sleep(WAIT_TIMEOUT);
                } else {
                    lastErrorMessage = "Page not found! (404)";
                    tryCount = MAX_TRY_COUNT;
                }
            } catch (Exception e) {
                lastErrorMessage = "Can't retrieve page! Details: " + e.getLocalizedMessage();
                Utils.sleep(WAIT_TIMEOUT);
            }

            if (document != null) {
                return document;
            }

            tryCount++;
        }

        System.err.println(lastErrorMessage);

        return null;
    }

    public static List<String> getLines(String fileName) {
        URL resource = getResourceUrl(fileName);

        if (resource == null) {
            System.out.println("Файл со ссылками не найден!");
            return Collections.emptyList();
        }

        try {
            return Files.readAllLines(new File(resource.getFile()).toPath());
        } catch (IOException e) {
            System.err.println("Не получается прочитать файл со ссылками - " + e.getMessage());
        }

        return Collections.emptyList();
    }

    public static void writeToFile(String fileName, List<String> strings) {
        try {
            Files.write(Paths.get("allUrls.txt"), strings);
        } catch (IOException e) {
            System.err.println("Can't write to file. Details: " + e.getMessage());
        }
    }

    private static URL getResourceUrl(String fileName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        return classLoader.getResource(fileName);
    }


    public static void removeFile(String path) {
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            System.err.println("Can't delete temp file");
        }
    }
}
