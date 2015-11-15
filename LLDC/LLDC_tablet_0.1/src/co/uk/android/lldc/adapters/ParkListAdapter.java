package co.uk.android.lldc.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.R;

public class ParkListAdapter extends BaseAdapter {
	Activity activity;
	String[] mStrings;

	int[] res = { R.drawable.overview_icon, R.drawable.parttrain_icon,
			R.drawable.accessibilty_icon, R.drawable.help_icon };
	int layout;

	public ParkListAdapter(Activity activity, int layout, String[] mStrings) {
		this.activity = activity;
		this.mStrings = mStrings;
		this.layout = layout;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent) {

		String text[] = mStrings[position].split("\n");

		LayoutInflater inflater = activity.getLayoutInflater();
		final View row = inflater.inflate(layout, parent, false);
		ImageView image = (ImageView) row.findViewById(R.id.parklist_image);
		image.setImageResource(res[position]);
		TextView label1 = (TextView) row.findViewById(R.id.parklist_header);
		label1.setText(text[0].toString().toUpperCase());
		TextView label2 = (TextView) row.findViewById(R.id.parklist_normal);
		label2.setText(text[1].toString());
		return (row);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mStrings.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return mStrings[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}