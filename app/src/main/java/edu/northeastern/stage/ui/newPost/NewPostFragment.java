package edu.northeastern.stage.ui.newPost;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import edu.northeastern.stage.databinding.FragmentNewPostBinding;
import edu.northeastern.stage.model.music.Track;
import edu.northeastern.stage.ui.adapters.TrackSearchAdapter;
import edu.northeastern.stage.ui.viewmodels.NewPostViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

// TODO: resolve errors

public class NewPostFragment extends Fragment {
    private FragmentNewPostBinding binding;
    private NewPostViewModel viewModel;
    private SharedDataViewModel sharedDataViewModel;
    private JsonObject selectedTrack;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // share data between models
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        // get user ID
        sharedDataViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
            if (userID != null) {
                viewModel.setUserID(userID);
            }
        });

        // get track if it exists
        sharedDataViewModel.getTrackPost().observe(getViewLifecycleOwner(), track -> {
            if (track != null) {
                viewModel.setTrack(track);
            }
        });

        // Set up the interactions for the new post elements
        binding.btnSubmitPost.setOnClickListener(v -> {
            String postContent = binding.etPostContent.getText().toString();
            viewModel.createPost(postContent);
        });

        // Setup AutoCompleteTextView for song search
        setupSearch();

        // Observe the post submission status from the ViewModel
        viewModel.getPostSubmissionStatus().observe(getViewLifecycleOwner(), isSuccess -> {
            // Handle post submission status
        });

        return root;
    }

    // TODO: it seems like the autocomplete/search doesn't work until you delete something from the search string
    // TODO: API is getting 10 songs but the view is not being updated
    private void setupSearch() {

            TrackSearchAdapter searchAdapter = new TrackSearchAdapter(getContext(), binding.actvSongSearch);

            binding.actvSongSearch.setAdapter(searchAdapter);
            binding.actvSongSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            binding.actvSongSearch.setOnItemClickListener((parent, view, position, id) -> {
                selectedTrack = searchAdapter.getItem(position);
                if (selectedTrack != null) {
                    String artists = "";
                    JsonArray artistsArray = selectedTrack.getAsJsonArray("artists");
                    if (artistsArray != null && artistsArray.size() > 0) {
                        for (JsonElement artist : artistsArray) {
                            artists = artists + artist.getAsJsonObject().get("name").getAsString() + " ";
                        }
                    }
                    artists = artists.trim();
                    Track trackToStore = viewModel.createTrack(selectedTrack);
                    sharedDataViewModel.setTrackPost(trackToStore);
                    binding.actvSongSearch.setText(selectedTrack.get("name").getAsString() + " by " + artists);
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}