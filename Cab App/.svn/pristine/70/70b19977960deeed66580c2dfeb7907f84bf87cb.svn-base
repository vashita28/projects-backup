package com.android.cabapp.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class DistanceTask extends AsyncTask<String, Void, String> {
	public String pickupLat, pickupLng, dropoffLat, dropoffLng, userLat,
			userLng;
	public TextView txtviewDistancePickup, txtviewDistanceDropoff;
	String szPickupToDropOffDistance = "", szUserToPickupDistance = "";
	// String jobID;
	Context mContext;

	public void DistanceTask() {

	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url;
		// http://maps.googleapis.com/maps/api/distancematrix/json?origins=54.406505,18.67708&destinations=54.446251,18.570993&mode=driving&language=en-EN&sensor=false
		//url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
		url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
		url = String.format(url, pickupLat, pickupLng, dropoffLat, dropoffLng);
		Log.e("NowJobsExpandableAdapter", "mapsURLPickupToDropOff:: " + url);

		Connection connection = new Connection(mContext);
		connection.mapURL = url;
		connection.connect(Constants.MAP_DISTANCE);
		try {
			szPickupToDropOffDistance = Util.parseMapDistanceData(connection
					.inputStreamToString(connection.mInputStream).toString(),
					mContext);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
		url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
		url = String.format(url, userLat, userLng, pickupLat, pickupLng);
		Log.e("NowJobsExpandableAdapter", "mapsURLUserToPickup:: " + url);

		connection = new Connection(mContext);
		connection.mapURL = url;
		connection.connect(Constants.MAP_DISTANCE);

		try {
			szUserToPickupDistance = Util.parseMapDistanceData(connection
					.inputStreamToString(connection.mInputStream).toString(),
					mContext);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		try {
			if (Util.mContext != null) {
				// txtDistanceUnit.setText("miles");

				if (!szPickupToDropOffDistance.equals("-")
						&& !szPickupToDropOffDistance.equals(""))
					txtviewDistanceDropoff.setText(Util.getDistance(
							Util.mContext,
							Double.parseDouble(szPickupToDropOffDistance),
							Util.getDistanceFormat(Util.mContext)));
				else
					txtviewDistanceDropoff.setText("-");

				if (!szUserToPickupDistance.equals("-")
						&& !szUserToPickupDistance.equals(""))
					txtviewDistancePickup.setText(Util.getDistance(
							Util.mContext,
							Double.parseDouble(szUserToPickupDistance),
							Util.getDistanceFormat(Util.mContext)));
				else
					txtviewDistancePickup.setText("-");

			}
			// else {
			// // txtDistanceUnit.setText("km");
			// if (!szPickupToDropOffDistance.equals("-")
			// && !szPickupToDropOffDistance.equals(""))
			// txtviewDistanceDropoff.setText(Util
			// .getDistance(mContext, Double
			// .parseDouble(szPickupToDropOffDistance),
			// "km"));
			// else
			// txtviewDistanceDropoff.setText("-");
			//
			// if (!szUserToPickupDistance.equals("-")
			// && !szUserToPickupDistance.equals(""))
			// txtviewDistancePickup.setText(Util.getDistance(mContext,
			// Double.parseDouble(szUserToPickupDistance), "km"));
			// else
			// txtviewDistancePickup.setText("-");
			//
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
