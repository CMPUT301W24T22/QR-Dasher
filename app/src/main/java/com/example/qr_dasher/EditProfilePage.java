package com.example.qr_dasher;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfilePage extends Activity {

    private ImageView profileImage;
    private Button removePictureButton;
    private Button generatePictureButton;
    private EditText nameEditText;

    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile); // Make sure this is the correct layout name

        profileImage = findViewById(R.id.profile_image);
        removePictureButton = findViewById(R.id.button_remove_picture);
        generatePictureButton = findViewById(R.id.button_upload_picture);
        nameEditText = findViewById(R.id.edittext_name);
        cancelButton = findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity, which will close the current screen and go back to the Attendee dashboard
                finish();
            }
        });

        removePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Remove the current picture from the ImageView
                profileImage.setImageResource(android.R.color.transparent);
            }
        });

        generatePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                if (!name.isEmpty()) {
                    // Generate profile picture with initial
                    Bitmap profilePicture = generateProfilePicture(name);
                    profileImage.setImageBitmap(profilePicture);
                } else {
                    Toast.makeText(EditProfilePage.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO: Add listeners and logic for other UI elements as needed
    }

    private Bitmap generateProfilePicture(String name) {
        // Assuming we want the image to be 120dp x 120dp
        final int imageSize = (int) (120 * getResources().getDisplayMetrics().density);

        Bitmap image = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        // Fill the canvas with a circle shape or any background you prefer
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY); // Circle color
        canvas.drawCircle(imageSize / 2, imageSize / 2, imageSize / 2, paint);

        // Draw the letter
        paint.setColor(Color.BLACK); // Letter color
        paint.setTextSize(imageSize / 2); // Set letter size
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);

        // Get the first letter of the name
        String letter = name.substring(0, 1).toUpperCase();
        // Calculate the positions
        int xPos = canvas.getWidth() / 2;
        // yPos needs to take into account the baseline measurement
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        // Draw the letter on the canvas
        canvas.drawText(letter, xPos, yPos, paint);

        return image;
    }
}