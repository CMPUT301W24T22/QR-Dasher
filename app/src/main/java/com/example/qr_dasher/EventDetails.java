package com.example.qr_dasher;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;


import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Activity displaying details of a specific event.
 * It retrieves the event details from Firestore and displays the list of attendees.
 */
public class EventDetails extends AppCompatActivity {
    private Event event;
    private MapView mapView;
    private FirebaseFirestore db;
    private ListView attendeeListView;
    private ListView signupListView;
    private List<String> attendeeListUserNames;
    private List<String> attendeeListDetails;
    private List<String> attendeeListEmails;
    private List<com.google.firebase.firestore.GeoPoint> attendeeListlocations;
    private List<String> signUpListListUserNames,signUpListListDetails, signUpListListEmails;
    private List<Integer>attendeeListUserIds, signUpListListUserIds;
    /**
     * Initializes the activity, sets up UI components and listeners,
     * and retrieves event details from Firebase Firestore.
     *
     * @param savedInstanceState Saved instance state bundle
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Get event name from intent extras
//<<<<<<< HEAD
        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        String eventIDStr = intent.getStringExtra("event_id");
        List<String> attendeeList = intent.getStringArrayListExtra("attendee_list");
        List<String> signupList = intent.getStringArrayListExtra("signup_list");
//=======
//        String eventName = getIntent().getStringExtra("eventName");
//        int eventId = getIntent().getIntExtra("event_id", -1);
////        Log.d("EventDetails", "Received Event ID: " + eventIDStr);  // Add this line
////        int eventId = Integer.parseInt(eventIDStr);
//        Log.d("EventDetails", "Received Event ID: " + eventId);  // Add this line
//>>>>>>> b6106ded29a92da06cc5909b3112849e43cc94e2


        if (attendeeList != null) {
            Log.d("ListSize", "Attendee List Size: " + attendeeList.size());
        } else {
            Log.d("ListSize", "Attendee List is null");
        }

        if (signupList != null) {
            Log.d("ListSize", "Signup List Size: " + signupList.size());
        } else {
            Log.d("ListSize", "Signup List is null");
        }

        TextView eventNameTextView = findViewById(R.id.eventNameTextView);

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);
        mapView = findViewById(R.id.mapView);
        attendeeListView = findViewById(R.id.attendee_list_view);
        signupListView = findViewById(R.id.signup_listview);

        db = FirebaseFirestore.getInstance();

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        getUserDetailsFromFirebase(attendeeList,signupList);
        setUpMap();
        Button notifyButton = findViewById(R.id.notify_button);
        Button qrButton = findViewById(R.id.qr_code_button);
//        Button posterUploadButton = findViewById(R.id.event_poster_button);

        // Set OnClickListener for the notification button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notification button click
                Intent intent = new Intent(EventDetails.this, SendNotification.class);
                startActivity(intent);
            }
        });


//        posterUploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle notification button click
////                Intent intent = new Intent(EventDetails.this, SendNotification.class);
////                startActivity(intent);
//            }
//        });
    }
    /**
     * Displays the list of attendees in a ListView.
     *
     * @param attendeeList The list of attendees to display.
     */

//    private void displayAttendeesAndSignups(List<String> attendeeList,List<String> signUpList ) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, attendeeList);
//        attendeeListView.setAdapter(adapter);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.mytextview, signUpList);
//        signupListView.setAdapter(adapter2);
//    }
    private void getUserDetailsFromFirebase(List<String> attendeeList, List<String> signUpList){
       // Log.d("length of attendeeList","Attendee List Size: " + attendeeList.size());
        db = FirebaseFirestore.getInstance();
        attendeeListUserNames = new ArrayList<>();
        attendeeListUserIds = new ArrayList<>();
        attendeeListDetails= new ArrayList<>();
        attendeeListEmails = new ArrayList<>();
        attendeeListlocations = new ArrayList<com.google.firebase.firestore.GeoPoint>();

        signUpListListUserNames = new ArrayList<>();
        signUpListListUserIds = new ArrayList<>();
        signUpListListDetails= new ArrayList<>();
        signUpListListEmails = new ArrayList<>();

        if (attendeeList!=null&&!attendeeList.isEmpty()) {

            List<Integer> attendeeListInt = new ArrayList<>();
            for (String str : attendeeList) {
                attendeeListInt.add(Integer.parseInt(str));
            }



            // Attendee List
            db.collection("users")
                    .whereIn("userId", attendeeListInt)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Log.d("Firestore", "Data retrieval successful");

                            // Clear the lists before updating with new data
                            attendeeListUserNames.clear();
                            attendeeListUserIds.clear();
                            attendeeListDetails.clear();
                            attendeeListEmails.clear();
                            attendeeListlocations.clear();


                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Integer userId = documentSnapshot.getLong("userId").intValue();
                                String userName = documentSnapshot.getString("name");
                                String userEmail = documentSnapshot.getString("email");
                                String userDetail = documentSnapshot.getString("details");
                                com.google.firebase.firestore.GeoPoint userlocation = documentSnapshot.getGeoPoint("geopoint");

                                // Add user details to respective lists
                                attendeeListUserNames.add(userName);
                                attendeeListUserIds.add(userId);
                                attendeeListDetails.add(userDetail);
                                attendeeListEmails.add(userEmail);
                                attendeeListlocations.add(userlocation);

                            }
                            if (attendeeListUserNames != null) {
                                displayAttendee(attendeeListUserNames);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    });
        }
        if (signUpList!=null&&!signUpList.isEmpty()) {
            List<Integer> signUpListInt = new ArrayList<>();
            for (String str : signUpList) {
                signUpListInt.add(Integer.parseInt(str));
            }


            // for sign up list
            db.collection("users")
                    .whereIn("userId", signUpListInt)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Log.d("Firestore", "Data retrieval successful");

                            // Clear the lists before updating with new data
                            signUpListListUserNames.clear();
                            signUpListListUserIds.clear();
                            signUpListListDetails.clear();
                            signUpListListEmails.clear();

                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Integer userId = documentSnapshot.getLong("userId").intValue();
                                String userName = documentSnapshot.getString("name");
                                String userEmail = documentSnapshot.getString("email");
                                String userDetail = documentSnapshot.getString("details");

                                // Add user details to respective lists
                                signUpListListUserNames.add(userName);
                                signUpListListUserIds.add(userId);
                                signUpListListDetails.add(userDetail);
                                signUpListListEmails.add(userEmail);

                            }
                            if (signUpListListUserNames != null) {
                                displaySignup(signUpListListUserNames);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    });
        }

    }
    private void displayAttendee(List<String> attendeeList) {
        Log.d("length of attendeeList","Attendee List Size: " + attendeeList.size());

        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, attendeeList);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        attendeeListView.setAdapter(adapter);
    }
    private void displaySignup(List<String> signUplist) {

        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, signUplist);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        signupListView.setAdapter(adapter);
    }
    private void setUpMap() {
        // Initialize mapView
        mapView = findViewById(R.id.mapView); // Correct the ID here

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setTilesScaledToDpi(true);
        mapView.getLocalVisibleRect(new Rect());
        IMapController controller = mapView.getController();
        controller.setZoom(3);

        mapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Intent mapIntent = new Intent(EventDetails.this, MapActivity.class);
                mapIntent.putExtra("event", String.valueOf(event));
                startActivity(mapIntent);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));
    }
}
