package edu.northeastern.stage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.stage.databinding.ActivityMainBinding;
import edu.northeastern.stage.ui.authentication.Login;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedDataViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private Integer LOCATION_PERMISSION_REQUEST_CODE = 101;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            UID = currentUser.getUid();
            viewModel.setUserID(UID);
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

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

        // Set up a NavController listener to handle menu item selection
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_music_review) {
                // Only set the selected item if it's not already selected
                if (binding.navView.getSelectedItemId() != R.id.navigation_explore) {
                    binding.navView.getMenu().findItem(R.id.navigation_explore).setChecked(true);
                }
            } else {
                // Handle other destinations if needed
            }
        });

        // used for handling selections of different items in the BottomNavigationView
        binding.navView.setOnItemSelectedListener(item -> {
            if (navController.getCurrentDestination().getId() == R.id.navigation_music_review && item.getItemId() == R.id.navigation_explore) {
                // Navigate back to Explore fragment
                navController.popBackStack(R.id.navigation_explore, false);
                return true; // Event handled
            }
            // Default navigation behavior
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        // used to handle the scenario where the user re-selects the Explore button while on the Music Review fragment
        binding.navView.setOnItemReselectedListener(item -> {
            if (navController.getCurrentDestination().getId() == R.id.navigation_music_review && item.getItemId() == R.id.navigation_explore) {
                // Navigate back to Explore fragment
                navController.popBackStack(R.id.navigation_explore, false);
            }
        });

        // Binds the BottomNavigationView to the NavController.
        // Sets up listeners on the bottom navigation items such that when the user tap an item,
        // the NavController receives a callback and takes the appropriate action defined in the navigation graph (mobile_navigation.xml).
        // The NavHostFragment then inflates the appropriate fragment.
        NavigationUI.setupWithNavController(binding.navView, navController);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkAndRequestLocationPermission();

        // DO NOT USE: it hinders normal backstack operation.
        // Binds the BottomNavigationView to the NavController.
        // Sets up listeners on the bottom navigation items such that when the user tap an item,
        // the NavController receives a callback and takes the appropriate action defined in the navigation graph (mobile_navigation.xml).
        // The NavHostFragment then inflates the appropriate fragment.
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // ask if the user hasn't give location permission
    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // user permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateUser();
            } else {
                Toast.makeText(this, "Location permission is needed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUser();
    }

    // update user information
    private void updateUser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                updateUserHelper(location);
                            }
                        }
                    });
        }
    }

    private void updateUserHelper(Location location) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference("users").child(UID);

        Map<String, Object> updates = new HashMap<>();
        updates.put("lastLocation", new edu.northeastern.stage.model.Location(location.getLatitude(), location.getLongitude()));
        updates.put("lastLoggedInTimeStamp", System.currentTimeMillis());

        reference.updateChildren(updates, (error, ref) -> {
            if (error == null) {
                Log.d("UpdateUserLocation", "Location update successful");
            } else {
                Log.e("UpdateUserLocation", "Location update failed: " + error.getMessage());
            }
        });
    }

}