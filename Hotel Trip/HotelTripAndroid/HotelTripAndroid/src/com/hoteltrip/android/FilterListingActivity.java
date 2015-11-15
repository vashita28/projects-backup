package com.hoteltrip.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FilterListingActivity extends Activity {

	private ImageButton priceBtn;
	private ImageButton starRatingBtn;
	private ImageButton typeBtn;
	private ImageButton amenitiesBtn;
	private ImageButton themeBtn;
	private ImageButton chainBtn;
	private SeekBar seekBar;
	private TextView distanceText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_filter_listing_new);

		priceBtn = (ImageButton) findViewById(R.id.priceimgbtn);
		starRatingBtn = (ImageButton) findViewById(R.id.starrating_imgbtn);
		typeBtn = (ImageButton) findViewById(R.id.type_imgbtn);
		amenitiesBtn = (ImageButton) findViewById(R.id.amenities_imgbtn);
		themeBtn = (ImageButton) findViewById(R.id.theme_imgbtn);
		chainBtn = (ImageButton) findViewById(R.id.chain_imgbtn);

		priceBtn.setOnClickListener(onPriceClick);
		starRatingBtn.setOnClickListener(onStarRatingClick);

		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setMax(20);
		seekBar.setOnSeekBarChangeListener(seekBarChangedListener);

		distanceText = (TextView) findViewById(R.id.distance_tv);
		distanceText.setText("" + 3);
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	OnSeekBarChangeListener seekBarChangedListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			distanceText.setText("" + progress);
		}
	};

	OnClickListener onPriceClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent priceIntent = new Intent(getApplicationContext(),
					FilterPriceActivity.class);
			startActivity(priceIntent);
		}
	};

	OnClickListener onStarRatingClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent starIntent = new Intent(getApplicationContext(),
					FilterStarRatingActivity.class);
			startActivity(starIntent);
		}
	};

	OnClickListener onTypeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	OnClickListener onAmenitiesClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	OnClickListener onThemeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	OnClickListener onChainClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

}
