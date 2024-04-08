package com.example.qr_dasher;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Fragment class to display a QR code image along with options to download, share, or delete it.
 */
public class QRImageFragment extends DialogFragment {

    private ImageView QRImage;
    private TextView QRType;
    private Button download_qr, share_qr, delete_qr;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private CollectionReference eventsCollection;
    private String eventID, qrType;

    /**
     * Default constructor for QRImageFragment .
     */
    public QRImageFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the layout for the fragment and initializes views.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr_image, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");
        eventsCollection = db.collection("eventsCollection");

        // Initialize views
        QRImage = view.findViewById(R.id.qr_image);
        QRType = view.findViewById(R.id.qr_type);
        download_qr = view.findViewById(R.id.download_image_button);
        share_qr = view.findViewById(R.id.share_image_button);
        delete_qr = view.findViewById(R.id.delete_image_button);

        // Get flag to hide buttons
        boolean hideButtons = getArguments().getBoolean("hideButtons", false);
        if (hideButtons) {
            download_qr.setVisibility(View.GONE);
            share_qr.setVisibility(View.GONE);
        }

        // Get QR bitmap and QR type from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            Bitmap qrBitmap = bundle.getParcelable("qrBitmap");
            qrType = bundle.getString("qrType");
            eventID = String.valueOf(bundle.getInt("eventID"));

            // Set QR image and type
            QRImage.setImageBitmap(qrBitmap);
            QRType.setText(qrType);
        }

        delete_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Promotional QR".equals(qrType)) {
                    eventsCollection.document(eventID)
                            .update("promotional_qr", null)
                            .addOnSuccessListener(aVoid -> {
                                // Handle deletion success
                                Toast.makeText(getContext(), "Promotional QR deleted successfully", Toast.LENGTH_SHORT).show();
                                dismiss();                                       // Dismiss the fragment after deletion
                            })
                            .addOnFailureListener(e -> {
                                // Handle deletion failure
                                Toast.makeText(getContext(), "Failed to delete Promotional QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else if ("Attendee QR".equals(qrType)) {
                    eventsCollection.document(eventID)
                            .update("attendee_qr", null)
                            .addOnSuccessListener(aVoid -> {
                                // Handle deletion success
                                Toast.makeText(getContext(), "Attendee QR deleted successfully", Toast.LENGTH_SHORT).show();
                                dismiss();                                      // Dismiss the fragment after deletion
                            })
                            .addOnFailureListener(e -> {
                                // Handle deletion failure
                                Toast.makeText(getContext(), "Failed to delete Attendee QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        return view;
    }
}
