package com.hoteltrip.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

/**
 * Created by Pramod on 8/5/13.
 */
public class LoginActivity extends BaseSherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.hide();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		// startActivity(new Intent(LoginActivity.this, LoginActivity.class));
		Log.d("LoginActivity", "OnOptionsItemSelected");
		return true;
	}

	public void login(View view) {
		switch (view.getId()) {
		case R.id.btn_login:
			startActivity(new Intent(LoginActivity.this,
					FindHotelActivity.class));
			finish();
			break;
		default:
			break;
		}
	}

	// @Override
	// public void onClick(View view) {
	// Log.v("**********************", "**************************");
	// switch (view.getId()) {
	// case R.id.btn_login:
	// startActivity(new Intent(LoginActivity.this, MainActivity.class));
	// break;
	// default:
	// break;
	// }
	// }
}
