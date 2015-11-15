package com.android.cabapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.android.cabapp.R;

public class LandingActivity extends CabAppParentActivity {

	private static final String TAG = LandingActivity.class.getSimpleName();

	TextView textSignUp, textLogIn;
	static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_landing);
		mContext = this;

		textSignUp = (TextView) findViewById(R.id.tvSignUp);
		textLogIn = (TextView) findViewById(R.id.tvLogIn);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		textSignUp.setOnClickListener(new TextViewOnClickListener());
		textLogIn.setOnClickListener(new TextViewOnClickListener());
	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvSignUp:

				Intent intentSignUp = new Intent(LandingActivity.this,
						SignUp_AboutYou_Activity.class);
				intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentSignUp);

				break;
			case R.id.tvLogIn:
				Intent intent = new Intent(LandingActivity.this,
						LogInActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;

			}
		}

	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

}
