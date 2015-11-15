package co.uk.pocketapp.gmd.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.tasks.LoginTask;
import co.uk.pocketapp.gmd.util.AppValues;

public class Activity_login extends Activity {
	private final String TAG = "LOG IN";
	// Button forget_Password_Button;
	Button log_In_Button;
	EditText user_Name;
	EditText password;
	Button forget_Password_Button;
	TextView DateNtime_View;
	String reportDate;

	// To apply font:
	TextView gmd_Heading;
	TextView report_log_in_Text;
	TextView page_Heading_Middle;
	TextView user_Name_TextView;
	TextView password_TextView;

	ProgressBar m_progress_Login;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		DateNtime_View = (TextView) findViewById(R.id.DateNtime);

		m_progress_Login = (ProgressBar) findViewById(R.id.progress_login);

		// forget_Password_Button=(Button)findViewById(R.id.forgetPassword);
		forget_Password_Button = (Button) findViewById(R.id.forgetPassword_Button);
		log_In_Button = (Button) findViewById(R.id.logInButton);

		// font implementation for user name:EditText
		user_Name = (EditText) findViewById(R.id.user_name_text);

		// font implementation for password:EditText
		password = (EditText) findViewById(R.id.password_text);
		password.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		user_Name.setImeOptions(EditorInfo.IME_ACTION_DONE);
		password.setImeOptions(EditorInfo.IME_ACTION_DONE);

		// font implementation for GMD main heading:TextView
		gmd_Heading = (TextView) findViewById(R.id.main_Heading);
		gmd_Heading.setTypeface(GMDApplication.fontHeading);
		// font implementation for page heading "report log in":TextView
		report_log_in_Text = (TextView) findViewById(R.id.pageTitle_ReportLogIn);
		report_log_in_Text.setTypeface(GMDApplication.fontText);
		// font implementation for middle heading:TextView
		page_Heading_Middle = (TextView) findViewById(R.id.page_heading_middle);
		page_Heading_Middle.setTypeface(GMDApplication.fontText);
		// font implementation for user name:TextView
		user_Name_TextView = (TextView) findViewById(R.id.user_name_view);
		user_Name_TextView.setTypeface(GMDApplication.fontText);
		// user name : EditText
		user_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		user_Name.setTypeface(GMDApplication.fontText);
		// font implementation for password:TextView
		password_TextView = (TextView) findViewById(R.id.password_view);
		password_TextView.setTypeface(GMDApplication.fontText);
		// password : EditText
		password.setTypeface(GMDApplication.fontText);

		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			reportDate = dateFormat.format(today);

			DateNtime_View.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Log.d(TAG, user_Name.getText().toString() + " "
				+ password.getText().toString());

		// forget Password Button:
		forget_Password_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent1 = new Intent(getBaseContext(),
						ForgotPassword.class);
				startActivity(myIntent1);
				user_Name.setText("");
				password.setText("");
			}

		});

		// log In Button:
		log_In_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String szUserName = user_Name.getText().toString().trim();
				String szPassword = password.getText().toString().trim();
				if (szUserName.length() == 0) {
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							Activity_login.this)
							.setTitle("Alert!")
							.setMessage("All fields are mandatory")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {

										}
									}).create();
					m_AlertDialog.setCanceledOnTouchOutside(false);
					m_AlertDialog.show();

				} else if (szPassword.length() == 0) {
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							Activity_login.this)
							.setTitle("Alert!")
							.setMessage("All fields are mandatory")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {

										}
									}).create();
					m_AlertDialog.setCanceledOnTouchOutside(false);
					m_AlertDialog.show();
				} else {
					Log.d("inside Log in button", "TASK1");
					// // calling async task for getting data from server
					// Download_XML_Task my_Async_Task = new
					// Download_XML_Task();
					// my_Async_Task.mContext = Activity_login.this;
					// my_Async_Task.execute();
					// Log.d("inside Log in button", "TASK2");

					// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					m_progress_Login.setVisibility(View.VISIBLE);
					LoginTask loginTask = new LoginTask();
					loginTask.mContext = Activity_login.this;
					loginTask.szUserName = szUserName;
					loginTask.szPassword = AppValues.getMd5Hash(szPassword);
					loginTask.progressBar = m_progress_Login;
					loginTask.execute();
					// } else {
					// // Toast.makeText(Activity_login.this,
					// // "Network not available", Toast.LENGTH_LONG)
					// // .show();
					// AlertDialog m_AlertDialog = new AlertDialog.Builder(
					// Activity_login.this)
					// .setTitle("Alert!")
					// .setMessage("Network not available")
					// .setPositiveButton("Ok",
					// new DialogInterface.OnClickListener() {
					// @Override
					// public void onClick(
					// DialogInterface argDialog,
					// int argWhich) {
					//
					// }
					// }).create();
					// m_AlertDialog.setCanceledOnTouchOutside(false);
					// m_AlertDialog.show();
					// m_progress_Login.setVisibility(View.GONE);
					// }

					// Intent myIntent1 = new Intent(getBaseContext(),
					// MainActivity.class);
					// startActivity(myIntent1);
					// finish();
				}
			}
		});

	}

}
