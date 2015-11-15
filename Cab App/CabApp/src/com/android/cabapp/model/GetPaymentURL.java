package com.android.cabapp.model;

import java.io.IOException;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class GetPaymentURL {
	Connection connection;

	public GetPaymentURL(String szURL, Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		connection.paymentURL = szURL;
	}

	// public Payment getPayment() {
	// Payment payment = null;
	// connection.connect(Constants.PAYMENT);
	//
	// InputStream inputStream = connection.mInputStream;
	//
	// if (connection.mInputStream != null) {
	// try {
	// Log.e("GetPayment",
	// "Response is:: "
	// + connection.inputStreamToString(inputStream));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Gson gson = new Gson();
	// Reader reader = new InputStreamReader(connection.mInputStream);
	// payment = gson.fromJson(reader,
	// com.android.cabapp.datastruct.json.Payment.class);
	// }
	// return payment;
	// }

	public String getPaymentResponse() {
		connection.connect(Constants.PAYMENT);
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
