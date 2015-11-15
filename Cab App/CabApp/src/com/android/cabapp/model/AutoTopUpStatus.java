package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class AutoTopUpStatus {

	private static final String TAG = AutoTopUpStatus.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	int autoTopUp;

	// boolean isAvailable;

	public AutoTopUpStatus(Context context, int iAutoTopUP) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.autoTopUp = iAutoTopUP;
	}

	public String UpdateAutoTopUp() {

		if (mContext != null)
			if (autoTopUp == 1)
				nameValuePairs.add(new BasicNameValuePair("data[autoTopup]",
						"1"));
			else
				nameValuePairs.add(new BasicNameValuePair("data[autoTopup]",
						"0"));
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.AUTO_TOP_UP_STATUS);

		String autoTopUpResponse = "";

		if (Constants.isDebug)
			Log.e(TAG, "autoTopUpStatus:>nameValuePairs " + nameValuePairs);

		if (connection.mInputStream != null) {
			try {
				autoTopUpResponse = connection.inputStreamToString(
						connection.mInputStream).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return autoTopUpResponse;

	}

}
