package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.push.PushManager;

public class ForgotPassword {

	private static final String TAG = ForgotPassword.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String szEmail, szOldPassword, szNewPassword, szMD5OldPassword,
			szMD5NewPassword;
	Location currentLocation = null;

	public ForgotPassword(Context context, String sEmail) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.szEmail = sEmail;
	}

	public ForgotPassword(Context context, String sEmail, String sOldPassword,
			String sNewPassword) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.szEmail = sEmail;
		this.szOldPassword = sOldPassword;
		this.szNewPassword = sNewPassword;
	}

	public String isForgotPassword() {
		nameValuePairs.add(new BasicNameValuePair("data[email]", szEmail));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.FORGOT_PASSWORD);

		String response = "";

		if (connection.mInputStream != null) {
			try {
				{
					response = connection.inputStreamToString(
							connection.mInputStream).toString();
					Log.e(TAG, "ForgotPassword " + response);
					JSONObject jObject = new JSONObject(response);

					if (jObject.has("error")) {
						response = jObject.getString("message");
						return response;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return response;

	}

	public String ChangePassword() {
		String apid = PushManager.shared().getAPID();

		if (mContext != null)
			currentLocation = Util.getLocation(mContext);

		Log.e(TAG, "LogIn LoginCredentials " + apid);
		// try {
		// Log.e(TAG, "password::OLD(From Email) " + oldPassword);
		// md5OldPassword = Util.md5(oldPassword);
		// Log.e(TAG, "password::NEW " + newPassword);
		// md5NewPassword = Util.md5(newPassword);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		if (currentLocation != null && !szOldPassword.isEmpty()
				&& !szNewPassword.isEmpty()) {

			String lat = String.valueOf(currentLocation.getLatitude());
			String lng = String.valueOf(currentLocation.getLongitude());
			Log.e("current Location", "lat lng" + lat + "   " + lng);

			nameValuePairs.add(new BasicNameValuePair("data[email]", szEmail));
			nameValuePairs.add(new BasicNameValuePair("data[oldPassword]",
					szOldPassword));
			nameValuePairs.add(new BasicNameValuePair("data[newPassword]",
					szNewPassword));
			nameValuePairs.add(new BasicNameValuePair("data[currentLatitude]",
					lat));
			nameValuePairs.add(new BasicNameValuePair("data[currentLongitude]",
					lng));
			nameValuePairs.add(new BasicNameValuePair("data[deviceType]",
					"android"));
			nameValuePairs
					.add(new BasicNameValuePair("data[deviceToken]", apid));

		}
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.CHANGE_PASSWORD);

		String response = "";

		if (connection.mInputStream != null) {
			try {
				{
					response = connection.inputStreamToString(
							connection.mInputStream).toString();
					Log.e(TAG, "ChangePassword Result::> " + response);
					JSONObject jObject = new JSONObject(response);

					if (jObject.has("error")) {
						response = jObject.getString("message");
						return response;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return response;

	}

}
