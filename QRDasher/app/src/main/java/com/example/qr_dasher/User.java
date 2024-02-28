package com.example.qr_dasher;

public class User {
    private String name;
    private String email;
    private String details;
    private boolean location;

    public User(String name, String email, String details, boolean location){
        this.name = name;
        this.email = email;
        this.details = details;
        this.location = location;
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
}
