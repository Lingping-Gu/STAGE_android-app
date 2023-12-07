package edu.northeastern.stage.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.stage.API.Spotify;
import edu.northeastern.stage.model.Circle;
import edu.northeastern.stage.R;
import edu.northeastern.stage.model.music.Album;
import edu.northeastern.stage.model.music.Artist;
import edu.northeastern.stage.model.music.Track;
import edu.northeastern.stage.ui.explore.CircleView;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<List<JsonObject>> recommendations = new MutableLiveData<>();
    private String track;
    private Spotify spotify = new Spotify();
    private static final Random rand = new Random();
    CircleView circleView;
    Map<Circle, String> circleTextMap = new HashMap<>();
    List<Circle> circles;

    public Map<String,Integer> getTracksNearby(Integer radius) {
        Map<String,Integer> tracksFrequency = new HashMap<>();
        // query all users that are within a x mile radius
        // among those users compile a whole list of all songs they posted about
        // create a map of all these songs to keep track of frequency Key trackID Value frequency
        // return this map

        // in the fragment, when a circle is clicked, get entire track JsonElement by API call
        // then, store this in the shared view model and convert the jsonelement to Track object and store that in shared view model

        return tracksFrequency;
    }

    public LiveData<List<JsonObject>> performSearch(String query) {
        MutableLiveData<List<JsonObject>> searchResults = new MutableLiveData<>();

        // change numResults
        CompletableFuture<ArrayList<JsonObject>> trackSearchFuture = spotify.trackSearch(query, 10);
        trackSearchFuture.thenAccept(searchResult -> {
            searchResults.postValue(searchResult);
        }).exceptionally(e -> {
            Log.e("TrackSearchError", e.getMessage());
            return null;
        });
        return searchResults;
    }

    // method to create Track object based on the selectedTrack JsonObject from Spotify API
    public Track createTrack(JsonObject selectedTrack) {
        // album variables
        String albumURL = selectedTrack.get("album").getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
        String albumID = selectedTrack.get("album").getAsJsonObject().get("id").getAsString();
        String albumImageURL = selectedTrack.get("album").getAsJsonObject().getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
        String albumName = selectedTrack.get("album").getAsJsonObject().get("name").getAsString();
        String albumReleaseDate = selectedTrack.get("album").getAsJsonObject().get("release_date").getAsString();
        String albumReleaseDatePrecision = selectedTrack.get("album").getAsJsonObject().get("release_date_precision").getAsString();
        JsonArray albumArtistsJsonArray = selectedTrack.get("album").getAsJsonObject().getAsJsonArray("artists");
        ArrayList<Artist> albumArtists = new ArrayList<Artist>();
        for(JsonElement artist : albumArtistsJsonArray) {
            Artist artistToAdd = new Artist(artist.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString(),
                    artist.getAsJsonObject().get("id").getAsString(),artist.getAsJsonObject().get("name").getAsString());
            albumArtists.add(artistToAdd);
        }

        // track variables
        Album album = new Album(albumURL, albumID, albumImageURL, albumName, albumReleaseDate, albumReleaseDatePrecision, albumArtists);
        JsonArray trackArtistsJsonArray = selectedTrack.getAsJsonArray("artists");
        ArrayList<Artist> trackArtists = new ArrayList<Artist>();
        for(JsonElement artist : trackArtistsJsonArray) {
            Artist artistToAdd = new Artist(artist.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString(),
                    artist.getAsJsonObject().get("id").getAsString(),artist.getAsJsonObject().get("name").getAsString());
            trackArtists.add(artistToAdd);
        }
        int durationMs = selectedTrack.get("duration_ms").getAsInt();
        String spotifyURL = selectedTrack.get("external_urls").getAsJsonObject().get("spotify").getAsString();
        String trackID = selectedTrack.get("id").getAsString();
        String trackName = selectedTrack.get("name").getAsString();
        int popularity = selectedTrack.get("popularity").getAsInt();
        return new Track(album,trackArtists,durationMs,spotifyURL,trackID,trackName,popularity);
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;

    }

    public MutableLiveData<List<JsonObject>> getRecommendations() {
        return recommendations;
    }

    public void setCircles(CircleView circleView) {
        // Process the circles as needed in the ViewModel
        this.circleView = circleView;
        createCircles();
    }

    public List<Circle> createCircles() {
        circles = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = 100000; // Limit the number of attempts to avoid infinite loop
        int MIN_DISTANCE_THRESHOLD = 10;

        while (circles.size() < 20 && attempts < maxAttempts) {
            float x = rand.nextFloat() * 2000 - 1000; //-1000 to 1000
            float y = rand.nextFloat() * 2000 - 1000;
            float radius = rand.nextFloat() * 200 + 100;

            // Ensure the newly created circle doesn't overlap with existing circles
            boolean isOverlapping = false;
            for (Circle existingCircle : circles) {
                float distance = calculateDistance(x, y, existingCircle.getX(), existingCircle.getY());
                // add min_distance_threshold so they are bit further away from each other
                float minDistance = radius + existingCircle.getRadius() + MIN_DISTANCE_THRESHOLD;
                if (distance < minDistance) {
                    isOverlapping = true;
                    break; // This circle overlaps, generate a new one
                }
            }

            if (!isOverlapping) {
                circles.add(new Circle(x, y, radius));
            }

            attempts++;
        }

        generateCircleTexts();

        // Set the circles to the existing CircleView
        if (circleView != null) {
            circleView.setCircles(circles, (HashMap<Circle, String>) circleTextMap);
            circleView.invalidate(); // Request a redraw
        }
        return circles;
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        //euclidean distance
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void generateCircleTexts(){
        String textInCircle;
        for(Circle c: circles){
            textInCircle = generateRandomText();
            // Store in map
            circleTextMap.put(c, textInCircle);
        }
    }

    // Function to generate random text
    public String generateRandomText() {
        // Replace this with your own logic to generate random text
        String[] texts = {"Text1", "Text2", "Text3", "Text4", "Text5"};
        int randomIndex = new Random().nextInt(texts.length);
        return texts[randomIndex];
    }

}
