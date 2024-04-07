package com.example.qr_dasher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.SharedPreferences;
import android.widget.Toast;


public class RolePage extends AppCompatActivity {
    private Button organizerButton, attendeeButton, adminButton;
    private SharedPreferences app_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_page);

        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        organizerButton = findViewById(R.id.organizer_button);
        attendeeButton = findViewById(R.id.attendee_button);
        adminButton = findViewById(R.id.admin_button);

        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle organizer role selection
                Intent intent = new Intent(RolePage.this, Organizer.class);
                startActivity(intent);
            }
        });

        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                Intent intent = new Intent(RolePage.this, Attendee.class);
                startActivity(intent);
            }
        });
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle admin role selection
                AdminPinFragment adminPinFragment = AdminPinFragment.newInstance();
                adminPinFragment.show(getSupportFragmentManager(), "AdminPinFragment");
            }
        });

        updateOrganizerButtonVisibility(); // Update the visibility initially
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOrganizerButtonVisibility(); // Update the visibility whenever the activity resumes
    }

    // Method to update the visibility of the organizer button based on the guest flag
    private void updateOrganizerButtonVisibility() {
        boolean guest = app_cache.getBoolean("Guest", false);
        if (guest) {
            organizerButton.setVisibility(View.GONE); // Hide organizer button
            Toast.makeText(RolePage.this, "Create Profile to Access Organizer Functionality", Toast.LENGTH_SHORT).show();
        } else {
            organizerButton.setVisibility(View.VISIBLE); // Show organizer button
        }
    }
}
