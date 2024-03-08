package com.example.qr_dasher;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void testAddAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(456);

        assertTrue(event.getAttendee_list().contains(456));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(456);
        event.addAttendee(456);
    }

    @Test
    public void testRemoveAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(456);
        event.removeAttendee(456);

        assertFalse(event.getAttendee_list().contains(456));
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveNonExistentAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.removeAttendee(456);
    }
}
