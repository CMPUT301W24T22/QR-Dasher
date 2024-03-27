package com.example.qr_dasher;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class NotificationSender {

    private FirebaseFirestore db;

    public NotificationSender() {
        db = FirebaseFirestore.getInstance();
    }

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
    }
}