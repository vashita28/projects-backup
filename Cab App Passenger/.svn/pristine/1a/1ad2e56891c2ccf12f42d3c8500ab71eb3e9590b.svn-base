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
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.maps.model.LatLng;

public class AddFavourites extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	Double latitude, longitude;
	String address, pincode;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	TextView tvFavourite;

	public AddFavourites(Context context, String address, String pincode,
			Double latitude, Double longitude, TextView tvFavourite) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.pincode = pincode;
		this.tvFavourite = tvFavourite;
		Constant.arr_favourites.clear();

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

				jsonObject.put("address", address);
				jsonObject.put("postcode", pincode);
				jsonObject.put("latitude", latitude);
				jsonObject.put("longitude", longitude);

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
				Log.i("Connection Timeout in Add Favourites",
						"" + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error adding favourite, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Add Favourites", "" + e.getMessage());
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
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(result);
			if (!result.equals("")) {
				Log.i("Json Result", "" + jsonObject.toString());
				jsonObject = new JSONObject(result);
				editor.putString("Favourite Response", result);
				editor.commit();
				if (jsonObject != null && jsonObject.length() > 0) {
					JSONArray jArray_fav = jsonObject
							.getJSONArray("favourites");
					for (int i = 0; i < jArray_fav.length(); i++) {
						JSONObject jobj = jArray_fav.getJSONObject(i);
						String favourite_id = jobj.getString("favouriteId");
						String passenger_id = jobj.getString("passengerId");
						Double latitude = jobj.getDouble("latitude");
						Double longitude = jobj.getDouble("longitude");
						String address = jobj.getString("address");
						String pincode = jobj.getString("postcode");

						if (address != null && latitude != null
								&& longitude != null && pincode != null) {

							if (address.matches("null"))
								address = "";
							if (pincode.matches("null"))
								pincode = "";

							address = address + ", " + pincode;
							LatLng pickuplatlng = new LatLng(latitude,
									longitude);
							Constant.arr_favourites.add(address);

							Constant.hash_latng_favourites.put(address,
									pickuplatlng);
						}

					}

				}
				if (mDialog != null)
					mDialog.dismiss();
				tvFavourite.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.already_fev, 0, 0, 0);
				Toast.makeText(mContext,
						"Location added to favourites successfully.",
						Toast.LENGTH_SHORT).show();

				Constant.arr_favourites.add(address.toLowerCase().trim());
			}

		} catch (Exception e) {
			if (mDialog != null)
				mDialog.dismiss();
			JSONObject jobj;
			try {
				if (result != null) {
					jobj = new JSONObject(result);

					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");

					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Add Fav Error****", "" + message);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block

				Log.i("Exception in Add Favourites Post Exeute",
						"" + e.getMessage());
			}
			Log.i("Exception in Add Favourites Post Exeute",
					"" + e.getMessage());
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
