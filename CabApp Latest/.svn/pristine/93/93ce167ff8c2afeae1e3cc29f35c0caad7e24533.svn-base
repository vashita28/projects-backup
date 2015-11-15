package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.model.ForgotPassword;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class ChangePasswordActivity extends RootActivity {

	private static final String TAG = ChangePasswordActivity.class
			.getSimpleName();

	EditText etEmail, etEmailPassword, etNewPassword, etConfirmPassword;
	TextView textLogIn, textHiddenLogIn;
	ImageView ivBack;

	Bundle changePasswordBundle;

	private ProgressDialog dialogChangePassword;

	View activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		mContext = this;

		changePasswordBundle = getIntent().getExtras();
		if (Constants.isDebug)
			Log.e(TAG, "changePasswordBundle " + changePasswordBundle);

		activityRootView = findViewById(R.id.relMain);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etEmailPassword = (EditText) findViewById(R.id.etEmailPassword);
		etNewPassword = (EditText) findViewById(R.id.etNewPass);
		etConfirmPassword = (EditText) findViewById(R.id.etConfirmPass);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new onClickListener());
		textLogIn = (TextView) findViewById(R.id.tvLogIn);
		textLogIn.setOnClickListener(new onClickListener());
		textHiddenLogIn = (TextView) findViewById(R.id.tvHiddenLogIn);
		textHiddenLogIn.setOnClickListener(new onClickListener());

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
							textHiddenLogIn.setVisibility(View.VISIBLE);
							textLogIn.setVisibility(View.GONE);
						} else {

							textHiddenLogIn.setVisibility(View.GONE);
							textLogIn.setVisibility(View.VISIBLE);
						}
					}
				});

		if (changePasswordBundle != null) {
			etEmail.setText(changePasswordBundle.getString("email"));
			Util.setEmailOrUserName(mContext,
					changePasswordBundle.getString("email"));
			etEmail.setClickable(false);

			if (changePasswordBundle.containsKey("fromLogIn")) {
				boolean isFromLogIn = changePasswordBundle
						.getBoolean("fromLogIn");
				if (isFromLogIn) {
					etEmail.setText(changePasswordBundle.getString("email"));
					Util.setEmailOrUserName(mContext,
							changePasswordBundle.getString("email"));
					etEmail.setClickable(false);
				}
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	class onClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.tvLogIn:
				LogInButtonCall();

				break;

			case R.id.tvHiddenLogIn:
				LogInButtonCall();

				break;

			case R.id.ivBack:
				Intent intentForgotPassword = new Intent(
						ChangePasswordActivity.this,
						ForgotPasswordActivity.class);
				startActivity(intentForgotPassword);
				finish();
				break;

			}
		}
	}

	void LogInButtonCall() {
		Util.hideSoftKeyBoard(ChangePasswordActivity.this, textLogIn);

		String email = etEmail.getText().toString().trim();
		String emailPassword = etEmailPassword.getText().toString().trim();
		String newPassword = etNewPassword.getText().toString().trim();
		String confirmPassword = etConfirmPassword.getText().toString().trim();

		if (email.isEmpty() || emailPassword.isEmpty() || newPassword.isEmpty()
				|| confirmPassword.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else if (!email.matches(Constants.EMAIL_PATTERN)) {
			Util.showToastMessage(mContext,
					"Please enter a valid email address", Toast.LENGTH_LONG);
		} else if (newPassword.length() < 6 && confirmPassword.length() < 6) {
			Util.showToastMessage(mContext,
					"Password should be minimum of 6 characters",
					Toast.LENGTH_LONG);
		} else if (!newPassword.equals(confirmPassword)) {
			Util.showToastMessage(mContext, "Your passwords don't match",
					Toast.LENGTH_LONG);
		} else {

			if (NetworkUtil.isNetworkOn(ChangePasswordActivity.this)) {
				if (!email.isEmpty() && !emailPassword.isEmpty()
						&& !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
					ChangePasswordTask changePasswordTask = new ChangePasswordTask();
					changePasswordTask.sEmail = email;
					changePasswordTask.sEmailPassword = emailPassword;
					changePasswordTask.sNewPassword = newPassword;
					changePasswordTask.execute();

				}
			} else {
				Util.showToastMessage(ChangePasswordActivity.this,
						getResources().getString(R.string.no_network_error),
						Toast.LENGTH_LONG);
			}

		}
	}

	public class ChangePasswordTask extends AsyncTask<String, Void, String> {
		public String sEmail, sEmailPassword, sNewPassword;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogChangePassword = new ProgressDialog(
					ChangePasswordActivity.this);
			dialogChangePassword.setMessage("Logging in...");
			dialogChangePassword.setCancelable(false);
			dialogChangePassword.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ForgotPassword changePassword = new ForgotPassword(
					ChangePasswordActivity.this, sEmail, sEmailPassword,
					sNewPassword);
			String response = changePassword.ChangePassword();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					return "success";
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

			if (dialogChangePassword != null)
				dialogChangePassword.dismiss();

			if (result.equals("success")) {
				Util.setAccessToken(mContext, "");
				Util.setPassword(mContext, sNewPassword);
				Util.setLogInPassword(mContext, "");

				Util.showToastMessage(mContext,
						"Password changed successfully", Toast.LENGTH_LONG);
				Util.finishActivity(mContext);

				Intent intentChangePassword = new Intent(
						ChangePasswordActivity.this, LogInActivity.class);// MainActivity
				intentChangePassword.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intentChangePassword.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentChangePassword);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

}
