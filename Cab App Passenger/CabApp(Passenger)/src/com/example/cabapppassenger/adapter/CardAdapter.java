package com.example.cabapppassenger.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cabapppassenger.R;

public class CardAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<String> arr_passenger_card = new ArrayList<String>();
	Handler handler;

	public CardAdapter(Context context, ArrayList<String> list_card,
			Handler handler) {
		this.mContext = context;
		arr_passenger_card = list_card;
		this.handler = handler;
		Log.e("Passeneger Card Size in Adapte", "" + arr_passenger_card.size());

	}

	public void setData(ArrayList<String> recentlist) {
		this.arr_passenger_card = recentlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr_passenger_card.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr_passenger_card.get(position);
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
		viewHolder.tv_address.setText(arr_passenger_card.get(position));
		// if (arr_passenger_card.size() > 1)
		// viewHolder.btn_spinneritems.setVisibility(View.VISIBLE);

		return convertView;

	}

	class ViewHolder {

		TextView tv_address;

		// Button btn_spinneritems;

		public ViewHolder(final View v) {
			tv_address = (TextView) v.findViewById(R.id.tv_address);
			tv_address.setText("");
			// btn_spinneritems = (Button)
			// v.findViewById(R.id.btn_spinneritems);
		}
	}

}