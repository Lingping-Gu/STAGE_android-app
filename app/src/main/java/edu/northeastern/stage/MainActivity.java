package edu.northeastern.stage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Spotify.SearchCallBack {

    Button loginBT;
    Button registerBT;
    Button tokenBT;

    Spotify spotify = new Spotify(MainActivity.this);
    String[] tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBT=findViewById(R.id.login);
        registerBT=findViewById(R.id.register);
        tokenBT=findViewById(R.id.token);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        tokenBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotify.trackSearch("hello",MainActivity.this);
            }
        });
    }

    @Override
    public void onSearchResults(String[] searchResults) {
        tracks = searchResults;
        for(String track : tracks) {
            Toast.makeText(MainActivity.this, track, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSearchError(String errorMessage) {

    }
}