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
import com.android.cabapp.fragments.JobAcceptedFragment;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.NoShow;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class NoShowTask extends AsyncTask<String, Void, String> {
	private static final String TAG = JobAcceptedFragment.class.getSimpleName();

	public String szJobId;
	Context mContext;
	ProgressDialog progressDialog;

	public NoShowTask(Context context, ProgressDialog progressDialog) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		NoShow noShow = new NoShow(szJobId, mContext);
		String response = noShow.NoShowCall();

		if (Constants.isDebug)
			Log.e(TAG, "NoShow response::> " + response);

		try {
			JSONObject jObject = new JSONObject(response);
			String errorMessage = "";
			if (jObject.has("success")
					&& jObject.getString("success").equals("true")) {
				DriverSettingDetails driverSettings = new DriverSettingDetails(
						Util.mContext);
				driverSettings.retriveDriverSettings(Util.mContext);
				return "success";
			} else if (jObject.has("success")
					&& jObject.getString("success").equals("false")) {
				errorMessage = jObject.getString("error");
				return errorMessage;
			} else {
				if (jObject.has("errors")) {
					JSONArray jErrorsArray = jObject.getJSONArray("errors");

					errorMessage = jErrorsArray.getJSONObject(0).getString(
							"message");
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

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

		if (result.equals("success")) {

			Fragment fragment = null;
			fragment = new com.android.cabapp.fragments.JobsFragment();
			((MainActivity) mContext)
					.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
			if (fragment != null)
				((MainActivity) mContext).replaceFragment(fragment, true);
		} else {
			Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
		}

	}

}
