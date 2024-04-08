package com.example.qr_dasher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This activity displays a map with markers representing check-in locations for a specific event.
 * It retrieves check-in locations from Firestore, converts them to GeoPoints, and adds markers to the map accordingly.
 */
public class MapsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private IMapController controller;
    private MyLocationNewOverlay locationOverlay;
    private String eventIDstr;
    ArrayList<Marker> markerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        populateMapWithMarkers();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
    /**
     * Initializes components required for the activity, such as map and location overlay.
     */

    public void initializeComponents() {
        eventIDstr = getIntent().getStringExtra("eventIDstr");


        initializeMap();
        initializeLocationOverlay();
    }
    /**
     * Initializes the map view.
     */

    public void initializeMap() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);
        map.getLocalVisibleRect(new Rect());
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(3.0);

        GeoPoint myLocationGeoPoint = new GeoPoint(0, 0);
        controller = map.getController();
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);

        locationOverlay.enableMyLocation();
        locationOverlay.setDrawAccuracyEnabled(false);

        controller.setCenter(myLocationGeoPoint);
        controller.animateTo(myLocationGeoPoint);
        controller.setZoom(1);

        map.getOverlays().add(locationOverlay);
    }
    /**
     * Initializes the location overlay on the map.
     */

    public void initializeLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        locationOverlay.enableMyLocation();
        locationOverlay.setDrawAccuracyEnabled(false);
        map.getOverlays().add(locationOverlay);
    }

    /**
     * Populates the map with markers representing check-in locations.
     */
    public void populateMapWithMarkers() {
        Toast.makeText(getApplicationContext(), "Event ID: " + eventIDstr, Toast.LENGTH_SHORT).show();
        FirebaseFirestore.getInstance().collection("eventsCollection").document(eventIDstr)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot eventDocument = task.getResult();
                            if (eventDocument.exists()) {
                                ArrayList<String> attendeeList = (ArrayList<String>) eventDocument.get("attendee_list");
                                if (attendeeList != null) {
                                    StringBuilder attendeeNames = new StringBuilder();
                                    for (String userId : attendeeList) {
                                        FirebaseFirestore.getInstance().collection("users").document(userId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot userDocument = task.getResult();
                                                            if (userDocument.exists()) {
                                                                com.google.firebase.firestore.GeoPoint firebaseGeoPoint = userDocument.getGeoPoint("geoPoint");
                                                                if (firebaseGeoPoint != null) {
                                                                    GeoPoint geoPoint = convertFirebaseGeoPoint(firebaseGeoPoint);
                                                                    if (geoPoint != null) {
                                                                        Marker marker = createMarker(geoPoint);
                                                                        map.getOverlays().add(marker);
                                                                        markerList.add(marker); // Add marker to list for future reference
                                                                        marker.setTitle(userDocument.getString("name"));
                                                                        map.invalidate(); // Refresh map
                                                                        // Append attendee name to the StringBuilder
                                                                        attendeeNames.append(userDocument.getString("name")).append("\n");
                                                                        // Show Toast to verify marker addition
                                                                        Toast.makeText(getApplicationContext(), "Marker added: " + userDocument.getString("name"), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            Log.d("Firestore", "Failed to fetch user document: ", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                    // Show Toast with attendee names
                                    Toast.makeText(getApplicationContext(), "Attendee List:\n" + attendeeNames.toString(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.d("Firestore", "Failed to fetch event document: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Creates a marker at the given GeoPoint.
     *
     * @param geoPoint The GeoPoint where the marker will be placed.
     * @return The created Marker object.
     */

    public Marker createMarker(GeoPoint geoPoint) {
        Marker marker = new Marker(map);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        return marker;
    }
    /**
     * Fetches the attendee name from Firestore and sets it as the title for the marker.
     *
     * @param attendees The Firestore collection reference for attendees.
     * @param attendeeID The ID of the attendee.
     * @param marker The marker object to which the title will be set.
     */

    public void fetchAttendeeName(CollectionReference attendees, String attendeeID, Marker marker) {
        attendees.document(attendeeID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            marker.setTitle(documentSnapshot.getString("name"));
                            Log.d("DEBUG", "onSuccess: marker.setTitle() is called");
                        }
                    }
                });
    }

    /**
     * Converts a Firebase GeoPoint object to an osmdroid GeoPoint object.
     *
     * @param firebaseGeoPoint The Firebase GeoPoint to be converted.
     * @return The converted osmdroid GeoPoint object, or null if the input is null.
     */

    public GeoPoint convertFirebaseGeoPoint(com.google.firebase.firestore.GeoPoint firebaseGeoPoint) {
        if (firebaseGeoPoint != null) {
            double latitude = firebaseGeoPoint.getLatitude();
            double longitude = firebaseGeoPoint.getLongitude();
            return new org.osmdroid.util.GeoPoint(latitude, longitude);
        } else {
            return null;
        }
    }
}
