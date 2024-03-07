package com.example.qr_dasher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RolePage extends Activity {
    private Button organizerButton, attendeeButton, adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_page);

        organizerButton = findViewById(R.id.organizer_button);
        attendeeButton = findViewById(R.id.attendee_button);
        adminButton = findViewById(R.id.admin_button);

        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle organizer role selection
                // For example, start a new activity for organizer tasks
                Intent intent = new Intent(RolePage.this, OrganizerDashboard.class);
                startActivity(intent);
            }
        });

        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(RolePage.this, AttendeeDashboard.class);
                startActivity(intent);
            }
        });

//        adminButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle admin role selection
//                // For example, start a new activity for admin tasks
//                Intent intent = new Intent(RolePage.this, AdminActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}