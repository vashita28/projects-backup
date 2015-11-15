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
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.activity.VerificationActivity;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;
import com.urbanairship.push.PushManager;

public class SignupTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	public boolean terms, rememberme;
	String PREF_NAME = "CabApp_Passenger";
	double latitude, longitude;
	static Context mContext;
	public Handler mHandler;
	public boolean flag_keepmeloged;
	ProgressDialog mDialog;
	boolean check_email;

	public String email, password, city, firstname, lastname, mobile_no,
			international_code, country, apid;

	public SignupTask(Context context, ProgressDialog dialog,
			boolean check_email) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		mDialog = dialog;
		this.check_email = check_email;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (flag_keepmeloged) {
			mDialog = showProgressDialog("Signing in");
			mDialog.show();
		}
	}

	@Override
	protected String doInBackground(String... urls) {

		apid = PushManager.shared().getAPID();
		// if (apid == null)
		// apid = "12345";
		Log.i("****APPID*****", "" + apid);

		Location location = Constant.getLocation(mContext);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		getset = new GetterSetter();
		getset.setFirstname(firstname);
		getset.setLastname(lastname);
		getset.setCity(city);
		getset.setEmail(email);
		getset.setPassword(password);
		getset.setMobile_no(mobile_no);
		getset.setDeviceToken(apid);
		getset.setLatitude(latitude);
		getset.setLongitude(longitude);
		getset.setCountry(country);
		getset.setInternational_code(international_code);

		return POST(urls[0]);
	}

	public String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout
				HttpPost httpPost = new HttpPost(url);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				String json = "";

				JSONObject jsonObject = new JSONObject();

				jsonObject.put("email", getset.getEmail());
				jsonObject.put("password", getset.getPassword());
				jsonObject.put("deviceToken", getset.getDeviceToken());
				jsonObject.put("deviceType", "android");
				jsonObject.put("surname", getset.getLastname());
				jsonObject.put("address", getset.getCity());
				jsonObject.put("firstname", getset.getFirstname());
				jsonObject.put("internationalCode",
						getset.getInternational_code());
				jsonObject.put("latitude", getset.getLatitude());
				jsonObject.put("longitude", getset.getLongitude());
				jsonObject.put("mobileNumber", getset.getMobile_no());
				jsonObject.put("location", getset.getCountry());
				Log.e("Signup", jsonObject.toString());
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
				Log.i("Connection Timeout in Signup", "" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error signing up, Please try again.",

								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Signup Task", "" + e.getMessage());
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
		JSONObject signupUserjsonObject = null;
		String clientKey, code;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			try {
				signupUserjsonObject = new JSONObject(result);
				if (signupUserjsonObject != null) {
					clientKey = signupUserjsonObject.getString("clientKey");
					code = signupUserjsonObject.getString("code");
					Log.e("CODE", "" + code);
					// if (mHandler != null) {
					// mHandler.sendEmptyMessage(0);
					// } else {
					Intent slider = new Intent(mContext,
							VerificationActivity.class);
					slider.putExtra("mobileno", mobile_no);
					slider.putExtra("international_code", international_code);
					slider.putExtra("FromSignup", true);
					// Constant.togglepush();
					mContext.startActivity(slider);
					editor.putString("ClientKey", clientKey);
					editor.commit();
					// }
				}
			} catch (Exception e1) {
				// PushManager.disablePush();
				// PushManager.shared().setNotificationBuilder(null);
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						if (check_email) {
							if (message.equals("email address already exist")
									|| message.equals("invalid email")) {
								Toast.makeText(mContext, message,
										Toast.LENGTH_SHORT).show();
								return;
							} else {
								if (mHandler != null)
									mHandler.sendEmptyMessage(0);
								return;
							}

						} else {
							if (message
									.matches("missing or invalid accessToken")) {
								Constant.logout(mContext, editor, true);
								Log.i("****Signup  Error****", "" + message);
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

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}
}
