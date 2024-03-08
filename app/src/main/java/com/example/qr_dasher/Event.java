package com.example.qr_dasher;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Represents an event in the application.
 */
public class Event {
    private int event_id;
    private String name;
    private String details;
    private ArrayList<User> attendee_list;
    private QRCode attendee_qr;
    private QRCode promotional_qr;
    private int organizer;

    /**
     * Constructor for the Event class.
     *
     * @param name     The name of the event.
     * @param details  The details of the event.
     * @param userID   The ID of the organizer of the event.
     */
    public Event(String name, String details, int userID) {
        Random random = new Random();
        this.name = name;
        this.details = details;
        this.attendee_list = new ArrayList<>();
        this.event_id = random.nextInt();
        this.organizer = userID;
    }

    // Getter and setter methods

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

    /**
     * Generates a QR code for the event.
     *
     * @param content     The content to encode in the QR code.
     * @param promotional Indicates whether the QR code is promotional or not.
     */
    public void generateQR(String content, boolean promotional) {
        if (promotional) {
            this.promotional_qr = new QRCode(this.event_id, content, this.organizer, true);
        } else {
            this.attendee_qr = new QRCode(this.event_id, content, this.organizer, false);
        }
    }

    /**
     * Adds an attendee to the event.
     *
     * @param attendee The attendee to add.
     * @throws IllegalArgumentException if the attendee is null or already exists in the attendee list.
     */
    public void addAttendee(User attendee) {
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
     * @param attendee The attendee to remove.
     * @throws NoSuchElementException if the attendee is not found in the attendee list.
     */
    public void removeAttendee(User attendee) {
        if (!attendee_list.contains(attendee)) {
            throw new NoSuchElementException("Attendee not found in the attendee list.");
        }
        attendee_list.remove(attendee);
    }
}
