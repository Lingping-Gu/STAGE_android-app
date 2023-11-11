package edu.northeastern.stage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile extends AppCompatActivity {
    private static final int REQUEST_EDIT_PROFILE = 1;
    private boolean isOwner;
    private TextView textViewUsername, textViewDescription;
    private RecyclerView tagsRecyclerView;
    private TagsAdapter tagsAdapter;
    private List<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        textViewUsername = findViewById(R.id.userName);
        textViewDescription = findViewById(R.id.description);

        //Tags
        //TODO: connect list of tags to the database
        tags = Arrays.asList("#IndiePop", "#AlternativeRock");
        tagsRecyclerView = findViewById(R.id.tags);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        tagsAdapter = new TagsAdapter(tags);
        tagsRecyclerView.setAdapter(tagsAdapter);

        // Edit Button
        Button editProfileButton = findViewById(R.id.editProfileButton);
        isOwner = checkIfOwner();
        if (!isOwner) {
            editProfileButton.setVisibility(View.GONE);
        } else {
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchEditProfile();
                }
            });
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile.start(Profile.this);
            }
        });

        tagsRecyclerView = findViewById(R.id.tags);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        tagsAdapter = new TagsAdapter(tags);
        tagsRecyclerView.setAdapter(tagsAdapter);
    }

    private boolean checkIfOwner() {
        //TODO: use firebase function to finish the userId check.
        return true;
    }

    private void launchEditProfile() {
        Intent intent = new Intent(this, EditProfile.class);
        startActivityForResult(intent, REQUEST_EDIT_PROFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK && data != null) {
            // Retrieve the data from the result intent
            String username = data.getStringExtra(EditProfile.EXTRA_USERNAME);
            String description = data.getStringExtra(EditProfile.EXTRA_DESCRIPTION);
            String tagsString = data.getStringExtra(EditProfile.EXTRA_TAGS);

            // Update your TextViews
            textViewUsername.setText(username);
            textViewDescription.setText(description);
            tags = Arrays.asList(tagsString.split("#(?=[^#])")); // Split by the delimiter used when setting the tags

            // Update the RecyclerView with new tags
            tagsAdapter.setTags(tags);
            tagsAdapter.notifyDataSetChanged();
        }
    }
}
