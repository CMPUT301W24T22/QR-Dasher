package com.example.qr_dasher;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * This activity represents the Organizer's interface where they can view and manage their events.
 * It retrieves events associated with the logged-in organizer from Firestore and displays them in a ListView.
 * The organizer can also create new events by clicking the "Create Event" button.
 */
public class Organizer extends AppCompatActivity {
    private static final String TAG = "OrganizerActivity";
    private Button CreateEventButton;
    private ListView eventListView;
    private FirebaseFirestore db;

    private ArrayList<String> reuseQRCodes = new ArrayList<>();

    private List<String> eventNames,eventIds,eventPosters;
    private  List<List<String>> attendeeLists, signupLists;
    private List<Map<String, Object>> attendeeQrMap, promotionalQrMap;


    private SharedPreferences app_cache; // To get the userID
    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        CreateEventButton = findViewById(R.id.createEventButton);
        eventListView = findViewById(R.id.eventListView);

        db = FirebaseFirestore.getInstance();
        // getting the UserID from the cache
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        int userId = app_cache.getInt("UserID", -1);
        retrieveEventsFromFirestore(userId);//userId);

        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(Organizer.this, CreateEventOrganizer.class);
                intent.putStringArrayListExtra("reuseQRCodes", reuseQRCodes);

                startActivity(intent);
            }
        });

    }
    /**
     * Retrieves events associated with the organizer from Firestore.
     *
     * @param userId The ID of the organizer.
     */
    private void retrieveEventsFromFirestore(int userId) {
        // Query Firestore for events
        eventNames = new ArrayList<>();
        eventIds = new ArrayList<>();
        attendeeLists= new ArrayList<>();
        signupLists = new ArrayList<>();
        eventPosters = new ArrayList<>();
        attendeeQrMap = new ArrayList<>();

        db.collection("eventsCollection")
                .whereEqualTo("attendee_qr.userID", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        eventNames.clear();
                        eventIds.clear();
                        attendeeLists.clear();
                        signupLists.clear();
                        eventPosters.clear();
                        attendeeQrMap.clear();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String eventName = documentSnapshot.getString("name");
                            String eventPoster = documentSnapshot.getString("Poster");

                            List<String> attendeeList = (List<String>) documentSnapshot.get("attendee_list");
                            List<String> signupList = (List<String>) documentSnapshot.get("signup_list");
                            Map<String, Object> attendeeQr =(Map<String, Object>) documentSnapshot.getData().get("attendee_qr");

                            if (documentSnapshot.contains("event_id")) {
                                Long eventIdLong = documentSnapshot.getLong("event_id");
                                if (eventIdLong != null) {
                                    String eventId = String.valueOf(eventIdLong);
                                    // Add event name and event_id to the lists
                                    eventNames.add(eventName);
                                    eventIds.add(eventId);
                                    attendeeLists.add(attendeeList);
                                    signupLists.add(signupList);
                                    eventPosters.add(eventPoster);}
                                    attendeeQrMap.add(attendeeQr);



                            }
                            //eventNames.add(eventName);
                            //eventPosters.add(eventPoster);
                        }

                        displayEventList(eventNames, eventIds);
                    }
                });

    }
    /**
     * Displays the list of events in the ListView.
     *
     * @param eventNames The list of event names.
     * @param eventIds   The list of event IDs.
     */
    private void displayEventList(List<String> eventNames, List<String> eventIds) {
        // Create an ArrayAdapter to display the event names
        EventAdapter adapter = new EventAdapter(this, eventNames, eventPosters);
        adapter.notifyDataSetChanged();
        eventListView.setAdapter(adapter);

        // Set item click listener
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event name
                String eventName = eventNames.get(position);
                String eventId = eventIds.get(position);
                List<String> attendeeList = attendeeLists.get(position);
                List<String> signupList = signupLists.get(position);
                Map<String, Object> attendeeQr = attendeeQrMap.get(position);
                String qrImage = (String) attendeeQr.get("qrImage");
                String qrContent =(String) attendeeQr.get("content");
                Long qrUserIdLong = (Long) attendeeQr.get("userID");
                Long qrEventIdLong = (Long) attendeeQr.get("event_id");

                Intent intent = new Intent(Organizer.this, EventDetails.class);
                intent.putExtra("eventName", eventName);
                intent.putExtra("event_id", eventId);
                intent.putExtra("qrImage",qrImage);
                intent.putExtra("qrContent", qrContent);
                intent.putExtra("userID",qrUserIdLong);
                intent.putExtra("eventId",qrEventIdLong );
                Log.d("MainActivity", "qrUserid before putExtra: " + qrUserIdLong);
                Log.d("MainActivity", "qrEventId before putExtra: " + qrEventIdLong);
                //Log.d("Debug", "Attendee List before adding to intent: " + attendeeList);
                //Log.d("Debug", "Signup List before adding to intent: " + signupList);
                if (attendeeList!= null){
                    intent.putStringArrayListExtra("attendee_list",(ArrayList<String>) attendeeList);
                }
                if (signupList!= null){
                intent.putStringArrayListExtra("signup_list", (ArrayList<String>) signupList);}
                //Log.d("Debug", "Attendee List after adding to intent: " + attendeeList);
                //Log.d("Debug", "Signup List after adding to intent: " + signupList);
               startActivity(intent);
//                intent.putExtra("event_id", eventId);

            }
        });

    }
}
