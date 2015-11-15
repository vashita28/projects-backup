package com.android.cabapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.SectorList;

public class SuburbanAreasAdapter extends BaseAdapter {
	private static final String TAG = CustomGridAdapter.class.getSimpleName();

	private Context mContext;
	private final List<SectorList> SectorList;

	public SuburbanAreasAdapter(Context c, List<SectorList> listSectors) {
		mContext = c;
		this.SectorList = listSectors;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return SectorList.size();
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
		View sectorView;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			sectorView = new View(mContext);
			sectorView = inflater.inflate(R.layout.suburban_areas_row, null);
			TextView textNumber = (TextView) sectorView
					.findViewById(R.id.tvNumber);
			TextView tvSectorData = (TextView) sectorView
					.findViewById(R.id.tvSectorData);
			textNumber.setText(SectorList.get(position).getSectorNumber());
			tvSectorData.setText(SectorList.get(position).getSectorName());

		} else {
			sectorView = (View) convertView;
		}
		return sectorView;
	}

}
