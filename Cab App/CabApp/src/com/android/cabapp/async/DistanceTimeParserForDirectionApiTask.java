package com.android.cabapp.async;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * A class to parse the Google Places in JSON format
 */
public class DistanceTimeParserForDirectionApiTask extends
		AsyncTask<String, Integer, List<HashMap<String, String>>> {

	JSONObject jObject;
	Context context;
	Activity activity;
	String mDuration = "0 min", mDistanceValueInMiles = "0 miles";
	long mDurationValueInSeconds = 0, mDistanceValueInMeters = 0;
	Handler mHandler;
	int mRequestId;

	public DistanceTimeParserForDirectionApiTask(Context context,
			Handler handler, int requestId) {
		super();
		this.context = context;
		this.mHandler = handler;
		this.mRequestId = requestId;

	}

	@Override
	protected List<HashMap<String, String>> doInBackground(String... jsonData) {

		try {
			JSONObject jObject = new JSONObject(jsonData[0]);

			JSONArray jRoutesArray = jObject.getJSONArray("routes");

			for (int i = 0; i < jRoutesArray.length(); i++) {
				JSONObject jRouteObject = jRoutesArray.getJSONObject(i);
				JSONArray jLegsArray = jRouteObject.getJSONArray("legs");

				for (int j = 0; j < jLegsArray.length(); j++) {
					JSONObject jLegObject = jLegsArray.getJSONObject(j);
					JSONObject jDurationObject = null;
					Long durationValue = 0l;
					if (jLegObject.has("duration"))
						jDurationObject = jLegObject.getJSONObject("duration");

					JSONObject jDistanceObject = jLegObject
							.getJSONObject("distance");

					Long distanceValue = jDistanceObject.getLong("value");

					String distanceValueInMiles = jDistanceObject
							.getString("text");

					if (jDurationObject != null)
						durationValue = jDurationObject.getLong("value");

					if (mDistanceValueInMeters == 0
							|| distanceValue < mDistanceValueInMeters) {

						mDistanceValueInMeters = distanceValue;
						mDurationValueInSeconds = durationValue;
						mDistanceValueInMiles = distanceValueInMiles;
						mDuration = jDurationObject.getString("text");

					}

				}

			}

			// as we need to show 1 hr 30 mins as 90mins so
			if (mDuration.contains("hour") || mDuration.contains("hours"))
				;
			mDuration = (mDurationValueInSeconds / 60) + " mins";

			// as we need to edit duration to have min with 1 or 0 and mins with
			// all other values
			int iDuration = Integer.parseInt(mDuration.replaceAll("[\\D]", ""));
			if (iDuration == 0 || iDuration == 1)
				mDuration = iDuration + " min";
			else
				mDuration = iDuration + " mins";
			Bundle bundleData = new Bundle();

			bundleData.putLong("durationValueInSeconds",
					mDurationValueInSeconds);

			bundleData.putString("duration", mDuration);
			bundleData.putString("distanceValueInMiles", mDistanceValueInMiles);
			bundleData.putLong("distanceValueInMeters", mDistanceValueInMeters);
			bundleData.putInt("requestId", mRequestId);

			Message message = new Message();
			message.setData(bundleData);

			if (mHandler != null)
				mHandler.sendMessage(message);

			// Getting the parsed data as a List construct

		} catch (Exception e) {
			Bundle bundleData = new Bundle();
			bundleData.putLong("durationValueInSeconds",
					mDurationValueInSeconds);
			bundleData.putString("duration", mDuration); // duration in minutes
			bundleData.putString("distanceValueInMiles", mDistanceValueInMiles);
			bundleData.putLong("distanceValueInMeters", mDistanceValueInMeters);
			bundleData.putInt("requestId", mRequestId);

			Message message = new Message();
			message.setData(bundleData);

			if (mHandler != null)
				mHandler.sendMessage(message);
			Log.d("Exception", e.toString());
		}

		return null;
	}

	@Override
	protected void onPostExecute(List<HashMap<String, String>> result) {

		Log.i(getClass().getSimpleName(), "distanceValueInMeters"
				+ mDistanceValueInMeters + "  durationValueInSeconds"
				+ mDurationValueInSeconds + " duration" + mDuration);
	}

}
