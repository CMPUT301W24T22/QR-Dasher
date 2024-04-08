package com.example.qr_dasher;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

/**
 * Test class for the User class.
 */
public class UserTest {

    /**
     * Tests the constructor and getter methods of the User class.
     */
    @Test
    public void testConstructorAndGetters() {
        User user = new User("John Doe", "john@example.com", true);

        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertTrue(user.getLocation());
        assertNotNull(user.getEventsCreated());
        assertNotNull(user.getEventsJoined());
        assertEquals(0, user.getEventsCreated().size());
        assertEquals(0, user.getEventsJoined().size());
    }

    /**
     * Tests the setter methods of the User class.
     */
    @Test
    public void testSetters() {
        User user = new User("Alice", "alice@example.com", false);

        user.setName("Bob");
        assertEquals("Bob", user.getName());

        user.setEmail("bob@example.com");
        assertEquals("bob@example.com", user.getEmail());

        user.setLocation(true);
        assertTrue(user.getLocation());

        user.setProfile_image("profile.jpg");
        assertEquals("profile.jpg", user.getProfile_image());

        user.setDetails("Some details about Bob");
        assertEquals("Some details about Bob", user.getDetails());

        user.setUserId(123);
        assertEquals(123, user.getUserId());

        List<String> eventsCreated = user.getEventsCreated();
        eventsCreated.add("event1");
        assertEquals(1, user.getEventsCreated().size());

        List<String> eventsJoined = user.getEventsJoined();
        eventsJoined.add("event2");
        assertEquals(1, user.getEventsJoined().size());
    }

    /**
     * Tests the addition of events to the User's created events lists.
     */
    @Test
    public void testAddEventsCreated() {
        User user = new User("Alice", "alice@example.com", false);

        user.addEventsCreated("event1");
        assertEquals(1, user.getEventsCreated().size());
        assertEquals("event1", user.getEventsCreated().get(0));

    }

    /**
     * Tests the addition of events to the User's joined events lists.
     */
    @Test
    public void testAddEventsJoined(){
        User user = new User("Alice", "alice@example.com", false);
        user.addEventsJoined("event2");
        assertEquals(1, user.getEventsJoined().size());
        assertEquals("event2", user.getEventsJoined().get(0));
    }

}
