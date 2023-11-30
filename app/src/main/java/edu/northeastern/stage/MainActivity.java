package edu.northeastern.stage;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import edu.northeastern.stage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isUserInteraction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

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
            if (destination.getId() == R.id.navigation_music_review || destination.getId() == R.id.navigation_submit_review) {
                // Only set the selected item if it's not already selected
                if (binding.navView.getSelectedItemId() != R.id.navigation_explore) {
                    binding.navView.getMenu().findItem(R.id.navigation_explore).setChecked(true);
                }
            }
        });

        // Handle navigation through bottom nav bar
        binding.navView.setOnItemSelectedListener(item -> {
            isUserInteraction = true;
            int itemId = item.getItemId();
            if (navController.getCurrentDestination().getId() != itemId) {
                navController.navigate(itemId);
            }
            isUserInteraction = false;
            return true;
        });

        // used to handle the scenario where the user re-selects the Explore button while on the Music Review fragment
        binding.navView.setOnItemReselectedListener(item -> {
            if (navController.getCurrentDestination().getId() == R.id.navigation_music_review && item.getItemId() == R.id.navigation_explore) {
                // Navigate back to Explore fragment
                navController.popBackStack(R.id.navigation_explore, false);
            } else if (navController.getCurrentDestination().getId() == R.id.navigation_submit_review && item.getItemId() == R.id.navigation_explore) {
                // Navigate back to music review page
                navController.popBackStack(R.id.navigation_music_review, false);
            }
        });

        // DO NOT USE: it hinders normal backstack operation.
        // Binds the BottomNavigationView to the NavController.
        // Sets up listeners on the bottom navigation items such that when the user tap an item,
        // the NavController receives a callback and takes the appropriate action defined in the navigation graph (mobile_navigation.xml).
        // The NavHostFragment then inflates the appropriate fragment.
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}