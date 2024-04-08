package com.example.qr_dasher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
/**
 * Utility class for working with images, providing methods to convert between Bitmap, Base64 string, and JPEG byte array formats.
 */
public class Picture {
    private Bitmap image_bitmap;
    private String image_string;
    private byte[] image_jpeg; // Store JPEG image as byte array

    // Constructor
    /**
     * Constructor to initialize Picture object with Bitmap, Base64 string, and JPEG byte array.
     *
     * @param bitmap The Bitmap image.
     * @param string The Base64 string representation of the image.
     * @param jpeg   The JPEG byte array of the image.
     */
    public Picture(Bitmap bitmap, String string, byte[] jpeg) {
        this.image_bitmap = bitmap;
        this.image_string = string;
        this.image_jpeg = jpeg;
    }
     /**
     * Convert a Bitmap image to a JPEG byte array.
     *
     * @param bitmap The Bitmap image to be converted.
     * @return The JPEG byte array representation of the image.
     */
    // Method to convert Bitmap to JPEG
    public static byte[] convertBitmapToJPEG(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
    /**
     * Convert a Bitmap image to a Base64 string.
     *
     * @param bitmap The Bitmap image to be converted.
     * @return The Base64 string representation of the image.
     */
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
    /**
     * Convert a Base64 string to a Bitmap image.
     *
     * @param string The Base64 string representation of the image.
     * @return The Bitmap image.
     */
    // Method to convert Base64 string to Bitmap
    public static Bitmap convertStringtoBitmap(String string) {
        byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    /**
     * Convert a Base64 string to a JPEG byte array.
     *
     * @param string The Base64 string representation of the image.
     * @return The JPEG byte array representation of the image.
     */

    // Method to convert Base64 string to JPEG byte array
    public static byte[] convertStringtoJPEG(String string) {
        Bitmap bitmap = convertStringtoBitmap(string);
        return convertBitmapToJPEG(bitmap);
    }
    /**
     * Convert a JPEG byte array to a Bitmap image.
     *
     * @param jpeg The JPEG byte array representation of the image.
     * @return The Bitmap image.
     */

    // Method to convert JPEG byte array to Bitmap
    public static Bitmap convertJPEGtoBitmap(byte[] jpeg) {
        return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
    }
    /**
     * Convert a JPEG byte array to a Base64 string.
     *
     * @param jpeg The JPEG byte array representation of the image.
     * @return The Base64 string representation of the image.
     */

    // Method to convert JPEG byte array to Base64 string
    public static String convertJPEGtoString(byte[] jpeg) {
        Bitmap bitmap = convertJPEGtoBitmap(jpeg);
        return convertBitmaptoString(bitmap);
    }

    // Method to create an Image instance based on input type
    // Method to create an instance of Image
    /**
     * Create an instance of Picture based on the input type (Bitmap, Base64 string, or JPEG byte array).
     *
     * @param input The input object representing the image.
     * @return An instance of Picture.
     */
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
     /**
     * Crop a Bitmap image into a circular shape.
     *
     * @param bitmap The Bitmap image to be cropped.
     * @return The cropped Bitmap image.
     */
    public static Bitmap cropToCircle(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int diameter = Math.min(width, height);
        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(diameter / 2f, diameter / 2f, diameter / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (width - diameter) / 2f, (height - diameter) / 2f, paint);
        return output;
    }
    /**
     * Get the Bitmap image.
     *
     * @return The Bitmap image.
     */
    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }
     /**
     * Get the Base64 string representation of the image.
     *
     * @return The Base64 string representation of the image.
     */
    public String getImage_string() {
        return image_string;
    }
    /**
     * Get the JPEG byte array representation of the image.
     *
     * @return The JPEG byte array representation of the image.
     */
    public byte[] getImage_jpeg() {
        return image_jpeg;
    }
}
