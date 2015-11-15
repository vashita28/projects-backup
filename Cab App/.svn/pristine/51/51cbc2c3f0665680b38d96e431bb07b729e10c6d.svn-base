package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.datastruct.json.DriverDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.google.gson.Gson;

public class DriverAccountDetails {
	private static final String TAG = DriverAccountDetails.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;

	public DriverAccountDetails(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();

	}

	public DriverDetails retriveAccountDetails(Context context) {

		connection.connect(Constants.RETRIVE_DRIVER_ACCOUNT_DETAILS);
		DriverDetails driverDetailsResponse = null;

		Gson gson = new Gson();

		if (connection.mInputStream != null) {

			String szJSONResponse = "";
			try {
				szJSONResponse = connection.inputStreamToString(connection.mInputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Reader reader = new InputStreamReader(connection.mInputStream);
			// Util.setDriverDetails(mContext, szJSONResponse);
			driverDetailsResponse = gson.fromJson(szJSONResponse, DriverDetails.class);
			if (driverDetailsResponse != null)
				AppValues.driverDetails = driverDetailsResponse;
			Util.setNumberOfCredits(mContext, driverDetailsResponse.getCredits());
			Util.setEmailOrUserName(context, driverDetailsResponse.getEmail());

			//if (Constants.isDebug)
				//Log.e(TAG, "AccountDetails:Credits: " + driverDetailsResponse.getCredits());

			if (MainActivity.creditsRefreshHandler != null)
				MainActivity.creditsRefreshHandler.sendEmptyMessage(0);
		}
		return driverDetailsResponse;
	}
}
