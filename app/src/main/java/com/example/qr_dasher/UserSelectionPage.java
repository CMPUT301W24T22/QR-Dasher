package com.example.qr_dasher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserSelectionPage extends AppCompatActivity {

    private Button attendeeButton, organizerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_selection_page);

        attendeeButton = findViewById(R.id.attendee_button);
        organizerButton = findViewById(R.id.organizer_button);

        attendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to add
            }
        });

        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to add
            }
        });
    }

}
