package com.example.qr_dasher;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Organizer extends AppCompatActivity {
    private Button CreateEventButton;
    private ListView eventListView;
    private FirebaseFirestore db;

    private ArrayList<String> reuseQRCodes = new ArrayList<>();

    private List<String> eventNames;
    private List<String> eventIds;

    private SharedPreferences app_cache; // To get the userID

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

    private void retrieveEventsFromFirestore(int userId) {
        // Query Firestore for events
        eventNames = new ArrayList<>();
        eventIds = new ArrayList<>();

        db.collection("eventsCollection")
                .whereEqualTo("attendee_qr.userID",userId)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle errors
                            eventListView.setVisibility(View.GONE);
                            e.printStackTrace();
                            return;
                        }

                        // Update the list with new data
                        eventNames.clear();
                        eventIds.clear();

                        // Iterate through the query results
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String documentId = documentSnapshot.getId();
                            String eventName = documentSnapshot.getString("name");
                            String qrCode = documentSnapshot.getString("attendee_qr.qrImage");

                            if (documentSnapshot.contains("event_id")) {
                                Long eventIdLong = documentSnapshot.getLong("event_id");
                                if (eventIdLong != null) {
                                    String eventId = String.valueOf(eventIdLong);
                                    //String eventId = String.valueOf(eventIdLong);
                                    // Add event name and event_id to the lists
                                    eventNames.add(eventName);
                                    eventIds.add(eventId);
                                }
                            }
                        }
                        // Display the list of event names in the ListView
                        displayEventList(eventNames,eventIds);
                    }
                });

    }

    private void displayEventList(List<String> eventNames, List<String> eventIds) {
        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, eventNames);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        eventListView.setAdapter(adapter);

        // Set item click listener
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event name
                String eventName = eventNames.get(position);
                String eventId = eventIds.get(position);
                //Integer eventId = Integer.parseInt(eventIdStr);
                // Start new activity with the event name
                Intent intent = new Intent(Organizer.this, EventDetails.class);
                intent.putExtra("eventName", eventName);
                intent.putExtra("event_id", eventId);
                startActivity(intent);
            }
        });


    }
}