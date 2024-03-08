import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class EventTest {
    private Event event;
    private User organizer;
    private ArrayList<User> attendees;

    @Before
    public void setUp() {
        organizer = new User("Organizer", "organizer@example.com");
        attendees = new ArrayList<>();
        attendees.add(new User("Attendee 1", "attendee1@example.com"));
        attendees.add(new User("Attendee 2", "attendee2@example.com"));
        event = new Event("Test Event", attendees, organizer);
    }

    @Test
    public void testAddAttendee() {
        User newAttendee = new User("New Attendee", "newattendee@example.com");
        event.addAttendee(newAttendee);
        assertTrue(event.getAttendee_list().contains(newAttendee));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAttendee() {
        event.addAttendee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateAttendee() {
        User existingAttendee = attendees.get(0);
        event.addAttendee(existingAttendee);
    }

    @Test
    public void testRemoveAttendee() {
        User attendeeToRemove = attendees.get(0);
        event.removeAttendee(attendeeToRemove);
        assertFalse(event.getAttendee_list().contains(attendeeToRemove));
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveNonexistentAttendee() {
        User nonExistentAttendee = new User("Nonexistent Attendee", "nonexistent@example.com");
        event.removeAttendee(nonExistentAttendee);
    }
}
