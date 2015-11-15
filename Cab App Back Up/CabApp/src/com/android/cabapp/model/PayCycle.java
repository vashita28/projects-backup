package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class PayCycle {
	private static final String TAG = PayCycle.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;

	public PayCycle(Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public String getPayCycle() {
		connection.connect(Constants.PAYCYCLE);

		String payCycleResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					payCycleResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return payCycleResponse;
	}

}
