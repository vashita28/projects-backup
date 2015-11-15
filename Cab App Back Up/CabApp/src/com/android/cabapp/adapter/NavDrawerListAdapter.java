package com.android.cabapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.NavDrawerItem;
import com.android.cabapp.util.Constants;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	int nJobsCount = 0;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	public void updateItems(int nJobsCount) {
		this.nJobsCount = nJobsCount;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		// if (navDrawerItems.get(position).getCounterVisibility()) {
		// txtCount.setText(navDrawerItems.get(position).getCount());
		// } else {
		// // hide the counter view
		// txtCount.setVisibility(View.GONE);
		// }

		if (position == Constants.MY_JOBS_FRAGMENT && nJobsCount > 0) {
			txtCount.setVisibility(View.VISIBLE);
			txtCount.setText(String.valueOf(nJobsCount));
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

}
