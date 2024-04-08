package com.example.qr_dasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * An activity to display a list of users for an admin to view.
 */
public class AdminUserList extends AppCompatActivity {

    ListView userListView;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private ArrayList<String> userNamesList, userDPList, userIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_list);

        userListView = findViewById(R.id.userList);

        userNamesList = new ArrayList<>(); // List to hold user names
        userDPList = new ArrayList<>(); // List to hold user names
        userIDList = new ArrayList<>(); // List to hold user ids

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        // Fetch user names and IDs from Firestore
        usersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get user name from each document
                String userName = documentSnapshot.getString("name");
                userNamesList.add(userName);
                // Get user profile picture from each document
                String profile_picture = documentSnapshot.getString("profile_image");
                userDPList.add(profile_picture);
                // Get userID (documentID) from each document
                String userID = documentSnapshot.getId();
                userIDList.add(userID);
            }
            // Populate ListView with user names
            ListAdapter adapter = new ListAdapter(AdminUserList.this, userNamesList, userDPList);
            userListView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(AdminUserList.this, "Failed to retrieve user names", Toast.LENGTH_SHORT).show();
            Log.e("AdminUserList", "Error fetching user names: " + e.getMessage());
        });

        // Set item click listener for the ListView
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected user ID
            String selectedUserID = userIDList.get(position);
            // Create an intent to start the AdminUserProfile activity
            Intent intent = new Intent(AdminUserList.this, AdminUserProfile.class);
            // Pass the selected user ID as an extra to the intent
            intent.putExtra("userID", selectedUserID);
            // Start the activity
            startActivity(intent);
        });
    }

    /**
     * Called when the activity is resumed.
     * Method to fetch users from Firestore and populate the UI with it.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Clear previous data
        userNamesList.clear();
        userDPList.clear();
        userIDList.clear();

        // Fetch user names and IDs from Firestore
        usersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get user name from each document
                String userName = documentSnapshot.getString("name");
                userNamesList.add(userName);
                // Get user profile picture from each document
                String profile_picture = documentSnapshot.getString("profile_image");
                userDPList.add(profile_picture);
                // Get userID (documentID) from each document
                String userID = documentSnapshot.getId();
                userIDList.add(userID);
            }
            // Update ListView with new data
            ListAdapter adapter = new ListAdapter(AdminUserList.this, userNamesList, userDPList);
            userListView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(AdminUserList.this, "Failed to retrieve user names", Toast.LENGTH_SHORT).show();
            Log.e("AdminUserList", "Error fetching user names: " + e.getMessage());
        });
    }


}
