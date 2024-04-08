package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * An activity to display a list of events stored in Firestore.
 * This activity fetches event data from Firestore and populates a ListView with event names and posters.
 * Clicking on an event in the list navigates to the AdminEventPage activity for further details.
 */
public class AdminEventList extends AppCompatActivity {

    private ListView eventListView;
    private FirebaseFirestore db;
    private CollectionReference eventsCollection;
    private ArrayList<String> eventNamesList, eventPostersList, eventIDList;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_list);

        eventListView = findViewById(R.id.eventList);

        eventNamesList = new ArrayList<>(); // List to hold event names
        eventPostersList = new ArrayList<>(); // List to hold event poster base64 strings
        eventIDList = new ArrayList<>(); // List to hold event ids


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection("eventsCollection");

        // Fetch event names and IDs from Firestore
        eventsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get event name from each document
                String eventName = documentSnapshot.getString("name");
                eventNamesList.add(eventName);
                // Get event poster (base64 string) from each document
                String eventPosterBase64 = documentSnapshot.getString("eventPoster");
                eventPostersList.add(eventPosterBase64);
                // Get userID (documentID) from each document
                String eventID = documentSnapshot.getId();
                eventIDList.add(eventID);
            }

            // Create and set ListAdapter
            ListAdapter adapter = new ListAdapter(AdminEventList.this, eventNamesList, eventPostersList);
            eventListView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(AdminEventList.this, "Failed to retrieve event data", Toast.LENGTH_SHORT).show();
            Log.e("AdminEventList", "Error fetching event data: " + e.getMessage());
        });

        // Set item click listener for the ListView
        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected event ID
            String selectedEventID = eventIDList.get(position);
            // Create an intent to start the AdminEventPage activity
            Intent intent = new Intent(AdminEventList.this, AdminEventPage.class);
            // Pass the selected event ID as an extra to the intent
            intent.putExtra("eventID", selectedEventID);
            // Start the activity
            startActivity(intent);
        });
    }

    /**
     * Called when the activity is resumed.
     * Fetches event data from Firestore and populates the ListView.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Clear previous data
        eventNamesList.clear();
        eventPostersList.clear();
        eventIDList.clear();

        // Fetch event names and IDs from Firestore
        eventsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get event name from each document
                String eventName = documentSnapshot.getString("name");
                eventNamesList.add(eventName);
                // Get event poster (base64 string) from each document
                String eventPosterBase64 = documentSnapshot.getString("eventPoster");
                eventPostersList.add(eventPosterBase64);
                // Get userID (documentID) from each document
                String eventID = documentSnapshot.getId();
                eventIDList.add(eventID);
            }

            // Update ListView with new data
            ListAdapter adapter = new ListAdapter(AdminEventList.this, eventNamesList, eventPostersList);
            eventListView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(AdminEventList.this, "Failed to retrieve event data", Toast.LENGTH_SHORT).show();
            Log.e("AdminEventList", "Error fetching event data: " + e.getMessage());
        });
    }
}
