package com.example.qr_dasher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private Button selectImageButton, uploadButton, nextButton;
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
        nextButton = findViewById(R.id.next_button);


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
                            user.settoken(token);

                            if (details != null){
                                user.setDetails(details);
                            }
                            if (profile_picture != null){
                                user.setProfile_image(Picture.convertBitmaptoString(profile_picture));
                            }
                            saveUserToCache(user);
                            addUserToFirestore(user);
                        });



               /* FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (task.isSuccessful()) {
                                    // Get new FCM registration token
                                    String token = task.getResult();
                                    user.settoken(token);
                                }
                            }
                        });*/


            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, RolePage.class));
            }
        });
    }

    // Callback method to receive the uploaded image bitmap from ImageUploadFragment
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
     */

    private void saveUserToCache(User user){
        SharedPreferences.Editor editor = app_cache.edit();
        editor.putInt("UserID", user.getUserId());
        editor.apply();
    }
}
