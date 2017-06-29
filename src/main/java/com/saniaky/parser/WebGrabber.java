package com.saniaky.parser;

import com.saniaky.model.BasicModel;

/**
 * @author saniaky
 * @since 12/4/16
 */
public class WebGrabber {

    public static BasicModel go(String url) {

        // Get appropriate website fetcher
        Fetcher fetcher = FetcherFactory.getFetcher(url);

        // Parse page
        BasicModel article = fetcher.parse(url);

        // Check if page parsed successfully
        if (article == null) {
            System.err.println(String.format("*** Article missed: %s", url));
        } else {
            System.out.println(String.format("%s (%s)", article.getTitle(), article.getUrl()));
        }


        return article;
    }

}
