package edu.northeastern.stage.ui.profile;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.stage.PostAdapter;
import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.databinding.FragmentProfileBinding;
import edu.northeastern.stage.ui.editProfile.EditProfile;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initUI();
        observeViewModel();
        return binding.getRoot();
    }

    private void initUI() {
        //set view
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Tags
        tagsAdapter = new TagsAdapter(new ArrayList<>());
        binding.tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.tags.setAdapter(tagsAdapter);

        // Set up Posts Adapter
        postsAdapter = new PostAdapter(new ArrayList<>());
        binding.activities.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.activities.setAdapter(postsAdapter);

        // Set up RecentListened Adapter
        recentListenedAdapter = new RecentListenedAdapter(new ArrayList<>());
        binding.recentListened.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recentListened.setAdapter(recentListenedAdapter);

        // Edit Button
        binding.editProfileButton.setVisibility(checkIfOwner() ? View.VISIBLE : View.GONE);
        binding.editProfileButton.setOnClickListener(v -> launchEditProfile());
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.profile_edit).mutate();
        drawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.profile_edit_button_tint), PorterDuff.Mode.SRC_IN);
        binding.editProfileButton.setBackground(drawable);

        // If the fragment is not for the profile owner, the edit button should not be shown
        if (!checkIfOwner()) {
            binding.editProfileButton.setVisibility(View.GONE);
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

    private boolean checkIfOwner() {
        // TODO: use firebase function to finish the userId check.
        return true;
    }

    private void launchEditProfile() {
        // TODO: Refactor for ActivityResultLauncher if using
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
