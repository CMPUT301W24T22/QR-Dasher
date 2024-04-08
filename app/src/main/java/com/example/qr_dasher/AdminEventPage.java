package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity to display detailed information about an event retrieved from Firebase Firestore.
 * This activity allows administrators to view event details, manage attendees, delete events, and view QR codes.
 */
public class AdminEventPage extends AppCompatActivity {

    private Button deleteEventButton, deleteEventPosterButton, backButton;
    private Button promotionalQRButton, attendeeQRButton;
    private Button signedUpAttendeesButton, attendeeListButton;
    private TextView eventName, eventOrganizer, eventDetails, eventID;
    private ImageView eventPoster;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private CollectionReference eventsCollection;
    private Event event;
    private String promotionalQR, attendeeQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_page);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");
        eventsCollection = db.collection("eventsCollection");

        deleteEventButton = findViewById(R.id.delete_button);
        deleteEventPosterButton = findViewById(R.id.delete_image_button);
        backButton = findViewById(R.id.back_button);
        eventName = findViewById(R.id.event_name);
        eventDetails = findViewById(R.id.event_details);
        eventOrganizer = findViewById(R.id.event_organizer);
        eventID = findViewById(R.id.event_id);
        eventPoster = findViewById(R.id.event_poster);
        promotionalQRButton = findViewById(R.id.promotional_qr_button);
        attendeeQRButton = findViewById(R.id.attendee_qr_button);
        signedUpAttendeesButton = findViewById(R.id.signed_up_button);
        attendeeListButton = findViewById(R.id.attendees_button);

        // Retrieve userID from the intent extras
        String selectedEventID = getIntent().getStringExtra("eventID");
        // Set userID text
        eventID.setText(selectedEventID);

        retrieveEventFromFirebase(selectedEventID, new FirebaseCallback() {
            @Override
            public void onCallback(Event event) {
                if (event != null) {
                    // Populate UI with retrieved user data
                    populateUIWithEventData(event);
                } else {
                    Toast.makeText(AdminEventPage.this, "Failed to retrieve user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        attendeeListButton.setOnClickListener(view -> {
            openAdminEventUserListFragment('a',event.getAttendee_list());
        });

        signedUpAttendeesButton.setOnClickListener(view -> {
            openAdminEventUserListFragment('s',event.getSignup_list());
        });

        promotionalQRButton.setOnClickListener(view -> {
            // Check if promotional QR image exists
            if (event.getPromotional_qr() != null && event.getPromotional_qr().getQrImage() != null) {
                // Create a new instance of QRImageFragment
                QRImageFragment qrImageFragment = new QRImageFragment();
                // Pass the promotional QR bitmap to the fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("qrBitmap", Picture.convertStringtoBitmap(event.getPromotional_qr().getQrImage()));
                bundle.putString("qrType", "Promotional QR");
                bundle.putInt("eventID",event.getEvent_id());
                bundle.putBoolean("hideButtons", true); // Set flag to hide buttons
                qrImageFragment.setArguments(bundle);
                // Display the fragment
                qrImageFragment.show(getSupportFragmentManager(), "PromotionalQRFragment");
            } else {
                // Show toast message indicating that promotional QR image doesn't exist
                Toast.makeText(AdminEventPage.this, "Promotional QR image does not exist", Toast.LENGTH_SHORT).show();
            }
        });

        attendeeQRButton.setOnClickListener(view -> {
            // Create a new instance of QRImageFragment
            QRImageFragment qrImageFragment = new QRImageFragment();
            // Pass the attendee QR bitmap to the fragment
            Bundle bundle = new Bundle();
            bundle.putParcelable("qrBitmap", Picture.convertStringtoBitmap(event.getAttendee_qr().getQrImage()));
            bundle.putString("qrType", "Attendee QR");
            bundle.putInt("eventID",event.getEvent_id());
            bundle.putBoolean("hideButtons", true); // Set flag to hide buttons
            qrImageFragment.setArguments(bundle);
            // Display the fragment
            qrImageFragment.show(getSupportFragmentManager(), "AttendeeQRFragment");
        });


        deleteEventPosterButton.setOnClickListener(view -> {
            // Update the user document in Firestore to set profile image to null
            eventsCollection.document(selectedEventID)
                    .update("profile_image", null)
                    .addOnSuccessListener(aVoid -> {
                        // On successful deletion, update the UI and notify the user
                        Toast.makeText(AdminEventPage.this, "Event Poster deleted successfully", Toast.LENGTH_SHORT).show();
                        // Update the UI to display the profile picture as null
                        eventPoster.setImageResource(android.R.color.transparent); // Clears the ImageView
                    })
                    .addOnFailureListener(e -> {
                        // Handle deletion failure
                        Toast.makeText(AdminEventPage.this, "Failed to delete event poster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        deleteEventButton.setOnClickListener(view -> {
            ArrayList<String> attendeeList = event.getAttendee_list();
            ArrayList<String> signUpList = event.getSignup_list();

            for (String userID : attendeeList) {
                usersCollection.document(userID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        user.removeEventJoined(selectedEventID);
                        // Update the event in Firestore
                        eventsCollection.document(userID).set(user);
                    }
                });
            }

            for (String userID : signUpList) {
                usersCollection.document(userID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        user.removeEventSignedUp(selectedEventID);
                        // Update the event in Firestore
                        eventsCollection.document(userID).set(user);
                    }
                });
            }

            usersCollection.document(String.valueOf(event.getOrganizer())).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    user.removeEventCreated(selectedEventID);
                    // Update the event in Firestore
                    eventsCollection.document(String.valueOf(event.getOrganizer())).set(user);
                }
            });

            // Delete the user document in Firestore
            eventsCollection.document(selectedEventID)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // On successful deletion, go back to the previous activity
                        onBackPressed();
                        Toast.makeText(AdminEventPage.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle deletion failure
                        Toast.makeText(AdminEventPage.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        backButton.setOnClickListener(view -> onBackPressed());
    }

    /**
     * Open AdminEventUserListFragment to display a list of users.
     *
     * @param listType The type of user list ('s' for signed up attendees, 'a' for all attendees).
     * @param userList The list of user IDs.
     */
    private void openAdminEventUserListFragment(char listType, ArrayList<String> userList) {
        // Create a bundle to pass the eventType character and eventList
        Bundle bundle = new Bundle();
        bundle.putChar("listType", listType);
        bundle.putStringArrayList("userList", userList);

        // Check if the eventList is empty
        if (userList == null || userList.isEmpty()) {
            // Show a toast message based on the eventType
            switch (listType) {
                case 's':
                    Toast.makeText(this, "No one signed up", Toast.LENGTH_SHORT).show();
                    break;
                case 'a':
                    Toast.makeText(this, "No attendees", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            // Open AdminUserEventListFragment and pass the bundle
            AdminEventUserListFragment userListFragment = new AdminEventUserListFragment();
            userListFragment.setArguments(bundle);
            userListFragment.show(getSupportFragmentManager(), "AdminEventUserListFragment");
        }
    }

    /**
     * Populate UI with event data.
     *
     * @param event The event object containing data.
     */
    private void populateUIWithEventData(Event event) {
        eventName.setText(event.getName());
        eventOrganizer.setText(String.valueOf(event.getOrganizer()));
        eventDetails.setText(event.getDetails());
        if (event.getEventPoster() != null) {
            eventPoster.setImageBitmap(Picture.convertStringtoBitmap(event.getEventPoster()));
        }
    }

    /**
     * Retrieve event data from Firebase Firestore.
     *
     * @param eventID  The ID of the event to retrieve.
     * @param callback Callback to pass retrieved event data.
     */
    public void retrieveEventFromFirebase(String eventID, final FirebaseCallback callback) {
        // Retrieve the user document from Firestore using documentID (userID)
        eventsCollection.document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, create a User object from the data
                        event = documentSnapshot.toObject(Event.class);
                        // Optionally, set the eventID in the Event object
                        event.setEvent_id(Integer.parseInt(eventID));
                        // Retrieve attendee list
                        event.setAttendee_list((ArrayList<String>) documentSnapshot.get("attendee_list"));
                        // Retrieve sign up list
                        event.setSignup_list((ArrayList<String>) documentSnapshot.get("signup_list"));
                        // Retrieve organizer
                        event.setOrganizer(documentSnapshot.getLong("organizer").intValue());
                        //Retrieve QR Codes
                        attendeeQR = documentSnapshot.getString("attendee_qr.qrImage");
                        promotionalQR = documentSnapshot.getString("promotional_qr.qrImage");
                        // Retrieve Poster
                        event.setEventPoster(documentSnapshot.getString("Poster"));
                        // Invoke the callback with the retrieved user object
                        callback.onCallback(event);
                    } else {
                        // Document doesn't exist
                        Toast.makeText(AdminEventPage.this, "Event not found", Toast.LENGTH_SHORT).show();
                        callback.onCallback(null); // Notify callback with null event
                    }
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    Toast.makeText(AdminEventPage.this, "Error retrieving event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onCallback(null); // Notify callback with null event
                });
    }

    /**
     * Interface for Firebase callback.
     */
    public interface FirebaseCallback {
        void onCallback(Event event);
    }
}
