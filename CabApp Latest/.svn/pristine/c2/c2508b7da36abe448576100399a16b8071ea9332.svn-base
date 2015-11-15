package com.android.cabapp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.location.Location;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class UpdateLocation {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;

	public UpdateLocation(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public void updateCurrentLocation(String paymentType, String szJobID,
			Location location) {

		if (location == null)
			if (Util.getLocation(mContext) != null) {
				nameValuePairs
						.add(new BasicNameValuePair("data[latitude]", String
								.valueOf(Util.getLocation(mContext)
										.getLatitude())));
				nameValuePairs.add(new BasicNameValuePair("data[longitude]",
						String.valueOf(Util.getLocation(mContext)
								.getLongitude())));
			} else {
				try {
					nameValuePairs.add(new BasicNameValuePair("data[latitude]",
							String.valueOf(location.getLatitude())));
					nameValuePairs.add(new BasicNameValuePair(
							"data[longitude]", String.valueOf(location
									.getLongitude())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		nameValuePairs.add(new BasicNameValuePair("data[paymentType]",
				paymentType));
		connection.prepareConnection(nameValuePairs);
		connection.jobId = szJobID;
		connection.connect(Constants.UPDATE_POSITION_AND_GET_JOB);
	}
}
