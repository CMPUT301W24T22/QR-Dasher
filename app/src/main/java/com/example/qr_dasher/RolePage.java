package com.example.qr_dasher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * RolePage activity presents role selection options to users.
 * Users can choose between different roles such as Organizer, Attendee, and Admin.
 * Clicking on the respective role button will navigate the user to the corresponding activity.
 */
public class RolePage extends Activity {
    private Button organizerButton, attendeeButton, adminButton;
    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
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
                Intent intent = new Intent(RolePage.this, Organizer.class);
                startActivity(intent);
            }
        });

        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(RolePage.this, Attendee.class);
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
