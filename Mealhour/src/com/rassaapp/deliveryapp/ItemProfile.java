package com.rassaapp.deliveryapp;

import com.get2gether.deliveryapp.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

public class ItemProfile extends Activity {

	Context context = this;
	String sign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_profile);

		//force portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//get data from strings.xml
		sign = getResources().getString(R.string.CurrencySign);

		//get resources from xml
		ImageView item_image = (ImageView) findViewById(R.id.image);
		TextView item_title = (TextView) findViewById(R.id.title);
		TextView item_desc = (TextView) findViewById(R.id.description);
		TextView item_price = (TextView) findViewById(R.id.price);
		TextView backtomenu = (TextView) findViewById(R.id.backtomenu);

		//get item data from extras
		item_title.setText(getIntent().getStringExtra("item_title"));
		item_desc.setText(getIntent().getStringExtra("item_desc"));
		item_price.setText(String.format(sign + "%.2f", getIntent().getDoubleExtra("item_price", 0)));
		item_image.setImageResource(getIntent().getIntExtra("item_image", 0));

		//set fonts
		item_title.setTextSize(fontpercent_screenheight(5));
		item_desc.setTextSize(fontpercent_screenwidth(5));
		item_price.setTextSize(fontpercent_screenwidth(5));
		backtomenu.setTextSize(fontpercent_screenwidth(5));

		//back to menu button pressed
		backtomenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		backtomenu.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((TextView) v).setTextColor(0xff212121);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					((TextView) v).setTextColor(0xffff0000);
				}
				return false;
			}
		});

	}

	//used to make fonts look good on every screen
	public int fontpercent_screenheight(double d) {
		//get resolution
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int px = (int) ((float) dm.heightPixels * ((float) d / 100));
		float dp = px / dm.density;
		return (int) dp;
	}

	public int fontpercent_screenwidth(double d) {
		//get resolution
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int px = (int) ((float) dm.widthPixels * ((float) d / 100));
		float dp = px / dm.density;
		return (int) dp;
	}
}
