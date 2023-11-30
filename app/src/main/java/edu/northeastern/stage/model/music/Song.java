package edu.northeastern.stage.model.music;

import java.util.ArrayList;
import java.util.List;

public class Song {
    private String name;
    private String id;
    private int durationMs;
    private boolean explicit;
    private ArrayList<String> availableMarkets;
    private String previewUrl;
    private int popularity;
    private int trackNumber;
    private Album album;
    private ArrayList<Artist> artists;
    private String spotifyUrl; // from external_urls
    private String href; // A link to the Web API endpoint providing full details of the track.
    private String uri;
    private boolean isPlayable;
    private LinkedTrack linkedFrom;

    public Song(String name) {
      this.name = name;
    }
    public Song(String name, String id, int durationMs, boolean explicit, ArrayList<String> availableMarkets,
                String previewUrl, int popularity, int trackNumber, Album album, ArrayList<Artist> artists,
                String spotifyUrl, String href, String uri, boolean isPlayable, LinkedTrack linkedFrom) {
        this.name = name;
        this.id = id;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.availableMarkets = availableMarkets;
        this.previewUrl = previewUrl;
        this.popularity = popularity;
        this.trackNumber = trackNumber;
        this.album = album;
        this.artists = artists;
        this.spotifyUrl = spotifyUrl;
        this.href = href;
        this.uri = uri;
        this.isPlayable = isPlayable;
        this.linkedFrom = linkedFrom;
    }

    public String getAlbumName() {
        return album.getName();
    }

    public List<String> getArtistNames() {
        List<String> artistNames = new ArrayList<>();
        for (Artist artist : artists) {
            artistNames.add(artist.getName());
        }
        return artistNames;
    }

    public List<String> getArtistGenres() {
        List<String> artistGenres = new ArrayList<>();
        for (Artist artist : artists) {
            artistGenres.add(artist.getName());
        }
        return artistGenres;
    }

    public String getReleaseDate() {
        return album.getReleaseDate();
    }

    public String getPreciseReleaseDate() {
        return album.getReleaseDatePrecision();
    }
  
    // to be removed later - should be able to update all the methods with getName
    public String getTitle(){
      return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public ArrayList<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(ArrayList<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
    }

    public LinkedTrack getLinkedFrom() {
        return linkedFrom;
    }

    public void setLinkedFrom(LinkedTrack linkedFrom) {
        this.linkedFrom = linkedFrom;
    }

    // LinkedTrack class
    public static class LinkedTrack {
        private String id;
        private String href;
        private String uri;
        private String spotifyUrl;

        public LinkedTrack(String id, String href, String uri, String spotifyUrl) {
            this.id = id;
            this.href = href;
            this.uri = uri;
            this.spotifyUrl = spotifyUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
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
}
