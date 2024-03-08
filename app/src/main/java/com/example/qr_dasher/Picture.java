package com.example.qr_dasher;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Base64;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public String getImage_string() {
        return image_string;
    }

    public byte[] getImage_jpeg() {
        return image_jpeg;
    }

    // Method to save JPEG byte array to the local device
    public static void saveJPEGLocally(Activity activity, byte[] jpeg, String fileName) throws IOException {
        // Check if the permission to write to external storage is granted
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1); // You can use any unique request code
            return; // Return without saving the image for now, user will be asked for permission
        }

        // Permission is granted, continue with saving the image
        saveImage(jpeg, fileName);
    }

    // Method to handle saving the image when permission is granted
    private static void saveImage(byte[] jpeg, String fileName) throws IOException {
        // Check if external storage is available and not read-only
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            throw new IOException("External storage is not mounted.");
        }

        // Create a directory for saving images (You can customize the directory path)
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "MyImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file where the JPEG will be saved
        File file = new File(directory, fileName + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        try {
            outputStream.write(jpeg);
        } finally {
            outputStream.close();
        }
    }
}
