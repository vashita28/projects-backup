package com.android.cabapp.async;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.cabapp.fragments.FareCalculatorFragment;
import com.android.cabapp.fragments.InboxFragment;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.UrlSigner;
import com.android.cabapp.util.Util;

public class CalculateDistanceTask extends AsyncTask<String, Void, String> {
	private static final String TAG = CalculateDistanceTask.class
			.getSimpleName();

	public Context mContext;

	public String pickupLat, pickupLng, dropoffLat, dropoffLng;

	public TextView txtviewDistance;
	String szPickupToDropOffDistance = "";
	String jobID;
	public static boolean isFromFareCalculator = false;

	// HashMap<String, Distance> mapDistance = new HashMap<String, Distance>();

	public CalculateDistanceTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	// public void calculateAndDisplayDistancePickupAndDropOff() {
	//
	// }

	@Override
	protected String doInBackground(String... params) {

		Log.e(TAG, "CalculateDistanceTask   " + pickupLat + "  pickupLng "
				+ pickupLng + "  dropoffLat  " + dropoffLat + "  dropoffLng  "
				+ dropoffLng);
		// TODO Auto-generated method stub
		String url;
		// http://maps.googleapis.com/maps/api/distancematrix/json?origins=54.406505,18.67708&destinations=54.446251,18.570993&mode=driving&language=en-EN&sensor=false
		//url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
		url = "http://maps.googleapis.com/maps/api/directions/json?client=gme-cabapp&origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
		url = String.format(url, pickupLat, pickupLng, dropoffLat, dropoffLng);
		
		Log.e(TAG, "mapsURLPickupToDropOff client URL :: " + url);
		try {
			url = UrlSigner.getMapURL(url);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e(TAG, "mapsURLPickupToDropOff signed URL :: " + url);

		Connection connection = new Connection(mContext);
		connection.mapURL = url;
		connection.connect(Constants.MAP_DISTANCE);
		try {
			szPickupToDropOffDistance = Util.parseMapDistanceData(connection
					.inputStreamToString(connection.mInputStream).toString(),
					mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (Util.mContext != null) {
			if (!szPickupToDropOffDistance.equals("-")
					&& !szPickupToDropOffDistance.equals(""))
				txtviewDistance.setText(Util.getDistance(mContext,
						Double.parseDouble(szPickupToDropOffDistance),
						Util.getDistanceFormat(mContext))
						+ Util.getDistanceFormat(mContext));
			else
				txtviewDistance.setText("0");
		}

		// else {
		// if (!szPickupToDropOffDistance.equals("-")
		// && !szPickupToDropOffDistance.equals(""))
		// txtviewDistance.setText(Util.getDistance(mContext,
		// Double.parseDouble(szPickupToDropOffDistance), "km")
		// + " km");
		// else
		// txtviewDistance.setText("0");
		// }

		if (isFromFareCalculator)
			if (FareCalculatorFragment.mHandlerCalculateAmt != null) {
				FareCalculatorFragment.mHandlerCalculateAmt.sendEmptyMessage(1);
			}

		Distance distance = new Distance();
		distance.szDistance = txtviewDistance.getText().toString();

	}

	class Distance {
		String szDistance;
	}

}
