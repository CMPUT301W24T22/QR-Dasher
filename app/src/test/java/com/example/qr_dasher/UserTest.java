import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("John Doe", "john@example.com", true);
    }

    @Test
    public void testGettersAndSetters() {
        String newName = "Jane Doe";
        String newEmail = "jane@example.com";
        boolean newLocation = false;
        String newDetails = "Some details";
        String newProfileImage = "profile.jpg";
        int newUserId = 123;
        
        user.setName(newName);
        user.setEmail(newEmail);
        user.setLocation(newLocation);
        user.setDetails(newDetails);
        user.setProfile_image(newProfileImage);
        user.setUserId(newUserId);
        
        assertEquals(newName, user.getName());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newLocation, user.getLocation());
        assertEquals(newDetails, user.getDetails());
        assertEquals(newProfileImage, user.getProfile_image());
        assertEquals(newUserId, user.getUserId());
    }

    @Test
    public void testEventsJoined() {
 
        String eventCode1 = "event1";
        String eventCode2 = "event2";
        
        user.addEventsJoined(eventCode1);
        user.addEventsJoined(eventCode2);
        
        List<String> eventsJoined = user.getEventsJoined();
        assertNotNull(eventsJoined);
        assertEquals(2, eventsJoined.size());
        assertTrue(eventsJoined.contains(eventCode1));
        assertTrue(eventsJoined.contains(eventCode2));
    }

    @Test
    public void testEventsCreated() {
        // Test data
        String eventCode1 = "event1";
        String eventCode2 = "event2";
      
        user.addEventsCreated(eventCode1);
        user.addEventsCreated(eventCode2);
        
        List<String> eventsCreated = user.getEventsCreated();
        assertNotNull(eventsCreated);
        assertEquals(2, eventsCreated.size());
        assertTrue(eventsCreated.contains(eventCode1));
        assertTrue(eventsCreated.contains(eventCode2));
    }
}
