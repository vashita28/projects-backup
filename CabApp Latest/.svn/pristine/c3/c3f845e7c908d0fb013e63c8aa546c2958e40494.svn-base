/*
 * Coded By Manpreet
 */

package com.android.cabapp.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import com.android.cabapp.R;
import com.android.cabapp.util.Constants;

//Fetches all places from GooglePlaces AutoComplete Web Service
public class GetPincodeTask extends AsyncTask<String, Void, String> {
	String placeId;
	Context context;
	String clicked;
	public String szPincode, szLatitute, szLongitute;
	public Handler mHandler;

	public GetPincodeTask(Context context, String placeId, String clicked) {
		super();
		this.context = context;
		this.placeId = placeId;
		this.clicked = clicked;

	}

	@Override
	protected String doInBackground(String... place) {
		// For storing data from web service
		String data = "", totalData = "";

		// Obtain browser key from https://code.google.com/apis/console
		String key = "key=" + Constants.LocationKey;

		String placeid = "placeid=" + placeId;

		// Building the parameters to the web service
		String parameters = placeid + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/details/"
				+ output + "?" + parameters;

		try {
			// Fetching the data from web service in background
			data = downloadUrl(url);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());
		}

		totalData = data;
		if (Constants.isDebug)
			Log.d("GetPincodeTask", "Geocoder Data is:: " + data);
		data = parseForPincode(data);
		parseForLatLong(totalData);

		return data;
	}

	private String parseForPincode(String data) {

		String pincode = "";
		try {
			Log.i("data", "data::> " + data);
			JSONObject joPlace = new JSONObject(data);
			JSONObject joResults = joPlace.getJSONObject("result");
			JSONArray resultArray = joResults
					.getJSONArray("address_components");

			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject joIterateJoResult = resultArray.getJSONObject(i);
				JSONArray jaTypes = joIterateJoResult.getJSONArray("types");

				if (jaTypes.getString(0).equals("postal_code")) {
					pincode = joIterateJoResult.getString("short_name");
					szPincode = pincode;

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pincode;

	}

	private String parseForLatLong(String data) {
		String latlong = "";
		try {

			JSONObject joPlace = new JSONObject(data);
			JSONObject joResults = joPlace.getJSONObject("result");

			JSONObject joGeometry = joResults.getJSONObject("geometry");

			JSONObject joLocation = joGeometry.getJSONObject("location");

			szLatitute = joLocation.getString("lat");
			szLongitute = joLocation.getString("lng");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latlong;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		Activity activity = (Activity) context;
		EditText etPickUpPincode, etDropOffPincode;
		etPickUpPincode = ((EditText) activity
				.findViewById(R.id.etPickupPincode));
		etDropOffPincode = ((EditText) activity
				.findViewById(R.id.etDropOffPinCode));

		if (etPickUpPincode != null && etDropOffPincode != null)
			if (clicked.equals("atvPickUpPlaces")) {
				etPickUpPincode.setText(result);
			} else if (clicked.equals("atvDropOffPlaces")) {
				etDropOffPincode.setText(result);
			}

		if (mHandler != null) {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("pincode", szPincode);
			bundle.putString("selectedlat", szLatitute);
			bundle.putString("selectedLng", szLongitute);
			msg.obj = bundle;
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}

	}

	/** A method to download json data from url */
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
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}
}
