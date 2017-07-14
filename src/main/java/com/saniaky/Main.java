package com.saniaky;

import com.saniaky.model.BasicModel;
import com.saniaky.parser.WebGrabber;
import com.saniaky.util.Utils;
import com.saniaky.word.WordRenderer;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class Main {

    private static final String URLS_TXT = "urls.txt";
    private static final int BATCH_SIZE = 100;
    private static final WordRenderer WORD = new WordRenderer();

    public static void main(String[] args) {

        // Get links
        final List<String> urls = Utils.getLines(URLS_TXT);

        // Remove duplicates
        final Set<String> uniqueLinks = new LinkedHashSet<>(urls);
        if (uniqueLinks.size() != urls.size()) {
            System.err.println("Warning! Found " + (urls.size() - uniqueLinks.size()) + " duplicates!");
        } else {
            System.out.println("No duplicates found.");
        }

        // Batch parse & create word file
        List<BasicModel> articles = new LinkedList<>();
        int batchNum = 1;


        for (String url : uniqueLinks) {
            // Fetch url from website
            BasicModel article = WebGrabber.go(url);

            if (article != null) {
                articles.add(article);
                System.out.println(String.format("%s (%s)", article.getTitle(), article.getUrl()));
            } else {
                System.err.println(String.format("*** Article missed: %s", url));
            }

            // Create word file with articles
            if (articles.size() == BATCH_SIZE) {
                System.out.println("Creating word file.");
                WORD.createWord(articles, getFilename(batchNum++));
                articles.clear();
            }
        }

        if (articles.size() > 0) {
            System.out.println("Creating word file.");
            WORD.createWord(articles, getFilename(batchNum));
        }
    }

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }
}
