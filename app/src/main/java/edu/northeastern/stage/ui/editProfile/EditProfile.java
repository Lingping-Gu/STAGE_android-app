package edu.northeastern.stage.ui.editProfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import edu.northeastern.stage.R;


public class EditProfile extends AppCompatActivity {
    private EditText editUsername;
    private EditText editDescription;
    private AutoCompleteTextView editTags;
    private Button buttonSave;
    private ImageView profilePicture;
    private List<String> selectedTags = new ArrayList<>();
    private TagsAdapter_EditProfile tagsAdapter;
    private RecyclerView tagsRecyclerView;
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_TAGS = "extra_tags";
    public static final int REQUEST_IMAGE_GET = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, EditProfile.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Change the color of status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        editUsername = findViewById(R.id.editUsername);
        editDescription = findViewById(R.id.editDescription);
        editTags = findViewById(R.id.editTags);
        buttonSave = findViewById(R.id.buttonSaveProfile);
        profilePicture = findViewById(R.id.profilePicture);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        editTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = "#" + parent.getItemAtPosition(position);
                addTag(selectedTag);
            }
        });

        editTags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Detect if backspace was pressed and a item_tag_edit.xml is being deleted
                if (before > count && !selectedTags.isEmpty()) {
                    String text = s.toString();
                    if (text.endsWith(" ")) {
                        addTag("#" + text.trim());
                        editTags.setText("");
                    }
                }
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

        for (String tag : selectedTags) {
            addTag(tag);
        }
        tagsAdapter = new TagsAdapter_EditProfile(this, selectedTags);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(EXTRA_USERNAME, editUsername.getText().toString());
                data.putExtra(EXTRA_DESCRIPTION, editDescription.getText().toString());
                data.putExtra(EXTRA_TAGS, editTags.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });

        tagsRecyclerView = findViewById(R.id.tagsRecyclerView);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagsAdapter = new TagsAdapter_EditProfile(this, selectedTags);
        tagsRecyclerView.setAdapter(tagsAdapter);

        tagsAdapter.setOnItemClickListener(new TagsAdapter_EditProfile.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                removeTag(selectedTags.get(position));
            }
        });

    }

    private void addTag(String tag) {
        if (!selectedTags.contains(tag)) {
            selectedTags.add(tag);
            tagsAdapter.notifyItemInserted(selectedTags.size() - 1);
        }
    }

    private void removeTag(String tag) {
        int position = selectedTags.indexOf(tag);
        if (position >= 0) {
            selectedTags.remove(position);
            tagsAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            // Use the URI to load the image
            profilePicture.setImageURI(fullPhotoUri);
            // TODO: upload to the database
        }
    }

}
