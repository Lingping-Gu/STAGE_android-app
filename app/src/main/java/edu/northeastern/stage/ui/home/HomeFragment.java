package edu.northeastern.stage.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.stage.MainActivity;
import edu.northeastern.stage.ui.adapters.NavigationCallback;
import edu.northeastern.stage.ui.adapters.PostAdapter;
import edu.northeastern.stage.R;
import edu.northeastern.stage.ui.viewmodels.HomeViewModel;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

public class HomeFragment extends Fragment implements NavigationCallback {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private HomeViewModel viewModel;
    private SharedDataViewModel sharedDataViewModel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);

        // Initially, hide RecyclerView and show ProgressBar
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // initialize view models
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);

        // get current user ID
        sharedDataViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
            if (userID != null) {
                viewModel.setCurrentUserId(userID);
                viewModel.loadPosts();
                adapter = new PostAdapter(getActivity(),new ArrayList<>(),viewModel.getCurrentUserId(), this); // Initialize with empty list
                recyclerView.setAdapter(adapter);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // observe changes in posts and update adapter
        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            // Update the UI when the data changes
            adapter.setPosts(posts);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });

        return view;
    }

    @Override
    public void onNavigateToProfile(String profileOwnerId) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            Bundle bundle = new Bundle();
            bundle.putString("PROFILE_OWNER_ID", profileOwnerId);
            mainActivity.navigateToFragment("OTHER_PROFILE_FRAGMENT", true, bundle);
        }
    }
}