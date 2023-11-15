package edu.northeastern.stage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Explore extends AppCompatActivity {

    private static final String TAG = Explore.class.getSimpleName();
    private EditText searchBox;
    //    private TextView resultText;
    private String trackName;
    private String artistName;
    //    private String[] recs;
    ArrayList<String> recs = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView actv;
    Button reviewButton;

    //    String[] recs = {"Option1", "Option2", "Option3", "Option4", "Option5"};
    // a method to toast a message given
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "in beforeTextChanged");
            reviewButton.setEnabled(false);
            if(s.length() == 0){
//                resultText.setText("");
            }
            // This function is called before text is edited
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "in onTextChanged");
            // This function is called when text is edited
//            toastMsg("Text is edited, and onTextChangedListener is called.");
            if(s.length() == 0){
//                resultText.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "in afterTextChanged");

            recs = new ArrayList<>();
            if(s.length() != 0) {
                makeDeezerReq(String.valueOf(s));
            }
            if(s.length() == 0){
//                resultText.setText("");
            }
        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore); // Inflate the layout

        Log.e("Explore", "Circles to be created.");
        createCircles();

////        suggestions = findViewById(R.id.suggestions_list);
//        Log.d(TAG, "in oncreate");
//
//        reviewButton = findViewById(R.id.reviewButton);
//
//        //Creating the instance of ArrayAdapter containing list of song names
//        //Getting the instance of AutoCompleteTextView
//        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//        actv.setThreshold(1);//will start working from first character
//
//        actv.addTextChangedListener(textWatcher);
//        // Set a listener to handle item selection
//        actv.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedSong = (String) parent.getItemAtPosition(position);
////            makeDeezerReq();
//            Log.d(TAG, "IN ACTV.setOnItemClickListener");
//            reviewButton.setEnabled(true);
//        });
//
//        actv.setTextColor(Color.BLUE);

    }

    public void reviewButtonPressed(View v){
        // Opens a new activity (AboutMeActivity) that displays my name and email
        Intent intent = new Intent(this, Review.class);
        startActivity(intent);
    }

    public void makeDeezerReq(String inputArtistName) {
        Log.d(TAG, "in deezer req");

        // API key
        String apiKey = getString(R.string.DEEZER_API);

        // Async thread to avoid blocking UI
        Thread generateTrackThread = new Thread(() -> {
            Log.d(TAG, "in generateTrackThread");

            // API endpoint
            try {
                // Create URL
                URL url = new URL(getString(R.string.DEEZER_BASE_URL) + inputArtistName);
                // Open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                // Set headers
                urlConnection.setRequestProperty("X-RapidAPI-Key", apiKey);
                urlConnection.setRequestProperty("X-RapidAPI-Host", "deezerdevs-deezer.p.rapidapi.com");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                // Handle response
                InputStream inputStream = urlConnection.getInputStream();
                String jsonString = convertStreamToString(inputStream);
                // Parse JSON
                JSONObject response = new JSONObject(jsonString);

                if(response.getInt("total") == 0){
//                    runOnUiThread(() -> {
//                        buttonOneLayout.setVisibility(View.GONE);
//                        loadingSpinner.setVisibility(View.GONE);
//                        noArtistLayout.setVisibility(View.VISIBLE);
//                    });
                } else {
                    Log.d(TAG, "in ELSE");

                    JSONArray data = response.getJSONArray("data");
                    //get a random key (each key in "data" array holds metadata about a track)
                    int lengthofRecs = data.length();
                    Log.d(TAG, "lengthofRecs -> " + lengthofRecs);

                    if(lengthofRecs > 10){
                        lengthofRecs = 10;
                    }

                    String tempResult = "";
                    for(int i = 0; i < lengthofRecs; i++){
                        JSONObject currentResult = data.getJSONObject(i);
                        //artist is the object within currentResult that holds data about the artist
                        //that the track belongs to
                        JSONObject artist = currentResult.getJSONObject("artist");
                        //store values such as Track Name, Album Name, and Artist's Image to display on the UI
                        trackName = currentResult.getString("title");
                        artistName = artist.getString("name");
                        recs.add(trackName + " by " + artistName);
                    }
                    adapter = new ArrayAdapter<>(Explore.this, android.R.layout.simple_list_item_1, recs);

                    Log.d(TAG, "recs final out of for loop --> " + recs);

                    // Update views on UI thread
                    runOnUiThread(() -> {
                        Log.d(TAG, "in runOnUiThread");
                        // Set the adapter for AutoCompleteTextView on the main thread
                        actv.setAdapter(adapter);
                        Log.d(TAG, "ABOUT TO GET OUT OF runOnUiThread");

                    });
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        generateTrackThread.start();

    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, to grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus,
        // create a new one to grab a window token from it (avoid null pointer exception)
        if (view == null) {
            view = new View(activity);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void createCircles() {
        List<Circle> circles = createNonOverlappingCircles();
        Log.e("Explore", "Circles are created.");

        CircleView cv = findViewById(R.id.circleView);
        cv.setCircles(circles);
    }

    private List<Circle> createNonOverlappingCircles() {
        List<Circle> circles = new ArrayList<>();
        Random rand = new Random();

        while (circles.size() < 20) {
            float x = rand.nextFloat() * 100;
            float y = rand.nextFloat() * 100;
            float radius = rand.nextFloat() * 100 + 5;

            // Ensure the newly created circle doesn't overlap with existing circles
            boolean isOverlapping = false;
            for (Circle existingCircle : circles) {
                float distance = calculateDistance(x, y, existingCircle.getX(), existingCircle.getY());
                float minDistance = radius + existingCircle.getRadius();
                if (distance < minDistance) {
                    isOverlapping = true;
                    break; // This circle overlaps, generate a new one
                }
            }

            if (!isOverlapping) {
                circles.add(new Circle(x, y, 10, 10, radius, radius * radius));
            }
        }

        return circles;
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

}
