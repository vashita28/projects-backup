package com.rassaapp.deliveryapp;

import java.util.ArrayList;

import com.get2gether.deliveryapp.R;

import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class ConfirmPage extends Activity {
	Context context = this;

	LinearLayout order;
	TextView texthint;
	String[] item_title;
	String[] item_price;
	String sign = "Rs.";
	String name, phone, houseandstreet, town;
	String OrderTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_page);

		//get data from strings.xml
		item_price = getResources().getStringArray(R.array.ItemPrice);
		item_title = getResources().getStringArray(R.array.ItemTitle);

		//get resourses from xml
		order = (LinearLayout) findViewById(R.id.orderscroll);
		LinearLayout buttonholder = (LinearLayout) findViewById(R.id.buttonholder);
		TextView menu_title = (TextView) findViewById(R.id.menu_title);
		TextView itemconfirm = (TextView) findViewById(R.id.itemconfirm_text);
		TextView priceconfirm = (TextView) findViewById(R.id.priceconfirm_text);
		TextView help1 = (TextView) findViewById(R.id.help);
		TextView help2 = (TextView) findViewById(R.id.help2);
		TextView addressconfirm = (TextView) findViewById(R.id.addressconfirm_text);
		Button ordernowsms = (Button) findViewById(R.id.ordernowsms);
		Button ordernowemail = (Button) findViewById(R.id.ordernowemail);

		//get items in order from extras
		ArrayList<Integer> items_in_order = (ArrayList<Integer>) getIntent().getSerializableExtra("items_in_order");
		//get address from extras
		name = getIntent().getStringExtra("name");
		phone = getIntent().getStringExtra("phone_input");
		houseandstreet = getIntent().getStringExtra("housestreet_input");
		town = getIntent().getStringExtra("town");

		//set textsizes according to resolution
		menu_title.setTextSize(fontpercent_screenheight(5));
		priceconfirm.setTextSize(fontpercent_screenheight(2.5));
		itemconfirm.setTextSize(fontpercent_screenheight(2.5));
		help1.setTextSize(fontpercent_screenheight(2.5));
		help2.setTextSize(fontpercent_screenheight(2.5));
		ordernowsms.setTextSize(fontpercent_screenheight(2.5));
		ordernowemail.setTextSize(fontpercent_screenheight(2.5));
		addressconfirm.setTextSize(fontpercent_screenheight(2.5));

		//force portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//delete email/sms button if not needed
		if (getString(R.string.OrderBySMSEnabled).equals("false")) {
			buttonholder.removeView(ordernowsms);
		}
		if (getString(R.string.OrderByEmailEnabled).equals("false")) {
			buttonholder.removeView(ordernowemail);
		}

		//fill confirm text
		String text = new String();
		String price = new String();
		OrderTitles = "";
		double total = 0;
		for (int i = 0; i < items_in_order.size(); i++) {
			text += item_title[items_in_order.get(i)] + "\n";
			OrderTitles += item_title[items_in_order.get(i)] + ", ";
			price += String.format(sign + "%.2f", Double.parseDouble(item_price[items_in_order.get(i)])) + "\n";
			total = total + Double.parseDouble(item_price[items_in_order.get(i)]);
		}
		text += "\n" + "TOTAL\n";
		price += "..................\n" + String.format(sign + "%.2f", total) + "\n";
		//display order
		itemconfirm.setText(text);
		priceconfirm.setText(price);

		//display order address
		addressconfirm.setText(getString(R.string.Confirm3) + "\n" + name + "\n" + houseandstreet + "\n" + town + "\nTel:" + phone);

		//order now buttons
		ordernowsms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//send sms
				send_sms(getString(R.string.MobileNumberForSMSOrders), "Order:" + OrderTitles + "\nAddress:" + name + ".\n" + houseandstreet + ".\n" + town + ".\n" + phone);
			}
		});
		ordernowemail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//send email
				send_email(getString(R.string.EmailAdressForEmailOrders), "Take Away Order", "Order:" + OrderTitles + "\nAddress:" + name + ".\n" + houseandstreet + ".\n" + town + ".\n" + phone);
			}
		});
	}

	//this function sends an email to a particular email address.
	public void send_email(String to, String subject, String message) {

		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(Intent.EXTRA_TEXT, message);

		//need this to prompts email client only
		email.setType("message/rfc822");

		try {
			startActivity(Intent.createChooser(email, "Choose an Email client to send Order:"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}

	}

	//this function sends an sms to a particular mobile number
	public void send_sms(String address, String message) {

		if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
			// THIS PHONE HAS SMS FUNCTIONALITY
			SmsManager smsMgr = SmsManager.getDefault();
			ArrayList<String> messages = smsMgr.divideMessage(message);
			final String SMS_ADDRESS_PARAM = "SMS_ADDRESS_PARAM";
			final String SMS_DELIVERY_MSG_PARAM = "SMS_DELIVERY_MSG_PARAM";
			final String SMS_SENT_ACTION = "com.tilab.msn.SMS_SENT";

			ArrayList<PendingIntent> listOfIntents = new ArrayList<PendingIntent>();
			for (int i = 0; i < messages.size(); i++) {
				Intent sentIntent = new Intent(SMS_SENT_ACTION);
				sentIntent.putExtra(SMS_ADDRESS_PARAM, address);
				sentIntent.putExtra(SMS_DELIVERY_MSG_PARAM, (messages.size() > 1) ? "Part " + i + " of SMS " : "SMS ");
				PendingIntent pi = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
				listOfIntents.add(pi);
			}

			smsMgr.sendMultipartTextMessage(address, null, messages, listOfIntents, null);

			ContentValues values = new ContentValues();
			values.put("address", address);
			values.put("body", message);
			getContentResolver().insert(Uri.parse("content://sms/sent"), values);
			
			Toast.makeText(this, "SMS with your order was sent", Toast.LENGTH_LONG).show();
		} else {
			// NO SMS FUNCTION HERE
			Toast.makeText(this, "You can't send an SMS through this device", Toast.LENGTH_SHORT).show();
		}
	}

	//the functions below are used to make sure fonts and dimensions look good on all screens
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
