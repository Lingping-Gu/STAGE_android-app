package edu.northeastern.stage.ui.musicReview;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.stage.R;
import edu.northeastern.stage.ReviewAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class MusicReviewFragment extends Fragment {
    private MusicReviewViewModel mViewModel;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TextView overallScoreTextView;

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

        mViewModel = new ViewModelProvider(this).get(MusicReviewViewModel.class);
        mViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            reviewAdapter = new ReviewAdapter(reviews);
            reviewsRecyclerView.setAdapter(reviewAdapter);
            updateOverallScore();
        });

        mViewModel.fetchReviews(); // Simulate fetching data

        return view;
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