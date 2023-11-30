package edu.northeastern.stage.ui.newPost;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.northeastern.stage.R;
import edu.northeastern.stage.databinding.FragmentNewPostBinding;

public class NewPostFragment extends Fragment {
    private FragmentNewPostBinding binding;
    private NewPostViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        // Initialize resources
        String deezerBaseUrl = getString(R.string.DEEZER_BASE_URL);
        String deezerApiKey = getString(R.string.DEEZER_API);

        // Set up the interactions for the new post elements
        binding.btnSubmitPost.setOnClickListener(v -> viewModel.submitPost(binding.etPostContent.getText().toString()));

        // Setup AutoCompleteTextView for song search
        setupSearch(deezerBaseUrl, deezerApiKey);

        // Observe the post submission status from the ViewModel
        viewModel.getPostSubmissionStatus().observe(getViewLifecycleOwner(), isSuccess -> {
            // Handle post submission status
        });

        return root;
    }

    private void setupSearch(String deezerBaseUrl, String deezerApiKey) {
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        binding.actvSongSearch.setAdapter(searchAdapter);

        binding.actvSongSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.performSearch(s.toString(), deezerBaseUrl, deezerApiKey)
                        .observe(getViewLifecycleOwner(), searchResults -> {
                            searchAdapter.clear();
                            searchAdapter.addAll(searchResults);
                            searchAdapter.notifyDataSetChanged();
                        });
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}