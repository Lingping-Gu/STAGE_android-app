package edu.northeastern.stage.model.music;

import java.util.ArrayList;

public class Album {
    private String spotifyUrl; // from external_urls
    private String id;
    private String imageURL;
    private String name;
    private String releaseDate;
    private String releaseDatePrecision;
    private ArrayList<Artist> artists;

    public Album(String spotifyUrl, String id, String imageURL, String name, String releaseDate, String releaseDatePrecision, ArrayList<Artist> artists) {
        this.spotifyUrl = spotifyUrl;
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.artists = artists;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }
}