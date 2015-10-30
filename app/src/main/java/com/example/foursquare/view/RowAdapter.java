package com.example.foursquare.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foursquare.R;

import java.util.ArrayList;

class RowAdapter extends ArrayAdapter<Venue> {
    public RowAdapter(Context context, ArrayList<Venue> venues) {
        super(context, 0, venues);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Venue venue = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_name_distance);
        TextView address = (TextView) convertView.findViewById(R.id.list_address);

        String nameDistance = String.format(getContext().getResources().getString(R.string.name_and_distance), venue.mName, Integer.toString(venue.mDistance));

        name.setText(nameDistance);
        address.setText(venue.mAddress);

        return convertView;
    }
}
