package com.example.qr_dasher;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
/**
 * SendNotification activity allows users to compose and send notifications.
 * It provides input fields for notification title and content, along with a button to send the notification.
 */
public class SendNotification extends AppCompatActivity {
    private EditText notificationTitleEditText;
    private EditText notificationContentEditText;
    private Button sendNotificationButton;
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
    }
}
