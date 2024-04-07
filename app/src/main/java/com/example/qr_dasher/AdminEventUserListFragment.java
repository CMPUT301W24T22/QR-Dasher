package com.example.qr_dasher;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminEventUserListFragment extends DialogFragment {

    private ListView usersListView;
    private TextView listTypeTextView;
    private ArrayList<String> usersArrayList;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    public AdminEventUserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_event_user_list, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        // Initialize views
        usersListView = rootView.findViewById(R.id.users_list);
        listTypeTextView = rootView.findViewById(R.id.list_type);

        // Retrieve arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            char eventType = bundle.getChar("listType", ' ');
            usersArrayList = bundle.getStringArrayList("userList");

            // Set event type text
            switch (eventType) {
                case 's':
                    listTypeTextView.setText("Signed Up Users");
                    break;
                case 'a':
                    listTypeTextView.setText("Attendees");
                    break;
                default:
                    listTypeTextView.setText("Events");
                    break;
            }

            // Retrieve event names from Firebase
            ArrayList<String> userIds = bundle.getStringArrayList("userList");
            if (userIds != null) {
                ArrayList<String> userNames = new ArrayList<>();
                for (String userId : userIds) {
                    // Retrieve event name from Firestore using event ID
                    usersCollection.document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Document exists, retrieve event name
                                    String userName = documentSnapshot.getString("name");
                                    userNames.add(userName + "\nId: " + userId);
                                    // Update ListView adapter when all event names are retrieved
                                    if (userNames.size() == userIds.size()) {
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                                android.R.layout.simple_list_item_1, userNames);
                                        usersListView.setAdapter(adapter);
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure
                                Log.e("AdminEventUserListFragment", "Error retrieving user name: " + e.getMessage());
                            });
                }
            }
        }

        return rootView;
    }
}