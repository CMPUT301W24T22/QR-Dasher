import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserConstructor() {
        String name = "John Doe";
        String email = "johndoe@example.com";
        boolean location = true;

        User user = new User(name, email, location);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(location, user.getLocation());
        assertNotNull(user.getUserId());
        assertTrue(user.getEventsCreated().isEmpty());
        assertTrue(user.getEventsJoined().isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        User user = new User();

        String name = "Jane Doe";
        String email = "janedoe@example.com";
        boolean location = false;
        String details = "Details";
        String profileImage = "profile_image_url";

        user.setName(name);
        user.setEmail(email);
        user.setLocation(location);
        user.setDetails(details);
        user.setProfile_image(profileImage);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(location, user.getLocation());
        assertEquals(details, user.getDetails());
        assertEquals(profileImage, user.getProfile_image());
    }

    @Test
    public void testEventsJoinedAndCreated() {
        User user = new User();
        String qrCode1 = "QRCode1";
        String qrCode2 = "QRCode2";

        user.addEventsJoined(qrCode1);
        user.addEventsCreated(qrCode2);

        List<String> eventsJoined = user.getEventsJoined();
        List<String> eventsCreated = user.getEventsCreated();

        assertEquals(1, eventsJoined.size());
        assertEquals(1, eventsCreated.size());
        assertTrue(eventsJoined.contains(qrCode1));
        assertTrue(eventsCreated.contains(qrCode2));
    }
}
