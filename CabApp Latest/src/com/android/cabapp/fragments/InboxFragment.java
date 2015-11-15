package com.android.cabapp.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.ChatActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.adapter.InboxAdapter;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.datastruct.json.MyJobsList;
import com.android.cabapp.model.MyJobs;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class InboxFragment extends RootFragment {

	private static final String TAG = InboxFragment.class.getSimpleName();

	ListView lvMessageList;
	InboxAdapter inboxAdapter;

	public static List<Job> jobsList;
	// TextView textNoMessages;
	RelativeLayout rlEmpty;
	ProgressBar inboxProgress;
	public static Handler mHandler;

	Bundle mBundle;

	public InboxFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_inbox, container,
				false);
		super.initWidgets(rootView, this.getClass().getName());

		lvMessageList = (ListView) rootView.findViewById(R.id.lvMessagesList);
		// textNoMessages = (TextView)
		// rootView.findViewById(android.R.id.empty);
		rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rlEmpty);
		inboxProgress = (ProgressBar) rootView.findViewById(R.id.inboxProgress);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		lvMessageList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (inboxAdapter != null && jobsList != null
						&& jobsList.size() > 0) {
					mBundle = new Bundle();
					mBundle.putString(Constants.PASSENGER_NAME,
							jobsList.get(position).getName());
					mBundle.putString(Constants.JOB_ID, jobsList.get(position)
							.getId());
					mBundle.putString(Constants.PICK_UP_PINCODE,
							jobsList.get(position).getPickupLocation()
									.getPostCode());
					mBundle.putString(Constants.DROP_OFF_PINCODE,
							jobsList.get(position).getDropLocation()
									.getPostCode());
					mBundle.putString(Constants.JSON_GET_MESSAGE,
							jobsList.get(position).getMessages().toString());
					mBundle.putString(Constants.PASSENGER_INTERNATIONAL_CODE,
							jobsList.get(position).getInternationalCode());
					mBundle.putString(Constants.PASSENGER_NUMBER,
							jobsList.get(position).getMobileNumber());
					mBundle.putString(Constants.HEARING_IMPAIRED,
							jobsList.get(position).getIsHearingImpaired());
					if (!ChatActivity.szJobID.isEmpty()) {
						ChatActivity.finishMe();
						ChatActivity.szJobID = "";
					}
					ChatActivity.szJobID = jobsList.get(position).getId();
					Intent intent = new Intent(Util.mContext,
							ChatActivity.class);
					intent.putExtras(mBundle);
					startActivity(intent);
				}

			}
		});

		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MyJobs fetchJobs = new MyJobs(Util.mContext);
				MyJobsList jobs = fetchJobs.getMyJobsList();
				if (jobs != null) {
					jobsList = jobs.getJobs();
				}

				// preparing list data
				mHandler.sendEmptyMessage(0);
			}
		});

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			networkThread.start();
			lvMessageList.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					rlEmpty.setVisibility(View.GONE);
					// textNoMessages.setVisibility(View.GONE);
					inboxProgress.setVisibility(View.VISIBLE);

				}
			});
		} else {
			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				boolean bIsAdapterFresh = false;
				if (inboxProgress != null)
					inboxProgress.setVisibility(View.GONE);

				if (message.what == 0) {
					if (jobsList != null && jobsList.size() > 0) {

						lvMessageList.setVisibility(View.VISIBLE);

						// preparing list data
						rlEmpty.setVisibility(View.GONE);
						// textNoMessages.setVisibility(View.GONE);
						applyFilter();

						// // sort the inbox list datewise descending
						Collections.sort(jobsList,
								new InboxMessagesComparator());

						if (inboxAdapter == null) {
							inboxAdapter = new InboxAdapter(Util.mContext,
									jobsList);
							bIsAdapterFresh = true;
						} else
							inboxAdapter.updateList(jobsList);

						// setting list adapter
						if (bIsAdapterFresh)
							lvMessageList.setAdapter(inboxAdapter);
					}

					lvMessageList.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (jobsList == null || jobsList.size() == 0) {
								// textNoMessages.setVisibility(View.VISIBLE);
								rlEmpty.setVisibility(View.VISIBLE);

							} else {
								// textNoMessages.setVisibility(View.GONE);
								rlEmpty.setVisibility(View.GONE);
							}
						}
					});

				} else if (message.what == 1) {
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

	class InboxMessagesComparator implements Comparator<Job> {

		@Override
		public int compare(Job lhs, Job rhs) {

			try {

				// {
				// "source": "passenger",
				// "message": "I'm waiting,are you on the way?",
				// "messageTime": "19/11/2014 06:27:32"
				// }

				String szLHSMessagesArray = lhs.getMessages().toString();
				JSONArray jLHSMessagesArray = new JSONArray(szLHSMessagesArray);

				String szRHSMessagesArray = rhs.getMessages().toString();
				JSONArray jRHSMessagesArray = new JSONArray(szRHSMessagesArray);

				String lhsInboxMessageTime = jLHSMessagesArray.getJSONObject(
						jLHSMessagesArray.length() - 1)
						.getString("messageTime");
				String rhsInboxMessageTime = jRHSMessagesArray.getJSONObject(
						jRHSMessagesArray.length() - 1)
						.getString("messageTime");

				Date lhsDate = new Date(lhsInboxMessageTime);

				Date rhsDate = new Date(rhsInboxMessageTime);

				if (rhsDate.after(lhsDate)) {
					return 1;
				} else if (rhsDate.before(lhsDate)) {
					return -1;
				} else {
					return 0;
				}

			} catch (Exception e) {
				return -1;
			}
		}
	}

	void applyFilter() {
		ArrayList<Job> filteredJobsList = new ArrayList<Job>();
		int nHoursInInbox = 0;
		if (AppValues.driverSettings != null
				&& AppValues.driverSettings.getHoursInInbox() != null)
			AppValues.driverSettings.getHoursInInbox();
		for (int i = 0; i < jobsList.size(); i++) {
			Job job = jobsList.get(i);
			String passengerName = job.getName();
			if (!passengerName.toLowerCase().equalsIgnoreCase("fake passenger")) {
				if (job.getCompletedAt() == null
						|| job.getCompletedAt().toString().equals(""))
					filteredJobsList.add(job);
				else {
					// completedAt = "16/08/2014 10:53:21";
					String szJobCompletedTime = job.getCompletedAt().toString();
					Date jobCompletedTime = Util
							.getDateFormatted(szJobCompletedTime);
					Calendar calendar = Calendar.getInstance();
					Date currentTime = calendar.getTime();
					calendar.setTime(jobCompletedTime);
					calendar.add(Calendar.HOUR_OF_DAY, nHoursInInbox);
					Date timeTillMessageAllowed = calendar.getTime();

					Log.e(TAG, "currentTime::> " + currentTime
							+ "  timeTillMessageAllowed::> "
							+ timeTillMessageAllowed + "   nHoursInInbox:> "
							+ nHoursInInbox + "   szJobCompletedTime::> "
							+ szJobCompletedTime);

					if (currentTime.before(timeTillMessageAllowed)) {
						filteredJobsList.add(job);
						if (Constants.isDebug)
							Log.e("applyFilter",
									"applyfilter - job id: " + job.getId());
					}
				}
			}
		}
		InboxFragment.jobsList = filteredJobsList;
		if (Constants.isDebug)
			Log.e("applyFilter",
					"applyfilter - jobs size: " + filteredJobsList.size());
	}
}
