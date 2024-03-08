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


public class QRCode {
    private int event_id;
    private String content;
    private String QRImage;
    private int userID;
    private boolean promotional;

    public QRCode (int event_id, String content, int userID, boolean promotional){
        this.content = content;
        this.event_id = event_id;
        this.userID = userID;
        this.promotional = promotional;
        this.QRImage = createQRImage(content);
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
    public String getQrImage() {
        return this.QRImage;
    }


    public void setQrImage(String qrImage) {
        this.QRImage = qrImage;
    }
    public int getUserID() {
        return this.userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public boolean getQRType(){
        return this.promotional;
    }
    public void setQRType(boolean promotional){
        this.promotional = promotional;
    }


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
