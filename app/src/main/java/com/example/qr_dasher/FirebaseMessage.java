package com.example.qr_dasher;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessage extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle incoming messages here
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // TODO: Handle the message data payload
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // TODO: Handle the message notification
        }
    }

    /*@Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        // Get updated InstanceID token.
        Log.d(TAG, "Refreshed token: " + token);

        // TODO: If you need to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.
    }*/
}
