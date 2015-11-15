package com.android.cabapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.ChatMessages;

public class MyChatAdpater extends BaseAdapter {

	Context mContext;
	List<ChatMessages> messagesList;

	public MyChatAdpater(Context context, List<ChatMessages> messages) {
		// TODO Auto-generated constructor stub
		mContext = context;
		this.messagesList = messages;
	}

	public void add(String key, String message) {
		// messagesList.put(key, message);
	}

	public void updateList(List<ChatMessages> messages) {
		this.messagesList = messages;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (messagesList != null)
			return messagesList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messagesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_window, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvChatTextDriver = (TextView) convertView
				.findViewById(R.id.chatTextDriver);
		holder.tvChatPassenger = (TextView) convertView
				.findViewById(R.id.chatTextPassenger);
		holder.rlDriver = (RelativeLayout) convertView
				.findViewById(R.id.rlDriver);
		holder.rlPassenger = (RelativeLayout) convertView
				.findViewById(R.id.rlPassenger);

		String source = messagesList.get(position).getSource();
		String message = "";

		if (source.equals("driver")) {
			message = messagesList.get(position).getMessage();
			holder.tvChatTextDriver.setText(message);
			holder.rlDriver.setVisibility(View.VISIBLE);
			holder.rlPassenger.setVisibility(View.GONE);

			// holder.ivUserImage.setBackgroundDrawable(mContext.getResources()
			// .getDrawable(R.drawable.driver));

		} else if (source.equals("passenger")) {
			message = messagesList.get(position).getMessage();
			holder.tvChatPassenger.setText(message);
			holder.rlPassenger.setVisibility(View.VISIBLE);
			holder.rlDriver.setVisibility(View.GONE);

			// holder.ivUserImage.setBackgroundDrawable(mContext.getResources()
			// .getDrawable(R.drawable.user));

		}
		return convertView;
	}

	class ViewHolder {
		RelativeLayout rlPassenger, rlDriver;
		TextView tvChatPassenger, tvChatTextDriver;
	}

}
