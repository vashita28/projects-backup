package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.ResendCodeTask;
import com.example.cabapppassenger.task.VerifyMobileNoTask;
import com.example.cabapppassenger.util.Constant;

public class VerificationActivity extends Activity {

	static Context mContext;
	EditText et_first_code, et_second_code, et_third_code, et_fourth_code;
	TextView btn_done, btn_resend_code;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Handler mHandler;
	String verification_code, mobile_no, internation_code;
	ImageView iv_menu;;
	boolean from_login, from_signup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_verificationcode);
		mContext = this;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		iv_menu = (ImageView) findViewById(R.id.ivBack);
		iv_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Constant.hideKeybord(v, mContext);
			}
		});
		Intent verify = getIntent();
		from_login = verify.getBooleanExtra("From_login", false);
		from_signup = verify.getBooleanExtra("FromSignup", false);
		internation_code = verify.getStringExtra("international_code");
		mobile_no = verify.getStringExtra("mobileno");
		et_first_code = (EditText) findViewById(R.id.et_first_code);
		et_first_code.addTextChangedListener(new textchangelistener(1));
		et_second_code = (EditText) findViewById(R.id.et_second_code);
		et_second_code.addTextChangedListener(new textchangelistener(2));
		et_third_code = (EditText) findViewById(R.id.et_third_code);
		et_third_code.addTextChangedListener(new textchangelistener(3));
		et_fourth_code = (EditText) findViewById(R.id.et_fourthcode);
		btn_done = (TextView) findViewById(R.id.tvdone);
		btn_resend_code = (TextView) findViewById(R.id.tvresend);
		btn_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_first_code.length() > 0 && et_second_code.length() > 0
						&& et_third_code.length() > 0
						&& et_fourth_code.length() > 0) {
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

						verification_code = et_first_code.getText().toString()
								+ et_second_code.getText().toString()
								+ et_third_code.getText().toString()
								+ et_fourth_code.getText().toString();
						if (verification_code != null) {
							Constant.hideKeybord(v, mContext);
							VerifyMobileNoTask task = new VerifyMobileNoTask(
									mContext);
							task.clientkey = shared_pref.getString("ClientKey",
									"");
							task.mHandler = mHandler;// to change the
							// mobile no
							task.verification_code = verification_code;
							if (from_login) {
								task.navigate_loginpage = true;

							} else if (from_signup) {

							} else
								task.navigate_myaccounts = true;

							task.execute(Constant.passengerURL
									+ "ws/v2/passenger/verifymobile/");
						}

					} else {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Constant.alertdialog("emptyfields", mContext);
				}
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					finish();
				}

			}

		};
		btn_resend_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

					ResendCodeTask task = new ResendCodeTask(mContext);
					task.clientkey = shared_pref.getString("ClientKey", "");
					task.international_code = internation_code;
					task.mobile_no = mobile_no;
					task.execute(Constant.passengerURL
							+ "ws/v2/passenger/resendVerificationPin/");

				} else {
					Toast.makeText(mContext, "No network connection",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public class textchangelistener implements TextWatcher {

		int position;

		public textchangelistener(int position) {
			this.position = position;
		}

		@Override
		public void afterTextChanged(Editable text) {
			// TODO Auto-generated method stub
			if (position == 1 && text.length() > 0)
				et_second_code.requestFocus();

			if (position == 2 && text.length() > 0)
				et_third_code.requestFocus();

			if (position == 3 && text.length() > 0)
				et_fourth_code.requestFocus();

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}
	}

}
