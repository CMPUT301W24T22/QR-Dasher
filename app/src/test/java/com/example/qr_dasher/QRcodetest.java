import static org.junit.Assert.*;
import org.junit.Test;

public class QRCodeTest {

    @Test
    public void testCreateQRImage() {
        String content = "Test QR Code Content";
        QRCode qrCode = new QRCode(123, content, 456, false);
        String qrImage = qrCode.getQrImage();
        assertNotNull(qrImage);
        assertFalse(qrImage.isEmpty());
    }
    
    @Test
    public void testGettersAndSetters() {
        int eventId = 123;
        String content = "Test QR Code Content";
        int userId = 456;
        boolean promotional = false;
        QRCode qrCode = new QRCode(eventId, content, userId, promotional);
        assertEquals(eventId, qrCode.getEvent_id());
        assertEquals(content, qrCode.getContent());
        assertEquals(userId, qrCode.getUserID());
        assertEquals(promotional, qrCode.getQRType());
        int newEventId = 789;
        String newContent = "New QR Code Content";
        int newUserId = 101112;
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

