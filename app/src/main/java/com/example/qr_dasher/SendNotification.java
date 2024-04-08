package com.example.qr_dasher;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;


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
 *
 * https://www.youtube.com/watch?v=6_t87WW6_Gc , The Code City,2022, Accessed on March 2024
 */
public class SendNotification extends AppCompatActivity {
    private EditText notificationTitleEditText;
    private EditText notificationContentEditText;
    private Button sendNotificationButton;
    private FirebaseFirestore db;
    private String eventId;
    List<String> tokensList;


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

        // Get event ID passed from EventDetails activity
        eventId = getIntent().getStringExtra("event_id");
        // Get attendeeList from EventDetails activity
        tokensList = getIntent().getStringArrayListExtra("tokensList");


        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notificationTitleEditText.getText().toString();
                String message = notificationContentEditText.getText().toString();
                sendNotification(eventId,title,message);
                finish();     // move to previous activity
            }
        });
    }

    /**
     * Sends the notification to the attendees using Firebase Cloud Messaging (FCM) API.
     *
     * @param eventId The ID of the event associated with the notification.
     * @param title   The title of the notification.
     * @param message The content of the notification.
     */
    private void sendNotification(String eventId, String title, String message) {

        for (String token : tokensList) {
            Log.d("SendNotification", "Tokens : " + token);
            Log.d("SendNotification", "Title : " + title);
            Log.d("SendNotification", "Message : " + message);

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
                Log.d("mylog",e.toString());
            }

            RequestBody rbody = RequestBody.create(bodyObject.toString(),mediaType);
            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(rbody)
                    .addHeader("Authorization", "key=AAAAIbdAKWc:APA91bG-N5tHLHW277XhphX1sSp1f57WYqw68GrOoVprQYuMuYKAl7J3zzMj-KytNZ3tsi5XUEe_l5tYe1cicE17PvGbfWlMgWvy1cm5ha6x3poKve3EDFS0IO3adidf0os0vqJZhHpL")    // key = your_server_key
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("SendNotification", "Error sending notification", e);

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("SendNotification", "Notification sending failed: " + response.code());
                    } else {
                        Log.d("SendNotification", "Notification sent successfully!");
                    }
                }
            });
        }
    }
}