package com.example.moetaz.movieapp.models;

/**
 * Created by Moetaz on 11/10/2016.
 */

public class ReviewModel {
    private String author, content, url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {

        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
