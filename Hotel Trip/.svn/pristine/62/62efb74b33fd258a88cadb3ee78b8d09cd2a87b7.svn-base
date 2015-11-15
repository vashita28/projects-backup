package com.hoteltrip.android;

import com.hoteltrip.android.util.Utils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SpecialRequestActivity extends BaseSherlockActivity {

	EditText flightnumberandarrivaltime_et, addtionalnotes_tv;
	Button btn_back;
	ImageButton done_imgbtn, imgbtn_about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_specialrequest);
		done_imgbtn = (ImageButton) findViewById(R.id.done_imgbtn);
		imgbtn_about = (ImageButton) findViewById(R.id.imgbtn_about);
		imgbtn_about.setVisibility(View.GONE);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		done_imgbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		flightnumberandarrivaltime_et = (EditText) findViewById(R.id.flightnumberandarrivaltime_et);
		addtionalnotes_tv = (EditText) findViewById(R.id.addtionalnotes_tv);
		flightnumberandarrivaltime_et.setTypeface(Utils
				.getHelveticaNeue(SpecialRequestActivity.this));
		addtionalnotes_tv.setTypeface(Utils
				.getHelveticaNeue(SpecialRequestActivity.this));
	}
}