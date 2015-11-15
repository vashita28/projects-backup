package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
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

import com.example.cabapppassenger.database.RecentTable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;

public class GetAddressTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	public double latitude, longitude;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	public boolean flag_hail, noloading_flag;

	public GetAddressTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		if (mContext != null) {
			shared_pref = mContext.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			editor = shared_pref.edit();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog = showProgressDialog("Loading");
		if (mDialog != null)
			mDialog.show();
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = null;
		if (mContext != null) {
			dialog = new ProgressDialog(mContext);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.setMessage(message + "...");
		}
		return dialog;

	}

	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		try {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				String address = String
						.format(Locale.ENGLISH,
								"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
										+ Locale.getDefault().getCountry(),
								latitude, longitude);

				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpConnectionParams.setSoTimeout(httpParams, 10000); // timeout
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				String json = "";
				HttpGet httpGet = new HttpGet(address);

				HttpResponse httpResponse = httpclient.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
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
			Log.i("Connection Time out in  Get Address", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			Log.i("Exception in  Get Address", "" + e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		String formated_address = "", postcode = "", withoutdesc = "", country = "";
		JSONObject jsonObject;

		try {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			jsonObject = new JSONObject(result);
			Log.i("Json Result", "" + jsonObject.toString());
			if (!result.equals("")) {

				if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
					JSONArray results = jsonObject.getJSONArray("results");
					JSONObject result1 = results.getJSONObject(0);
					formated_address = result1.getString("formatted_address");

					Log.i("***Address****", "" + formated_address);
					country = formated_address.substring(formated_address
							.lastIndexOf(" ") + 1);

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
							Log.e("Tag", "successfulll");
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
						// String lastWord = formated_address
						// .substring(formated_address.lastIndexOf(" ") + 1);
						Log.i("Country", "" + country);
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("Country", country);

						bundle.putString("currentaddress", withoutdesc);
						bundle.putString("currentpincode", postcode);
						bundle.putString("onmapaddress", withoutdesc);
						bundle.putString("onmappincode", postcode);
						msg.obj = bundle;
						msg.setData(bundle);
						mHandler.sendMessage(msg);

					}

					// if (addressHandler != null) {
					// Message msg = new Message();
					// Bundle bundle = new Bundle();
					// bundle.putString("address", formated_address);
					// bundle.putString("pincode", postcode);
					// bundle.putString("address", formated_address);
					// bundle.putString("pincode", postcode);
					// msg.obj = bundle;
					// msg.setData(bundle);
					// addressHandler.sendMessage(msg);
					// }
					// if (formated_address != null && flag_hail
					// && postcode != null) {
					// Hail_NowFragment.tv_address.setText(formated_address);
					// // addto_recentdb(formated_address, postcode);
					// }

					// if (mHandler != null && flag_hail) {
					//
					// Message msg = new Message();
					// Bundle bundle = new Bundle();
					// bundle.putString("currentaddress", formated_address);
					// bundle.putString("currentpostcode", postcode);
					//
					// }

				} else {
					Toast.makeText(
							mContext,
							"Unable to get current location, Please try again.",
							Toast.LENGTH_SHORT).show();
				}

			}
		} catch (Exception e) {
			// Log.i("Error", e.getMessage().toString());
			if (result != null) {
				if (mDialog != null)
					mDialog.dismiss();
				// ((Activity) mContext).runOnUiThread(new Runnable() {
				// public void run() {
				// Toast.makeText(
				// mContext,
				// "Error getting current location, Please try again.",
				// Toast.LENGTH_SHORT).show();
				// }
				// });
			}
		}
	}

	// public String POST(String url) {
	// String result = "";
	// try {
	//
	// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
	// String address = String
	// .format(Locale.ENGLISH, url
	// + Locale.getDefault().getCountry(), latitude,
	// longitude);
	// HttpGet httpGet = new HttpGet(address);
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse response;
	// StringBuilder stringBuilder = new StringBuilder();
	//
	// List<Address> retList = null;
	//
	// response = client.execute(httpGet);
	// HttpEntity entity = response.getEntity();
	// InputStream stream = entity.getContent();
	// int b;
	// while ((b = stream.read()) != -1) {
	// stringBuilder.append((char) b);
	// }
	//
	// JSONObject jsonObject = new JSONObject();
	// jsonObject = new JSONObject(stringBuilder.toString());
	// Log.i("Json Result", "" + jsonObject.toString());
	//
	// retList = new ArrayList<Address>();
	//
	// if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
	// JSONArray results = jsonObject.getJSONArray("results");
	// JSONObject result1 = results.getJSONObject(0);
	// result = result1.getString("formatted_address");
	// if (result != null)
	// Hail_NowFragment.str_address = result;
	// Log.i("***Address****", "" + result);
	// if (mHandler != null) {
	// String lastWord = result.substring(result
	// .lastIndexOf(" ") + 1);
	// Log.i("Country", "" + lastWord);
	// Message msg = new Message();
	// Bundle bundle = new Bundle();
	// bundle.putString("Country", lastWord);
	// msg.obj = bundle;
	// msg.setData(bundle);
	// mHandler.sendMessage(msg);
	// }
	//
	// // getting postcode
	// JSONArray Jaddress_components_arr = result1
	// .getJSONArray("address_components");
	//
	// for (int i = 0; i < Jaddress_components_arr.length(); i++) {
	// JSONObject leg = Jaddress_components_arr
	// .getJSONObject(i);
	// JSONArray types_arr = leg.getJSONArray("types");
	// Log.i("Array", "" + types_arr.getString(0));
	//
	// if (types_arr.getString(0).equals("postal_code")) {
	// Log.e("Tag", "successfulll");
	// String postcode = leg.getString("short_name");
	// Log.e("Postcode", "" + postcode);
	// Hail_NowFragment.str_postcode = postcode;
	//
	// }
	// }
	// } else {
	// Toast.makeText(mContext,
	// "Unable to get current location.Please try again.",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	// if (mDialog != null)
	// mDialog.dismiss();
	// } catch (Exception e) {
	//
	// if (mDialog != null)
	// mDialog.dismiss();
	// }
	// return result;
	// }

	private void addto_recentdb(String address, String postcode) {
		// TODO Auto-generated method stub
		try {
			RecentTable recent = new RecentTable(mContext);
			recent.open();

			if (!recent.isrecentAdded(address)) {
				recent.insert_recent(shared_pref.getString("Email", ""),
						shared_pref.getString("AccessToken", ""), address,
						postcode);
			} else {
				recent.updateItem(shared_pref.getString("Email", ""),
						shared_pref.getString("AccessToken", ""), address,
						postcode);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
