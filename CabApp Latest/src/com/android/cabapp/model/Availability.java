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

public class Availability {

	private static final String TAG = Availability.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	boolean isAvailable;

	public Availability(Context context, boolean isAvailable) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.isAvailable = isAvailable;
	}

	public String IsAvailabilty() {

		if (mContext != null)
			nameValuePairs.add(new BasicNameValuePair("data[availability]",
					String.valueOf(isAvailable)));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.AVAILABLE);

		String availableResponse = "";

		if (connection.mInputStream != null) {
			try {
				availableResponse = connection.inputStreamToString(
						connection.mInputStream).toString();
				Log.e(TAG,
						"JSONObject  AVAILABLE::> "
								+ availableResponse.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return availableResponse;

	}
}
