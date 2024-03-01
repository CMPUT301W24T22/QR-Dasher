package com.example.myapplication;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// zxing lib
import com.google.firebase.FirebaseApp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.w3c.dom.Text;

// adding firebase references
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    ImageView qrimage;
    Button generateQR;
    EditText inputText;

    // Firebase link
    private FirebaseFirestore db;
    private CollectionReference generatedQRCodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrimage = findViewById(R.id.qrCode); // image
        generateQR = findViewById(R.id.generateQR); // button
        inputText = findViewById(R.id.inputText); // TextView

        // Create Collection in Firebase
        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();

        generatedQRCodes = db.collection("generatedQRCodes");

        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputText.getText())){
                    // to check if there is some text or not
                    String text = inputText.getText().toString();
                    Bitmap qrCode = createBitmap(text);
                    qrimage.setImageBitmap(qrCode);

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

}