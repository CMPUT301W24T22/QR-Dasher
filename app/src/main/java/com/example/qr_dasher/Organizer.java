package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Organizer extends AppCompatActivity {
    private Button CreateEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        CreateEventButton = findViewById(R.id.createEventButton);

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
}