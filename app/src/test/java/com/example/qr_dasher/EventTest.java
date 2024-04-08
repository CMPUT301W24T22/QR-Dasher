package com.example.qr_dasher;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * This test tests getters and setters for the event class
 * References:
 * GPT3.5, OPEN AI 2024
 * prompt:
 * Here is my event class "", Can you create tests for the getters and setters of this class
 */
public class EventTest {

    @Test
    public void testAddAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");

        assertTrue(event.getAttendee_list().contains("456"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");
        event.addAttendee("456");
    }

    @Test
    public void testRemoveAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");
        event.removeAttendee("456");

        assertFalse(event.getAttendee_list().contains(456));
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveNonExistentAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.removeAttendee("456");
    }
    @Test
    public void testAddAttendeeSignup() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.addAttendeeSignup("123");
        assertTrue(event.getSignup_list().contains("123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAttendeeSignup() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.addAttendeeSignup(null);
    }


    @Test
    public void testGeneratePromotionalQR() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.generateQR("Promo Content", true);
        assertNotNull(event.getPromotional_qr());
    }

    @Test
    public void testSetAndGetEventPoster() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.setEventPoster("Poster Path");
        assertEquals("Poster Path", event.getEventPoster());
    }

    @Test
    public void testSetAndGetMaxAttendees() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.setMaxAttendees(50);
        assertEquals(50, event.getMaxAttendees());
    }

    @Test
    public void testSetAndGetOrganizer() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.setOrganizer(2);
        assertEquals(2, event.getOrganizer());
    }

    @Test
    public void testSetAndGetAnnouncements() {
        Event event = new Event("Test Event", "Test Details", 1);
        event.getAnnouncements().add("Announcement 1");
        event.getAnnouncements().add("Announcement 2");
        assertEquals(2, event.getAnnouncements().size());
        assertTrue(event.getAnnouncements().contains("Announcement 1"));
        assertTrue(event.getAnnouncements().contains("Announcement 2"));
    }

}
