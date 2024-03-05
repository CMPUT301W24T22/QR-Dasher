package com.example.qr_dasher;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;


public class QRCode {
    private int event_id;
    private String content;
    private Bitmap QRBitmap;
    private String QRString;
    private User organizer;
    private boolean promotional;
    public QRCode (int event_id, String content, User organizer, boolean promotional){
        this.content = content;
        this.event_id = event_id;
        this.organizer = organizer;
        this.promotional = promotional;

        this.QRBitmap = createBitmap(content);
        this.QRString = convertQRtoString(this.QRBitmap);
    }
    public int getEvent_id() {
        return event_id;    }
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getQRString() {
        return QRString;
    }

    public Bitmap getQRBitmap() {
        return QRBitmap;
    }

    public void setQRBitmap(Bitmap QRBitmap) {
        this.QRBitmap = QRBitmap;
        this.QRString = convertQRtoString(QRBitmap);
    }

    public User getOrganizer() {
        return organizer;
    }
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
    public boolean getQRType(){
        return this.promotional;
    }
    public void setQRType(boolean promotional){
        this.promotional = promotional;
    }
    private Bitmap createBitmap(String text){
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
        return myBitmap;
    }

    private String convertQRtoString(Bitmap qrCode){
        // Create a new output Stream (base64 string)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // compress the qr code
        qrCode.compress(Bitmap.CompressFormat.PNG, 100,baos);
        byte[] imageBytes = baos.toByteArray();
        // return the String in base64 format
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

}
