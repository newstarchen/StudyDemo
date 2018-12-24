package com.example.truetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class Mp3Adapter extends ArrayAdapter<Mp3> {

    private int resourceId;

    public Mp3Adapter(Context context, int textViewResourceId, List<Mp3> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mp3 mp3 = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView Mp3number = (TextView) view.findViewById(R.id.number);
        TextView Mp3songName = (TextView) view.findViewById(R.id.music_songName);
        TextView Mp3Artist = (TextView) view.findViewById(R.id.music_Artist);
        Mp3number.setText((int) mp3.getSongId());
        Mp3Artist.setText(mp3.getArtist());
        Mp3songName.setText(mp3.getSongName());
        return view;
    }
}
