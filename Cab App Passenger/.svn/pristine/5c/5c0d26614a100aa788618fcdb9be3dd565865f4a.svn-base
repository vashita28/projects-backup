package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.ForgotPasswordTask;
import com.example.cabapppassenger.util.Constant;

public class ForgotPasswordActivity extends Activity {

	static Context mContext;
	ImageView ivBack;
	TextView btn_resetpassword;
	EditText et_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgotpassword);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		et_email = (EditText) findViewById(R.id.et_email);
		mContext = this;
		btn_resetpassword = (TextView) findViewById(R.id.tvresetpassword);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
				hideKeybord(v);
			}
		});

		btn_resetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (et_email.getText().toString() != null
						&& et_email.length() > 0) {
					if (Constant.isEmailAddressValid(et_email.getText()
							.toString())) {
						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

							ForgotPasswordTask task = new ForgotPasswordTask(
									mContext);
							task.email = et_email.getText().toString();
							task.execute(Constant.passengerURL
									+ "ws/v2/passenger/forgotpassword");

						} else {
							Toast.makeText(mContext, "No network connection",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Constant.alertdialog("invalidemail", mContext);
					}
				} else {
					Constant.alertdialog("emptyfields", mContext);
				}
			}
		});
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}
}