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

    private static final int TIMEOUT = 5000;

    public static String removeDuplicatedNewlines(String str) {
        return str.replaceAll("[\r\n]+", "\n");
    }

    public static String removeHTMLTags(String html) {
        return html.replaceAll("<[^>]*>", "");
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

    public static String replaceSpanWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("span")) {
            b.append(p.text());
            b.append(System.getProperty("line.separator"));
        }
        return b.toString();
    }

    public static String replaceDivWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("div")) {
            b.append(p.text());
            b.append(System.getProperty("line.separator"));
        }
        return b.toString();
    }

    public static String replaceBrWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("br")) {
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

        try {
            return Jsoup.connect(url).timeout(TIMEOUT).get();
        } catch (IOException ignored) {
            System.err.println("Can't retrieve page! Details: " + ignored.getLocalizedMessage());
        }

        return null;
    }

}
