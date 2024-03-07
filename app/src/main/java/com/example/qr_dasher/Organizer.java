package com.example.qr_dasher;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

public class Organizer extends AppCompatActivity {
    private ListView eventList;
    private EventAdapter eventAdapter;
    private ArrayList<Event> dataList;
    private int selected_event;

    private Button CreateEventButton;

    private FirebaseFirestore db;
    private CollectionReference events;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        dataList = new ArrayList<>();

        eventList = findViewById(R.id.eventList);

        eventAdapter = new EventAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);


        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        events = db.collection("events");


        CreateEventButton = findViewById(R.id.createEventButton);

        CreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new Event
                Intent intent = new Intent(Organizer.this, CreateEventOrganizer.class);
                startActivity(intent);
            }
        });



        events.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    dataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String name = doc.getString("name");
                        String details = doc.getString("details");
                        String userId = doc.getString("userID");
                        Log.d("Firestore", String.format("Event(%s, %s, %s) fetched", name,
                                details,userId));
                        int intValue = Integer.parseInt(userId);          //convert to integer
                        dataList.add(new Event(name, details, intValue));
                        eventAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_event = position;
                Event selectedEvent = dataList.get(selected_event);
                Intent intent = new Intent(Organizer.this, ManageEvent.class);   // need to change this to the arrow idk how
                intent.putExtra("name", (CharSequence) selectedEvent);
                //intent.putExtra("event", (CharSequence) clickedEvent);
                startActivity(intent);
            }
        });
    }
}