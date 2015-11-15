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

public class CabPinValidation {
	private static final String TAG = CabPinValidation.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String szCabPin, szJobId;

	public CabPinValidation(Context context, String cabPin, String jobID) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		mContext = context;
		this.szCabPin = cabPin;
		this.szJobId = jobID;
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public String isCabPinValidResponse() {
		nameValuePairs.add(new BasicNameValuePair("data[pin]", szCabPin));
		nameValuePairs.add(new BasicNameValuePair("data[bookingId]", szJobId));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.CAB_PIN_VALIDATION);

		String cabpinValidationResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					cabpinValidationResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					if (Constants.isDebug)
						Log.e(TAG, "JSONObject  cabpinValidationResponse::> "
								+ cabpinValidationResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cabpinValidationResponse;

	}

}
