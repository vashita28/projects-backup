package com.android.cabapp.async;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.SendReceipt;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class SendReceiptTask extends AsyncTask<String, Void, String> {
	public String szJobID, szType, szAmount, szTip, szFee, szMobile,
			szInternationalCode, szPU, szDO, szEmail, szPaymentType;
	ProgressDialog pDialog;
	public boolean bIsFromHistoryJobs = false;
	public static boolean bIsWalkUp = false;

	public Context mContext;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pDialog = new ProgressDialog(Util.mContext);
		pDialog.setMessage("Updating! Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (Constants.isDebug)
			Log.d("SendReceiptTask", "jobID:: " + szJobID + " szType: "
					+ szType + " szAmount: " + szAmount + " szTip: " + szTip
					+ " szFee: " + szFee + " szMobile: " + szMobile
					+ " szInternationalCode: " + szInternationalCode
					+ " szPU: " + szPU + " szDO: " + szDO + " szEmail: "
					+ szEmail + " szPaymentType: " + szPaymentType);

		SendReceipt sendReceipt;
		if (szJobID != null && !szJobID.isEmpty()) {
			if (szTip != null && szTip.isEmpty())
				szTip = "0";
			sendReceipt = new SendReceipt(mContext, szJobID, szFee, szType,
					szAmount, szTip, szEmail, szMobile);
		} else {
			if (szTip != null && szTip.isEmpty())
				szTip = "0";
			sendReceipt = new SendReceipt(mContext, szMobile,
					szInternationalCode, szPU, szDO, szEmail, szType, szAmount,
					szPaymentType, szTip);
		}

		String response = sendReceipt.sendingReceipt();
		Log.e("SendReceipt Response:: ", "SendReceipt Response:: " + response);
		try {
			JSONObject jObject = new JSONObject(response);

			if (jObject.has("success")
					&& jObject.getString("success").equals("true")) {

				DriverAccountDetails driverAccount = new DriverAccountDetails(
						mContext);
				driverAccount.retriveAccountDetails(mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						mContext);
				driverSettings.retriveDriverSettings(mContext);

				return "success";
			} else {
				if (jObject.has("errors")) {
					JSONArray jErrorsArray = jObject.getJSONArray("errors");

					String errorMessage = jErrorsArray.getJSONObject(0)
							.getString("message");
					return errorMessage;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (pDialog != null)
			pDialog.dismiss();

		if (result.contains("success")) {

			if (bIsFromHistoryJobs) {
				Util.showToastMessage(Util.mContext,
						"Receipt has been sent successfully", Toast.LENGTH_LONG);
			} else if (bIsWalkUp) {
				// Util.showToastMessage(Util.mContext,
				// "Receipt has been sent successfully", Toast.LENGTH_LONG);
			} else if (szJobID != null && !szJobID.isEmpty())
				Util.showToastMessage(
						Util.mContext,
						"Receipt has been sent to your passenger's registered email address",
						Toast.LENGTH_LONG);
			else {
				Util.showToastMessage(Util.mContext,
						"Receipt has been sent successfully", Toast.LENGTH_LONG);
			}
			// Job Completed
			AppValues.clearAllJobsList();
			Fragment fragment = null;
			fragment = new com.android.cabapp.fragments.JobsFragment();
			((MainActivity) Util.mContext)
					.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
			if (fragment != null)
				((MainActivity) Util.mContext).replaceFragment(fragment, true);
		} else {
			Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
		}
	}

}
