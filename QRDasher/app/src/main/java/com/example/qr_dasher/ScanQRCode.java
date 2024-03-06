package com.example.qr_dasher;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;


import android.widget.Toast;


public class ScanQRCode extends AppCompatActivity {

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

    private void saveScannedEvent(String scannedText) {
        SharedPreferences sharedPref = getSharedPreferences("MyEvents", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // Generate a unique key for the event, for example by using the current time
        String eventKey = "event_" + System.currentTimeMillis();
        editor.putString(eventKey, scannedText);
        editor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // 'result.getContents()' contains the scanned text
                String scannedText = result.getContents();
                saveScannedEvent(scannedText);

                // Now you can use 'scannedText' as the result from scanning
                // Handle the scanned text as needed
                // Example: Display the scanned text in a Toast
                Toast.makeText(this, "Scanned Text: " + scannedText, Toast.LENGTH_LONG).show();

            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
        finish();  // Finish the activity after scanning
    }
}