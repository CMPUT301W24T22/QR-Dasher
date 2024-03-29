package com.example.qr_dasher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<String> {
    private static final String TAG = "EventAdapter";
    private List<String> eventPostersBase64; // List of Base64 strings representing images

    public EventAdapter(Context context, List<String> eventNames, List<String> eventPostersBase64) {
        super(context, 0, eventNames);
        this.eventPostersBase64 = eventPostersBase64;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.organizer_listview, parent, false);
        }

        TextView eventNameTextView = itemView.findViewById(R.id.textView); // Update to use correct TextView ID
        ImageView eventPosterImageView = itemView.findViewById(R.id.imageView); // Update to use correct ImageView ID

        // Check if eventNameTextView is not null before setting text
        if (eventNameTextView != null) {
            eventNameTextView.setText(getItem(position));
        }

        // Decode Base64 string to Bitmap
        String base64String = eventPostersBase64.get(position);
        Log.d(TAG, "Base64 string for position " + position + ": " + base64String); // Add this line to check Base64 string
        if (base64String != null && !base64String.isEmpty()) {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedBitmap != null) {
                eventPosterImageView.setImageBitmap(decodedBitmap);
            }
        }

        return itemView;
    }
}
