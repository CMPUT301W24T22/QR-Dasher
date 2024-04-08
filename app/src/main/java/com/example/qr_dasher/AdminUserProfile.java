package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminUserProfile extends AppCompatActivity {

    private Button deleteUserButton, deleteProfilePictureButton, backButton;
    private Button signedUpEvents, attendedEvents, createdEvents;
    private TextView userName, userEmail, userDetails, userID;
    private ImageView profilePicture;
    private CheckBox geolocationCheckBox;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private CollectionReference eventsCollection;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");
        eventsCollection = db.collection("eventsCollection");

        deleteUserButton = findViewById(R.id.delete_button);
        deleteProfilePictureButton = findViewById(R.id.delete_image_button);
        backButton = findViewById(R.id.back_button);
        userName = findViewById(R.id.user_name);
        userDetails = findViewById(R.id.user_details);
        userEmail = findViewById(R.id.user_email);
        userID = findViewById(R.id.user_id);
        geolocationCheckBox = findViewById(R.id.geolocation);
        profilePicture = findViewById(R.id.image_upload);
        signedUpEvents = findViewById(R.id.signedup_events_button);
        attendedEvents = findViewById(R.id.attended_events_button);
        createdEvents = findViewById(R.id.created_events_button);

        // Retrieve userID from the intent extras
        String selectedUserID = getIntent().getStringExtra("userID");
        // Set userID text
        userID.setText(selectedUserID);
        // also get the role of the user: organizer or admin
        String role = getIntent().getStringExtra("role");
        if (role != null && role.equals("organizer")){
            deleteUserButton.setVisibility(View.GONE);
            deleteProfilePictureButton.setVisibility(View.GONE);
            geolocationCheckBox.setVisibility(View.GONE);
            signedUpEvents.setVisibility(View.GONE);
            attendedEvents.setVisibility(View.GONE);
            createdEvents.setVisibility(View.GONE);

        }
        retrieveUserFromFirebase(selectedUserID, new AdminUserProfile.FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    // Populate UI with retrieved user data
                    populateUIWithUserData(user);
                } else {
                    Toast.makeText(AdminUserProfile.this, "Failed to retrieve user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signedUpEvents.setOnClickListener(view -> {
            openAdminUserEventListFragment('s', user.getEventsSignedUp());
        });

        attendedEvents.setOnClickListener(view -> {
            openAdminUserEventListFragment('a', user.getEventsJoined());
        });

        createdEvents.setOnClickListener(view -> {
            openAdminUserEventListFragment('c', user.getEventsCreated());
        });

        deleteProfilePictureButton.setOnClickListener(view -> {
            // Update the user document in Firestore to set profile image to null
            usersCollection.document(selectedUserID)
                    .update("profile_image", null)
                    .addOnSuccessListener(aVoid -> {
                        // On successful deletion, update the UI and notify the user
                        Toast.makeText(AdminUserProfile.this, "Profile picture deleted successfully", Toast.LENGTH_SHORT).show();
                        // Update the UI to display the profile picture as null
                        profilePicture.setImageResource(android.R.color.transparent); // Clears the ImageView
                    })
                    .addOnFailureListener(e -> {
                        // Handle deletion failure
                        Toast.makeText(AdminUserProfile.this, "Failed to delete profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        deleteUserButton.setOnClickListener(view -> {
            // Remove user from associated events
            ArrayList<String> createdEvents = (ArrayList<String>) user.getEventsCreated();
            ArrayList<String> attendedEvents = (ArrayList<String>) user.getEventsJoined();
            ArrayList<String> signedUpEvents = (ArrayList<String>) user.getEventsSignedUp();

            for (String eventID : createdEvents) {
                eventsCollection.document(eventID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        ArrayList<String> attendeeList = event.getAttendee_list();
                        ArrayList<String> signUpList = event.getSignup_list();

                        // Remove event from attendees' joined events list
                        for (String userID : attendeeList) {
                            usersCollection.document(userID).get().addOnSuccessListener(userSnapshot -> {
                                if (userSnapshot.exists()) {
                                    User user = userSnapshot.toObject(User.class);
                                    if (user != null) {
                                        user.removeEventJoined(eventID);
                                        usersCollection.document(userID).set(user);
                                    }
                                }
                            });
                        }

                        // Remove event from sign-up lists of users
                        for (String userID : signUpList) {
                            usersCollection.document(userID).get().addOnSuccessListener(userSnapshot -> {
                                if (userSnapshot.exists()) {
                                    User user = userSnapshot.toObject(User.class);
                                    if (user != null) {
                                        user.removeEventSignedUp(eventID);
                                        usersCollection.document(userID).set(user);
                                    }
                                }
                            });
                        }

                        // Delete the event document from Firestore
                        eventsCollection.document(eventID).delete().addOnSuccessListener(aVoid -> {
                            Log.d("AdminUserProfile", "Event deleted successfully");
                        }).addOnFailureListener(e -> {
                            Log.e("AdminUserProfile", "Error deleting event: " + e.getMessage());
                        });
                    }
                });
            }


            for (String eventID : attendedEvents) {
                eventsCollection.document(eventID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        event.removeUserFromEvent(selectedUserID);
                        // Update the event in Firestore
                        eventsCollection.document(eventID).set(event);
                    }
                });
            }

            for (String eventID : signedUpEvents) {
                eventsCollection.document(eventID).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        event.removeUserFromEvent(selectedUserID);
                        // Update the event in Firestore
                        eventsCollection.document(eventID).set(event);
                    }
                });
            }

            // Delete the user document in Firestore
            usersCollection.document(selectedUserID)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // On successful deletion, go back to the previous activity
                        onBackPressed();
                        Toast.makeText(AdminUserProfile.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle deletion failure
                        Toast.makeText(AdminUserProfile.this, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void openAdminUserEventListFragment(char eventType, List<String> eventList) {
        // Create a bundle to pass the eventType character and eventList
        Bundle bundle = new Bundle();
        bundle.putChar("eventType", eventType);
        bundle.putStringArrayList("eventList", (ArrayList<String>) eventList);

        // Check if the eventList is empty
        if (eventList == null || eventList.isEmpty()) {
            // Show a toast message based on the eventType
            switch (eventType) {
                case 's':
                    Toast.makeText(this, "No signed up events", Toast.LENGTH_SHORT).show();
                    break;
                case 'a':
                    Toast.makeText(this, "No attended events", Toast.LENGTH_SHORT).show();
                    break;
                case 'c':
                    Toast.makeText(this, "No created events", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            // Open AdminUserEventListFragment and pass the bundle
            AdminUserEventListFragment eventListFragment = new AdminUserEventListFragment();
            eventListFragment.setArguments(bundle);
            eventListFragment.show(getSupportFragmentManager(), "AdminUserEventListFragment");
        }
    }

    private void populateUIWithUserData(User user) {
        String profileImageString = user.getProfile_image();
        Bitmap profile_picture = null;
        if (profileImageString != null) {
            profile_picture = Picture.convertStringtoBitmap(profileImageString);
        }
        String name = user.getName();
        String email = user.getEmail();
        String details = user.getDetails();
        boolean location = user.getLocation();

        userName.setText(name);
        userEmail.setText(email);
        userDetails.setText(details);
        geolocationCheckBox.setChecked(location);
        geolocationCheckBox.setEnabled(false);
        profilePicture.setImageBitmap(profile_picture);
    }

    public void retrieveUserFromFirebase(String userID, final FirebaseCallback callback) {
        // Retrieve the user document from Firestore using documentID (userID)
        usersCollection.document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, create a User object from the data
                        user = documentSnapshot.toObject(User.class);
                        // Optionally, set the userID in the User object
                        user.setUserId(Integer.parseInt(userID));
                        // Retrieve attended events
                        user.setEventsJoined((List<String>) documentSnapshot.get("eventsJoined"));
                        // Retrieve created events
                        user.setEventsCreated((List<String>) documentSnapshot.get("eventsCreated"));
                        // Retrieve signed-up events
                        user.setEventsSignedUp((List<String>) documentSnapshot.get("eventsSignedUp"));
                        // Invoke the callback with the retrieved user object
                        callback.onCallback(user);
                    } else {
                        // Document doesn't exist
                        Toast.makeText(AdminUserProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                        callback.onCallback(null); // Notify callback with null user
                    }
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    Toast.makeText(AdminUserProfile.this, "Error retrieving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onCallback(null); // Notify callback with null user
                });
    }

    public interface FirebaseCallback {
        void onCallback(User user);
    }

}