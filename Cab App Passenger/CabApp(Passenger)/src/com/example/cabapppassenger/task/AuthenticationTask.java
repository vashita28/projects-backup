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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.activity.LandingActivity;
import com.example.cabapppassenger.activity.MobileNoActivity;
import com.example.cabapppassenger.activity.SignupActivity;
import com.example.cabapppassenger.activity.VerificationActivity;
import com.example.cabapppassenger.activity.ViewTutorialActivity;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;

public class AuthenticationTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	ProgressDialog mDialog;
	public String clientkey;

	public AuthenticationTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mDialog = showProgressDialog("Authenticating");
		mDialog.show();
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {

		getset = new GetterSetter();
		getset.setClientkey(clientkey);

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
				JSONObject dataobj = new JSONObject();

				JSONObject jsonObject = new JSONObject();

				jsonObject.accumulate("clientKey", getset.getClientkey());
				jsonObject.accumulate("deviceType", "android");
				jsonObject.accumulate("deviceToken", "test123");

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
				Log.i("Connection Time out in Authentication task",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Error authenticating user, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Authentication Task", "" + e.getMessage());
			}

			Log.i("****RESULT****", "" + result);
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
		super.onPostExecute(result);
		JSONObject autthentication_Jobj = null;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			try {
				autthentication_Jobj = new JSONObject(result);
				if (autthentication_Jobj != null) {
					boolean success = autthentication_Jobj
							.getBoolean("success");
					String accessToken = autthentication_Jobj
							.getString("accessToken");
					Log.i("Access Token", accessToken);
					editor.putString("AccessToken", accessToken);
					editor.commit();
					Intent tutorial = new Intent(mContext,
							ViewTutorialActivity.class);
					mContext.startActivity(tutorial);
					LandingActivity.finishMe();
					SignupActivity.finishMe();
					MobileNoActivity.finishMe();
					VerificationActivity.finishMe();
				}

			} catch (Exception e1) {
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Authentication Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in Authentication Post Exeute",
							"" + e1.getMessage());
				}

				Log.i("Exception in Authentication Post Exeute",
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
