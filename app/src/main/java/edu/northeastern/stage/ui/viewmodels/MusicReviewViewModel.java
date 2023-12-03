package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.stage.model.Review;

public class MusicReviewViewModel extends ViewModel {
    private MutableLiveData<List<Review>> reviews;
    private String userID;
    private JsonObject selectedTrack;

    public MusicReviewViewModel() {
        reviews = new MutableLiveData<>();
        reviews.setValue(new ArrayList<>());
        fetchReviews(); // do this to get all reviews
    }

    // Method to add a review, simulate fetching data
    public void fetchReviews() {
        // TODO: here add the FB DB logic to get all reviews
        // replace by actual data retrieval logic
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 3.2f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.8f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 3.9f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "This album is great!", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "last", 4.5f));
        reviewList.add(new Review("user123", "uri_to_avatar", "last+1", 4.5f));
        // ... add more reviews
        reviews.setValue(reviewList);
    }

    // Method to calculate the overall rating
    public float calculateOverallRating() {
        List<Review> reviewList = reviews.getValue();
        if (reviewList == null || reviewList.isEmpty()) {
            return 0; // Return 0 if there are no reviews
        }
        float totalRating = 0;
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }
        return totalRating / reviewList.size();
    }

    public void addReview(String content) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if (getUserID() != null && getSelectedTrack() != null) {
            // get reference to DB
            DatabaseReference reference = mDatabase
                    .getReference("users")
                    .child(getUserID())
                    .child("reviews");

            // generate unique ID for review
            DatabaseReference newReviewRef = reference.push();

            //


            List<Review> currentReviews = reviews.getValue();
            if(currentReviews == null) {
                currentReviews = new ArrayList<>();
            }
//            currentReviews.add(review);
            setReviews(currentReviews);
        }
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.setValue(reviews);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public JsonObject getSelectedTrack() {
        return selectedTrack;
    }

    public void setSelectedTrack(JsonObject selectedTrack) {
        this.selectedTrack = selectedTrack;
    }
}
