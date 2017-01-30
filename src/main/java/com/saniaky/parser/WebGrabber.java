package com.saniaky.parser;

import com.saniaky.model.BasicModel;
import com.saniaky.model.GrabberResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saniaky
 * @since 12/4/16
 */
public class WebGrabber {

    public GrabberResult go(List<String> urls) {

        List<BasicModel> articles = new ArrayList<>(urls.size());
        List<String> failedUrls = new ArrayList<>();

        for (String url : urls) {
            // Get appropriate website fetcher
            Fetcher fetcher = FetcherFactory.getFetcher(url);

            // Parse page
            BasicModel article = fetcher.parse(url);

            // Check if page parsed successfully
            if (article == null) {
                failedUrls.add(url);
                System.err.println(String.format("*** Пропущена статья: %s", url));

            } else {
                articles.add(article);
                System.out.println(String.format("%s (%s)", article.getTitle(), article.getUrl()));
            }

        }

        return new GrabberResult(articles, failedUrls);
    }

}
