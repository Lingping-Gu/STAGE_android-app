package edu.northeastern.stage.model;

public class Post {
    private String userAvatarUrl;
    private String musicLink;
    private String musicImageUrl;
    private String trackName;
    private String artistName;
    private String postContent;
    private boolean isLiked;
    private String visibilityState;
    private Long timestamp;

    public Post(String userAvatarUrl, String musicLink, String musicImageUrl, String trackName,
                String artistName, String postContent, boolean isLiked, String visibilityState, Long timestamp) {
        this.userAvatarUrl = userAvatarUrl;
        this.musicLink = musicLink;
        this.musicImageUrl = musicImageUrl;
        this.trackName = trackName;
        this.artistName = artistName;
        this.postContent = postContent;
        this.isLiked = isLiked;
        this.visibilityState = visibilityState;
        this.timestamp = timestamp;
    }

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

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getVisibilityState() {
        return visibilityState;
    }

    public void setVisibilityState(String visibilityState) {
        this.visibilityState = visibilityState;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
