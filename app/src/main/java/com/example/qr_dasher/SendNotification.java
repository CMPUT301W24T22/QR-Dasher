package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SendNotification activity allows users to compose and send notifications.
 * It provides input fields for notification title and content, along with a button to send the notification.
 */
public class SendNotification extends AppCompatActivity {
    private EditText notificationTitleEditText;
    private EditText notificationContentEditText;
    private Button sendNotificationButton;
    private FirebaseFirestore db;

    private Event event;
    private int eventId;

    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);



        /*int eventID = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("event_id")));
        Log.d("SendNotifications", "Event Id:  " + eventID);*/
        //Log.d("EventDetails", "Received Event ID: " + eventIDStr);  // Add this line
        //int eventId = Integer.parseInt(eventIDStr);

        notificationTitleEditText = findViewById(R.id.notification_title_text);
        notificationContentEditText = findViewById(R.id.notification_content);
        sendNotificationButton = findViewById(R.id.send_notifications_button);

        db = FirebaseFirestore.getInstance();

        // Get event ID passed from EventDetails activity
        eventId = getIntent().getIntExtra("event_id", -1);


        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notificationTitleEditText.getText().toString();
                String message = notificationContentEditText.getText().toString();
                sendNotification(eventId,title,message);
            }
        });
    }
    private void sendNotification(int eventId, String title, String message) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("content", message);

        db.collection("eventsCollection")
                .document(String.valueOf(eventId))
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SendNotification.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after sending notification
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SendNotification.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
