package com.android.cabapp.model;

import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;

import com.android.cabapp.datastruct.json.DriverSettings;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.google.gson.Gson;

public class DriverSettingDetails {
	private static final String TAG = DriverSettingDetails.class
			.getSimpleName();

	Connection connection;
	Context mContext;

	public DriverSettingDetails(Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		this.mContext = context;
	}

	public DriverSettings retriveDriverSettings(Context context) {
		connection.connect(Constants.DRIVER_SETTINGS);
		DriverSettings driverSettings = null;

		Gson gson = new Gson();

		if (connection.mInputStream != null) {
			Reader reader = new InputStreamReader(connection.mInputStream);
			driverSettings = gson.fromJson(reader, DriverSettings.class);

			if (driverSettings != null)
				AppValues.driverSettings = driverSettings;

			if (driverSettings.getName().equalsIgnoreCase("london"))
				Util.setCitySelectedLondon(mContext, true);
			else
				Util.setCitySelectedLondon(mContext, false);

			if (driverSettings != null
					&& driverSettings.getChipAndPin() != null) {
				String sChipAndPin = String.valueOf(driverSettings
						.getChipAndPin());
				Util.setPOSDeviceName(mContext, sChipAndPin);
			}

		}
		return driverSettings;

	}

}
