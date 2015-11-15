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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.activity.ForgotPasswordActivity;
import com.example.cabapppassenger.activity.LoginActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.ResetPasswordActivity;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;
import com.urbanairship.push.PushManager;

public class ChangePasswordTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	double latitude, longitude;
	ProgressDialog mDialog;
	public boolean flag_fromresetpassword, flag_fromloginscreen;
	public String email, oldpassword, newpassword, apid;

	public ChangePasswordTask(Context context) {
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

		apid = PushManager.shared().getAPID();
		Log.i("****APPID*****", "" + apid);
		getset = new GetterSetter();
		Location location = Constant.getLocation(mContext);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		getset = new GetterSetter();

		getset.setLatitude(latitude);
		getset.setLongitude(longitude);
		getset.setEmail(email);
		getset.setDeviceToken(apid);
		getset.setOldpassword(oldpassword);
		getset.setNewpassword(newpassword);

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

				JSONObject jsonObject = new JSONObject();

				jsonObject.accumulate("email", getset.getEmail());
				jsonObject.accumulate("oldPassword", getset.getOldpassword());
				jsonObject.accumulate("newPassword", getset.getNewpassword());
				jsonObject.accumulate("deviceToken", getset.getDeviceToken());
				jsonObject.accumulate("deviceType", "android");
				jsonObject.accumulate("latitude", getset.getLatitude());
				jsonObject.accumulate("longitude", getset.getLongitude());

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
				Log.i("Connection Timeout in Change Password",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Unable to save details, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Change Password", "" + e.getMessage());
			}

			Log.i("****RESULT****", "" + result);
		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
			if (mDialog != null)
				mDialog.dismiss();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONObject verificationjson_obj = null;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			try {
				verificationjson_obj = new JSONObject(result);
				if (verificationjson_obj != null) {
					boolean success = verificationjson_obj
							.getBoolean("success");
					if (success) {
						Toast.makeText(mContext,
								"Password Changed Successfully.",
								Toast.LENGTH_SHORT).show();
						if (flag_fromresetpassword) {
							LoginActivity.finishMe();
							Intent reset = new Intent(mContext,
									LoginActivity.class);
							reset.putExtra("Email", getset.getEmail());
							mContext.startActivity(reset);
							ForgotPasswordActivity.finishMe();
							ResetPasswordActivity.finishMe();

						} else {
							Intent reset = new Intent(mContext,
									MainFragmentActivity.class);
							mContext.startActivity(reset);
							LoginActivity.finishMe();
							ForgotPasswordActivity.finishMe();
							ResetPasswordActivity.finishMe();
						}

					}
				}

			} catch (Exception e1) {
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Change Password Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in Change Password Post execute",
							"" + e1.getMessage());
				}

				Log.i("Exception in Change Password Post execute",
						"" + e1.getMessage());
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
}
