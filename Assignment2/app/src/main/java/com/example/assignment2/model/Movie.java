package com.example.assignment2.model;

import java.io.Serializable;
public class Movie implements Serializable {
    private String title;
    private String year;
    private String poster;
    private String imdbID;

    public Movie(String title, String year, String poster, String imdbID) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbID() {
        return imdbID;
    }
}
