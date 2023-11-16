package edu.northeastern.stage.ui.explore;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import edu.northeastern.stage.Circle;
import edu.northeastern.stage.R;

public class ExploreViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> recommendations = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> selectedSong = new MutableLiveData<>();

    public ExploreViewModel(Application application) {
        super(application);
    }

    public LiveData<List<String>> getRecommendations() {
        return recommendations;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchTextChanged(String text) {
        if (text.isEmpty()) {
            recommendations.setValue(new ArrayList<>());
        } else {
            makeDeezerReq(text);
        }
    }

    public void songSelected(String song) {
        selectedSong.setValue(song);
        // Any other logic related to song selection
    }

    public LiveData<String> getSelectedSong() {
        return selectedSong;
    }

    public void makeDeezerReq(String inputArtistName) {
        String deezerApiKey = getApplication().getString(R.string.DEEZER_API);
        isLoading.setValue(true);

        new Thread(() -> {
            try {
                URL url = new URL(getApplication().getString(R.string.DEEZER_BASE_URL) + inputArtistName);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key", deezerApiKey);
                urlConnection.setRequestProperty("X-RapidAPI-Host", "deezerdevs-deezer.p.rapidapi.com");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                String jsonString = convertStreamToString(inputStream);
                parseJsonAndUpdate(jsonString);

            } catch (Exception e) {
                e.printStackTrace();
                recommendations.postValue(new ArrayList<>());
            } finally {
                isLoading.postValue(false);
            }
        }).start();
    }


    private void parseJsonAndUpdate(String jsonString) {
        try {
            JSONObject response = new JSONObject(jsonString);
            JSONArray data = response.getJSONArray("data");
            List<String> tempRecs = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject currentResult = data.getJSONObject(i);
                JSONObject artist = currentResult.getJSONObject("artist");
                String trackName = currentResult.getString("title");
                String artistName = artist.getString("name");
                tempRecs.add(trackName + " by " + artistName);
            }
            recommendations.postValue(tempRecs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


    public void setCircles(List<Circle> circles) {
        // Process the circles as needed in the ViewModel
        // This method can be called from the ExploreFragment
    }
}
