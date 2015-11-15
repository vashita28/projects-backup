package com.hoteltrip.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class FilterPriceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pricing);
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	/*
	 * addPreferencesFromResource(R.xml.search_filter_price);
	 * 
	 * }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getSupportMenuInflater().inflate(R.menu.filter_done, menu); return true;
	 * }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * 
	 * switch (item.getItemId()) { case R.id.menu_filter_done: this.finish();
	 * return true; } return super.onOptionsItemSelected(item); }
	 */
}
