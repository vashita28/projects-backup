package com.android.cabapp.async;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.cabapp.activity.TakePaymentActivity;
import com.android.cabapp.fragments.BuyAddCreditsFragment;
import com.android.cabapp.fragments.PaymentDetailsFragment;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.GetPaymentURL;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Util;
import com.flurry.android.FlurryAgent;

public class GetPaymentURLTask extends AsyncTask<Void, Void, String> {
	public String szURL = "";
	public Context mContext;
	public Bundle mBundle;
	public boolean bIsAddCredit = false;
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

		JSONObject jPaymentResponseObject = null;
		try {
			jPaymentResponseObject = new JSONObject(
					getPaymentURL.getPaymentResponse());

			if (jPaymentResponseObject.has("response")) {
				jPaymentResponseObject = jPaymentResponseObject
						.getJSONObject("response");

				if (jPaymentResponseObject.has("link")) {
					JSONArray jLinksArray = jPaymentResponseObject
							.getJSONArray("link");

					String szURL = jLinksArray.getJSONObject(0)
							.getString("uri");
					return szURL;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				jPaymentResponseObject = new JSONObject(
						getPaymentURL.getPaymentResponse());
				if (jPaymentResponseObject.has("response")
						&& jPaymentResponseObject.getString("response") != null
						&& jPaymentResponseObject.getString("response").equals(
								"success")) {
					FlurryAgent.logEvent("Payment successful");

					DriverAccountDetails driverAccount = new DriverAccountDetails(
							mContext);
					driverAccount.retriveAccountDetails(mContext);

					DriverSettingDetails driverSettings = new DriverSettingDetails(
							mContext);
					driverSettings.retriveDriverSettings(mContext);

					return "success";
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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

		if (result == null || result.equals("error")) {
			Util.showToastMessage(mContext,
					"Error processing payment request. Please try again!",
					Toast.LENGTH_LONG);
		} else if (result.equals("success")) {
			if (!bIsAddCredit) {
				AppValues.mNowJobsList.clear();
				if (PaymentDetailsFragment.mHandler != null) {
					PaymentDetailsFragment.mHandler.sendEmptyMessage(100);
				}
			} else {
				FlurryAgent.logEvent("Credits purchase successful");
				BuyAddCreditsFragment.mBuyCreditsHandler.sendEmptyMessage(0);
			}

		} else {
			Intent takePaymentIntent = new Intent(mContext,
					TakePaymentActivity.class);
			if (mBundle == null)
				mBundle = new Bundle();
			mBundle.putString("paymentURL", result);
			mBundle.putBoolean("isaddcredit", bIsAddCredit);
			takePaymentIntent.putExtras(mBundle);
			mContext.startActivity(takePaymentIntent);
		}
	}

}
