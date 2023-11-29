package edu.northeastern.stage.model;

public class Song {
    //TODO: create song model
    private String title;
    private String songId;

    // Constructor to initialize a Song with a string (title)
    public Song(String title) {
        this.title = title;
//        this.id = songId;
    }

    // Getter method for retrieving the title
    public String getTitle() {
        return title;
    }

//    public String getId() {
//        return id;
//    }

}
