package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import com.example.cabapppassenger.util.GetterSetter;
import com.google.android.gms.maps.model.LatLng;

public class DeleteFavourites extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	String result = "";
	public String favourite_id;
	Handler mHandler;
	ArrayList<String> arr_favourites_address;
	ProgressDialog mDialog;
	String address;

	public DeleteFavourites(Context context, Handler handler, String address) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		this.address = address;
		editor = shared_pref.edit();
		this.mHandler = handler;
		arr_favourites_address = new ArrayList<String>();
		if (Constant.hash_latng_favourites != null)
			Constant.hash_latng_favourites.clear();

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

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
			HttpGet request = new HttpGet(Constant.passengerURL
					+ "ws/v2/passenger/favourite/?accessToken="
					+ shared_pref.getString("AccessToken", "") + "&id="
					+ favourite_id);
			HttpResponse response;

			try {
				response = client.execute(request);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					InputStream instream = entity.getContent();
					result = convertInputStreamToString(instream);
					Log.i("User Details", "" + result);

				}
			} catch (ClientProtocolException e1) {

				e1.printStackTrace();

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
				Log.i("Connection Time out in Delete Favourite ",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error deleting favourites, Please try again.",

								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Delete Favourite ", "" + e.getMessage());
			}

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
		JSONObject jsonObject = null;
		String address_pincode;
		if (mDialog != null)
			mDialog.dismiss();
		try {
			if (!result.equals("")) {
				jsonObject = new JSONObject(result);
				if (result != null) {
					Log.i("Json Result", "" + jsonObject.toString());
					jsonObject = new JSONObject(result);
					editor.putString("Favourite Response", result);
					editor.commit();
					if (jsonObject != null && jsonObject.length() > 0) {
						FavoritesFragment.tv_nofavadded
								.setVisibility(View.GONE);
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

								address_pincode = address + ", " + pincode;
								arr_favourites_address.add(address_pincode);

								LatLng pickuplatlng = new LatLng(latitude,
										longitude);
								Constant.arr_favourites.add(address_pincode);
								Constant.hash_latng_favourites.put(address,
										pickuplatlng);

								Constant.hash_favid_favourites.put(
										address_pincode, favourite_id);
							}

						}
						if (Constant.arr_favourites.size() > 0) {
							if (Constant.arr_favourites.lastIndexOf(address) != -1) {
								Constant.arr_favourites
										.remove(Constant.arr_favourites
												.lastIndexOf(address));
							}
						}
						Toast.makeText(mContext,
								"Favourites deleted successfully.",
								Toast.LENGTH_SHORT).show();

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
						FavoritesFragment.tv_nofavadded
								.setVisibility(View.VISIBLE);
					}
				} else {
					FavoritesFragment.tv_nofavadded.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			try {
				JSONObject jobj = new JSONObject(result);
				if (result != null) {
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Delete Favorites Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Exception in Delete Favourite Post execute",
						"" + e.getMessage());
			}

			Log.i("Exception in Delete Favourite Post execute",
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

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}
