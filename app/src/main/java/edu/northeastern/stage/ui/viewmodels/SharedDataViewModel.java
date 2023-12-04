package edu.northeastern.stage.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import edu.northeastern.stage.model.music.Track;

public class SharedDataViewModel extends ViewModel {
    private MutableLiveData<String> userID = new MutableLiveData<>();
    private MutableLiveData<JsonObject> trackJson = new MutableLiveData<>();
    private MutableLiveData<Track> track = new MutableLiveData<>();

    public void setUserID(String userID) {
        this.userID.setValue(userID);
    }

    public LiveData<String> getUserID() {
        return userID;
    }

    public void setTrackJson(JsonObject track) {
        this.trackJson.setValue(track);
    }

    public LiveData<JsonObject> getTrackJson() {
        return trackJson;
    }

    public void setTrack(Track track) {
        this.track.setValue(track);
    }

    public LiveData<Track> getTrack() {
        return track;
    }

}
