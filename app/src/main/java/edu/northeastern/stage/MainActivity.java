package edu.northeastern.stage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start Explore activity
        Intent intent = new Intent(this, Explore.class);
        startActivity(intent);

        // Optionally, if you don't want to keep MainActivity on the back stack
        finish();
    }
}