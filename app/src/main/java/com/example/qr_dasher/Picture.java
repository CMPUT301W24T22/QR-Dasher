package com.example.qr_dasher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Picture {
    private Bitmap image_bitmap;
    private String image_string;
    private byte[] image_jpeg; // Store JPEG image as byte array

    // Constructor
    public Picture(Bitmap bitmap, String string, byte[] jpeg) {
        this.image_bitmap = bitmap;
        this.image_string = string;
        this.image_jpeg = jpeg;
    }

    // Method to convert Bitmap to JPEG
    public static byte[] convertBitmapToJPEG(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // Method to convert Bitmap to Base64 string
    public static String convertBitmaptoString(Bitmap bitmap){
        // Create a new output Stream (base64 string)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // compress the qr code
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // return the String in base64 format
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // Method to convert Base64 string to Bitmap
    public static Bitmap convertStringtoBitmap(String string) {
        byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    // Method to convert Base64 string to JPEG byte array
    public static byte[] convertStringtoJPEG(String string) {
        Bitmap bitmap = convertStringtoBitmap(string);
        return convertBitmapToJPEG(bitmap);
    }

    // Method to convert JPEG byte array to Bitmap
    public static Bitmap convertJPEGtoBitmap(byte[] jpeg) {
        return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
    }

    // Method to convert JPEG byte array to Base64 string
    public static String convertJPEGtoString(byte[] jpeg) {
        Bitmap bitmap = convertJPEGtoBitmap(jpeg);
        return convertBitmaptoString(bitmap);
    }

    // Method to create an Image instance based on input type
    // Method to create an instance of Image
    public static Picture createImage(Object input) {
        Bitmap bitmap = null;
        String string = null;
        byte[] jpeg = null;

        if (input instanceof Bitmap) {
            bitmap = (Bitmap) input;
            string = convertBitmaptoString((Bitmap) input);
            jpeg = convertBitmapToJPEG((Bitmap) input);
        } else if (input instanceof String) {
            string = (String) input;
            bitmap = convertStringtoBitmap((String) input);
            jpeg = convertStringtoJPEG((String) input);
        } else if (input instanceof byte[]) {
            jpeg = (byte[]) input;
            bitmap = convertJPEGtoBitmap((byte[]) input);
            string = convertJPEGtoString((byte[]) input);
        }

        Picture picture = new Picture(bitmap, string, jpeg);
        return picture;
    }

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public String getImage_string() {
        return image_string;
    }

    public byte[] getImage_jpeg() {
        return image_jpeg;
    }
}
