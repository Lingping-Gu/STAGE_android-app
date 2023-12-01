package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    // Method to handle post submission logic
    public void createPost(Post post) {
        // get instance of FBDB
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        // get reference to DB
        DatabaseReference reference = mDatabase
                .getReference("users")
                .child("posts");

        // generate unique ID for post
        DatabaseReference newPostRef = reference.push();

        // add fields to post
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

        // Update the LiveData with the submission status
        // TODO: what is the purpose of this?
        postSubmissionStatus.setValue(true); // Or false if submission fails
    }


    // Method to handle search logic
    public LiveData<List<JsonObject>> performSearch(String query) {
        MutableLiveData<List<JsonObject>> searchResults = new MutableLiveData<>();

        // change numResults
        CompletableFuture<ArrayList<JsonObject>> trackSearchFuture = spotify.trackSearch(query,10);
        trackSearchFuture.thenAccept(searchResult -> {
            searchResults.postValue(searchResult);
        }).exceptionally(e -> {
            Log.e("TrackSearchError",e.getMessage());
            return null;
        });
        return searchResults;
    }

    // Getters for LiveData
    public LiveData<Boolean> getPostSubmissionStatus() {
        return postSubmissionStatus;
    }
}