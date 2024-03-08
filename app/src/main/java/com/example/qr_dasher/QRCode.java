package com.example.qr_dasher;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Intent;
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
 * Class for generating QR codes.
 */
public class QRCode {
    private int event_id;
    private String content;
    private String QRImage;
    private int userID;
    private boolean promotional;
    /**
     * Constructor to initialize a QRCode object with event ID, content, user ID, and promotional flag.
     *
     * @param event_id     The ID of the event associated with the QR code.
     * @param content      The content embedded in the QR code.
     * @param userID       The ID of the user associated with the QR code.
     * @param promotional  A boolean flag indicating if the QR code is promotional.
     */
    public QRCode (int event_id, String content, int userID, boolean promotional){
        this.content = content;
        this.event_id = event_id;
        this.userID = userID;
        this.promotional = promotional;
        this.QRImage = createQRImage(content);
    }
    /**
     * Get the event ID associated with the QR code.
     *
     * @return The event ID.
     */
    public int getEvent_id() {
        return event_id;    }
    /**
     * Set the event ID associated with the QR code.
     *
     * @param event_id The event ID to set.
     */
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }
    /**
     * Get the content embedded in the QR code.
     *
     * @return The content of the QR code.
     */
    public String getContent() {
        return content;
    }
    /**
     * Set the content embedded in the QR code.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * Get the Base64 string representation of the QR code image.
     *
     * @return The Base64 string representation of the QR code image.
     */
    public String getQrImage() {
        return this.QRImage;
    }


    /**
     * Set the Base64 string representation of the QR code image.
     *
     * @param qrImage The Base64 string representation of the QR code image to set.
     */
    public void setQrImage(String qrImage) {
        this.QRImage = qrImage;
    }
    /**
     * Get the user ID associated with the QR code.
     *
     * @return The user ID.
     */
    public int getUserID() {
        return this.userID;
    }
    /**
     * Set the user ID associated with the QR code.
     *
     * @param userID The user ID to set.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**
     * Get the type of the QR code (promotional or not).
     *
     * @return A boolean indicating if the QR code is promotional.
     */
    public boolean getQRType(){
        return this.promotional;
    }
    /**
     * Set the type of the QR code (promotional or not).
     *
     * @param promotional A boolean flag indicating if the QR code is promotional.
     */
    public void setQRType(boolean promotional){
        this.promotional = promotional;
    }
    /**
     * Method to create a QR code image represented as a Base64 string.
     *
     * @param text The text content to encode into the QR code.
     * @return The Base64 string representation of the QR code image.
     */
    public String createQRImage(String text){
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, null);
            // add the input text, format needed and the dimensions of the QR code
        } catch (WriterException e){
            e.printStackTrace();
            return null;
        }
        int height = result.getHeight();
        int width = result.getWidth();

        int[] pixels = new int[width*height];
        for(int x = 0; x<height; x++){
            int offset = x * width;
            for(int k = 0; k<width; k++){
                pixels[offset + k] = result.get(k,x)?BLACK: WHITE;
            }
        }
        Bitmap myBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        myBitmap.setPixels(pixels, 0, width,0,0,width,height);
        String qrImage = Picture.convertBitmaptoString(myBitmap);
        return qrImage;


    }



}

