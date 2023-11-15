package edu.northeastern.stage.ui.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.northeastern.stage.R;
import edu.northeastern.stage.databinding.FragmentExploreBinding;
import edu.northeastern.stage.ui.musicReview.MusicReviewFragment;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private ExploreViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Button buttonToMusicReview = view.findViewById(R.id.test_button);

        buttonToMusicReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the NavController to navigate to the MusicReviewFragment
                NavController navController = NavHostFragment.findNavController(ExploreFragment.this);
                navController.navigate(R.id.action_navigation_explore_to_navigation_music_review);
            }
        });
        return view;
    }
}