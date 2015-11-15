package com.android.cabapp.async;

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
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.cabapp.activity.AddScanCardJobsActivity;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.google.android.gms.internal.jb;

public class AddNewCardTask extends AsyncTask<String, Void, String> {

	Context mContext;
	// Editor editor;
	// SharedPreferences shared_pref;
	String PREF_NAME = "CabAppDriver";
	public Handler handler;
	public String truncatedPan, token, cardType, cashBack = "";
	public ProgressDialog mDialog;

	public AddNewCardTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		// shared_pref = mContext.getSharedPreferences(PREF_NAME,
		// Context.MODE_PRIVATE);
		// editor = shared_pref.edit();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		// TODO Auto-generated method stub
		if (mDialog == null) {
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
		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			try {

				String address = Constants.driverURL + "addcard/?accessToken="
						+ Util.getAccessToken(mContext);
				Log.e("Add New Card Request", address);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpPost httpPost = new HttpPost(address);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				String json = "";

				JSONObject jsonObject = new JSONObject();

				jsonObject.accumulate("token", token);
				jsonObject.accumulate("cardType", cardType);
				jsonObject.accumulate("truncatedPan", truncatedPan);

				json = jsonObject.toString();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", json));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				httpPost.addHeader("Content-Type",
						"application/x-www-form-urlencoded");
				Log.e("POST", "JSON  DATA is :: " + json.toString());
				String response = json.toString();
				try {
					JSONObject jObject = new JSONObject(response);
					if (jObject.has("token"))
						AddScanCardJobsActivity.tokenReturned = jObject
								.getString("token").toString();
					if (jObject.has("cardType"))
						AddScanCardJobsActivity.cardTypeReturned = jObject
								.getString("cardType");
					if (jObject.has("cashbackValue")) {
						cashBack = jObject.getString("cashbackValue");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				HttpResponse httpResponse = httpclient.execute(httpPost);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null){
					result = convertInputStreamToString(inputStream);
					
					 DriverAccountDetails driverAccount = new DriverAccountDetails(
					 mContext);
					 driverAccount.retriveAccountDetails(mContext);
				}
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
				Log.i("Connection Time out in EditBooking task",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error while editing your booking, Please try again.",
								Toast.LENGTH_SHORT).show();
					}

				});
				Log.i("Exception in EditBooking Task", "" + e.getMessage());

				Log.i("****RESULT****", "" + result);
			}

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
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(result);
			boolean success = jsonObject.getBoolean("success");
			// DriverAccountDetails driverAccount = new DriverAccountDetails(
			// mContext);
			// driverAccount.retriveAccountDetails(mContext);

			// Update cashback:
			Util.setCashBack(Util.mContext, cashBack);

			if (success && handler != null) {
				handler.sendEmptyMessage(1);
			} else
				handler.sendEmptyMessage(0);// failed
		} catch (Exception e) {
			JSONObject jobj;
			try {
				jobj = new JSONObject(result);
				String message = jobj.getString("error");
				if (message.matches("missing or invalid accessToken")) {
					// Constant.logout(mContext, editor, true);
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
