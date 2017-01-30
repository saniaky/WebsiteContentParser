package com.saniaky.parser;

import com.saniaky.model.BasicModel;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public interface Fetcher {

    BasicModel parse(String url);

}
