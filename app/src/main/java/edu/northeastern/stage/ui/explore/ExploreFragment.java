package edu.northeastern.stage.ui.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.northeastern.stage.Circle;
import edu.northeastern.stage.CircleView;
import edu.northeastern.stage.Explore;
import edu.northeastern.stage.R;
import edu.northeastern.stage.Review;

public class ExploreFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView actv;
    private Button reviewButton;
    private CircleView circleView;
    private SeekBar geoSlider;
    private ExploreViewModel viewModel;
    private static final String TAG = Explore.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_explore, container, false);

        reviewButton = fragmentView.findViewById(R.id.reviewButton);
        circleView = fragmentView.findViewById(R.id.circleView);
        geoSlider = fragmentView.findViewById(R.id.slider);
        actv = fragmentView.findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);

        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        observeViewModel();

        actv.addTextChangedListener(textWatcher);

        actv.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSong = (String) parent.getItemAtPosition(position);
            viewModel.songSelected(selectedSong);
            reviewButton.setEnabled(true);
        });

        reviewButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Review.class);
            startActivity(intent);
        });

        viewModel.setCircles(createCircles());

        return fragmentView;
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "in beforeTextChanged");
            reviewButton.setEnabled(false);
            if(s.length() == 0){
//                resultText.setText("");
            }
            // This function is called before text is edited
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "in onTextChanged");
            // This function is called when text is edited
//            toastMsg("Text is edited, and onTextChangedListener is called.");
            if(s.length() == 0){
//                resultText.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "in afterTextChanged");

            viewModel.searchTextChanged(s.toString());

        }
    };

    private void observeViewModel() {
        viewModel.getRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recommendations);
            actv.setAdapter(adapter);
        });
    }

    public List<Circle> createCircles(){
        List<Circle> circles = new ArrayList<>();

        Random rand = new Random();

        circles.add(new Circle(circleView.getHeight()/2, circleView.getWidth()/2, 55));

        for(int i=1; i<50; i++) {

            float x = rand.nextFloat()*100;
            float y = rand.nextFloat()*100;
            float radius = rand.nextFloat()*100 + 5;

            circles.add(new Circle(x, y, radius));
        }

        // Set the circles to the existing CircleView
        if (circleView != null) {
            circleView.setCircles(circles);
            circleView.invalidate(); // Request a redraw
        }
        return circles;
    }

}