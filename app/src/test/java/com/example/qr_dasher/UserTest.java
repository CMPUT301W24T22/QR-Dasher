import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetName() {
        User user = new User("John", "john@example.com", true);
        assertEquals("John", user.getName());
    }

    @Test
    void testGetEmail() {
        User user = new User("John", "john@example.com", true);
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testGetDetails() {
        User user = new User("John", "john@example.com", true);
        user.setDetails("Details about John");
        assertEquals("Details about John", user.getDetails());
    }

    @Test
    void testGetLocation() {
        User user = new User("John", "john@example.com", true);
        assertTrue(user.getLocation());
    }

    @Test
    void testUserIdNotNull() {
        User user = new User("John", "john@example.com", true);
        assertNotNull(user.getUserId());
    }

    @Test
    void testSetAndGetProfileImage() {
        User user = new User("John", "john@example.com", true);
        user.setProfile_image("profile_image_url");
        assertEquals("profile_image_url", user.getProfile_image());
    }

    @Test
    void testAddEventsJoined() {
        User user = new User("John", "john@example.com", true);
        user.addEventsJoined("event_qr_code");
        assertTrue(user.getEventsJoined().contains("event_qr_code"));
    }

    @Test
    void testAddEventsCreated() {
        User user = new User("John", "john@example.com", true);
        user.addEventsCreated("event_qr_code");
        assertTrue(user.getEventsCreated().contains("event_qr_code"));
    }
}
