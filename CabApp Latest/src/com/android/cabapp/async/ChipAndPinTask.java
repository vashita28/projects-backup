package com.android.cabapp.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.cabapp.fragments.CabPayFragment;
import com.android.cabapp.fragments.PaymentDetailsFragment;
import com.android.cabapp.model.ChipAndPin;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.handpoint.api.TransactionResult;

public class ChipAndPinTask extends AsyncTask<String, Void, String> {
	public Bundle bundle;
	public ProgressDialog pDialog;
	public TransactionResult transactionResult;
	public Context mContext;

	public String szTip, szJobID, szTotalValue, szCardFees;
	public boolean bIsFromCabPayFragment = false;

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		// data[bookingID] = 202 (will be blank for walkup jobs)
		// data[totalAmount] = 700
		// data[fee] = 300
		// data[feePaidBy] = Passenger
		// data[tip] = 0
		// data[merchantRefNum] = driver17-746595873498
		// data[currencyCode] = GBP
		// data[transactionID] = 1
		// data[transactionType] = SALE (Can be any value you get in
		// response from chip and pin)
		// data[financialStatus] = APPROVED (Can be any value you get in
		// response from chip and pin)
		// data[cardEntryType] = CNP (Can be any value you get in response
		// from chip and pin)
		// data[cardSchemeName] = MASTERCARD (Can be any value you get in
		// response from chip and pin)
		// data[CVM] = SIGNATURE (Can be any value you get in response from
		// chip and pin)
		// data[authorisationCode] = LIDFT9035
		// data[EFTTimestamp] = 9857689576548
		// data[ApplicationName] = mPOS
		// data[ApplicationVersion] = 1.6.23.2
		// data[BatteryStatus] = 100%
		// data[cardNumber] = 2345 (Please send last four digits)
		// data[receipt] = (receipt html body you get from chip and pin)

		String szReceipt = "<html><body><div id='merchant_name'>Cab:App</div><div id='merchant_address'>0 <br> 0 0 </div><br/>MID: **19106<br/>TID: ****8910<br/><div id='date'>Date: 27.08.2014</div><div id='time'>Time: 13:18</div>Auth code: 639869<br/>Reference: 28ad2f52-75b0-432f-8cee-646ef175cb3d<br/><br/><div id='receipt_owner'>** CARDHOLDER COPY **</div><br/>Application Label: VISA DEBIT02<br/>Entry: ICC<br/>Card Scheme: VISA<br/>CardNumber: **** **** **** 0226<br/>Aid: A0000000031010<br/>APP PSN: 05<br/>{COPY_RECEIPT}<br/><div id='transaction_type'>SALE</div><div id='amount_value'>GBP587.03</div><br/>Your account will be debited with the above amount<br/>       ** Cardholder PIN verified **<br/>** AUTHORISED **<br/><br/><div id='footer_text'>Please keep this receipt for your records</div></body></html>";
		int nIndex = szReceipt.indexOf("CardNumber: **** **** **** ");
		nIndex = nIndex + 27;
		String szCardNumber = szReceipt.substring(nIndex, nIndex + 4);

