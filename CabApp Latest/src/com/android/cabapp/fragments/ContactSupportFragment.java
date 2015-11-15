package com.android.cabapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.AddCardActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.activity.ReportAnIncidentActivity;
import com.android.cabapp.model.ContactSupport;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContactSupportFragment extends RootFragment {

	String TAG = ContactSupportFragment.class.getName();

	RelativeLayout relSend, relReportAnIncident;
	public static Handler mHandler;
	ProgressBar contactProgress;
	EditText etMessage;
	TextView txtTitle, tvSkipOverlay, tvNextOverlay;
	RelativeLayout rlOverlayContact, rlOverlayBottomContact,
			rlOverlayReportaProblem, rlOkGotIt;

	public ContactSupportFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				if (message.what == 0) {
					contactProgress.setVisibility(View.GONE);
					etMessage.setText("");
					Util.showToastMessage(Util.mContext,
							"Message successfully sent", Toast.LENGTH_LONG);
					Util.hideSoftKeyBoard(Util.mContext, etMessage);
				} else {
					Fragment fragment = null;
					fragment = new com.android.cabapp.fragments.JobsFragment();
					((MainActivity) Util.mContext)
							.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);

					if (fragment != null)
						((MainActivity) Util.mContext).replaceFragment(
								fragment, true);
				}
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contact_support,
				container, false);
		relSend = (RelativeLayout) rootView.findViewById(R.id.relSend);
		relReportAnIncident = (RelativeLayout) rootView
				.findViewById(R.id.relReportAIncident);
		contactProgress = (ProgressBar) rootView
				.findViewById(R.id.contactProgress);
		etMessage = (EditText) rootView.findViewById(R.id.etMessage);
		txtTitle = (TextView) rootView.findViewById(R.id.title);

		// For overlay
		tvSkipOverlay = (TextView) rootView.findViewById(R.id.tvSkipOverlay);
		tvNextOverlay = (TextView) rootView.findViewById(R.id.tvNextOverlay);
		rlOverlayContact = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayContact);
		rlOverlayBottomContact = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayBottomContact);
		rlOverlayReportaProblem = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayReportaProblem);
		rlOkGotIt = (RelativeLayout) rootView.findViewById(R.id.rlOkGotIt);

		boolean value = Util.getIsOverlaySeen(getActivity(), TAG);
		if (!Util.getIsOverlaySeen(getActivity(), TAG)) {
			rlOverlayContact.setVisibility(View.VISIBLE);
			rlOverlayBottomContact.setVisibility(View.VISIBLE);
			relSend.setEnabled(false);
			etMessage.setEnabled(false);

			if (MainActivity.slidingMenu != null)
				MainActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		String szTitle = "<font color=#fd6f01>@&nbsp;&nbsp;</font>"
				+ getResources().getString(R.string.contact_support);
		txtTitle.setText(Html.fromHtml(szTitle));

		relSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.hideSoftKeyBoard(Util.mContext, relSend);
				sendMessage();
			}
		});

		relReportAnIncident.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Util.mContext,
						ReportAnIncidentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		tvSkipOverlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rlOverlayContact.setVisibility(View.GONE);
				rlOverlayBottomContact.setVisibility(View.GONE);
				relSend.setEnabled(true);
				etMessage.setEnabled(true);
				Util.setIsOverlaySeen(getActivity(), true, TAG);

				if (MainActivity.slidingMenu != null)
					MainActivity.slidingMenu
							.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}
		});

		tvNextOverlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlOverlayContact.setVisibility(View.GONE);
				rlOverlayBottomContact.setVisibility(View.GONE);
				rlOverlayReportaProblem.setVisibility(View.VISIBLE);
				relSend.setEnabled(false);
				etMessage.setEnabled(false);
			}
		});

		rlOkGotIt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlOverlayContact.setVisibility(View.GONE);
				rlOverlayBottomContact.setVisibility(View.GONE);
				rlOverlayReportaProblem.setVisibility(View.GONE);
				Util.setIsOverlaySeen(getActivity(), true, TAG);
				relSend.setEnabled(true);
				etMessage.setEnabled(true);

				if (MainActivity.slidingMenu != null)
					MainActivity.slidingMenu
							.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

			}
		});
	}

	void sendMessage() {

		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ContactSupport contactSupport = new ContactSupport(etMessage
						.getText().toString(), Util.mContext);
				String response = contactSupport.getResponse();

				if (response != null)
					mHandler.sendEmptyMessage(0);
			}
		});

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			if (etMessage.getText().toString().length() > 0) {
				networkThread.start();
				contactProgress.setVisibility(View.VISIBLE);
			} else {
				Util.showToastMessage(Util.mContext, "Please add a message",
						Toast.LENGTH_LONG);
			}
		} else {
			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

}
