
package com.example.qr_dasher;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

// zxing lib
import com.google.firebase.FirebaseApp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

// adding firebase references
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CreateEventOrganizer extends AppCompatActivity {
    public static final String EXTRA_QR_CODES = "extra_qr_codes";
    private ImageView qrImage, promotionalImage;
    private Button generateQR, generatePromotionalQR, displayQRcodes;
    private EditText eventName, eventDetails;

    // TO:DO change the default userID with the one in Cache
    int userID;

//    private int event_id;
//    private String name;
//    private String details;

    // Firebase link
    private FirebaseFirestore db;
    private CollectionReference events;


    //private CollectionReference generatedQRCodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_organizer);
        qrImage = findViewById(R.id.qrCode); // image
        generateQR = findViewById(R.id.generateQR); // button
        generatePromotionalQR = findViewById(R.id.generatePromotionalQR);
        eventName = findViewById(R.id.eventName); // event Name
        eventDetails = findViewById(R.id.details); // event Name

        displayQRcodes = findViewById(R.id.displayQRcodes);

        // Create Collection in Firebase
        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();

        events = db.collection("events");


        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputText.getText())){
                    // to check if there is some text or not
                    String text = inputText.getText().toString();
                    Bitmap qrCode = createBitmap(text);
                    qrImage.setImageBitmap(qrCode);

                    String qrCodeString = convertQRtoString(qrCode);
                    generatedQRCodes.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                            @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore", error.toString());
                                return;
                            }
                            if (querySnapshots != null) {
                                AddQRtoFirebase(qrCodeString,text);

                            }
                        }
                    });


                }
            }
        });

        displayQRcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQRFromFirebase();
            }
        });

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

    private void AddQRtoFirebase(String qrCodeString, String eventID){
        // eventID will be the ID linked with the QR code/ Content of the QR code
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Create a Hashmap to store the QR code string
        Map<String, Object> qrCodeData = new HashMap<>();
        qrCodeData.put("image", qrCodeString);

        db.collection("generatedQRCodes")
                .document(eventID)
                .set(qrCodeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("QR", "Successfully uploaded to firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("QR", "Couldn't be uploaded to firestore");
                        e.printStackTrace();
                    }
                });
    }

    public void getQRFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("generatedQRCodes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> qrCodes = new ArrayList<>(); // list of qrCodes
                    //Iterate through the list
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> qrCodeData = documentSnapshot.getData();
                        String qrCodeString = (String) qrCodeData.get("image");
                        qrCodes.add(qrCodeString); // append to the list
                    }
                    // Process the retrieved QR codes, for example, display them or do further operations
                    Intent intent = new Intent(CreateEventOrganizer.this, reuseQRcodes.class);
                    intent.putStringArrayListExtra(EXTRA_QR_CODES, (ArrayList<String>) qrCodes);
                    startActivity(intent);
                })
                // Once we have all the qr codes, we can call in displayQRcodes activity
                //Intent intent = new Intent(MainActivity.this, displayQRcodes.class);
                //intent.putStringArrayListExtra(displayQRcodes.EXTRA_QR_CODES, (ArrayList<String>) qrCodes);
                // DisplayQRActivity.EXTRA_QR_CODES is the list of QRcodes we got from firebase
                //startActivity(intent);

                .addOnFailureListener(e -> {
                    Log.d("QR", "Failed to retrieve QR codes from Firestore");
                    e.printStackTrace();
                });

    }

//    private void displayQRcodes(List<String> qrCodes){
//        for (String qrCodeString: qrCodes){
//            // Converting it back to Bitmap from base 64
//            byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
//            Bitmap qrCodeBitmap  = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            ImageView imageView = new ImageView(this);
//            imageView.setImageBitmap(qrCodeBitmap);
//            setContentView(imageView);
//        }
//    }
//



}