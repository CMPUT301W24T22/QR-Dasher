package com.example.qr_dasher;
import android.widget.ImageView;

import java.sql.Time;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ArrayList;
import com.google.firebase.Timestamp;

/**
 * Represents an event.
 */
public class Event {
    private int event_id;
    private String name;
    private String details;
    private ArrayList<String> attendee_list;
    private ArrayList<String> signup_list;
    private QRCode attendee_qr;
    private QRCode promotional_qr;
    private int organizer;
    private DateTime dateTime;
    private Timestamp timestamp;
    private String event_poster;

    private int maxAttendees;

    public Event(){

    }
    /**
     * Constructor for creating an Event object.
     *
     * @param name     The name of the event.
     * @param details  Details about the event.
     * @param userID   The ID of the organizer.
     */


    public Event(String name, String details, int userID){
        Random random = new Random();
        this.name = name;
        this.details = details;
        this.attendee_list = new ArrayList<>();
        this.event_id = random.nextInt();
        this.organizer = userID;
        this.signup_list = new ArrayList<>();
        this.maxAttendees = -1;

        //this.dateTime = dateTime;
    }
    public Event(String name, String details, int userID,int maxAttendees){
        Random random = new Random();
        this.name = name;
        this.details = details;
        this.attendee_list = new ArrayList<>();
        this.event_id = random.nextInt();
        this.organizer = userID;
        this.signup_list = new ArrayList<>();
        this.maxAttendees = maxAttendees;

        //this.dateTime = dateTime;
    }

    //    public User getOrganizer() {
//        return organizer;
//    }
//    public void setOrganizer(User organizer) {
//        this.organizer = organizer;
//    }
    /**
     * Gets the event ID.
     *
     * @return The event ID.
     */
    public int getEvent_id() {
        return event_id;
    }
    /**
     * Sets the event ID.
     *
     * @param event_id The event ID to set.
     */
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }
    /**
     * Gets the details of the event.
     *
     * @return The details of the event.
     */
    public String getDetails() {
        return details;
    }
    /**
     * Sets the details of the event.
     *
     * @param details The details to set.
     */
    public void setDetails(String details) {
        this.details = details;
    }
    /**
     * Gets the list of attendees for the event.
     *
     * @return The list of attendees.
     */
    public ArrayList<String> getAttendee_list() {
        return attendee_list;
    }
    public void setAttendee_list(ArrayList<String> attendee_list) {
        this.attendee_list = attendee_list;
    }
     /**
     * Gets the attendee QR code.
     *
     * @return The attendee QR code.
     */
    public QRCode getAttendee_qr() {
        return attendee_qr;
    }
    /**
     * Sets the attendee QR code.
     *
     * @param attendee_qr The QR code to set.
     */
    public void setAttendee_qr(QRCode attendee_qr) {
        this.attendee_qr = attendee_qr;
    }
     /**
     * Gets the name of the event.
     *
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }
     /**
     * Sets the name of the event.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
     /**
     * Gets the promotional QR code.
     *
     * @return The promotional QR code.
     */
    public QRCode getPromotional_qr() {
        return promotional_qr;
    }
    /**
     * Sets the promotional QR code.
     *
     * @param promotional_qr The QR code to set.
     */
    public void setPromotional_qr(QRCode promotional_qr) {
        this.promotional_qr = promotional_qr;
    }
    /**
     * Generates a QR code for the event.
     *
     * @param content     The content of the QR code.
     * @param promotional Whether the QR code is promotional or not.
     */
    public void generateQR(String content, boolean promotional){
        if (promotional){
            String pcontent = "p" +content;
            this.promotional_qr = new QRCode(this.event_id, pcontent, this.organizer, true);
        } else {
            this.attendee_qr = new QRCode(this.event_id, content, this.organizer, false);
        }
    }
     /**
     * Adds an attendee to the event.
     *
     * @param attendee The ID of the attendee to add.
     * @throws IllegalArgumentException if the attendee is null or already exists in the list.
     */
    public void addAttendee(String attendee) {
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee cannot be null.");
        }
        if (attendee_list.contains(attendee)) {
            throw new IllegalArgumentException("Attendee already exists in the attendee list.");
        }
        attendee_list.add(attendee);
    }
    /**
     * Removes an attendee from the event.
     *
     * @param attendee The ID of the attendee to remove.
     * @throws NoSuchElementException if the attendee is not found in the list.
     */
    public void removeAttendee(String attendee) {
        if (!attendee_list.contains(attendee)) {
            throw new NoSuchElementException("Attendee not found in the attendee list.");
        }
        attendee_list.remove(attendee);
    }
    /**
     * Gets the list of attendees who signed up for the event.
     *
     * @return The list of attendees who signed up for the event.
     */
    public ArrayList<String> getSignup_list() {
        return signup_list;
    }
//    public void setAttendee_list(ArrayList<User> attendee_list) {
//        this.attendee_list = attendee_list;
//    }

    /**
     * Signs up an attendee for the event.
     *
     * @param attendee The ID of the attendee to add.
     * @throws IllegalArgumentException if the attendee is null or already exists in the list.
     */
    public void addAttendeeSignup(String attendee) {
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee cannot be null.");
        }
        if (attendee_list.contains(attendee)) {
            throw new IllegalArgumentException("Attendee already exists in the attendee list.");
        }
        signup_list.add(attendee);
    }
    /**
     * Removes an attendee from the signup sheet for an event.
     *
     * @param attendee The ID of the attendee to remove.
     * @throws NoSuchElementException if the attendee is not found in the list.
     */
    public void removeAttendeeSignup(Integer attendee) {
        if (!attendee_list.contains(attendee)) {
            throw new NoSuchElementException("Attendee not found in the attendee list.");
        }
        signup_list.remove(attendee);
    }
    /**
     * Gets the DateTime object of the event.
     *
     * @return DateTime of the event.
     */
    public DateTime getDateTime() {
        return dateTime;
    }
    /**
     * Sets the DateTime object of the event.
     *
     * @param dateTime The dateTime of the event.
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the event poster.
     *
     * @return The event poster ImageView.
     */
    public String getEventPoster() {
        return event_poster;
    }

    /**
     * Sets the event poster.
     *
     * @param event_poster The event poster ImageView to set.
     */
    public void setEventPoster(String event_poster) {
        this.event_poster = event_poster;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;}
    public int getOrganizer() {
        return organizer;
    }

    public void setOrganizer(int organizer) {
        this.organizer = organizer;
    }

    public void setSignup_list(ArrayList<String> signup_list) {
        this.signup_list = signup_list;
    }

    public void removeUserFromEvent(String userID) {
        // Remove user from attendee_list
        if (attendee_list.contains(userID)) {
            attendee_list.remove(userID);
        }

        // Remove user from signup_list
        if (signup_list.contains(userID)) {
            signup_list.remove(userID);
        }
    }
}
