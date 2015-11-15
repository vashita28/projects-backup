package com.android.cabapp.adapter;

import java.util.ArrayList;

import com.android.cabapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MonthYearSpinnerAdapter extends ArrayAdapter<String> {
	Context context;
	ArrayList<String> passengersCount = new ArrayList<String>();

	public MonthYearSpinnerAdapter(Context ctx, int txtViewResourceId,
			ArrayList<String> arr_passengercount) {
		super(ctx, txtViewResourceId, arr_passengercount);
		context = ctx;
		passengersCount = arr_passengercount;
		Log.e("Adapter Passenger Count", "" + passengersCount.size());
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomDropDownView(position, cnvtView, prnt);
	}

	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomInitialView(pos, cnvtView, prnt);
	}

	public View getCustomDropDownView(int position, View convertView,
			ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context
				.getApplicationContext());

		View mySpinner = inflater.inflate(
				R.layout.spinner_selector_after_click, parent, false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.text_main_seen);
		main_text.setText(passengersCount.get(position));

		return mySpinner;

	}

	public View getCustomInitialView(int position, View convertView,
			ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context
				.getApplicationContext());

		View mySpinner = inflater.inflate(R.layout.spinner_selector, parent,
				false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.text_main_seen);
		main_text.setText(passengersCount.get(position));

		return mySpinner;

	}

}
