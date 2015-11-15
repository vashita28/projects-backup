package com.android.cabapp.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

//Fetches all places from GooglePlaces AutoComplete Web Service
public class GetDistanceByDirectionApiTask extends
		AsyncTask<String, Void, String> {

	Context context;
	Handler mHandler;
	Location mPickUpLocation, mDropOffLocation;
	int mRequestId;
	Dialog mDialog;

	public GetDistanceByDirectionApiTask(Context context,
			Location pickUpLocation, Location dropOffLocation, Handler handler,
			int requestId) {
		super();
		this.context = context;
		this.mPickUpLocation = pickUpLocation;
		this.mDropOffLocation = dropOffLocation;
		this.mHandler = handler;
		this.mRequestId = requestId;
		if (handler != null) {

			try {
				mDialog = showProgressDialog("Loading");
				mDialog.show();
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "Exception " + e.getMessage());
			}
		}

	}

	@Override
	protected String doInBackground(String... place) {
		// For storing data from web service
		String data = "";
		// Obtain browser key from https://code.google.com/apis/console
		// String key = "key=AIzaSyB30WXIpN_hwncBD66_PmBqH0HGaKueUXY";
		// String key = "key=AIzaSyDLaFAUpZcdk_UeAbrr48x2Uzo2up4QeJI";
		String key = "key=AIzaSyCio-EHK4nVtsJftg1abpO5veggoxZKkHA";

		String origins = "origin=" + mPickUpLocation.getLatitude() + ","
				+ mPickUpLocation.getLongitude();

		String destinations = "destination=" + mDropOffLocation.getLatitude()
				+ "," + mDropOffLocation.getLongitude();

		String mode = "mode=driving";

		String units = "units=imperial";

		String alternatives = "alternatives=true";

		// Building the parameters to the web service
		String parameters = origins + "&" + destinations + "&" + mode + "&"
				+ units + "&" + alternatives + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		Log.d(getClass().getSimpleName(), "********* NETWORK REQUEST URL *****");
		Log.d(getClass().getSimpleName(), url);

		try {
			// Fetching the data from web service in background
			data = downloadUrl(url);

			Log.d(getClass().getSimpleName(),
					"*********NETWORK REQUEST RESPONSE *****");

			Log.d(getClass().getSimpleName(), data);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());

			Log.d(getClass().getSimpleName(), "Exception " + e.getMessage());
		}

		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// Creating ParserTask
		Log.i(getClass().getSimpleName(), result);
		DistanceTimeParserForDirectionApiTask parserTask = new DistanceTimeParserForDirectionApiTask(
				context, mHandler, mRequestId);
		parserTask.execute(result);
		if (mHandler != null)
			mDialog.dismiss();

	}

	/**
	 * A method to download json data from url
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "Exception while downloading url"
					+ e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}

}
