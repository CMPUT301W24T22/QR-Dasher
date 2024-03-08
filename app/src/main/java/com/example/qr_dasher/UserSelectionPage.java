package com.example.qr_dasher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for user selection page where users can choose between attendee and organizer roles.
 */
public class UserSelectionPage extends AppCompatActivity {

    // Buttons for selecting attendee or organizer role
    private Button attendeeButton, organizerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_selection_page);

        // Initialize attendee and organizer buttons
        attendeeButton = findViewById(R.id.attendee_button);
        organizerButton = findViewById(R.id.organizer_button);

        // Set onClickListener for attendee button
        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action to be performed when attendee button is clicked
                // Add code here to handle attendee role selection
            }
        });

        // Set onClickListener for organizer button
        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action to be performed when organizer button is clicked
                // Add code here to handle organizer role selection
            }
        });
    }
}

