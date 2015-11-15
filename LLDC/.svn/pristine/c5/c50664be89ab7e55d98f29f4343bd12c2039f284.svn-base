package co.uk.android.lldc.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.models.ServerModel;

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<ServerModel> {

	final String TAG = "AutocompleteCustomArrayAdapter.java";

	LayoutInflater inflater;
	Context mContext;
	int layoutResourceId;
	ArrayList<ServerModel> data = new ArrayList<ServerModel>();
	String szSearchText = "";

	public AutocompleteCustomArrayAdapter(Context mContext,
			int layoutResourceId, ArrayList<ServerModel> data,
			String szSearchText) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
		this.szSearchText = szSearchText;
	}

	public ServerModel getItemAtPosition(int position) {
		if (data.size() >= position)
			return data.get(position);
		else
			return null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			/*
			 * The convertView argument is essentially a "ScrapView" as
			 * described is Lucas post
			 * http://lucasr.org/2012/04/05/performance-tips
			 * -for-androids-listview/ It will have a non-null value when
			 * ListView is asking you recycle the row layout. So, when
			 * convertView is not null, you should simply update its contents
			 * instead of inflating a new row layout.
			 */
			if (convertView == null) {
				// inflate the layout
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layoutResourceId, parent, false);
			}

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView textViewItem = (TextView) convertView
					.findViewById(R.id.textViewItem);
			// textViewItem.setText(data.get(position).getName());
			textViewItem.setTag(data.get(position).get_id() + ","
					+ data.get(position).getModelType());

			String filter = szSearchText;
			String itemValue = data.get(position).getName();

			int startPos = itemValue.toLowerCase(Locale.US).indexOf(
					filter.toLowerCase(Locale.US));
			int endPos = startPos + filter.length();

			if (startPos != -1) // This should always be true, just a sanity
								// check
			{
				Spannable spannable = new SpannableString(itemValue);
				ColorStateList blueColor = new ColorStateList(
						new int[][] { new int[] {} },
						new int[] { getContext().getResources().getColor(
								R.color.black_heading) });
				TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null,
						Typeface.BOLD, -1, blueColor, null);

				spannable.setSpan(highlightSpan, startPos, endPos,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				textViewItem.setText(spannable);
			} else
				textViewItem.setText(itemValue);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;

	}

}