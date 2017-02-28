package com.saniaky;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public final class Utils {

    private static final int TIMEOUT = 3000;

    public static String replaceParagraphWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("p")) {
            b.append(p.text());
            b.append(System.getProperty("line.separator"));
            b.append(System.getProperty("line.separator"));
            b.append("    ");
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

        try {
            return Jsoup.connect(url).timeout(TIMEOUT).get();
        } catch (IOException ignored) {
            System.err.println("Can't retrieve page! Details: " + ignored.getLocalizedMessage());
        }

        return null;
    }

}
