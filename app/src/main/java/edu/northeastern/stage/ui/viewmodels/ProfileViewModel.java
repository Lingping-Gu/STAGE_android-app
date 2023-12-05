package edu.northeastern.stage.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.stage.model.Post;

public class ProfileViewModel extends ViewModel {
    private String profilePicResource;
    private List<Post> posts;
    private List<String> recentlyListenedToImageURLs;
    private List<String> tags;

    public String getProfilePicResource() {
        return profilePicResource;
    }

    public void setProfilePicResource(String profilePicResource) {
        this.profilePicResource = profilePicResource;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<String> getRecentlyListenedToImageURLs() {
        return recentlyListenedToImageURLs;
    }

    public void setRecentlyListenedToImageURLs(List<String> recentlyListenedToImageURLs) {
        this.recentlyListenedToImageURLs = recentlyListenedToImageURLs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}