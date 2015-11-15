/**
 * Created: by vashita on 24/dec/2014
 */
package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class PaymentWithURL {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String meterAmount, tip, cardFees, totalAmount, cabMiles, szJobId = "", sType = "", sMobileNumber = "",
			sInternationalcode = "", sEmail = "", sPickUpAdd = "", sDropOffAdd = "", currency = "", currencyCode = "";
	boolean isWalkUp = false;

	public PaymentWithURL(Context context, String meterAmt, String tip, String cardFees, String totalAmount, String jobID,
			String sReceiptType, String sMobileNo, String sInternationalNo, String sEmail, String sPickUp, String sDropOff,String currency,String currencyCode) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.meterAmount = meterAmt;
		this.tip = tip;
		this.cardFees = cardFees;
		this.totalAmount = totalAmount;
		this.szJobId = jobID;
		this.sType = sReceiptType;
		this.sMobileNumber = sMobileNo;
		this.sInternationalcode = sInternationalNo;
		this.sEmail = sEmail;
		this.sPickUpAdd = sPickUp;
		this.sDropOffAdd = sDropOff;
		this.currency = currency;
		this.currencyCode = currencyCode;
	}

	public String SendPaymentUrlToCustomer() {

		if (Util.getLocation(mContext) != null) {
			Location location = Util.getLocation(mContext);
			nameValuePairs
					.add(new BasicNameValuePair("data[latitude]", String.valueOf(Util.getLocation(mContext).getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("data[longitude]", String
					.valueOf(Util.getLocation(mContext).getLongitude())));

			String szAddress = Util.getCurrentAddress(mContext, location);
			nameValuePairs.add(new BasicNameValuePair("data[address]", szAddress));
		}

		if (szJobId != null && !szJobId.isEmpty())
			nameValuePairs.add(new BasicNameValuePair("data[bookingId]", szJobId));

		if (sType.equals("sms")) {
			nameValuePairs.add(new BasicNameValuePair("data[mobile]", sMobileNumber));
			nameValuePairs.add(new BasicNameValuePair("data[internationalCode]", sInternationalcode));
			nameValuePairs.add(new BasicNameValuePair("data[email]", ""));
		} else {
			nameValuePairs.add(new BasicNameValuePair("data[mobile]", ""));
			nameValuePairs.add(new BasicNameValuePair("data[internationalCode]", ""));
			nameValuePairs.add(new BasicNameValuePair("data[email]", sEmail));
		}
		nameValuePairs.add(new BasicNameValuePair("data[type]", sType));

		nameValuePairs.add(new BasicNameValuePair("data[cabMiles]", String.valueOf(cabMiles)));

		float meterAmt = 0, fTip = 0, fCardFees = 0, fCabMiles = 0;
		if (meterAmount != null && !meterAmount.isEmpty())
			meterAmt = Float.valueOf(meterAmount);
		if (tip != null && !tip.isEmpty())
			fTip = Float.valueOf(tip);
		if (cardFees != null && !cardFees.isEmpty())
			fCardFees = Float.valueOf(cardFees);
		if (cabMiles != null && !cabMiles.isEmpty())
			fCabMiles = Float.valueOf(cabMiles);
		float totalAmount = meterAmt + fTip - fCabMiles + fCardFees;

		nameValuePairs.add(new BasicNameValuePair("data[totalAmount]", String.valueOf(totalAmount)));
		nameValuePairs.add(new BasicNameValuePair("data[meterAmount]", String.valueOf(meterAmount)));
		nameValuePairs.add(new BasicNameValuePair("data[tip]", String.valueOf(tip)));
		nameValuePairs.add(new BasicNameValuePair("data[cardFee]", cardFees));

		if (AppValues.driverSettings != null && AppValues.driverSettings.getCardPaymentFeePaidBy() != null)
			nameValuePairs.add(new BasicNameValuePair("data[feePaidBy]", AppValues.driverSettings.getCardPaymentFeePaidBy()));
		nameValuePairs.add(new BasicNameValuePair("data[paymentType]", "card"));

//		if (AppValues.driverSettings.getCurrencyCode() != null)
//			nameValuePairs.add(new BasicNameValuePair("data[currency]",
//					String.valueOf(AppValues.driverSettings.getCurrencyCode())));

		nameValuePairs.add(new BasicNameValuePair("data[pu]", sPickUpAdd));
		nameValuePairs.add(new BasicNameValuePair("data[do]", sDropOffAdd));

		nameValuePairs.add(new BasicNameValuePair("data[currency]", currency));
		nameValuePairs.add(new BasicNameValuePair("data[currencyCode]", currencyCode));
		
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.PAYMENT_URL_TO_PASSENGER);

		Log.e("PaymentWithURL", "nameValuePairs: " + nameValuePairs.toString());
		String paymentResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					paymentResponse = connection.inputStreamToString(connection.mInputStream).toString();
					Log.e("PaymentWithURL", "PaymentWithURLResponse " + paymentResponse);
					JSONObject jObject = new JSONObject(paymentResponse);

					if (jObject.has("error")) {
						paymentResponse = jObject.getString("message");
						return paymentResponse;
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
		return paymentResponse;
	}
}
