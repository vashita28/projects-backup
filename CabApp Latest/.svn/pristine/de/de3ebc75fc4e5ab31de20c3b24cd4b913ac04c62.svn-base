package com.android.cabapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.activity.SignUp_AboutYou_Activity;
import com.android.cabapp.datastruct.json.City;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Util;

public class CityBaseAdapter extends BaseAdapter {
	private static final String TAG = CityBaseAdapter.class.getSimpleName();

	Context context;
	City city;

	public CityBaseAdapter(Context context, City cities) {
		this.context = context;
		this.city = cities;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return city.getCities().size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return city.getCities().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_spinner_item, null);
			holder = new ViewHolder();
			holder.txtCityName = (TextView) convertView
					.findViewById(R.id.tvContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Grey out if coming from My account
		if (SignUp_AboutYou_Activity.isComingFromEditMyAccount) {
			holder.txtCityName.setTextColor(context.getResources().getColor(
					R.color.divider_color));
		}

		if (position == 0) {
			holder.txtCityName.setText("Please select a city");
			SignUp_AboutYou_Activity.cityName = "Please select a city";
		} else {
			position = position - 1;

			String cityName = city.getCities().get(position).getCityname();
			holder.txtCityName.setText(cityName.substring(0, 1).toUpperCase()
					+ cityName.substring(1));

			SignUp_AboutYou_Activity.cityName = cityName;

			SignUp_AboutYou_Activity.cityCode = city.getCities().get(position)
					.getId();

			if (city.getCities().get(position).getMaximumPassengersPerCab() != null
					&& city.getCities().get(position)
							.getMinimumPassengersPerCab() != null) {
				AppValues.maxNoOfPassenger = Integer.valueOf(city.getCities()
						.get(position).getMaximumPassengersPerCab());
				AppValues.mimNoOFPassenger = Integer.valueOf(city.getCities()
						.get(position).getMinimumPassengersPerCab());
			}

			if (city.getCities().get(position).getId().equals("1")) {
				Util.setCitySelectedLondon(context, true);
			} else
				Util.setCitySelectedLondon(context, false);

			String isWheelChairAccess = "";
			if (city.getCities().get(position).getDefaultWheelchairAccess() != null) {
				isWheelChairAccess = city.getCities().get(position)
						.getDefaultWheelchairAccess();
			}

			// For wheel chair
			if (!isWheelChairAccess.isEmpty()) {
				if (isWheelChairAccess.equals("true")) {
					AppValues.isWheelChairAccess = true;
				} else {
					AppValues.isWheelChairAccess = false;
				}
			} else {
				AppValues.isWheelChairAccess = false;
			}

			SignUp_AboutYou_Activity.szDriverDocument = city.getCities()
					.get(position).getDriverDocuments();
			SignUp_AboutYou_Activity.szDriverDocumentsList = city.getCities()
					.get(position).getDriverDocumentsList();

		}

		return convertView;
	}

	private class ViewHolder {
		TextView txtCityName;
	}
}
