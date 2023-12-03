package edu.northeastern.stage.model;

import java.util.ArrayList;

public class Post {
    private String user;
    private String userAvatarUrl;
    private String musicLink;
    private String musicImageUrl;
    private String trackName;
    private String artistName;
    private String postContent;
    private ArrayList<String> likes;
    private String visibilityState;

    // TODO: Simplify the Post constructor
    public Post(String userId, String userAvatarUrl, String musicLink, String postContent, ArrayList<String> likes, String visibilityState,
                String musicImageUrl, String trackName, String artistName) {
        this.user = userId;
        // user avatar, can get from usedId
        // & get user name by userId
        this.userAvatarUrl = userAvatarUrl;
        // included
        this.postContent = postContent;
        this.likes = likes;
        // need visible state
        this.visibilityState = visibilityState;
        // have track info, can get from track Id
        this.musicLink = musicLink;
        this.musicImageUrl = musicImageUrl;
        this.trackName = trackName;
        this.artistName = artistName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    // Getters and Setters
    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getMusicLink() {
        return musicLink;
    }

    public void setMusicLink(String musicLink) {
        this.musicLink = musicLink;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getVisibilityState() {
        return visibilityState;
    }

    public void setVisibilityState(String visibilityState) {
        this.visibilityState = visibilityState;
    }

    public String getMusicImageUrl() {
        return musicImageUrl;
    }

    public void setMusicImageUrl(String musicImageUrl) {
        this.musicImageUrl = musicImageUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

}
