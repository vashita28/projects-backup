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

public class RecentAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<String> arr_recent = new ArrayList<String>();
	Handler handler;

	public RecentAdapter(Context context, ArrayList<String> list_recent,
			Handler handler) {
		this.mContext = context;
		arr_recent = list_recent;
		this.handler = handler;

	}

	public void setData(ArrayList<String> recentlist) {
		this.arr_recent = recentlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr_recent.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr_recent.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	int pos;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		pos = position;
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
		viewHolder.tv_address.setText(arr_recent.get(position));

		return convertView;

	}

	class ViewHolder {

		TextView tv_address;

		public ViewHolder(final View v) {
			tv_address = (TextView) v.findViewById(R.id.tv_address);
			tv_address.setText("");
		}
	}

}