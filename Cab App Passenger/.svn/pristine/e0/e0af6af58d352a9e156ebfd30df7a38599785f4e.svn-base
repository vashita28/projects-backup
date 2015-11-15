package com.example.cabapppassenger.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.util.Constant;

public class AddNewCardFragment extends RootFragment implements
		OnCustomBackPressedListener {

	WebView wv_newcard;
	Context mContext;
	ProgressBar progressPayment;
	ImageView iv_back;
	Activity mActivity;
	ProgressDialog mDialog;
	String url;
	PreBookParcelable pbParcelable;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		View rootView = inflater.inflate(R.layout.fragment_newcard, container,
				false);
		mContext = getActivity();
		// mActivity = getActivity();
		wv_newcard = (WebView) rootView.findViewById(R.id.wv_addnewcard);
		iv_back = (ImageView) rootView.findViewById(R.id.ivBack);
		progressPayment = (ProgressBar) rootView
				.findViewById(R.id.progressPayment);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getParentFragment().getChildFragmentManager().popBackStack();
				hideKeybord(v);
			}
		});

		Bundle bundle = getArguments();
		if (bundle != null && bundle.containsKey("URL")) {
			url = bundle.getString("URL");
			pbParcelable = bundle.getParcelable("pbParcelable");
			wv_newcard.getSettings().setJavaScriptEnabled(true);
			wv_newcard.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			wv_newcard.getSettings().setLoadWithOverviewMode(true);
			wv_newcard.getSettings().setUseWideViewPort(true);
			wv_newcard.setScrollbarFadingEnabled(false);
			Log.e("URL", url);
		}
		wv_newcard.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				progressPayment.setVisibility(View.VISIBLE);
			}

			public void onPageFinished(WebView view, String url) {

				Log.e("Page Finished", "Inside Page Finished");
				Log.e("URL", url);

				if (progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}
				if (url != null) {
					if (url.contains("success")) {
						Toast.makeText(mContext, "Card added successfully.",
								Toast.LENGTH_SHORT).show();
						if (getActivity() != null)
							getParentFragment().getChildFragmentManager()
									.popBackStack();
						Constant.flag_added_newcard = true;
					} else if (url.contains("error")) {
						Toast.makeText(
								mContext,
								"Error processing payment request. Please try again.",
								Toast.LENGTH_SHORT).show();
						if (getActivity() != null)
							getParentFragment().getChildFragmentManager()
									.popBackStack();
					} else if (url.contains("timeout")) {
						Toast.makeText(
								mContext,
								"Payment request has timed out. Please try again.",
								Toast.LENGTH_SHORT).show();
						if (getActivity() != null)
							getParentFragment().getChildFragmentManager()
									.popBackStack();
					} else if (url.contains("hold")) {
						Toast.makeText(mContext,
								"Payment is on hold. Please try again.",
								Toast.LENGTH_SHORT).show();
						if (getActivity() != null)
							getParentFragment().getChildFragmentManager()
									.popBackStack();
					} else if (url.contains("decline")) {
						Toast.makeText(mContext,
								"Payment declined. Please try again.",
								Toast.LENGTH_SHORT).show();
						if (getActivity() != null)
							getParentFragment().getChildFragmentManager()
									.popBackStack();
					}
				}
			}

		});
		wv_newcard.loadUrl(url);
		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onDetach() {

		/*
		 * Fragment fPreBook = getParentFragment().getChildFragmentManager()
		 * .findFragmentByTag("bookingDetailsFragment"); try {
		 * ((BookingDetailsFragment) fPreBook).updateValues(pbParcelable); }
		 * catch (Exception e) {
		 * 
		 * }
		 */

		super.onDetach();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
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

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.setMessage(message + "...");
		return dialog;
	}

}
