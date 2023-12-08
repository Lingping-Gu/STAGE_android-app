package edu.northeastern.stage.ui.musicReview;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.stage.R;

import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import edu.northeastern.stage.databinding.FragmentMusicReviewBinding;
import edu.northeastern.stage.ui.adapters.ReviewAdapter;
import edu.northeastern.stage.ui.viewmodels.MusicReviewViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.Locale;

public class MusicReviewFragment extends Fragment {
    private FragmentMusicReviewBinding binding;
    private MusicReviewViewModel mViewModel;
    private SharedDataViewModel sharedDataViewModel;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TextView overallScoreTextView;
    private TextView noReviewsTextView;
    private TextView musicAttributesTextView;
    private Button addReviewButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentMusicReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // share data between models
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        mViewModel = new ViewModelProvider(this).get(MusicReviewViewModel.class);

        // set current user
        sharedDataViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
            if (userID != null) {
                mViewModel.setUserID(userID);
            }
        });

        // set track
        sharedDataViewModel.getTrack().observe(getViewLifecycleOwner(), track -> {
            if (track != null) {
                mViewModel.setTrack(track);
                Glide.with(this)
                        .load(track.getAlbum().getImageURL())
//                  .placeholder(R.drawable.placeholder_image) // Set a placeholder image
//                  .error(R.drawable.error_image) // Set an error image
                        .into(binding.albumCoverImageView);
            }
        });

        musicAttributesTextView = binding.musicAttributesTextView;
        reviewsRecyclerView = binding.reviewsRecyclerView;
        overallScoreTextView = binding.overallScoreTextView;
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noReviewsTextView = binding.noReviewsTextView;
        addReviewButton = binding.addReviewButton;

        updateMusicAttributes("Desire", "November 1975", "Classic Rock, Folk Rock, Protest Song");

        addReviewButton.setOnClickListener(v -> {
            // Use the NavController to navigate to the MusicReviewFragment
            NavController navController = NavHostFragment.findNavController(MusicReviewFragment.this);
            navController.navigate(R.id.action_navigation_music_review_to_submit_review);
        });

        mViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            if (reviews == null || reviews.isEmpty()) {
                // Show "No reviews yet." text and hide RecyclerView
                noReviewsTextView.setVisibility(View.VISIBLE);
                reviewsRecyclerView.setVisibility(View.GONE);
                updateOverallScore();
            } else {
                // Show RecyclerView and hide "No reviews yet." text
                reviewAdapter = new ReviewAdapter(reviews);
                reviewsRecyclerView.setAdapter(reviewAdapter);
                noReviewsTextView.setVisibility(View.GONE);
                reviewsRecyclerView.setVisibility(View.VISIBLE);
                updateOverallScore();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.fetchReviews(); // fetch all reviews
    }

    private void updateMusicAttributes(String album, String releaseDate, String genre) {
        String attributesText = String.format(Locale.getDefault(),
                "Album: %s\nReleased: %s\nGenre: %s",
                album, releaseDate, genre);

        musicAttributesTextView.setText(attributesText);
    }

    private void updateOverallScore() {
        float overallRating = mViewModel.calculateOverallRating();
        if (overallRating == 0) {
            overallScoreTextView.setText("Overall rating: N/A");
        } else {
            String formattedRating = String.format(Locale.getDefault(), "Overall rating: %.1f / 5", overallRating);
            overallScoreTextView.setText(formattedRating);
        }
    }
}