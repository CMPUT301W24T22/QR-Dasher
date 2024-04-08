package com.example.qr_dasher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

/**
 * A DialogFragment subclass used for entering an admin PIN.
 * This fragment displays an input field for entering a PIN and a button to submit the entered PIN.
 * If the entered PIN matches the predefined admin PIN ("QRDasher"), it starts the AdminActivity.
 * Otherwise, it displays a toast message indicating an incorrect PIN.
 */
public class AdminPinFragment extends DialogFragment {
    private Button enter_button;
    private EditText enter_pin;

    /**
     * Default constructor for the AdminPinFragment.
     */
    public AdminPinFragment() {
        // Required empty public constructor
    }

    /**
     * Static factory method to create a new instance of AdminPinFragment.
     *
     * @return A new instance of AdminPinFragment.
     */
    public static AdminPinFragment newInstance() {
        return new AdminPinFragment();
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_pin, container, false);

        enter_button = rootView.findViewById(R.id.enter_button);
        enter_pin = rootView.findViewById(R.id.enter_pin);

        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered PIN
                String enteredPin = enter_pin.getText().toString().trim();

                // Check if entered PIN matches the predefined one
                if (enteredPin.equals("QRDasher")) {
                    // Open AdminActivity
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    // Close the dialog fragment
                    dismiss();
                } else {
                    // Show a message indicating incorrect PIN
                    Toast.makeText(getActivity(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
