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
import com.android.cabapp.model.MposSendReceipt;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class MPosSendReceiptTask extends AsyncTask<String, Void, String> {

	public String szJobID, szAmount, szTip, szMobile, szInternationalCode,
			szPU, szDO, szEmail, szPaymentType, szCardFees, szSendingType,
			szTransactionId, szPaymentStatus, szTruncatedPan, szBrand,
			szMerchantId, szTerminalId, szEntryMode, szAID, szPWID,
			szAuthorization;
	public ProgressDialog pDialog;
	public boolean bIsFromHistoryJobs = false, bIsShowSuccessToast = false;
	public static boolean bIsWalkUp = false;
	public Context mContext;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (bIsShowSuccessToast) {
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Updating! Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (Constants.isDebug)
			Log.d("SendReceiptTask", "jobID:: " + szJobID + " szAmount: "
					+ szAmount + " szTip: " + szTip + " szMobile: " + szMobile
					+ " szInternationalCode: " + szInternationalCode
					+ " szPU: " + szPU + " szDO: " + szDO + " szEmail: "
					+ szEmail + " szPaymentType: " + szPaymentType
					+ "transacetion id: " + szTransactionId
					+ "  szPaymentstatus: " + szPaymentStatus
					+ "  szTruncatedPan: " + szTruncatedPan + " szBrand:  "
					+ szBrand + "   szMerchantId: " + szMerchantId
					+ " szTerminalId:  " + szTerminalId + "  szEntryMode: "
					+ szEntryMode + " szAID: " + szAID + " szPWID:  " + szPWID
					+ "  szAutorisation: " + szAuthorization);

		MposSendReceipt mPosSendReceipt;
		mPosSendReceipt = new MposSendReceipt(mContext, szMobile,
				szInternationalCode, szPU, szDO, szEmail, szAmount,
				szPaymentType, szTip, szJobID, szCardFees, szSendingType,
				szTransactionId, szPaymentStatus, szTruncatedPan, szBrand,
				szMerchantId, szTerminalId, szEntryMode, szAID, szPWID,
				szAuthorization);

		String response = mPosSendReceipt.mPosSendingReceipt();
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
			} else if (jObject.has("receipt")
					&& jObject.getString("receipt").equals("not send")
					&& !jObject.has("bookingId")) {
				return "Transaction status updated";
			} else if (jObject.has("status")
					&& jObject.get("status").equals(
							"Payment status already updated.")) {
				return "Transaction status updated";
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
			if (!bIsShowSuccessToast) {
			} else {
				if (bIsFromHistoryJobs) {
					Util.showToastMessage(Util.mContext,
							"Receipt has been sent successfully",
							Toast.LENGTH_LONG);
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
							"Receipt has been sent successfully",
							Toast.LENGTH_LONG);
				}

				// Job Completed
				AppValues.clearAllJobsList();
				Fragment fragment = null;
				fragment = new com.android.cabapp.fragments.JobsFragment();
				((MainActivity) Util.mContext)
						.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
				if (fragment != null)
					((MainActivity) Util.mContext).replaceFragment(fragment,
							true);
			}

		} else if (result.contains("Transaction status updated")) {

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
