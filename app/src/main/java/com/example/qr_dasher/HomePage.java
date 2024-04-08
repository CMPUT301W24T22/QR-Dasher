package com.example.qr_dasher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This activity represents the home page where users can input their information and upload their profile picture.
 * User data is stored locally in SharedPreferences and uploaded to Firestore upon submission.
 */

public class HomePage extends AppCompatActivity implements ImageUploadFragment.ImageUploadListener{

    private EditText nameEdit, emailEdit, detailsEdit;
    private CheckBox geolocationCheckBox;
    private ImageView imageUpload;
    private Button selectImageButton, uploadButton, skipButton;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private Bitmap profile_picture;
    private SharedPreferences app_cache;

    /**
     * Initializes the activity, sets up UI components and listeners,
     * and checks if the user is already logged in. If logged in, redirects to RolePage.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        app_cache = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        int userId = app_cache.getInt("UserID", -1);
        if (userId != -1) {
            startActivity(new Intent(HomePage.this, RolePage.class));
            finish();
        }

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        detailsEdit = findViewById(R.id.details_edit);
        geolocationCheckBox = findViewById(R.id.geolocation);
        imageUpload = findViewById(R.id.image_upload);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadButton = findViewById(R.id.upload_button);
        skipButton = findViewById(R.id.next_button);


        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUploadFragment imageUploadFragment = new ImageUploadFragment();
                imageUploadFragment.setImageUploadListener(HomePage.this);
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

                // Validate user input
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(HomePage.this, "Please fill in name and email", Toast.LENGTH_SHORT).show();
                    return;

                }

                // Check for valid email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Log.d("EmailValidation", "Invalid email: " + email);
                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Log.d("EmailValidation", "Valid email: " + email);
                }
                // If no image uploaded, generate a profile picture
                if (profile_picture == null) {
                    profile_picture = generateProfilePicture(name);
                }
                
                // Create a User object
                User user = new User(name, email, location);
                
                // tokens used for notifications
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("tag","Couldn't retrieve FCM token",task.getException());
                                return;
                            }
                            String token = task.getResult();
                            Log.d("Token", token);
                            user.setToken(token);

                            if (details != null){
                                user.setDetails(details);
                            }
                            if (profile_picture != null){
                                user.setProfile_image(Picture.convertBitmaptoString(profile_picture));
                            }
                            saveUserToCache(user, false);
                            addUserToFirestore(user);
                        });
                startActivity(new Intent(HomePage.this, RolePage.class));
                finish();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User guest = User.createGuest();
                saveUserToCache(guest, true);
                addUserToFirestore(guest);
                startActivity(new Intent(HomePage.this, RolePage.class));
                finish();
            }
        });
    }

    /**
     * Callback method to receive the uploaded image bitmap from ImageUploadFragment.
     *
     * @param imageBitmap The bitmap of the uploaded image.
     */
    @Override
    public void onImageUpload(Bitmap imageBitmap) {
            // Display the uploaded image in ImageView
        imageUpload.setImageBitmap(imageBitmap);
            // Store the image bitmap in profile_picture variable
        profile_picture = imageBitmap;
    }

     /**
     * Adds the user data to Firestore
     *
     * @param user The User object containing user data.
     */
    private void addUserToFirestore(User user) {
        int userId = user.getUserId();
        usersCollection.document(String.valueOf(userId))
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HomePage.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomePage.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

      /**
      * Saves the user data to SharedPreferences for local caching.
      *
      * @param user The User object containing user data.
      * @param guest Boolean value to find out if user is logging in as a guest or not.
      *
      */

    private void saveUserToCache(User user, boolean guest){
        SharedPreferences.Editor editor = app_cache.edit();
        editor.putInt("UserID", user.getUserId());
        if(guest == true){
            editor.putBoolean("Guest", true);
        } else {
            editor.putBoolean("Guest", false);
        }
        editor.apply();
    }

    /**
     * Generates a profile picture with the first letter of the user's name.
     *
     * @param name The name of the user.
     *
     * @return The generated profile picture bitmap.
     */
    private Bitmap generateProfilePicture(String name) {
        int widthHeight = 200; // Width and Height in pixel
        Bitmap bitmap = Bitmap.createBitmap(widthHeight, widthHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

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
