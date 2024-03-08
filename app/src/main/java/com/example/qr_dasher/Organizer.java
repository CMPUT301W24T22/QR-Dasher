package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private FirebaseFirestore db;
    private LinearLayout eventButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        CreateEventButton = findViewById(R.id.createEventButton);
        eventButtonsLayout = findViewById(R.id.eventButtonsLayout);
        //eventButtonsLayout = findViewById(R.id.eventButtonsLayout); // Add this line to get the LinearLayout from the XML layout

        db = FirebaseFirestore.getInstance();

        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(Organizer.this, CreateEventOrganizer.class);
                startActivity(intent);
            }
        });

        // Fetch events from Firestore and create buttons dynamically
        fetchEvents();
    }

    private void fetchEvents() {
        // Assuming user_id is known, replace it with the actual user_id value
        Integer user_id = 50505050;

        CollectionReference eventsCollection = db.collection("eventsCollection");

        // Query events for the specific user_id
        eventsCollection.whereEqualTo("attendee_qr.userID", user_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String eventName = document.getString("name");
                            createEventButton(eventName);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to fetch events
                    }
                });
    }

    private void createEventButton(String eventName) {
        // Create a new button for each event
        Button eventButton = new Button(this);
        eventButton.setText(eventName);

        // Set OnClickListener for each button to handle click events
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., navigate to event details activity
                // You can implement this part based on your requirements
            }
        });

        // Add the button to the LinearLayout
        eventButtonsLayout.addView(eventButton);
    }
}