package com.example.qr_dasher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Activity for attendees of an event. Allows attendees to view notifications, edit their profile,
 * and scan a QR code to join an event.
 */
public class Attendee extends AppCompatActivity implements LocationListener {
    private Button notificationButton, editProfileButton, qrCodeButton, browseEvents;
    private ListView scannedEvents, signedUpevents;
    private List<String> scannedEventNames, scannedEventIds, scannedEventDetails, scannedEventPoster;
    private List<String> signedEventNames, signedEventIds, signedEventDetails, signedEventPoster;
    private List<Timestamp> scannedEventTimestamps, signedEventTimestamps;
    private SharedPreferences app_cache;
    private GeoPoint geoPoint;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocationPermissions();
        // Check if permissions are granted and request location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted, request location updates
            requestLocationUpdates();
        } else {
            // Permissions are not granted, show a toast message
            Toast.makeText(this, "Location permissions are not granted", Toast.LENGTH_SHORT).show();
            // You can handle this according to your app's logic, such as requesting permissions again or displaying a message to the user
        }

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
                // get the latest checked and signed events
                getCheckedSignedEvents(userId);
            }
        });


    }

    protected void onDestroy() {
        super.onDestroy();
        // Unregister location listener to avoid memory leaks
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update geoPoint with the latest location
        geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
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
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        int userId = app_cache.getInt("UserID", -1);

        getCheckedSignedEvents(userId);
        if (requestCode == 1) { // Check if the result is from ScanQR activity
            Log.d("Scan", "scan");
            if (resultCode == RESULT_OK) {
                String scannedText = data.getStringExtra("scannedText");
                Toast.makeText(this, "Scanning successful " + scannedText, Toast.LENGTH_SHORT).show();
                if (scannedText != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference eventsCollection = db.collection("eventsCollection");

                    // Query Firestore to find the document containing the scanned text in either attendee_qr or promotional_qr
                    eventsCollection
                            .whereEqualTo("attendee_qr.content", scannedText)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                // Handle documents found in attendee_qr
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Map<String, Object> attendeeQR = (Map<String, Object>) documentSnapshot.get("attendee_qr");
                                    if (attendeeQR != null && attendeeQR.get("content").equals(scannedText)) {
                                        // Proceed with attendee QR action
                                        String eventId = String.valueOf(attendeeQR.get("event_id"));
                                        updateFirebase(eventId);
                                        return;
                                    }
                                }

                                // If not found in attendee_qr, query promotional_qr separately
                                eventsCollection
                                        .whereEqualTo("promotional_qr.content", scannedText)
                                        .get()
                                        .addOnSuccessListener(promotionalQueryDocumentSnapshots -> {
                                            // Handle documents found in promotional_qr
                                            for (QueryDocumentSnapshot promotionalDocumentSnapshot : promotionalQueryDocumentSnapshots) {
                                                Map<String, Object> promotionalQR = (Map<String, Object>) promotionalDocumentSnapshot.get("promotional_qr");
                                                if (promotionalQR != null && promotionalQR.get("content").equals(scannedText)) {
                                                    // Proceed with promotional QR action
                                                    displayEventSignUpPage(scannedText);
                                                    return;
                                                }
                                            }

                                            // Handle case where scanned text is not found in any document
                                            Toast.makeText(this, "Scanned text not found in any document: " + scannedText, Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle any errors that may occur during the promotional_qr query
                                            Log.e("Firestore", "Error getting promotional documents: ", e);
                                            // Handle case where there's an error in querying Firestore for promotional_qr
                                            Toast.makeText(this, "Error querying promotional Firestore", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors that may occur during the attendee_qr query
                                Log.e("Firestore", "Error getting attendee documents: ", e);
                                // Handle case where there's an error in querying Firestore for attendee_qr
                                Toast.makeText(this, "Error querying attendee Firestore", Toast.LENGTH_SHORT).show();
                            });

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


    private void getlocation() {
        int userId = app_cache.getInt("UserID", -1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            // Logic to handle location object
            double latitude = lastKnownLocation.getLatitude();
            double longitude = lastKnownLocation.getLongitude();
            Toast.makeText(Attendee.this, String.format("Retrieved location: Latitude - %f, Longitude - %f", latitude, longitude), Toast.LENGTH_SHORT).show();

            geoPoint = new GeoPoint(latitude, longitude);
            Toast.makeText(Attendee.this, "GeoPoint set: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude(), Toast.LENGTH_SHORT).show();

            Toast.makeText(Attendee.this, "Calling addLocation", Toast.LENGTH_SHORT).show();
            addLocation(userId, geoPoint);

        }
    }

    private void requestLocationUpdates() {
        // Check if location manager is not null
        if (locationManager != null) {
            // Request location updates for all available providers
            for (String provider : locationManager.getProviders(true)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(provider, 1000L, (float) 0, (LocationListener) this);
            }
        } else {
            Toast.makeText(this, "Location manager is null", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);

        }
    }


    private void checkLocation(String docId, LocationCheckCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(docId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean locationEnabled = false; // default value
                    if (documentSnapshot.exists()) {
                        locationEnabled = documentSnapshot.getBoolean("location");
                        if (!locationEnabled) {
                            Toast.makeText(Attendee.this, "Location feature is not enabled for this user.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User document doesn't exist
                        Toast.makeText(Attendee.this, "User document not found in Firestore.", Toast.LENGTH_SHORT).show();
                    }
                    // Invoke the callback with the boolean value
                    callback.onLocationChecked(locationEnabled);
                })
                .addOnFailureListener(e -> {
                    // Error fetching user document
                    Toast.makeText(Attendee.this, "Failed to fetch user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Invoke the callback with a default value (false) indicating failure
                    callback.onLocationChecked(false);
                });
    }



    private void addLocation(Integer userId, GeoPoint geoPoint) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(String.valueOf(userId))
                .update("geoPoint", geoPoint)
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


    private void displayEventSignUpPage(String pQRcontent) {
        // Remove "p" and get the event info from firebase
        // Start EventSignUpPage Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventIdPromo = pQRcontent.substring(1);
        Log.d("pQRcontent", pQRcontent);
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
                        bundle.putString("eventDetail", detail);


                        String eventId = String.valueOf(event.getEvent_id());
                        bundle.putString("eventId", eventId);

                        boolean signUpBool = true;
                        bundle.putBoolean("signUpBool", signUpBool);
                        
                        // TODO /////////////////////////////////
                        
                        // Converting timeStamp to date to put in bundle
                        Timestamp eventTimestamp = event.getTimestamp();
                        Date date = eventTimestamp.toDate();
                        bundle.putSerializable("timestamp", date);


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
                        assert event != null;
                        List<String> attendeeList = new ArrayList<>(event.getAttendee_list());
                        // Use a Set to count unique attendees
                        Set<String> uniqueAttendees = new HashSet<>(attendeeList);
                        int uniqueAttendeeCount = uniqueAttendees.size();
                        int maxAttendees = event.getMaxAttendees();

                        String userIdStr = String.valueOf(userId);

                        if ((uniqueAttendeeCount < maxAttendees) || (maxAttendees == -1)) {
                            // Add the attendee to the event's attendee list

                            attendeeList.add(userIdStr);
                            event.setAttendee_list(new ArrayList<>(attendeeList));
                            updateFirebaseEvent(eventID, event); // Update the event in Firestore
                            Toast.makeText(Attendee.this, "Joined event successfully!", Toast.LENGTH_SHORT).show();

                            checkLocation(userIdStr, new LocationCheckCallback() {
                                @Override
                                public void onLocationChecked(boolean locationEnabled) {
                                    // Use the boolean value here
                                    if (locationEnabled) {
                                        getlocation();
                                    } else {
                                        Toast.makeText(Attendee.this, "Cannot add location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Attendee.this, "Event is full", Toast.LENGTH_SHORT).show();
                        }
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

    private void getEventDetails(List<String> checkedInEvents, List<String> eventsSignedUp) {
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
                bundle.putString("eventDetail", detail);

                String eventId = scannedEventIds.get(position);
                bundle.putString("eventId", eventId);


                // Converting timeStamp to date to put in bundle
                Timestamp eventTimestamp = scannedEventTimestamps.get(position);
                Date date = eventTimestamp.toDate();
                bundle.putSerializable("timestamp", date);

//                if (signedEventPoster != null &&!scannedEventPoster.get(position).isEmpty()) {
//                    String eventPoster = scannedEventPoster.get(position);
//                    bundle.putString("Poster",eventPoster);
//                }
                boolean signUpBool = false;
                bundle.putBoolean("signUpBool", signUpBool);

                // TODO ////////////////////////
                boolean checkAnnounce = true;
                bundle.putBoolean("checkAnnounce",checkAnnounce);
                
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
                bundle.putString("eventDetail", detail);



                String eventId = signedEventIds.get(position);
                bundle.putString("eventId", eventId);

                // Converting timeStamp to date to put in bundle
                Timestamp eventTimestamp = signedEventTimestamps.get(position);
                Date date = eventTimestamp.toDate();
                bundle.putSerializable("timestamp", date);

                boolean signUpBool = false;
                bundle.putBoolean("signUpBool", signUpBool);

                
                //TODO  ///////////////////////////
                boolean checkAnnounce = true;
                bundle.putBoolean("checkAnnounce",checkAnnounce);
                
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
    interface LocationCheckCallback {
        void onLocationChecked(boolean locationEnabled);
    }
}
