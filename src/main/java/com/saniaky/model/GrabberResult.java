package com.saniaky.model;

import java.util.List;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class GrabberResult {

    private List<BasicModel> processedArticles;
    private List<String> failedUrls;

    public GrabberResult() {
    }

    public GrabberResult(List<BasicModel> processedArticles, List<String> failedUrls) {
        this.processedArticles = processedArticles;
        this.failedUrls = failedUrls;
    }

    public List<BasicModel> getProcessedArticles() {
        return processedArticles;
    }

    public void setProcessedArticles(List<BasicModel> processedArticles) {
        this.processedArticles = processedArticles;
    }

    public List<String> getFailedUrls() {
        return failedUrls;
    }

    public void setFailedUrls(List<String> failedUrls) {
        this.failedUrls = failedUrls;
    }
}
