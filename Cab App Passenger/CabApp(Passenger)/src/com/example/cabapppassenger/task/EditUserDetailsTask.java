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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;

public class EditUserDetailsTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	public Handler handler;
	public String firstname, lastname, wheelchair, payingmethod,
			hearingimpaired, truncatedPan;
	String result = "";
	public boolean hit_resendtask;
	ProgressDialog mDialog;

	public EditUserDetailsTask(Context context) {
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

				jsonObject.put("firstname", firstname);
				jsonObject.put("surname", lastname);
				jsonObject.put("email", shared_pref.getString("Email", ""));
				jsonObject.put("mobileNumber",
						shared_pref.getString("MobileNo", ""));
				jsonObject.put("internationalCode",
						shared_pref.getString("InternationalCode", ""));
				jsonObject.put("address", shared_pref.getString("Address", ""));
				jsonObject.put("country", shared_pref.getString("Country", ""));

				Log.e("Edit User Details", payingmethod);
				jsonObject.put("wheelchairAccessRequired", wheelchair);
				jsonObject.put("isHearingImpaired", hearingimpaired);
				jsonObject.put("preferredPayingMethod", payingmethod);

				jsonObject.put("preferredCard", truncatedPan);
				json = jsonObject.toString();

				Log.d("Request of edit user details :: ", jsonObject.toString());
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", json));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				httpPost.addHeader("Content-Type",
						"application/x-www-form-urlencoded");
				Log.e("Response of edit user details ::", "JSON  DATA is :: "
						+ json.toString());

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
				Log.i("Connection Timeout in Edit user details ",
						"" + e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error updating your  details, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in Edit user details ", "" + e.getMessage());
			}

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
		JSONObject Juserdetails_obj = null;
		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {

			try {
				Juserdetails_obj = new JSONObject(result);
				boolean response = Juserdetails_obj.getBoolean("success");
				if (response) {
					editor.putString("FirstName", firstname);
					editor.putString("LastName", lastname);
					editor.putString("WheelChair", wheelchair);
					editor.putString("HearingImpaired", hearingimpaired);
					if (payingmethod.equals("cash and card"))
						editor.putString("PayingMethod", "Card");
					else
						editor.putString("PayingMethod", "Cash");

					editor.putString("truncatedPan", truncatedPan);
					editor.commit();

					Log.e("Edit User Details in post execute", payingmethod);
					if (handler != null && hit_resendtask)
						handler.sendEmptyMessage(1);
					else
						handler.sendEmptyMessage(0);
					Toast.makeText(mContext, "Changes saved successfully.",
							Toast.LENGTH_SHORT).show();

				} else
					Toast.makeText(mContext,
							"Error updating your details, Please try again.",
							Toast.LENGTH_SHORT).show();

			} catch (Exception e1) {
				try {
					if (result != null) {
						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("errors");
						for (int i = 0; i <= jArray.length() - 1; i++) {
							JSONObject jObj = jArray.getJSONObject(i);
							String message = jObj.getString("message");
							if (message
									.matches("missing or invalid accessToken")) {
								Constant.logout(mContext, editor, true);
								Log.i("****Edit User Details Error****", ""
										+ message);
							} else
								Toast.makeText(mContext, message,
										Toast.LENGTH_SHORT).show();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in Edit user details Post execute",
							"" + e.getMessage());
				}

				Log.i("Exception in Edit user details Post execute ",
						"" + e1.getMessage());
			}
		}
	}

	// private void updatevalues(String firstname, String lastname,
	// String wheelchairAccessRequired, String isHearingImpaired,
	// String preferredPayingMethod) {
	// // TODO Auto-generated method stub
	// Message msg = new Message();
	// Bundle bundle = new Bundle();
	// bundle.putString("Firstname", firstname);
	// bundle.putString("Lastname", lastname);
	// bundle.putString("WheelChair", wheelchairAccessRequired);
	// bundle.putString("Hearing_impaired", isHearingImpaired);
	// bundle.putString("Paying_method", preferredPayingMethod);
	//
	// msg.obj = bundle;
	// msg.setData(bundle);
	// handler.sendMessage(msg);
	//
	// }

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
