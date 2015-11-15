package com.hoteltrip.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewOptionsActivity extends Activity {
	private Context context = this;
	ImageView currency_iv, language_iv, conversion_iv;
	TextView tv_currval, tv_langval, tv_conversion;
	String[] languages, currencies, unitConversion;
	Button done;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_options);
		done = (Button) findViewById(R.id.btn_done);
		tv_currval = (TextView) findViewById(R.id.tv_currval);
		tv_langval = (TextView) findViewById(R.id.tv_langval);
		tv_conversion = (TextView) findViewById(R.id.tv_unitval);
		languages = getResources().getStringArray(R.array.array_languages);
		currencies = getResources().getStringArray(R.array.array_currencies);
		unitConversion = getResources().getStringArray(
				R.array.array_distance_unit);
		currency_iv = (ImageView) findViewById(R.id.currency_iv);
		conversion_iv = (ImageView) findViewById(R.id.conversion_iv);
		language_iv = (ImageView) findViewById(R.id.language_iv);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		currency_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				currency();
			}
		});

		language_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				languages();
			}
		});

		conversion_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				conversion();
			}
		});

	}

	public void currency() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please Select a Option");
		builder.setItems(currencies, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				tv_currval.setText(currencies[item]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void languages() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please Select a Option");
		builder.setItems(languages, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				tv_langval.setText(languages[item]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void conversion() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please Select a Option");
		builder.setItems(unitConversion, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				tv_conversion.setText(unitConversion[item]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
