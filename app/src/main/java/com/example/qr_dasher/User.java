package com.example.qr_dasher;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * User class represents a user in the system.
 */
public class User {
    private String name;
    private int UserId;
    private String email;
    private String details;
    private boolean location;
    private String profile_image;
    private List<String> eventsCreated;
    private List<String> eventsJoined;
    private List<String> eventsSignedUp;
    private String token;

    private static GeoPoint geoPoint;

    public User(){

    }

    /**
     * Constructor for the User class.
     * @param name The name of the user.
     * @param email The email address of the user.
     * @param location The location status of the user.
     */
    public User(String name, String email, boolean location){
        Random random = new Random();
        this.name = name;
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
        this.location = location;
        this.UserId = random.nextInt();
        this.eventsCreated = new ArrayList<>();
        this.eventsJoined = new ArrayList<>();
        this.eventsSignedUp = new ArrayList<>();
        this.geoPoint = geoPoint;
        this.token = token;
    }
    public static User createGuest(){
        User user = new User();
        Random random = new Random();

        List<String> eventsCreated = new ArrayList<>();
        List<String> eventsJoined = new ArrayList<>();
        List<String> eventsSignedUp = new ArrayList<>();
        String token = new String();

        user.setUserId(random.nextInt());
        user.setName("guest_" + String.valueOf(user.getUserId()));
        user.setLocation(true);
        user.setToken(token);
        user.setEventsJoined(eventsJoined);
        user.setEventsCreated(eventsCreated);
        user.setEventsSignedUp(eventsSignedUp);
        user.setGeoPoint(geoPoint);

        return user;
    }
    /**
     * Get the name of the user.
     *
     * @return The name of the user.
     */
    public String getName(){
        return this.name;
    }
    /**
     * Get the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail(){
        return this.email;
    }

    public GeoPoint getGeoPoint(){
        return this.geoPoint;
    }
    /**
     * Get the details of the user.
     *
     * @return The details of the user.
     */
    public String getDetails(){
        return this.details;
    }
    /**
     * Get the location status of the user.
     *
     * @return The location status of the user.
     */
    public boolean getLocation(){
        return this.location;
    }
    /**
     * Get the unique ID of the user.
     *
     * @return The unique ID of the user.
     */
    public int getUserId() { return UserId; }
    /**
     * Get the profile image of the user.
     *
     * @return The profile image of the user.
     */
    public String getProfile_image() { return profile_image; }
    /**
     * Set the name of the user.
     *
     * @param Name The name of the user.
     */
    public void setName(String Name){
        this.name = Name;
    }
    /**
     * Set the email address of the user.
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email){
        this.email = email;
    }
    /**
     * Set the details of the user.
     *
     * @param details The details of the user.
     */
    public void setDetails(String details){
        this.details = details;
    }
    /**
     * Set the location status of the user.
     *
     * @param location The location status of the user.
     */
    public void setLocation(boolean location){
        this.location = location;
    }

    public void setGeoPoint(GeoPoint geoPoint){
        this.geoPoint = geoPoint;
    }
    /**
     * Set the profile image of the user.
     *
     * @param profile_image The profile image of the user.
     */
    public void setProfile_image(String profile_image) { this.profile_image = profile_image; }
    /**
     * Set the unique ID of the user.
     *
     * @param userId The unique ID of the user.
     */
    public void setUserId(int userId) { UserId = userId; }
    /**
     * Add an event joined by the user.
     *
     * @param qrCode The QR code of the event.
     */

    public void addEventsJoined(String qrCode){
        this.eventsJoined.add(qrCode);
    }
    /**
     * Add an event created by the user.
     *
     * @param qrCode The QR code of the event.
     */
    public void addEventsCreated(String qrCode){
        this.eventsCreated.add(qrCode);
    }
    /**
     * Get the list of events joined by the user.
     *
     * @return The list of events joined by the user.
     */
    public List<String> getEventsJoined(){
        return this.eventsJoined;
    }
    /**
     * Get the list of events created by the user.
     *
     * @return The list of events created by the user.
     */
    public List<String> getEventsCreated(){
        return this.eventsCreated;
    }
    public List<String> getEventsSignedUp() {
        return eventsSignedUp;
    }
    public void setEventsSignedUp(List<String> eventsSignedUp) { this.eventsSignedUp = eventsSignedUp; }
    public void addEventsSignedUp(String eventID){
        this.eventsSignedUp.add(eventID);
    }
    public void setEventsCreated(List<String> eventsCreated) {
        this.eventsCreated = eventsCreated;
    }
    public void setEventsJoined(List<String> eventsJoined) {
        this.eventsJoined = eventsJoined;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void removeEventCreated(String qrCode) {
        this.eventsCreated.remove(qrCode);
    }
    public void removeEventSignedUp(String eventID) {
        this.eventsSignedUp.remove(eventID);
    }
    public void removeEventJoined(String qrCode) {
        this.eventsJoined.remove(qrCode);
    }
}