package com.android.cabapp.handpoint;

import java.math.BigInteger;
import java.util.List;

import android.content.Context;

import com.handpoint.api.ConnectionMethod;
import com.handpoint.api.Currency;
import com.handpoint.api.Device;
import com.handpoint.api.Events.Required;
import com.handpoint.api.FinancialStatus;
import com.handpoint.api.Hapi;
import com.handpoint.api.HapiFactory;
import com.handpoint.api.SignatureRequest;
import com.handpoint.api.TransactionResult;

public class MyClass implements Required {
	Hapi api;
	Device device;

	public MyClass(Context context) {
		initApi(context);
	}

	public void initApi(Context context) {
		String sharedSecret = "0102030405060708091011121314151617181920212223242526272829303132";
		this.api = HapiFactory.getAsyncInterface(this, context)
				.defaultSharedSecret(sharedSecret);

		// The api is now initialized. Yay! we've even set a default shared
		// secret!
		// But we need to connect to a device to start taking payments.
		// Let's search for them:
		this.api.listDevices(ConnectionMethod.BLUETOOTH);
		// This triggers the search, you should expect the results in the
		// listener defined above

		api.useDevice(new Device("Name", "Port", "Address",
				ConnectionMethod.SIMULATOR));
	}

	public boolean pay() {
		return this.api.sale(new BigInteger("1000"), Currency.GBP);
	}

	@Override
	public void signatureRequired(SignatureRequest signatureRequest,
			Device device) {
		// You'll be notified here if a sale process needs signature
		// verification
		// See documentation
	}

	@Override
	public void endOfTransaction(TransactionResult transactionResult,
			Device device) {
		if (transactionResult.getFinStatus() == FinancialStatus.AUTHORISED) {
			// ...
		}
	}

	@Override
	public void deviceDiscoveryFinished(List<Device> devices) {
		for (Device device : devices) {
			if (device.getName().equals("MyDeviceName")) {
				// We'll remember the device for this session, but is cool that
				// you do too
				this.device = device;
				this.api.useDevice(this.device);
			}
		}
	}
}