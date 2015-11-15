package com.android.cabapp.async;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.cabapp.activity.AutoTopUpConfirmActivity;
import com.android.cabapp.activity.BuyCreditsConfirmActivity;
import com.android.cabapp.model.GetPaymentURL;
import com.android.cabapp.util.Util;

public class GetBuyCreditPaymentURLTask extends AsyncTask<Void, Void, String> {
	public String szURL = "";
	public Context mContext;
	public Bundle mBundle;
	ProgressDialog pDialog;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		GetPaymentURL getPaymentURL = new GetPaymentURL(szURL, mContext);
		String response = getPaymentURL.getPaymentResponse();
		// {"response":"success","numberOfCredits":226}
		// {"errors":[{"type":"AuthenticationException","message":"missing or invalid accessToken","requiredAction":"login"}]}
		// {"Error":"Card not found!"}
		// {"error":"Minimum 20 credits can be purchased"}
		try {
			JSONObject jObject = new JSONObject(response);
			if (jObject.has("response")) {
				String szSuccess = "";

				szSuccess = jObject.getString("response");
				if (szSuccess.equals("success")) {
					BuyCreditsConfirmActivity.totalNooOfCreditsAfterSuccess = jObject
							.getInt("numberOfCredits");

					Util.setNumberOfCredits(
							mContext,
							BuyCreditsConfirmActivity.totalNooOfCreditsAfterSuccess);
					return "success";
				}

			} else {
				if (jObject.has("errors") || jObject.has("error")) {
					String errorMessage = "";
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString(
								"message");
					} else {
						errorMessage = jObject.getString("error");
					}
					return errorMessage;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "error";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
		if (result.equals("success")) {

			if (BuyCreditsConfirmActivity.mBuyCreditsConfirmPopUpHandler != null)
				BuyCreditsConfirmActivity.mBuyCreditsConfirmPopUpHandler
						.sendEmptyMessage(100);

			if (AutoTopUpConfirmActivity.mAutoTopUpConfirmPopUpHandler != null)
				AutoTopUpConfirmActivity.mAutoTopUpConfirmPopUpHandler
						.sendEmptyMessage(100);

		} else if (result.equals("error")) {
			Util.showToastMessage(mContext,
					"Error processing payment request. Please try again!",
					Toast.LENGTH_LONG);
		} else {
			Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
		}
	}
}