package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class DisplayQRActivity extends AppCompatActivity {
    public static final String EXTRA_QR_CODES = "extra_qr_codes";
    Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qractivity);


        List<String> qrCodes = getIntent().getStringArrayListExtra(EXTRA_QR_CODES);
        if (qrCodes != null) {
            displayQRcodes(qrCodes);
        }
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void displayQRcodes(List<String> qrCodes){
        // To dynamically add image view we make a container
        LinearLayout container = findViewById(R.id.container);

        for (String qrCodeString: qrCodes){
            if (qrCodeString != null) { // null check
                // converting string back to bitmap format
                byte[] imageBytes = Base64.decode(qrCodeString, Base64.DEFAULT);
                Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(qrCodeBitmap);
                // Add ImageView to your layout (LinearLayout, RelativeLayout, etc.)
                // For example, if you have a LinearLayout with ID 'container':
                // LinearLayout container = findViewById(R.id.container);
                // container.addView(imageView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 16);

                imageView.setLayoutParams(layoutParams);
                container.addView(imageView);
            }
        }

    }
}