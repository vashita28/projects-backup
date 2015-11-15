package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class TrackDriverTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public String driver_id;
	public Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";

	public TrackDriverTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog = showProgressDialog("Loading");
		mDialog.show();
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}

	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		try {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				String address = Constant.passengerURL
						+ "ws/v2/passenger/trackdriver/?accessToken="
						+ shared_pref.getString("AccessToken", "")
						+ "&driverId=" + driver_id;
				Log.e("Track Driver Request", address);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpGet httpGet = new HttpGet(address);
				HttpClient client = new DefaultHttpClient(httpParams);

				HttpResponse httpResponse = client.execute(httpGet);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";
			} else {
				Toast.makeText(mContext, "No network connection",
						Toast.LENGTH_SHORT).show();
				if (mDialog != null)
					mDialog.dismiss();
			}

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
			Log.i("Connection Timeout in Track Driver ", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(
							mContext,
							"Error tracking driver's position, Please try again.",

							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in Track Driver ", "" + e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject;
		Double driver_latitude = null, driver_longitude = null, distance = null, distance_km = null, eta = null;

		try {
			jsonObject = new JSONObject(result);
			if (result != null) {
				driver_latitude = jsonObject.getDouble("latitude");
				driver_longitude = jsonObject.getDouble("longitude");
				// eta = jsonObject.getDouble("eta");
				// distance = jsonObject.getDouble("distance");
				// if (distance != null)
				// distance_km = distance * 0.00062137;

			}

			if (mHandler != null && driver_latitude != null
					&& driver_longitude != null) {
				Bundle bundle = new Bundle();
				Message msg = new Message();
				bundle.putDouble("DriverLatitude", driver_latitude);
				bundle.putDouble("DriverLongitude", driver_longitude);
				// bundle.putDouble("Distance", distance_km);
				// bundle.putDouble("Time", eta);
				msg.setData(bundle);
				msg.obj = bundle;
				mHandler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (result != null) {
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Track Driver Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Log.i("Error", e.getMessage().toString());
			}
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
}
