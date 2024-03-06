package com.example.qr_dasher;

import android.content.Intent;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;


import android.widget.Toast;


public class ScanQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force portrait mode
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startScanning();
    }

    private void startScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a QR code");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // 'result.getContents()' contains the scanned text
                String scannedText = result.getContents();

                // Now you can use 'scannedText' as the result from scanning
                // Handle the scanned text as needed
                // Example: Display the scanned text in a Toast
                Toast.makeText(this, "Scanned Text: " + scannedText, Toast.LENGTH_LONG).show();

            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
        //finish();  // Finish the activity after scanning
    }
}