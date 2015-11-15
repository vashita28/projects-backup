package co.uk.pocketapp.gmd.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.ui.Data_Model_Archive_Reports;

public class Custom_Adapter_Archive_Reports extends BaseAdapter {
	ArrayList<Data_Model_Archive_Reports> listArray;

	public Custom_Adapter_Archive_Reports(
			ArrayList<Data_Model_Archive_Reports> listArray) {
		this.listArray = listArray;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater_Site_Condition = LayoutInflater.from(parent
					.getContext());
			convertView = inflater_Site_Condition.inflate(
					R.layout.archive_reports_simplerow, parent, false);
		}

		final Data_Model_Archive_Reports dataModel = listArray.get(position);

		TextView report_Title = (TextView) convertView
				.findViewById(R.id.report_title_TextView);
		report_Title.setText(dataModel.getReport_Title());

		TextView dateNtime = (TextView) convertView
				.findViewById(R.id.dateNtime_TextView);
		dateNtime.setText(dataModel.getDate_Time().toLowerCase());

		return convertView;
	}
}
