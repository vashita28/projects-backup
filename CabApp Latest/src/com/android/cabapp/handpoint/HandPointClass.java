package com.android.cabapp.handpoint;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Util;
import com.handpoint.api.ConnectionMethod;
import com.handpoint.api.Currency;
import com.handpoint.api.Device;
import com.handpoint.api.Events;
import com.handpoint.api.FinancialStatus;
import com.handpoint.api.Hapi;
import com.handpoint.api.HapiFactory;
import com.handpoint.api.SignatureRequest;
import com.handpoint.api.TransactionResult;

public class HandPointClass implements Events.Required {
	Hapi api;
	Device device;
	CallbackListener mCallbackListener;
	String deviceNameString;

	public HandPointClass(Context context, CallbackListener callbackListener,
			String deviceName) {

		// Log.d(HandPointClass.class.getName(), "Device Name:: " + deviceName);
		this.mCallbackListener = callbackListener;
		this.deviceNameString = deviceName;
		initApi(context);
	}

	public void initApi(Context context) {
		String sharedSecret = "C1B721955FAE0ACC8293DC8265267038E10A275115990D3A41DA5A7EF4E4467A";
		this.api = HapiFactory.getAsyncInterface(this, context)
				.defaultSharedSecret(sharedSecret);

		// The api is now initialized. Yay! we've even set a default shared
		// secret!
		// But we need to connect to a device to start taking payments.
		// Let's search for them:

		this.api.listDevices(ConnectionMethod.BLUETOOTH);

		// This triggers the search, you should expect the results in the
		// listener defined above
	}

	public boolean pay(String payment, Currency currency) {

		Log.d("Handpoint", "pay :: " + payment + " " + currency);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("CustomerReference",
				"driver_" + Util.getDriverID(Util.mContext)
						+ System.currentTimeMillis());// TaxiDriverID_timestamp
		// value

		return this.api.sale(new BigInteger(payment), currency, map);
		// return this.api.sale(new BigInteger(payment), currency);
	}

	@Override
	public void signatureRequired(SignatureRequest signatureRequest,
			Device device) {
		// You'll be notified here if a sale process needs signature
		// verification
		// See documentation
		Log.d("Handpoint", "endOfTransaction :: signatureRequest = "
				+ signatureRequest.getMerchantReceipt());
		Log.d("Handpoint", "endOfTransaction :: device = " + device.getName());
	}

	@Override
	public void endOfTransaction(TransactionResult transactionResult,
			Device device) {

		Log.d("Handpoint", "endOfTransaction :: transactionResult = "
				+ transactionResult.getFinStatus());
		Log.d("Handpoint", "endOfTransaction :: device = " + device.getName());

		String result = "";

		if (transactionResult.getFinStatus() == FinancialStatus.AUTHORISED) {
			// Whenever a transaction ends (correctly or due to an error) here
			// you'll be notified
			result = "AUTHORISED " + result;
			mCallbackListener.onTransactionComplete(transactionResult);
			return;
		} else if (transactionResult.getFinStatus() == FinancialStatus.DECLINED) {
			// Whenever a transaction ends (correctly or due to an error) here
			// you'll be notified
			result = "DECLINED " + result;
		} else if (transactionResult.getFinStatus() == FinancialStatus.PROCESSED) {
			// Whenever a transaction ends (correctly or due to an error) here
			// you'll be notified
			result = "PROCESSED " + result;
		} else if (transactionResult.getFinStatus() == FinancialStatus.FAILED) {
			// Whenever a transaction ends (correctly or due to an error) here
			// you'll be notified
			result = "FAILED " + result;
		} else if (transactionResult.getFinStatus() == FinancialStatus.CANCELLED) {
			// Whenever a transaction ends (correctly or due to an error) here
			// you'll be notified
			result = "CANCELLED " + result;
		}

		mCallbackListener.onTransactionComplete(result);
	}

	@Override
	public void deviceDiscoveryFinished(List<Device> devices) {

		Log.d("Handpoint", "deviceDiscoveryFinished ::Target Device name "
				+ deviceNameString);
		for (Device device : devices) {
			Log.d("Handpoint", "deviceDiscoveryFinished :: " + device.getName());
			if (device.getName().equals(deviceNameString)) {
				// We'll remember the device for this session, but is cool that
				// you do too
				this.device = device;
				this.api.useDevice(this.device);
				mCallbackListener.onDeviceConnect(device.getName());
				// pay();
				return;
			}
		}
		mCallbackListener.onDeviceNotFound();
	}
}
