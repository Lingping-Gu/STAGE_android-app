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
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import edu.northeastern.stage.model.music.Song;
import edu.northeastern.stage.ui.adapters.ReviewAdapter;
import edu.northeastern.stage.ui.viewmodels.MusicReviewViewModel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class MusicReviewFragment extends Fragment {
    private MusicReviewViewModel mViewModel;
    private edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel sharedViewModel;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TextView overallScoreTextView;
    private TextView noReviewsTextView;
    private Button addReviewButton;
    Song s;

    public static MusicReviewFragment newInstance() {
        return new MusicReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_review, container, false);

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

        sharedViewModel = new ViewModelProvider(requireActivity()).get(edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel.class);

        Log.d("Music Review Fragment", "getSongTitle from explore fragment -> " + sharedViewModel.getSong());

        s = sharedViewModel.getSong();
        if(s!= null){
            Log.d("Music Review Fragment", "getSongTitle if s not null -> " + s.getTitle());
        }

//        sharedViewModel.getSong().observe(getViewLifecycleOwner(), song -> {
//            if(song != null) {
//                Log.d("Song title", song.getTitle());
//            }
//        });
        sharedViewModel.getLiveDataSong().observe(getViewLifecycleOwner(), song -> {
            Log.d("Music Review Fragment", "in observe getlivedatasong in music reviewfragment");
            if (song != null) {
                // Update the UI with the song title
                Log.d("Music Review Fragment", "getLiveDataSong from explore fragment -> " + song.getTitle());
            }
        });

        mViewModel = new ViewModelProvider(this).get(MusicReviewViewModel.class);
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