package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class LogOut {
	private static final String TAG = LogOut.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;

	public LogOut(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public String doLogOut() {

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.LOGOUT);

		String logOutResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					logOutResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					if (Constants.isDebug)
						Log.e(TAG,
								"JSONObject Logout:: "
										+ logOutResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return logOutResponse;

	}

}
