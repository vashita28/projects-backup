package com.coudriet.snapnshare.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.coudriet.snapnshare.android.R;

public class RequestsAdapter extends ArrayAdapter<ParseObject> {
	
	protected Context mContext;
	protected List<ParseObject> mRequests;
	
	public RequestsAdapter(Context context, List<ParseObject> requests) {
		super(context, R.layout.requests_item, requests);
		mContext = context;
		mRequests = requests;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.requests_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView)convertView.findViewById(R.id.requestTime);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		ParseObject request = mRequests.get(position);
		
		holder.iconImageView.setImageResource(R.drawable.ic_request);
		
		Date date = request.getCreatedAt();
		
		String timestamp = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm a", Locale.US).format(date);
		
		holder.nameLabel.setText(request.getString(ParseConstants.KEY_SENDER_NAME));
		
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






