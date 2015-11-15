package com.android.cabapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleAdapter;

public class CustomPlacesSimpleAdapter extends SimpleAdapter implements
		Filterable {

	private ArrayList<HashMap<String, String>> mAllData, mDataShown=null;
	String[] from;
	int[] to;
	int resource;
	Context context;

	@SuppressWarnings("unchecked")
	public CustomPlacesSimpleAdapter(Context context, List data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		mDataShown = (ArrayList<HashMap<String, String>>) data;
	
		try {
			mAllData = (ArrayList<HashMap<String, String>>) mDataShown.clone();
		} catch (Exception e) {
		
		}
			this.from = from;
		
		this.to = to;
		this.resource = resource;
	}

	public void updateList(List data) {
		mAllData = (ArrayList<HashMap<String, String>>) data;
		new SimpleAdapter(context, mAllData, resource, from, to);
		this.notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		Filter nameFilter = new Filter() {

			@Override
			public String convertResultToString(Object resultValue) {
				return ((HashMap<String, String>) (resultValue)).get(from[0]);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					ArrayList<HashMap<String, String>> tmpAllData = mAllData;
					ArrayList<HashMap<String, String>> tmpDataShown = mDataShown;
					tmpDataShown.clear();
					for (int i = 0; i < tmpAllData.size(); i++) {

						tmpDataShown.add(tmpAllData.get(i));

					}

					FilterResults filterResults = new FilterResults();
					filterResults.values = tmpDataShown;
					filterResults.count = tmpDataShown.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
			}
		};

		return nameFilter;
	}
}
