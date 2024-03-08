import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void testEventConstructor() {
        String name = "Event Name";
        String details = "Event Details";
        int organizerId = 123;

        Event event = new Event(name, details, organizerId);

        assertNotNull(event);
        assertEquals(name, event.getName());
        assertEquals(details, event.getDetails());
        assertEquals(organizerId, event.getOrganizer());
        assertNotNull(event.getEvent_id());
        assertNotNull(event.getAttendee_list());
        assertNull(event.getAttendee_qr());
        assertNull(event.getPromotional_qr());
    }

    @Test
    public void testGenerateQR() {
        Event event = new Event();
        String content = "QR Content";

        event.generateQR(content, false);
        assertNotNull(event.getAttendee_qr());
        assertNull(event.getPromotional_qr());

        event.generateQR(content, true);
        assertNull(event.getAttendee_qr());
        assertNotNull(event.getPromotional_qr());
    }

    @Test
    public void testAddAndRemoveAttendee() {
        Event event = new Event();
        Integer attendeeId = 456;

        event.addAttendee(attendeeId);
        assertTrue(event.getAttendee_list().contains(attendeeId));

        // Adding same attendee again should throw IllegalArgumentException
        try {
            event.addAttendee(attendeeId);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Attendee already exists in the attendee list.", e.getMessage());
        }

        event.removeAttendee(attendeeId);
        assertFalse(event.getAttendee_list().contains(attendeeId));

        // Removing attendee not in the list should throw NoSuchElementException
        try {
            event.removeAttendee(attendeeId);
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertEquals("Attendee not found in the attendee list.", e.getMessage());
        }
    }
}
