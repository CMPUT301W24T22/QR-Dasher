package com.example.qr_dasher;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * SendNotification activity allows users to compose and send notifications.
 * It provides input fields for notification title and content, along with a button to send the notification.
 */
public class SendAnnouncement extends AppCompatActivity {
    private EditText announcementTitleEditText;
    private Button sendAnnouncementButton;

    private FirebaseFirestore db;
    private int eventId;


    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_announcement);

        announcementTitleEditText = findViewById(R.id.announcment_title_text);
        sendAnnouncementButton = findViewById(R.id.send_announcement_button);


        db = FirebaseFirestore.getInstance();


        // Get event ID passed from EventDetails activity
        //eventId = getIntent().getStringExtra("event_id");
        eventId = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("event_id")));




        sendAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = announcementTitleEditText.getText().toString();
                sendAnnouncement(eventId,title);
                finish();     // move to previous activity
            }
        });
    }




    private void sendAnnouncement(int eventId, String title) {
        db.collection("eventsCollection")
                .document(String.valueOf(eventId))
                .update("announcements", FieldValue.arrayUnion(title))
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SendAnnouncement.this, "Announcement added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after sending announcement
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SendAnnouncement.this, "Failed to add announcement", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}