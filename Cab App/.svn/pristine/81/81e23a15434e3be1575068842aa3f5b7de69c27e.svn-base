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

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class BuyCredit {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	int numberOfCredits;
	float szCostPerCredit;
	String szTruncatedPan = "", szCardType = "", szCardPayworkToken = "";
	boolean isAutoTopUp;

	public BuyCredit(Context context, int noOfCredits, boolean autoTopUp,
			String truncatedPan, String cardType, float costPerCredit,
			String cardPayworkToken) {
		// TODO Auto-generated constructor stub
		mContext = context;
		connection = new Connection(mContext);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.numberOfCredits = noOfCredits;
		this.isAutoTopUp = autoTopUp;
		this.szTruncatedPan = truncatedPan;
		this.szCardType = cardType;
		this.szCostPerCredit = costPerCredit;
		this.szCardPayworkToken = cardPayworkToken;

	}

	public String BuyCreditCall() {

		nameValuePairs.add(new BasicNameValuePair("data[numberOfCredits]",
				String.valueOf(numberOfCredits)));
		if (isAutoTopUp)
			nameValuePairs.add(new BasicNameValuePair("data[autotopup]", "1"));
		else
			nameValuePairs.add(new BasicNameValuePair("data[autotopup]", "0"));
		nameValuePairs.add(new BasicNameValuePair("data[payworksToken]",
				szCardPayworkToken));
		nameValuePairs.add(new BasicNameValuePair("data[truncatedPan]",
				szTruncatedPan));
		nameValuePairs
				.add(new BasicNameValuePair("data[cardType]", szCardType));
		nameValuePairs.add(new BasicNameValuePair("data[costPerCredits]",
				String.valueOf(szCostPerCredit)));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.BUY_CREDITS);

		Log.e("BuyCredits", "nameValuePairs: " + nameValuePairs.toString());
		String buyCreditsResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					buyCreditsResponse = connection.inputStreamToString(
							connection.mInputStream).toString();
					Log.e("BuyCredit", "BuyCreditsResponse "
							+ buyCreditsResponse);
					JSONObject jObject = new JSONObject(buyCreditsResponse);

					if (jObject.has("error")) {
						buyCreditsResponse = jObject.getString("message");
						return buyCreditsResponse;
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
		return buyCreditsResponse;
	}
}
