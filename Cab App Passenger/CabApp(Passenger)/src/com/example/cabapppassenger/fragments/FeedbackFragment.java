package com.example.cabapppassenger.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.SendFeedbackTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class FeedbackFragment extends RootFragment implements
		OnCustomBackPressedListener {

	Context mContext;

	RelativeLayout rlDone;
	ImageView ivBack;
	EditText etFeedbackText;
	String PREF_NAME = "CabApp_Passenger", accessToken;
	SharedPreferences sharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		View rootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);
		mContext = getActivity();
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		rlDone = (RelativeLayout) rootView.findViewById(R.id.rl_done);
		etFeedbackText = (EditText) rootView.findViewById(R.id.et_feedback);
		sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		accessToken = sharedPreferences.getString("AccessToken", "");
		rlDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (etFeedbackText.getText().toString() != null
						&& etFeedbackText.getText().toString().trim().length() > 0) {

					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						SendFeedbackTask sendFeedbackTask = new SendFeedbackTask(
								mContext,
								Constant.passengerURL
										+ "/ws/v2/passenger/problem/?accessToken="

										+ accessToken, etFeedbackText.getText()
										.toString(), etFeedbackText);

						sendFeedbackTask.execute();
					} else {
						Toast.makeText(mContext, "No network connection.",
								Toast.LENGTH_SHORT).show();
					}
					hideKeybord(v);
				} else {
					Constant.alertdialog("nofeedback", mContext);

				}
			}
		});
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
	public void onDetach() {

		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etFeedbackText.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
		super.onDetach();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
	}

	@Override
	public void onCustomBackPressed() {
		getParentFragment().getChildFragmentManager().popBackStack();
	}

}