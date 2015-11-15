package com.android.cabapp.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.activity.NowJobsActivity;
import com.android.cabapp.adapter.NowJobsAdapter;
import com.android.cabapp.async.DriverSettingsTask;
import com.android.cabapp.datastruct.json.Card;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.datastruct.json.JobsList;
import com.android.cabapp.model.FetchJobs;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class NowFragment extends ListFragment {
	private static final String TAG = NowFragment.class.getSimpleName();
	static NowJobsAdapter listAdapter;
	ListView mListView;
	static List<Job> listDataHeader;
	// private int lastExpandedPosition = -1;
	Handler mHandler;
	ProgressBar nowProgress;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	public static Handler refreshListOnAcceptRejectHandler;
	public static Job acceptedJob;
	IntentFilter newJobsIntentFilter;
	RelativeLayout rlEmpty;

	public NowFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_now, container,
				false);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		mListView = (ListView) rootView.findViewById(android.R.id.list);
		rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rlEmpty);
		nowProgress = (ProgressBar) rootView.findViewById(R.id.nowProgress);
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

		Util.bIsNowOrPreebookFragment = true;

		Util.bIsNowFragment = true;
		Util.bIsPrebookFragment = false;

		if (MainActivity.slidingMenu.isMenuShowing())
			MainActivity.slidingMenu.toggle();

		prepareListData(AppValues.getNowJobs());
		setDataAdapter();

		// if (AppValues.getNowJobs().size() == 0)
		fetchData(true);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// if (AppValues.driverSettings != null
				// && AppValues.driverSettings.getAvailability().equals(
				// "true")) {
				if (listDataHeader != null) {
					Job job = listDataHeader.get(position);
					NowJobsActivity.mJob = job;
					Intent nowJobsIntent = new Intent(Util.mContext,
							NowJobsActivity.class);
					nowJobsIntent.putExtra("position", position);
					startActivity(nowJobsIntent);
				}
				// }
				// else {
				// Util.showToastMessage(Util.mContext,
				// "You are currently busy!", Toast.LENGTH_LONG);
				// }
			}
		});

		// Listview Group click listener
		// expListView.setOnGroupClickListener(new OnGroupClickListener() {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// // Toast.makeText(getApplicationContext(),
		// // "Group Clicked " + listDataHeader.get(groupPosition),
		// // Toast.LENGTH_SHORT).show();
		// return true;// return false will expands the listview
		// }
		// });

		// Listview Group expanded listener
		// expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(final int groupPosition) {
		// // Toast.makeText(getActivity(),
		// // listDataHeader.get(groupPosition) + " Expanded",
		// // Toast.LENGTH_SHORT).show();
		//
		// if (lastExpandedPosition != -1
		// && groupPosition != lastExpandedPosition) {
		// expListView.collapseGroup(lastExpandedPosition);
		// }
		// lastExpandedPosition = groupPosition;
		// expListView.post(new Runnable() {
		// public void run() {
		// // expListView.smoothScrollToPosition(groupPosition);
		// expListView.setSelection(groupPosition);
		// }
		// });
		//
		// // Log.e("NowJobsFragment", "Position: " + groupPosition
		// // + " List items :: " + expListView.getChildCount());
		//
		// // View childView = expListView.getChildAt(groupPosition);
		// // if (childView != null)
		// // if (groupPosition % 2 == 0)
		// // childView.setBackgroundColor(getResources().getColor(
		// // R.color.list_item_even_bg));
		// // else {
		// // childView.setBackgroundColor(getResources().getColor(
		// // R.color.list_item_odd_bg));
		// // }
		// }
		// });

		// Listview Group collasped listener
		// expListView.setOnGroupCollapseListener(new OnGroupCollapseListener()
		// {
		//
		// @Override
		// public void onGroupCollapse(int groupPosition) {
		// // Toast.makeText(getActivity(),
		// // listDataHeader.get(groupPosition) + " Collapsed",
		// // Toast.LENGTH_SHORT).show();
		//
		// }
		// });

		// Listview on child click listener
		// expListView.setOnChildClickListener(new OnChildClickListener() {
		//
		// @Override
		// public boolean onChildClick(ExpandableListView parent, View v,
		// int groupPosition, int childPosition, long id) {
		// // TODO Auto-generated method stub
		// // Toast.makeText(
		// // getActivity(),
		// // listDataHeader.get(groupPosition)
		// // + " : "
		// // + listDataChild.get(
		// // listDataHeader.get(groupPosition)).get(
		// // childPosition), Toast.LENGTH_SHORT)
		// // .show();
		// return false;
		// }
		// });

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				// if (message != null) {
				// String jobsListSize = (String) message.obj;
				// JobsFragment.setNowTabTitle(jobsListSize);
				// }
				if (nowProgress != null)
					nowProgress.setVisibility(View.GONE);

				if (mSwipeRefreshLayout != null)
					mSwipeRefreshLayout.setRefreshing(false);
				JobsFragment.setNowTabTitle("Now ("
						+ AppValues.nNowJobsFilteredCount + ")");

				setDataAdapter();
				if (mListView.getCount() > 0)
					rlEmpty.setVisibility(View.GONE);
				else
					rlEmpty.setVisibility(View.VISIBLE);
			}
		};

		refreshListOnAcceptRejectHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				DriverSettingsTask driverSettingTask = new DriverSettingsTask();
				driverSettingTask.mContext = Util.mContext;
				driverSettingTask.execute();

				Bundle bundle = (Bundle) message.obj;
				final int position = bundle.getInt("position");

				if (Constants.isDebug)
					Log.e(TAG, "POSITION::>   " + position);

				if (message.what == Constants.JOB_ACCEPTED_SUCCESS) {

					// Job job = listDataHeader.get(position);
					Fragment fragment = null;
					fragment = new com.android.cabapp.fragments.JobAcceptedFragment();
					if (fragment != null) {
						// FragmentManager fragmentManager =
						// ((FragmentActivity)
						// _context)
						// .getSupportFragmentManager();
						// fragmentManager.beginTransaction()
						// .replace(R.id.frame_container, fragment)
						// .addToBackStack("JobAcceptedFragment").commit();

						// Bundle bundle = new Bundle();
						bundle.putBoolean("isPreBook", false);
						String szPickUpAddress = acceptedJob
								.getPickupLocation().getAddressLine1();
						if (szPickUpAddress.contains(","))
							szPickUpAddress = szPickUpAddress.substring(0,
									szPickUpAddress.lastIndexOf(","));
						bundle.putString(Constants.PICK_UP_ADDRESS,
								szPickUpAddress);
						bundle.putString(Constants.PICK_UP_PINCODE, acceptedJob
								.getPickupLocation().getPostCode());

						String szDropOffAddress = acceptedJob.getDropLocation()
								.getAddressLine1();
						if (szDropOffAddress.contains(","))
							szDropOffAddress = szDropOffAddress.substring(0,
									szDropOffAddress.lastIndexOf(","));
						bundle.putString(Constants.DROP_ADDRESS,
								szDropOffAddress);
						bundle.putString(Constants.DROP_OFF_PINCODE,
								acceptedJob.getDropLocation().getPostCode());

						bundle.putString(Constants.NO_OF_PASSENGERS, String
								.valueOf(acceptedJob.getNumberOfPassengers()));
						bundle.putString(Constants.WHEEL_CHAIR_ACCESS_REQUIRED,
								acceptedJob.getWheelchairAccessRequired());
						bundle.putString(Constants.HEARING_IMPAIRED,
								acceptedJob.getIsHearingImpaired());
						bundle.putString(Constants.PICK_UP_LOCATION_LAT,
								acceptedJob.getPickupLocation().getLatitude());
						bundle.putString(Constants.PICK_UP_LOCATION_LNG,
								acceptedJob.getPickupLocation().getLongitude());
						bundle.putString(Constants.DROP_LOCATION_LAT,
								acceptedJob.getDropLocation().getLatitude());
						bundle.putString(Constants.DROP_LOCATION_LNG,
								acceptedJob.getDropLocation().getLongitude());
						bundle.putString(Constants.PASSENGER_NAME,
								acceptedJob.getName());
						bundle.putString(Constants.FARE,
								acceptedJob.getFixedPriceAmount());
						bundle.putString(Constants.JOB_PAYMENT_TYPE,
								acceptedJob.getPaymentType());
						bundle.putString(Constants.TIME,
								acceptedJob.getPickupDateTime());
						bundle.putString(
								Constants.PASSENGER_INTERNATIONAL_CODE,
								acceptedJob.getInternationalCode());
						bundle.putString(Constants.PASSENGER_NUMBER,
								acceptedJob.getMobileNumber());
						bundle.putString(Constants.PASSENGER_EMAIL,
								acceptedJob.getEmailAddress());
						// Put is cabShare
						if (acceptedJob.getCabShare() != null
								&& !acceptedJob.getCabShare().isEmpty()) {
							bundle.putString(Constants.CAB_SHARE,
									acceptedJob.getCabShare());
						}
						// Put cab miles
						if (acceptedJob.getCabMiles() != null
								&& !acceptedJob.getCabMiles().isEmpty()) {
							bundle.putString(Constants.CAB_MILES,
									acceptedJob.getCabMiles());
						}
						if (acceptedJob.getPaymentType().equalsIgnoreCase(
								"card")
								|| acceptedJob.getPaymentType()
										.equalsIgnoreCase("both")) {
							List<Card> cardsList = new ArrayList<Card>();
							cardsList.clear();
							if (acceptedJob.getCard() != null
									&& acceptedJob.getCard().size() > 0) {
								for (int i = 0; i < acceptedJob.getCard()
										.size(); i++) {
									Card newItem = new Card();
									newItem.setBrand(acceptedJob.getCard()
											.get(i).getBrand());
									newItem.setTruncatedPan(acceptedJob
											.getCard().get(i).getTruncatedPan());
									newItem.setIsSelected(acceptedJob.getCard()
											.get(i).getIsSelected());
									newItem.setPaymentToken(acceptedJob
											.getCard().get(i).getPaymentToken());
									cardsList.add(newItem);
								}
								Util.setCardsList(cardsList);
							}
						}

						// if (mapDistance.containsKey(job.getId())) {
						// bundle.putString(
						// Constants.DISTANCE_DROP,
						// mapDistance.get(job.getId()).szDistanceDropoff);
						// bundle.putString(
						// Constants.DISTANCE_PICKUP,
						// mapDistance.get(job.getId()).szDistancePickup);
						// }
						bundle.putString(Constants.LUGGAGE_NOTE,
								acceptedJob.getNote());
						bundle.putBoolean("isfrommyjobs", false);
						bundle.putBoolean("ishistory", false);
						bundle.putString(Constants.JOB_ID, acceptedJob.getId());
						fragment.setArguments(bundle);
						// bundle.putString("pickupDistance",
						// job.getPickupLocation().getAddressLine1());
						// bundle.putString("dropDistance",
						// job.getDropLocation().getAddressLine1());
						// removeChild(groupPosition, childPosition);
						// removeGroup(groupPosition);
						// mExpListView.collapseGroup(groupPosition);
						// updateTabsTitle();

						((MainActivity) Util.mContext).replaceFragment(
								fragment, true);

					}

				}

				// getActivity().runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// // if (listAdapter != null) {
				// // listAdapter.removeItem(position);
				// // }
				// }
				// });
			}
		};

		newJobsIntentFilter = new IntentFilter();
		newJobsIntentFilter.addAction("NewJobs");

		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(Util.mContext);
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(Util.mContext);
		locationBroadcastManager.unregisterReceiver(newJobsReceivedReceiver);

		Util.bIsNowOrPreebookFragment = false;
	}

	void fetchData(boolean bShowRefresh) {
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FetchJobs fetchJobs = new FetchJobs(Util.mContext);
				JobsList jobs = fetchJobs.getJobsList(false, Util.mContext,
						RootFragment.currentLocation);
				if (jobs != null) {
					List<Job> jobsList = jobs.getJobs();
					AppValues.setNowJobs(jobsList);
					Log.d("NowFragment", "JOB LIST:: " + jobsList);
					AppValues.nNowJobsFilteredCount = jobsList.size();
					// preparing list data
					Message msg = new Message();
					msg.obj = "Now (" + jobsList.size() + ")";
					prepareListData(AppValues.getNowJobs());
					mHandler.sendMessage(msg);
					return;
				}

				mHandler.sendEmptyMessage(0);
			}
		});

		if (Util.mContext != null && NetworkUtil.isNetworkOn(Util.mContext)) {
			networkThread.start();
			if (bShowRefresh)
				mListView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// mListView.getEmptyView().setVisibility(View.GONE);
						rlEmpty.setVisibility(View.GONE);
						nowProgress.setVisibility(View.VISIBLE);
					}
				});
		} else {
			mListView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mSwipeRefreshLayout != null)
						mSwipeRefreshLayout.setRefreshing(false);
				}
			});

			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	void setDataAdapter() {
		try {
			if (listAdapter == null)
				listAdapter = new NowJobsAdapter(Util.mContext, listDataHeader,
						true);
			else
				listAdapter.updateList(listDataHeader);

			applyFilter();
			// setting list adapter
			mListView.setAdapter(listAdapter);
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
