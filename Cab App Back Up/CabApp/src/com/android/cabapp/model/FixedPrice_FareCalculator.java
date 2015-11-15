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

public class FixedPrice_FareCalculator {

	private static final String TAG = FixedPrice_FareCalculator.class
			.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	Bundle bundleFixedPrice;

	public FixedPrice_FareCalculator(Context context, Bundle bundle) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.bundleFixedPrice = bundle;
	}

	public String fixedPriceCalculation() {

		if (bundleFixedPrice != null) {

			nameValuePairs.add(new BasicNameValuePair(
					"data[pickupData][latitude]", bundleFixedPrice
							.getString(Constants.PICKUP_LATITUDE)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[pickupData][longitude]", bundleFixedPrice
							.getString(Constants.PICKUP_LONGITUDE)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[destinationData][latitude]", bundleFixedPrice
							.getString(Constants.DESTINATION_LATITUDE)));
			nameValuePairs.add(new BasicNameValuePair(
					"data[destinationData][longitude] ", bundleFixedPrice
							.getString(Constants.DESTINATION_LONGITUDE)));

			if (Constants.isDebug)
				Log.e(TAG,
						"Fixed Price bundle: " + bundleFixedPrice.toString()
								+ "  \n Request Parameters:: "
								+ nameValuePairs.toString());

		}

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.FIXED_PRICE);

		String szFixedPriceResponse = "";

		if (connection.mInputStream != null) {
			try {
				szFixedPriceResponse = connection.inputStreamToString(
						connection.mInputStream).toString();

				if (Constants.isDebug)
					Log.e(TAG, "szFixedPriceResponse: " + szFixedPriceResponse);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return szFixedPriceResponse;

	}

}
