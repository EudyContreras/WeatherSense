package com.eudycontreras.weathersense.adapters;

/**
 * Created by Eudy on 2/1/2017.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.Filter;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class AdapterSuggestions extends SimpleAdapter {

    private Activity context;
    private LayoutInflater inflater;
    private List<HashMap<String, String>> arrayList;

    public AdapterSuggestions(Activity context, List<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.arrayList = data;
        inflater.from(context);
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        Filter filter = new Filter() {
            String keyword;

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // TODO Auto-generated method stub
                return keyword;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // TODO Auto-generated method stub
                if (results.values != null)
                    notifyDataSetChanged();
                else
                    notifyDataSetInvalidated();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
                return filterResults;
            }

        };
        return filter;

    }
}