package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.stage.model.Post;

public class ProfileViewModel extends ViewModel {
    private Integer profilePicResource;
    private String description;
    private String email;
    private List<Post> posts = new ArrayList<>();
    private List<String> recentlyListenedToImageURLs;
    private List<String> tags;
    private String currentID;
    private String profileOwnerID;

    public void retrieveDataFromDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference userRef = rootRef.child("users").child(profileOwnerID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("profilePicResource")) {
                         setProfilePicResource(snapshot.child("profilePicResource").getValue(Integer.class));
                    }
                    if(snapshot.hasChild("description")) {
                        setDescription(snapshot.child("description").getValue(String.class));
                    }
                    if(snapshot.hasChild("email")) {
                        setEmail(snapshot.child("email").getValue(String.class));
                    }
                    if(snapshot.hasChild("tags")) {
                        for (DataSnapshot tagsSnapshot : snapshot.child("tags").getChildren()) {
                            String tag = tagsSnapshot.getValue(String.class);
                            tags.add(tag);
                        }
                    }
                    if(snapshot.hasChild("posts")) {
                        for (DataSnapshot postsSnapshot : snapshot.child("posts").getChildren()) {
                            Post post = new Post(postsSnapshot.child("postID").getValue(String.class),
                                    postsSnapshot.child("ownerID").getValue(String.class),
                                    postsSnapshot.child("trackName").getValue(String.class),
                                    postsSnapshot.child("trackID").getValue(String.class),
                                    postsSnapshot.child("artistName").getValue(String.class),
                                    postsSnapshot.child("content").getValue(String.class),
                                    postsSnapshot.child("timestamp").getValue(Long.class),
                                    postsSnapshot.child("imageURL").getValue(String.class),
                                    postsSnapshot.child("visibilityState").getValue(String.class),
                                    postsSnapshot.child("spotifyURL").getValue(String.class));
                            posts.add(post);
                        }
                        Collections.sort(posts,new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void follow() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference currentUserRef = rootRef.child("users").child(currentID).child("following").child(profileOwnerID);
        DatabaseReference profileOwnerRef = rootRef.child("users").child(profileOwnerID).child("followers").child(currentID);

        currentUserRef.setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("ProfileFragment","Follow success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ProfileFragment","Follow unsuccessful!");
            }
        });
        profileOwnerRef.setValue(true);
    }

    public void unfollow() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference currentUserRef = rootRef.child("users").child(currentID).child("following").child(profileOwnerID);
        DatabaseReference profileOwnerRef = rootRef.child("users").child(profileOwnerID).child("followers").child(currentID);

        currentUserRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("ProfileFragment","Unfollow success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ProfileFragment","Unfollow unsuccessful!");
            }
        });
        profileOwnerRef.setValue(true);
    }

    public Integer getProfilePicResource() {
        return profilePicResource;
    }

    public void setProfilePicResource(Integer profilePicResource) {
        this.profilePicResource = profilePicResource;

    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
    public LiveData<List<String>> getTags() {
        return tags;
    }

    public MutableLiveData<String> getUserAvatarUrl() {
        return userAvatarUrl;
    }

    private void loadPosts() {
        // Load posts here
        // Once loaded, set them to the 'posts' LiveData
        // posts.setValue(loadedPosts);

//        MutableLiveData<List<Post>> exampleData = new MutableLiveData<>();
//        String musicLink = "http://open.spotify.com/track/6rqhFgbbKwnb9MLmUQDhG6";
//        String musicImageUrl = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";
//        Post examplePost = new Post("userAvatarUrl", musicLink, "Speak to me by Pink Floyd is amazing!", false,
//                "public", musicImageUrl, "Speak to me", "Pink Floyd",11111);
//        exampleData.setValue(Arrays.asList(examplePost, examplePost, examplePost));
//        this.posts = exampleData;
    }

    private void loadImageUrls() {
        MutableLiveData<List<String>> exampleData = new MutableLiveData<>();
        String exampleUrl = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";
        exampleData.setValue(Arrays.asList(exampleUrl, exampleUrl));
        this.recentListenedUrls = exampleData;
    }

    private void loadTags() {
        MutableLiveData<List<String>> exampleData = new MutableLiveData<>();
        exampleData.setValue(Arrays.asList("#IndiePop", "#AlternativeRock"));
        this.tags = exampleData;
    }

    private void loadUserAvatarUrl() {
        MutableLiveData<String> exampleData = new MutableLiveData<>();
        this.userAvatarUrl = exampleData;
    }

    public void setTags(List<String> tags) {
        this.tags.setValue(tags);
    }


    public String getCurrentID() {
        return currentID;
    }

    public void setCurrentID(String currentID) {
        this.currentID = currentID;
    }

    public String getProfileOwnerID() {
        return profileOwnerID;
    }

    public void setProfileOwnerID(String profileOwnerID) {
        this.profileOwnerID = profileOwnerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}