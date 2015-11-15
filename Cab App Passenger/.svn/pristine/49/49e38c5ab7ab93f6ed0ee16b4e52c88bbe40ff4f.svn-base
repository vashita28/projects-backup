package com.example.cabapppassenger.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cabapppassenger.R;

public class CityListAdpater extends BaseAdapter {

	Context mContext;
	ArrayList<String> arr_citylist = new ArrayList<String>();
	Handler handler;
	ViewHolder viewHolder;

	public CityListAdpater(Context context, ArrayList<String> list_citylist,
			Handler handler) {
		this.mContext = context;
		arr_citylist = list_citylist;
		this.handler = handler;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr_citylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr_citylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favourites_recent_row,
					parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (arr_citylist.get(position) != null)
			viewHolder.tv_address.setText(arr_citylist.get(position));

		return convertView;

	}

	public class ViewHolder {

		TextView tv_address;

		public ViewHolder(final View v) {
			tv_address = (TextView) v.findViewById(R.id.tv_address);
			tv_address.setText("");

		}
	}

}
