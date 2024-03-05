package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelection extends AppCompatActivity {
    private Button organizer_button;
    private Button attendee_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_selection); // Make sure this is the correct layout name

        organizer_button = findViewById(R.id.organizer_button); // replace with your actual button ID
        attendee_button = findViewById(R.id.attendee_button); // replace with your actual button ID

//        organizer_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Intent to start Organizer Activity
//                Intent intent = new Intent(RoleSelection.this, OrganizerActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        attendee_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Intent to start Attendee Activity
//                Intent intent = new Intent(RoleSelection.this, AttendeeActivity.class);
//                startActivity(intent);
//            }
//        });
    }



}
