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

public class UserAuthorisation {

	private static final String TAG = UserAuthorisation.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String eMail;

	// boolean isAvailable;

	public UserAuthorisation(Context context, String Email) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.eMail = Email;
	}

	public String IsUserAuthorised() {

		if (mContext != null)
			nameValuePairs.add(new BasicNameValuePair("data[email]", eMail));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.USER_AUTHORISATION);

		String authorisationResponse = "";

		if (connection.mInputStream != null) {
			try {
				authorisationResponse = connection.inputStreamToString(
						connection.mInputStream).toString();
				Log.e(TAG, "JSONObject  IS_AUTHORISE::> "
						+ authorisationResponse.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return authorisationResponse;

	}
}
