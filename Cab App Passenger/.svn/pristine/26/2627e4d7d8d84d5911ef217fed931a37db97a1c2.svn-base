package com.example.cabapppassenger.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.util.Constant;

public class CountryCodeAdapter extends ArrayAdapter<String> {

	Context mContext;

	public CountryCodeAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
				false);
		TextView main_text = (TextView) mySpinner
				.findViewById(R.id.tv_countrycode);
		main_text.setText(Constant.arr_dialingcode.get(position));
		ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.iv_flag);
		String country_code = Constant.arr_dialingcode.get(position);
		if (country_code != null && left_icon != null) {

			Bitmap bitmap = Constant.hashmap_countrycode.get(country_code);
			if (bitmap != null) {
				left_icon.setImageBitmap(bitmap);
			}
		} else
			left_icon.setImageResource(R.drawable.thumbnail);

		// notifyDataSetChanged();
		return mySpinner;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return getCustomView(position, convertView, parent);
	}

}
