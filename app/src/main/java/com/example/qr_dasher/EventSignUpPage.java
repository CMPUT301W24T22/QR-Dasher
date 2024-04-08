package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * This class represents a page where an attendee can sign up for an event and view the event details
 * and poster for an event they haven't attended or signed up for yet.
 */
public class EventSignUpPage extends AppCompatActivity {
    private TextView signUp_Name, signUp_Details, signUp_Time;
    private Button signup_button, announcement_button;
    private SharedPreferences app_cache;
    private String eventId;
    private ImageView signUp_poster;

    /**
     * Initializes the activity, sets up UI components and listeners,
     * and retrieves event details from the intent extras.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @SuppressLint("MissingInflatedId")          // TODO    ///////////////////
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
        announcement_button = findViewById(R.id.check_announcement);
        signUp_poster = findViewById(R.id.signUp_poster);

        if (extras != null) {
            // Extract data from the bundle
            String eventName = extras.getString("eventName");
            String eventDetail = extras.getString("eventDetail");
            eventId = extras.getString("eventId");
            Date date = (Date) extras.getSerializable("timestamp");
            boolean signUpBool = extras.getBoolean("signUpBool",false);
            boolean checkAnnounce = extras.getBoolean("checkAnnounce",false);

            if (!signUpBool){
                signup_button.setVisibility(View.GONE);
            }
            if (!checkAnnounce){
                announcement_button.setVisibility(View.GONE);
            }

            signUp_Name.setText(eventName);
            signUp_Details.setText(eventDetail);
            //Timestamp firestoreTimestamp = new Timestamp(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
            String formattedDate = sdf.format(date);
            signUp_Time.setText(formattedDate);

            String posterBase64 = extras.getString("eventPoster");
            if (posterBase64 != null) {
                // Convert Base64 string to bitmap
                Bitmap posterBitmap = base64ToBitmap(posterBase64);
                if (posterBitmap != null) {
                    // Compress and display the bitmap
                    Bitmap compressedBitmap = compressBitmap(posterBitmap);
                    if (compressedBitmap != null) {
                        signUp_poster.setImageBitmap(compressedBitmap);
                    } else {
                        // Log an error if compression fails
                        Log.e("EventSignUpPage", "Failed to compress bitmap");
                    }
                } else {
                    // Log an error if conversion fails
                    Log.e("EventSignUpPage", "Failed to convert Base64 string to bitmap");
                }
            }
        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateFirebase(eventId);
                finish();
            }
        });

        announcement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventSignUpPage.this, CheckAnnouncements.class);
                Log.d("EventSignUpPage", "Event ID 1  : " + eventId);

                intent.putExtra ("event_id", eventId);
                Log.d("EventSignUpPage", "Event ID 2 : " + eventId);

                startActivity(intent);
            }
        });

    }

    /**
     * Converts a Base64 encoded string to a Bitmap.
     *
     * @param base64String The Base64 encoded string representing the image
     * @return The Bitmap decoded from the Base64 string
     */
    private Bitmap base64ToBitmap(String base64String) {
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Compresses the given Bitmap to a specified width and height.
     *
     * @param bitmap The Bitmap to be compressed
     * @return The compressed Bitmap
     */
    private Bitmap compressBitmap(Bitmap bitmap) {
        // Calculate the compressed width and height as per your requirements
        int compressedWidth = 200;
        int compressedHeight = 200;
        return Bitmap.createScaledBitmap(bitmap, compressedWidth, compressedHeight, true);
    }


    /**
     * Updates the Firestore database with the attendee's sign-up information for an event.
     *
     * @param event_id The ID of the event to sign up for
     */
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

    /**
     * Updates the Firestore database with the user's sign-up information.
     *
     * @param userId The ID of the user to update
     * @param user   The updated User object
     */
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
