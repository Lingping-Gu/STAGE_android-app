package edu.northeastern.stage.model;

public class User {

    private String userID;
    private String email;
    private Long lastLoggedInTimeStamp;
    private Location lastLocation;
    private String profilePicURL;

    public User(String userID, String email, Long lastLoggedInTimeStamp, Location lastLocation, String profilePicURL) {
        this.userID = userID;
        this.email = email;
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
        this.lastLocation = lastLocation;
        this.profilePicURL = profilePicURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLastLoggedInTimeStamp() {
        return lastLoggedInTimeStamp;
    }

    public void setLastLoggedInTimeStamp(Long lastLoggedInTimeStamp) {
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
