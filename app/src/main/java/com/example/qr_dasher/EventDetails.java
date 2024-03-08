package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Activity displaying details of a specific event.
 * It retrieves the event details from Firestore and displays the list of attendees.
 */
public class EventDetails extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView attendeeListView;
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
        String eventName = getIntent().getStringExtra("eventName");
        String eventIDStr = getIntent().getStringExtra("event_id");
        Log.d("EventDetails", "Received Event ID: " + eventIDStr);  // Add this line
        int eventId = Integer.parseInt(eventIDStr);
        Log.d("EventDetails", "Received Event ID: " + eventId);  // Add this line

        //Integer eventId = Integer.valueOf(Objects.requireNonNull(getIntent().getStringExtra("event_id")));

        TextView eventNameTextView = findViewById(R.id.eventNameTextView);

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        attendeeListView = findViewById(R.id.attendee_list_view);
        db = FirebaseFirestore.getInstance();

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        // Fetch the attendee list for the selected event
        fetchAttendees(eventId);

        Button notifyButton = findViewById(R.id.notify_button);
        Button qrButton = findViewById(R.id.qr_code_button);
        Button posterUploadButton = findViewById(R.id.event_poster_button);

        // Set OnClickListener for the notification button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notification button click
                Intent intent = new Intent(EventDetails.this, SendNotification.class);
                startActivity(intent);
            }
        });

        posterUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notification button click
//                Intent intent = new Intent(EventDetails.this, SendNotification.class);
//                startActivity(intent);
            }
        });

        posterUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notification button click
//                Intent intent = new Intent(EventDetails.this, SendNotification.class);
//                startActivity(intent);
            }
        });
    }
    /**
     * Fetches the list of attendees for the given event ID from Firestore.
     *
     * @param eventId The ID of the event for which attendees need to be fetched.
     */
    private void fetchAttendees(Integer eventId) {
        // Query Firestore for attendees


        db.collection("eventsCollection")
                .whereEqualTo("event_id",eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override

                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);

                            List<String> attendeeList = (List<String>) documentSnapshot.get("attendee_list");
                            displayAttendeeList(attendeeList);
                        } else{
                            Toast.makeText(EventDetails.this, "Event details not found!", Toast.LENGTH_SHORT).show();
                        }
                        List<String> attendeeList = new ArrayList<>();
                        // Iterate through the query results
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                            List<String> attendees = (List<String>) documentSnapshot.get("attendee_list");   //"attendee_list is the user Id "
                            if (attendees != null) {
                                attendeeList.addAll(attendees);
                            }

                        }
                        // Display the list of attendees in the event
                        displayAttendeeList(attendeeList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }
    /**
     * Displays the list of attendees in a ListView.
     *
     * @param attendeeList The list of attendees to display.
     */

    private void displayAttendeeList(List<String> attendeeList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, attendeeList);
        attendeeListView.setAdapter(adapter);
    }
}
