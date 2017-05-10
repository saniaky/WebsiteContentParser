package com.saniaky;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public final class Utils {

    private static final int TIMEOUT = 5000;
    private static final int WAIT_TIMEOUT = 10000;
    private static final int MAX_TRY_COUNT = 30;

    public static String formatHTMLToText(String str) {
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

    public static String removeHTMLTags(String html) {
        // Jsoup.clean(article, Whitelist.none());
        return html.replaceAll("<[^>]*>", "")       // html tags
                .replaceAll("&nbsp;", "")           // &nbsp chars
                .replaceAll("\\[[^\\]]*\\]", "");   // evertying inside []
    }

    public static String removeContentInTage(String html, String tag) {
        // Jsoup.clean(article, Whitelist.none());
        return html.replaceAll("\\[[^\\]]*\\]", "");   // evertying inside []
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

    public static String replaceDivWithNewLines(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("div")) {
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
                document = Jsoup.connect(url).timeout(TIMEOUT).get();
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
}
