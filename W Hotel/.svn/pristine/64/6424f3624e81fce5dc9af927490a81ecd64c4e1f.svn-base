package uk.co.pocketapp.whotel.adapters;

import java.util.ArrayList;

import uk.co.pocketapp.whotel.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FilterAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<String> filterItemsList;
	private static final String TAG = "uk.co.pocketapp.whotel.adapter.FilterAdapter";

	public FilterAdapter(Context context, ArrayList<String> filterItems) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		filterItemsList = filterItems;
	}

	@Override
	public int getCount() {
		return (filterItemsList.size());
	}

	@Override
	public Object getItem(int position) {
		return filterItemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.simplerow_filterdata,
					parent, false);

			holder = new ViewHolder();
			holder.textview_filter = (TextView) convertView
					.findViewById(R.id.textview_item);
			convertView.setTag(holder); // set the View holder

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textview_filter.setText(filterItemsList.get(position));

		return convertView;
	}

	class ViewHolder {
		TextView textview_filter;
	}
}