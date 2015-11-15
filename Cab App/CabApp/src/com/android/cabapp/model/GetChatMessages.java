package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.google.gson.Gson;

public class GetChatMessages {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;

	public GetChatMessages(String jobID, Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		connection.jobId = jobID;
	}

	public Job getJobByJobID() {

		if (Util.getLocation(mContext) != null) {
			nameValuePairs.add(new BasicNameValuePair("data[latitude]", String
					.valueOf(Util.getLocation(mContext).getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("data[longitude]", String
					.valueOf(Util.getLocation(mContext).getLongitude())));
		}
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.UPDATE_POSITION_AND_GET_JOB);

		String message = "";
		Job job = null;

		if (connection.mInputStream != null) {
			try {
				{
					message = connection.inputStreamToString(
							connection.mInputStream).toString();
					// Log.e("GetChatMessages", "getJobByJobID " + message);
					// JSONObject jObject = new JSONObject(message);

					if (connection.mInputStream != null) {
						Gson gson = new Gson();
						// Reader reader = new InputStreamReader(
						// connection.mInputStream);
						job = gson.fromJson(message, Job.class);
					}

					// if (jObject.has("error")) {
					// message = jObject.getString("message");
					// return message;
					// }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return job;

	}
}
