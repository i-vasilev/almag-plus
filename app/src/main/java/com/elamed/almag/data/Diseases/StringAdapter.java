package com.elamed.almag.data.Diseases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elamed.almag.R;
import com.elamed.almag.data.Disease;

import org.w3c.dom.Text;

import java.util.List;

public class StringAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private int layout;
    private List<String> strings;

    public StringAdapter(Context context, int layout, List<String> strings) {
        super(context, layout, strings);
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
        this.strings = strings;
    }

    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view = inflater.inflate(this.layout, parent, false);
        TextView textView =view.findViewById(R.id.textName);
        textView.setText(strings.get(position));
        return view;
    }
}
