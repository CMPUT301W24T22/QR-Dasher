import org.junit.Test;
import static org.junit.Assert.*;

public class QRCodeTest {

    @Test
    public void testQRCodeConstructor() {
        int eventId = 1;
        String content = "QR Content";
        int userId = 123;
        boolean promotional = true;

        QRCode qrCode = new QRCode(eventId, content, userId, promotional);

        assertNotNull(qrCode);
        assertEquals(eventId, qrCode.getEvent_id());
        assertEquals(content, qrCode.getContent());
        assertEquals(userId, qrCode.getUserID());
        assertEquals(promotional, qrCode.getQRType());
        assertNotNull(qrCode.getQrImage());
    }

    @Test
    public void testCreateQRImage() {
        String content = "QR Content";

        QRCode qrCode = new QRCode();
        String qrImage = qrCode.createQRImage(content);

        assertNotNull(qrImage);
        // Assuming the QR image creation is correct, it should not be an empty string
        assertFalse(qrImage.isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        QRCode qrCode = new QRCode();

        int eventId = 1;
        qrCode.setEvent_id(eventId);
        assertEquals(eventId, qrCode.getEvent_id());

        String content = "QR Content";
        qrCode.setContent(content);
        assertEquals(content, qrCode.getContent());

        int userId = 123;
        qrCode.setUserID(userId);
        assertEquals(userId, qrCode.getUserID());

        boolean promotional = true;
        qrCode.setQRType(promotional);
        assertEquals(promotional, qrCode.getQRType());

        String qrImage = "Base64EncodedQRImage";
        qrCode.setQrImage(qrImage);
        assertEquals(qrImage, qrCode.getQrImage());
    }
}
