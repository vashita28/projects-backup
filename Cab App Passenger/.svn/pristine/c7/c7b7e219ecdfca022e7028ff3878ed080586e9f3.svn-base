/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class GetBookingsTask extends AsyncTask<String, Void, String> {

	protected DefaultHttpClient client;
	HttpParams httpParams = new BasicHttpParams();
	String url, logInResponse;
	Handler bookingHandler;
	public InputStream mInputStream;
	String PREF_NAME = "CabApp_Passenger";
	Editor editor;
	SharedPreferences shared_pref;
	Context mContext;
	boolean flag_slider = false;
	ProgressDialog mDialog;

	public GetBookingsTask(Context context, String url, Handler bookingHandler,
			boolean flag_slider) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.bookingHandler = bookingHandler;
		this.url = url;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		this.flag_slider = flag_slider;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (!flag_slider) {
			mDialog = showProgressDialog("Loading");
			mDialog.show();
		}

	}

	@Override
	protected String doInBackground(String... urls) {
		return POST();
	}

	public String POST() {

		String result = "";
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {

				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);// timeout
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				client = new DefaultHttpClient(httpParams);

				getRequest(url);
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error in booking, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				// Log.d("InputStream", e.getLocalizedMessage());
			}

			Log.i(getClass().getSimpleName(), "" + logInResponse);

		} else {
			if (mDialog != null)
				mDialog.dismiss();
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext, "No network connection",
							Toast.LENGTH_SHORT).show();
				}
			});

		}
		return logInResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (mDialog != null)
			mDialog.dismiss();
		if (null != result) {
			try {
				Log.e("My bookings Result", result);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("bookingsData", result);
				msg.obj = bundle;
				msg.setData(bundle);
				bookingHandler.sendMessage(msg);

			} catch (Exception e1) {

				try {
					if (result != null) {

						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("errors");
						// for (int i = 0; i <= jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						if (message.matches("missing or invalid accessToken")) {
							Constant.logout(mContext, editor, true);
							Log.i("****Get Bookings Error****", "" + message);
						} else
							Toast.makeText(mContext, message,
									Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (result != null)
						Toast.makeText(mContext,
								"Error getting cabs, Please try again.",
								Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void executeRequest(HttpPost post, HttpGet get) {
		try {
			HttpResponse httpResponse;
			try {
				if (get == null)
					httpResponse = client.execute(post);
				else
					httpResponse = client.execute(get);

				Log.w(getClass().getSimpleName(), "executeRequest::URL:: "
						+ url);
				final int statusCode = httpResponse.getStatusLine()
						.getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w(getClass().getSimpleName(), "Error " + statusCode
							+ " for URL " + url);
					mInputStream = null;
					return;
				}

				HttpEntity getResponseEntity = httpResponse.getEntity();
				mInputStream = getResponseEntity.getContent();
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
			}
			if (mInputStream != null) {
				try {
					{
						logInResponse = convertInputStreamToString(mInputStream)
								.toString();

						Log.e(getClass().getSimpleName(), "JSONObject  LogIn"
								+ logInResponse);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			if (post != null)
				post.abort();
			else
				get.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
			mInputStream = null;
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

	public void getRequest(String url) {
		// getRequest.addHeader("Content-Type",
		// "application/json; charset=utf-8"); // addHeader()
		this.url = url;
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// httpGet.addHeader("Content-Type", "application/json");
		executeRequest(null, httpGet);
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}
