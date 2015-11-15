package co.uk.pocketapp.gmd.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.tasks.ForgotPasswordTask;

public class ForgotPassword extends Activity {
	// To apply font:
	TextView gmd_Heading;
	TextView Forgot_password_Text;
	TextView password_text_center;
	TextView number_text;
	Button btn_cancel;
	static Handler mHandler;
	TextView date_N_Time;
	String reportDate;

	public static String ms_szPhoneNumber = "";
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);

		gmd_Heading = (TextView) findViewById(R.id.main_Heading);
		gmd_Heading.setTypeface(GMDApplication.fontHeading);
		Forgot_password_Text = (TextView) findViewById(R.id.pageTitle_Forgot_password);
		Forgot_password_Text.setTypeface(GMDApplication.fontText);
		password_text_center = (TextView) findViewById(R.id.password_text);
		password_text_center.setTypeface(GMDApplication.fontText);
		number_text = (TextView) findViewById(R.id.number_text);
		number_text.setTypeface(GMDApplication.fontHeading);

		date_N_Time = (TextView) findViewById(R.id.DateNtime);
		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			reportDate = dateFormat.format(today);
			date_N_Time.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {

				number_text.setText(ms_szPhoneNumber);

				super.handleMessage(message);
			}
		};

		ForgotPasswordTask task = new ForgotPasswordTask();
		task.handler = mHandler;
		task.execute();

		btn_cancel = (Button) findViewById(R.id.backToLogInPage);
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
