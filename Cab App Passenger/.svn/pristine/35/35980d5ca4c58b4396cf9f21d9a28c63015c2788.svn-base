package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.view.View;
import android.widget.Toast;

import com.example.cabapppassenger.fragments.RecentFragment;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.maps.model.LatLng;

public class GetRecentTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	ArrayList<String> arr_recentaddress;

	public GetRecentTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		arr_recentaddress = new ArrayList<String>();
		if (Constant.hash_latng_recent != null)
			Constant.hash_latng_recent.clear();

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
						+ "ws/v2/passenger/recentjobs/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Recent Request", address);
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
			Log.i("Connection Time out in GetRecent ", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Unable to get recent data, Please try again.",

							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in GetRecent ", "" + e.getMessage());
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

		try {
			jsonObject = new JSONObject(result);
			if (!result.equals("")) {
				Log.i("Json Result", "" + jsonObject.toString());
				JSONArray jArray_jobs = jsonObject.getJSONArray("jobs");

				Double pickuplatitude = null, pickuplongitude = null, droplatitude = null, droplongitude = null;

				if (jArray_jobs != null && jArray_jobs.length() > 0) {
					RecentFragment.tv_norecentadded.setVisibility(View.GONE);
					for (int i = 0; i < jArray_jobs.length(); i++) {
						JSONObject jobj = jArray_jobs.getJSONObject(i);
						String pickupaddress = null, dropaddress = null;
						if (jobj.has("pickupAddress"))
							pickupaddress = jobj.getString("pickupAddress");

						if (jobj.has("dropAddress"))
							dropaddress = jobj.getString("dropAddress");

						if (jobj.has("pickupLatitude"))
							pickuplatitude = jobj.getDouble("pickupLatitude");

						if (jobj.has("pickupLongitude"))
							pickuplongitude = jobj.getDouble("pickupLongitude");

						if (jobj.has("dropLatitude"))
							droplatitude = jobj.getDouble("dropLatitude");

						if (jobj.has("dropLongitude"))
							droplongitude = jobj.getDouble("dropLongitude");

						if (pickupaddress != null && pickuplatitude != null
								&& pickuplongitude != null) {
							arr_recentaddress.add(pickupaddress);

							LatLng pickuplatlng = new LatLng(pickuplatitude,
									pickuplongitude);

							Constant.hash_latng_recent.put(pickupaddress,
									pickuplatlng);
						}

						if (dropaddress != null
								&& !dropaddress.matches("As Directed")
								&& droplatitude != null
								&& droplongitude != null) {
							arr_recentaddress.add(dropaddress);
							LatLng droplatlng = new LatLng(droplatitude,
									droplongitude);
							Constant.hash_latng_recent.put(dropaddress,
									droplatlng);
						}

					}
					if (mHandler != null) {
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putStringArrayList("Addresslist",
								arr_recentaddress);

						msg.obj = bundle;
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					} else {
						RecentFragment.tv_norecentadded
								.setVisibility(View.VISIBLE);
					}
				} else {
					RecentFragment.tv_norecentadded.setVisibility(View.VISIBLE);
				}

			} else {
				RecentFragment.tv_norecentadded.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			RecentFragment.tv_norecentadded.setVisibility(View.VISIBLE);
			try {
				if (result != null) {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Get Recent Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Error", e1.getMessage().toString());
			}

			Log.i("Error", e.getMessage().toString());
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
