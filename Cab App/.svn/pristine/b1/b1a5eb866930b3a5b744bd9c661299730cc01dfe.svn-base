package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.push.PushManager;

public class LogIn {
	private static final String TAG = LogIn.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String username, password, md5Password;
	Location currentLocation = null;
	public boolean bIsLogout = false;

	public LogIn(Context context, String sUsername, String sPassword) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.username = sUsername;
		this.password = sPassword;
	}

	public String LoginCredentials() {
		String apid = PushManager.shared().getAPID();

		if (mContext != null)
			currentLocation = Util.getLocation(mContext);

		if (Constants.isDebug)
			Log.e(TAG, "LogIn LoginCredentials APID: " + apid);
		// try {
		// Log.e(TAG, "password:: " + password);
		// md5Password = Util.md5(password);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String lat = "", lng = "";
		if (currentLocation != null) {
			lat = String.valueOf(currentLocation.getLatitude());
			lng = String.valueOf(currentLocation.getLongitude());
			if (Constants.isDebug)
				Log.e("current Location", "lat lng" + lat + "   " + lng);
		} else { // for using location in emulator
			lat = "18.99026";
			lng = "72.82088";
		}

		nameValuePairs
				.add(new BasicNameValuePair("data[currentLatitude]", lat));
		nameValuePairs
				.add(new BasicNameValuePair("data[currentLongitude]", lng));
		nameValuePairs.add(new BasicNameValuePair("data[username]", username));
		nameValuePairs.add(new BasicNameValuePair("data[password]", password));
		nameValuePairs
				.add(new BasicNameValuePair("data[deviceType]", "android"));

		if (bIsLogout)
			nameValuePairs.add(new BasicNameValuePair("data[deviceToken]",
					"NOAPID"));
		else
			nameValuePairs
					.add(new BasicNameValuePair("data[deviceToken]", apid));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.LOGIN);

		String logInResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					logInResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					if (Constants.isDebug)
						Log.e(TAG,
								"JSONObject  LogIn" + logInResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return logInResponse;

	}

}
