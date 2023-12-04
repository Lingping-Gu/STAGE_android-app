package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.northeastern.stage.model.music.Track;

public class Explore_Review_SharedViewModel extends ViewModel {
    private MutableLiveData<Track> track = new MutableLiveData<>();
    private MutableLiveData<String> selectedSong = new MutableLiveData<>();

    private Track s;

    public Explore_Review_SharedViewModel() {
        Log.d("Explore Review SharedViewModel", "initialized");
    }

    public Track getSong() {
        return s;
    }

    public LiveData<Track> getLiveDataSong(){
        return track;
    }

    public void setSong(String title) {
        Log.d("Explore Review SharedViewModel", "in set song");
//        songString = new Song(title);
        s = new Track(title);
        track.setValue(s);
//        song.postValue(s);
        s.getName();
        Log.d("Explore Review SharedViewModel", "s.getTitle() in shared model set song --> " + s.getName());
        Log.d("Explore Review SharedViewModel", "song.getValue() in shared model set song --> " + track.getValue());

    }

    public void songSelected(String songTitle) {
        selectedSong.setValue(songTitle);
//        s = new Song(songTitle);
//        sharedViewModel.setSong(song);
    }

    public LiveData<String> getSelectedSong() {
        return selectedSong;
    }
}
