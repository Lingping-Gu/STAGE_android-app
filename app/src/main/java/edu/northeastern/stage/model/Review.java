package edu.northeastern.stage.model;

import androidx.annotation.NonNull;

public class Review {
    private String userID;
    private String content;
    private float rating;
    private Long timestamp;
    private String trackID;

    // Constructor
    public Review(String userID, String content, float rating, Long timestamp, String trackID) {
        this.userID = userID;
        this.content = content;
        this.rating = rating;
        this.timestamp = timestamp;
        this.trackID = trackID;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

