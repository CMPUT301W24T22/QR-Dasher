package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Attendee extends AppCompatActivity {
    private Button notificationButton, editProfileButton, qrCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee);

        notificationButton = findViewById(R.id.notification_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        qrCodeButton = findViewById(R.id.qr_button);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(Attendee.this, Notifications.class);
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
                Intent intent = new Intent(Attendee.this, ScanQR.class);
                startActivity(intent);
            }
        });


    }
}