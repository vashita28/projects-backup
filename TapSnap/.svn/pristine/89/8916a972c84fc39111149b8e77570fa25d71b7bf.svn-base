package com.coudriet.snapnshare.android;

import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.coudriet.snapnshare.android.R;

public class LoginActivity extends Activity {

	protected EditText mUsername;
	protected EditText mPassword;
	protected TextView mLoginTextView;
	protected TextView mPasswordResetTextView;
	protected TextView mSignUpTextView;
	private Button btnfb;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);
		
		mPasswordResetTextView = (TextView)findViewById(R.id.resetTextView);
		mPasswordResetTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
				startActivity(intent);
			}
		});
		
		mSignUpTextView = (TextView)findViewById(R.id.signUpTextView);
		mSignUpTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
		
		mUsername = (EditText)findViewById(R.id.usernameField);
		mPassword = (EditText)findViewById(R.id.passwordField);
		mLoginTextView = (TextView)findViewById(R.id.loginTextView);
		mLoginTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				
				username = username.trim();
				password = password.trim();
				
				if (username.isEmpty() || password.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setMessage(R.string.login_error_message)
						.setTitle(R.string.login_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					// Login
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException e) {
							setProgressBarIndeterminateVisibility(false);
							
							if (e == null) {
								// Success!
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							}
							else {
								AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
								//change network status message -1
								builder.setMessage(R.string.connectivity_error)
									.setTitle(R.string.login_error_title)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});
				}
			}
		});
		
		btnfb = (Button) findViewById(R.id.btnFB);
		btnfb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onLoginButtonClicked();

			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	//callin parseFacebookUtils
	private void onLoginButtonClicked() {
	
		progressDialog = ProgressDialog.show(this, "", "Logging in...", true); 
		
	    List<String> permissions = Arrays.asList("basic_info", "user_about_me", "user_relationships", "user_birthday", "user_location", "email");
	    ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException err) {
				
				progressDialog.dismiss();
				
	            if (user == null) {   
	            	Toast.makeText(LoginActivity.this, "Uh oh. The user cancelled the Facebook login.", Toast.LENGTH_SHORT).show();
	            } else if (user.isNew()) {
	            	Toast.makeText(LoginActivity.this, "User signed up and logged in through Facebook!", Toast.LENGTH_SHORT).show();
	                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
	            } else {
	            	Toast.makeText(LoginActivity.this, "User logged in through Facebook!", Toast.LENGTH_SHORT).show();
	                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
	            }
				
			}
	    });
	}
	
}
