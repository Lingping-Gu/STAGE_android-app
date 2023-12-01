package edu.northeastern.stage.model.music;

import java.util.ArrayList;
import java.util.List;

public class Track {
    private Album album;
    private ArrayList<Artist> artists;
    private int durationMs;
    private String spotifyUrl; // from external_urls
    private String id;
    private String name;
    private int popularity;

    public Track(String name) {
        this.name = name;
    }
    public Track(Album album, ArrayList<Artist> artists, int durationMs, String spotifyUrl, String id, String name, int popularity) {
        this.album = album;
        this.artists = artists;
        this.durationMs = durationMs;
        this.spotifyUrl = spotifyUrl;
        this.id = id;
        this.name = name;
        this.popularity = popularity;
    }

    public Album getAlbum() {
        return album;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}

