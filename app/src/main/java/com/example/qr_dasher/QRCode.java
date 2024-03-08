package com.example.qr_dasher;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a QRCode object used for generating QR codes.
 */
public class QRCode {
    private int event_id;
    private String content;
    private String QRImage;
    private int userID;
    private boolean promotional;

    /**
     * Constructor to create a QRCode object.
     *
     * @param event_id The ID of the event associated with the QR code.
     * @param content The content of the QR code.
     * @param userID The ID of the user associated with the QR code.
     * @param promotional Boolean indicating whether the QR code is promotional or not.
     */
    public QRCode(int event_id, String content, int userID, boolean promotional) {
        this.content = content;
        this.event_id = event_id;
        this.userID = userID;
        this.promotional = promotional;
        this.QRImage = createQRImage(content);
    }

    /**
     * Retrieves the event ID associated with the QR code.
     *
     * @return The event ID.
     */
    public int getEvent_id() {
        return event_id;
    }

    /**
     * Sets the event ID associated with the QR code.
     *
     * @param event_id The event ID to be set.
     */
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    /**
     * Retrieves the content of the QR code.
     *
     * @return The content of the QR code.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the QR code.
     *
     * @param content The content to be set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieves the image representation of the QR code.
     *
     * @return The image representation of the QR code.
     */
    public String getQrImage() {
        return this.QRImage;
    }

    /**
     * Sets the image representation of the QR code.
     *
     * @param qrImage The image representation to be set.
     */
    public void setQrImage(String qrImage) {
        this.QRImage = qrImage;
    }

    /**
     * Retrieves the user ID associated with the QR code.
     *
     * @return The user ID.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Sets the user ID associated with the QR code.
     *
     * @param userID The user ID to be set.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Retrieves whether the QR code is promotional or not.
     *
     * @return True if the QR code is promotional, false otherwise.
     */
    public boolean getQRType() {
        return this.promotional;
    }

    /**
     * Sets whether the QR code is promotional or not.
     *
     * @param promotional True if the QR code is promotional, false otherwise.
     */
    public void setQRType(boolean promotional) {
        this.promotional = promotional;
    }

    /**
     * Creates a bitmap image of the QR code based on the given text.
     *
     * @param text The text for which the QR code image is to be generated.
     * @return The image representation of the QR code as a Base64 encoded string.
     */
    public String createQRImage(String text) {
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int height = result.getHeight();
        int width = result.getWidth();

        int[] pixels = new int[width * height];
        for (int x = 0; x < height; x++) {
            int offset = x * width;
            for (int k = 0; k < width; k++) {
                pixels[offset + k] = result.get(k, x) ? BLACK : WHITE;
            }
        }
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        myBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        String qrImage = Picture.convertBitmaptoString(myBitmap);
        return qrImage;
    }
}
