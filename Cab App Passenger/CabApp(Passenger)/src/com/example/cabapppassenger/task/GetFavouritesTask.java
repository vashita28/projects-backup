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

import com.example.cabapppassenger.fragments.FavoritesFragment;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.maps.model.LatLng;

public class GetFavouritesTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	ArrayList<String> arr_favourites_address;
	public boolean fromhailnow;

	public GetFavouritesTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		arr_favourites_address = new ArrayList<String>();
		if (Constant.hash_latng_favourites != null)
			Constant.hash_latng_favourites.clear();

		Constant.arr_favourites.clear();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (!fromhailnow) {
			mDialog = showProgressDialog("Loading");
			mDialog.show();
		}
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
						+ "ws/v2/passenger/favourite/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Favourites Request", address);
				HttpGet httpGet = new HttpGet(address);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout
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
			if (!fromhailnow) {
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			Log.i("Exception in GetFavourite ", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			if (!fromhailnow) {
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Unable to get favourites data, Please try again.",

								Toast.LENGTH_SHORT).show();
					}
				});
			}
			Log.i("Exception in GetFavourite ", "" + e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		String address_pincode;
		super.onPostExecute(result);
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(result);
			if (!result.equals("")) {
				Log.i("Json Result", "" + jsonObject.toString());
				jsonObject = new JSONObject(result);
				editor.putString("Favourite Response", result);
				editor.commit();
				if (jsonObject != null) {
					if (FavoritesFragment.tv_nofavadded != null)
						FavoritesFragment.tv_nofavadded
								.setVisibility(View.GONE);
					JSONArray jArray_fav = jsonObject
							.getJSONArray("favourites");
					for (int i = 0; i < jArray_fav.length(); i++) {
						JSONObject jobj = jArray_fav.getJSONObject(i);
						String favourite_id = jobj.getString("favouriteId");
						String passenger_id = jobj.getString("passengerId");

						Double latitude = null, longitude = null;

						try {
							if (jobj.get("latitude") != null)
								latitude = jobj.getDouble("latitude");
							if (jobj.get("longitude") != null)
								longitude = jobj.getDouble("longitude");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						String address = jobj.getString("address");
						String pincode = jobj.getString("postcode");

						if (address == null || address.equalsIgnoreCase("null"))
							address = "";
						if (pincode == null || pincode.equalsIgnoreCase("null"))
							pincode = "";

						address_pincode = address + ", " + pincode;
						arr_favourites_address.add(address_pincode);
						Constant.arr_favourites.add(address.toLowerCase()
								.trim());
						Log.e("Fav arr", "" + Constant.arr_favourites.size());

						if (latitude != null && longitude != null) {
							LatLng pickuplatlng = new LatLng(latitude,
									longitude);

							Constant.hash_latng_favourites.put(address,
									pickuplatlng);
						} else
							Constant.hash_latng_favourites.put(address, null);

						Constant.hash_favid_favourites.put(address_pincode,
								favourite_id);

					}

				}
				if (mHandler != null) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("Addresslist",
							arr_favourites_address);

					msg.obj = bundle;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				} else {
					if (FavoritesFragment.tv_nofavadded != null)
						FavoritesFragment.tv_nofavadded
								.setVisibility(View.VISIBLE);
				}
			} else {
				if (FavoritesFragment.tv_nofavadded != null)
					FavoritesFragment.tv_nofavadded.setVisibility(View.VISIBLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (FavoritesFragment.tv_nofavadded != null)
				FavoritesFragment.tv_nofavadded.setVisibility(View.VISIBLE);
			try {
				if (!result.equals("")) {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("Error");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("type");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Get Favourite Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Error", e1.getMessage().toString());
			}

			Log.i("Exception in GetFavourite ", "" + e.getMessage());
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
