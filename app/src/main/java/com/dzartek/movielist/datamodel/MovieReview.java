package com.dzartek.movielist.datamodel;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class MovieReview {
    private String author;
    private String content;

    public MovieReview(){}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
