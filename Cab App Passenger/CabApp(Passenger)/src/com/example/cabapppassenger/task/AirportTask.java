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

public class AirportTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	ProgressDialog mDialog;
	public double latitude, longitude;
	public Handler handler;

	public AirportTask(Context context) {
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
				Log.i("Connection Time out in Airport task",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error fetching nearby airports, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Airport Task", "" + e.getMessage());
			}

			Log.i("****RESULT****", "" + result);
		} else {
			Toast.makeText(mContext, "No network connection.",
					Toast.LENGTH_SHORT).show();
			if (mDialog != null)
				mDialog.dismiss();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONObject airport_jobj = null;

		if (!result.equals("")) {

			try {
				airport_jobj = new JSONObject(result);
				if (handler != null) {
					Bundle bundle_data = new Bundle();
					bundle_data.putString("Airport_Response", result);
					Message msg = new Message();
					msg.obj = bundle_data;
					msg.setData(bundle_data);
					handler.sendMessage(msg);
				}
				if (mDialog != null)
					mDialog.dismiss();
			} catch (Exception e1) {
				try {
					JSONObject jobj = new JSONObject(result);
					String message = jobj.getString("Message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Airport Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
					// }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in Airport Task Post Exeute",
							"" + e1.getMessage());
				}

				Log.i("Exception in Airport Task Post Exeute",
						"" + e1.getMessage());
			}
		}
	}

	// private void pass_bundle_airport(String airportid, String airportName,
	// String address1, String postcode, String latitude2,
	// String longitude2, String cityId, String fixedPrice,
	// String fixedPriceCurrency) {
	// // TODO Auto-generated method stub
	// Bundle bundle = new Bundle();
	// bundle.putString("AirportId", airportid);
	// bundle.putString("AirportName", airportName);
	// bundle.putString("Address1", address1);
	// bundle.putString("Postcode", postcode);
	// bundle.putString("Latitude", latitude2);
	// bundle.putString("Longitude", longitude2);
	// bundle.putString("CityId", cityId);
	// bundle.putString("FixedPrice", fixedPrice);
	// bundle.putString("FixedPriceCurrency", fixedPriceCurrency);
	//
	// if (handler != null) {
	// Message msg = new Message();
	// msg.obj = bundle;
	// msg.setData(bundle);
	// handler.sendMessage(msg);
	// }
	// }

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
