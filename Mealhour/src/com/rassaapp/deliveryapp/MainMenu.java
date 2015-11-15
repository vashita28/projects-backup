package com.rassaapp.deliveryapp;

import com.get2gether.deliveryapp.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;



import android.os.Bundle;
import android.widget.Toast;

public class MainMenu extends Activity {

	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		//get scrolling images from strings.xml
		String[] scrollingitems = getResources().getStringArray(R.array.DisplayImages);

		//load resources
		//TextView restaurant_title = (TextView) findViewById(R.id.restaurant_title);
		TextView restaurant_subtitle = (TextView) findViewById(R.id.restaurant_subtitle);
		TextView restaurant_motto = (TextView) findViewById(R.id.restaurant_motto);
		TextView restaurant_phonenumber = (TextView) findViewById(R.id.restaurant_phonenumber);
		TextView restaurant_address = (TextView) findViewById(R.id.restaurant_address);
		final Button startorder = (Button) findViewById(R.id.startorder);
		LinearLayout layout = (LinearLayout) findViewById(R.id.imagescroll);

		//set textsizes according to resolution
		//restaurant_title.setTextSize(fontpercent_screenwidth(10));
		restaurant_subtitle.setTextSize(fontpercent_screenwidth(3));
		restaurant_phonenumber.setTextSize(fontpercent_screenwidth(3));
		restaurant_address.setTextSize(fontpercent_screenwidth(3));
		restaurant_motto.setTextSize(fontpercent_screenwidth(4));
		startorder.setTextSize(fontpercent_screenwidth(8));

		//force portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//Add items to display image scroller
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(5, 0, 5, 0);

		for (int i = 0; i < scrollingitems.length; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(getResources().getIdentifier(scrollingitems[i], "drawable", getPackageName()));
			item.setScaleType(ScaleType.FIT_CENTER);
			item.setAdjustViewBounds(true);
			item.setLayoutParams(lp);
			layout.addView(item);
		}

		//when start order is clicked go to menu
		startorder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(context, Menu.class);
				startActivity(intent);
			}
		});

		//make touch animation on start order button
		startorder.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					startorder.setBackgroundResource(R.drawable.borderradius1_high);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					startorder.setBackgroundResource(R.drawable.borderradius1);
				}
				return false;
			}
		});

	}

	//used to make sure text is good on all devices
	public int fontpercent_screenwidth(double d) {
		//get resolution
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int px = (int) ((float) dm.widthPixels * ((float) d / 100));
		float dp = px / dm.density;
		return (int) dp;
	}

}
