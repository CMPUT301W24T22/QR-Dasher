package com.example.qr_dasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity displays QR codes associated with the current user from Firestore.
 */
public class reuseQRcodes extends AppCompatActivity {
    public static final String EXTRA_QR_CODES = "extra_qr_codes";
    Button cancelButton;
    private FirebaseFirestore db;
    private SharedPreferences app_cache;
    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuse_qrcodes);

        // Initialize Firestore and SharedPreferences
        db = FirebaseFirestore.getInstance();
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Initialize UI components
        cancelButton = findViewById(R.id.cancel_button);

        // Set click listener for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Fetch and display QR codes associated with the user
        fetchQRcodes();
    }

    /**
     * Fetches QR codes associated with the current user from Firestore.
     */
    private void fetchQRcodes() {
        // Get the user ID from SharedPreferences
        int userId = app_cache.getInt("UserID", -1);

        // Query Firestore for QR codes associated with the user
        db.collection("eventsCollection")
                .whereEqualTo("attendee_qr.userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<String> qrCodes = new ArrayList<>();
                        // Iterate through the query results
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the QR code string from the document
                            String qrCodeString = document.getString("attendee_qr.qrImage");
                            if (qrCodeString != null) {
                                qrCodes.add(qrCodeString);
                            }
                        }
                        // Display the fetched QR codes
                        displayQRcodes(qrCodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to retrieve QR codes
                        LinearLayout container = findViewById(R.id.container);
                        container.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Displays QR codes in the UI.
     *
     * @param qrCodes List of QR code strings
     */
    // Inside displayQRcodes method, set onClickListener for each ImageView to select the QR code
    private void displayQRcodes(List<String> qrCodes) {
        LinearLayout container = findViewById(R.id.container);

        // Iterate through the list of QR code strings
        for (String qrCodeString : qrCodes) {
            if (qrCodeString != null) {
                // Decode the QR code string into a bitmap
                byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Create an ImageView to display the QR code bitmap
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(qrCodeBitmap);

                // Set layout parameters for the ImageView
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 16);
                imageView.setLayoutParams(layoutParams);

                // Set onClickListener for selecting the QR code
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Pass the selected QR code back to the calling activity
                        Intent intent = new Intent();
                        intent.putExtra("selectedQRCode", qrCodeString);
                        setResult(RESULT_OK, intent);
                        finish(); // Finish the activity and return to the calling activity
                    }
                });

                // Add the ImageView to the container layout
                container.addView(imageView);
            }
        }
    }

}



