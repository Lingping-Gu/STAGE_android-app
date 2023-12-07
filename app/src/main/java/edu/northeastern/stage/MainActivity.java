package edu.northeastern.stage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.stage.databinding.ActivityMainBinding;
import edu.northeastern.stage.model.Location;
import edu.northeastern.stage.ui.authentication.Login;
import edu.northeastern.stage.ui.explore.ExploreFragment;
import edu.northeastern.stage.ui.home.HomeFragment;
import edu.northeastern.stage.ui.musicReview.MusicReviewFragment;
import edu.northeastern.stage.ui.musicReview.SubmitReviewFragment;
import edu.northeastern.stage.ui.newPost.NewPostFragment;
import edu.northeastern.stage.ui.profile.ProfileFragment;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isUserInteraction = false;
    private boolean isProgrammaticSelection = false;
    private SharedDataViewModel viewModel;
    private CustomBackStack customBackStack = new CustomBackStack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String UID = "";
        if (currentUser != null) {
            UID = currentUser.getUid();
            viewModel.setUserID(UID);
            updateUser(UID);
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        customBackStack.pushOrBringToFront("HOME_FRAGMENT");
        Log.d("BackStackStatus", "Stack after push: " + customBackStack.getStackStatus());

        // This is for the appbar/actionbar/toolbar at the top of the screen if we are to implement it.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_new_post, R.id.navigation_profile)
                .build();

        // Find the NavHostFragment using SupportFragmentManager,
        // the placeholder/container in layout that gets replaced with the actual fragment that the user navigates to.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        // obtain the NavController from the NavHostFragment
        // the NavController is responsible for switching fragments using res.navigation.mobile_navigation.xml
        NavController navController = navHostFragment.getNavController();

        // Handle bottom nav bar display
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (!isUserInteraction) { // Check if change is not due to user interaction
                int destinationId = destination.getId();
                if (destinationId == R.id.navigation_home) {
                    binding.navView.setSelectedItemId(R.id.navigation_home);
                } else if (destinationId == R.id.navigation_explore) {
                    binding.navView.setSelectedItemId(R.id.navigation_explore);
                } else if (destinationId == R.id.navigation_new_post) {
                    binding.navView.setSelectedItemId(R.id.navigation_new_post);
                } else if (destinationId == R.id.navigation_profile) {
                    binding.navView.setSelectedItemId(R.id.navigation_profile);
                }
            }
            if (destination.getId() == R.id.navigation_music_review
                    || destination.getId() == R.id.navigation_submit_review) {
                // Only set the selected item if it's not already selected
                if (binding.navView.getSelectedItemId() != R.id.navigation_explore) {
                    binding.navView.getMenu().findItem(R.id.navigation_explore).setChecked(true);
                }
            }
        });

        binding.navView.setOnItemSelectedListener(item -> {
            if (!isProgrammaticSelection) {
                isUserInteraction = true;
                int itemId = item.getItemId();
                Log.d("NavigationAttempt", "Attempting to navigate to item ID: " + itemId);
                navigateToFragment(getFragmentTag(itemId), true); // Ensure this is calling navigateToFragment
                isUserInteraction = false;
            }
            return true;
        });

        // used to handle the scenario where the user re-selects the Explore button while on the Music Review fragment
        binding.navView.setOnItemReselectedListener(item -> {
            // Directly handle reselection without NavController
            int itemId = item.getItemId();
            handleReselection(itemId);
        });
    }

    public void navigateToFragment(String tag, boolean addToStack) {
        if (addToStack) {
            customBackStack.pushOrBringToFront(tag);
        }

        int itemId = getItemIdFromTag(tag);
        isProgrammaticSelection = true;
        if ((tag.equalsIgnoreCase("MUSIC_REVIEW_FRAGMENT") && (binding.navView.getSelectedItemId() != R.id.navigation_explore))
                || (tag.equalsIgnoreCase("SUBMIT_REVIEW_FRAGMENT")) && (binding.navView.getSelectedItemId() != R.id.navigation_explore)) {
            binding.navView.setSelectedItemId(R.id.navigation_explore);
        } else {
            binding.navView.setSelectedItemId(itemId);
        }
        isProgrammaticSelection = false;

        switchFragment(itemId);
        Log.d("NavigateToFragment", "Navigating to: " + tag + ", addToStack: " + addToStack);

    }

    private void handleReselection(int itemId) {
        if (itemId == R.id.navigation_explore) {
            if (customBackStack.peekFirst().equalsIgnoreCase("MUSIC_REVIEW_FRAGMENT")) {
                customBackStack.pop();
                navigateToFragment("EXPLORE_FRAGMENT", true);
            } else if (customBackStack.peekFirst().equalsIgnoreCase("SUBMIT_REVIEW_FRAGMENT")) {
                customBackStack.pop();
                navigateToFragment("MUSIC_REVIEW_FRAGMENT", true);
            }
        }
    }

    private void switchFragment(int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = getFragmentTag(itemId);

//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
//        if (currentFragment != null && tag.equals(customBackStack.peekFirst())) {
//            return;
//        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment newFragment = createFragmentForItem(itemId);
        transaction.replace(R.id.nav_host_fragment_activity_main, newFragment, tag);
        transaction.commit();

        Log.d("SwitchFragment", "Switched to fragment with tag: " + tag);
        Log.d("BackStackStatus", "Stack after push: " + customBackStack.getStackStatus());
    }

    OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (customBackStack.isEmpty()
                    || (customBackStack.size() == 1 && customBackStack.peekFirst().equalsIgnoreCase("HOME_FRAGMENT"))) {
                finish(); // Finish the activity instead of calling onBackPressed()
            } else {
                if (customBackStack.size() == 2 && !customBackStack.peekLast().equalsIgnoreCase("HOME_FRAGMENT")) {
                    customBackStack.pushLast("HOME_FRAGMENT");
                }
                customBackStack.pop(); // Remove current fragment from custom stack
                String newTopFragmentTag = customBackStack.peekFirst();
                navigateToFragment(newTopFragmentTag, false);
            }
        }
    };

    private int getItemIdFromTag(String tag) {
        if ("HOME_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_home;
        } else if ("EXPLORE_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_explore;
        } else if ("NEW_POST_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_new_post;
        } else if ("PROFILE_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_profile;
        } else if ("MUSIC_REVIEW_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_music_review;
        } else if ("SUBMIT_REVIEW_FRAGMENT".equalsIgnoreCase(tag)) {
            return R.id.navigation_submit_review;
        }
        return -1; // Indicates error
    }

    private String getFragmentTag(int itemId) {
        if (itemId == R.id.navigation_home) {
            return "HOME_FRAGMENT";
        } else if (itemId == R.id.navigation_explore) {
            return "EXPLORE_FRAGMENT";
        } else if (itemId == R.id.navigation_new_post) {
            return "NEW_POST_FRAGMENT";
        } else if (itemId == R.id.navigation_profile) {
            return "PROFILE_FRAGMENT";
        } else if (itemId == R.id.navigation_music_review) {
            return "MUSIC_REVIEW_FRAGMENT";
        } else if (itemId == R.id.navigation_submit_review) {
            return "SUBMIT_REVIEW_FRAGMENT";
        }
        return null;
    }
    private Fragment createFragmentForItem(int itemId) {
        if (itemId == R.id.navigation_home) {
            return new HomeFragment();
        } else if (itemId == R.id.navigation_explore) {
            return new ExploreFragment();
        } else if (itemId == R.id.navigation_new_post) {
            return new NewPostFragment();
        } else if (itemId == R.id.navigation_profile) {
            return new ProfileFragment();
        } else if (itemId == R.id.navigation_music_review) {
            return new MusicReviewFragment();
        } else if (itemId == R.id.navigation_submit_review) {
            return new SubmitReviewFragment();
        } else {
            return null;
        }
    }

    private void updateUser(String UID) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = mDatabase
                .getReference("users")
                .child(UID);

        Map<String, Object> updates = new HashMap<>();
        updates.put("lastLocation",new Location(100.0,100.0)); // need to edit this
        updates.put("lastLoggedInTimeStamp",System.currentTimeMillis());

        reference.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d("UpdateUser", "User update successful");
                } else {
                    Log.e("UpdateUser","Update user failed: " + error.getMessage());
                }
            }
        });
    }
}