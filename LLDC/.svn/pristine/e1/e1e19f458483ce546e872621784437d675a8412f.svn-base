package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.LLDCApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

public class RecommendationListingAdapter extends BaseAdapter {
	private static final String TAG = RecommendationListingAdapter.class
			.getSimpleName();
	Activity ctx;
	ArrayList<ServerModel> rec_list;
	LayoutInflater inflater;

	public RecommendationListingAdapter(Activity ctx, int layout_id,
			ArrayList<ServerModel> rec_list) {
		this.ctx = ctx;
		this.rec_list = rec_list;
	}

	@SuppressLint({ "ViewHolder", "InflateParams", "DefaultLocale" })
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			if (inflater == null)
				inflater = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null)
				convertView = inflater.inflate(
						R.layout.recommendationlist_row_tablet, null);

			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.recommendationlist_image);
			holder.eventName = (TextView) convertView
					.findViewById(R.id.recommendationlist_eventname);
			holder.venueDesc = (TextView) convertView
					.findViewById(R.id.recommendationlist_Desc);
			holder.activeDays = (TextView) convertView
					.findViewById(R.id.recommendationlist_date);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		try {
			int screenWidth = (int) (LLDCApplication.screenWidth / 5);
			String szImageUrl = "";
			szImageUrl = rec_list.get(position).getThumbImage() + "&width="
					+ screenWidth + "&height=" + screenWidth + "&crop-to-fit";
			// Log.e(TAG, "szImageUrl::> " + szImageUrl);
			
			holder.imageView.getLayoutParams().height = screenWidth;
			holder.imageView.getLayoutParams().width = screenWidth;
			ImageLoader.getInstance().displayImage(szImageUrl,
					holder.imageView);

			// holder.imageView.setImageUrl(szImageUrl, ImageCacheManager
			// .getInstance().getImageLoader());
			// holder.imageView.setDefaultImageResId(R.drawable.qeop_placeholder);
			// holder.imageView.setErrorImageResId(R.drawable.imgnt_placeholder);
			// holder.imageView.setAdjustViewBounds(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.eventName.setText(rec_list.get(position).getName().toString()
				.toUpperCase());
		holder.eventName.setSelected(true);
		holder.venueDesc.setText(Html.fromHtml(rec_list.get(position)
				.getShortDesc().toString()));
		holder.activeDays.setText(rec_list.get(position).getActiveDays()
				.toString().toUpperCase());
		holder.eventName.setTextColor(Color.parseColor(rec_list.get(position)
				.getColor()));

		return convertView;

	}

	class ViewHolder {
		TextView eventName, venueDesc, activeDays;
		ImageView imageView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rec_list.size();
	}

	@Override
	public ServerModel getItem(int arg0) {
		// TODO Auto-generated method stub
		return rec_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}