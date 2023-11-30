package edu.northeastern.stage.ui.musicReview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Review;
import edu.northeastern.stage.ui.viewmodels.MusicReviewViewModel;

public class SubmitReviewFragment extends Fragment {
    private EditText reviewContentEditText;
    private RatingBar reviewRatingBar;
    private Button submitReviewButton;
    private MusicReviewViewModel mViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_review, container, false);

        reviewContentEditText = view.findViewById(R.id.reviewContentEditText);
        reviewRatingBar = view.findViewById(R.id.reviewRatingBar);
        submitReviewButton = view.findViewById(R.id.submitReviewButton);

        mViewModel = new ViewModelProvider(requireActivity()).get(MusicReviewViewModel.class);

        submitReviewButton.setOnClickListener(v -> submitReview());

        return view;
    }

    private void submitReview() {
        // TODO: hide keyboard after button press; handle empty rating or content
        String content = reviewContentEditText.getText().toString();
        float rating = reviewRatingBar.getRating();

        String userId = "user_id"; // Replace with actual user ID logic
        String avatarUri = "avatar_uri"; // Replace with actual avatar URI logic

        Review newReview = new Review(userId, avatarUri, content, rating);
        mViewModel.addReview(newReview);
        // Clear the fields after submission
        reviewContentEditText.setText("");
        reviewRatingBar.setRating(0);
    }
}
