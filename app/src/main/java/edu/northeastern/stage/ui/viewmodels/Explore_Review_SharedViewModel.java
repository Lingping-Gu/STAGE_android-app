package edu.northeastern.stage.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.northeastern.stage.model.Song;

public class Explore_Review_SharedViewModel extends ViewModel {
    private MutableLiveData<Song> song = new MutableLiveData<>();
    private MutableLiveData<String> selectedSong = new MutableLiveData<>();

    private Song s;

    public Explore_Review_SharedViewModel() {
        Log.d("Explore Review SharedViewModel", "initialized");
    }

    public Song getSong() {
        return s;
    }

    public LiveData<Song> getLiveDataSong(){
        return song;
    }

    public void setSong(String title) {
        Log.d("Explore Review SharedViewModel", "in set song");
//        songString = new Song(title);
        s = new Song(title);
        song.setValue(s);
//        song.postValue(s);
        s.getTitle();
        Log.d("Explore Review SharedViewModel", "s.getTitle() in shared model set song --> " + s.getTitle());
        Log.d("Explore Review SharedViewModel", "song.getValue() in shared model set song --> " + song.getValue());

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
