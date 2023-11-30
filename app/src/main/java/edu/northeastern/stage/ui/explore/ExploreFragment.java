package edu.northeastern.stage.ui.explore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import edu.northeastern.stage.R;
import edu.northeastern.stage.ui.viewmodels.ExploreViewModel;

public class ExploreFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView actv;
    private Button buttonToMusicReview;
    private CircleView circleView;
    private SeekBar geoSlider;
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
            viewModel.searchTextChanged(s.toString());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_explore, container, false);

        buttonToMusicReview = fragmentView.findViewById(R.id.reviewButton);
        circleView = fragmentView.findViewById(R.id.circleView);
        actv = fragmentView.findViewById(R.id.autoCompleteTextView);
        geoSlider = fragmentView.findViewById(R.id.locationSeekBar);
        progressTextView = fragmentView.findViewById(R.id.textView);

        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        observeViewModel();
        viewModel.setCircles(circleView);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(edu.northeastern.stage.ui.viewmodels.Explore_Review_SharedViewModel.class);

        actv.setThreshold(1);
        actv.addTextChangedListener(textWatcher);
        actv.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("Explore Fragment", "setOnItemClickListener");
            String selectedSong = (String) parent.getItemAtPosition(position);
            sharedViewModel.setSong(selectedSong);
            // Todo - need a method that defines the trackId variable based on selected song
            // Todo - validate the trackId before passing the value (just extra caution)
            //sharedViewModel.setTrackId(trackId); //need the trackId variable defined
            buttonToMusicReview.setEnabled(true);
        });

        buttonToMusicReview.setOnClickListener(v -> {

            // Use the NavController to navigate to the MusicReviewFragment
            NavController navController = NavHostFragment.findNavController(ExploreFragment.this);
            navController.navigate(R.id.action_navigation_explore_to_navigation_music_review);
        });

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

        return fragmentView;
    }

    private void observeViewModel() {
        viewModel.getRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recommendations);
            actv.setAdapter(adapter);
        });
    }

}