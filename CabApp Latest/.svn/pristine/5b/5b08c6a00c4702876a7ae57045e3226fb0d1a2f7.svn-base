package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.push.PushManager;

public class Registration {
	private static final String TAG = Registration.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	Bundle registrationBundle;

	// Location currentLocation = null;

	public Registration(Context context, Bundle bundle) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.registrationBundle = bundle;
	}

	public String RegistrationCredentials() {
		String apid = PushManager.shared().getAPID();
		Log.e(TAG, "Registration Credentials devicetoken::> " + apid + "   bundle::> " + registrationBundle.toString());

		if (registrationBundle != null) {

			if (Util.getLocation(mContext) != null) {
				nameValuePairs.add(new BasicNameValuePair("data[currentLatitude]", String.valueOf(Util.getLocation(mContext)
						.getLatitude())));
				nameValuePairs.add(new BasicNameValuePair("data[currentLongitude]", String.valueOf(Util.getLocation(mContext)
						.getLongitude())));
			}

			if (registrationBundle.containsKey(Constants.WORKING_CITY))
				nameValuePairs.add(new BasicNameValuePair("data[workingCity]", registrationBundle
						.getString(Constants.WORKING_CITY)));
			if (registrationBundle.containsKey(Constants.FIRSTNAME))
				nameValuePairs.add(new BasicNameValuePair("data[firstname]", registrationBundle.getString(Constants.FIRSTNAME)));
			if (registrationBundle.containsKey(Constants.SURNAME))
				nameValuePairs.add(new BasicNameValuePair("data[surname]", registrationBundle.getString(Constants.SURNAME)));
			if (registrationBundle.containsKey(Constants.MOBILE_NUMBER))
				nameValuePairs.add(new BasicNameValuePair("data[mobileNumber]", registrationBundle
						.getString(Constants.MOBILE_NUMBER)));
			if (registrationBundle.containsKey(Constants.COUNRTY_CODE))
				nameValuePairs.add(new BasicNameValuePair("data[internationalCode]", registrationBundle
						.getString(Constants.COUNRTY_CODE)));
			if (registrationBundle.containsKey(Constants.EMAIL_ADDRESS))
				nameValuePairs.add(new BasicNameValuePair("data[email]", registrationBundle.getString(Constants.EMAIL_ADDRESS)));
			if (registrationBundle.containsKey(Constants.USERNAME))
				nameValuePairs.add(new BasicNameValuePair("data[username]", registrationBundle.getString(Constants.USERNAME)
						.toUpperCase()));
			if (registrationBundle.containsKey(Constants.REGISTRATION_PASSWORD))
				nameValuePairs.add(new BasicNameValuePair("data[password]", registrationBundle
						.getString(Constants.REGISTRATION_PASSWORD)));
			if (registrationBundle.containsKey(Constants.BADGE_NUMBER))
				nameValuePairs.add(new BasicNameValuePair("data[badgeNumber]", registrationBundle
						.getString(Constants.BADGE_NUMBER)));
			if (registrationBundle.containsKey(Constants.DRIVER_BADGE_EXPIRY))
				nameValuePairs.add(new BasicNameValuePair("data[badgeExpiration]", registrationBundle
						.getString(Constants.DRIVER_BADGE_EXPIRY)));
			if (Util.getCitySelectedLondon(mContext)) {
				if (registrationBundle.containsKey(Constants.BADGE_COLOUR)) {
					if (Constants.isDebug)
						Log.e(TAG, "BadgeColor: " + registrationBundle.getString(Constants.BADGE_COLOUR));
					if (registrationBundle.containsKey(Constants.BADGE_COLOUR))
						nameValuePairs.add(new BasicNameValuePair("data[badgeColour]", registrationBundle
								.getString(Constants.BADGE_COLOUR)));

					if (registrationBundle.getString(Constants.BADGE_COLOUR).equals(Constants.BADGE_COLOUR_YELLOW)) {
						if (registrationBundle.containsKey(Constants.SECTOR))
							nameValuePairs.add(new BasicNameValuePair("data[sector]", registrationBundle
									.getString(Constants.SECTOR)));
					}
				}
			}

			if (registrationBundle.containsKey(Constants.VECHILE_REGISTRATION_NO))
				nameValuePairs.add(new BasicNameValuePair("data[vehicle][registration]", registrationBundle.getString(
						Constants.VECHILE_REGISTRATION_NO).toUpperCase()));
			if (registrationBundle.containsKey(Constants.VECHILE_LICENSE_NO))
				nameValuePairs.add(new BasicNameValuePair("data[vehicleLicenseNo]", registrationBundle.getString(
						Constants.VECHILE_LICENSE_NO).toUpperCase()));
			if (registrationBundle.containsKey(Constants.VECHILE_MAKE))
				nameValuePairs.add(new BasicNameValuePair("data[vehicle][make]", registrationBundle.getString(
						Constants.VECHILE_MAKE).toUpperCase()));
			if (registrationBundle.containsKey(Constants.VECHILE_MODEL))
				nameValuePairs.add(new BasicNameValuePair("data[vehicle][model]", registrationBundle.getString(
						Constants.VECHILE_MODEL).toUpperCase()));
			if (registrationBundle.containsKey(Constants.VECHILE_COLOUR))
				nameValuePairs.add(new BasicNameValuePair("data[vehicle][colour]", registrationBundle
						.getString(Constants.VECHILE_COLOUR)));
			if (registrationBundle.containsKey(Constants.VECHILE_WHEELCHAIR_ACCESS))
				nameValuePairs.add(new BasicNameValuePair("data[wheelchairAccess]", registrationBundle
						.getString(Constants.VECHILE_WHEELCHAIR_ACCESS)));
			if (registrationBundle.containsKey(Constants.VECHILE_MAXIMUM_PASSENGERS))
				nameValuePairs.add(new BasicNameValuePair("data[maximumPassengers]", registrationBundle
						.getString(Constants.VECHILE_MAXIMUM_PASSENGERS)));
			if (registrationBundle.containsKey(Constants.PAYMENT_TYPE))
				nameValuePairs.add(new BasicNameValuePair("data[paymentType]", registrationBundle
						.getString(Constants.PAYMENT_TYPE)));
			nameValuePairs.add(new BasicNameValuePair("data[deviceToken]", apid));
			nameValuePairs.add(new BasicNameValuePair("data[deviceType]", "android"));

			if (registrationBundle.getBoolean(Constants.IS_CARD_CHECKED)) {
				boolean isCardSelected = registrationBundle.getBoolean(Constants.IS_CARD_CHECKED);
				if (isCardSelected) {
					if (registrationBundle.containsKey(Constants.BANK_NAME))
						nameValuePairs.add(new BasicNameValuePair("data[bankName]", registrationBundle
								.getString(Constants.BANK_NAME)));
					if (registrationBundle.containsKey(Constants.BANK_ACCOUNT_NAME))
						nameValuePairs.add(new BasicNameValuePair("data[bankAccountName]", registrationBundle
								.getString(Constants.BANK_ACCOUNT_NAME)));
					if (registrationBundle.containsKey(Constants.BANK_ACCOUNT_NUMBER))
						nameValuePairs.add(new BasicNameValuePair("data[bankAccountNumber]", registrationBundle
								.getString(Constants.BANK_ACCOUNT_NUMBER)));
					if (registrationBundle.containsKey(Constants.BANK_SORT_CODE))
						nameValuePairs.add(new BasicNameValuePair("data[bankSortCode]", registrationBundle
								.getString(Constants.BANK_SORT_CODE)));
					if (registrationBundle.containsKey(Constants.BANK_IBAN))
						nameValuePairs.add(new BasicNameValuePair("data[bankIban]", registrationBundle
								.getString(Constants.BANK_IBAN)));
				}
			}

		}
		if (Constants.isDebug)
			Log.e(TAG, "nameValuePairs::> " + nameValuePairs);
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.REGISTRATION);

		String registrationResponse = "";

		if (connection.mInputStream != null) {
			try {
				registrationResponse = connection.inputStreamToString(connection.mInputStream).toString();
				if (Constants.isDebug)
					Log.e(TAG, "registrationResponse::> " + registrationResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return registrationResponse;
	}
}
