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

public class Organizer extends AppCompatActivity {
    private Button CreateEventButton;
    private ListView eventListView;
    private FirebaseFirestore db;

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
                startActivity(intent);
            }
        });

    }

//    private void retrieveEventsFromFirestore(int userId) {
//        // Query Firestore for events
//        Log.d("retrieveEventsFromFirestore","retrieval?");
//        db.collection("eventsCollection")
//                .whereEqualTo("userID",userId)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<String> eventNames = new ArrayList<>();
//                        // Iterate through the query results
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            // Extract event name from each document
//                            String eventName = documentSnapshot.getString("name");
//                            // Add event name to the list
//                            eventNames.add(eventName);
//                        }
//                        // Display the list of event names in the ListView
//                        displayEventList(eventNames);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle failure to retrieve events
//                        eventListView.setVisibility(View.GONE);
//                        e.printStackTrace();
//                    }
//                });
//    }
private void retrieveEventsFromFirestore(int userId) {
    // Query Firestore for events
            Log.d("retrieveEventsFromFirestore","retrieval?");
            Log.d("retrieveEventsFromFirestore","retrieval?"+userId);


    db.collection("eventsCollection")
            .whereEqualTo("userID",userId)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<String> eventNames = new ArrayList<>();
                    // Iterate through the query results
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Extract event name from each document
                        String eventName = documentSnapshot.getString("name");
                        // Add event name to the list
                        eventNames.add(eventName);
                    }
                    // Display the list of event names in the ListView
                    displayEventList(eventNames);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure to retrieve events
                    eventListView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            });
}
    private void displayEventList(List<String> eventNames) {
        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        // Set the adapter to the ListView
        eventListView.setAdapter(adapter);

        // Set item click listener
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event name
                String eventName = eventNames.get(position);
                // Start new activity with the event name
                Intent intent = new Intent(Organizer.this, EventDetails.class);
                intent.putExtra("eventName", eventName);
                startActivity(intent);
            }
        });
    }
}