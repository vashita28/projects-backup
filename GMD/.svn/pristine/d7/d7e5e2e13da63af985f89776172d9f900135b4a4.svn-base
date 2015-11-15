package co.uk.pocketapp.gmd.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import co.uk.pocketapp.gmd.ui.ForgotPassword;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.HttpConnect;

public class ForgotPasswordTask extends AsyncTask<Void, Void, String> {
	final String szURL = AppValues.SERVER_URL
			+ "access_token=%s&action=forgotpwd";
	public ProgressBar progressBar;
	public Context mContext;

	public Handler handler;

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		String szResponse = "";
		try {
			szResponse = new HttpConnect().connect(String.format(szURL,
					AppValues.ACCESS_TOKEN));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return szResponse;
	}

	@Override
	protected void onPostExecute(String szResult) {
		super.onPostExecute(szResult);
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
		if (!szResult.equals("")) {
			parseResponse(szResult);
			Log.d("ForgotPasswordTask-()", "onPostExecute SUCCESS");

		} else {
			Log.d("ForgotPasswordTask-()", "onPostExecute ERROR");
		}
	}

	private void parseResponse(String szResult) {
		try {

			JSONObject jsonObj = new JSONObject(szResult);

			JSONObject jsonHeadObj = jsonObj.getJSONObject("head");
			String szStatus = jsonHeadObj.getString("status");

			if (szStatus != null && szStatus.equalsIgnoreCase("1")) {

				JSONObject bodyObj = jsonObj.getJSONObject("body");
				String szPhoneNumber = bodyObj.getString("phone");
				Log.d("ForgotPasswordTask : ", "Phone :: " + szPhoneNumber);
				ForgotPassword.ms_szPhoneNumber = szPhoneNumber;

				if (handler != null) {
					handler.sendEmptyMessage(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
