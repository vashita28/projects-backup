package com.android.cabapp.model;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class ChipAndPin {
	private static final String TAG = CabPinValidation.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;

	public ChipAndPin(Context context, List<NameValuePair> chipAndPinValuePairs) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		mContext = context;
		this.nameValuePairs = chipAndPinValuePairs;
	}

	public String chipAndPinResponse() {
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.CHIP_AND_PIN);

		String szchipAndPinResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					szchipAndPinResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					if (Constants.isDebug)
						Log.e(TAG, "ChipAndPinResponse:: "
								+ szchipAndPinResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return szchipAndPinResponse;

	}
}
