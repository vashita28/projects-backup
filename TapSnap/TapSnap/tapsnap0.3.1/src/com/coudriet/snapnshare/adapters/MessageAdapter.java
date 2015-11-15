package com.coudriet.snapnshare.adapters;

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

import com.coudriet.snapnshare.android.MainApplicationStartup;
import com.coudriet.snapnshare.android.ParseConstants;
import com.coudriet.snapnshare.android.R;
import com.parse.ParseObject;

public class MessageAdapter extends ArrayAdapter<ParseObject> {

	public static final String TAG = MessageAdapter.class.getSimpleName();

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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.message_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView) convertView
					.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView) convertView
					.findViewById(R.id.messageTime);

			holder.nameLabel
					.setTypeface(MainApplicationStartup.font_ProximaNovaRegular);
			holder.nameLabel2
					.setTypeface(MainApplicationStartup.font_ProximaNovaBold);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ParseObject message = mMessages.get(position);

		if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(
				ParseConstants.TYPE_IMAGE)) {
			holder.iconImageView.setImageResource(R.drawable.camera);
		} else {
			holder.iconImageView.setImageResource(R.drawable.video);
		}

		Date date = message.getCreatedAt();

		String timestamp = new SimpleDateFormat("hh:mma   dd MMMM",// EEE MMM
																	// dd, yyyy
																	// hh:mm a
				Locale.US).format(date).toUpperCase();

		holder.nameLabel.setText(message
				.getString(ParseConstants.KEY_SENDER_NAME));

		holder.nameLabel2.setText(timestamp);

		holder.nameLabel
				.setTypeface(MainApplicationStartup.font_ProximaNovaRegular);
		holder.nameLabel2
				.setTypeface(MainApplicationStartup.font_ProximaNovaBold);

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
