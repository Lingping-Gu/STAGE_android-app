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
import edu.northeastern.stage.model.Song;
import edu.northeastern.stage.ui.ReviewAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicReviewFragment extends Fragment {
    private MusicReviewViewModel mViewModel;
    private edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel sharedViewModel;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    Song s;


    public static MusicReviewFragment newInstance() {
        return new MusicReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_review, container, false);
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
            reviewAdapter = new ReviewAdapter(reviews);
            reviewsRecyclerView.setAdapter(reviewAdapter);
        });

        mViewModel.fetchReviews(); // Simulate fetching data

        return view;
    }
}
