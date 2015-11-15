package com.coudriet.snapnshare.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.snapshare.utility.Common;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUsernameActivity extends Activity {
	public static String TAG = "EditUsernameActivity";

	private ActionBar actionBar;
	private EditText mUsernameField;
	private Button mSaveBtn;
	private String mUsername = null;

	private ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_username);

		if (ParseUser.getCurrentUser() != null)
			currentUser = ParseUser.getCurrentUser();

		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		mUsername = getIntent().getExtras().getString("USERNAME");
		Log.e(TAG, "USERNAME:: " + mUsername);

		mUsernameField = (EditText) findViewById(R.id.usernameField);
		mSaveBtn = (Button) findViewById(R.id.saveButton);

		mUsernameField.setText(mUsername);
		mUsernameField.setSelection(mUsernameField.getText().length());

		mUsernameField
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mSaveBtn.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		mSaveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = mUsernameField.getText().toString();
				username = username.trim();

				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(username);
				boolean found = matcher.find();

				if (username.isEmpty()) {

					String msg = getString(R.string.login_error_message_for_user);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(EditUsernameActivity.this, msg, tit);

					// Toast.makeText(EditUsernameActivity.this,
					// "Please enter username", Toast.LENGTH_SHORT).show();
				} else if (username.length() < 1 || username.length() > 17) {
					String msg = getString(R.string.signup_error_message_for_user_length);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(EditUsernameActivity.this, msg, tit);

					// Toast.makeText(
					// EditUsernameActivity.this,
					// "Username should be minimum 6 and  maximum 16 characters long.",
					// Toast.LENGTH_SHORT).show();
				} else if (username.startsWith("1") || username.startsWith("2")
						|| username.startsWith("3") || username.startsWith("4")
						|| username.startsWith("5") || username.startsWith("6")
						|| username.startsWith("7") || username.startsWith("8")
						|| username.startsWith("9") || username.startsWith("0")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditUsernameActivity.this);
					builder.setMessage(
							"The username must not start with a number! Please try again by entering a username starting with a letter.")
							.setTitle(R.string.signup_error_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				} else if (found) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditUsernameActivity.this);
					builder.setMessage(
							"The username must not contain any spaces! Please try again by entering a username without spaces.")
							.setTitle(R.string.signup_error_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					currentUser.setUsername(username);
					currentUser.saveInBackground();

					finish();

					Intent intent = new Intent(EditUsernameActivity.this,
							SettingsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

					Toast.makeText(EditUsernameActivity.this,
							"Username successfully updated.",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void setupActionBar() {

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
