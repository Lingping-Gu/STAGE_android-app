package edu.northeastern.stage.model;

public class User {

    private Long lastLoggedInTimeStamp;
    private Name name;
    private Location lastLocation;
    private String profilePicURL;

    public User(Long lastLoggedInTimeStamp, Name name, Location lastLocation, String profilePicURL) {
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
        this.name = name;
        this.lastLocation = lastLocation;
        this.profilePicURL = profilePicURL;
    }

    public User(Long lastLoggedInTimeStamp, Location lastLocation) {
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
        this.lastLocation = lastLocation;
    }


    public Long getLastLoggedInTimeStamp() {
        return lastLoggedInTimeStamp;
    }

    public void setLastLoggedInTimeStamp(Long lastLoggedInTimeStamp) {
        this.lastLoggedInTimeStamp = lastLoggedInTimeStamp;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
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
