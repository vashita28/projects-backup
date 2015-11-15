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

public class MposSendReceipt {
	private static final String TAG = MposSendReceipt.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String szJobId = "", szSendingType = "", szMobileNumber = "",
			szInterCode = "", pickup = "", dropOff = "", email = "",
			szAmount = "0", szTip = "0", szFee = "0", szPaymentType = "",
			szTransactionID = "", szPaymentstatus = "", truncatedPan = "",
			brand = "", merchantId = "", terminalId = "", entryMode = "",
			AID = "", PWID = "", authorization = "";

	public MposSendReceipt(Context context, String szMobileNumber,
			String szInternationalCode, String szPickUp, String szDropOff,
			String szEmail, String amount, String paymentType, String tip,
			String jobID, String fees, String sendingType,
			String szTransactionId, String szPaymentStatus,
			String szTruncatedPan, String szBrand, String szMerchantId,
			String szTerminalId, String szEntryMode, String szAID,
			String szPWID, String szAuthorization) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.szMobileNumber = szMobileNumber;
		this.szInterCode = szInternationalCode;
		this.pickup = szPickUp;
		this.dropOff = szDropOff;
		this.email = szEmail;
		this.szAmount = amount;
		this.szPaymentType = paymentType;
		this.szTip = tip;
		this.szJobId = jobID;
		this.szFee = fees;
		this.szSendingType = sendingType;
		this.szTransactionID = szTransactionId;
		this.szPaymentstatus = szPaymentStatus;
		this.truncatedPan = szTruncatedPan;
		this.brand = szBrand;
		this.merchantId = szMerchantId;
		this.terminalId = szTerminalId;
		this.entryMode = szEntryMode;
		this.AID = szAID;
		this.PWID = szPWID;
		this.authorization = szAuthorization;

	}

	public String mPosSendingReceipt() {

		nameValuePairs.add(new BasicNameValuePair("data[bookingId]", szJobId));
		if (szSendingType.equals("sms")) {
			nameValuePairs.add(new BasicNameValuePair("data[mobile]",
					szMobileNumber));
			nameValuePairs.add(new BasicNameValuePair(
					"data[internationalCode]", szInterCode));
		}
		if (szSendingType.equals("email")) {
			nameValuePairs.add(new BasicNameValuePair("data[email]", email));
		}

		nameValuePairs.add(new BasicNameValuePair("data[truncatedPan]",
				truncatedPan));
		nameValuePairs.add(new BasicNameValuePair("data[brand]", brand));
		nameValuePairs.add(new BasicNameValuePair("data[merchantId]",
				merchantId));
		nameValuePairs.add(new BasicNameValuePair("data[terminalId]",
				terminalId));
		nameValuePairs
				.add(new BasicNameValuePair("data[entryMode]", entryMode));
		nameValuePairs.add(new BasicNameValuePair("data[AID]", AID));
		nameValuePairs.add(new BasicNameValuePair("data[PWID]", PWID));
		nameValuePairs.add(new BasicNameValuePair("data[authorization]",
				authorization));

		nameValuePairs.add(new BasicNameValuePair("data[pu]", pickup));
		nameValuePairs.add(new BasicNameValuePair("data[do]", dropOff));
		nameValuePairs.add(new BasicNameValuePair("data[paymentType]",
				szPaymentType));
		nameValuePairs.add(new BasicNameValuePair("data[type]", szSendingType));
		nameValuePairs.add(new BasicNameValuePair("data[amount]", szAmount));
		nameValuePairs.add(new BasicNameValuePair("data[tip]", szTip));
		nameValuePairs.add(new BasicNameValuePair("data[fee]", szFee));

		nameValuePairs.add(new BasicNameValuePair("data[transationId]",
				szTransactionID));
		nameValuePairs.add(new BasicNameValuePair("data[paymentStatus]",
				szPaymentstatus));

		if (Constants.isDebug)
			Log.e("SendReceipt", "nameValuePairs:: " + nameValuePairs);

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.MPOS_SEND_RECEIPT);

		String sendReceiptResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					sendReceiptResponse = connection.inputStreamToString(
							connection.mInputStream).toString();

					Log.e(TAG, "JSONObject SendReceipt::> "
							+ sendReceiptResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendReceiptResponse;

	}
}
