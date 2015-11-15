package com.hoteltrip.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.hoteltrip.android.util.AppValues;

public class SortingActivity extends Activity implements OnClickListener {

	CheckBox cbPriceAscending, cbPriceDescending, cbRatingAscending,
			cbRatingDescending;

	int currentSelectedValue = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sort);

		cbPriceAscending = (CheckBox) findViewById(R.id.cb_priceasc);
		cbPriceDescending = (CheckBox) findViewById(R.id.cb_pricedesc);
		cbRatingAscending = (CheckBox) findViewById(R.id.cb_ratingascen);
		cbRatingDescending = (CheckBox) findViewById(R.id.cb_ratingdesc);

		currentSelectedValue = AppValues.nSortType;

		switch (AppValues.nSortType) {
		case R.id.cb_priceasc:
			cbPriceAscending.setChecked(true);
			break;
		case R.id.cb_pricedesc:
			cbPriceDescending.setChecked(true);
			break;
		case R.id.cb_ratingascen:
			cbRatingAscending.setChecked(true);
			break;
		case R.id.cb_ratingdesc:
			cbRatingDescending.setChecked(true);
			break;
		default:
			break;
		}

		cbPriceAscending.setOnClickListener(this);
		cbPriceDescending.setOnClickListener(this);
		cbRatingAscending.setOnClickListener(this);
		cbRatingDescending.setOnClickListener(this);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", currentSelectedValue);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		currentSelectedValue = v.getId();
		AppValues.nSortType = currentSelectedValue;
		cbPriceAscending.setChecked(false);
		cbPriceDescending.setChecked(false);
		cbRatingAscending.setChecked(false);
		cbRatingDescending.setChecked(false);
		switch (v.getId()) {
		case R.id.cb_priceasc:
			cbPriceAscending.setChecked(true);
			break;
		case R.id.cb_pricedesc:
			cbPriceDescending.setChecked(true);
			break;
		case R.id.cb_ratingascen:
			cbRatingAscending.setChecked(true);
			break;
		case R.id.cb_ratingdesc:
			cbRatingDescending.setChecked(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", currentSelectedValue);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

}
