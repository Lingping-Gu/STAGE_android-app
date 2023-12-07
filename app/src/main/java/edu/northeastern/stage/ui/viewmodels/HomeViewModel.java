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
import java.util.List;

import edu.northeastern.stage.model.Post;

public class HomeViewModel extends ViewModel {

//    private MutableLiveData<List<Post>> posts;
//    private String currentUserId;
//
//    public HomeViewModel() {
//        posts = new MutableLiveData<>();
//        loadPosts();
//    }
//
//    public LiveData<List<Post>> getPosts() {
//        return posts;
//    }
//
//    private void loadPosts() {
//
//        List<Post> userPosts = new ArrayList<>();
//
//        if(currentUserId != null && !currentUserId.isEmpty()) {
//            // if currentUserId is not empty and not null
//        } else {
//            // currentUserId exists
//            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//
//            DatabaseReference reference = mDatabase
//                    .getReference("users")
//                    .child(currentUserId)
//                    .child("posts");
//            reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for(DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        Post post = new Post(postSnapshot.child("postID").toString(),
//                                postSnapshot.child("ownerID").toString(),
//                                postSnapshot.child("trackName").toString(),
//                                postSnapshot.child("trackID").toString(),
//                                postSnapshot.child("artistName").toString(),
//                                postSnapshot.child("content").toString(),
//                                Long.parseLong(postSnapshot.child("timestamp").toString()),
//                                postSnapshot.child("imageURL").toString(),
//                                postSnapshot.child("visibilityState").toString(),
//                                postSnapshot.child("spotifyURL").toString());
//                        userPosts.add(post);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            posts.setValue(userPosts);
//        }
//    }
//
//    public String getCurrentUserId() {
//        return currentUserId;
//    }
//
//    public void setCurrentUserId(String currentUserId) {
//        this.currentUserId = currentUserId;
//    }
}