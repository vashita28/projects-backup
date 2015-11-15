package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.model.ForgotPassword;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class ForgotPasswordActivity extends RootActivity {

	private static final String TAG = ForgotPasswordActivity.class
			.getSimpleName();

	EditText etEmail;
	TextView textResetPassword, textHiddenResetPassword;
	Context mContext;

	private ProgressDialog dialogForgotPassword;

	View activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reset_password);

		mContext = this;

		activityRootView = findViewById(R.id.relMain);
		etEmail = (EditText) findViewById(R.id.etEmail);
		textResetPassword = (TextView) findViewById(R.id.tvResetPassword);
		textResetPassword.setOnClickListener(new TextViewOnClickListener());
		textHiddenResetPassword = (TextView) findViewById(R.id.tvHiddenResetPassword);
		textHiddenResetPassword
				.setOnClickListener(new TextViewOnClickListener());

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						// r will be populated with the coordinates of your view
						// that area still visible.
						activityRootView.getWindowVisibleDisplayFrame(r);

						int heightDiff = activityRootView.getRootView()
								.getHeight() - (r.bottom - r.top);
						if (heightDiff > 100) {
							textHiddenResetPassword.setVisibility(View.VISIBLE);
							textResetPassword.setVisibility(View.GONE);
						} else {

							textHiddenResetPassword.setVisibility(View.GONE);
							textResetPassword.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvResetPassword:
				ResetButtonCall();

				break;

			case R.id.tvHiddenResetPassword:
				ResetButtonCall();

				break;

			}
		}

	}

	void ResetButtonCall() {
		Util.hideSoftKeyBoard(ForgotPasswordActivity.this, textResetPassword);

		String sEmail = etEmail.getText().toString().trim();
		if (sEmail.isEmpty()) {
			Toast.makeText(mContext,
					"Please enter your registered email address",
					Toast.LENGTH_SHORT).show();
		} else if (!sEmail.matches(Constants.EMAIL_PATTERN)) {
			Toast.makeText(mContext, "Please enter a valid email address",
					Toast.LENGTH_SHORT).show();
		} else {

			if (NetworkUtil.isNetworkOn(ForgotPasswordActivity.this)) {
				if (!sEmail.isEmpty()) {
					dialogForgotPassword = new ProgressDialog(
							ForgotPasswordActivity.this);
					dialogForgotPassword.setMessage("Loading...");
					dialogForgotPassword.setCancelable(false);
					dialogForgotPassword.show();

					// textResetPassword.setEnabled(false);

					ForgotPasswordTask forgotPasswordTask = new ForgotPasswordTask();
					forgotPasswordTask.szEmail = sEmail;
					forgotPasswordTask.execute();

				}
			} else {
				Util.showToastMessage(ForgotPasswordActivity.this,
						getResources().getString(R.string.no_network_error),
						Toast.LENGTH_LONG);
			}
		}
	}

	public class ForgotPasswordTask extends AsyncTask<String, Void, String> {
		public String szEmail = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ForgotPassword forgetPassword = new ForgotPassword(
					ForgotPasswordActivity.this, szEmail);
			String response = forgetPassword.isForgotPassword();
			try {
				JSONObject jObject = new JSONObject(response);
				Log.e(TAG, "JSONObject " + response.toString());
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				}
				return "success";
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

			if (dialogForgotPassword != null)
				dialogForgotPassword.dismiss();

			if (result.equals("success")) {
				Util.showToastMessage(mContext,
						"A new password has been sent to your email",
						Toast.LENGTH_LONG);
				Util.finishActivity(mContext);
				Util.setPassword(mContext, "");

				Bundle bundle = new Bundle();
				bundle.putString("email", szEmail);
				Intent intentChangePassword = new Intent(
						ForgotPasswordActivity.this,
						ChangePasswordActivity.class);
				intentChangePassword.putExtras(bundle);
				startActivity(intentChangePassword);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

}
