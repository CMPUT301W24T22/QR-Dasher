package com.example.qr_dasher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class represents the Activity displaying details of a specific event.
 * It retrieves the event details from Firestore and displays the list of attendees.
 */
public class EventDetails extends AppCompatActivity {
    private MapView mapView;
    private FirebaseFirestore db;
    private ListView attendeeListView;
    private ListView signupListView;
    private String eventIDstr;
    private List<String> attendeeListUserNames,  attendeeListDetails, attendeeListEmails, attendeeScanCounts;
    private List<String> signUpListListUserNames,signUpListListDetails, signUpListListEmails;
    private List<Integer>attendeeListUserIds, signUpListListUserIds;


    private TextView attendeeCountTextView;

    private static final int MILESTONE_ONE = 5;
    private static final int MILESTONE_TWO = 10;
    private static final int MILESTONE_THREE = 15;

    private boolean isMilestoneOneReached = false;
    private boolean isMilestoneTwoReached = false;
    private boolean isMilestoneThreeReached = false;

    private Bitmap AttendeeQRCode, PromotionalQRcode;
    private String eventName, promotionalQRString;
    private Boolean twoQRcodes = false;
    private Button generatePromoQRbutton;

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

        Intent intent = getIntent();
        eventName = intent.getStringExtra("eventName");
        eventIDstr = intent.getStringExtra("event_id");
        Toast.makeText(getApplicationContext(), "Event ID: " + eventIDstr, Toast.LENGTH_SHORT).show();
        List<String> attendeeList = intent.getStringArrayListExtra("attendee_list");
        List<String> signupList = intent.getStringArrayListExtra("signup_list");
        String attendeeQr = intent.getStringExtra("qrImage");
        String qrContent = intent.getStringExtra("qrContent");
        Long qrUserid = intent.getLongExtra("userID",0);
        Long qrEventId= intent.getLongExtra("eventId",0 );
        Log.d("TargetActivity", "Received qrUserid: " + qrUserid);
        Log.d("TargetActivity", "Received qrEventId: " + qrEventId);


        // Converting the string to bitmap
        byte[] imageBytes = Base64.decode(attendeeQr, Base64.DEFAULT);
        AttendeeQRCode = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        retrievePromotionalQR( ""+qrEventId);


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
        attendeeCountTextView = findViewById(R.id.attendeeCount);

        db = FirebaseFirestore.getInstance();

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);
        setUpMap();

        getUserDetailsFromFirebase(attendeeList,signupList);
        Button notifyButton = findViewById(R.id.notify_button);
        Button qrButton = findViewById(R.id.qr_code_button);
        generatePromoQRbutton = findViewById(R.id.promoQRbutton);
        Button announcementButton = findViewById(R.id.announcement_button);
//        Button posterUploadButton = findViewById(R.id.event_poster_button);
        if (!twoQRcodes){
            generatePromoQRbutton.setVisibility(View.VISIBLE);
        } else{
            generatePromoQRbutton.setVisibility(View.INVISIBLE);
        }

        ArrayList<String> tokensList= new ArrayList<>();                       // tokens
        for(String userId: attendeeList){
            // Get the document reference for the user with this userId
            DocumentReference userRef = db.collection("users").document(userId);

            // Retrieve the token from Firestore for this user
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the token field from the document
                            String token = document.getString("token");
                            // Add the token to tokensList
                            Log.d("SendNotification", "token : " + token); // Log the attendee list

                            if (token != null) {
                                tokensList.add(token);
                            }
                        } else {
                            Log.d("HomePage", "No such document");
                        }
                    } else {
                        Log.d("HomePage", "get failed with ", task.getException());
                    }
                }
            });
        }
        generatePromoQRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QRCode (int event_id, String content, int userID, boolean promotional)
                QRCode promotionalQRcode = new QRCode(qrEventId.intValue(),"p"+qrContent, qrUserid.intValue(),true);
                db.collection("eventsCollection")
                        .document("" + eventIDstr)
                        .update("promotional_qr", promotionalQRcode)
                        .addOnSuccessListener(aVoid ->{
                            Log.d("EventDetails", "Event QR PROMOTIONAL ADDED successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.d("Organizer", "Failed to update event in Firestore"+ eventIDstr);
                            e.printStackTrace();
                        });
                promotionalQRString = promotionalQRcode.getQrImage();
                twoQRcodes = true;
                byte[] imageBytes = Base64.decode(promotionalQRString, Base64.DEFAULT);
                PromotionalQRcode = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                generatePromoQRbutton.setVisibility(View.GONE);
            }
        });



        // Set OnClickListener for the notification button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetails.this, SendNotification.class);
                          // Pass the event ID to SendNotification activity
                intent.putExtra("event_id", eventIDstr);
                         // Pass the attendee list as an extra to the SendNotification activity
                intent.putStringArrayListExtra("tokensList", (ArrayList<String>) tokensList);
                startActivity(intent);
            }
        });

        // if promotional qr does not exist:

        // Set OnClickListener for the Announcement button
        announcementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetails.this, SendAnnouncement.class);
                // Pass the event ID to SendNotification activity
                intent.putExtra("event_id", eventIDstr);
                startActivity(intent);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!twoQRcodes){
                    ShareQRFragment fragment = ShareQRFragment.newInstance(AttendeeQRCode,eventName);
                    fragment.showFragment(getSupportFragmentManager());}

                else {
                    ShareQRFragment fragment = ShareQRFragment.newInstance(AttendeeQRCode, PromotionalQRcode,eventName);
                    fragment.showFragment(getSupportFragmentManager());
                }
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


