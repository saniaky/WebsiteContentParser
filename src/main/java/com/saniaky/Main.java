package com.saniaky;

import com.saniaky.model.GrabberResult;
import com.saniaky.parser.WebGrabber;
import com.saniaky.word.DocumentRenderer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class Main {

    private static final String URLS_TXT = "urls.txt";

    public static void main(String[] args) {

        // Get links
        List<String> urlsList = new Main().getLines(URLS_TXT);
        Set<String> originalLinks = new LinkedHashSet<>(urlsList);

        // Remove duplicates
        List<String> urls = new ArrayList<>(originalLinks);
        if (originalLinks.size() != urls.size()) {
            System.out.println("Warning! Found " + (originalLinks.size() - urls.size()) + " duplicates!");
        }

        // Start parsing
        GrabberResult result = new WebGrabber().go(urls);

        // Create word file with articles
        new DocumentRenderer().createWord(result.getProcessedArticles());
    }

    public List<String> getLines(String fileName) {
        URL resource = getResourceUrl(fileName);

        if (resource == null) {
            System.out.println("Файл со ссылками не найден!");
            return Collections.emptyList();
        }

        try {
            return Files.readAllLines(new File(resource.getFile()).toPath());
        } catch (IOException e) {
            System.out.println("Не получается прочитать файл со ссылками - " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private URL getResourceUrl(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(fileName);
    }


}
