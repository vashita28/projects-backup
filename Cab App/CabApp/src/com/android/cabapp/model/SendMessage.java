package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.R;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class SendMessage {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String message;
	boolean isSoundMessage = false;

	public SendMessage(String jobID, Context context, String message,
			boolean isSoundSend) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		connection.jobId = jobID;
		this.message = message;
		this.isSoundMessage = isSoundSend;
	}

	public String sendMessages() {

		nameValuePairs.add(new BasicNameValuePair("data[message]", message));

		if (Util.getLocation(mContext) != null) {
			nameValuePairs.add(new BasicNameValuePair("data[latitude]", String
					.valueOf(Util.getLocation(mContext).getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("data[longitude]", String
					.valueOf(Util.getLocation(mContext).getLongitude())));
		} else {
			Util.showAlertDialog(mContext, "Unable to find current location");
		}

		if (message.equals(mContext.getResources().getString(
				R.string.arrived_at_pick_up))) {
			nameValuePairs.add(new BasicNameValuePair("data[isOutside]", "Y"));
		} else
			nameValuePairs.add(new BasicNameValuePair("data[isOutside]", "N"));

		if (isSoundMessage) {
			nameValuePairs.add(new BasicNameValuePair("data[sound]",
					"cabApp_horn.wav"));// soundfile.wav
		} else {
			nameValuePairs.add(new BasicNameValuePair("data[sound]", ""));
		}

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.SEND_MESSAGE);
		if (Constants.isDebug)
			Log.e("SendMessage",
					"nameValuePairs:: " + nameValuePairs.toString());

		String sendmessageResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					sendmessageResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					Log.e("SendMessage", "SendMessage " + sendmessageResponse);
					JSONObject jObject = new JSONObject(sendmessageResponse);

					if (jObject.has("error")) {
						sendmessageResponse = jObject.getString("message");
						return sendmessageResponse;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendmessageResponse;

	}
}
