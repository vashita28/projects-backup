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

public class AddNewCardTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	public Handler handler;

	public AddNewCardTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
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
				String address = Constant.passengerURL
						+ "ws/v2/passenger/addcard/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Track Driver Request", address);
				HttpGet httpGet = new HttpGet(address);
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
			Log.i("Connection Time out in Add New Card", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Error adding new card, Please try again.",

							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in Add New Card", "" + e.getMessage());
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
		String url = "";

		try {
			jsonObject = new JSONObject(result);
			if (!result.equals("")) {

				url = jsonObject.getString("url");
				if (handler != null) {
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					Message msg = new Message();
					msg.obj = bundle;
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}

		} catch (Exception e) {
			JSONObject jobj;
			try {
				jobj = new JSONObject(result);
				String message = jobj.getString("error");
				if (message.matches("missing or invalid accessToken")) {
					Constant.logout(mContext, editor, true);
					Log.i("****New Card Error****", "" + message);
				} else
					Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
							.show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Log.i("Exception in Add New Card Post Exeute",
						"" + e.getMessage());
			}
			Log.i("Exception in Add New Card Post Exeute", "" + e.getMessage());
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
