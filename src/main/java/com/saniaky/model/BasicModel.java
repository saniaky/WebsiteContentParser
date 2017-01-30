package com.saniaky.model;

import java.util.Collection;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class BasicModel {

    private String url;
    private String title;
    private String text;
    private String imageUrl;
    private Collection<String> keywords;

    public BasicModel() {
    }

    public BasicModel(String url, String title, String text, String imageUrl, Collection<String> keywords) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.keywords = keywords;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
