package com.example.qr_dasher;
import java.util.Random;

public class User {
    private String name;
    private int UserId;
    private String email;
    private String details;
    private boolean location;

    private String profile_image;

    public User(String name, String email, boolean location){
        Random random = new Random();
        this.name = name;
        this.email = email;
        this.location = location;
        this.UserId = random.nextInt();
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getDetails(){
        return this.details;
    }
    public boolean getLocation(){
        return this.location;
    }
    public int getUserId() { return UserId; }
    public String getProfile_image() { return profile_image; }
    public void setName(String Name){
        this.name = Name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setDetails(String details){
        this.details = details;
    }
    public void setLocation(boolean location){
        this.location = location;
    }
    public void setProfile_image(String profile_image) { this.profile_image = profile_image; }
    public void setUserId(int userId) { UserId = userId; }
}
