package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.GetterSetter;

public class GetUserDetailsTask extends AsyncTask<String, Void, String> {

	GetterSetter getset;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Context mContext;
	String result = null;
	public Handler handler;
	ProgressDialog mDialog;
	public TextView textview_username, textview_payingmethod;

	public GetUserDetailsTask(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		// editor.putString("PayingMethod", "Cash");
		// editor.commit();

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
		String result = "";
		InputStream inputStream = null;
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpGet request = new HttpGet(Constant.passengerURL
						+ "ws/v2/passenger/account/?accessToken="
						+ shared_pref.getString("AccessToken", ""));
				String url = Constant.passengerURL
						+ "ws/v2/passenger/account/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.i("User details request", url);
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = client.execute(httpGet);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

				Log.e("User details response", result);
			} catch (ClientProtocolException e1) {

				e1.printStackTrace();

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
				Log.i("Connection Time out exception in GetUserDetails ", ""
						+ e.getMessage());
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error getting user details, Please try again.",

								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Exception in GetUserDetails ", "" + e.getMessage());
			}

		} else {
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		JSONObject Juserdetails_obj = null;
		String firstname, lastname, email, internationalcode, mobileno, address, country, wheelchairAccessRequired, isHearingImpaired, preferredPayingMethod, preferredCardTruncatedPan = "";
		if (mDialog != null)
			mDialog.dismiss();
		if (null != result) {
			Constant.flag_added_newcard = false;
			try {
				Juserdetails_obj = new JSONObject(result);
				firstname = Juserdetails_obj.getString("firstname");
				lastname = Juserdetails_obj.getString("surname");
				email = Juserdetails_obj.getString("email");
				internationalcode = Juserdetails_obj
						.getString("internationalCode");
				mobileno = Juserdetails_obj.getString("mobileNumber");
				address = Juserdetails_obj.getString("address");
				country = Juserdetails_obj.getString("country");
				wheelchairAccessRequired = Juserdetails_obj
						.getString("wheelchairAccessRequired");
				isHearingImpaired = Juserdetails_obj
						.getString("isHearingImpaired");
				preferredPayingMethod = Juserdetails_obj
						.getString("preferredPayingMethod");

				JSONArray jCard_ar = Juserdetails_obj.getJSONArray("cards");
				if (jCard_ar != null && jCard_ar.length() > 0) {
					Log.e("Card Array Length", "" + jCard_ar.length());
					ArrayList<ArrayList<String>> arr_usercard = new ArrayList<ArrayList<String>>();
					for (int i = 0; i < jCard_ar.length(); i++) {
						ArrayList<String> temp = new ArrayList<String>();
						JSONObject jobj = jCard_ar.getJSONObject(i);
						temp.add(jobj.getString("passengerCardId"));
						temp.add(jobj.getString("truncatedPan"));
						if (jobj.getString("preferredCard").trim()
								.equalsIgnoreCase("Y"))
							preferredCardTruncatedPan = jobj
									.getString("truncatedPan");
						temp.add(jobj.getString("brand"));
						arr_usercard.add(temp);
					}
					Constant.hashmap_passengercard.put(Constant.USER_CARD_KEY,
							arr_usercard);
				}

				editor.putString("FirstName", firstname);
				editor.putString("LastName", lastname);
				editor.putString("Email", email);
				editor.putString("InternationalCode", internationalcode);
				editor.putString("MobileNo", mobileno);
				editor.putString("Address", address);
				editor.putString("Country", country);
				editor.putString("WheelChair", wheelchairAccessRequired);
				editor.putString("HearingImpaired", isHearingImpaired);
				editor.putString("truncatedPan", preferredCardTruncatedPan);
				if (preferredPayingMethod.equals("cash and card"))
					editor.putString("PayingMethod", "Card");
				else
					editor.putString("PayingMethod", "Cash");
				editor.commit();
				Log.e("Country", country);
				((MainFragmentActivity) mContext).onsetdata();
				if (firstname != null && lastname != null
						&& !firstname.matches("null")
						&& !lastname.matches("null"))
					if (textview_username != null)
						textview_username.setText(firstname + " " + lastname);

				// if (preferredPayingMethod != null
				// && !preferredPayingMethod.matches("null")
				// && !preferredPayingMethod.matches(""))
				// if (textview_payingmethod != null)
				// textview_payingmethod.setText(preferredPayingMethod);

				if (handler != null) {
					handler.sendEmptyMessage(0);
				}
			} catch (Exception e1) {
				try {
					if (result != null) {
						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("errors");
						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						if (message.matches("missing or invalid accessToken")) {
							Constant.logout(mContext, editor, true);
							Log.i("****Get User Details Error****", ""
									+ message);
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
