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

/**
 * ScanQR activity facilitates scanning QR codes using the device's camera.
 * It utilizes the IntentIntegrator library for integrating QR code scanning functionality.
 */
public class ScanQR extends AppCompatActivity {
     /**
     * Initializes the activity's UI components and sets up click listeners.
     *
     * @param savedInstanceState This activity's previously saved state, or null if it has no saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force portrait mode
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startScanning();
    }
    /**
     * Initiates the QR code scanning process using IntentIntegrator.
     */
    private void startScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a QR code");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    /**
     * Handles the result of the QR code scanning activity.
     *
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode  The result code returned by the child activity.
     * @param data        The intent data returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Scanned text is available
                String scannedText = result.getContents();

                // Prepare the intent to pass back to the calling activity
                Intent intent = new Intent();
                intent.putExtra("scannedText", scannedText);

                // Set the result code and pass the intent back to the calling activity
                setResult(RESULT_OK, intent);
                finish(); // Finish the activity
            } else {
                // Handle case where scanning was canceled or failed
                Toast.makeText(this, "Scanning failed or canceled", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED); // Set result canceled
                finish(); // Finish the activity
            }
        }
    }

}
