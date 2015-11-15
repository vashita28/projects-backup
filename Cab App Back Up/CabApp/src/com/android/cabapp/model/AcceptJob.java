package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class AcceptJob {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	public String errorMessage;

	public AcceptJob(String jobID, Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		connection.jobId = jobID;
		mContext = context;
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public boolean isJobAccepted() {
		if (Util.getLocation(mContext) != null) {
			nameValuePairs.add(new BasicNameValuePair(
					"data[acceptedAtLatitude]", String.valueOf(Util
							.getLocation(mContext).getLatitude())));
			nameValuePairs.add(new BasicNameValuePair(
					"data[acceptedAtLongitude]", String.valueOf(Util
							.getLocation(mContext).getLongitude())));
		}
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.JOB_ACCEPTED);

		if (connection.mInputStream != null)
			try {
				{
					// Log.v("AcceptJob",
					// "Response is :: "
					// + connection.inputStreamToString(
					// connection.mInputStream).toString());

					JSONObject jObject = new JSONObject(connection
							.inputStreamToString(connection.mInputStream)
							.toString());
					if (jObject.has("success")
							&& jObject.getString("success").equals("true"))
						return true;

					if (jObject.has("errors")) {
						errorMessage = jObject.getJSONArray("errors")
								.getJSONObject(0).getString("message");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return false;

	}
}
