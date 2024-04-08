package com.example.qr_dasher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * This activity allows users to select their role: Organizer, Attendee, or Admin.
 * Users can navigate to different functionalities based on their selected role.
 */
public class RolePage extends AppCompatActivity {
    private Button organizerButton, attendeeButton, adminButton;
    private SharedPreferences app_cache;

    /**
     * Called when the activity is starting. Initializes UI components and sets up click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *    this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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

    /**
     * Called when the activity will start interacting with the user.
     * Updates the visibility of the organizer button whenever the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateOrganizerButtonVisibility(); // Update the visibility whenever the activity resumes
    }

    /**
     * Updates the visibility of the organizer button based on the guest flag stored in SharedPreferences.
     * If the user is a guest, the organizer button is hidden and a message is displayed prompting the user to create a profile.
     */
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
