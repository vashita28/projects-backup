package com.android.cabapp.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.util.Constants;

public class InboxAdapter extends BaseAdapter {
	private static final String TAG = InboxAdapter.class.getSimpleName();

	private Context mContext;
	List<Job> jobsList;

	public InboxAdapter(Context context, List<Job> jobsList) {
		// TODO Auto-generated constructor stub
		mContext = context;
		this.jobsList = jobsList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (jobsList != null)
			return jobsList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jobsList.get(position);
	}

	public void updateList(List<Job> jobsList) {
		this.jobsList = jobsList;
		notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.inbox_row, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvLocation = (TextView) convertView
				.findViewById(R.id.tvLocation);
		holder.tvPassenger = (TextView) convertView
				.findViewById(R.id.textPassengerName);
		holder.tvTimeDate = (TextView) convertView
				.findViewById(R.id.tvTimeDate);
		holder.tvChat = (TextView) convertView.findViewById(R.id.tvChat);

		String passengerName = jobsList.get(position).getName();
		String szFirstName = passengerName.substring(0,
				passengerName.indexOf(" "));
		String szLastName = passengerName.substring(passengerName.indexOf(" "));
		holder.tvPassenger.setText(szFirstName.substring(0, 1) + "."
				+ szLastName);

		String szDOPinCode = jobsList.get(position).getDropLocation()
				.getPostCode();
		String szPUPinCode = jobsList.get(position).getPickupLocation()
				.getPostCode();

		if (szPUPinCode == null || szPUPinCode.isEmpty()
				|| szPUPinCode.equals(""))
			szPUPinCode = "AD";// As directed

		if (szDOPinCode == null || szDOPinCode.equals("")
				|| szDOPinCode.isEmpty())
			szDOPinCode = "AD";// As directed

		String txtLocation = "<font color=#f5f5f5>" + szPUPinCode
				+ "</font><font color=#fd6f01> > </font> <font color=#f5f5f5>"
				+ szDOPinCode + "</font>";
		holder.tvLocation.setText(Html.fromHtml(txtLocation));

		String lastMessage = "";
		String dateTime = jobsList.get(position).getPickupDateTime();
		SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("EEE dd MMM HH:mm"); // HH:mm
																			// dd/MM/yy
		Date convertedDate = new Date();

		if (jobsList != null) {
			String szMessagesArray = jobsList.get(position).getMessages()
					.toString();

			JSONArray jMessagesArray;
			try {
				jMessagesArray = new JSONArray(szMessagesArray);
				int lengthJsonArr = jMessagesArray.length();
				// for (int i = 0; i < lengthJsonArr; i++) {
				if (lengthJsonArr > 0) {
					JSONObject jsonChildNode = jMessagesArray
							.getJSONObject(lengthJsonArr - 1);
					String source = jsonChildNode.getString(Constants.SOURCE)
							.toString();
					lastMessage = jsonChildNode.getString(Constants.MESSAGE)
							.toString();
					dateTime = jsonChildNode.getString(Constants.MESSAGETIME)
							.toString();
					// messagesList.add(new ChatMessages(source, messageText));
					// }
				} else {
					Log.d(TAG, "No messages found");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		String newDate = "";

		if (lastMessage.equals("")) {
			lastMessage = "Tap here to start conversation";
			newDate = "NO MESSAGES";

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvTimeDate
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			holder.tvTimeDate.setLayoutParams(params);
			holder.tvChat.setGravity(Gravity.CENTER_HORIZONTAL);
		} else {

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvTimeDate
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			holder.tvTimeDate.setLayoutParams(params);
			holder.tvChat.setGravity(Gravity.LEFT);

			try {
				convertedDate = input.parse(dateTime);
				newDate = output.format(convertedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		holder.tvChat.setText(lastMessage);
		holder.tvTimeDate.setText(newDate);

		return convertView;
	}

	class ViewHolder {
		TextView tvLocation, tvPassenger, tvTimeDate, tvChat;
	}

}
