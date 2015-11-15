package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.ExploreListingFragment;
import co.uk.android.lldc.models.ServerModel;

import com.nostra13.universalimageloader.core.ImageLoader;

public class VenuesAndAttractionsAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<ServerModel> venueList;
	String pageTitle = "";
	int screenWidth = 0, screenHeight = 0;

	public VenuesAndAttractionsAdapter(Context _context,
			ArrayList<ServerModel> venueList, String pagetitle) {
		this.context = _context;
		this.venueList = venueList;
		this.pageTitle = pagetitle;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y / 6;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return venueList.size();
	}

	@Override
	public ServerModel getItem(int position) {
		// TODO Auto-generated method stub
		return venueList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "InflateParams", "DefaultLocale" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if (inflater == null)
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
				if (ExploreListingFragment.bIsTablet) {
					convertView = inflater.inflate(
							R.layout.row_venues_attraction_tablet, null);
				} else {
					convertView = inflater.inflate(
							R.layout.row_venues_attaction, null);
				}
			holder.ivTime = (ImageView) convertView.findViewById(R.id.ivTime);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.tvHeading = (TextView) convertView
					.findViewById(R.id.tvHeading);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			String szImageUrl = venueList.get(position).getLargeImage()
					+ "&width=" + screenWidth + "&height=" + screenHeight
					+ "&crop-to-fit";//
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
			ImageLoader.getInstance().displayImage(szImageUrl, holder.ivImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.tvHeading.setSelected(true);
		holder.tvTime.setSelected(true);
		holder.tvHeading.setText(venueList.get(position).getName()
				.toUpperCase());
		holder.tvTime.setText(venueList.get(position).getActiveDays()
				.toUpperCase());
		holder.tvDesc.setText(venueList.get(position).getShortDesc());
		holder.tvHeading.setTextColor(Color.parseColor(venueList.get(position)
				.getColor()));

		if (pageTitle.equals("Trails")) {
			holder.tvTime.setTextColor(Color.parseColor(venueList.get(position)
					.getColor()));
			holder.ivTime.setVisibility(View.VISIBLE);
		} else {
			holder.ivTime.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView ivImage, ivTime;
		TextView tvHeading, tvTime, tvDesc;

	}

}
