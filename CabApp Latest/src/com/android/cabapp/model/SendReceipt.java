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

public class SendReceipt {
	private static final String TAG = SendReceipt.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String szJobId = "", szSendingType = "", szMobileNumber = "", szInterCode = "", pickup = "", dropOff = "", email = "",
			szAmount = "0", szTip = "0", szFee = "0", szPaymentType = "", currency = "", currencyCode = "";

	public SendReceipt(Context context, String szMobileNumber, String szInternationalCode, String szPickUp, String szDropOff,
			String szEmail, String szSendingMode, String amount, String paymentType, String tip, String currency,
			String currencyCode) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.szMobileNumber = szMobileNumber;
		this.szInterCode = szInternationalCode;
		this.pickup = szPickUp;
		this.dropOff = szDropOff;
		this.email = szEmail;
		this.szSendingType = szSendingMode;
		this.szAmount = amount;
		this.szPaymentType = paymentType;
		this.szTip = tip;
		this.currency = currency;
		this.currencyCode = currencyCode;
	}

	public SendReceipt(Context context, String jobID, String fees, String sendingType, String amount, String tip, String email,
			String mobileNumber, String currency, String currencyCode) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.szJobId = jobID;
		this.szFee = fees;
		this.szSendingType = sendingType;
		this.szAmount = amount;
		this.szTip = tip;
		this.szMobileNumber = mobileNumber;
		this.email = email;
		this.currency = currency;
		this.currencyCode = currencyCode;
	}

	public String sendingReceipt() {

		if (szJobId != null && !szJobId.isEmpty()) {
			if (Constants.isDebug)
				Log.e(TAG, "Booking id Exist");
			// if jobid exist:
			// data[bookingId] = 1
			// data[type] = sms/email/none
			// data[amount] = 100
			// data[tip] = 100
			// data[fee]

			nameValuePairs.add(new BasicNameValuePair("data[bookingId]", szJobId));
			nameValuePairs.add(new BasicNameValuePair("data[fee]", szFee));
		} else {
			if (Constants.isDebug)
				Log.e(TAG, "Booking id Not Exist");
			// if jobid doesnt not exist:
			// data[mobile] = 9999999999 (if type is sms)
			// data[internationalCode] = 91(if type is sms)
			// data[pu] = Pune
			// data[do] = Mumbai
			// data[email] = abc@abc.com (if type is email)
			// data[type] = sms/email/none
			// data[amount] = 100
			// data[paymentType] = card / cash
			// data[tip] = 20

			nameValuePairs.add(new BasicNameValuePair("data[pu]", pickup));
			nameValuePairs.add(new BasicNameValuePair("data[do]", dropOff));
			nameValuePairs.add(new BasicNameValuePair("data[paymentType]", szPaymentType));
		}
		if (szSendingType.equals("sms")) {
			nameValuePairs.add(new BasicNameValuePair("data[mobile]", szMobileNumber));
			nameValuePairs.add(new BasicNameValuePair("data[internationalCode]", szInterCode));
		}
		if (szSendingType.equals("email")) {
			nameValuePairs.add(new BasicNameValuePair("data[email]", email));
		}
		nameValuePairs.add(new BasicNameValuePair("data[type]", szSendingType));
		nameValuePairs.add(new BasicNameValuePair("data[amount]", szAmount));
		nameValuePairs.add(new BasicNameValuePair("data[tip]", szTip));
		nameValuePairs.add(new BasicNameValuePair("data[currency]",currency));
		nameValuePairs.add(new BasicNameValuePair("data[currencyCode]", currencyCode));
		
		if (Constants.isDebug)
			Log.e("SendReceipt", "nameValuePairs:: " + nameValuePairs);

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.SEND_RECEIPT);

		String sendReceiptResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					sendReceiptResponse = connection.inputStreamToString(connection.mInputStream).toString();

					Log.e(TAG, "JSONObject SendReceipt::> " + sendReceiptResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendReceiptResponse;

	}
}
