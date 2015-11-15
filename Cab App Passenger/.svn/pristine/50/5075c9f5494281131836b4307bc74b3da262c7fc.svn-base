package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.cabapppassenger.R;

public class TermsActivity extends Activity {

	Context mContext;

	ImageView ivBack;
	WebView wvTandC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_terms_and_conditions);
		mContext = this;
		ivBack = (ImageView) findViewById(R.id.ivBack);
		wvTandC = (WebView) findViewById(R.id.wv_tandc);
		wvTandC.loadUrl("file:///android_asset/html/tAndC.html");
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	}
}
