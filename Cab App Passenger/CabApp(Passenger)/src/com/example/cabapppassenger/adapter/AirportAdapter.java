package com.example.cabapppassenger.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.datastruct.json.Airport;
import com.example.cabapppassenger.fragments.Hail_NowFragment;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.model.PreBookParcelable;

public class AirportAdapter extends BaseAdapter {

	Context mContext;
	List<Airport> arr_airport = new ArrayList<Airport>();
	PreBookParcelable pbParcelable;
	Hail_NowFragment hailNowFragment;

	public AirportAdapter(Context context, Fragment fragment,
			List<Airport> result, PreBookParcelable pbParcelable) {
		this.mContext = context;
		arr_airport = result;
		this.pbParcelable = pbParcelable;
		hailNowFragment = (Hail_NowFragment) fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr_airport.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr_airport.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	int pos;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		pos = position;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.airport_row, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_airport.setText(arr_airport.get(position)
				.getAirportName());
		viewHolder.tv_price.setText(arr_airport.get(position)
				.getFixedPriceCurrency()
				+ arr_airport.get(position).getFixedPrice());

		viewHolder.tv_airport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("Clicked", viewHolder.tv_airport.getText().toString());
				if (viewHolder.tv_airport.getText().toString()
						.equals(arr_airport.get(position).getAirportName())) {
					pbParcelable.getDefaultDropOffLocation().setAddress1(
							arr_airport.get(position).getAirportName());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							arr_airport.get(position).getPostcode());
					pbParcelable.getDefaultDropOffLocation().setLatitude(
							Double.valueOf(arr_airport.get(position)
									.getLatitude()));
					pbParcelable.getDefaultDropOffLocation().setLongitude(
							Double.valueOf(arr_airport.get(position)
									.getLongitude()));
					pbParcelable.seteDropOffSelected(EnumLocations.DEFAULT);
					pbParcelable
							.setePickUpSelected(EnumLocations.PRESENTLOCATION);
					if (!arr_airport.get(position).getFixedPrice().equals(""))
						pbParcelable.setFixedPrice(Double.valueOf(arr_airport
								.get(position).getFixedPrice()));
					if (pbParcelable.getFixedPrice() != 0)

					{
						pbParcelable.setIsFixedPrice(true);
						pbParcelable.setFixedPriceCurrency(arr_airport.get(
								position).getFixedPriceCurrency());
					}

					hailNowFragment.prebooklistenermethod(true);
					Log.e(getClass().getSimpleName(),
							"lat "
									+ arr_airport.get(position).getLatitude()
									+ " long "
									+ arr_airport.get(position).getLongitude()
									+ " pc "
									+ arr_airport.get(position).getPostcode()
									+ " fp "
									+ arr_airport.get(position).getFixedPrice()
									+ " add"
									+ arr_airport.get(position).getAddress1()
									+ " name "
									+ arr_airport.get(position)
											.getAirportName()

									+ " fpcs "
									+ arr_airport.get(position)
											.getFixedPriceCurrency());
				}
			}
		});
		return convertView;

	}

	class ViewHolder {

		TextView tv_airport, tv_price;

		public ViewHolder(final View v) {
			tv_airport = (TextView) v.findViewById(R.id.tv_airport);
			tv_airport.setText("");
			tv_price = (TextView) v.findViewById(R.id.tv_price);
			tv_price.setText("");
		}
	}

}
