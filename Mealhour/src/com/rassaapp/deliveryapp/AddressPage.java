package com.rassaapp.deliveryapp;

import java.util.ArrayList;

import com.get2gether.deliveryapp.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

public class AddressPage extends Activity {
	Context context = this;

	Spinner town_input;
	EditText name_input;
	EditText phone_input;
	EditText housestreet_input;
	LinearLayout order;
	TextView texthint;

	String[] item_image;
	ArrayList<Integer> items_in_order;
	String[] accepted_towns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_page);

		//get data from strings.xml
		accepted_towns = getResources().getStringArray(R.array.AcceptedTowns);
		item_image = getResources().getStringArray(R.array.ItemImage);

		//get resources from xml
		order = (LinearLayout) findViewById(R.id.orderscroll);
		texthint = (TextView) findViewById(R.id.hinttext);
		TextView menu_title = (TextView) findViewById(R.id.menu_title);
		TextView yourorder = (TextView) findViewById(R.id.yourorder);
		ImageButton confirm = (ImageButton) findViewById(R.id.confirm);
		TextView name = (TextView) findViewById(R.id.name);
		TextView phone = (TextView) findViewById(R.id.phone);
		TextView housestreet = (TextView) findViewById(R.id.housestreet);
		TextView town = (TextView) findViewById(R.id.town);
		town_input = (Spinner) findViewById(R.id.town_input);
		name_input = (EditText) findViewById(R.id.name_input);
		phone_input = (EditText) findViewById(R.id.phone_input);
		housestreet_input = (EditText) findViewById(R.id.housestreet_input);

		//force portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//set textsizes according to resolution
		menu_title.setTextSize(fontpercent_screenheight(5));
		yourorder.setTextSize(fontpercent_screenheight(2.5));
		name.setTextSize(fontpercent_screenheight(2.5));
		phone.setTextSize(fontpercent_screenheight(2.5));
		housestreet.setTextSize(fontpercent_screenheight(2.5));
		town.setTextSize(fontpercent_screenheight(2.5));

		//set accepted towns
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accepted_towns);
		town_input.setAdapter(adapter);

		//get items in order from extras
		items_in_order = (ArrayList<Integer>) getIntent().getSerializableExtra("items_in_order");

		//add items back to order
		for (int i = 0; i < items_in_order.size(); i++)
			addItemToOrder(items_in_order.get(i));

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//check that everything is filled
				boolean notready = false;
				if (name_input.getText().toString().length() < 2) {
					Toast.makeText(context, R.string.IncorrectName, Toast.LENGTH_LONG).show();
					notready = true;
				}
				if (((String) (town_input.getSelectedItem())) == accepted_towns[0]) {
					Toast.makeText(context, R.string.IncorrectTown, Toast.LENGTH_LONG).show();
					notready = true;
				}
				if (phone_input.getText().toString().length() < 4) {
					Toast.makeText(context, R.string.IncorrectPhone, Toast.LENGTH_LONG).show();
					notready = true;
				}
				if (housestreet_input.getText().toString().length() < 3) {
					Toast.makeText(context, R.string.IncorrectHomeStreet, Toast.LENGTH_LONG).show();
					notready = true;
				}

				if (!notready) {
					Intent intent = new Intent(context, ConfirmPage.class).putExtra("items_in_order", items_in_order).putExtra("name", name_input.getText().toString()).putExtra("town", ((String) (town_input.getSelectedItem()))).putExtra("phone_input", phone_input.getText().toString()).putExtra("housestreet_input", housestreet_input.getText().toString());
					startActivity(intent);
				}
			}
		});

	}

	//Adds an item to the order in the bottom
	public void addItemToOrder(final int item) {

		LinearLayout.LayoutParams order_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		order_lp.setMargins(dp(10), 0, dp(10), 0);

		FrameLayout.LayoutParams newitem_fp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		ImageView newitem = new ImageView(this);
		newitem.setImageResource(getResources().getIdentifier(item_image[item], "drawable", getPackageName()));
		newitem.setScaleType(ScaleType.FIT_CENTER);
		newitem.setAdjustViewBounds(true);
		newitem.setLayoutParams(newitem_fp);

		FrameLayout layout = new FrameLayout(this);
		layout.setLayoutParams(order_lp);
		layout.addView(newitem);

		order.removeView(texthint);
		order.addView(layout);

	}

	//the functions below ensure that fonts and elements are displayed good on screen
	public int dp(int size) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
	}

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
