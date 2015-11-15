package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class MPos_ChipAndPin_Payment {
	String TAG = MPos_ChipAndPin_Payment.class.getName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String cardFee = "", currency = "", feesPaidBy = "", latitude = "",
			longitude = "", meterAmount = "", tip = "", totalAmount = "",
			transactionId = "";

	public MPos_ChipAndPin_Payment(Context context, String szCardFees,
			String szTip, String szMeterAmt, String szTotalAmt,
			String szTransactionId) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.cardFee = szCardFees;
		this.meterAmount = szMeterAmt;
		this.tip = szTip;
		this.totalAmount = szTotalAmt;
		this.transactionId = szTransactionId;

	}

	public String MsPosPaymentCall() {

		if (Util.getLocation(mContext) != null) {
			nameValuePairs.add(new BasicNameValuePair("data[latitude]", String
					.valueOf(Util.getLocation(mContext).getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("data[longitude]", String
					.valueOf(Util.getLocation(mContext).getLongitude())));
		}
		if (AppValues.driverSettings != null
				&& AppValues.driverSettings.getCardPaymentFeePaidBy() != null) {
			feesPaidBy = AppValues.driverSettings.getCardPaymentFeePaidBy();
			nameValuePairs.add(new BasicNameValuePair("data[feePaidBy]",
					feesPaidBy));
			if (AppValues.driverSettings.getCurrencyCode() != null)
				nameValuePairs.add(new BasicNameValuePair("data[currency]",
						String.valueOf(AppValues.driverSettings
								.getCurrencyCode())));
		}
		nameValuePairs.add(new BasicNameValuePair("data[cardFee]", cardFee));
		nameValuePairs.add(new BasicNameValuePair("data[meterAmount]",
				meterAmount));
		nameValuePairs.add(new BasicNameValuePair("data[tip]", tip));
		nameValuePairs.add(new BasicNameValuePair("data[transactionId]",
				transactionId));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.MPOS_CHIP_AND_PIN_PAYMENT);

		Log.e(TAG, "nameValuePairs: " + nameValuePairs.toString());
		String mPosResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					mPosResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					if (Constants.isDebug)
						Log.e(TAG, "MPosPaymentResponse " + mPosResponse);
					JSONObject jObject = new JSONObject(mPosResponse);

					if (jObject.has("error")) {
						mPosResponse = jObject.getString("message");
						return mPosResponse;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mPosResponse;
	}
}
