package com.saniaky.parser;

import com.saniaky.model.BasicModel;
import com.saniaky.util.Utils;
import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class GenericFetcher implements Fetcher {

    // System variables
    private static final int SLEEP_TIMEOUT = 5000;
    private static final int MAX_FAILED_COUNT = 20;
    private static final int RESOLVE_TIMEOUT = 30000;

    private HtmlFetcher fetcher = new HtmlFetcher();

    @Override
    public String url() {
        return null;
    }

    public BasicModel parse(String url) {
        BasicModel result = null;
        int failedCount = 0;

        while (failedCount < MAX_FAILED_COUNT) {
            try {
                JResult jResult = fetcher.fetchAndExtract(url, RESOLVE_TIMEOUT, true);
                if (!jResult.getTitle().isEmpty()) {
                    result = new BasicModel(
                            jResult.getUrl(), jResult.getTitle(), jResult.getText(),
                            jResult.getImageUrl(), jResult.getKeywords());
                }
            } catch (Exception e) {
                if (e.getMessage().equals("Invalid Http response")) {
                    failedCount++;
                    Utils.sleep(SLEEP_TIMEOUT);
                } else {
                    System.err.println("Пропущена статья - " + url + ", Error: " + e.getMessage());
                    break;
                }
            }
        }

        return result;
    }


}
