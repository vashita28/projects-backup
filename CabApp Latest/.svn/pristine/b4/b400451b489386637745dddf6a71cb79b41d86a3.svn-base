package com.android.cabapp.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.cabapp.R;
import com.android.cabapp.activity.LogInActivity;
import com.android.cabapp.activity.SuburbanSectorsActivity;
import com.android.cabapp.datastruct.json.SectorList;
import com.android.cabapp.util.AppValues;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomGridAdapter extends BaseAdapter {
	private static final String TAG = CustomGridAdapter.class.getSimpleName();

	private Context mContext;
	private final ArrayList<String> noOfSectors;
	List<String> listSelectedBadges = new ArrayList<String>();

	public CustomGridAdapter(Context c, ArrayList<String> listNoOfSectors) {
		mContext = c;
		this.noOfSectors = listNoOfSectors;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noOfSectors.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.grid_single, null);
			TextView textView = (TextView) grid.findViewById(R.id.grid_text);
			RelativeLayout rlCabImage = (RelativeLayout) grid
					.findViewById(R.id.rlCabImage);
			textView.setText(noOfSectors.get(position));
			if (position == (noOfSectors.size() - 1)) {
				textView.setVisibility(View.GONE);
				rlCabImage.setVisibility(View.VISIBLE);
			}

			// In case coming from MyAccount:
			if (AppValues.driverDetails != null
					&& AppValues.driverDetails.getSector() != null) {
				String sSelectedBadges = AppValues.driverDetails.getSector()
						.toString();
				String[] items = sSelectedBadges.split(",");
				for (int i = 0; i < items.length; i++) {
					if (!listSelectedBadges.contains(items[i].trim())) {
						listSelectedBadges.add(items[i].trim());
					}
				}
				for (int i = 0; i < listSelectedBadges.size(); i++) {
					if (listSelectedBadges.get(i).equals(
							noOfSectors.get(position))) {
						textView.setTextColor(mContext.getResources().getColor(
								R.color.textview_selected));
					}
				}

			}

			rlCabImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							SuburbanSectorsActivity.class);
					mContext.startActivity(intent);
				}
			});
		} else {
			grid = (View) convertView;
		}
		return grid;
	}
}
