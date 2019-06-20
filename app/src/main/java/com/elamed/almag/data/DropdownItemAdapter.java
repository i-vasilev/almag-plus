package com.elamed.almag.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.elamed.almag.R;

import java.util.List;

public class DropdownItemAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    Activity activity;
    private int layout;
    private int width;
    private List<String> labels;

    public DropdownItemAdapter(Activity activity, int layout, List<String> labels, int width) {
        super(activity, layout, labels);

        this.layout = layout;
        this.labels = labels;
        this.activity = activity;
        this.width = width;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(activity)
                .inflate(R.layout.multiline_spinner_item, parent, false);
        textView.setText(labels.get(position));
        textView.setMaxWidth(width);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.multiline_spinner_dropdown_item, null);

        TextView title = ((TextView) convertView.findViewById(android.R.id.text1));
        title.setText(labels.get(position));

        return convertView;
    }
}