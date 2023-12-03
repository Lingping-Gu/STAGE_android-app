package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.stage.API.Spotify;
import edu.northeastern.stage.model.Post;

public class NewPostViewModel extends ViewModel {
    // LiveData for observing post submission status
    private final MutableLiveData<Boolean> postSubmissionStatus = new MutableLiveData<>();
    // Initiate Spotify object
    private Spotify spotify = new Spotify();
    // Initialize User ID
    private String userID = "";

    // Method to handle post submission logic
    public void createPost(JsonObject selectedTrack, String postContent) {
        // get instance of Firebase DB
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Log.d("ABC123",userID);

        if (getUserID() != null) {
            // get reference to DB
            DatabaseReference reference = mDatabase
                    .getReference("users")
                    .child(getUserID())
                    .child("posts");

            // generate unique ID for post
            DatabaseReference newPostRef = reference.push();

            // Set Post object
            if (selectedTrack != null && selectedTrack.size() > 0 && !postContent.isEmpty()) {
                String trackName = selectedTrack.get("name").getAsString();
                String trackID = selectedTrack.get("id").getAsString();
                String artistName = "";
                String content = postContent;
                Long timestamp = System.currentTimeMillis();
                String imageURL = "";

                // get all artists
                JsonArray artistsArray = selectedTrack.getAsJsonArray("artists");
                if (artistsArray != null && artistsArray.size() > 0) {
                    for (JsonElement artist : artistsArray) {
                        artistName = artistName + artist.getAsJsonObject().get("name").getAsString() + " ";
                    }
                    artistName.trim();
                }

                // get first album object image
                JsonObject albumObject = selectedTrack.getAsJsonObject("album");
                if (albumObject != null) {
                    JsonArray imagesArray = albumObject.getAsJsonArray("images");
                    if (imagesArray != null && imagesArray.size() > 0) {
                        imageURL = imagesArray.get(0).getAsJsonObject().get("url").getAsString();
                    }
                }

                Post post = new Post(trackName, trackID, artistName, content, timestamp, imageURL);
                newPostRef.setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Log.d("NewPost", "New post created!");
                        } else {
                            Log.d("NewPost", "New post created failed!");
                        }
                    }
                });
            }
        }
        // Update the LiveData with the submission status
        // TODO: what is the purpose of this?
        postSubmissionStatus.setValue(true); // Or false if submission fails
    }

    // Method to handle search logic
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

    // Getters for LiveData
    public LiveData<Boolean> getPostSubmissionStatus() {
        return postSubmissionStatus;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

}