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

public class MessageAdapter extends ArrayAdapter<ParseObject> {
	
	protected Context mContext;
	protected List<ParseObject> mMessages;
	
	public MessageAdapter(Context context, List<ParseObject> messages) {
		super(context, R.layout.message_item, messages);
		mContext = context;
		mMessages = messages;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView)convertView.findViewById(R.id.messageTime);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		ParseObject message = mMessages.get(position);
		
		if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
			holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
		}
		else {
			holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
		}
		
		Date date = message.getCreatedAt();
		
		String timestamp = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm a", Locale.US).format(date);
		
		holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
		
		holder.nameLabel2.setText(timestamp);
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView iconImageView;
		TextView nameLabel;
		TextView nameLabel2;
	}
	
	public void refill(List<ParseObject> messages) {
		
		// System.out.println("Messages Refill");
		
		mMessages.clear();
		mMessages.addAll(messages);
		notifyDataSetChanged();
	}
}






