package com.example.qr_dasher;

import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;

/**
 * Test class for the Event class.
 */
public class EventTest {

    /**
     * Tests the constructor and getters of the Event class.
     */
    @Test
    public void testConstructorAndGetters() {
        Event event = new Event("Birthday Party", "Celebrate John's birthday", 1, 5);

        assertEquals("Birthday Party", event.getName());
        assertEquals("Celebrate John's birthday", event.getDetails());
        assertEquals(1, event.getOrganizer());
        assertEquals(0, event.getAttendee_list().size());
        assertEquals(0, event.getSignup_list().size());
        assertEquals(5, event.getMaxAttendees());
        assertNotNull(event.getAnnouncements());
    }

    /**
     * Tests the setter methods of the Event class.
     */
    @Test
    public void testSetters() {
        Event event = new Event();

        ArrayList<String> attendeeList = new ArrayList<>();
        attendeeList.add("user1");
        ArrayList<String> signupList = new ArrayList<>();
        signupList.add("user2");

        event.setName("New Name");
        assertEquals("New Name", event.getName());

        event.setDetails("New Details");
        assertEquals("New Details", event.getDetails());

        event.setOrganizer(2);
        assertEquals(2, event.getOrganizer());

        event.setAttendee_list(attendeeList);
        assertEquals(attendeeList, event.getAttendee_list());

        event.setSignup_list(signupList);
        assertEquals(signupList, event.getSignup_list());

        event.setMaxAttendees(50);
        assertEquals(50, event.getMaxAttendees());

        ArrayList<String> announcements = new ArrayList<>();
        announcements.add("Announcement 1");
        event.setAnnouncements(announcements);
        assertEquals(announcements, event.getAnnouncements());
    }

    /**
     * Tests adding an attendee to the event.
     */
    @Test
    public void testAddAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");
        assertEquals(1, event.getAttendee_list().size());
        assertTrue(event.getAttendee_list().contains("456"));
    }

    /**
     * Tests adding a null attendee, which should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee(null);
    }

    /**
     * Tests adding a duplicate attendee, which should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");
        event.addAttendee("456");
    }

    /**
     * Tests removing an attendee from the event.
     */
    @Test
    public void testRemoveAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.addAttendee("456");
        event.removeAttendee("456");

        assertFalse(event.getAttendee_list().contains("456"));
    }

    /**
     * Tests removing a non-existent attendee, which should throw a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemoveNonExistentAttendee() {
        Event event = new Event("EventName", "EventDetails", 123);
        event.removeAttendee("456");
    }

    /**
     * Tests adding an attendee signup to the event.
     */
    @Test
    public void testAddAttendeeSignup() {
        Event event = new Event("Birthday Party", "Celebrate John's birthday", 1);

        event.addAttendeeSignup("user1");
        assertEquals(1, event.getSignup_list().size());
        assertTrue(event.getSignup_list().contains("user1"));
    }

    /**
     * Tests removing a user from the event.
     */
    @Test
    public void testRemoveUserFromEvent() {
        Event event = new Event("Birthday Party", "Celebrate John's birthday", 1);
        event.addAttendee("user1");
        event.addAttendeeSignup("user2");

        event.removeUserFromEvent("user1");
        event.removeUserFromEvent("user2");

        assertEquals(0, event.getAttendee_list().size());
        assertEquals(0, event.getSignup_list().size());
    }
}