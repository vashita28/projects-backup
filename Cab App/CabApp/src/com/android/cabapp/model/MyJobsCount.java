package com.android.cabapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class MyJobsCount {
	Connection connection;
	Context mContext;

	public MyJobsCount(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
	}

	public int getMyJobsCount() {

		int nJobs = 0;

		connection.connect(Constants.MY_JOBS_COUNT);
		String szResponse = "";
		if (connection.mInputStream != null) {
			try {
				szResponse = connection
						.inputStreamToString(connection.mInputStream);

				JSONObject jObject;// getMyJobsCountResponse:> {"count":0}
				try {
					jObject = new JSONObject(szResponse);
					if (jObject.has("count")) {
						String szCount = jObject.getString("count");
						nJobs = Integer.valueOf(szCount);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return nJobs;

	}

}
