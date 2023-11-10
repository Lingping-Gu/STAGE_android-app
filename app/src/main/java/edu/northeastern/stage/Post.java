package edu.northeastern.stage;

public class Post {
    private String musicLink;
    private String postContent;
    private int userAvatar; // Resource ID for the user avatar image
    private boolean isLiked; // Status of the 'like'
    private int visibilityState; // Resource ID for the visibility state image

    // Constructor
    public Post(String musicLink, String postContent, int userAvatar, boolean isLiked, int visibilityState) {
        this.musicLink = musicLink;
        this.postContent = postContent;
        this.userAvatar = userAvatar;
        this.isLiked = isLiked;
        this.visibilityState = visibilityState;
    }

    // Getters and Setters
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

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getVisibilityState() {
        return visibilityState;
    }

    public void setVisibilityState(int visibilityState) {
        this.visibilityState = visibilityState;
    }
}
