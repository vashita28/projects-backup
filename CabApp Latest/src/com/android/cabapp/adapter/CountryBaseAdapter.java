package com.android.cabapp.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.activity.SignUp_AboutYou_Activity;
import com.android.cabapp.datastruct.json.CountryList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CountryBaseAdapter extends BaseAdapter {

	Context context;
	List<CountryList> countries;
	ImageLoader imageloader;
	DisplayImageOptions options;

	public CountryBaseAdapter(Context context, List<CountryList> countries) {
		this.context = context;
		this.countries = countries;
		imageloader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return countries.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return countries.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// return countries.indexOf(getItem(position));
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_countryspinner_item,
					null);
			holder = new ViewHolder();
			holder.txtCountryCode = (TextView) convertView
					.findViewById(R.id.textviewCountryCode);
			holder.ivCountryFlag = (ImageView) convertView
					.findViewById(R.id.ivCountryFlag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Grey out if coming from My accouny
		if (SignUp_AboutYou_Activity.isComingFromEditMyAccount) {
			holder.txtCountryCode.setTextColor(context.getResources().getColor(
					R.color.divider_color));
		}

		if (position == 0) {
			holder.txtCountryCode.setText("");
			imageloader.displayImage(null, holder.ivCountryFlag, options);
			SignUp_AboutYou_Activity.countryCode = "";
		} else {
			position = position - 1;

			holder.txtCountryCode.setText("+ "
					+ countries.get(position).getDialingCode());
			imageloader.displayImage(countries.get(position).getImage(),
					holder.ivCountryFlag, options);

			SignUp_AboutYou_Activity.countryCode = countries.get(position)
					.getDialingCode();
		}

		return convertView;
	}

	private class ViewHolder {
		TextView txtCountryName, txtCountryCode;
		ImageView ivCountryFlag;
	}
}
