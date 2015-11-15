package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.task.ChangePasswordTask;
import com.example.cabapppassenger.util.Constant;

public class ResetPasswordActivity extends Activity {

	TextView btn_login, tvLogIn_scroll;
	static Context mContext;
	String email;
	View activityRootView;
	ImageView ivBack;
	EditText et_email, et_password, et_newpassword, et_newconfirm_password;
	boolean flag_fromforgotpass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reset_password);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mContext = this;
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				hideKeybord(v);
			}
		});
		tvLogIn_scroll = (TextView) findViewById(R.id.tvLogIn_scroll);
		et_email = (EditText) findViewById(R.id.et_email);
		btn_login = (TextView) findViewById(R.id.tvLogIn);
		et_password = (EditText) findViewById(R.id.et_emailpassword);
		et_newpassword = (EditText) findViewById(R.id.et_newpass);
		Intent into = getIntent();
		email = into.getStringExtra("Email");
		flag_fromforgotpass = into.getBooleanExtra("Flag", false);
		Log.e("Email in resetpass", email);
		if (email != null && !email.matches("null"))
			et_email.setText(email);

		if (flag_fromforgotpass) {
			ivBack.setVisibility(View.VISIBLE);
			btn_login.setText("Change Password");
			tvLogIn_scroll.setText("Change Password");

		} else {
			ivBack.setVisibility(View.GONE);
			btn_login.setText("Log in");
			tvLogIn_scroll.setText("Log in");
		}

		et_newconfirm_password = (EditText) findViewById(R.id.et_confirmpass);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changePassword();
				hideKeybord(v);
			}
		});

		tvLogIn_scroll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changePassword();
				hideKeybord(v);

			}
		});

		activityRootView = findViewById(R.id.rel_main);
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
						if (heightDiff > 100) { // if more than 100 pixels, its
							// Log.e("Visible", "Visible");
							btn_login.setVisibility(View.GONE);
							tvLogIn_scroll.setVisibility(View.VISIBLE);
						} else {
							// Log.e("Not visible", "not visible");
							btn_login.setVisibility(View.VISIBLE);
							tvLogIn_scroll.setVisibility(View.GONE);
						}
					}
				});
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public void changePassword() {
		if (et_email.length() > 0 && et_password.length() > 0
				&& et_newpassword.length() > 0
				&& et_newconfirm_password.length() > 0) {
			if (Constant.isEmailAddressValid(et_email.getText().toString())) {
				if (et_newpassword.length() >= 6
						&& et_newconfirm_password.length() >= 6) {
					if (!et_password.getText().toString()
							.equals(et_newpassword.getText().toString())) {
						if (et_newpassword
								.getText()
								.toString()
								.equals(et_newconfirm_password.getText()
										.toString())) {

							ChangePasswordTask task = new ChangePasswordTask(
									mContext);
							task.email = et_email.getText().toString();
							task.oldpassword = Constant.md5(et_password
									.getText().toString());
							task.newpassword = Constant.md5(et_newpassword
									.getText().toString());
							task.flag_fromresetpassword = flag_fromforgotpass;
							task.execute(Constant.passengerURL
									+ "ws/v2/passenger/changepassword");
						} else {
							Constant.alertdialog("passwordmatches", mContext);
						}

					} else {
						Constant.alertdialog("notpasswordmatches", mContext);
					}
				} else {
					Constant.alertdialog("passwordlessthan", mContext);
				}
			} else {
				Constant.alertdialog("invalidemail", mContext);
			}
		} else {
			// empty fields
			Constant.alertdialog("emptyfields", mContext);
		}
	}
}