package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendeeActivity extends AppCompatActivity {
    private Button notificationButton, editProfileButton, qrCodeButton;
    private ListView scannedEventsListView;
    private ArrayAdapter<String> Adapter;

 
    private List<String> retrieveScannedEvents() {
        SharedPreferences sharedPref = getSharedPreferences("MyEvents", MODE_PRIVATE);
        Map<String, ?> allEvents = sharedPref.getAll();
        List<String> eventList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEvents.entrySet()) {
            eventList.add((String) entry.getValue());
        }
        return eventList;
    }

    private void updateScannedEventsList(List<String> scannedEvents) {
        Adapter.clear();
        Adapter.addAll(scannedEvents);
        Adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> scannedEvents = retrieveScannedEvents();
        updateScannedEventsList(scannedEvents);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee);

        scannedEventsListView = findViewById(R.id.scanned_events_list_view); // Replace with your actual list ID
        notificationButton = findViewById(R.id.notification_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        qrCodeButton = findViewById(R.id.qr_button);

        List<String> scannedEvents = retrieveScannedEvents();
        Adapter = new ArrayAdapter<>(this, R.layout.list_item_event, scannedEvents);
        scannedEventsListView.setAdapter(Adapter);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(AttendeeActivity.this, Notifications.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
//                Intent intent = new Intent(Attendee.this, Attendee.class);
//                startActivity(intent);
            }
        });

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(AttendeeActivity.this, ScanQRCode.class);
                startActivity(intent);
            }
        });




    }
}
