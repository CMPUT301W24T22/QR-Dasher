package com.example.qr_dasher;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




public class NotificationSender {

    /*private FirebaseFirestore db;

    public NotificationSender() {
        db = FirebaseFirestore.getInstance();
    }


    // TODO for TAP
    // Create an explicit intent for an Activity in your app.
    *//*Intent intent = new Intent(this, AlertDetails.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);*//*




    // TODO   NOTIFICATION CONTENT
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    // TODO
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // for tap
            // Set the intent that fires when the user taps the notification.
            *//*.setContentIntent(pendingIntent)
            .setAutoCancel(true);*//*

    // TODO





    //  TODO     To show notification

    *//*with(NotificationManagerCompat.from(this)) {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
        Manifest.permission.POST_NOTIFICATIONS
       ) != PackageManager.PERMISSION_GRANTED
   ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                        int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
        // notificationId is a unique int for each notification that you must define.
        notify(NOTIFICATION_ID, builder.build())
    }
*//*



    public void sendNotificationToAttendees(int eventId, String title, String message) {
        // Retrieve attendees of the event from Firestore
        db.collection("eventsCollection")
                .document(String.valueOf(eventId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> attendeeList = (List<String>) document.get("attendee_list");
                            if (attendeeList != null && !attendeeList.isEmpty()) {
                                // Send notification to each attendee
                                for (String attendee : attendeeList) {
                                    // Generate a unique message ID for each notification
                                    String messageId = UUID.randomUUID().toString();
                                    // Assuming you have a method to send notification to individual attendees
                                    sendNotificationToUser(attendee, title, message, messageId);
                                }
                            }
                        } else {
                            Log.d("NotificationSender", "No such document");
                        }
                    } else {
                        Log.d("NotificationSender", "get failed with ", task.getException());
                    }
                });
    }

    private void sendNotificationToUser(String userId, String title, String message, String messageId) {
        // Implementation for sending notification to individual user
        // You can use Firebase Cloud Messaging (FCM) or other services for this purpose
        // This method is left for you to implement based on your notification mechanism
        Log.d("NotificationSender", "Sending notification to user: " + userId);
        // Here you can use FCM or any other service to send the notification to the user
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(userId + "@fcm.googleapis.com")
                .setMessageId(messageId)
                .addData("title", title)
                .addData("message", message)
                .build());
    }*/
}