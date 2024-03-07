package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AttendeeDashboard extends AppCompatActivity {
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
                Intent intent = new Intent(AttendeeDashboard.this, Notifications.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
//                Intent intent = new Intent(AttendeeDashboard.this, AttendeeDashboard.class);
//                startActivity(intent);
            }
        });

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle attendee role selection
                // For example, start a new activity for attendee tasks
                Intent intent = new Intent(AttendeeDashboard.this, ScanQR.class);
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Check if the result is from ScanQR activity
            if (resultCode == RESULT_OK) {
                String scannedText = data.getStringExtra("scannedText");
                Toast.makeText(this, "Scanning successful " + scannedText, Toast.LENGTH_SHORT).show();
                // Implement firestore check
            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}