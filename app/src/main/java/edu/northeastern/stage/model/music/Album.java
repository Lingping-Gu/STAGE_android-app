package edu.northeastern.stage.model.music;

import java.util.ArrayList;

public class Album {
    private String albumType;
    private int totalTracks;
    private ArrayList<String> availableMarkets;
    private String spotifyUrl; // from external_urls
    private String href; // A link to the Web API endpoint providing full details of the album.
    private String id;
    private String name;
    private String releaseDate;
    private String releaseDatePrecision;
    private String uri;
    private ArrayList<Artist> artists;
    private String imageURL;
    //    private ArrayList<Image> images; use this if creating a separate image class.

    public Album(String albumType, int totalTracks, ArrayList<String> availableMarkets,
                 String spotifyUrl, String href, String id, String name, String releaseDate,
                 String releaseDatePrecision, String uri, ArrayList<Artist> artists, String imageURL) {
        this.albumType = albumType;
        this.totalTracks = totalTracks;
        this.availableMarkets = availableMarkets;
        this.spotifyUrl = spotifyUrl;
        this.href = href;
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.uri = uri;
        this.artists = artists;
        this.imageURL = imageURL;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public ArrayList<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(ArrayList<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}