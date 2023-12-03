package edu.northeastern.stage.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

public class SharedDataViewModel extends ViewModel {
    private MutableLiveData<String> userID = new MutableLiveData<>();
    private MutableLiveData<JsonObject> track = new MutableLiveData<>();

    public void setUserID(String userID) {
        this.userID.setValue(userID);
    }

    public LiveData<String> getUserID() {
        return userID;
    }

    public void setTrack(JsonObject track) {
        this.track.setValue(track);
    }

    public LiveData<JsonObject> getTrack() {
        return track;
    }

}
