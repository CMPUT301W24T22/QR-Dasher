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

public class AdminUserEventListFragment extends DialogFragment {

    private ListView eventListView;
    private TextView eventTypeTextView;
    private ArrayList<String> eventArrayList;
    private FirebaseFirestore db;
    private CollectionReference eventsCollection;

    public AdminUserEventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_user_event_list, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection("eventsCollection");

        // Initialize views
        eventListView = rootView.findViewById(R.id.events_list);
        eventTypeTextView = rootView.findViewById(R.id.event_type);

        // Retrieve arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            char eventType = bundle.getChar("eventType", ' ');
            eventArrayList = bundle.getStringArrayList("eventList");

            // Set event type text
            switch (eventType) {
                case 's':
                    eventTypeTextView.setText("Signed Up Events");
                    break;
                case 'a':
                    eventTypeTextView.setText("Attended Events");
                    break;
                default:
                    eventTypeTextView.setText("Events");
                    break;
            }

            // Retrieve event names from Firebase
            ArrayList<String> eventIds = bundle.getStringArrayList("eventList");
            if (eventIds != null) {
                ArrayList<String> eventNames = new ArrayList<>();
                for (String eventId : eventIds) {
                    // Retrieve event name from Firestore using event ID
                    eventsCollection.document(eventId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Document exists, retrieve event name
                                    String eventName = documentSnapshot.getString("name");
                                    eventNames.add(eventName + "\nId: " + eventId);
                                    // Update ListView adapter when all event names are retrieved
                                    if (eventNames.size() == eventIds.size()) {
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                                android.R.layout.simple_list_item_1, eventNames);
                                        eventListView.setAdapter(adapter);
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure
                                Log.e("AdminUserEventListFragment", "Error retrieving event name: " + e.getMessage());
                            });
                }
            }
        }

        return rootView;
    }
}
