package com.saniaky.parser;

import com.saniaky.model.BasicModel;

/**
 * @author saniaky
 * @since 12/4/16
 */
public class WebGrabber {

    public static BasicModel go(String url) {
        Fetcher fetcher = FetcherFactory.getFetcher(url);
        return fetcher.parse(url);
    }

}
