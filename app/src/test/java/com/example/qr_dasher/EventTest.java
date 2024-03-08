import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testGenerateQR() {
        Event event = new Event("Test Event", "Details", 1);
        event.generateQR("Attendee QR", false);
        assertNotNull(event.getAttendee_qr());
        assertNull(event.getPromotional_qr());
    }

    @Test
    void testAddAttendee() {
        Event event = new Event("Test Event", "Details", 1);
        event.addAttendee(2);
        ArrayList<Integer> attendeeList = event.getAttendee_list();
        assertEquals(1, attendeeList.size());
        assertTrue(attendeeList.contains(2));
    }

    @Test
    void testRemoveAttendee() {
        Event event = new Event("Test Event", "Details", 1);
        event.addAttendee(2);
        event.removeAttendee(2);
        ArrayList<Integer> attendeeList = event.getAttendee_list();
        assertEquals(0, attendeeList.size());
        assertFalse(attendeeList.contains(2));
    }

    @Test
    void testRemoveAttendeeNotFound() {
        Event event = new Event("Test Event", "Details", 1);
        assertThrows(NoSuchElementException.class, () -> event.removeAttendee(2));
    }
}
