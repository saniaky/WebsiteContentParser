package com.saniaky;

import com.saniaky.model.BasicModel;
import com.saniaky.parser.WebGrabber;
import com.saniaky.word.WordRenderer;

import java.util.ArrayList;
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

    public static void main(String[] args) {

        // Get links
        List<String> urlsList = Utils.getLines(URLS_TXT);
        Set<String> originalLinks = new LinkedHashSet<>(urlsList);

        // Remove duplicates
        List<String> urls = new ArrayList<>(originalLinks);
        if (originalLinks.size() != urls.size()) {
            System.out.println("Warning! Found " + (originalLinks.size() - urls.size()) + " duplicates!");
        }

        WordRenderer wordRenderer = new WordRenderer();
        List<BasicModel> articles = new LinkedList<>();

        // Batch parse & create word file
        int batchNum = 1;
        for (String url : urls) {
            // Fetch url from website
            articles.add(WebGrabber.go(url));

            // Create word file with articles
            if (articles.size() == BATCH_SIZE) {
                System.out.println("Creating word file.");
                wordRenderer.createWord(articles, getFilename(batchNum++));
                articles.clear();
            }
        }

        if (articles.size() > 0) {
            System.out.println("Creating word file.");
            wordRenderer.createWord(articles, getFilename(batchNum));
        }
    }

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }
}
