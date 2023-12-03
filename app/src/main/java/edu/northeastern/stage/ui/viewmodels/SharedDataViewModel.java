package edu.northeastern.stage.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedDataViewModel extends ViewModel {
    private MutableLiveData<String> userID = new MutableLiveData<>();

    public void setUserID(String userID) {
        this.userID.setValue(userID);
    }

    public LiveData<String> getUserID() {
        return userID;
    }

}
