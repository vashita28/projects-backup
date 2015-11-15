package com.android.cabapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class AddCard {
	private static final String TAG = AddCard.class.getSimpleName();

	Connection connection;
	Context mContext;

	public AddCard(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
	}

	public String getAddCardURL() {

		connection.connect(Constants.ADD_CARD);

		String szResponse = "";
		if (connection.mInputStream != null) {
			try {
				szResponse = connection
						.inputStreamToString(connection.mInputStream);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return szResponse;

	}
}
