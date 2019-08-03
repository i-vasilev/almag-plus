package com.elamed.almag.data.Diseases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.elamed.almag.R;

import java.util.ArrayList;
import java.util.List;

public class StringAdapter extends ArrayAdapter<String> implements Filterable {
    private LayoutInflater inflater;
    private List<String> filteredData = null;
    private int layout;
    private List<String> originalData;
    private ItemFilter mFilter = new ItemFilter();

    public StringAdapter(Context context, int layout, List<String> strings) {
        super(context, layout, strings);
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
        this.originalData = strings;
        this.filteredData = strings;
    }

    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view = inflater.inflate(this.layout, parent, false);
        TextView textView = view.findViewById(R.id.textName);
        textView.setText(filteredData.get(position));
        return view;
    }

    public int getCount() {
        return filteredData.size();
    }

    public String getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<String> list = originalData;
            int count = list.size();
            final List<String> nlist = new ArrayList<String>(count);
            String filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
