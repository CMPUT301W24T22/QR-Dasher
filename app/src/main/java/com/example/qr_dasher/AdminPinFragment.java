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
 * A simple {@link DialogFragment} subclass.
 * Use the {@link AdminPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPinFragment extends DialogFragment {
    private Button enter_button;
    private EditText enter_pin;

    public AdminPinFragment() {
        // Required empty public constructor
    }

    public static AdminPinFragment newInstance() {
        return new AdminPinFragment();
    }

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
