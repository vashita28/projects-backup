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

import com.example.cabapppassenger.fragments.CabappLocation_fragment;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class CityListTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	ArrayList<String> arr_citylist;

	public CityListTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		arr_citylist = new ArrayList<String>();

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
				String list = Constant.passengerURL + "ws/v2/city/list";
				Log.e("City List Request", list);
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
							"Unable to get city list data, Please try again.",

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
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(result);
			if (!result.equals("")) {
				if (CabappLocation_fragment.tv_nocities != null)
					CabappLocation_fragment.tv_nocities
							.setVisibility(View.GONE);
				Log.i("Json Result", "" + jsonObject.toString());
				jsonObject = new JSONObject(result);
				editor.putString("City List Response", result);
				editor.commit();
				if (jsonObject != null) {
					JSONArray jArray_city = jsonObject.getJSONArray("cities");
					for (int i = 0; i < jArray_city.length(); i++) {
						JSONObject jobj = jArray_city.getJSONObject(i);
						String city_name = jobj.getString("cityname");
						city_name = city_name.substring(0, 1).toUpperCase()
								+ city_name.substring(1).toLowerCase();
						arr_citylist.add(city_name);
					}
				}
				if (mHandler != null) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("CityList", arr_citylist);
					msg.obj = bundle;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				} else {
					if (CabappLocation_fragment.tv_nocities != null)
						CabappLocation_fragment.tv_nocities
								.setVisibility(View.VISIBLE);
				}
			} else {
				if (CabappLocation_fragment.tv_nocities != null)
					CabappLocation_fragment.tv_nocities
							.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (CabappLocation_fragment.tv_nocities != null)
					CabappLocation_fragment.tv_nocities
							.setVisibility(View.VISIBLE);
				if (!result.equals("")) {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("Error");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("type");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Get City List Error****", "" + message);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Error", e1.getMessage().toString());
			}

			Log.i("Exception in City List ", "" + e.getMessage());
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
