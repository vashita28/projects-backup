package co.uk.peeki.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;

public class ListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public ListAdapter(Context context, String[] values) {
		super(context, R.layout.listview_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listview_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tvlistitem);

		textView.setTypeface(Photo_Application.font_Regular);
		textView.setText(values[position]);

		View viewDividerBottom = (View) rowView
				.findViewById(R.id.viewDividerBottom);
		if (position + 1 == values.length) {
			viewDividerBottom.setVisibility(View.VISIBLE);
		} else {
			viewDividerBottom.setVisibility(View.GONE);
		}

		return rowView;
	}
}
