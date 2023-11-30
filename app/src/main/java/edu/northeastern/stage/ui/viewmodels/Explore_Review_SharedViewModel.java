package edu.northeastern.stage.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.northeastern.stage.model.music.Song;

public class Explore_Review_SharedViewModel extends ViewModel {
    private MutableLiveData<Song> song = new MutableLiveData<>();
    private MutableLiveData<String> trackId = new MutableLiveData<>();


    public Explore_Review_SharedViewModel() {
        Log.d("Explore Review SharedViewModel", "initialized");
    }

    public LiveData<Song> getLiveDataSong(){
        Log.d("Explore Review SharedViewModel", "in getLiveDataSong song -> " + song);
        return song;
    }

    public void setSong(String title) {
        Song s = new Song(title);
        song.setValue(s);
        Log.d("Explore Review SharedViewModel", "song.getValue() in shared model set song --> " + song.getValue().getTitle());
    }

    public LiveData<String> getTrackId(){
        return trackId;
    }

    public void setTrackId(String id){
        trackId.setValue(id);
    }
}
