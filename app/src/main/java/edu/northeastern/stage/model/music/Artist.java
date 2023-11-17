package edu.northeastern.stage.model.music;

import java.util.ArrayList;

public class Artist {
    private String href; // A link to the Web API endpoint providing full details of the artist.
    private String id;
    private ArrayList<String> genres;
    private String name;
    private String uri;
    private String spotifyUrl; // from external_urls

    public Artist(String href, String id, ArrayList<String> genres, String name, String uri, String spotifyUrl) {
        this.href = href;
        this.id = id;
        this.genres = genres;
        this.name = name;
        this.uri = uri;
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

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }
}
