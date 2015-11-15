package com.example.cabapppassenger.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.CountryCodeAdapter;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.EditUserDetailsTask;
import com.example.cabapppassenger.task.GetCountryCodeTask;
import com.example.cabapppassenger.task.ResendCodeTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class EditProfileFragment extends RootFragment implements
		OnCustomBackPressedListener {

	EditText et_firstname, et_lastname, et_mobileno;
	Context mContext;
	Handler mhandler;
	TextView tv_done;
	ImageView ivBack;
	Editor editor;
	SharedPreferences shared_pref;
	Spinner sp_countrycode;
	String PREF_NAME = "CabApp_Passenger";

	public EditProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		View rootView = inflater.inflate(R.layout.fragment_editprofile,
				container, false);

		mContext = getActivity();
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		et_firstname = (EditText) rootView.findViewById(R.id.et_firstname);
		et_firstname.setText(shared_pref.getString("FirstName", ""));
		sp_countrycode = (Spinner) rootView
				.findViewById(R.id.spinner_countrycode);
		et_lastname = (EditText) rootView.findViewById(R.id.et_lastname);
		et_lastname.setText(shared_pref.getString("LastName", ""));
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		et_mobileno = (EditText) rootView.findViewById(R.id.et_mobileno);
		et_mobileno.setText(shared_pref.getString("MobileNo", ""));

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getParentFragment().getChildFragmentManager().popBackStack();
				hideKeybord(v);
			}
		});
		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					// only when firstname or last name changes
					getParentFragment().getChildFragmentManager()
							.popBackStack();
				}
				if (msg.what == 1) {
					// when either of them changes
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

						ResendCodeTask task = new ResendCodeTask(mContext);
						task.clientkey = shared_pref.getString("ClientKey", "");
						task.from_myaccount = true;
						task.international_code = sp_countrycode
								.getSelectedItem().toString().substring(1);
						task.mobile_no = et_mobileno.getText().toString();
						task.execute(Constant.passengerURL
								+ "ws/v2/passenger/resendVerificationPin/");
						getParentFragment().getChildFragmentManager()
								.popBackStack();
					} else {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (msg.what == 2 && Constant.arr_dialingcode.size() > 0) {
					sp_countrycode.setAdapter(new CountryCodeAdapter(
							getActivity(), R.layout.custom_spinner,
							Constant.arr_dialingcode));
					int position = Constant.arr_dialingcode.indexOf("+"
							+ shared_pref.getString("InternationalCode", ""));
					Log.e("Position of international code",
							"" + shared_pref.getString("InternationalCode", ""));

					sp_countrycode.setSelection(position);
				}

			}

		};
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			if (Constant.arr_dialingcode != null
					& Constant.arr_dialingcode.size() > 0)
				Constant.arr_dialingcode.clear();

			GetCountryCodeTask task = new GetCountryCodeTask(mContext);
			task.fromlanding_flag = false;
			task.mhandler = mhandler;
			task.execute();

		} else {
			Toast.makeText(mContext, "No network connection.",
					Toast.LENGTH_SHORT).show();
		}
		tv_done = (TextView) rootView.findViewById(R.id.tv_done);

		tv_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeybord(v);
				if (et_firstname.length() > 0 && et_lastname.length() > 0
						&& et_mobileno.length() > 0
						&& sp_countrycode.getSelectedItem() != null) {

					if (et_mobileno.getText().toString() != null
							&& et_mobileno.length() >= 10
							&& et_mobileno.length() <= 11) {
						// if nothing changes
						if (et_firstname.getText().toString().trim()
								.equals(shared_pref.getString("FirstName", ""))
								&& et_lastname
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"LastName", ""))
								&& sp_countrycode
										.getSelectedItem()
										.toString()
										.trim()
										.equals("+"
												+ shared_pref
														.getString(
																"InternationalCode",
																""))
								&& et_mobileno
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"MobileNo", ""))) {

							getParentFragment().getChildFragmentManager()
									.popBackStack();

						}
						// when firstname or lastname changes but not mobile
						// number
						// and country
						else if (!et_firstname.getText().toString().trim()
								.equals(shared_pref.getString("FirstName", ""))
								|| !et_lastname
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"LastName", ""))
								&& sp_countrycode
										.getSelectedItem()
										.toString()
										.trim()
										.equals("+"
												+ shared_pref
														.getString(
																"InternationalCode",
																""))
								&& et_mobileno
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"MobileNo", ""))) {

							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
								EditUserDetailsTask task = new EditUserDetailsTask(
										mContext);
								task.firstname = et_firstname.getText()
										.toString();
								task.lastname = et_lastname.getText()
										.toString();
								task.wheelchair = shared_pref.getString(
										"WheelChair", "");
								task.hearingimpaired = shared_pref.getString(
										"HearingImpaired", "");
								task.payingmethod = shared_pref.getString(
										"PayingMethod", "");
								task.handler = mhandler;
								task.execute(Constant.passengerURL
										+ "ws/v2/passenger/account/?accessToken="
										+ shared_pref.getString("AccessToken",
												""));
								hideKeybord(v);
							} else {
								Toast.makeText(mContext,
										"No network connection",
										Toast.LENGTH_SHORT).show();
							}

						}
						// when firstname and lastname doesnt change but
						// mobilenumber or country code changes
						else if (et_firstname.getText().toString().trim()
								.equals(shared_pref.getString("FirstName", ""))
								&& et_lastname
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"LastName", ""))
								&& !sp_countrycode
										.getSelectedItem()
										.toString()
										.trim()
										.equals("+"
												+ shared_pref
														.getString(
																"InternationalCode",
																""))
								|| !et_mobileno
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"MobileNo", ""))) {
							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

								ResendCodeTask task = new ResendCodeTask(
										mContext);
								task.clientkey = shared_pref.getString(
										"ClientKey", "");
								task.from_myaccount = true;
								task.international_code = sp_countrycode
										.getSelectedItem().toString()
										.substring(1);
								task.mobile_no = et_mobileno.getText()
										.toString();
								task.execute(Constant.passengerURL
										+ "ws/v2/passenger/resendVerificationPin/");
								getParentFragment().getChildFragmentManager()
										.popBackStack();
							} else {
								Toast.makeText(mContext,
										"No network connection",
										Toast.LENGTH_SHORT).show();
							}
						}
						// when either of them changes
						else if (!et_firstname.getText().toString().trim()
								.equals(shared_pref.getString("FirstName", ""))
								|| !et_lastname
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"LastName", ""))
								|| !sp_countrycode
										.getSelectedItem()
										.toString()
										.trim()
										.equals("+"
												+ shared_pref
														.getString(
																"InternationalCode",
																""))
								|| !et_mobileno
										.getText()
										.toString()
										.trim()
										.equals(shared_pref.getString(
												"MobileNo", ""))) {

							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
								EditUserDetailsTask task = new EditUserDetailsTask(
										mContext);
								task.firstname = et_firstname.getText()
										.toString();
								task.lastname = et_lastname.getText()
										.toString();
								task.wheelchair = shared_pref.getString(
										"WheelChair", "");
								task.hearingimpaired = shared_pref.getString(
										"HearingImpaired", "");
								task.payingmethod = shared_pref.getString(
										"PayingMethod", "");
								task.hit_resendtask = true;
								task.handler = mhandler;
								task.execute(Constant.passengerURL
										+ "ws/v2/passenger/account/?accessToken="
										+ shared_pref.getString("AccessToken",
												""));
								hideKeybord(v);
							} else {
								Toast.makeText(mContext,
										"No network connection",
										Toast.LENGTH_SHORT).show();
							}

						}

						// proceed
					} else {
						Constant.alertdialog("invalidmobile", mContext);
					}
				} else {
					Constant.alertdialog("emptyfields", mContext);
				}
			}
		});

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

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
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		if (Constant.arr_dialingcode != null)
			Constant.arr_dialingcode.clear();

	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ivBack.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
}
