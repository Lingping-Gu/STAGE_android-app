package edu.northeastern.stage.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.stage.model.Post;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<List<Post>> posts;
    private MutableLiveData<List<String>> recentListenedUrls;
    private MutableLiveData<List<String>> tags;

    public ProfileViewModel() {
        recentListenedUrls = new MutableLiveData<>();
        loadImageUrls();
        posts = new MutableLiveData<>();
        loadPosts();
        tags = new MutableLiveData<>();
        loadTags();
    }

    public LiveData<List<String>> getImageUrls() {
        return recentListenedUrls;
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
    public LiveData<List<String>> getTags() {
        return tags;
    }

    private void loadPosts() {
        // Load posts here
        // Once loaded, set them to the 'posts' LiveData
        // posts.setValue(loadedPosts);
        MutableLiveData<List<Post>> exampleData = new MutableLiveData<>();
        String musicLink = "http://open.spotify.com/track/6rqhFgbbKwnb9MLmUQDhG6";
        String musicImageUrl = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";
        Post examplePost = new Post("userAvatarUrl", musicLink, "Speak to me by Pink Floyd is amazing!", false,
                "public", musicImageUrl, "Speak to me", "Pink Floyd");
        exampleData.setValue(Arrays.asList(examplePost));
        this.posts = exampleData;
    }

    private void loadImageUrls() {
        MutableLiveData<List<String>> exampleData = new MutableLiveData<>();
        String exampleUrl = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";
        exampleData.setValue(Arrays.asList(exampleUrl));
        this.recentListenedUrls = exampleData;
    }

    private void loadTags() {
        MutableLiveData<List<String>> exampleData = new MutableLiveData<>();
        exampleData.setValue(Arrays.asList("#IndiePop", "#AlternativeRock"));
        this.tags = exampleData;
    }

    public void setTags(List<String> tags) {
        this.tags.setValue(tags);
    }

}