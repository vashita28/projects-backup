package com.android.cabapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.cabapp.datastruct.json.DriverSettings;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.AppValues;

public class DriverSettingsTask extends AsyncTask<String, Void, DriverSettings> {
	private static final String TAG = DriverSettingsTask.class.getSimpleName();

	public Context mContext;

	@Override
	protected DriverSettings doInBackground(String... params) {
		// TODO Auto-generated method stub
		DriverSettingDetails driverSettings = new DriverSettingDetails(mContext);
		driverSettings.retriveDriverSettings(mContext);

		return AppValues.driverSettings;
	}

	@Override
	protected void onPostExecute(DriverSettings driverDetails) {
		// TODO Auto-generated method stub
		super.onPostExecute(driverDetails);
		if (AppValues.driverSettings != null)
			Log.e(TAG, "Driver settings details::> " + AppValues.driverSettings.getCurrencySymbol());
	}

}