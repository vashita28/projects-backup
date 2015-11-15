package com.example.cabapppassenger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TermsAndConditionsFragment extends RootFragment implements
		OnCustomBackPressedListener {

	Context mContext;

	ImageView ivBack;
	WebView wvTandC;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.fragment_terms_and_conditions, container, false);
		mContext = getActivity();
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		wvTandC = (WebView) rootView.findViewById(R.id.wv_tandc);
		wvTandC.loadUrl("file:///android_asset/html/tAndC.html");
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getParentFragment().getChildFragmentManager().popBackStack();

			}
		});

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		super.onResume();

	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		getParentFragment().getChildFragmentManager().popBackStack();

	}

	@Override
	public void onPause() {
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

}