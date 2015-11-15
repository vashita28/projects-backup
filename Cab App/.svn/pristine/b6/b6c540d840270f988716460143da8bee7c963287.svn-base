package com.android.cabapp.model;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.cabapp.datastruct.json.JobsList;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.google.gson.Gson;

public class FetchJobs {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Location currentLocation = null;

	public FetchJobs(Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public JobsList getJobsList(boolean bIsPreBook, Context context,
			Location currentDriverLocation) {
		JobsList jobsListResponse = null;
		if (!bIsPreBook) {
			if (currentDriverLocation != null)
				currentLocation = currentDriverLocation;
			else
				currentLocation = Util.getLocation(context);

			if (currentLocation != null) {

				String lat = String.valueOf(currentLocation.getLatitude());
				String lng = String.valueOf(currentLocation.getLongitude());
				Log.e("current Location", "lat lng" + lat + "   " + lng);
				nameValuePairs
						.add(new BasicNameValuePair("data[latitude]", lat));// london:
				// "51.500152"//
				// mumbai:"18.99026"
				nameValuePairs.add(new BasicNameValuePair("data[longitude]",
						lng));// london:"-0.126236"//mumbai: "72.82088"
			}
			connection.prepareConnection(nameValuePairs);
			connection.connect(Constants.JOBS_FRAGMENT);

		} else {
			connection.connect(Constants.PRE_BOOK_FRAGMENT);
		}

		if (connection.mInputStream != null) {
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(connection.mInputStream);
			jobsListResponse = gson.fromJson(reader, JobsList.class);
			AppValues.jobsListResponse = jobsListResponse;
		}

		return jobsListResponse;

	}
}
