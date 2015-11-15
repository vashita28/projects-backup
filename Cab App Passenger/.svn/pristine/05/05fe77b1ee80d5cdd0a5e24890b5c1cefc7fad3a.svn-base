/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.task;

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

import com.example.cabapppassenger.R;
import com.google.android.gms.maps.model.LatLng;

//Fetches all places from GooglePlaces AutoComplete Web Service
public class GetPincodeTask extends AsyncTask<String, Void, String> {
	String placeId;
	Context context;
	String clicked;
	Handler latLngHandler, pincodeHandler, cityHandler;

	public GetPincodeTask(Context context, String placeId, String clicked,
			Handler latLngHandler, Handler pincodeHandler) {
		super();
		this.context = context;
		this.placeId = placeId;
		this.latLngHandler = latLngHandler;
		this.clicked = clicked;
		this.pincodeHandler = pincodeHandler;

	}

	public GetPincodeTask(Context context, String placeId, String clicked,
			Handler cityHandler) {
		super();
		this.context = context;
		this.placeId = placeId;
		this.cityHandler = cityHandler;
		this.clicked = clicked;

	}

	@Override
	protected String doInBackground(String... place) {
		// For storing data from web service
		String data = "";

		// Obtain browser key from https://code.google.com/apis/console
		/* String key = "key=AIzaSyB30WXIpN_hwncBD66_PmBqH0HGaKueUXY"; */
		String key = "key=AIzaSyDLaFAUpZcdk_UeAbrr48x2Uzo2up4QeJI";

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

		Log.d("GetPincodeTask", data);
		if (cityHandler != null) {
			parseForCity(data);
		} else {
			parseForLatLong(data);
			data = parseForPincode(data);
		}

		return data;
	}

	private String parseForPincode(String data) {

		String pincode = "";
		try {

			Log.i("data", data);
			JSONObject joPlace = new JSONObject(data);
			JSONObject joResults = joPlace.getJSONObject("result");
			JSONArray resultArray = joResults
					.getJSONArray("address_components");

			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject joIterateJoResult = resultArray.getJSONObject(i);
				JSONArray jaTypes = joIterateJoResult.getJSONArray("types");

				if (jaTypes.getString(0).equals("postal_code")) {
					pincode = joIterateJoResult.getString("short_name");

					break;
				} else if (jaTypes.getString(0).equals("locality")
						|| jaTypes.getString(0).equals(
								"administrative_area_level_2")) {
					pincode = joIterateJoResult.getString("short_name");
				}
			}

			Message pincodeMessage = new Message();
			Bundle pincodeBundle = new Bundle();
			pincodeBundle.putString("pincode", pincode);
			pincodeMessage.obj = pincodeBundle;
			pincodeMessage.setData(pincodeBundle);

			if (pincodeHandler != null)

				pincodeHandler.sendMessage(pincodeMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pincode;

	}

	private LatLng parseForLatLong(String data) {

		String pincode = "";
		LatLng latLng = null;
		try {
			Log.i("data", data);
			JSONObject joPlace = new JSONObject(data);
			JSONObject joResults = joPlace.getJSONObject("result");
			JSONObject joGeometry = joResults.getJSONObject("geometry");
			JSONObject joLocation = joGeometry.getJSONObject("location");

			latLng = new LatLng(joLocation.getDouble("lat"),
					joLocation.getDouble("lng"));

			Message latLngMessage = new Message();
			Bundle latLngBundle = new Bundle();
			latLngBundle.putDouble("latitude", latLng.latitude);
			latLngBundle.putDouble("longitude", latLng.longitude);
			latLngMessage.obj = latLngBundle;
			latLngMessage.setData(latLngBundle);

			latLngHandler.sendMessage(latLngMessage);
			Log.i(getClass().getSimpleName(), "latLng" + latLng.latitude + " "
					+ latLng.longitude);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latLng;

	}

	private LatLng parseForCity(String data) {

		String name = "", vicinity = "";
		LatLng latLng = null;
		try {
			Log.i("data", data);
			JSONObject joPlace = new JSONObject(data);
			JSONObject joResults = joPlace.getJSONObject("result");
			if (joResults.has("name"))
				name = joResults.getString("name");
			if (joResults.has("vicinity"))
				vicinity = joResults.getString("vicinity");

			Message cityMessage = new Message();
			Bundle cityBundle = new Bundle();
			if (!name.equals(""))
				cityBundle.putString("City", name);
			if (!vicinity.equals(""))
				cityBundle.putString("City", vicinity);

			cityMessage.obj = cityBundle;
			cityMessage.setData(cityBundle);
			if (cityHandler != null)
				cityHandler.sendMessage(cityMessage);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latLng;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		Log.e("Result of Pincode", result);
		Activity activity = (Activity) context;
		EditText etPickUpPincode, etDropOffPincode;
		etPickUpPincode = ((EditText) activity
				.findViewById(R.id.et_pick_up_pincode));
		etDropOffPincode = ((EditText) activity
				.findViewById(R.id.et_drop_off_pincode));

		if (etPickUpPincode != null) {
			if (clicked.equals("atvPickUpPlaces")) {
				// etPickUpPincode.setText(result);
			}
		}
		if (etDropOffPincode != null) {
			if (clicked.equals("atvDropOffPlaces")) {
				// etDropOffPincode.setText(result);
			}

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
