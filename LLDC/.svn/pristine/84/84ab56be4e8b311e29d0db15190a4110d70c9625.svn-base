package co.uk.android.lldc.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.custom.RoundImageView;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.models.EventMediaModel;

import com.android.volley.toolbox.NetworkImageView;

public class EventSocialAdapter extends BaseAdapter {
	private static final String TAG = EventSocialAdapter.class.getSimpleName();

	Context mContext;
	LayoutInflater inflater;
	ArrayList<EventMediaModel> eventMediaList;

	public EventSocialAdapter(Context _context, ArrayList<EventMediaModel> mList) {
		this.mContext = _context;
		this.eventMediaList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventMediaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if (inflater == null)
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
				convertView = inflater.inflate(R.layout.row_social, null);

			holder.ivProfileImage = (RoundImageView) convertView
					.findViewById(R.id.ivProfileImage);
			holder.ivBanner = (NetworkImageView) convertView
					.findViewById(R.id.ivBanner);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvlikesCount = (TextView) convertView
					.findViewById(R.id.tvlikesCount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Setting profile Picture
		try {
			String szProfilePicUrl = eventMediaList.get(position)
					.getUrlProfile();
			Log.e(TAG, "szProfilePicUrl:: " + szProfilePicUrl);
			
			holder.ivProfileImage.setImageUrl(szProfilePicUrl, ImageCacheManager
					.getInstance().getImageLoader());
			
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Setting banner image
		try {
			String szBannerPicUrl = eventMediaList.get(position).getUrlBanner();

			holder.ivBanner.setImageUrl(szBannerPicUrl, ImageCacheManager
					.getInstance().getImageLoader());
			holder.ivBanner.setDefaultImageResId(R.drawable.qeop_placeholder);
			holder.ivBanner.setErrorImageResId(R.drawable.imgnt_placeholder);
			holder.ivBanner.setAdjustViewBounds(true);
			
			Log.e(TAG, "szBannerPicUrl:: " + szBannerPicUrl);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.tvName.setText("@" + eventMediaList.get(position).getName());
		holder.tvlikesCount.setText(String.valueOf(eventMediaList.get(position)
				.getNoOfLikes()));

		return convertView;
	}

	class ViewHolder {
		NetworkImageView ivBanner;
		RoundImageView ivProfileImage; 
		TextView tvName, tvlikesCount;

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
