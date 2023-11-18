package edu.northeastern.stage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseExample extends AppCompatActivity {

    Button createPostBT;
    Button createReviewBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_example);

        createPostBT = findViewById(R.id.createPostBT);
        createReviewBT = findViewById(R.id.createReviewBT);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String UID = "";
        if (currentUser != null) {
            // if logged in
            UID = currentUser.getUid();
        } else {
            UID = "TEST";
        }

        String trackID = "1234";
        String content = "Hello there!";
        String user = UID;
        Long timeStamp = System.currentTimeMillis();
        ArrayList<String> likes = new ArrayList<>();
        Integer rating = 5;

        Post newPost = new Post(trackID,content,user,timeStamp,likes);
        Review newReview = new Review(trackID, content, user, timeStamp, rating);

        String finalUID = UID;
        createPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost(newPost, finalUID);
            }
        });

        createReviewBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReview(newReview, finalUID);
            }
        });

    }

    public void createPost(Post post, String UID) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = mDatabase
                .getReference("users")
                .child(UID)
                .child("posts");

        DatabaseReference newPostRef = reference.push();
        final boolean[] successFlag = {true};

        newPostRef.setValue(post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    successFlag[0] = false;
                }
            }
        });
        if(successFlag[0]) {
            // success
            Log.d("NewPost","New post created!");
        } else {
            Log.e("NewPost","New post not created!");
        }
    }

    public void createReview(Review review, String UID) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = mDatabase
                .getReference("users")
                .child(UID)
                .child("reviews");

        DatabaseReference newReviewRef = reference.push();
        final boolean[] successFlag = {true};

        newReviewRef.setValue(review, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    successFlag[0] = false;
                }
            }
        });
        if(successFlag[0]) {
            // success
            Log.d("NewReview","New review created!");
        } else {
            Log.e("NewReview","New review not created!");
        }
    }
}