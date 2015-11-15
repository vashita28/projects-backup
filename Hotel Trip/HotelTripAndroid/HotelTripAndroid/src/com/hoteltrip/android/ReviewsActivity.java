package com.hoteltrip.android;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ReviewsActivity extends BaseActivity {

	WebView webview_reviews;
	String szHotelID;
	// http://www.tripadvisor.com/WidgetEmbed-cdspropertydetail?locationId=WSMA0812120783&partnerId=09B5D52857F84EAFB4288A87F36E1943&display=true&lang=en_US
	String szURL = "http://www.tripadvisor.com/WidgetEmbed-cdspropertydetail?locationId=%s&partnerId=09B5D52857F84EAFB4288A87F36E1943&display=true&lang=en_US";

	ProgressBar progressLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		super.init("Reviews");
		btnMap.setVisibility(View.GONE);
		progressLoading = (ProgressBar) findViewById(R.id.progressLoading);
		progressLoading.setVisibility(View.VISIBLE);
		if (getIntent().getExtras() != null) {
			szHotelID = getIntent().getExtras().getString("hotelid");
			szURL = String.format(szURL, szHotelID);
		}

		WebViewClient yourWebClient = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progressLoading.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null)
					// Load new URL Don't override URL Link
					return false;

				// Return true to override url loading (In this case do
				// nothing).
				return true;
			}
		};

		webview_reviews = (WebView) findViewById(R.id.webview_reviews);
		webview_reviews.getSettings().setJavaScriptEnabled(true);
		// Zoom Control on web (You don't need this)
		webview_reviews.getSettings().setSupportZoom(true);
		// if ROM supports Multi-Touch
		webview_reviews.getSettings().setBuiltInZoomControls(true);
		webview_reviews.setWebViewClient(yourWebClient);
		// Load URL
		webview_reviews.loadUrl(szURL);

	}
}
