package com.example.qr_dasher;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private String eventId;
    ArrayList<String> tokens;   // tokens


    /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);


        notificationTitleEditText = findViewById(R.id.notification_title_text);
        notificationContentEditText = findViewById(R.id.notification_content);
        sendNotificationButton = findViewById(R.id.send_notifications_button);



        db = FirebaseFirestore.getInstance();

        tokens = getIntent().getStringArrayListExtra("tokens");    //TODO

        // Get event ID passed from EventDetails activity
        eventId = getIntent().getStringExtra("eventId");
        Log.d("SendNotification", "Event Id: " + eventId);
        // Get attendeeList from EventDetails activity
        List<String> attendeeList = getIntent().getStringArrayListExtra("attendee_list");
        Log.d("SendNotification", "Attendee List: " + attendeeList); // Log the attendee list//


        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notificationTitleEditText.getText().toString();
                String message = notificationContentEditText.getText().toString();
                sendNotification(eventId,title,message);
            }
        });
    }




    /*private void sendNotification(int eventId, String title, String message) {
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
    }*/



    private void sendNotification(String eventId, String title, String message) {

        for (String token : tokens) {



            //String token = "TOKEN OF RECIEVER DEVICE";   // TODO String in our case
            // to send the notification we call the api of fcm(firebase)

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            JSONObject jsonNotification = new JSONObject();
            JSONObject bodyObject = new JSONObject();

            try {
                jsonNotification.put("title", title);
                jsonNotification.put("body", message);

                bodyObject.put("to", token);
                bodyObject.put("notification", jsonNotification);

            } catch (JSONException e) {
                Log.d("mylog", e.toString());
            }

            RequestBody rbody = RequestBody.create(mediaType, bodyObject.toString());
            Request request = new Request.Builder().url("https://fcm.googleapis.com/fcm/send")
                    .post(rbody)
                    //.addHeader("Authorization", "BMDCg-aAfwIjIMc6K96QEo-mMAhKPgvZA9f1ci7TOi2C46QmQIedn2k13yDPtq4S-ZLs9ZY33oYsTkPh-YzZdb0")    // key = your_server_key

                    .addHeader("Authorization", "Bearer AAAAIbdAKWc:APA91bG-N5tHLHW277XhphX1sSp1f57WYqw68GrOoVprQYuMuYKAl7J3zzMj-KytNZ3tsi5XUEe_l5tYe1cicE17PvGbfWlMgWvy1cm5ha6x3poKve3EDFS0IO3adidf0os0vqJZhHpL")    // key = your_server_key
                    .addHeader("Content-Type", "application/json").build();


            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                Log.d("mylog", e.toString());
            }

        }
    }



}