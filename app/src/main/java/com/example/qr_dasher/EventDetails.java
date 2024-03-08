package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Get event name from intent extras
        String eventName = getIntent().getStringExtra("eventName");

        TextView eventNameTextView = findViewById(R.id.eventNameTextView);

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        Button notifyButton = findViewById(R.id.notify_button);

        // Set OnClickListener for the notification button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notification button click
                Intent intent = new Intent(EventDetails.this, SendNotification.class);
                startActivity(intent);
            }
        });
    }
}