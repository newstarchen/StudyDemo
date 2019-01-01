package com.example.convest;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TwoAdapter extends ArrayAdapter<Two> {

    private int resourceId;

    public TwoAdapter(Context context, int textViewResourceId, List<Two> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Two two = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView Twotitle = (TextView) view.findViewById(R.id.isname);
        TextView TwoArtist = (TextView) view.findViewById(R.id.isman);
        Twotitle.setText(two.getTitle());
        TwoArtist.setText(two.getArtist());
        return view;
    }
}
