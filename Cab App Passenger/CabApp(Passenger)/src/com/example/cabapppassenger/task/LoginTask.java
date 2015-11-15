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

import com.example.cabapppassenger.activity.LandingActivity;
import com.example.cabapppassenger.activity.LoginActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MobileNoActivity;
import com.example.cabapppassenger.activity.ResetPasswordActivity;
import com.example.cabapppassenger.activity.SplashActivity;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;
import com.urbanairship.push.PushManager;

public class LoginTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	public boolean terms, rememberme;
	String PREF_NAME = "CabApp_Passenger";
	double latitude, longitude;
	Context mContext;
	ProgressDialog mDialog_login;
	public boolean fromsplash_flag;
	public String email, password, apid;
	public boolean flag_keepmeloged;

	public LoginTask(Context context, ProgressDialog mDialog) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		mDialog_login = mDialog;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (flag_keepmeloged) {
			mDialog_login = showProgressDialog("Logging in");
			mDialog_login.show();
		}
	}

	@Override
	protected String doInBackground(String... urls) {

		apid = PushManager.shared().getAPID();
		Log.i("****APPID*****", "" + apid);

		Location location = Constant.getLocation(mContext);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		getset = new GetterSetter();
		getset.setLatitude(latitude);
		getset.setDeviceToken(apid);
		getset.setLongitude(longitude);
		getset.setEmail(email);
		getset.setPassword(password);

		return POST(urls[0], getset);
	}

	public String POST(String url, GetterSetter login) {
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

				jsonObject.accumulate("username", getset.getEmail());
				jsonObject.accumulate("password", getset.getPassword());
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
				if (mDialog_login != null) {
					mDialog_login.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.d("InputStream", e.getLocalizedMessage());
			} catch (Exception e) {
				if (mDialog_login != null) {
					mDialog_login.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error logging in, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.d("InputStream", e.getLocalizedMessage());
			}

			Log.i("****RESULT****", "" + result);

		} else {
			if (mDialog_login != null)
				mDialog_login.dismiss();
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONObject loginUserjsonObject = null;
		String forename, lastname, email, accessToken, internationalcode, address, mobilenumber, fullname, isForgot;
		if (mDialog_login != null)
			mDialog_login.dismiss();
		if (!result.equals("")) {
			try {
				loginUserjsonObject = new JSONObject(result);
				if (loginUserjsonObject != null) {
					forename = loginUserjsonObject.getString("forename");
					lastname = loginUserjsonObject.getString("surname");
					email = loginUserjsonObject.getString("email");
					accessToken = loginUserjsonObject.getString("accessToken");
					Log.i("AccessToken", accessToken);
					internationalcode = loginUserjsonObject
							.getString("internationalCode");
					address = loginUserjsonObject.getString("address");
					mobilenumber = loginUserjsonObject
							.getString("mobileNumber");
					isForgot = loginUserjsonObject.getString("isForgot");
					// Constant.togglepush();
					fullname = forename + " " + lastname;

					if (fromsplash_flag)
						SplashActivity.finishMe();

					// saving values to preferences
					editor.putString("Email", email);
					Log.e("Password", getset.getPassword());
					editor.putString("Password", getset.getPassword());
					editor.putString("FirstName", forename);
					editor.putString("LastName", lastname);
					editor.putString("AccessToken", accessToken);
					editor.putString("InternationalCode", internationalcode);
					editor.putString("Address", address);
					editor.putString("MobileNo", mobilenumber);
					editor.commit();

					if (isForgot.matches("N")) {
						Intent slider = new Intent(mContext,
								MainFragmentActivity.class);
						slider.putExtra("FullName", fullname);
						mContext.startActivity(slider);
					} else if (isForgot.matches("Y")) {
						Intent slider = new Intent(mContext,
								ResetPasswordActivity.class);
						slider.putExtra("Email", email);
						mContext.startActivity(slider);
					}
					LoginActivity.finishMe();

				}

			} catch (Exception e1) {
				// PushManager.disablePush();
				// PushManager.shared().setNotificationBuilder(null);
				try {
					if (fromsplash_flag) {
						fromsplash_flag = false;
						Intent login = new Intent(mContext,
								LandingActivity.class);
						mContext.startActivity(login);
					} else {
						LoginActivity.et_password.setText("");
					}
					// {"errors":[{"type":"ValidationException","message":"Your Mobile number is not verified"}],"clientkey":"8D792B394DE23600F8C7AC3033C439C46E8EC848"}

					JSONObject jobj = new JSONObject(result);
					if (result != null) {
						JSONArray jArray = jobj.getJSONArray("errors");
						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						if (jobj.has("clientkey")) {
							String clientkey = jobj.getString("clientkey");
							editor.putString("ClientKey", clientkey);
							editor.commit();
						}
						if (message
								.equals("Your Mobile number is not verified")) {
							LoginActivity.finishMe();
							SplashActivity.finishMe();
							Intent mobileno = new Intent(mContext,
									MobileNoActivity.class);
							mobileno.putExtra("From_Login", true);
							mContext.startActivity(mobileno);
							return;

						}
						if (message.matches("missing or invalid accessToken")) {
							Constant.logout(mContext, editor, true);
							Log.i("****Login Error****", "" + message);
						} else
							Toast.makeText(mContext, message,
									Toast.LENGTH_SHORT).show();
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
}
