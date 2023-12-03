package edu.northeastern.stage.ui.profile;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.stage.ui.adapters.PostAdapter;
import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.databinding.FragmentProfileBinding;
import edu.northeastern.stage.ui.adapters.RecentListenedAdapter;
import edu.northeastern.stage.ui.adapters.TagsAdapter;
import edu.northeastern.stage.ui.editProfile.EditProfile;
import edu.northeastern.stage.ui.viewmodels.ProfileViewModel;

import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private TagsAdapter tagsAdapter;
    private PostAdapter postsAdapter;
    private RecentListenedAdapter recentListenedAdapter;
    private static final int REQUEST_EDIT_PROFILE = 1;
    private String currentUserId;
    private String profileOwnerId;
    private String viewState;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initUI();
        observeViewModel();
        return binding.getRoot();
    }

    private void initUI() {
        // Get Ids from database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            currentUserId = "TEST";
        }
        if (profileOwnerId != null) profileOwnerId = currentUserId;

        //set view
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Tags
        tagsAdapter = new TagsAdapter(new ArrayList<>());
        binding.tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.tags.setAdapter(tagsAdapter);

        // Set up Posts Adapter
        if (isOwner()) viewState = "Owner";
        else if (isFriend()) viewState = "Friend";
        else viewState = "Stranger";
        // set posts
        postsAdapter = new PostAdapter(getActivity(), new ArrayList<>(), viewState);
        binding.activities.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.activities.setAdapter(postsAdapter);

        // Set up RecentListened Adapter
        recentListenedAdapter = new RecentListenedAdapter(new ArrayList<>());
        binding.recentListened.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recentListened.setAdapter(recentListenedAdapter);

        // Set up Edit Profile Button or Follow Button
        if (isOwner()) {
            // User is viewing their own profile, show Edit Profile Button
            binding.editProfileButton.setVisibility(View.VISIBLE);
            binding.followButton.setVisibility(View.GONE);

            binding.editProfileButton.setOnClickListener(v -> launchEditProfile());
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.profile_edit).mutate();
            drawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.profile_edit_button_tint), PorterDuff.Mode.SRC_IN);
            binding.editProfileButton.setBackground(drawable);
        } else {
            // User is viewing someone else's profile, show Follow Button
            binding.followButton.setVisibility(View.VISIBLE);
            binding.editProfileButton.setVisibility(View.GONE);
            binding.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseExample firebaseExample = new FirebaseExample();
                    firebaseExample.follow(currentUserId, profileOwnerId);
                }
            });
        }
    }

    private void observeViewModel() {
        //get user avatar
        viewModel.getUserAvatarUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String url) {
                if (url != null) {
                    if (url != null) {
                        Picasso.get()
                                .load(url)
                                .error(R.drawable.default_pfp)
                                .into(binding.profileImage);
                    }
                }
            }
        });

        // Observe the LiveData for posts
        viewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                postsAdapter.setPosts(posts);
            }
        });

        // Observe the LiveData for image URLs
        viewModel.getImageUrls().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> imageUrls) {
                if (imageUrls.isEmpty()) {
                    binding.textNoImages.setVisibility(View.VISIBLE);
                    binding.recentListened.setVisibility(View.GONE);
                } else {
                    binding.textNoImages.setVisibility(View.GONE);
                    binding.recentListened.setVisibility(View.VISIBLE);
                    recentListenedAdapter.setImageUrls(imageUrls);
                }
            }
        });

        // Observe the LiveData for tags
        viewModel.getTags().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> tags) {
                tagsAdapter.setTags(tags);
            }
        });

    }

    private boolean isOwner() {
        return currentUserId.equals(profileOwnerId);
    }

    // TODO: Implement in DataBaseExample
    private boolean isFriend() {
        return true;
    }

    private boolean checkIfFriends() {
        //TODO: Implement friends in database
        return currentUserId.equals(profileOwnerId);
    }

    private void launchEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        startActivityForResult(intent, REQUEST_EDIT_PROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == Activity.RESULT_OK && data != null) {
            // Retrieve the data from the result intent
            String username = data.getStringExtra(EditProfile.EXTRA_USERNAME);
            String description = data.getStringExtra(EditProfile.EXTRA_DESCRIPTION);
            String tagsString = data.getStringExtra(EditProfile.EXTRA_TAGS);

            // Update your views using binding
            binding.userName.setText(username);
            binding.description.setText(description);
            // Split by the delimiter used when setting the tags
            List<String> updatedTags = Arrays.asList(tagsString.split("#(?=[^#])")); // Ensure this regex correctly matches your format
            viewModel.setTags(updatedTags);
        }
    }

}
