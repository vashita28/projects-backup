package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.models.ServerModel;

import com.android.volley.toolbox.NetworkImageView;

public class VenuesAndAttractionsAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<ServerModel> venueList;
	String pageTitle = "";

	public VenuesAndAttractionsAdapter(Context _context,
			ArrayList<ServerModel> venueList, String pagetitle) {
		this.context = _context;
		this.venueList = venueList;
		this.pageTitle = pagetitle;
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
				convertView = inflater.inflate(R.layout.row_venues_attaction,
						null);
			holder.ivTime = (ImageView) convertView.findViewById(R.id.ivTime);
			holder.ivImage = (NetworkImageView) convertView.findViewById(R.id.ivImage);
			holder.tvHeading = (TextView) convertView
					.findViewById(R.id.tvHeading);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			int height = LLDCApplication.dpToPx(120);
			String szImageUrl = venueList.get(position).getLargeImage() 
					+ "&width=" + LLDCApplication.screenWidth + "&height=" + height + "&crop-to-fit";//
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
			holder.ivImage.setImageUrl(szImageUrl, ImageCacheManager
					.getInstance().getImageLoader());
			holder.ivImage.setDefaultImageResId(R.drawable.qeop_placeholder);
			holder.ivImage.setErrorImageResId(R.drawable.imgnt_placeholder);
			holder.ivImage.setAdjustViewBounds(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.tvHeading.setSelected(true);
		holder.tvTime.setSelected(true);
		holder.tvHeading.setText(venueList.get(position).getName()
				.toUpperCase());
		
		holder.tvDesc.setText(Html.fromHtml(venueList.get(position).getShortDesc()));
		holder.tvHeading.setTextColor(Color.parseColor(venueList.get(position)
				.getColor()));

		if (pageTitle.equals("Trails")) {
			holder.tvTime.setTextColor(Color.parseColor(venueList.get(position)
					.getColor()));
			holder.ivTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText(venueList.get(position).getActiveDays());
		} else {
			holder.tvTime.setText(venueList.get(position).getActiveDays()
					.toUpperCase());
			holder.ivTime.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView ivTime;
		NetworkImageView ivImage;
		TextView tvHeading, tvTime, tvDesc;
//		RelativeLayout ivholder;
//		View topbar;
	}

}
