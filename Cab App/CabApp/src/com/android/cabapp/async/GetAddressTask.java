package com.android.cabapp.async;

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
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class GetAddressTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	public double latitude, longitude;

	public GetAddressTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;

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
			if (NetworkUtil.isNetworkOn(mContext)) {
				String address = String
						.format(Locale.ENGLISH,
								"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
										+ Locale.getDefault().getCountry(),
								latitude, longitude);
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
			} else {
				Util.showToastMessage(mContext, "No network connection",
						Toast.LENGTH_LONG);
				if (mDialog != null)
					mDialog.dismiss();
			}

		} catch (ConnectTimeoutException e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Util.showToastMessage(mContext,
							"Connection timed out, Please try again",
							Toast.LENGTH_LONG);
				}
			});
			Log.d("InputStream", e.getLocalizedMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Util.showToastMessage(mContext,
							"Unable to get current location, Please try again",
							Toast.LENGTH_LONG);
				}
			});
			Log.d("InputStream", e.getLocalizedMessage());
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		String formated_address = "", postcode = "", withoutdesc = "";
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(result);
			Log.i("Json Result", "" + jsonObject.toString());
			if (result != null) {

				if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
					JSONArray results = jsonObject.getJSONArray("results");
					JSONObject result1 = results.getJSONObject(0);
					formated_address = result1.getString("formatted_address");
					String fullAddress = formated_address;

					// getting postcode
					JSONArray Jaddress_components_arr = result1
							.getJSONArray("address_components");

					for (int i = 0; i < Jaddress_components_arr.length(); i++) {
						JSONObject leg = Jaddress_components_arr
								.getJSONObject(i);
						JSONArray types_arr = leg.getJSONArray("types");
						Log.i("Array", "" + types_arr.getString(0));

						if (types_arr.getString(0).equals("postal_code")
								|| types_arr.getString(0).equals("postal_town")) {
							postcode = leg.getString("short_name");
							Log.e("Postcode", "" + postcode);
							// Hail_NowFragment.str_postcode = postcode;

						}
					}
					withoutdesc = formated_address;
					while (formated_address.lastIndexOf(",") != -1) {
						withoutdesc = formated_address.substring(0,
								formated_address.lastIndexOf(","));
						if (withoutdesc.indexOf(",") == -1) {
							withoutdesc = formated_address;
						}
						formated_address = formated_address.substring(0,
								formated_address.lastIndexOf(","));

					}
					if (mHandler != null) {
						String lastWord = fullAddress.substring(fullAddress
								.lastIndexOf(" ") + 1);

						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("Country", lastWord);
						bundle.putString("currentaddress", withoutdesc);
						bundle.putString("currentpincode", postcode);
						msg.obj = bundle;
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					}

				} else {
					Util.showToastMessage(mContext,
							"Unable to get current location, Please try again",
							Toast.LENGTH_LONG);
				}

			}
		} catch (Exception e) {
			Log.i("Error", e.getStackTrace().toString());
			if (mDialog != null)
				mDialog.dismiss();
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Util.showToastMessage(mContext,
							"Error getting current location, Please try again",
							Toast.LENGTH_LONG);
				}
			});
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
