package com.coudriet.snapnshare.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.coudriet.snapnshare.android.R;

public class PasswordResetActivity extends Activity {
	
	protected EditText mEmail;
	protected TextView mSubmitTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_password_reset);
		
		mEmail = (EditText)findViewById(R.id.emailField);
		mSubmitTextView = (TextView)findViewById(R.id.resetTextView);
		mSubmitTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = mEmail.getText().toString();
				
				email = email.trim();
				
				if (email.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
					builder.setMessage(R.string.password_reset_error_message)
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					// create the new user!
					setProgressBarIndeterminateVisibility(true);
					
					ParseUser.requestPasswordResetInBackground(email,
                            new RequestPasswordResetCallback() {
						public void done(ParseException e) {
							
							if (e == null) {
								// Success!
								
								hideSoftKeyboard(PasswordResetActivity.this);
								
								String requestMessage = "Please check your email for instructions on reseting your password.";
								
								AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
								builder.setMessage(requestMessage)
									.setTitle("Email Sent!")
									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											
											final Handler handler = new Handler();
								    		 handler.postDelayed(new Runnable() {
								    		   @Override
								    		   public void run() {
								    		    
													Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
													intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
													intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
													startActivity(intent);
								    			}
								    		 
								    		 }, 1000);
										}
								});

								AlertDialog dialog1 = builder.create();
								dialog1.show();
							}
							else {
								AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
								//change network status message -1
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
	
	public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
