package edu.northeastern.stage.ui.newPost;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.northeastern.stage.R;
import edu.northeastern.stage.databinding.FragmentNewPostBinding;
import edu.northeastern.stage.ui.adapters.TrackSearchAdapter;
import edu.northeastern.stage.ui.viewmodels.NewPostViewModel;

// TODO: resolve errors

public class NewPostFragment extends Fragment {
    private FragmentNewPostBinding binding;
    private NewPostViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        // Set up the interactions for the new post elements
        binding.btnSubmitPost.setOnClickListener(v -> viewModel.submitPost(binding.etPostContent.getText().toString()));

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

        TrackSearchAdapter searchAdapter = new TrackSearchAdapter(getContext());

        binding.actvSongSearch.setAdapter(searchAdapter);
        binding.actvSongSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.performSearch(s.toString())
                        .observe(getViewLifecycleOwner(), searchResults -> {
                            searchAdapter.clear();
                            ArrayList<JsonObject> newResults = new ArrayList<>(searchResults);
                            searchAdapter.addAll(newResults);
                            searchAdapter.notifyDataSetChanged();
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}