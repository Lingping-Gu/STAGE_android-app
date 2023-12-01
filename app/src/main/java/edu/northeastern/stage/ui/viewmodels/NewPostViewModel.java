package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.stage.API.Spotify;

public class NewPostViewModel extends ViewModel {
    // LiveData for observing post submission status
    private final MutableLiveData<Boolean> postSubmissionStatus = new MutableLiveData<>();
    // Initiate Spotify object
    private Spotify spotify = new Spotify();

    // Method to handle post submission logic
    public void submitPost(String postContent) {
        // Logic to submit the post

        // Update the LiveData with the submission status
        postSubmissionStatus.setValue(true); // Or false if submission fails
    }

    // Method to handle search logic
    // TODO: need to change adapter so that each search can contain picture of album/artist
    public LiveData<List<JsonElement>> performSearch(String query) {
        MutableLiveData<List<JsonElement>> searchResults = new MutableLiveData<>();

        // change numResults
        CompletableFuture<ArrayList<Object>> trackSearchFuture = spotify.trackSearch(query,10);
        trackSearchFuture.thenAccept(searchResult -> {
            searchResults.postValue(searchResult);
        }).exceptionally(e -> {
            Log.e("TrackSearchError",e.getMessage());
            return null;
        });
        return searchResults;
    }

    private List<String> parseJson(String jsonString) {
        List<String> results = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(jsonString);
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject currentResult = data.getJSONObject(i);
                JSONObject artist = currentResult.getJSONObject("artist");
                String trackName = currentResult.getString("title");
                String artistName = artist.getString("name");
                results.add(trackName + " by " + artistName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Getters for LiveData
    public LiveData<Boolean> getPostSubmissionStatus() {
        return postSubmissionStatus;
    }
}