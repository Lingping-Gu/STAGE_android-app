package edu.northeastern.stage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import okhttp3.*;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button loginBT;
    Button registerBT;
    Button tokenBT;

    private static final String CLIENT_ID = "cdbc9d16da944c8dad47d543ed0741b2";
    private static final String CLIENT_SECRET = "d418779f7c994c6683bf27b1a35f141b";
    private String accessToken;
    private long tokenExpirationTime;

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
//                Intent intent = new Intent(MainActivity.this,Register.class);
//                startActivity(intent);
                artistSearch("Adele");
            }
        });

        tokenBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTokenExpired()) {
                    // Token is expired, refresh it
                    requestAccessToken();
                } else {
                    // Token is still valid, use it
                    Toast.makeText(MainActivity.this, tokenExpirationTime + " " +accessToken, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void requestAccessToken() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                String credentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET);

                RequestBody requestBody = new FormBody.Builder()
                        .add("grant_type", "client_credentials")
                        .build();

                Request request = new Request.Builder()
                        .url("https://accounts.spotify.com/api/token")
                        .addHeader("Authorization", credentials)
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseBody = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                accessToken = parseAccessToken(responseBody);
                                tokenExpirationTime = calculateTokenExpirationTime(responseBody);
                                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // unsuccessful response
                        Log.e("RequestError", "Unsuccessful response: " + response.code());
                    }
                } catch (IOException e) {
                    // handle IO exception
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void artistSearch(final String artist) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                String url = "https://api.spotify.com/v1/search?q=" + artist + "&type=artist";
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final String responseBody = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleArtistSearchResults(responseBody);
                            }
                        });
                    } else {
                        // unsuccessful response
                        Log.e("SearchError", "Unsuccessful search response: " + response.code());
                    }
                } catch (IOException e) {
                    // handle IO exception
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleArtistSearchResults(String responseBody) {
        try {
            JsonObject json = new Gson().fromJson(responseBody, JsonObject.class);
            if (json.has("artists") && json.getAsJsonObject("artists").has("items")) {
                JsonArray items = json.getAsJsonObject("artists").getAsJsonArray("items");
                if (items.size() > 0) {
                    // Retrieve information about the first artist in the search results
                    String artistName = items.get(0).getAsJsonObject().get("name").getAsString();
                    String artistId = items.get(0).getAsJsonObject().get("id").getAsString();

                    // Do something with the artist information (e.g., display it)
                    Toast.makeText(MainActivity.this, "Artist: " + artistName + ", ID: " + artistId, Toast.LENGTH_SHORT).show();
                } else {
                    // No artists found
                    Toast.makeText(MainActivity.this, "No artists found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Unexpected response format
                Log.e("SearchError", "Unexpected search response format");
            }
        } catch (JsonParseException e) {
            // JSON parsing exception
            Log.e("SearchError", "JSON parsing exception");
        }
    }

    private static String parseAccessToken(String responseBody) {
        try {
            JsonObject json = new Gson().fromJson(responseBody, JsonObject.class);
            if(json.has("access_token")) {
                return json.get("access_token").getAsString();
            } else {
                // if token is not present
                return null;
            }
        } catch (JsonParseException e) {
            // JSON parsing exception
            return null;
        }
    }

    private long calculateTokenExpirationTime(String responseBody) {
        try {
            JsonObject json = new Gson().fromJson(responseBody, JsonObject.class);
            if (json.has("expires_in")) {
                // Assuming the response contains the expiration time in seconds
                int expiresIn = json.get("expires_in").getAsInt();
                return Calendar.getInstance().getTimeInMillis() + expiresIn * 1000;
            }
        } catch (JsonParseException e) {
            // JSON parsing exception
            e.printStackTrace();
        }
        return 0;
    }

    private boolean isTokenExpired() {
        return tokenExpirationTime == 0 || tokenExpirationTime < Calendar.getInstance().getTimeInMillis();
    }

}