//    private void displayAttendeesAndSignups(List<String> attendeeList,List<String> signUpList ) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, attendeeList);
//        attendeeListView.setAdapter(adapter);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.mytextview, signUpList);
//        signupListView.setAdapter(adapter2);
//    }

    /**
     * Retrieves the promotional QR code associated with the specified event from Firestore.
     *
     * @param eventId The ID of the event to retrieve the promotional QR code for
     */
    private void retrievePromotionalQR(String eventId) {
        db = FirebaseFirestore.getInstance();

        db.collection("eventsCollection")
                .document(""+eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("Firestore", "DocumentSnapshot data: " + documentSnapshot.getData());
                        if (documentSnapshot.contains("promotional_qr") && documentSnapshot.get("promotional_qr") instanceof Map) {
                            // Assign the promotional_qr map to the global variable
                            Map<String, Object> promotionalQrMap = (Map<String, Object>) documentSnapshot.get("promotional_qr");
                            promotionalQRString = (String) promotionalQrMap.get("qrImage");
                            twoQRcodes = true;
                            byte[] imageBytes = Base64.decode(promotionalQRString, Base64.DEFAULT);
                            PromotionalQRcode = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            generatePromoQRbutton.setVisibility(View.GONE);

                            // Handle further operations with promotionalQrMap if needed
                        } else {
                            Log.d("PromotionalQR", "No promotional QR found");
                            twoQRcodes = false;
                        }}

                })
                .addOnFailureListener(e -> {
                    Log.d("PromotionalQR", "Failed to retrieve promotional QR");
                    e.printStackTrace();
                });
    }

    /**
     * Retrieves user details from Firebase Firestore for the given attendee and sign-up lists.
     * Updates the attendee and sign-up list views accordingly.
     *
     * @param attendeeList The list of attendee user IDs
     * @param signUpList   The list of sign-up user IDs
     */
    private void getUserDetailsFromFirebase(List<String> attendeeList, List<String> signUpList){
        db = FirebaseFirestore.getInstance();

        // Initialize lists
        attendeeListUserNames = new ArrayList<>();
        attendeeListUserIds = new ArrayList<>();
        attendeeListDetails = new ArrayList<>();
        attendeeListEmails = new ArrayList<>();
        attendeeScanCounts = new ArrayList<>();

        signUpListListUserNames = new ArrayList<>();
        signUpListListUserIds = new ArrayList<>();
        signUpListListDetails = new ArrayList<>();
        signUpListListEmails = new ArrayList<>();

        if (attendeeList != null && !attendeeList.isEmpty()) {
            List<Integer> attendeeListInt = new ArrayList<>();
            for (String str : attendeeList) {
                attendeeListInt.add(Integer.parseInt(str));
            }

            // Attendee List - Real-time Listener
            db.collection("users")
                    .whereIn("userId", attendeeListInt)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("EventDetails", "Firestore listen failed.", e);
                                return;
                            }

                            if (queryDocumentSnapshots != null) {
                                attendeeListUserNames.clear();
                                attendeeListUserIds.clear();
                                attendeeListDetails.clear();
                                attendeeListEmails.clear();
                                attendeeScanCounts.clear();

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Integer userId = documentSnapshot.getLong("userId").intValue();
                                    String userName = documentSnapshot.getString("name");
                                    String userEmail = documentSnapshot.getString("email");
                                    String userDetail = documentSnapshot.getString("details");
                                    List<String> eventsJoined = (List<String>) documentSnapshot.get("eventsJoined");

                                    int scanCount = 0;
                                    if (eventsJoined != null) {
                                        for (String eventId : eventsJoined) {
                                            if (eventId.equals(eventIDstr)) {
                                                scanCount++;
                                            }
                                        }
                                    }

                                    attendeeListUserNames.add(userName);
                                    attendeeListUserIds.add(userId);
                                    attendeeListDetails.add(userDetail);
                                    attendeeListEmails.add(userEmail);
                                    attendeeScanCounts.add(String.valueOf(scanCount));
                                }

                                updateAttendeeCount(attendeeListUserNames.size());
                                if (attendeeListUserNames.size() > 0) {
                                    displayAttendee(attendeeListUserNames);
                                }
                                checkAndUpdateMilestones(attendeeListUserNames.size());
                            }
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

    /**
     * Updates the attendee count text view with the provided count.
     *
     * @param count The number of attendees to display
     */
    private void updateAttendeeCount(int count) {
        attendeeCountTextView.setText("Attendees: " + count);
    }

    /**
     * Displays the list of attendees with their corresponding scan counts in the attendee list view.
     *
     * @param attendeeList The list of attendee names
     */
    private void displayAttendee(List<String> attendeeList) {
        Log.d("length of attendeeList","Attendee List Size: " + attendeeList.size());

        List<String> combinedList = new ArrayList<>();

        // Iterate through each item in the attendeeList and combine it with the corresponding scan count
        for (int i = 0; i < attendeeList.size(); i++) {
            String eventName = attendeeList.get(i);
            int scanCount = Integer.parseInt(attendeeScanCounts.get(i));

            // Concatenate the event name and scan count into a single string
            String combinedString = eventName + " (Scan Count: " + scanCount + ")";

            // Add the combined string to the list
            combinedList.add(combinedString);
        }

        // Create ArrayAdapter with the combined list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, combinedList);
        adapter.notifyDataSetChanged();

        // Set the adapter to the ListView
        attendeeListView.setAdapter(adapter);


        attendeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userid = attendeeListUserIds.get(position);
                Intent intent = new Intent(EventDetails.this, AdminUserProfile.class);
                intent.putExtra("userID",""+userid);
                intent.putExtra("role","organizer");
                startActivity(intent);

            }
        });

    }

    /**
     * Displays the list of sign-ups in the sign-up list view.
     *
     * @param signUpList The list of sign-up names
     */
    private void displaySignup(List<String> signUplist) {

        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, signUplist);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        signupListView.setAdapter(adapter);
        signupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int userid = signUpListListUserIds.get(position);
                Intent intent = new Intent(EventDetails.this, AdminUserProfile.class);
                intent.putExtra("userID",""+userid);
                intent.putExtra("role","organizer");
                startActivity(intent);
            }
        });
    }

    /**
     * Sets up the map view by configuring its tile source, scaling, zoom level, and map events receiver.
     */
    private void setUpMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setTilesScaledToDpi(true);
        mapView.getLocalVisibleRect(new Rect());
        IMapController controller = mapView.getController();
        controller.setZoom(3);

        // Set up map events receiver for single tap
        mapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // Launch MapsActivity when map is tapped
                Intent mapIntent = new Intent(EventDetails.this, MapsActivity.class);
                mapIntent.putExtra("eventIDstr", eventIDstr);
                startActivity(mapIntent);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));
    }

    /**
     * Checks if certain milestones based on attendee count have been reached and displays corresponding alerts.
     *
     * @param attendeeCount The current number of attendees
     */
    private void checkAndUpdateMilestones(int attendeeCount) {
        if (attendeeCount >= MILESTONE_ONE && !isMilestoneOneReached) {
            showMilestoneAlert(MILESTONE_ONE);
            isMilestoneOneReached = true;
        }
        if (attendeeCount >= MILESTONE_TWO && !isMilestoneTwoReached) {
            showMilestoneAlert(MILESTONE_TWO);
            isMilestoneTwoReached = true;
        }
        if (attendeeCount >= MILESTONE_THREE && !isMilestoneThreeReached) {
            showMilestoneAlert(MILESTONE_THREE);
            isMilestoneThreeReached = true;
        }
    }

    /**
     * Displays an alert indicating that a milestone has been reached.
     *
     * @param milestone The milestone reached
     */
    private void showMilestoneAlert(int milestone) {
        String message = "Milestone reached: Over " + milestone + " attendees!";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

