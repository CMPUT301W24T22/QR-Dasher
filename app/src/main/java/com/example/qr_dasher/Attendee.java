package com.example.qr_dasher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Activity for attendees of an event. Allows attendees to view notifications, edit their profile,
 * and scan a QR code to join an event.
 */
public class Attendee extends AppCompatActivity {
    private Button notificationButton, editProfileButton, qrCodeButton;
    private SharedPreferences app_cache;
    /**
     * Initializes the activity and sets up UI components and listeners.
     *
     * @param savedInstanceState Saved instance state bundle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee);

        // Initialize SharedPreferences
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Initialize buttons
        notificationButton = findViewById(R.id.notification_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        qrCodeButton = findViewById(R.id.qr_button);

        // Set OnClickListener for notificationButton
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Notifications activity
                Intent intent = new Intent(Attendee.this, Notifications.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for editProfileButton
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfile activity
                Intent intent = new Intent(Attendee.this, EditProfile.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for qrCodeButton
        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanQR activity for scanning QR code
                Intent intent = new Intent(Attendee.this, ScanQR.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    /**
     * Handles the result of the QR code scanning activity.
     *
     * @param requestCode The request code passed to startActivityForResult()
     * @param resultCode  The result code returned by the child activity
     * @param data        The Intent containing the result data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Check if the result is from ScanQR activity
            if (resultCode == RESULT_OK) {
                String scannedText = data.getStringExtra("scannedText");
                Toast.makeText(this, "Scanning successful " + scannedText, Toast.LENGTH_SHORT).show();
                updateFirebase(scannedText);
                // Implement firestore check
            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Update Firebase with scanned event ID.
     *
     * @param event_id The scanned event ID
     */
    private void updateFirebase(String event_id) {
        int userId = app_cache.getInt("UserID", -1);
        String eventID = event_id; // Assuming event_id is already the document ID

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update user document
        db.collection("users")
                .document(String.valueOf(userId))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        user.addEventsJoined(eventID); // Add the event ID to the user's eventsJoined list
                        updateFirebaseUser(String.valueOf(userId), user); // Update the user in Firestore
                    } else {
                        Log.d("Attendee", "No user found with UserId: " + userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to retrieve User from Firestore");
                    e.printStackTrace();
                });

        // Update event document
        db.collection("eventsCollection")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        event.addAttendee(userId); // Add the user ID to the event's attendee list
                        updateFirebaseEvent(eventID, event); // Update the event in Firestore
                    } else {
                        Log.d("Attendee", "No event found with EventId: " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to retrieve Event from Firestore");
                    e.printStackTrace();
                });
    }

    /**
     * Update user document in Firebase.
     *
     * @param userId The user ID
     * @param user   The User object to update
     */
    private void updateFirebaseUser(String userId, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .update("eventsJoined", user.getEventsJoined())
                .addOnSuccessListener(aVoid -> Log.d("Attendee", "User updated successfully"))
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to update user in Firestore");
                    e.printStackTrace();
                });
    }

    /**
     * Update event document in Firebase.
     *
     * @param eventId The event ID
     * @param event   The Event object to update
     */
    private void updateFirebaseEvent(String eventId, Event event) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventsCollection")
                .document(eventId)
                .update("attendee_list", event.getAttendee_list())
                .addOnSuccessListener(aVoid -> Log.d("Attendee", "Event updated successfully"))
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to update event in Firestore");
                    e.printStackTrace();
                });
    }
}

