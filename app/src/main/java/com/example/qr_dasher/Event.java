package com.example.qr_dasher;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ArrayList;

public class Event {
    private int event_id;
    private String name;
    private String details;
    private ArrayList<User> attendee_list;
    private QRCode attendee_qr;
    private QRCode promotional_qr;
    private int userID;
    public Event(String name, ArrayList<User> attendee_list, User organizer){
        Random random = new Random();
        this.name = name;
        this.attendee_list = attendee_list;
        this.event_id = random.nextInt();
        //this.organizer = organizer;
    }
//    public User getOrganizer() {
//        return organizer;
//    }
//    public void setOrganizer(User organizer) {
//        this.organizer = organizer;
//    }
    public int getEvent_id() {
        return event_id;
    }
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public ArrayList<User> getAttendee_list() {
        return attendee_list;
    }
    public void setAttendee_list(ArrayList<User> attendee_list) {
        this.attendee_list = attendee_list;
    }
    public QRCode getAttendee_qr() {
        return attendee_qr;
    }
    public void setAttendee_qr(QRCode attendee_qr) {
        this.attendee_qr = attendee_qr;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public QRCode getPromotional_qr() {
        return promotional_qr;
    }
    public void setPromotional_qr(QRCode promotional_qr) {
        this.promotional_qr = promotional_qr;
    }
    public void generateQRCode(String content, boolean promotional){
        if (promotional){
            this.promotional_qr = new QRCode(this.event_id, content, this.userID, true);
        } else {
            this.attendee_qr = new QRCode(this.event_id, content, this.userID, false);
        }
    }
    public void addAttendee(User attendee) {
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee cannot be null.");
        }
        if (attendee_list.contains(attendee)) {
            throw new IllegalArgumentException("Attendee already exists in the attendee list.");
        }
        attendee_list.add(attendee);
    }
    public void removeAttendee(User attendee) {
        if (!attendee_list.contains(attendee)) {
            throw new NoSuchElementException("Attendee not found in the attendee list.");
        }
        attendee_list.remove(attendee);
    }

}
