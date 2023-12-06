package edu.northeastern.stage.ui.editProfile;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.stage.R;
import edu.northeastern.stage.ui.adapters.ImageAdapter;
import edu.northeastern.stage.ui.adapters.TagsAdapter_EditProfile;
import edu.northeastern.stage.ui.viewmodels.SharedDataViewModel;

public class EditProfile extends AppCompatActivity {
    private EditText editDescription;
    private AutoCompleteTextView editTags;
    private Button buttonSave;
    private Spinner profilePicSpinner;
    private ImageView profilePic;
    private TagsAdapter_EditProfile tagsAdapter;
    private RecyclerView tagsRecyclerView;
    private SharedDataViewModel sharedDataViewModel;
    private String currentUserID;
    private List<String> selectedTags = new ArrayList<>();
    private Integer profilePictureResource;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Change the color of status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // get shared view model
        sharedDataViewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);

        // get current user ID
        sharedDataViewModel.getUserID().observe(this, userID -> {
            if (userID != null) {
                currentUserID = userID;
            }
        });

        // find all views needed and set adapters for recycler views
        editDescription = findViewById(R.id.editDescription);
        editTags = findViewById(R.id.editTags);
        buttonSave = findViewById(R.id.buttonSaveProfile);
        profilePic = findViewById(R.id.profilePicture);
        profilePicSpinner = findViewById(R.id.editProfilePicSpinner);
        tagsRecyclerView = findViewById(R.id.tagsRecyclerView);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagsAdapter = new TagsAdapter_EditProfile(this, selectedTags);
        tagsRecyclerView.setAdapter(tagsAdapter);

        // set spinner to images
        Integer[] images = {R.drawable.anger, R.drawable.sad, R.drawable.sob, R.drawable.shock, R.drawable.blush};

        ImageAdapter adapter = new ImageAdapter(this, images);
        profilePicSpinner.setAdapter(adapter);

        // update profile picture resource
        profilePicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profilePictureResource = images[position];
                profilePic.setImageResource(profilePictureResource);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // get selected tags, profile picture, description from database
        retrieveInitialData();

        // edit tags on text change listener to submit tags to tags recycler view
        editTags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Logic for Enter key to add item_tag_edit.xml
                if (s.toString().endsWith("\n")) {
                    String tag = s.toString().trim();
                    if (!tag.isEmpty()) {
                        addTag("#" + tag.replace("\n", ""));
                        editTags.setText("");
                    }
                }
            }
        });

        // button save on click set
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
            }
        });

        // when tag is selected, remove it
        tagsAdapter.setOnItemClickListener(new TagsAdapter_EditProfile.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                removeTag(selectedTags.get(position));
            }
        });

    }

    /**
     * Helper method to add tag to selected tags
     * @param tag - tag to add as String
     */
    private void addTag(String tag) {
        if (!selectedTags.contains(tag)) {
            selectedTags.add(tag);
            tagsAdapter.notifyItemInserted(selectedTags.size() - 1);
        }
    }

    /**
     * Helper method to remove tag from selected tags
     * @param tag - tag to add as String
     */
    private void removeTag(String tag) {
        int position = selectedTags.indexOf(tag);
        if (position >= 0) {
            selectedTags.remove(position);
            tagsAdapter.notifyItemRemoved(position);
        }
    }

    /**
     * Helper method to retrieve all initial data required from the database about the user
     */
    private void retrieveInitialData() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = mDatabase.getReference();
        DatabaseReference userRef = rootRef.child("users").child(currentUserID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("imageResource")) {
                        profilePictureResource = snapshot.child("imageResource").getValue(Integer.class);
                    }
                    if(snapshot.hasChild("description")) {
                        description = snapshot.child("description").getValue(String.class);
                    }
                    if(snapshot.hasChild("tags")) {
                        for (DataSnapshot tagsSnapshot : snapshot.child("tags").getChildren()) {
                            String tag = tagsSnapshot.getValue(String.class);
                            selectedTags.add(tag);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // initially set profile picture + description to be what exists in the database
        editDescription.setText(description);
        profilePic.setImageResource(profilePictureResource);
    }

    /**
     * Helper method to update database with updated fields for profile page
     */
    private void updateDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase
                .getReference("users")
                .child(currentUserID);

        Map<String, Object> updates = new HashMap<>();

        description = editDescription.getText().toString();

        updates.put("imageResource",profilePictureResource);
        updates.put("description",description);
        updates.put("tags",selectedTags);

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