		if (Constants.isDebug) {
			Log.e("ChipAndPinTask", "Customer Receipt:: " + transactionResult.getCustomerReceipt());
			Log.e("ChipAndPinTask", " ");
			Log.e("ChipAndPinTask", "Merchant Receipt:: " + transactionResult.getMerchantReceipt());
			Log.e("ChipAndPinTask", "card number:: " + szCardNumber);
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		if (!szJobID.isEmpty())
			nameValuePairs.add(new BasicNameValuePair("data[bookingID]", szJobID));
		else
			nameValuePairs.add(new BasicNameValuePair("data[bookingID]", ""));

		nameValuePairs.add(new BasicNameValuePair("data[totalAmount]", String.valueOf(szTotalValue)));
		nameValuePairs.add(new BasicNameValuePair("data[fee]", String.valueOf(szCardFees)));
		nameValuePairs.add(new BasicNameValuePair("data[feePaidBy]", AppValues.driverSettings.getCardPaymentFeePaidBy()));
		nameValuePairs.add(new BasicNameValuePair("data[tip]", szTip));
		nameValuePairs.add(new BasicNameValuePair("data[merchantRefNum]", "driver" + Util.getDriverID(mContext) + "-"
				+ System.currentTimeMillis()));
		nameValuePairs.add(new BasicNameValuePair("data[currencyCode]", transactionResult.getCurrency().toString()));
		nameValuePairs.add(new BasicNameValuePair("data[transactionID]", transactionResult.getTransactionID()));
		nameValuePairs.add(new BasicNameValuePair("data[financialStatus]", transactionResult.getFinStatus().toString()));

		nameValuePairs.add(new BasicNameValuePair("data[cardEntryType]", transactionResult.getCardEntryType().toString()));
		nameValuePairs.add(new BasicNameValuePair("data[cardSchemeName]", transactionResult.getCardSchemeName()));
		nameValuePairs.add(new BasicNameValuePair("data[CVM]", transactionResult.getOriginalEFTTransactionID()));

		nameValuePairs.add(new BasicNameValuePair("data[authorisationCode]", transactionResult.getAuthorisationCode()));
		nameValuePairs.add(new BasicNameValuePair("data[EFTTimestamp]", transactionResult.geteFTTimestamp().toString()));
		nameValuePairs.add(new BasicNameValuePair("data[ApplicationName]", transactionResult.getDeviceStatus()
				.getApplicationName()));

		nameValuePairs.add(new BasicNameValuePair("data[ApplicationVersion]", transactionResult.getDeviceStatus()
				.getApplicationVersion()));
		nameValuePairs.add(new BasicNameValuePair("data[BatteryStatus]", transactionResult.getDeviceStatus().getBatteryStatus()));
		nameValuePairs.add(new BasicNameValuePair("data[cardNumber]", szCardNumber));
		nameValuePairs.add(new BasicNameValuePair("data[receipt]", transactionResult.getCustomerReceipt()));

		// result = "StatusMessage = " +
		// transactionResult.getStatusMessage()
		// + " \n" + "Type = " + transactionResult.getType() + " \n"
		// + "FinStatus = " + transactionResult.getFinStatus() + " \n"
		// + "RequestedAmount = "
		// + transactionResult.getRequestedAmount() + " \n"
		// + "GratuityAmount = "
		// + transactionResult.getGratuityAmount() + " \n"
		// + "GratuityPercentage = "
		// + transactionResult.getGratuityPercentage() + " \n"
		// + "Currency = " + transactionResult.getCurrency() + " \n"
		// + "TransactionID = " + transactionResult.getTransactionID()
		// + " \n" + "eFTTransactionID = "
		// + transactionResult.geteFTTransactionID() + " \n"
		// + "OriginalEFTTransactionID = "
		// + transactionResult.getOriginalEFTTransactionID() + " \n"
		// + "eFTTimestamp = " + transactionResult.geteFTTimestamp()
		// + " \n" + "AuthorisationCode = "
		// + transactionResult.getAuthorisationCode() + " \n"
		// + "VerificationMethod = "
		// + transactionResult.getVerificationMethod() + " \n"
		// + "CardEntryType = " + transactionResult.getCardEntryType()
		// + " \n" + "CardSchemeName = "
		// + transactionResult.getCardSchemeName() + " \n"
		// + "ErrorMessage = " + transactionResult.getErrorMessage()
		// + " \n" + "CustomerReference = "
		// + transactionResult.getCustomerReference() + " \n"
		// + "BudgetNumber = " + transactionResult.getBudgetNumber()
		// + " \n" + "CardTypeId = "
		// + transactionResult.getCardTypeId() + " \n"
		// + "MerchantReceipt = "
		// + transactionResult.getMerchantReceipt() + " \n"
		// + "CustomerReceipt = "
		// + transactionResult.getCustomerReceipt() + " \n"
		// + "DeviceStatus = " + transactionResult.getDeviceStatus()
		// + " \n";

		ChipAndPin chipAndPin = new ChipAndPin(mContext, nameValuePairs);
		String response = chipAndPin.chipAndPinResponse();
		try {
			JSONObject jObject = new JSONObject(response);
			String errorMessage = "";
			if (jObject.has("success") && jObject.getString("success").equals("true")) {
				return "success";
			} else {
				if (jObject.has("errors")) {
					JSONArray jErrorsArray = jObject.getJSONArray("errors");

					errorMessage = jErrorsArray.getJSONObject(0).getString("message");
					return errorMessage;
				}
			}
			return "Error!";
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

		if (result.equals("success")) {// success
			Message message = new Message();
			if (bIsFromCabPayFragment) {
				message.what = 100;
				CabPayFragment.mHandler.sendMessage(message);
			} else {
				message.obj = bundle;
				message.what = 0;
				PaymentDetailsFragment.mHandler.sendMessage(message);
			}

		} else {
			Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
		}
	}
}
