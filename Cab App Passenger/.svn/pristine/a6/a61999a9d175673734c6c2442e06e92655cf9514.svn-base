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

import com.example.cabapppassenger.fragments.FindTaxiFragment;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;

public class CancelBookingTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	public boolean jobtimeout;
	public String booking_id;
	public Handler handler;
	ProgressDialog mDialog;

	public CancelBookingTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mDialog = showProgressDialog("Loading");
		mDialog.show();
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {

		String result = "";
		InputStream inputStream = null;
		try {
			if (FindTaxiFragment.pickerdialog != null)
				FindTaxiFragment.pickerdialog.dismiss();
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				String list = Constant.passengerURL
						+ "ws/v2/passenger/bookings/" + booking_id
						+ "/delete?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Cancel Booking Request", list);
				HttpGet httpGet = new HttpGet(list);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
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
			Log.i("Exception in Get City List ", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Unable to cancel your booking, Please try again.",

							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in GetCity List ", "" + e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONObject verificationjson_obj = null;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			if (FindTaxiFragment.pickerdialog != null)
				FindTaxiFragment.pickerdialog.dismiss();
			try {
				verificationjson_obj = new JSONObject(result);
				if (verificationjson_obj != null) {
					boolean success = verificationjson_obj
							.getBoolean("success");
					if (success) {

						if (handler != null) {
							Bundle bundle = new Bundle();
							if (jobtimeout)
								bundle.putString("Jobtimeout", "");
							else
								bundle.putString("CancelBooking", "");
							Message msg = new Message();
							msg.obj = bundle;
							msg.setData(bundle);
							handler.sendMessage(msg);
						}
					}
				}

			} catch (Exception e1) {
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Cancel Booking Error****", "" + message);
					} else if (message
							.equals("This booking is already completed")) {
						if (handler != null)
							handler.sendEmptyMessage(3);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in  Cancel Booking Post execute",
							"" + e.getMessage());
				}

				Log.i("Exception in Cancel Booking Post execute",
						"" + e1.getMessage());
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
