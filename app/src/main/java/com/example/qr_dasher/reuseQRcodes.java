package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class reuseQRcodes extends AppCompatActivity {
    public static final String EXTRA_QR_CODES = "extra_qr_codes";
    Button cancelButton;
    private FirebaseFirestore db;
    private SharedPreferences app_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuse_qrcodes);

        db = FirebaseFirestore.getInstance();
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);


        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchQRcodes();
    }

    private void fetchQRcodes() {
        int userId = app_cache.getInt("UserID", -1);

        db.collection("eventsCollection")
                .whereEqualTo("attendee_qr.userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<String> qrCodes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String qrCodeString = document.getString("attendee_qr.qrImage");
                            if (qrCodeString != null) {
                                qrCodes.add(qrCodeString);
                            }
                        }
                        displayQRcodes(qrCodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to retrieve qr codes
                        LinearLayout container = findViewById(R.id.container);
                        container.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });
    }

    private void displayQRcodes(List<String> qrCodes) {
        LinearLayout container = findViewById(R.id.container);

        for (String qrCodeString : qrCodes) {
            if (qrCodeString != null) {
                byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(qrCodeBitmap);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 16);

                imageView.setLayoutParams(layoutParams);
                container.addView(imageView);
            }
        }
    }
}
