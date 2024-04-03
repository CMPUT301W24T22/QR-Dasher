package com.example.qr_dasher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Activity displaying details of a specific event.
 * It retrieves the event details from Firestore and displays the list of attendees.
 */
public class EventDetails extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView attendeeListView;
    private ListView signupListView;
    private List<String> attendeeListUserNames,  attendeeListDetails, attendeeListEmails;
    private List<String> signUpListListUserNames,signUpListListDetails, signUpListListEmails;
    private List<Integer>attendeeListUserIds, signUpListListUserIds;
    /**
     * Initializes the activity, sets up UI components and listeners,
     * and retrieves event details from Firebase Firestore.
     *
     * @param savedInstanceState Saved instance state bundle
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Get event name from intent extras

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        String eventIDStr = intent.getStringExtra("event_id");
        List<String> attendeeList = intent.getStringArrayListExtra("attendee_list");
        List<String> signupList = intent.getStringArrayListExtra("signup_list");



        if (attendeeList != null) {
            Log.d("ListSize", "Attendee List Size: " + attendeeList.size());
        } else {
            Log.d("ListSize", "Attendee List is null");
        }

        if (signupList != null) {
            Log.d("ListSize", "Signup List Size: " + signupList.size());
        } else {
            Log.d("ListSize", "Signup List is null");
        }

        TextView eventNameTextView = findViewById(R.id.eventNameTextView);

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        attendeeListView = findViewById(R.id.attendee_list_view);
        signupListView = findViewById(R.id.signup_listview);

        db = FirebaseFirestore.getInstance();

        // Set the event name to the TextView
        eventNameTextView.setText(eventName);

        getUserDetailsFromFirebase(attendeeList,signupList);
        Button notifyButton = findViewById(R.id.notify_button);
        Button qrButton = findViewById(R.id.qr_code_button);
//        Button posterUploadButton = findViewById(R.id.event_poster_button);


        ArrayList<String> tokensList= new ArrayList<>();                       // tokens
        for(String userId: attendeeList){
            // Get the document reference for the user with this userId
            DocumentReference userRef = db.collection("users").document(userId);

            // Retrieve the token from Firestore for this user
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the token field from the document
                            String token = document.getString("token");
                            // Add the token to tokensList
                            Log.d("SendNotification", "token : " + token); // Log the attendee list

                            if (token != null) {
                                tokensList.add(token);
                            }
                        } else {
                            Log.d("HomePage", "No such document");
                        }
                    } else {
                        Log.d("HomePage", "get failed with ", task.getException());
                    }
                }
            });
        }


        // Set OnClickListener for the notification button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetails.this, SendNotification.class);
                          // Pass the event ID to SendNotification activity
                intent.putExtra("event_id", eventIDStr);
                         // Pass the attendee list as an extra to the SendNotification activity
                intent.putStringArrayListExtra("tokensList", (ArrayList<String>) tokensList);
                startActivity(intent);
            }
        });

//        posterUploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle notification button click
////                Intent intent = new Intent(EventDetails.this, SendNotification.class);
////                startActivity(intent);
//            }
//        });
    }
    /**
     * Displays the list of attendees in a ListView.
     *
     * @param attendeeList The list of attendees to display.
     */

//    private void displayAttendeesAndSignups(List<String> attendeeList,List<String> signUpList ) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview, attendeeList);
//        attendeeListView.setAdapter(adapter);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.mytextview, signUpList);
//        signupListView.setAdapter(adapter2);
//    }
    private void getUserDetailsFromFirebase(List<String> attendeeList, List<String> signUpList){
       // Log.d("length of attendeeList","Attendee List Size: " + attendeeList.size());
        db = FirebaseFirestore.getInstance();
        attendeeListUserNames = new ArrayList<>();
        attendeeListUserIds = new ArrayList<>();
        attendeeListDetails= new ArrayList<>();
        attendeeListEmails = new ArrayList<>();

        signUpListListUserNames = new ArrayList<>();
        signUpListListUserIds = new ArrayList<>();
        signUpListListDetails= new ArrayList<>();
        signUpListListEmails = new ArrayList<>();

        if (attendeeList!=null&&!attendeeList.isEmpty()) {

            List<Integer> attendeeListInt = new ArrayList<>();
            for (String str : attendeeList) {
                attendeeListInt.add(Integer.parseInt(str));
            }



            // Attendee List
            db.collection("users")
                    .whereIn("userId", attendeeListInt)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Log.d("Firestore", "Data retrieval successful");

                            // Clear the lists before updating with new data
                            attendeeListUserNames.clear();
                            attendeeListUserIds.clear();
                            attendeeListDetails.clear();
                            attendeeListEmails.clear();


                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Integer userId = documentSnapshot.getLong("userId").intValue();
                                String userName = documentSnapshot.getString("name");
                                String userEmail = documentSnapshot.getString("email");
                                String userDetail = documentSnapshot.getString("details");

                                // Add user details to respective lists
                                attendeeListUserNames.add(userName);
                                attendeeListUserIds.add(userId);
                                attendeeListDetails.add(userDetail);
                                attendeeListEmails.add(userEmail);

                            }
                            if (attendeeListUserNames != null) {
                                displayAttendee(attendeeListUserNames);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    });
        }
        if (signUpList!=null&&!signUpList.isEmpty()) {
            List<Integer> signUpListInt = new ArrayList<>();
            for (String str : signUpList) {
                signUpListInt.add(Integer.parseInt(str));
            }


            // for sign up list
            db.collection("users")
                    .whereIn("userId", signUpListInt)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Log.d("Firestore", "Data retrieval successful");

                            // Clear the lists before updating with new data
                            signUpListListUserNames.clear();
                            signUpListListUserIds.clear();
                            signUpListListDetails.clear();
                            signUpListListEmails.clear();

                            // Iterate through the query results
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Integer userId = documentSnapshot.getLong("userId").intValue();
                                String userName = documentSnapshot.getString("name");
                                String userEmail = documentSnapshot.getString("email");
                                String userDetail = documentSnapshot.getString("details");

                                // Add user details to respective lists
                                signUpListListUserNames.add(userName);
                                signUpListListUserIds.add(userId);
                                signUpListListDetails.add(userDetail);
                                signUpListListEmails.add(userEmail);

                            }
                            if (signUpListListUserNames != null) {
                                displaySignup(signUpListListUserNames);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            e.printStackTrace();
                        }
                    });
        }

    }
    private void displayAttendee(List<String> attendeeList) {
        Log.d("length of attendeeList","Attendee List Size: " + attendeeList.size());

        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, attendeeList);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        attendeeListView.setAdapter(adapter);
    }
    private void displaySignup(List<String> signUplist) {

        // Create an ArrayAdapter to display the event names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.mytextview_nopicture, signUplist);
        adapter.notifyDataSetChanged();
        // Set the adapter to the ListView
        signupListView.setAdapter(adapter);
    }
}
