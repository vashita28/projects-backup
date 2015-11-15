package com.example.cabapppassenger.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.LoginActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.activity.ViewTutorialActivity;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanairship.push.PushManager;

public class MoreFragment extends RootFragment implements
		OnCustomBackPressedListener {

	ImageView iv_airport;
	View view_logout;
	String PREF_NAME = "CabApp_Passenger";
	SharedPreferences shared_pref;
	RelativeLayout rlAboutUs, rlFeedBack, rlTermsAndConditions,
			rel_viewtutorial;
	TextView tv_cabapp_location;
	Editor editor;
	Context mContext;
	AlertDialog quitAlertDialog;

	public MoreFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_more, container,
				false);

		mContext = getActivity();
		tv_cabapp_location = (TextView) rootView
				.findViewById(R.id.tv_cabapp_location);
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		iv_airport = (ImageView) rootView.findViewById(R.id.iv_airport);
		iv_airport.setVisibility(View.GONE);
		view_logout = (View) rootView.findViewById(R.id.view_logout);
		rel_viewtutorial = (RelativeLayout) rootView
				.findViewById(R.id.rel_viewtutorial);
		rlAboutUs = (RelativeLayout) rootView.findViewById(R.id.rel_aboutus);
		rlFeedBack = (RelativeLayout) rootView.findViewById(R.id.rel_feedback);
		rlTermsAndConditions = (RelativeLayout) rootView
				.findViewById(R.id.rel_tandc);

		view_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLogoutDialog();
			}
		});
		View.OnClickListener fragmentTransactionListener = new OnClickListener() {
			FragmentManager fm;
			FragmentTransaction ft;

			@Override
			public void onClick(View v) {

				fm = getParentFragment().getChildFragmentManager();
				ft = fm.beginTransaction();
				switch (v.getId()) {
				case R.id.rel_aboutus:
					AboutUsFragment aboutUsFragment = new AboutUsFragment();
					ft.replace(R.id.frame_container, aboutUsFragment);
					break;

				case R.id.rel_feedback:
					FeedbackFragment feedbackFragment = new FeedbackFragment();
					ft.replace(R.id.frame_container, feedbackFragment);
					break;

				case R.id.rel_tandc:
					TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment();
					ft.replace(R.id.frame_container, termsAndConditionsFragment);
					break;

				case R.id.rel_viewtutorial:
					Intent tutorial = new Intent(getActivity(),
							ViewTutorialActivity.class);
					startActivity(tutorial);
					break;

				// case R.id.tv_cabapp_location:
				// CabappLocation_fragment citylistFragment = new
				// CabappLocation_fragment();
				// ft.replace(R.id.frame_container, citylistFragment);
				// break;

				}
				ft.addToBackStack("moreFragment");
				ft.commit();

			}
		};
		rlAboutUs.setOnClickListener(fragmentTransactionListener);
		tv_cabapp_location.setOnClickListener(fragmentTransactionListener);
		rlFeedBack.setOnClickListener(fragmentTransactionListener);
		rlTermsAndConditions.setOnClickListener(fragmentTransactionListener);
		rel_viewtutorial.setOnClickListener(fragmentTransactionListener);

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	@Override
	public void onDetach() {
		if (quitAlertDialog != null)
			quitAlertDialog.dismiss();
		super.onDetach();
	}

	public void showLogoutDialog() {
		quitAlertDialog = new AlertDialog.Builder(mContext)
				.setTitle("Logout")
				.setMessage("Are you sure you want to logout?")
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						// editor.clear();
						// editor.commit();
						// MainFragmentActivity.finishMe();
						// Intent landing = new Intent(mContext,
						// LoginActivity.class);
						// startActivity(landing);
						// argDialog.dismiss();
						// //PushManager.disablePush();
						// PushManager.shared().setNotificationBuilder(
						// null);
						// Constant.hashmap_passengercard.clear();
						// Constant.hash_favid_favourites.clear();
						// Constant.hash_latng_favourites.clear();
						// Constant.hash_latng_recent.clear();
						argDialog.dismiss();
					}
				})
				.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {

								argDialog.dismiss();
								editor.clear();
								editor.commit();
								MainFragmentActivity.finishMe();
								Intent landing = new Intent(mContext,
										LoginActivity.class);
								startActivity(landing);
								argDialog.dismiss();
								// PushManager.disablePush();
								PushManager.shared().setNotificationBuilder(
										null);
								Constant.hashmap_passengercard.clear();
								Constant.hash_favid_favourites.clear();
								Constant.hash_latng_favourites.clear();
								Constant.hash_latng_recent.clear();
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		if (MainFragmentActivity.slidingMenu.isMenuShowing())
			MainFragmentActivity.slidingMenu.toggle();
		else
			((MainFragmentActivity) getActivity()).showQuitDialog();

	}

	@Override
	public void onPause() {
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

}