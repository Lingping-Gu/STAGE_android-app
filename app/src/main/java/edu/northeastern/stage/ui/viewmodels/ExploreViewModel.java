package edu.northeastern.stage.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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
import edu.northeastern.stage.ui.explore.CircleView;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<List<JsonObject>> recommendations = new MutableLiveData<>();
    private String track;
    private Spotify spotify = new Spotify();
    private static final Random rand = new Random();
    CircleView circleView;
    Map<Circle, String> circleTextMap = new HashMap<>();
    List<Circle> circles;

    public void searchTextChanged(String text) {
        if (text.isEmpty()) {
            recommendations.setValue(new ArrayList<>());
        } else {
            Log.d("Explore View Model", "when searchTextChanged");
            getRecommendations(text);
        }
    }

    public LiveData<List<JsonObject>> getRecommendations(String query) {
        // change numResults
        CompletableFuture<ArrayList<JsonObject>> trackSearchFuture = spotify.trackSearch(query, 10);
        trackSearchFuture.thenAccept(searchResult -> {
            recommendations.postValue(searchResult);
        }).exceptionally(e -> {
            Log.e("TrackSearchError", e.getMessage());
            return null;
        });
        return recommendations;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    // TODO: maybe make circles a separate viewmodel
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

        while (circles.size() < 100 && attempts < maxAttempts) {
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
