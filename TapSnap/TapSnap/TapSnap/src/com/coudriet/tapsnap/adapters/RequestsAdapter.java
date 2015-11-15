package com.coudriet.tapsnap.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coudriet.tapsnap.android.MainApplicationStartup;
import com.coudriet.tapsnap.android.ParseConstants;
import com.coudriet.tapsnap.android.R;
import com.coudriet.tapsnap.utility.ImageLoaderCustom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class RequestsAdapter extends ArrayAdapter<ParseObject> {

	public static final String TAG = RequestsAdapter.class.getSimpleName();

	protected Context mContext;
	protected List<ParseObject> mRequests;

	ImageLoader imageloader;
	DisplayImageOptions options;
	String pictureURL = null;

	ImageLoaderCustom imageLoaderCustom;

	public RequestsAdapter(Context context, List<ParseObject> requests) {
		super(context, R.layout.requests_item, requests);
		mContext = context;
		mRequests = requests;
		imageLoaderCustom = new ImageLoaderCustom(context);
		imageloader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.requests_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView) convertView
					.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView) convertView
					.findViewById(R.id.requestTime);

			holder.nameLabel
					.setTypeface(MainApplicationStartup.font_ProximaNovaRegular);
			holder.nameLabel2
					.setTypeface(MainApplicationStartup.font_ProximaNovaBold);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ParseObject request = mRequests.get(position);

		ParseFile file = (ParseFile) mRequests.get(position).get(
				ParseConstants.KEY_PROFILE_PICTURE);
		pictureURL = (String) mRequests.get(position).get(
				ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE);

		if (file != null) {
			try {
				imageLoaderCustom.DisplayImage(file.getUrl(),
						holder.iconImageView);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (pictureURL != null) {
			imageloader.displayImage(pictureURL, holder.iconImageView, options);
		} else {
			holder.iconImageView.setImageResource(R.drawable.redicon);
		}

		Date date = request.getCreatedAt();

		String timestamp = new SimpleDateFormat("hh:mma   dd MMMM",// EEE MMM
																	// dd, yyyy
																	// hh:mm a
				Locale.US).format(date).toUpperCase();

		holder.nameLabel.setText(request
				.getString(ParseConstants.KEY_SENDER_NAME));

		System.out.println(timestamp);

		holder.nameLabel2.setText(timestamp);

		return convertView;
	}

	private static class ViewHolder {
		ImageView iconImageView;
		TextView nameLabel;
		TextView nameLabel2;
	}

	public void refill(List<ParseObject> requests) {

		// System.out.println("Request Refill");

		mRequests.clear();
		mRequests.addAll(requests);
		notifyDataSetChanged();
	}

	public void clear(List<ParseObject> requests) {

		// System.out.println("Request Clear");

		mRequests.clear();
		notifyDataSetChanged();
	}
}
