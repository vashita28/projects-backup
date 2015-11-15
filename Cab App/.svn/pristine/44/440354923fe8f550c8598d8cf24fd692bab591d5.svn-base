package com.android.cabapp.async;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.cabapp.fragments.PaymentDetailsFragment;
import com.android.cabapp.model.MPos_ChipAndPin_Payment;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class MPosChipAndPinTask extends AsyncTask<String, Void, String> {
	String TAG = MPosChipAndPinTask.class.getName();

	public Bundle bundle;
	// public ProgressDialog pDialog;
	public static boolean isChipAndPinAPISuccess = false;
	public Context mContext;
	String jobIdReturn = "", cashBackValueReturn = "";

	public String szTip, szJobID, szTotalValue, szCardFees, szMeterValue,
			szTransactionId;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		// pDialog = new ProgressDialog(Util.mContext);
		// pDialog.setMessage("Processing payment. Please wait...");
		// pDialog.setCancelable(false);
		// pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {

		MPos_ChipAndPin_Payment mPosChipPinTask = new MPos_ChipAndPin_Payment(
				mContext, szCardFees, szTip, szMeterValue, szTotalValue,
				szTransactionId);

		String response = "";
		response = mPosChipPinTask.MsPosPaymentCall();
		if (Constants.isDebug)
			Log.e(TAG, "MPosChipAndPinTask response: " + response.toString());
		// {"response":"success","cashbackValue":0.302,"bookingId":1199}
		JSONObject jObject;
		try {
			jObject = new JSONObject(response);
			if (jObject.has("response")
					&& jObject.getString("response").equals("success")) {
				jobIdReturn = jObject.getString("bookingId");
				cashBackValueReturn = jObject.getString("cashbackValue");
				return "success";
			} else if (jObject.has("errors")) {
				String error = jObject.getString("errors");
				return error;
			} else {
				String error = jObject.getString("error");
				return error;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		// if (pDialog != null)
		// pDialog.dismiss();

		if (result.equals("success")) {
			isChipAndPinAPISuccess = true;
			if (PaymentDetailsFragment.mHandler != null) {
				Bundle bundle = new Bundle();
				bundle.putString("JobIDReturn", jobIdReturn);
				bundle.putString("cashbackValueReturn", cashBackValueReturn);
				Message message = new Message();
				message.setData(bundle);
				message.what = 5;
				PaymentDetailsFragment.mHandler.sendMessage(message);
			}
		} else {
			Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
		}
	}

}
