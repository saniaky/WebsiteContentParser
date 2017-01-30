package com.saniaky.model;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class StartupMapper {


    private String id;
    private String url;

    public StartupMapper(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("id: %s, url: %s", id, url);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
