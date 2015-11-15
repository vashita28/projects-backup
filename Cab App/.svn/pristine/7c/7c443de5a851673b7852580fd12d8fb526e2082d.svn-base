package com.android.cabapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.cabapp.R;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.util.Constants;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class PromotionsFragment extends RootFragment {

	WebView wvPromotion;
	ProgressBar progressPromotion;

	String szPromotionURL = Constants.promotionURL;

	public PromotionsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promotions,
				container, false);

		wvPromotion = (WebView) rootView.findViewById(R.id.wvPromotion);
		progressPromotion = (ProgressBar) rootView
				.findViewById(R.id.progressPromotion);

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		WebSettings settings = wvPromotion.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		wvPromotion.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		if (MainActivity.slidingMenu != null)
			MainActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		wvPromotion.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

			}

			public void onPageFinished(WebView view, String url) {
				if (progressPromotion != null && progressPromotion.isShown()) {
					progressPromotion.setVisibility(View.GONE);
				}
				// https://pay.test.netbanx.com/cabapp-rest/tx_payment/en
			}

			private FragmentManager getSupportFragmentManager() {
				// TODO Auto-generated method stub
				return null;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (progressPromotion.isShown()) {
					progressPromotion.setVisibility(View.GONE);
				}
			}
		});

		wvPromotion.loadUrl(szPromotionURL);

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (MainActivity.slidingMenu != null)
			MainActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}
}
