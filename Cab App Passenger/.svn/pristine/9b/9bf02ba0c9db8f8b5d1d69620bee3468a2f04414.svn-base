/*****************************************
 * Coded By- Manpreet
 * 
 * 
 ***********************************/

package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;

public class GetLatLongTask extends AsyncTask<String, Void, String> {

	String placeName, pincode;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	double latitude, longitude;
	Context mContext;
	ProgressDialog mDialog;
	Handler latLongHandler, flowHandler;

	public GetLatLongTask(Context context, String placeName, String pincode,
			Handler handler) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.placeName = placeName;
		this.pincode = pincode;
		this.latLongHandler = handler;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		try {
			mDialog = showProgressDialog("Loading");
			mDialog.show();
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), "cant show dialog ");
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {

		String result = "", jsonOfPlaces;
		jsonOfPlaces = getJsonFromPlaceName();

		try {
			JSONObject jsonMainObject = new JSONObject(jsonOfPlaces);
			JSONArray jaResults = jsonMainObject.getJSONArray("results");
			Log.d("GetLatLongTask", "result array" + jaResults);
			for (int i = 0; i < jaResults.length(); i++) {
				JSONObject joIterateJoResult = jaResults.getJSONObject(i);
				if (parseForPincode(joIterateJoResult.toString()).equals(
						pincode)) {
					result = parseForLatLong(joIterateJoResult.toString());
				}
			}
			if (result.equals("")) {
				result = parseForLatLong(jaResults.getJSONObject(0).toString());
			}

			if (latLongHandler != null) {

				JSONObject joLatLong = new JSONObject(result);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putDouble("latitude", joLatLong.getDouble("lat"));
				bundle.putDouble("longitude", joLatLong.getDouble("lng"));
				bundle.putString("address", placeName);
				bundle.putString("pincode", pincode);

				// Log.i("GetLatLongTask", "latitude")
				msg.obj = bundle;
				msg.setData(bundle);
				latLongHandler.sendMessage(msg);

			}

		} catch (JSONException e) {
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext, "Please type valid locations.",
							Toast.LENGTH_SHORT).show();
				}
			});

			e.printStackTrace();
		}

		return result;
	}

	public String getJsonFromPlaceName() {
		InputStream inputStream = null;
		String result = "";
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {
				String address = String
						.format(Locale.ENGLISH,
								"http://maps.googleapis.com/maps/api/geocode/json?address=%1$s&sensor=true&language="
										+ Locale.getDefault().getCountry(),
								placeName.replace(" ", "+"));
				HttpGet httpGet = new HttpGet(address);
				HttpClient client = new DefaultHttpClient();
				StringBuilder stringBuilder = new StringBuilder();

				List<Address> retList = null;

				HttpResponse httpResponse = client.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} catch (ConnectTimeoutException e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				// Log.d("InputStream", e.getLocalizedMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error fetching latitude and longitude, Please try again.",

								Toast.LENGTH_SHORT).show();
					}
				});
				// Log.d("InputStream", e.getLocalizedMessage());
			}

			Log.i("****RESULT****", "" + result);
		} else {

			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext, "No network connection",
							Toast.LENGTH_SHORT).show();
				}
			});

			if (mDialog != null)
				mDialog.dismiss();
		}

		Log.i("GetLatLongTask", "getjsonfromplacename result= " + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (mDialog != null)
			mDialog.dismiss();
		if (null != result) {

		}
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}

	private String parseForPincode(String data) {

		String pincode = "";
		try {
			Log.i("GetLatLongTask", " parseforpincode data= " + data);
			JSONObject joPlace = new JSONObject(data);

			JSONArray resultArray = joPlace.getJSONArray("address_components");

			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject joIterateJoResult = resultArray.getJSONObject(i);
				JSONArray jaTypes = joIterateJoResult.getJSONArray("types");

				if (jaTypes.getString(0).equals("postal_code")) {
					pincode = joIterateJoResult.getString("short_name");

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("GetLatLongTask", "parseforpincode result=" + pincode);
		return pincode;

	}

	private String parseForLatLong(String data) {

		String latlong = "";
		try {
			Log.i("GetLatLongTask ", "parseForLatLong data== " + data);
			JSONObject joPlace = new JSONObject(data);

			JSONObject joGeometry = joPlace.getJSONObject("geometry");

			JSONObject joLocation = joGeometry.getJSONObject("location");

			latlong = joLocation.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(" GetLatLongTask", "parseForLatLong result=" + latlong);
		return latlong;
	}
}