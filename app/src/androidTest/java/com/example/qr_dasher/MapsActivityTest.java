package com.example.qr_dasher;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapsActivityTest {

    private List<String> someAttendeeList = Arrays.asList("attendee1", "attendee2", "attendee3");

    @Rule
    public ActivityScenarioRule<MapsActivity> activityScenarioRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class)
            .putExtra("eventIDstr", "dummyEventId")); // Provide a dummy event ID

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockEventsCollectionRef;

    @Mock
    private DocumentReference mockEventDocumentRef;

    @Mock
    private CollectionReference mockUsersCollectionRef;

    @Mock
    private DocumentReference mockUserDocumentRef;

    @Before
    public void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this);

        // Mock Firestore interactions
        Mockito.when(mockFirestore.collection("eventsCollection")).thenReturn(mockEventsCollectionRef);
        Mockito.when(mockFirestore.collection("users")).thenReturn(mockUsersCollectionRef);

        // Mock Firestore document references
        Mockito.when(mockEventsCollectionRef.document("dummyEventId")).thenReturn(mockEventDocumentRef);
        Mockito.when(mockUsersCollectionRef.document(Mockito.anyString())).thenReturn(mockUserDocumentRef);

        // Mock Firestore document snapshot results
        DocumentSnapshot mockEventSnapshot = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(mockEventDocumentRef.get()).thenReturn(Tasks.forResult(mockEventSnapshot));
        Mockito.when(mockEventSnapshot.exists()).thenReturn(true);
        Mockito.when(mockEventSnapshot.get("attendee_list")).thenReturn(someAttendeeList);

        DocumentSnapshot mockUserSnapshot = Mockito.mock(DocumentSnapshot.class);
        Mockito.when(mockUserDocumentRef.get()).thenReturn(Tasks.forResult(mockUserSnapshot));
        Mockito.when(mockUserSnapshot.exists()).thenReturn(true);

        // Provide the mocked dependencies to the MapsActivity instance
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                // Use reflection to access the private field
                Field dbField = MapsActivity.class.getDeclaredField("db");
                dbField.setAccessible(true); // Make the field accessible
                dbField.set(activity, mockFirestore); // Set the mockFirestore to the private field
                activity.populateMapWithMarkers(); // Call the method under test
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        });
    }

    @Test
    public void testPopulateMapWithMarkers() {
        // Perform actions on the activity and verify behavior
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.populateMapWithMarkers();


        });
    }
}
