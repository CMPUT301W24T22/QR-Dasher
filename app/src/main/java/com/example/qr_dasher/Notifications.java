package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Notifications extends AppCompatActivity {
    private Button notificationClearButton, enableDisableButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        notificationClearButton = findViewById(R.id.clear_notifications_button);
        enableDisableButton = findViewById(R.id.enable_disable_button);

        notificationClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        enableDisableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}