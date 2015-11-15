package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.push.PushManager;

public class AccountModification {
	private static final String TAG = AccountModification.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	Bundle modificationBundle;

	public AccountModification(Context context, Bundle bundle) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.modificationBundle = bundle;
	}

	public String ModificationCredentials() {
		String apid = PushManager.shared().getAPID();

		if (modificationBundle != null) {

			if (Util.getLocation(mContext) != null) {
				nameValuePairs.add(new BasicNameValuePair(
						"data[currentLatitude]", String.valueOf(Util
								.getLocation(mContext).getLatitude())));
				nameValuePairs.add(new BasicNameValuePair(
						"data[currentLongitude]", String.valueOf(Util
								.getLocation(mContext).getLongitude())));
			}

			nameValuePairs.add(new BasicNameValuePair("data[workingCity]",
					modificationBundle.getString(Constants.WORKING_CITY)));
			nameValuePairs.add(new BasicNameValuePair("data[firstname]",
					modificationBundle.getString(Constants.FIRSTNAME)
							.toUpperCase()));
			nameValuePairs.add(new BasicNameValuePair("data[surname]",
					modificationBundle.getString(Constants.SURNAME)
							.toUpperCase()));
			nameValuePairs.add(new BasicNameValuePair("data[mobileNumber]",
					modificationBundle.getString(Constants.MOBILE_NUMBER)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[internationalCode]", modificationBundle
							.getString(Constants.COUNRTY_CODE)));
			nameValuePairs.add(new BasicNameValuePair("data[email]",
					modificationBundle.getString(Constants.EMAIL_ADDRESS)));
			nameValuePairs.add(new BasicNameValuePair("data[username]",
					modificationBundle.getString(Constants.USERNAME)));
			nameValuePairs.add(new BasicNameValuePair("data[password]",
					modificationBundle
							.getString(Constants.REGISTRATION_PASSWORD)));
			nameValuePairs.add(new BasicNameValuePair("data[badgeNumber]",
					modificationBundle.getString(Constants.BADGE_NUMBER)));
			nameValuePairs
					.add(new BasicNameValuePair("data[badgeExpiration]",
							modificationBundle
									.getString(Constants.DRIVER_BADGE_EXPIRY)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[vehicle][registration]", modificationBundle
							.getString(Constants.VECHILE_REGISTRATION_NO)
							.toUpperCase()));
			nameValuePairs.add(new BasicNameValuePair("data[vehicle][make]",
					modificationBundle.getString(Constants.VECHILE_MAKE)
							.toUpperCase()));
			nameValuePairs.add(new BasicNameValuePair("data[vehicle][model]",
					modificationBundle.getString(Constants.VECHILE_MODEL)
							.toUpperCase()));
			nameValuePairs.add(new BasicNameValuePair("data[vehicle][colour]",
					modificationBundle.getString(Constants.VECHILE_COLOUR)));
			nameValuePairs.add(new BasicNameValuePair("data[wheelchairAccess]",
					modificationBundle
							.getString(Constants.VECHILE_WHEELCHAIR_ACCESS)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[maximumPassengers]", modificationBundle
							.getString(Constants.VECHILE_MAXIMUM_PASSENGERS)));
			nameValuePairs.add(new BasicNameValuePair("data[paymentType]",
					modificationBundle.getString(Constants.PAYMENT_TYPE)));
			nameValuePairs
					.add(new BasicNameValuePair("data[deviceToken]", apid));
			nameValuePairs.add(new BasicNameValuePair("data[deviceType]",
					"android"));

			nameValuePairs.add(new BasicNameValuePair("data[billingAddress1]",
					modificationBundle.getString(Constants.BILLING_ADDRESS1)));
			nameValuePairs.add(new BasicNameValuePair("data[billingAddress2]",
					modificationBundle.getString(Constants.BILLING_ADDRESS2)));
			nameValuePairs.add(new BasicNameValuePair("data[billingCity]",
					modificationBundle.getString(Constants.BILLING_CITY)));
			nameValuePairs.add(new BasicNameValuePair("data[billingCounty]",
					modificationBundle.getString(Constants.BILLING_COUNTRY)));
			nameValuePairs.add(new BasicNameValuePair("data[billingPostcode]",
					modificationBundle.getString(Constants.BILLING_POSTCODE)));

			if (Util.getCitySelectedLondon(mContext)) {
				if (modificationBundle.containsKey(Constants.BADGE_COLOUR)) {
					if (Constants.isDebug)
						Log.e(TAG,
								"BadgeColor: "
										+ modificationBundle
												.getString(Constants.BADGE_COLOUR));
					if (modificationBundle.containsKey(Constants.BADGE_COLOUR))
						nameValuePairs.add(new BasicNameValuePair(
								"data[badgeColour]", modificationBundle
										.getString(Constants.BADGE_COLOUR)));

					if (modificationBundle.getString(Constants.BADGE_COLOUR)
							.equals(Constants.BADGE_COLOUR_YELLOW)) {
						if (modificationBundle.containsKey(Constants.SECTOR))
							nameValuePairs.add(new BasicNameValuePair(
									"data[sector]", modificationBundle
											.getString(Constants.SECTOR)));
					}
				}
			}

			// nameValuePairs.add(new BasicNameValuePair("data[badgeColour]",
			// "Orange"));

			if (modificationBundle.getBoolean(Constants.IS_CARD_CHECKED)
					|| modificationBundle.getString(Constants.PAYMENT_TYPE)
							.equals("both")) {
				boolean isCardSelected = true;
				if (isCardSelected) {
					nameValuePairs.add(new BasicNameValuePair("data[bankName]",
							modificationBundle.getString(Constants.BANK_NAME)));
					nameValuePairs.add(new BasicNameValuePair(
							"data[bankAccountName]", modificationBundle
									.getString(Constants.BANK_ACCOUNT_NAME)));
					nameValuePairs.add(new BasicNameValuePair(
							"data[bankAccountNumber]", modificationBundle
									.getString(Constants.BANK_ACCOUNT_NUMBER)));
					nameValuePairs.add(new BasicNameValuePair(
							"data[bankSortCode]", modificationBundle
									.getString(Constants.BANK_SORT_CODE)));
					nameValuePairs.add(new BasicNameValuePair("data[bankIban]",
							modificationBundle.getString(Constants.BANK_IBAN)));
				}
			}

			if (modificationBundle.containsKey(Constants.BILLING_ADDRESS1)) {
				nameValuePairs.add(new BasicNameValuePair(
						"data[billingAddress1]", modificationBundle
								.getString(Constants.BILLING_ADDRESS1)));
				nameValuePairs.add(new BasicNameValuePair(
						"data[billingAddress2]", modificationBundle
								.getString(Constants.BILLING_ADDRESS2)));
				nameValuePairs.add(new BasicNameValuePair("data[billingCity]",
						modificationBundle.getString(Constants.BILLING_CITY)));
				nameValuePairs.add(new BasicNameValuePair(
						"data[billingPostcode]", modificationBundle
								.getString(Constants.BILLING_POSTCODE)));
				nameValuePairs.add(new BasicNameValuePair(
						"data[billingCounty]", modificationBundle
								.getString(Constants.BILLING_COUNTRY)));
			}

			if (Constants.isDebug)
				Log.e(TAG,
						"Modification bundle: " + modificationBundle.toString()
								+ "  \n Request Parameters::nameValuePairs "
								+ nameValuePairs.toString());

		}

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.ACCOUNT_MODIFICATION);

		String modificationResponse = "";

		if (connection.mInputStream != null) {
			try {
				modificationResponse = connection.inputStreamToString(
						connection.mInputStream).toString();
				// {"success":"true","driverId":34,"numberOfCredits":4942,"isauthorized":false}

				JSONObject jObject;
				try {
					jObject = new JSONObject(modificationResponse);
					if (jObject.has("driverId")) {
						Util.setDriverID(mContext,
								jObject.getString("driverId"));
					}
					if (jObject.has("numberOfCredits")) {
						Util.setNumberOfCredits(mContext,
								jObject.getInt("numberOfCredits"));
					}
					// if (jObject.has("isauthorized")) {
					// Util.setIsAuthorised(mContext,
					// jObject.getBoolean("isauthorized"));
					// }

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return modificationResponse;

	}
}
