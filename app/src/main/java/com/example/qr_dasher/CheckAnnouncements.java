package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CheckAnnouncements extends AppCompatActivity {
    private ListView listView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_announcements);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get event ID from intent extras
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("event_id");

        Log.d("CheckAnnouncements", "Event ID  : " + eventId);

        // Initialize ListView
        listView = findViewById(R.id.announcement_listview);

        // Display announcements for the event
        displayEventSignUpPage(eventId);
    }

    private void displayEventSignUpPage(String eventId) {
        // Retrieve event details from Firestore
        db.collection("eventsCollection")
                .document(eventId)
                .get()
                //.whereEqualTo("attendee_qr.event_id", eventId)

                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        // Retrieve announcements for the event
                        List<String> announcements = event.getAnnouncements();
                        Log.d("CheckAnnouncements", "Announcement List : " + announcements);

                        // Display announcements in the ListView
                        displayAnnouncements(announcements);
                    } else {
                        Log.d("CheckAnnouncements", "Event not found with ID: " + eventId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("CheckAnnouncements", "Failed to retrieve event from Firestore", e);
                });
    }

    private void displayAnnouncements(List<String> announcements) {
        if (announcements != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, announcements);
            listView.setAdapter(adapter);
        } else {
            // Handle the case where announcements is null
            Log.e("CheckAnnouncements", "Announcements list is null");
        }
    }

}
