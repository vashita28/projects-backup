package com.android.cabapp.async;

import android.content.Context;
import android.os.AsyncTask;

import com.android.cabapp.datastruct.json.DriverDetails;
import com.android.cabapp.fragments.MyFiltersFragment;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.util.AppValues;

public class DriverDetailsTask extends AsyncTask<String, Void, DriverDetails> {
	private static final String TAG = DriverDetailsTask.class.getSimpleName();

	public Context mContext;

	public boolean isFromMyFilters = false;

	@Override
	protected DriverDetails doInBackground(String... params) {
		// TODO Auto-generated method stub
		DriverAccountDetails driverAccount = new DriverAccountDetails(mContext);
		driverAccount.retriveAccountDetails(mContext);
		return AppValues.driverDetails;
	}

	@Override
	protected void onPostExecute(DriverDetails driverDetails) {
		// TODO Auto-generated method stub
		super.onPostExecute(driverDetails);

		if (isFromMyFilters)
			if (MyFiltersFragment.mHandlerMyFilter != null) {
				MyFiltersFragment.mHandlerMyFilter.sendEmptyMessage(1);
			}
	}

}
