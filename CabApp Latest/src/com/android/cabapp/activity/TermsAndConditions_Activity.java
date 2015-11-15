package com.android.cabapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.cabapp.R;

public class TermsAndConditions_Activity extends RootActivity {

	TextView txtEdit;
	WebView wvTandC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_and_conditions);

		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		wvTandC = (WebView) findViewById(R.id.webviewTandC);
		wvTandC.loadUrl("file:///android_asset/tAndC.html");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();
	}

}
