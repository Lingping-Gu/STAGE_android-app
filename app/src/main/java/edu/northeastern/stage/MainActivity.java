package edu.northeastern.stage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    Button loginBT;
    Button registerBT;
    Button tokenBT;
    Spotify spotify = new Spotify(MainActivity.this);

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
                CompletableFuture<ArrayList<JsonElement>> trackSearchFuture = spotify.artistSearch("hello",10);
                trackSearchFuture.thenAccept(searchResult -> {
                    for(JsonElement track : searchResult) {
                        Log.d("ArtistSearch", track.toString());
                    }
                }).exceptionally(e -> {
                    Log.e("ArtistSearchError",e.getMessage());
                    return null;
                });
            }
        });
    }
}