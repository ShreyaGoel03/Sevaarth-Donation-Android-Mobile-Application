package com.example.sevaarth;

public class Rating {
    String date;
    float rating;
    public Rating(){

    }
    public Rating(String date, float rating) {
        this.date = date;
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
