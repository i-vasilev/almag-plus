package com.elamed.almag.data.Diseases;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.elamed.almag.data.Disease;

import java.util.List;

public class DiseaseAdapter extends ArrayAdapter<Disease> {
    private LayoutInflater inflater;
    private int layout;
    private List<Disease> diseases;

    public DiseaseAdapter(Context context, int layout, List<Disease> diseases) {
        super(context, layout, diseases);
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
        this.diseases = diseases;
    }
}
