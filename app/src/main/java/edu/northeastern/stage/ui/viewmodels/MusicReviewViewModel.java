package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.model.Review;
import edu.northeastern.stage.model.music.Track;

public class MusicReviewViewModel extends ViewModel {
    private MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private Track track;
    private String userID;

    // Method to fetch all reviews for track
    public void fetchReviews() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference userRef = rootRef.child("users");

        List<Review> currentReviews = new ArrayList<>();

        Query reviewQuery = userRef.orderByChild("reviews/trackID").equalTo(track.getId());
        reviewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = new Review(reviewSnapshot.child("userID").getValue().toString(),
                            reviewSnapshot.child("content").getValue().toString(),
                            Float.parseFloat(reviewSnapshot.child("rating").getValue().toString()),
                            Long.parseLong(reviewSnapshot.child("timestamp").getValue().toString()),
                            reviewSnapshot.child("trackID").getValue().toString());
                    currentReviews.add(review);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setReviews(currentReviews);
    }

    // Method to add review
    public void addReview(Review review) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if (getUserID() != null) {
            // get reference to DB
            DatabaseReference reference = mDatabase
                    .getReference("users")
                    .child(getUserID())
                    .child("reviews");

            // generate unique ID for review
            DatabaseReference newReviewRef = reference.push();

            if (track != null && review != null) {
                newReviewRef.setValue(review, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Log.d("NewReview", "New review created!");
                        } else {
                            Log.d("NewReview", "New review created failed!");
                        }
                    }
                });
            }
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.setValue(reviews);
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
}
