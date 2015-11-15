package com.coudriet.snapnshare.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.coudriet.snapnshare.android.R;

public class SignUpActivity extends Activity {
	
	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText mEmail;
	protected TextView mCreateAccountTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);
		
		mUsername = (EditText)findViewById(R.id.usernameField);
		mPassword = (EditText)findViewById(R.id.passwordField);
		mEmail = (EditText)findViewById(R.id.emailField);
		mCreateAccountTextView = (TextView)findViewById(R.id.signUpTextView);
		mCreateAccountTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				String email = mEmail.getText().toString();
				
				username = username.trim();
				password = password.trim();
				email = email.trim();
				
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(username);
				boolean found = matcher.find();
				
				if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage(R.string.signup_error_message)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else if (username.startsWith("1") || username.startsWith("2") || username.startsWith("3") || username.startsWith("4") || username.startsWith("5") || username.startsWith("6") || username.startsWith("7") || username.startsWith("8") || username.startsWith("9") || username.startsWith("0"))
			    {
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage("The username must not start with a number! Please try again by entering a username starting with a letter.")
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
			    }
				else if (found)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
					builder.setMessage("The username must not contain any spaces! Please try again by entering a username without spaces.")
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					// create the new user!
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					newUser.signUpInBackground(new SignUpCallback() {
						@Override
						public void done(ParseException e) {
							setProgressBarIndeterminateVisibility(false);
							
							if (e == null) {
								// Success!
								Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							}else {
								AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
								builder.setMessage(R.string.connectivity_error)
									.setTitle(R.string.signup_error_title)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});
				}
			}
		});
	}
}
