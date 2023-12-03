package edu.northeastern.stage.ui.musicReview;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import edu.northeastern.stage.model.music.Track;
import edu.northeastern.stage.ui.adapters.ReviewAdapter;
import edu.northeastern.stage.ui.viewmodels.MusicReviewViewModel;
import edu.northeastern.stage.ui.viewmodels.NewPostViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Locale;

public class MusicReviewFragment extends Fragment {
    private MusicReviewViewModel mViewModel;
    private SharedDataViewModel sharedDataViewModel;
    private edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel sharedViewModel;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TextView overallScoreTextView;
    private TextView noReviewsTextView;
    private Button addReviewButton;
    private ImageView albumIV;
    private Track currentTrack;

    public static MusicReviewFragment newInstance() {
        return new MusicReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_review, container, false);

        // share data between models
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        mViewModel = new ViewModelProvider(this).get(MusicReviewViewModel.class);

        // set views
        albumIV = view.findViewById(R.id.albumCoverImageView);

        // get current track
        sharedDataViewModel.getTrack().observe(getViewLifecycleOwner(), track -> {
            if (track != null) {
                currentTrack = track;
                Glide.with(this)
                        .load(currentTrack.getAlbum().getImageURL())
//                  .placeholder(R.drawable.placeholder_image) // Set a placeholder image
//                  .error(R.drawable.error_image) // Set an error image
                        .into(albumIV);
            }
        });

        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        overallScoreTextView = view.findViewById(R.id.overallScoreTextView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noReviewsTextView = view.findViewById(R.id.noReviewsTextView);
        addReviewButton = view.findViewById(R.id.addReviewButton);

        addReviewButton.setOnClickListener(v -> {
            // Use the NavController to navigate to the MusicReviewFragment
            NavController navController = NavHostFragment.findNavController(MusicReviewFragment.this);
            navController.navigate(R.id.action_navigation_music_review_to_submit_review);
        });

        // fbdb get reviews
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
        mViewModel.fetchReviews(); // Simulate fetching data

        return view;
    }

    // TODO: validate observers and/or find better way to refresh data
    @Override
    public void onResume() {
        super.onResume();
        // Trigger a refresh of data
        mViewModel.fetchReviews();
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