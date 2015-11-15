package com.hoteltrip.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.hoteltrip.android.fragment.MyDialogFragment;
import com.hoteltrip.android.util.DateFetchedListener;
import com.hoteltrip.android.util.Utils;

public class BaseActivity extends SherlockFragmentActivity implements
		OnClickListener {
	Button btnBack;
	protected Button btnMap;
	TextView activityTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void init(String szTitle) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		// TODO Auto-generated method stub
		activityTitleText = (TextView) findViewById(R.id.tv_title);
		activityTitleText.setText(szTitle);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setTypeface(Utils.getHelveticaNeueBold(BaseActivity.this));
		btnBack.setVisibility(View.VISIBLE);
		btnMap = (Button) findViewById(R.id.btn_map);
		btnMap.setVisibility(View.VISIBLE);
		btnMap.setTypeface(Utils.getHelveticaNeueBold(BaseActivity.this));
		btnBack.setOnClickListener(this);
		btnMap.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_map:
			startActivity(new Intent(this, SearchResultsMapActivity.class));
			break;
		default:
			break;
		}

	}

	public void showMyDialog(int id, DateFetchedListener dateFetchedListener) {
		MyDialogFragment dialogFragment = MyDialogFragment.newInstance(id, 0);
		dialogFragment.show(getSupportFragmentManager(), "dialog");

		if (dateFetchedListener != null) {
			dialogFragment.setDateFetcher(dateFetchedListener);
		}

	}
}
