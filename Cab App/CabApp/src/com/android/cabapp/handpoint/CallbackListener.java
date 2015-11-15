package com.android.cabapp.handpoint;

import com.handpoint.api.TransactionResult;

public interface CallbackListener {
	void onDeviceConnect(String devicename);

	void onTransactionComplete(String response);

	void onDeviceNotFound();

	void onTransactionComplete(TransactionResult transactionResult);
}
