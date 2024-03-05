package com.example.qr_dasher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class HomePage extends Activity {

    private EditText nameEdit, emailEdit, detailsEdit;
    private CheckBox geolocationCheckBox;
    private ImageView imageUpload;
    private Button selectImageButton, uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        detailsEdit = findViewById(R.id.details_edit);
        geolocationCheckBox = findViewById(R.id.geolocation);
        imageUpload = findViewById(R.id.image_upload);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadButton = findViewById(R.id.upload_button);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle image selection
                // You can use an Intent to open an image picker
                // and set the selected image to the ImageView
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
                if (name.isEmpty() || email.isEmpty() || details.isEmpty()) {
                    Toast.makeText(HomePage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a User object
                User user = new User(name, email, location);
                if (details != null){
                    user.setDetails(details);
                }
            }
        });
    }
}
