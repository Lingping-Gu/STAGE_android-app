package edu.northeastern.stage.ui.profile;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.stage.ui.adapters.PostAdapter;
import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.databinding.FragmentProfileBinding;
import edu.northeastern.stage.ui.adapters.RecentListenedAdapter;
import edu.northeastern.stage.ui.adapters.TagsAdapter;
import edu.northeastern.stage.ui.authentication.Login;
import edu.northeastern.stage.ui.editProfile.EditProfile;
import edu.northeastern.stage.ui.viewmodels.ProfileViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private SharedDataViewModel sharedDataViewModel;
    private TagsAdapter tagsAdapter;
    private PostAdapter postsAdapter;
    private RecentListenedAdapter recentListenedAdapter;
    private String currentUserId;
    private String profileOwnerId;
    private Integer profilePictureResource;
    private String email;
    private String description;
    private List<Post> posts;
    private List<String> tags;
    private List<String> recentlyListenedToImageURLs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // initialize view models
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        sharedDataViewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);

        // get current user ID
        sharedDataViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
            if (userID != null) {
                currentUserId = userID;
            }
        });

        // if landed on fragment by clicking on another fragment, get profile owner
        if(getArguments().getString("PROFILE_OWNER_ID") != null && !getArguments().getString("PROFILE_OWNER_URL").isEmpty()) {
            profileOwnerId = getArguments().getString("PROFILE_OWNER_ID");
        } else {
            profileOwnerId = currentUserId;
        }

        // set up adapters
        setUpAdapters();

        // show edit button or follow button depending on profile owner and current user
        showEditProfileButtonOrFollowButton();

        // TODO: need to set onClick for follow/unfollow button

        // initialize variables
        tags = new ArrayList<>();
        posts = new ArrayList<>();
        recentlyListenedToImageURLs = new ArrayList<>();

        // retrieve all values from database first
        retrieveDataFromDatabase();

        // set values to UI
        setUIValues();

        return binding.getRoot();
    }

    private void retrieveDataFromDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference userRef = rootRef.child("users").child(profileOwnerId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("imageResource")) {
                        profilePictureResource = snapshot.child("imageResource").getValue(Integer.class);
                    }
                    if(snapshot.hasChild("description")) {
                        description = snapshot.child("description").getValue(String.class);
                    }
                    if(snapshot.hasChild("email")) {
                        email = snapshot.child("email").getValue(String.class);
                    }
                    if(snapshot.hasChild("tags")) {
                        for (DataSnapshot tagsSnapshot : snapshot.child("tags").getChildren()) {
                            String tag = tagsSnapshot.getValue(String.class);
                            tags.add(tag);
                        }
                    }
                    if(snapshot.hasChild("posts")) {
                        for (DataSnapshot postsSnapshot : snapshot.child("posts").getChildren()) {
                            Post post = new Post(postsSnapshot.child("postID").toString(),
                                    postsSnapshot.child("ownerID").toString(),
                                    postsSnapshot.child("trackName").toString(),
                                    postsSnapshot.child("trackID").toString(),
                                    postsSnapshot.child("artistName").toString(),
                                    postsSnapshot.child("content").toString(),
                                    Long.parseLong(postsSnapshot.child("timestamp").toString()),
                                    postsSnapshot.child("imageURL").toString(),
                                    postsSnapshot.child("visibilityState").toString(),
                                    postsSnapshot.child("spotifyURL").toString());
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

    private void setUIValues() {
        binding.description.setText(description);
        binding.profileImage.setImageResource(profilePictureResource);
        binding.userName.setText(email);
        for(Post post : posts) {
            recentlyListenedToImageURLs.add(post.getImageURL());
        }
    }

    private void setUpAdapters() {
        // Set up TagsAdapter and connect to view
        tagsAdapter = new TagsAdapter(new ArrayList<>());
        binding.tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.tags.setAdapter(tagsAdapter);

        // Set up PostAdapter and connect to view
        postsAdapter = new PostAdapter(getActivity(), new ArrayList<>(), currentUserId);
        binding.activities.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.activities.setAdapter(postsAdapter);

        // Set up RecentListenedAdapter and connect to view
        recentListenedAdapter = new RecentListenedAdapter(new ArrayList<>());
        binding.recentListened.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recentListened.setAdapter(recentListenedAdapter);
    }

    private void showEditProfileButtonOrFollowButton() {
        // Set up LogOut Button & Edit Profile Button, or Follow Button
        if (currentUserId.equals(profileOwnerId)) {
            binding.followButton.setVisibility(View.GONE);

            // User is viewing their own profile, show Edit Profile Button
            binding.editProfileButton.setVisibility(View.VISIBLE);
            binding.editProfileButton.setOnClickListener(v -> launchEditProfile());
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.profile_edit).mutate();
            drawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.profile_edit_button_tint), PorterDuff.Mode.SRC_IN);
            binding.editProfileButton.setBackground(drawable);
            // User is viewing their own profile, show Edit Profile Button
            binding.LogOutButton.setVisibility(View.VISIBLE);
            binding.editProfileButton.setOnClickListener(v -> {
                performLogout();
                // Navigate to Login screen after logout
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            });
        } else {
            // User is viewing someone else's profile, show Follow Button
            binding.followButton.setVisibility(View.VISIBLE);
            binding.editProfileButton.setVisibility(View.GONE);
            binding.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow();
                }
            });
        }
    }

    private void launchEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        startActivity(intent);
    }

    private void performLogout() {
        // TODO: implement LogOut function
    }

    private void follow() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference currentUserRef = rootRef.child("users").child(currentUserId).child("following").child(profileOwnerId);
        DatabaseReference profileOwnerRef = rootRef.child("users").child(profileOwnerId).child("followers").child(currentUserId);

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

    private void unfollow() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference currentUserRef = rootRef.child("users").child(currentUserId).child("following").child(profileOwnerId);
        DatabaseReference profileOwnerRef = rootRef.child("users").child(profileOwnerId).child("followers").child(currentUserId);

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
}