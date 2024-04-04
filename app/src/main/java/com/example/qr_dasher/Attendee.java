package com.example.qr_dasher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Activity for attendees of an event. Allows attendees to view notifications, edit their profile,
 * and scan a QR code to join an event.
 */
public class Attendee extends AppCompatActivity {
    private Button notificationButton, editProfileButton, qrCodeButton, browseEvents;
    private ListView scannedEvents, signedUpevents;
    private List<String> scannedEventNames, scannedEventIds, scannedEventDetails, scannedEventPoster;
    private List<String> signedEventNames, signedEventIds, signedEventDetails, signedEventPoster;
    private List<Timestamp> scannedEventTimestamps, signedEventTimestamps;
    private SharedPreferences app_cache;
    private GeoPoint geoPoint;
    private FusedLocationProviderClient fusedLocationClient;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    /**
     * Initializes the activity and sets up UI components and listeners.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee);
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        int userId = app_cache.getInt("UserID", -1);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCheckedSignedEvents(userId);

        notificationButton = findViewById(R.id.notification_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        qrCodeButton = findViewById(R.id.qr_button);
        browseEvents = findViewById(R.id.browseEvents);
        scannedEvents = findViewById(R.id.scanned_events_listview);
        signedUpevents = findViewById(R.id.signed_up_events_listview);
        browseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Attendee.this, BrowseEvents.class);
                startActivity(intent);
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(Attendee.this, Notifications.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Attendee.this, EditProfile.class);
                startActivity(intent);
            }
        });

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
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
            Log.d("Scan","scan");
            if (resultCode == RESULT_OK) {
                String scannedText = data.getStringExtra("scannedText");
                Toast.makeText(this, "Scanning successful " + scannedText, Toast.LENGTH_SHORT).show();

                // Determine if it is promotional or checkin QR
                if (scannedText!=null) {
                    if (scannedText.charAt(0) == 'p') {
                        // Promotional QR
                        Log.d("QR Scanning", "Promotional Detected");
                        displayEventSignUpPage(scannedText);
                    } else {
                        // Checkin QR
                        Log.d("QR Scanning", "Checkin Detected");
                        updateFirebase(scannedText);
                    }
                }
            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when the activity is resumed
        int userId = app_cache.getInt("UserID", -1);  // Retrieve UserID again
        getCheckedSignedEvents(userId);
    }


    private void displayEventSignUpPage(String pQRcontent){
        // Remove "p" and get the event info from firebase
        // Start EventSignUpPage Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventIdPromo = pQRcontent.substring(1);
        Log.d("pQRcontent",pQRcontent);
        Log.d("p without p ", eventIdPromo);
        db.collection("eventsCollection")
                .document(eventIdPromo)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        // Create bundle and start activity
                        Bundle bundle = new Bundle();
                        String eventName = event.getName();
                        bundle.putString("eventName", eventName);

                        String detail = event.getDetails();
                        bundle.putString("eventDetail",detail);

                        String eventId = String.valueOf(event.getEvent_id());
                        bundle.putString("eventId", eventId);

                        boolean signUpBool = true;
                        bundle.putBoolean("signUpBool",signUpBool);

                        // Converting timeStamp to date to put in bundle
                        Timestamp eventTimestamp = event.getTimestamp();
                        Date date = eventTimestamp.toDate();
                        bundle.putSerializable("timestamp",date);


                        //Integer eventId = Integer.parseInt(eventIdStr);
                        // Start new activity with the event name
                        Intent intent = new Intent(Attendee.this, EventSignUpPage.class);
                        //intent.putExtra("eventName", eventName);
                        //intent.putExtra("event_id", eventId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Log.d("Attendee", "No event found with EventId: ");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Attendee", "Failed to retrieve Event from Firestore");
                    e.printStackTrace();
                });
    }


    private void updateFirebase(String event_id){
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
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                            return;
                        }

                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            // Logic to handle location object
                                            double latitude = location.getLatitude();
                                            double longitude = location.getLongitude();
                                            Toast.makeText(Attendee.this, String.format("Retrieved location: Latitude - %f, Longitude - %f", latitude, longitude), Toast.LENGTH_SHORT).show();

                                            geoPoint = new GeoPoint(latitude, longitude);
                                            Toast.makeText(Attendee.this, "GeoPoint set: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude(), Toast.LENGTH_SHORT).show();

                                            user.setGeoPoint(geoPoint);

                                            Toast.makeText(Attendee.this, "Calling addLocation", Toast.LENGTH_SHORT).show();
                                            addLocation(userId, user);
                                        } else {
                                            // Location is null, could not get location
                                            Toast.makeText(Attendee.this, "Location is null. Could not get Location", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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
                        event.addAttendee(userId_str); // Add the user ID to the event's attendee list
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

    private void getCheckedSignedEvents(Integer userId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("userId", userId) // Filter documents where "userId" field matches the provided userId
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) { // Check if query results are not empty
                            QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);

                            // Get the list of signed in and checked in events
                            List<String> eventsCheckedIn = (List<String>) documentSnapshot.get("eventsJoined");
                            List<String> eventsSignedUp = (List<String>) documentSnapshot.get("eventsSignedUp");

                            // Display the list of attendees
                            getEventDetails(eventsCheckedIn, eventsSignedUp);
                        } else {
                            // Show a toast message if event details are not found
                            Toast.makeText(Attendee.this, "Event details not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure, such as printing the stack trace
                        e.printStackTrace();
                    }
                });
    }
    private void getEventDetails(List<String> checkedInEvents,List<String> eventsSignedUp) {
        // Once we have the event ids, we can get the other details
        // Getting the current date > dealing with only new events

        scannedEventTimestamps = new ArrayList<>();
        scannedEventPoster = new ArrayList<>();
        scannedEventIds = new ArrayList<>();
        scannedEventDetails = new ArrayList<>();
        scannedEventNames = new ArrayList<>();

        Date currentTime = new Date();
        Timestamp currentTimestamp = new Timestamp(currentTime);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (checkedInEvents != null && !checkedInEvents.isEmpty()) {
            db.collection("eventsCollection")
                    .whereIn(FieldPath.documentId(), checkedInEvents) // Filtering out events
                    //.whereGreaterThan("timestamp", currentTimestamp)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        List<DocumentSnapshot> filteredEvents = new ArrayList<>();

                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                // Handle errors
                                scannedEvents.setVisibility(View.GONE);
                                e.printStackTrace();
                                return;
                            }

                            // Update the list with new data
                            scannedEventNames.clear();
                            scannedEventIds.clear();
                            scannedEventDetails.clear();
                            scannedEventPoster.clear();
                            scannedEventTimestamps.clear();

                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                String eventName = documentSnapshot.getString("name");

                                if (documentSnapshot.contains("event_id")) {
                                    Long eventIdLong = documentSnapshot.getLong("event_id");
                                    if (eventIdLong != null) {
                                        // removing events where the user has signed up/ checked in
                                        String eventId = String.valueOf(eventIdLong);
                                        String eventDetail = documentSnapshot.getString("details");
                                        Timestamp eventTime = documentSnapshot.getTimestamp("timestamp");
                                        String eventPoster = documentSnapshot.getString("Poster");
                                        if (eventTime.compareTo(currentTimestamp) > 0) {
                                            scannedEventNames.add(eventName);
                                            scannedEventIds.add(eventId);
                                            scannedEventDetails.add(eventDetail);
                                            scannedEventTimestamps.add(eventTime);
                                            if (eventPoster != null) {
                                                scannedEventPoster.add(eventPoster);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
        // repeat for signed up events
        signedEventTimestamps = new ArrayList<>();
        signedEventPoster = new ArrayList<>();
        signedEventIds = new ArrayList<>();
        signedEventDetails = new ArrayList<>();
        signedEventNames = new ArrayList<>();
        if (eventsSignedUp != null && !eventsSignedUp.isEmpty()) {

            db.collection("eventsCollection")
                    .whereIn(FieldPath.documentId(), eventsSignedUp) // Filtering out events
                    //.whereGreaterThan("timestamp", currentTimestamp)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                // Handle errors
                                signedUpevents.setVisibility(View.GONE);
                                e.printStackTrace();
                                return;
                            }

                            // Update the list with new data
                            signedEventNames.clear();
                            signedEventIds.clear();
                            signedEventDetails.clear();
                            signedEventPoster.clear();
                            signedEventTimestamps.clear();

                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                String eventName = documentSnapshot.getString("name");

                                if (documentSnapshot.contains("event_id")) {
                                    Long eventIdLong = documentSnapshot.getLong("event_id");
                                    if (eventIdLong != null) {
                                        // removing events where the user has signed up/ checked in
                                        String eventId = String.valueOf(eventIdLong);
                                        String eventDetail = documentSnapshot.getString("details");
                                        Timestamp eventTime = documentSnapshot.getTimestamp("timestamp");
                                        String eventPoster = documentSnapshot.getString("Poster");
                                        if (eventTime.compareTo(currentTimestamp) > 0) {
                                            signedEventNames.add(eventName);
                                            signedEventIds.add(eventId);
                                            signedEventDetails.add(eventDetail);
                                            signedEventTimestamps.add(eventTime);
                                            if (eventPoster != null) {
                                                signedEventPoster.add(eventPoster);
                                            }
                                        }
                                    }
                                }
                            }
                            // Display the list of event names in the ListView
                            displayEventList(scannedEventNames, signedEventNames);

                        }
                    });


        }
    }

    // First get the events in separate lists then use those events to get

    private void displayEventList(List<String> scannedEventNames, List<String> signedEventNames) {
        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, scannedEventNames);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        scannedEvents.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, signedEventNames);
        adapter2.notifyDataSetChanged();
        // Set the adapter to the ListView
        signedUpevents.setAdapter(adapter2);

        // Set item click listener
        scannedEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event name
                // Create a bundle with all the required data needed for the sign up page
                Bundle bundle = new Bundle();

                String eventName = scannedEventNames.get(position);
                bundle.putString("eventName", eventName);

                String detail = scannedEventDetails.get(position);
                bundle.putString("eventDetail",detail);

                String eventId = scannedEventIds.get(position);
                bundle.putString("eventId", eventId);

                // Converting timeStamp to date to put in bundle
                Timestamp eventTimestamp = scannedEventTimestamps.get(position);
                Date date = eventTimestamp.toDate();
                bundle.putSerializable("timestamp",date);

//                if (signedEventPoster != null &&!scannedEventPoster.get(position).isEmpty()) {
//                    String eventPoster = scannedEventPoster.get(position);
//                    bundle.putString("Poster",eventPoster);
//                }
                boolean signUpBool = false;
                bundle.putBoolean("signUpBool",signUpBool);


                //Integer eventId = Integer.parseInt(eventIdStr);
                // Start new activity with the event name
                Intent intent = new Intent(Attendee.this, EventSignUpPage.class);
                //intent.putExtra("eventName", eventName);
                //intent.putExtra("event_id", eventId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        signedUpevents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event name
                // Create a bundle with all the required data needed for the sign up page
                Bundle bundle = new Bundle();

                String eventName = signedEventNames.get(position);
                bundle.putString("eventName", eventName);

                String detail = signedEventDetails.get(position);
                bundle.putString("eventDetail",detail);

                String eventId = signedEventIds.get(position);
                bundle.putString("eventId", eventId);

                // Converting timeStamp to date to put in bundle
                Timestamp eventTimestamp = signedEventTimestamps.get(position);
                Date date = eventTimestamp.toDate();
                bundle.putSerializable("timestamp",date);

                boolean signUpBool = false;
                bundle.putBoolean("signUpBool",signUpBool);

//                if (signedEventPoster != null && !signedEventPoster.get(position).isEmpty()) {
//                    String eventPoster = signedEventPoster.get(position);
//                    bundle.putString("Poster",eventPoster);
//                }


                //Integer eventId = Integer.parseInt(eventIdStr);
                // Start new activity with the event name
                Intent intent = new Intent(Attendee.this, EventSignUpPage.class);
                //intent.putExtra("eventName", eventName);
                //intent.putExtra("event_id", eventId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void addLocation(Integer docID, User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(String.valueOf(docID))
                .update("geoPoint", user.getGeoPoint())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Attendee", "GeoPoint added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Attendee", "Failed to add GeoPoint: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
    }
}
