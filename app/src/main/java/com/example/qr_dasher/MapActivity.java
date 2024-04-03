package com.example.qr_dasher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MapActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private IMapController controller;
    private MyLocationNewOverlay locationOverlay;
    private Event event;
    private ArrayList<Marker> markerList = new ArrayList<>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        event = getIntent().getParcelableExtra("event");
        Log.d("DEBUG", String.format("onCreate: %s", event.getEvent_id()));
      
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //create the map
        setContentView(R.layout.activity_map);
        // Check user permission for location access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
        // Create map view
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);
        map.getLocalVisibleRect(new Rect());
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(3.0);
        GeoPoint myLocationGeoPoint = new GeoPoint(0, 0);
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        controller = map.getController();
        locationOverlay.enableMyLocation();
        locationOverlay.setDrawAccuracyEnabled(false);
        controller.setCenter(myLocationGeoPoint);
        controller.animateTo(myLocationGeoPoint);
        controller.setZoom(1);
        map.getOverlays().add(locationOverlay);
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
        map.onPause();
    }

    private void populateMapWithMarkers() {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("users")
            .whereArrayContains("eventsJoined", String.valueOf(event.getEvent_id()))
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            GeoPoint geoPoint = convertFirebaseGeoPoint(document.getGeoPoint("location"));
                            if (geoPoint != null) {
                                Marker marker = new Marker(map);
                                marker.setPosition(geoPoint);
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                marker.setTitle("Location");
                                map.getOverlays().add(marker);
                            }
                        }
                    }
                }
            });
    }



    private static GeoPoint convertFirebaseGeoPoint(com.google.firebase.firestore.GeoPoint firebaseGeoPoint) {
        if (firebaseGeoPoint != null) {
            double latitude = firebaseGeoPoint.getLatitude();
            double longitude = firebaseGeoPoint.getLongitude();
            return new org.osmdroid.util.GeoPoint(latitude, longitude);
        } else {
            return null;
        }
    }
}
