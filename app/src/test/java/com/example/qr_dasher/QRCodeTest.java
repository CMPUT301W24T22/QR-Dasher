import android.graphics.Bitmap;
import org.junit.Test;
import static org.junit.Assert.*;

public class QRCodeTest {

    @Test
    public void testCreateQRImage() {
        String text = "Test QR Code";
        QRCode qrCode = new QRCode(1, text, 123, false);
        assertNotNull(qrCode);

        String qrImage = qrCode.getQrImage();
        assertNotNull(qrImage);
        assertFalse(qrImage.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        int eventId = 1;
        String content = "Test QR Code";
        int userId = 123;
        boolean promotional = false;

        QRCode qrCode = new QRCode(eventId, content, userId, promotional);
        assertNotNull(qrCode);

        assertEquals(eventId, qrCode.getEvent_id());
        assertEquals(content, qrCode.getContent());
        assertEquals(userId, qrCode.getUserID());
        assertEquals(promotional, qrCode.getQRType());

        // Test setters
        int newEventId = 2;
        String newContent = "Updated QR Code";
        int newUserId = 456;
        boolean newPromotional = true;

        qrCode.setEvent_id(newEventId);
        qrCode.setContent(newContent);
        qrCode.setUserID(newUserId);
        qrCode.setQRType(newPromotional);

        assertEquals(newEventId, qrCode.getEvent_id());
        assertEquals(newContent, qrCode.getContent());
        assertEquals(newUserId, qrCode.getUserID());
        assertEquals(newPromotional, qrCode.getQRType());
    }
}
