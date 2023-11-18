package edu.northeastern.stage;

import java.util.ArrayList;

public class User {

    private ArrayList<Post> posts;
    private ArrayList<String> likes;
    private ArrayList<String> friends;
    private Long lastLoggedInTimeStamp;
    private String firstName;
    private String lastName;
    private Location lastLocation;
    private ArrayList<Review> reviews;

    public User(ArrayList<Post> posts, ArrayList<String> likes, ArrayList<String> friends, Long lastLoggedInTimeStamp, String firstName, String lastName, Location lastLocation, ArrayList<Review> reviews) {
        this.posts = posts;
        this.likes = likes;
        this.friends = friends;
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastLocation = lastLocation;
        this.reviews = reviews;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public Long getLastLoggedInTimeStamp() {
        return lastLoggedInTimeStamp;
    }

    public void setLastLoggedInTimeStamp(Long lastLoggedInTimeStamp) {
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
