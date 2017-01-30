package com.saniaky.parser;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class GenericFetcher implements Fetcher {

    // System variables
    private static final int TIMEOUT = 1000;
    private static final int SLEEP_TIMEOUT = 5000;
    private static final int MAX_FAILED_COUNT = 20;
    private static final int RESOLVE_TIMEOUT = 30000;

    private HtmlFetcher fetcher = new HtmlFetcher();

    public BasicModel parse(String url) {
        BasicModel result = null;
        int failedCount = 0;

        while (true) {
            try {
                if (url.contains("firrma.ru")) {
                    Utils.sleep(TIMEOUT);
                }

                JResult jResult = fetcher.fetchAndExtract(url, RESOLVE_TIMEOUT, true);
                if (jResult.getTitle().isEmpty()) {
                    System.err.println("Пропущена статья - " + url);
                } else {
                    result = new BasicModel(
                            jResult.getUrl(), jResult.getTitle(), jResult.getText(),
                            jResult.getImageUrl(), jResult.getKeywords());
                }

                break;

            } catch (Exception e) {
                if (e.getMessage().equals("Invalid Http response") && failedCount < MAX_FAILED_COUNT) {
                    Utils.sleep(SLEEP_TIMEOUT);
                    failedCount++;
                } else {
                    System.err.println("Пропущена статья - " + url + ", Error: " + e.getMessage());
                    break;
                }
            }
        }

        return result;
    }


}
