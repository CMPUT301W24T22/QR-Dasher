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
 * This activity represents an attendee's dashboard.
 */
public class Attendee extends AppCompatActivity {
    private Button notificationButton, editProfileButton, qrCodeButton;
    private SharedPreferences app_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee);

        // Get reference to SharedPreferences for storing user data
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Initialize buttons
        notificationButton = findViewById(R.id.notification_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        qrCodeButton = findViewById(R.id.qr_button);

        // Set onClickListener for notification button
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Notifications activity
                Intent intent = new Intent(Attendee.this, Notifications.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditProfile activity
                Intent intent = new Intent(Attendee.this, EditProfile.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for QR code button
        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanQR activity for scanning QR code
                Intent intent = new Intent(Attendee.this, ScanQR.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from ScanQR activity
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Retrieve scanned text
                String scannedText = data.getStringExtra("scannedText");
                Toast.makeText(this, "Scanning successful " + scannedText, Toast.LENGTH_SHORT).show();
                // Update Firebase with scanned event ID
                updateFirebase(scannedText);
            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Updates Firebase with the scanned event ID and the attendee's information.
     *
     * @param event_id The ID of the scanned event.
     */
    private void updateFirebase(String event_id) {
        int userId = app_cache.getInt("UserID", -1);
        int eventID = Integer.parseInt(event_id);
        AtomicReference<User> user = new AtomicReference<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update user information in Firestore
        db.collection("users")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        user.set(documentSnapshot.toObject(User.class));
                        user.get().addEventsJoined(event_id); // Add the event ID to the user's eventsJoined list
                        updateFirebaseUser(documentSnapshot.getId(), user.get()); // Update the user in Firestore
                    } else {
                        Log.d("Attendee", "No user found with UserId: " + userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to retrieve User from Firestore");
                    e.printStackTrace();
                });

        // Update event information in Firestore
        db.collection("events")
                .whereEqualTo("event_id", eventID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        Event event = documentSnapshot.toObject(Event.class);
                        event.addAttendee(user.get()); // Add the user ID to the event's attendee list
                        updateFirebaseEvent(documentSnapshot.getId(), event); // Update the event in Firestore
                    } else {
                        Log.d("Attendee", "No event found with EventId: " + userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to retrieve Event from Firestore");
                    e.printStackTrace();
                });
    }

    /**
     * Updates user information in Firebase.
     *
     * @param userId The ID of the user.
     * @param user   The user object to be updated.
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
     * Updates event information in Firebase.
     *
     * @param eventId The ID of the event.
     * @param event   The event object to be updated.
     */
    private void updateFirebaseEvent(String eventId, Event event) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(eventId)
                .update("attendee_list", event.getAttendee_list())
                .addOnSuccessListener(aVoid -> Log.d("Attendee", "Event updated successfully"))
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to update event in Firestore");
                    e.printStackTrace();
                });
    }
}

