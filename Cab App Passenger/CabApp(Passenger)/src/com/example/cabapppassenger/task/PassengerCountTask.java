package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
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

public class PassengerCountTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	public Double latitude, longitude;
	String address, pincode;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";

	public PassengerCountTask(Context context, Handler passengercount) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		this.mHandler = passengercount;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog = showProgressDialog("Loading...");
		if (mDialog != null)
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
		return POST(url[0]);
	}

	public String POST(String url) {

		InputStream inputStream = null;
		String result = "";
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpPost httpPost = new HttpPost(url);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				String json = "";

				JSONObject jsonObject = new JSONObject();

				// jsonObject.put("address", address);
				// jsonObject.put("postcode", pincode);
				jsonObject.put("latitude", latitude);
				jsonObject.put("longitude", longitude);

				json = jsonObject.toString();
				Log.d("Passenger Count Request", json);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", json));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				httpPost.addHeader("Content-Type",
						"application/x-www-form-urlencoded");
				Log.e("POST", "JSON  DATA is :: " + json.toString());

				HttpResponse httpResponse = httpclient.execute(httpPost);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} catch (ConnectTimeoutException e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				Log.i("Connection Time out in Passenger Count",
						"" + e.getMessage());
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Passenger Count", "" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				// ((Activity) mContext).runOnUiThread(new Runnable() {
				// public void run() {
				// Toast.makeText(mContext,
				// "Error adding favourite, Please try again.",
				// Toast.LENGTH_SHORT).show();
				// }
				// });
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
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject;
		String cityId, maxPassenger, minPassenger;

		try {
			jsonObject = new JSONObject(result);
			if (result != null) {
				Log.i("Json Result", "" + jsonObject.toString());
				jsonObject = new JSONObject(result);
				editor.putString("Passenger Count Response", result);
				editor.commit();
				if (jsonObject != null && jsonObject.length() > 0) {
					cityId = jsonObject.getString("cityId");
					maxPassenger = jsonObject
							.getString("maximumPassengersPerCab");
					minPassenger = jsonObject
							.getString("minimumPassengersPerCab");
					Log.e("Min and Max", maxPassenger + "" + minPassenger);
					if (mHandler != null)
						setPassenger_count(maxPassenger, minPassenger);
				}

			}

		} catch (Exception e) {
			JSONObject jobj;
			try {
				if (result != null) {
					jobj = new JSONObject(result);

					JSONArray jArray = jobj.getJSONArray("errors");
					// for (int i = 0; i <= jArray.length(); i++) {
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Passenger Count Error****", "" + message);
					}
					// else
					// Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
					// .show();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block

				e1.printStackTrace();
			}

			Log.i("Error", e.getMessage().toString());
		}
	}

	public void setPassenger_count(String maxPassenger, String minPassenger) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString("passenger_count_min", minPassenger);
		bundle.putString("passenger_count_max", maxPassenger);
		Message msg = new Message();
		msg.obj = bundle;
		msg.setData(bundle);
		if (mHandler != null)
			mHandler.sendMessage(msg);
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
