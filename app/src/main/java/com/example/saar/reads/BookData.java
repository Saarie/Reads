package com.example.saar.reads;

/**
 * Created by Saar on 16-12-2016.
 */

public class BookData {
    // ArtData object contains these 'columns'
    private String plot;
    private String imgUrl;
    private String id;
    private String author;
    private String title;
    private Double rating;

    // Empty constructor
    public BookData() {}

    // Initialize object
    public BookData(String id, String title, String author, String plot, String imgUrl, Double rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.imgUrl = imgUrl;
        this.rating = rating;
    }

    // Getters and setters
    public String getPlot() {
        return plot;
    }

    public void setPlot(String newPlot) {
        this.plot = newPlot;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String newImgUrl) {
        this.imgUrl = newImgUrl;
    }

    public String getID() {
        return id;
    }

    public void setID(String newID) {
        this.id = newID;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String newAuthor){
        this.author = newAuthor;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public Double getRating(){
        return rating;
    }

    public void setRating(Double newRating){
        this.rating = newRating;
    }
}
