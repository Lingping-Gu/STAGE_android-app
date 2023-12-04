package edu.northeastern.stage.ui.explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import edu.northeastern.stage.R;
import edu.northeastern.stage.databinding.FragmentExploreBinding;
import edu.northeastern.stage.model.music.Album;
import edu.northeastern.stage.model.music.Artist;
import edu.northeastern.stage.model.music.Track;
import edu.northeastern.stage.ui.adapters.TrackSearchAdapter;
import edu.northeastern.stage.ui.viewmodels.ExploreViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private ExploreViewModel viewModel;
    private JsonObject selectedTrack;
    private AutoCompleteTextView actv;
    private Button buttonToMusicReview;
    private CircleView circleView;
    private SeekBar geoSlider;
    private TextView progressTextView;
    private SharedDataViewModel sharedDataViewModel;
    private ExploreViewModel viewModel;
    private edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel sharedViewModel;
    TextView progressTextView;


    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            buttonToMusicReview.setEnabled(false);
            if(s.length() == 0){
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 0){
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("Explore Fragment", "afterTextChanged -> " + s.toString());
            viewModel.searchTextChanged(s.toString());
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // instantiate views
        buttonToMusicReview = binding.reviewButton;
        circleView = binding.circleView;
        actv = binding.autoCompleteTextView;
        geoSlider = binding.locationSeekBar;
        progressTextView = binding.textView;

        // set up view models to share data
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        //setup search
        setupSearch();

        buttonToMusicReview.setOnClickListener(v -> {
            // Use the NavController to navigate to the MusicReviewFragment
            if(!actv.getText().toString().isEmpty()) {
                actv.setText("");
                NavController navController = NavHostFragment.findNavController(ExploreFragment.this);
                navController.navigate(R.id.action_navigation_explore_to_navigation_music_review);
            }
        });

        viewModel.setCircles(circleView);

        // perform seek bar change listener event used for getting the progress value
        geoSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                progressTextView.setText(String.valueOf(progressChangedValue));

                int width = geoSlider.getWidth() - geoSlider.getPaddingLeft() - geoSlider.getPaddingRight();
                int thumbPos = geoSlider.getPaddingLeft() + width * geoSlider.getProgress() / geoSlider.getMax();

                progressTextView.measure(0, 0);
                int txtW = progressTextView.getMeasuredWidth();
                int delta = txtW / 2;
                progressTextView.setX(geoSlider.getX() + thumbPos - delta);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(requireContext(), "Seek bar progress is :" + progressChangedValue,
//                        Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void setupSearch() {
        TrackSearchAdapter searchAdapter = new TrackSearchAdapter(getContext(), actv);

        actv.setAdapter(searchAdapter);

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                buttonToMusicReview.setEnabled(false);
                if(s.length() == 0){
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.performSearch(s.toString())
                        .observe(getViewLifecycleOwner(), searchResults -> {
                            searchAdapter.clear();
                            ArrayList<JsonObject> newResults = new ArrayList<>(searchResults);
                            searchAdapter.addAll(newResults);
                            searchAdapter.notifyDataSetChanged();
                        });
            }
        });

        actv.setOnItemClickListener((parent, view, position, id) -> {
            selectedTrack = searchAdapter.getItem(position);
            if (selectedTrack != null) {
                String artists = "";
                JsonArray artistsArray = selectedTrack.getAsJsonArray("artists");
                if (artistsArray != null && artistsArray.size() > 0) {
                    for (JsonElement artist : artistsArray) {
                        artists = artists + artist.getAsJsonObject().get("name").getAsString() + " ";
                    }
                }
                actv.setText(selectedTrack.get("name").getAsString() + " by " + artists);
                Track trackToStore = viewModel.createTrack(selectedTrack); // create track in view model
                sharedDataViewModel.setTrack(trackToStore); // set track in shared data view model
                buttonToMusicReview.setEnabled(true);
            }
        });
    }
}