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
import com.example.cabapppassenger.util.GetterSetter;

public class GetAvailableCabsTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	public boolean flag_noloading;;
	String PREF_NAME = "CabApp_Passenger";
	public double latitude, longitude;
	Context mContext;
	public Handler mHandler;
	ProgressDialog mDialog;

	public GetAvailableCabsTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (!flag_noloading) {
			mDialog = showProgressDialog("Loading");
			mDialog.show();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {

		return POST(urls[0]);
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
				JSONObject dataobj = new JSONObject();

				JSONObject jsonObject = new JSONObject();

				jsonObject.accumulate("latitude", latitude);
				jsonObject.accumulate("longitude", longitude);

				json = jsonObject.toString();
				Log.d("com.example.testcabapp", "POST :: " + json);
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
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.d("InputStream", e.getLocalizedMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error fetching cabs, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.d("InputStream", e.getLocalizedMessage());
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
		super.onPostExecute(result);
		JSONObject Javailablecabs_obj = null;
		// String totalDrivers, time, distance;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			try {
				Javailablecabs_obj = new JSONObject(result);

				if (Javailablecabs_obj != null) {
					Bundle bundle = new Bundle();
					bundle.putString("Available Cab Response", result);
					if (mHandler != null) {
						Message msg = new Message();
						msg.obj = bundle;
						msg.setData(bundle);
						mHandler.sendMessage(msg);

					}
				}
				// Bundle bundle = null;
				// Javailablecabs_obj = new JSONObject(result);
				// if (Javailablecabs_obj != null) {
				//
				//
				// totalDrivers = Javailablecabs_obj.getString("totalDrivers");
				// if (!totalDrivers.equals("0")) {
				// time = Javailablecabs_obj.getString("eta");
				// if (time.equals("false"))
				// time = "0";
				// else if (time.equals("true"))
				// time = "1";
				//
				// distance = Javailablecabs_obj.getString("distance");
				// editor.putString("CabTime", time);
				// editor.putString("TotalCabs", totalDrivers);
				// editor.commit();
				// bundle = new Bundle();
				// bundle.putString("CabTime", time);
				// bundle.putString("TotalCabs", totalDrivers);
				//
				// JSONArray jArr_drivers = Javailablecabs_obj
				// .getJSONArray("drivers");
				// for (int i = 0; i <= jArr_drivers.length(); i++) {
				// JSONObject jobj = jArr_drivers.getJSONObject(i);
				// String cablatitude = jobj.getString("latitude");
				// Double cablongitude = jobj.getDouble("longitude");
				// String forename = jobj.getString("forename");
				//
				// // LatLng ll = new LatLng(latitude, longitude);
				// // MarkerOptions marker = new MarkerOptions();
				// // marker.position(new LatLng(latitude, longitude));
				// // marker.icon(BitmapDescriptorFactory
				// // .fromResource(R.drawable.cabsarround));
				//
				// }
				// if (mHandler != null) {
				// Message msg = new Message();
				// msg.obj = bundle;
				// msg.setData(bundle);
				// mHandler.sendMessage(msg);
				//
				// }
				// }
				// }
			} catch (Exception e1) {
				try {
					if (result != null) {

						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("errors");
						for (int i = 0; i <= jArray.length(); i++) {
							JSONObject jObj = jArray.getJSONObject(i);
							String message = jObj.getString("message");
							if (message.toString().equals(
									"missing or invalid accessToken")) {
								Constant.logout(mContext, editor, true);
								Log.i("****Available Cab Error****", ""
										+ message);
							} else {
								Toast.makeText(mContext, message,
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				e1.printStackTrace();
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

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}
