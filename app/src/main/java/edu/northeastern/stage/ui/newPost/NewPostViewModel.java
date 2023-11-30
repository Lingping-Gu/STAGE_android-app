package edu.northeastern.stage.ui.newPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewPostViewModel extends ViewModel {
    // LiveData for observing post submission status
    private final MutableLiveData<Boolean> postSubmissionStatus = new MutableLiveData<>();

    // Method to handle post submission logic
    public void submitPost(String postContent) {
        // Logic to submit the post

        // Update the LiveData with the submission status
        postSubmissionStatus.setValue(true); // Or false if submission fails
    }

    // Method to handle search logic
    public LiveData<List<String>> performSearch(String query, String deezerBaseUrl, String deezerApiKey) {
        MutableLiveData<List<String>> searchResults = new MutableLiveData<>();

        new Thread(() -> {
            List<String> results = searchMusic(query, deezerBaseUrl, deezerApiKey);
            searchResults.postValue(results);
        }).start();

        return searchResults;
    }

    private List<String> searchMusic(String query, String deezerBaseUrl, String deezerApiKey) {
        List<String> searchResults = new ArrayList<>();
        try {
            URL url = new URL(deezerBaseUrl + query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("X-RapidAPI-Key", deezerApiKey);
            urlConnection.setRequestProperty("X-RapidAPI-Host", "deezerdevs-deezer.p.rapidapi.com");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            String jsonString = convertStreamToString(inputStream);
            searchResults = parseJson(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    // Getters for LiveData
    public LiveData<Boolean> getPostSubmissionStatus() {
        return postSubmissionStatus;
    }
}