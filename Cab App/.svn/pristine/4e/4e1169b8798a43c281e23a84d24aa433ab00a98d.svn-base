package com.android.cabapp.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.NowJobsExpandableListAdapter;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.datastruct.json.JobsList;
import com.android.cabapp.model.FetchJobs;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class PreBookFragment extends ListFragment {
	static NowJobsExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	static List<Job> listDataHeader;
	private int lastExpandedPosition = -1;

	Handler mHandler;
	ProgressBar prebookProgress;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	IntentFilter newJobsIntentFilter;
	RelativeLayout rlEmpty;

	int nExpandedJobID = -1;

	public PreBookFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_pre_book, container,
				false);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		expListView = (ExpandableListView) rootView
				.findViewById(android.R.id.list);
		prebookProgress = (ProgressBar) rootView
				.findViewById(R.id.prebookProgress);
		rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rlEmpty);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						// do work here
						fetchData(true);
					}
				});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		listAdapter = null;
		Util.bIsNowOrPreebookFragment = true;

		Util.bIsNowFragment = false;
		Util.bIsPrebookFragment = true;

		prepareListData(AppValues.getPrebook());
		setDataAdapter();

		// if (AppValues.getPrebook().size() == 0)
		fetchData(true);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(final int groupPosition) {
				// Toast.makeText(getActivity(),
				// listDataHeader.get(groupPosition) + " Expanded",
				// Toast.LENGTH_SHORT).show();

				if (lastExpandedPosition != -1
						&& groupPosition != lastExpandedPosition) {
					expListView.collapseGroup(lastExpandedPosition);
				} else {
					lastExpandedPosition = groupPosition;
					expListView.post(new Runnable() {
						public void run() {
							// expListView.smoothScrollToPosition(groupPosition);
							expListView.setSelection(groupPosition);
						}
					});

					nExpandedJobID = Integer.parseInt(listDataHeader.get(
							groupPosition).getId());

					// Log.e("NowJobsFragment", "Position: " + groupPosition
					// + " List items :: " + expListView.getChildCount());

					// View childView = expListView.getChildAt(groupPosition);
					// if (childView != null)
					// if (groupPosition % 2 == 0)
					// childView.setBackgroundColor(getResources().getColor(
					// R.color.list_item_even_bg));
					// else {
					// childView.setBackgroundColor(getResources().getColor(
					// R.color.list_item_odd_bg));
					// }
				}
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				// Toast.makeText(getActivity(),
				// listDataHeader.get(groupPosition) + " Collapsed",
				// Toast.LENGTH_SHORT).show();
				lastExpandedPosition = -1;
				nExpandedJobID = -1;
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(
				// getActivity(),
				// listDataHeader.get(groupPosition)
				// + " : "
				// + listDataChild.get(
				// listDataHeader.get(groupPosition)).get(
				// childPosition), Toast.LENGTH_SHORT)
				// .show();
				return false;
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				// if (message != null) {
				// String jobsListSize = (String) message.obj;
				// JobsFragment.setPreBookTabTitle(jobsListSize);
				// }

				if (prebookProgress != null)
					prebookProgress.setVisibility(View.GONE);

				if (mSwipeRefreshLayout != null)
					mSwipeRefreshLayout.setRefreshing(false);

				setDataAdapter();
				if (expListView.getCount() > 0)
					rlEmpty.setVisibility(View.GONE);
				else
					rlEmpty.setVisibility(View.VISIBLE);
			}
		};

		newJobsIntentFilter = new IntentFilter();
		newJobsIntentFilter.addAction("NewJobs");

		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		locationBroadcastManager.registerReceiver(newJobsReceivedReceiver,
				newJobsIntentFilter);
	}

	private BroadcastReceiver newJobsReceivedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!Util.getAccessToken(Util.mContext).isEmpty())
				fetchData(false);
		}
	};

	public void onStop() {
		super.onStop();
		if (Constants.isDebug)
			Log.e("PreBook", "onStop");
		listAdapter = null;
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (Constants.isDebug)
			Log.e("PreBook", "onDestroy");
		super.onDestroy();
		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		locationBroadcastManager.unregisterReceiver(newJobsReceivedReceiver);

		Util.bIsNowOrPreebookFragment = false;
		listAdapter = null;
	}

	void fetchData(boolean bShowRefresh) {
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FetchJobs fetchJobs = new FetchJobs(getActivity());
				JobsList jobs = fetchJobs.getJobsList(true, getActivity(),
						RootFragment.currentLocation);
				if (jobs != null) {
					List<Job> jobsList = jobs.getJobs();
					AppValues.setPreBook(jobsList);
					Log.d("PreBookFragment", "JOB LIST:: " + jobsList);
					AppValues.nPreBookJbsFilteredCount = jobsList.size();
					// preparing list data
					Message msg = new Message();
					msg.obj = "Pre-book (" + jobsList.size() + ")";
					prepareListData(AppValues.getPrebook());
					mHandler.sendMessage(msg);
					return;
				}

				mHandler.sendEmptyMessage(0);
			}
		});

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			networkThread.start();
			if (bShowRefresh)
				expListView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// expListView.getEmptyView().setVisibility(View.GONE);
						rlEmpty.setVisibility(View.GONE);
						prebookProgress.setVisibility(View.VISIBLE);
					}
				});
		} else {
			Util.showToastMessage(getActivity(),
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	void setDataAdapter() {
		try {
			if (listAdapter == null) {
				if (Constants.isDebug)
					Log.e("PreBook", "setDataAdapter()-If");
				listAdapter = new NowJobsExpandableListAdapter(Util.mContext,
						listDataHeader, expListView, false);
				// setting list adapter
				expListView.setAdapter(listAdapter);
			} else {
				if (Constants.isDebug)
					Log.e("PreBook", "setDataAdapter()-Else");
				((Activity) Util.mContext).runOnUiThread(new Runnable() {
					public void run() {
						listAdapter.updateList(listDataHeader);
					}
				});
			}

			applyFilter();

			if (nExpandedJobID != -1)
				for (int i = 0; i < listDataHeader.size(); i++) {
					if (nExpandedJobID == Integer.parseInt(listDataHeader
							.get(i).getId())) {
						expListView.expandGroup(i);
					}
				}

			JobsFragment.setPreBookTabTitle("Pre-book ("
					+ AppValues.nPreBookJbsFilteredCount + ")");

		} catch (Exception e) {

		}
	}

	void applyFilter() {
		if (AppValues.isCashSelected) {
			filterAdapter("filterbycash");
		}
		if (AppValues.isCashCardSelected) {
			filterAdapter("filterbyall");
		}
		if ((AppValues.isCashSelected && AppValues.isCashCardSelected)
				|| (!AppValues.isCashCardSelected && !AppValues.isCashSelected)) {
			filterAdapter("filterbyall");
		}

		if (listAdapter != null)
			listDataHeader = listAdapter.getCurrentList();

	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData(List<Job> jobList) {
		listDataHeader = new ArrayList<Job>();
		listDataHeader = jobList;
	}

	static void filterAdapter(String filter) {
		if (listAdapter != null)
			listAdapter.getFilter().filter(filter);
	}

}
