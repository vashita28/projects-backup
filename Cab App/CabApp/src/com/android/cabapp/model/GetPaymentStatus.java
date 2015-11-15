package com.android.cabapp.model;

import java.io.IOException;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class GetPaymentStatus {
	Connection connection;

	public GetPaymentStatus(Context context, String szJobId) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		connection.jobId = szJobId;
	}

	public String getPaymentStatus() {
		connection.connect(Constants.PAYMENT_STATUS);
		String szResponse = "";
		if (connection.mInputStream != null) {
			try {
				szResponse = connection
						.inputStreamToString(connection.mInputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return szResponse;
	}

}
