package com.example.qr_dasher;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

// zxing lib
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

// adding firebase references
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nullable;
/**
 * Activity for creating events by organizers.
 */

public class CreateEventOrganizer extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_QR_CODES = "extra_qr_codes";
    private ImageView qrImage, promotionalImage;
    private Button generateQRandCreateEvent, generatePromotionalQR, displayQRcodes, downloadButton, pickDateTime, eventPosterButton;
    private EditText eventName, eventDetails,maxAttendees;
    private TextView textDateTime;
    private Bitmap generatedQRCode, pgeneratedQRCode;

    private FirebaseFirestore db;
    private CollectionReference eventsCollection;
    private SharedPreferences app_cache; // To get the userID
    private static final int REQUEST_CODE_REUSE_QR = 123;
    private Event event;
    private int eventId;
    private List<String> reuseQRCodes;
    private DateTime dateTime;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int hour = 0;
    private int minute = 0;
    private int savedDay = 0;
    private int savedMonth = 0;
    private int savedYear = 0;
    private int savedHour = 0;
    private int savedMinute = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int SCAN_QR_REQUEST_CODE = 2;
    private boolean twoQRcodes = false;

    /**
     * onCreate method is called when the activity is starting. It initializes the activity layout,
     * retrieves necessary views from the layout, initializes Firebase Firestore, and sets up event listeners
     * for buttons to handle user interactions.
     * @param savedInstanceState A Bundle containing the activity's previously saved state, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_organizer);
        reuseQRCodes = getIntent().getStringArrayListExtra("reuseQRCodes");
        pickDateTime = findViewById(R.id.pickTime);
        textDateTime = findViewById(R.id.textDateTime);
        qrImage = findViewById(R.id.qrCode); // image
        promotionalImage = findViewById(R.id.promotionalQR);
        generateQRandCreateEvent = findViewById(R.id.generateQRandCreateEvent); // button
        generatePromotionalQR = findViewById(R.id.generatePromotionalQR);
        eventName = findViewById(R.id.eventName); // event Name
        eventDetails = findViewById(R.id.details); // event Name
        downloadButton = findViewById(R.id.downloadbutton);
        displayQRcodes = findViewById(R.id.displayQRcodes);
        eventPosterButton = findViewById(R.id.event_poster_button);
        maxAttendees = findViewById(R.id.max_attendees);


        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int userId = app_cache.getInt("UserID", -1);
        eventsCollection = db.collection("events");

        pickDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling datepicker dialog
                pickDate();
            }
        });

        generateQRandCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String event_name = eventName.getText().toString();
                String event_details = eventDetails.getText().toString();

                String maxAttendeesString = maxAttendees.getText().toString();

                String textDateTimeString = textDateTime.getText().toString();
                if (TextUtils.isEmpty(event_name) || (TextUtils.isEmpty(event_details)) ||TextUtils.isEmpty(textDateTimeString) ){
                    Toast.makeText(CreateEventOrganizer.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(maxAttendeesString)) {
                    if (!TextUtils.isDigitsOnly(maxAttendeesString)) {
                        Toast.makeText(CreateEventOrganizer.this, "Maximum attendees must be a number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int maxAttendees = Integer.parseInt(maxAttendeesString);
                    event = new Event(event_name, event_details, userId, maxAttendees);
                }
                else{
                    event = new Event(event_name, event_details, userId);

                }

                dateTime = new DateTime(savedYear, savedMonth, savedDay, savedHour, savedMinute);



                eventId = event.getEvent_id();
                //event.setDateTime(dateTime);
                //
                Calendar calendar = Calendar.getInstance();
                calendar.set(savedYear, savedMonth, savedDay, savedHour, savedMinute);
                Date eventDateTime = calendar.getTime();

                Timestamp eventTimestamp = new Timestamp(eventDateTime);
                event.setTimestamp(eventTimestamp);

                //
                event.generateQR("" + event.getEvent_id(), false);
                generatePromotionalQR.setVisibility(View.VISIBLE);
                downloadButton.setVisibility(View.VISIBLE);

                String qrCodeString = event.getAttendee_qr().getQrImage();
                byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                generatedQRCode = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                qrImage.setImageBitmap(generatedQRCode);

                AtomicReference<User> user = new AtomicReference<>();
                db.collection("users")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                                user.set(documentSnapshot.toObject(User.class));
                                user.get().addEventsCreated("" + event.getEvent_id());
                                updateFirebaseUser(documentSnapshot.getId(), user.get());
                            } else {
                                Log.d("Organizer", "No user found with UserId: " + userId);
                            }
                            downloadButton.setVisibility(View.VISIBLE);
                            displayQRcodes.setVisibility(View.VISIBLE);
                            eventPosterButton.setVisibility(View.VISIBLE);
                            generatePromotionalQR.setVisibility(View.VISIBLE);
                            pickDateTime.setVisibility(View.GONE);
                            generateQRandCreateEvent.setVisibility(View.GONE);
                            eventName.setEnabled(false);
                            eventDetails.setEnabled(false);
                            maxAttendees.setEnabled(false);
                            eventName.setFocusable(false);
                            eventDetails.setFocusable(false);
                            maxAttendees.setFocusable(false);
                            eventName.setFocusableInTouchMode(false);
                            eventDetails.setFocusableInTouchMode(false);
                            maxAttendees.setFocusableInTouchMode(false);

                        })
                        .addOnFailureListener(e -> {
                            Log.d("Organizer", "Failed to retrieve User from Firestore");
                            e.printStackTrace();
                        });

                addEventToFirebase(event);

            }
        });

        generatePromotionalQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.generateQR("" + event.getEvent_id(), true);
                String qrCodeString = event.getPromotional_qr().getQrImage();
                byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                promotionalImage.setImageBitmap(qrCodeBitmap);

                pgeneratedQRCode = qrCodeBitmap;
                // Upload the promotional QR code to firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("eventsCollection")
                        .document("" + eventId)
                        .update("promotional_qr", event.getPromotional_qr())
                        .addOnSuccessListener(aVoid ->{
                            Log.d("Organizer", "Event QR PROMOTIONAL ADDED successfully");
                            generatePromotionalQR.setEnabled(false);

                        })
                        .addOnFailureListener(e -> {
                            Log.d("Organizer", "Failed to update event in Firestore"+ eventId);
                            e.printStackTrace();
                        });
                twoQRcodes = true;

            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!twoQRcodes){
                    ShareQRFragment fragment = ShareQRFragment.newInstance(generatedQRCode, event.getName());
                    fragment.showFragment(getSupportFragmentManager());}
                else {
                    ShareQRFragment fragment = ShareQRFragment.newInstance(generatedQRCode, pgeneratedQRCode,event.getName());
                    fragment.showFragment(getSupportFragmentManager());
                }

            }

        });
        displayQRcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the ScanQR activity
                Intent intent = new Intent(CreateEventOrganizer.this, ScanQR.class);
                startActivityForResult(intent, SCAN_QR_REQUEST_CODE);
            }

        });




        eventPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }
    //    private Uri bitmapToUri(Bitmap bitmap) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, event.getName(), null);
//        return Uri.parse(path);
//    }
    private void pickDate(){
        getDateTimeCalendar();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
    private void getDateTimeCalendar(){
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        savedDay = dayOfMonth;
        savedMonth = month;
        savedYear = year;
        getDateTimeCalendar();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        savedHour = hourOfDay;
        savedMinute = minute;
        String s = String.format(Locale.US,"Date: %d-%d-%d\nTime: %d:%d"
                , savedYear, savedMonth, savedDay, savedHour, savedMinute);
        textDateTime.setText(s);

    }

    /**
     * Updates the user data in Firebase Firestore with the latest event created by the organizer.
     * This method is called after successfully generating a QR code for the event and adding the event
     * to Firestore.
     * @param userId The ID of the user whose data needs to be updated.
     * @param user The User object containing the updated user data.
     */
    private void updateFirebaseUser(String userId, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .update("eventsCreated", user.getEventsCreated())
                .addOnSuccessListener(aVoid -> Log.d("Organizer", "User updated successfully"))
                .addOnFailureListener(e -> {
                    Log.d("Organizer", "Failed to update user in Firestore");
                    e.printStackTrace();
                });
    }
    /**
     * Adds the newly created event to Firebase Firestore.
     * This method is called after generating the QR code for the event and before updating the user data.
     * @param event The Event object representing the newly created event.
     */

    private void addEventToFirebase(Event event) {
        db.collection("eventsCollection")
                .document("" + event.getEvent_id())
                .set(event)
                .addOnSuccessListener(aVoid -> Log.d("QR", "Successfully uploaded to firestore"))
                .addOnFailureListener(e -> {
                    Log.d("QR", "Couldn't be uploaded to firestore");
                    e.printStackTrace();
                });
    }

    /**
     * Retrieves QR codes for all events from Firebase Firestore.
     * This method is called when the organizer wants to display QR codes for all events stored in Firestore.
     * It retrieves the QR codes and starts a new activity to display them.
     */


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Method to save event poster to Firestore
    private void saveEventPosterToFirestore(String encodedImage) {
        db.collection("eventsCollection")
                .document("" + event.getEvent_id())
                .update("Poster", encodedImage)
                .addOnSuccessListener(aVoid -> Log.d("Event Poster", "Event poster saved to Firestore"))
                .addOnFailureListener(e -> {
                    Log.d("Event Poster", "Failed to save event poster to Firestore");
                    e.printStackTrace();
                });
    }
    /**
     * Handles the result of the activity launched to display QR codes for events.
     * This method is called when the activity to display QR codes returns a result.
     * It retrieves the selected QR code from the result and updates the QR code image view accordingly.
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_QR_REQUEST_CODE) { // Assuming 2 is the request code for QR code scanning
            if (resultCode == RESULT_OK && data != null) {
                String scannedText = data.getStringExtra("scannedText");
                if (scannedText != null) {
                    // Generate QR code based on scanned text
                    event.generateQR(scannedText, false);
                    String qrCodeString = event.getAttendee_qr().getQrImage();
                    byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                    generatedQRCode = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    qrImage.setImageBitmap(generatedQRCode);
                    addEventToFirebase(event);
                } else {
                    // Handle case where scanned text is null
                    Toast.makeText(this, "Failed to get scanned text", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle case where QR code scanning was canceled or failed
                Toast.makeText(this, "QR code scanning canceled or failed", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    // Resize bitmap if needed to avoid large image storage
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                    // Convert bitmap to Base64 string
                    String encodedImage = bitmapToBase64(resizedBitmap);
                    // Save the image to Firestore for the specific event
                    saveEventPosterToFirestore(encodedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



    }
}
