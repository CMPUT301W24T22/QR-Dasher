package com.example.qr_dasher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * Activity for editing user profile information.
 * Allows users to edit their profile details including name, email, details, and profile picture.
 * The edited profile information is updated in Firebase Firestore.
 */

public class EditProfile extends AppCompatActivity implements ImageUploadFragment.ImageUploadListener{

    private EditText nameEdit, emailEdit, detailsEdit;
    private CheckBox geolocationCheckBox;
    private ImageView imageUpload;
    private Button selectImageButton, uploadButton;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private Bitmap profile_picture;
    private SharedPreferences app_cache;
    /**
     * Initializes the activity, sets up UI components and listeners,
     * and retrieves user data from Firebase Firestore.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int userId = app_cache.getInt("UserID", -1);
        retrieveUserFromFirebase(String.valueOf(userId), new FirebaseCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    // Populate UI with retrieved user data
                    populateUIWithUserData(user);
                } else {
                    // Handle user not found or retrieval failure
                }
            }
        });

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        detailsEdit = findViewById(R.id.details_edit);
        geolocationCheckBox = findViewById(R.id.geolocation);
        imageUpload = findViewById(R.id.image_upload);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadButton = findViewById(R.id.upload_button);
        Button generateProfilePictureButton = findViewById(R.id.generate_profile_button);
        generateProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                if (!name.isEmpty()) {
                    Bitmap profilePicture = generateProfilePicture(name);
                    imageUpload.setImageBitmap(profilePicture);
                    profile_picture = profilePicture;
                } else {
                    Toast.makeText(EditProfile.this, "Please enter your name first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUploadFragment imageUploadFragment = new ImageUploadFragment();
                imageUploadFragment.setImageUploadListener(EditProfile.this);
                imageUploadFragment.show(getSupportFragmentManager(), "image_upload_fragment");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = nameEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String details = detailsEdit.getText().toString().trim();
                boolean location = geolocationCheckBox.isChecked();

                User user = new User(name, email, location);
                user.setUserId(userId);
                user.setDetails(details);
                if (profile_picture != null){
                    user.setProfile_image(Picture.convertBitmaptoString(profile_picture));
                }

                updateUserOnFirebase(user);

                // After updating the user, go back to the previous activity (Attendee)
                finish();
            }
        });

        // Add the back button functionality
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity (Attendee)
                finish();
            }
        });
    }

    // Callback method to receive the uploaded image bitmap from ImageUploadFragment
    /**
     * Callback method to receive the uploaded image bitmap from ImageUploadFragment.
     * Updates the profile picture ImageView and stores the image bitmap in the profile_picture variable.
     *
     * @param imageBitmap The uploaded image bitmap
     */
    @Override
    public void onImageUpload(Bitmap imageBitmap) {
        // Display the uploaded image in ImageView
        imageUpload.setImageBitmap(imageBitmap);
        // Store the image bitmap in profile_picture variable
        profile_picture = imageBitmap;
    }
      /**
     * Retrieves user data from Firebase Firestore based on the provided user ID.
     *
     * @param userID   The user ID used to retrieve user data.
     * @param callback The callback to handle the retrieved user data.
     */

    public void retrieveUserFromFirebase(String userID, final FirebaseCallback callback) {
        // Retrieve the user document from Firestore using documentID (userID)
        usersCollection.document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, create a User object from the data
                        User user = documentSnapshot.toObject(User.class);
                        // Optionally, set the userID in the User object
                        user.setUserId(Integer.parseInt(userID));
                        // Invoke the callback with the retrieved user object
                        callback.onCallback(user);
                    } else {
                        // Document doesn't exist
                        Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                        callback.onCallback(null); // Notify callback with null user
                    }
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    Toast.makeText(EditProfile.this, "Error retrieving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onCallback(null); // Notify callback with null user
                });
    }
     /**
     * Updates user data on Firebase Firestore.
     *
     * @param user The User object containing updated user data.
     */


    public void updateUserOnFirebase(User user){
        int userId = user.getUserId();

        usersCollection.document(String.valueOf(userId))
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfile.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProfile.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    /**
     * Populates UI with retrieved user data.
     *
     * @param user The User object containing user data to be displayed in UI.
     */
    // Method to populate UI with retrieved user data
    private void populateUIWithUserData(User user) {
        String profileImageString = user.getProfile_image();
        Bitmap original_profile_picture = null;
        if (profileImageString != null) {
            original_profile_picture = Picture.convertStringtoBitmap(profileImageString);
        }
        String original_name = user.getName();
        String original_email = user.getEmail();
        String original_details = user.getDetails();
        boolean original_location = user.getLocation();

        nameEdit.setText(original_name);
        emailEdit.setText(original_email);
        detailsEdit.setText(original_details);
        geolocationCheckBox.setChecked(original_location);
        imageUpload.setImageBitmap(original_profile_picture);
    }


    // Define a callback interface
     /**
     * Callback interface for handling retrieved user data from Firebase.
     */
    public interface FirebaseCallback {
        /**
         * Callback method to handle retrieved user data.
         *
         * @param user The retrieved User object, or null if user not found or retrieval failed.
         */
        void onCallback(User user);
    }

    private Bitmap generateProfilePicture(String name) {
        int widthHeight = 200; // Width and Height in pixel
        Bitmap bitmap = Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //background color and draw a circle on the canvas
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setAntiAlias(true);
        float centerX = widthHeight / 2f;
        float centerY = widthHeight / 2f;
        float radius = widthHeight / 2f;
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        // Draw the first letter of the name in the center of the circle
        char firstLetter = name.trim().isEmpty() ? 'A' : name.trim().toUpperCase().charAt(0);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(120f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        // Calculate the position for the text, so it's centered within the circle
        float textY = centerY - ((textPaint.descent() + textPaint.ascent()) / 2f);

        canvas.drawText(String.valueOf(firstLetter), centerX, textY, textPaint);

        return bitmap;
    }


}
