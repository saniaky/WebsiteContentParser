package com.saniaky;

import com.saniaky.model.GrabberResult;
import com.saniaky.parser.WebGrabber;
import com.saniaky.word.WordRenderer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

        // Batch parse & create word file
        for (int i = 0; i * BATCH_SIZE < urls.size(); i++) {
            List<String> batchUrls = urls.subList(i, Math.min(urls.size(), i + BATCH_SIZE));

            // Fetch data from websites
            int batchNum = i + 1;
            System.out.println("Fetching " + batchNum + " batch.");
            GrabberResult result = WebGrabber.go(batchUrls);

            // Create word file with articles
            System.out.println("Creating word file.");
            wordRenderer.createWord(result.getProcessedArticles(), getFilename(batchNum));
        }
    }

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }
}
