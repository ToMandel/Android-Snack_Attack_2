package com.example.snackattack_2.Game_2.utilities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.snackattack_2.Game_2.data.ScoreRecord;

import java.util.List;

public class ScoreRecordAdapter extends ArrayAdapter<ScoreRecord> {

    private final Context mContext;

    public ScoreRecordAdapter(Context context, int resource, List<ScoreRecord> items) {
        super(context, resource, items);
        this.mContext = context;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(android.R.layout.simple_list_item_1, null);
        }

        ScoreRecord p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(android.R.id.text1);

            if (tt1 != null) {
                tt1.setText(p.toString());
                tt1.setTextColor(Color.BLACK);
            }
        }

        return v;
    }
}