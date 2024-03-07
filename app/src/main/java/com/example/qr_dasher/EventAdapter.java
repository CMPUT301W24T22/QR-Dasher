package com.example.qr_dasher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventAdapter(Context context, ArrayList<Event> events){
        super(context,0, events);
       /* this.events = events;
        this.context = context;*/
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view ;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter, parent,false);
        }
        else {
            view = convertView;
        }
        Event event = getItem(position);    // events.get(position);

        TextView eventName = view.findViewById(R.id.selectedEvent);
        assert event != null;
        eventName.setText(event.getName());


        return view;
    }
}