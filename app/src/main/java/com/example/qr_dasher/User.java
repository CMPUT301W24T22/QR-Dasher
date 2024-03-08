package com.example.qr_dasher;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private String name;
    private int UserId;
    private String email;
    private String details;
    private boolean location;

    private String profile_image;
    private List<String> eventsCreated;
    private List<String> eventsJoined;
    public User(){

    }
    public User(String name, String email, boolean location){
        Random random = new Random();
        this.name = name;
        if (email.contains("@") && email.contains(".com")){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
        this.location = location;
        this.UserId = random.nextInt();
        this.eventsCreated = new ArrayList<>();
        this.eventsJoined = new ArrayList<>();
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

    public void addEventsJoined(String qrCode){
        this.eventsJoined.add(qrCode);
    }
    public void addEventsCreated(String qrCode){
        this.eventsCreated.add(qrCode);
    }
    public List<String> getEventsJoined(){
        return this.eventsJoined;
    }
    public List<String> getEventsCreated(){
        return this.eventsCreated;
    }
}
