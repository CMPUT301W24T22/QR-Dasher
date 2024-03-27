package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.text.SimpleDateFormat;


public class EventSignUpPage extends AppCompatActivity {
    private TextView signUp_Name, signUp_Details, signUp_Time;
    private Button signup_button;
    private SharedPreferences app_cache;
    private String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_sign_up_page);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        signup_button = findViewById(R.id.signup_button);
        signUp_Name = findViewById(R.id.signUp_Name);
        signUp_Details = findViewById(R.id.signUp_Details);
        signUp_Time = findViewById(R.id.signUp_Time);

        if (extras != null) {
            // Extract data from the bundle
            String eventName = extras.getString("eventName");
            String eventDetail = extras.getString("eventDetail");
            eventId = extras.getString("eventId");
            Date date = (Date) extras.getSerializable("timestamp");
            boolean signUpBool = extras.getBoolean("signUpBool",false);

            if (!signUpBool){
                signup_button.setVisibility(View.GONE);
            }

            signUp_Name.setText(eventName);
            signUp_Details.setText(eventDetail);
            //Timestamp firestoreTimestamp = new Timestamp(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
            String formattedDate = sdf.format(date);
            signUp_Time.setText(formattedDate);
        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateFirebase(eventId);
                finish();
            }
        });

    }

    private void updateFirebase(String event_id){

        int userId = app_cache.getInt("UserID", -1);
        String eventID = event_id; // Assuming event_id is already the document ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update User Document
        db.collection("users")
                .document(String.valueOf(userId))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        user.addEventsSignedUp(eventID); // Add the event ID to the user's eventsJoined list
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
                        Integer user_id = userId;
                        String userId_str = String.valueOf(user_id);
                        event.addAttendeeSignup(userId_str); // Add the user ID to the event's attendee list
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


    private void updateFirebaseUser(String userId, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .update("eventsSignedUp", user.getEventsSignedUp())
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
                .update("signup_list", event.getSignup_list())
                .addOnSuccessListener(aVoid -> Log.d("Attendee", "Event updated successfully"))
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to update event in Firestore");
                    e.printStackTrace();
                });
    }

}
