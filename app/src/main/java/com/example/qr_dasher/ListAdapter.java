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

/**
 * A custom ArrayAdapter for displaying a list of event names with corresponding images.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "ListAdapter";
    private List<String> eventPostersBase64; // List of Base64 strings representing images


    /**
     * Constructs a new ListAdapter.
     *
     * @param context             The context in which the adapter is being used.
     * @param eventNames          The list of event names to be displayed.
     * @param eventPostersBase64  The list of Base64 strings representing images for each event.
     */
    public ListAdapter(Context context, List<String> eventNames, List<String> eventPostersBase64) {
        super(context, 0, eventNames);
        this.eventPostersBase64 = eventPostersBase64;
    }


    /**
     * Gets a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.mytextview, parent, false);
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
