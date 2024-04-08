package com.example.qr_dasher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Service class responsible for handling Firebase Cloud Messaging (FCM) messages and generating notifications.
 *
 * https://www.geeksforgeeks.org/how-to-push-notification-in-android-using-firebase-cloud-messaging/ , GeeksForGeeks ,Dec 2020, Accessed in March 2024
 *
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called when a new FCM token is generated for the device.
     *
     * @param token The new FCM token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    /**
     * Called when a new FCM message is received to extract the title and body from the message.
     *
     * @param remoteMessage The received FCM message.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        generateNotification(title,body);
    }

    /**
     * Generates a notification based on the provided title and content.
     *
     * @param title   The title of the notification.
     * @param content The content/body of the notification.
     */
    public void generateNotification(String title,String content){
        String channel_id = "notification_channel";
        String channelName = "com.example.qr_dasher";
        Intent intent = new Intent(MyFirebaseMessagingService.this, Attendee.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Pass the intent to PendingIntent to start the next Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            // Create a Builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.qrdasher)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content);

            // notify the user of events
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis();                             // to get unique notification Id for every notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "notification_channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationId, builder.build());
    }
}