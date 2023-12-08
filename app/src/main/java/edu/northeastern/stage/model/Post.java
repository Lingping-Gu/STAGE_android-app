package edu.northeastern.stage.model;

public class Post {

    private String postID;
    private String ownerID;
    private String trackName;
    private String trackID;
    private String artistName;
    private String content;
    private Long timestamp;
    private String imageURL;

    public Post(String postID, String ownerID, String trackName, String trackID, String artistName, String content,
                Long timestamp, String imageURL, String visibilityState, String spotifyURL) {
        this.postID = postID;
        this.ownerID = ownerID;
        this.trackName = trackName;
        this.trackID = trackID;
        this.artistName = artistName;
        this.content = content;
        this.timestamp = timestamp;
        this.imageURL = imageURL;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
