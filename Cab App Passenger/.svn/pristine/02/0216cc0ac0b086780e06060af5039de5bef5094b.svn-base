package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;

public class GetCountryCodeTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger", dialingcode;
	Context mContext;

	String result = null;
	public Handler mhandler;
	public boolean fromlanding_flag;
	ProgressDialog mDialog;

	public GetCountryCodeTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		Constant.hashmap_countrycode = new HashMap<String, Bitmap>();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (!fromlanding_flag) {
			mDialog = showProgressDialog("Loading");
			mDialog.show();

		}

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpGet request = new HttpGet(Constant.passengerURL
						+ "ws/v2/passenger/countrycode");
				HttpResponse response;

				response = client.execute(request);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					InputStream instream = entity.getContent();
					result = convertInputStreamToString(instream);
					Log.i("***wb_aboutus", "" + result);

				}
			} else {
				if (mDialog != null)
					mDialog.dismiss();
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext, "No network connection.",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();

		} catch (ConnectTimeoutException e) {

			if (!fromlanding_flag) {
				if (mDialog != null)
					mDialog.dismiss();

			}
			if (!fromlanding_flag)
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
			Log.i("Connection Time out in GetCountry Code ",
					"" + e.getMessage());
		} catch (Exception e) {
			if (!fromlanding_flag) {
				if (mDialog != null)
					mDialog.dismiss();

			}
			if (!fromlanding_flag)
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error fetching country codes, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
			Log.i("Exception in GetCountry Code ", "" + e.getMessage());
		}

		return result;

	}

	@SuppressWarnings("null")
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONArray jsonArray;
		JSONObject jobject = null;

		if (null != result) {
			try {
				jsonArray = new JSONArray(result);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						jobject = jsonArray.getJSONObject(i);
						dialingcode = jobject.getString("dialingCode");
						dialingcode = "+" + dialingcode;
						Constant.arr_dialingcode.add(dialingcode);
						String thumbnail = jobject.getString("image");
						Log.i("Country Code", "" + dialingcode);
						ImageDownloader img = new ImageDownloader(dialingcode);
						img.execute(thumbnail);

					}
					if (!fromlanding_flag) {
						if (mDialog != null)
							mDialog.dismiss();

					}
				}

			} catch (Exception e1) {
				try {
					if (!fromlanding_flag) {
						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("error");
						for (int i = 0; i < jArray.length() - 1; i++) {
							JSONObject jObj = jArray.getJSONObject(i);
							String message = jObj.getString("message");
							if (message
									.matches("missing or invalid accessToken")) {
								Constant.logout(mContext, editor, true);
								Log.i("****Country Code Error****", ""
										+ message);
							} else
								Toast.makeText(mContext, message,
										Toast.LENGTH_SHORT).show();
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

	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

		public String country_code;

		public ImageDownloader(String code) {
			country_code = code;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				mIcon = BitmapFactory.decodeStream(in);
				Constant.hashmap_countrycode.put(country_code, mIcon);
				Log.e("Hash Map Size", "" + Constant.hashmap_countrycode.size());
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			if (mhandler != null) {
				mhandler.sendEmptyMessage(2);
			}
		}
	}

}
