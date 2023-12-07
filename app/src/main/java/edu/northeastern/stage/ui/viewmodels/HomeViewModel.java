package edu.northeastern.stage.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.northeastern.stage.model.Post;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Post>> posts;
    private String currentUserId;

    public HomeViewModel() {
        posts = new MutableLiveData<>();
        loadPosts();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    // TODO: check if it's sufficient to filter posts in PostAdapter
    private void loadPosts() {

        List<Post> homePosts = new ArrayList<>();

        if(currentUserId != null && !currentUserId.isEmpty()) {
            // if currentUserId is not empty and not null
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

            DatabaseReference reference = mDatabase.getReference("users");
            // get us
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot postSnapshot : snapshot.child("posts").getChildren()) {
                        Post post = new Post(postSnapshot.child("postID").getValue(String.class),
                                postSnapshot.child("ownerID").getValue(String.class),
                                postSnapshot.child("trackName").getValue(String.class),
                                postSnapshot.child("trackID").getValue(String.class),
                                postSnapshot.child("artistName").getValue(String.class),
                                postSnapshot.child("content").getValue(String.class),
                                Long.parseLong(postSnapshot.child("timestamp").getValue(String.class)),
                                postSnapshot.child("imageURL").getValue(String.class),
                                postSnapshot.child("visibilityState").getValue(String.class),
                                postSnapshot.child("spotifyURL").getValue(String.class));
                        homePosts.add(post);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Collections.sort(homePosts,new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {
                    return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                }
            });
            posts.setValue(homePosts);
        } else {
            // currentUser does not exist
        }
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}