package com.example.qr_dasher;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the QRCode class getters and setters.
 */
public class QRCodeUnitTest {

    /**
     * Test case to verify the getters and setters of the QRCode class.
     */
    @Test
    public void testGettersAndSetters() {
        QRCode qrCode = new QRCode();

        // Test setting and getting event ID
        int event_id = 123;
        qrCode.setEvent_id(event_id);
        assertEquals(event_id, qrCode.getEvent_id());

        // Test setting and getting content
        String content = "Test Content";
        qrCode.setContent(content);
        assertEquals(content, qrCode.getContent());

        // Test setting and getting user ID
        int userID = 456;
        qrCode.setUserID(userID);
        assertEquals(userID, qrCode.getUserID());

        // Test setting and getting promotional flag
        boolean promotional = true;
        qrCode.setQRType(promotional);
        assertEquals(promotional, qrCode.getQRType());

        // Test setting and getting QR image
        String qrImage = "Test QR Image";
        qrCode.setQrImage(qrImage);
        assertEquals(qrImage, qrCode.getQrImage());
    }

}
