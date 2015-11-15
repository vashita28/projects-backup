package com.coudriet.snapnshare.android;

import com.parse.ParseUser;
import com.snapshare.utility.Common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEmailActivity extends Activity {
	public static String TAG = "EditEmailActivity";

	private ActionBar actionBar;
	private EditText mEmailFeild;
	private Button mSaveBtn;
	private String mEmail = null;

	private ParseUser currentUser;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_email_address);

		if (ParseUser.getCurrentUser() != null)
			currentUser = ParseUser.getCurrentUser();

		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		mEmail = getIntent().getExtras().getString("EMAIL");
		Log.e(TAG, "EMAIL:: " + mEmail);

		mEmailFeild = (EditText) findViewById(R.id.emailField);
		mSaveBtn = (Button) findViewById(R.id.saveButton);

		mEmailFeild.setText(mEmail);
		mEmailFeild.setSelection(mEmailFeild.getText().length());

		mEmailFeild.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mSaveBtn.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		mSaveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = mEmailFeild.getText().toString().trim();

				if (email.isEmpty()) {

					String msg = getString(R.string.signup_error_message_email);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(EditEmailActivity.this, msg, tit);

					// Toast.makeText(EditEmailActivity.this,
					// "Please enter a email address", Toast.LENGTH_SHORT)
					// .show();
				} else if (!email.matches(EMAIL_PATTERN)) {

					String msg = getString(R.string.signup_error_message_valid_email);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(EditEmailActivity.this, msg, tit);

					// Toast.makeText(EditEmailActivity.this,
					// "Please enter a valid email address",
					// Toast.LENGTH_SHORT).show();

				} else {
					currentUser.setEmail(email);
					currentUser.saveInBackground();

					finish();

					Intent intent = new Intent(EditEmailActivity.this,
							SettingsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

					Toast.makeText(EditEmailActivity.this,
							"Email successfully updated.", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});
	}

	private void setupActionBar() {
		Log.e("setupActionBar", "setupActionBar");
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
