package com.android.cabapp.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.datastruct.json.JobsList;
import com.android.cabapp.model.Availability;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.FetchJobs;
import com.android.cabapp.model.GetAllCityList;
import com.android.cabapp.model.GetAllCountryList;
import com.android.cabapp.model.MyJobsCount;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.android.cabapp.view.OverlayRectangle;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class JobsFragment extends RootFragment {
	private static final String TAG = JobsFragment.class.getSimpleName();

	TabHost mTabHost;
	TabManager mTabManager;

	ImageView imageViewFilter;
	TextView tvAvailable, tvCash, tvCashCard, tvCabPay, tvHome, tvComingIn,
			tvGoingTo, tvCashOnly, tvAroundMe, tvEditFilters, tvCashback;

	RelativeLayout rlFilterOptions;

	// private static boolean isHomeSelected = false;
	// private static boolean isComingInSelected = false;
	// private static boolean isGoingToSelected = false;
	// private static boolean isCashOnlySelected = false;
	// private static boolean isAroundMeSelected = false;
	// private static boolean isEditFiltersSelected = false;

	static TextView tvTabsTitleNow;
	static TextView tvTabsTitlePreBook;

	private ProgressDialog dialogJobFragment;

	public static SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs";
	Editor editor;

	private ProgressDialog fetchSettingsDialog;
	Handler mSettingsHandler;

	IntentFilter newJobsIntentFilter;
	View nowTabsView, preBookView;

	// Overlay
	RelativeLayout rlJobsOkyGotIt, rlJobsOverlay;

	public JobsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabManager = new TabManager(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_jobs, container, false);
		mTabHost = mTabManager.handleCreateView(v);

		Util.hideSoftKeyBoard(Util.mContext, v);

		rlJobsOverlay = (RelativeLayout) v
				.findViewById(R.id.rlJobsOverlayParent);
		rlJobsOkyGotIt = (RelativeLayout) v.findViewById(R.id.rlJobsOkyGotIt);
		rlJobsOkyGotIt.setOnTouchListener(new TextTouchListener());

		imageViewFilter = (ImageView) v.findViewById(R.id.imageviewFilter);
		tvAvailable = (TextView) v.findViewById(R.id.tvAvailable);
		tvCash = (TextView) v.findViewById(R.id.tvCash);
		// AppValues.isCashSelected = true;
		// AppValues.isCashCardSelected = false;

		tvCashCard = (TextView) v.findViewById(R.id.tvCashCard);
		tvCabPay = (TextView) v.findViewById(R.id.tvCabPay);
		tvHome = (TextView) v.findViewById(R.id.tvHome);
		tvComingIn = (TextView) v.findViewById(R.id.tvComingIn);
		tvGoingTo = (TextView) v.findViewById(R.id.tvGoingTo);
		tvCashOnly = (TextView) v.findViewById(R.id.tvCashOnly);
		tvAroundMe = (TextView) v.findViewById(R.id.tvAroundMe);
		tvEditFilters = (TextView) v.findViewById(R.id.tvEditFilters);
		rlFilterOptions = (RelativeLayout) v.findViewById(R.id.rlFilterOptions);
		tvCashback = (TextView) v.findViewById(R.id.tvCashback);

		imageViewFilter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleFilter(rlFilterOptions);
			}
		});

		ImageView ivTabImage;

		nowTabsView = LayoutInflater.from(getActivity()).inflate(
				R.layout.tabs_row, null);
		ivTabImage = (ImageView) nowTabsView.findViewById(R.id.ivTabImage);
		tvTabsTitleNow = (TextView) nowTabsView.findViewById(R.id.tvTabsTitle);
		ivTabImage.setImageDrawable(getResources().getDrawable(
				R.drawable.nowicon));
		tvTabsTitleNow.setText("Now (" + AppValues.nNowJobsFilteredCount + ")");

		preBookView = LayoutInflater.from(getActivity()).inflate(
				R.layout.tabs_row, null);
		ivTabImage = (ImageView) preBookView.findViewById(R.id.ivTabImage);
		tvTabsTitlePreBook = (TextView) preBookView
				.findViewById(R.id.tvTabsTitle);
		ivTabImage.setImageDrawable(getResources().getDrawable(
				R.drawable.prebookicon));
		tvTabsTitlePreBook.setText("Pre-book ("
				+ AppValues.nPreBookJbsFilteredCount + ")");

		mTabManager.addTab(
				mTabHost.newTabSpec("Now").setIndicator(nowTabsView),
				NowFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("Pre-book").setIndicator(preBookView),
				PreBookFragment.class, null);

		if (!Util.getIsOverlaySeen(getActivity(), TAG)) {
			nowTabsView.setEnabled(false);
			preBookView.setEnabled(false);
			rlJobsOverlay.setVisibility(View.VISIBLE);
			tvAvailable.setEnabled(false);
			tvCashback.setEnabled(false);
			tvCabPay.setEnabled(false);
			if (MainActivity.slidingMenu != null)
				MainActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		super.initWidgets(v, this.getClass().getName());
		return v;
	}

	void toggleAvailableStatus() {

		if (isAdded()) {
			if (MainActivity.mMainActivityContext != null)
				((Activity) MainActivity.mMainActivityContext)
						.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (AppValues.driverSettings == null
										|| AppValues.driverSettings
												.getAvailability() == null
										|| AppValues.driverSettings
												.getAvailability().equals(
														"true")) {
									tvAvailable
											.setCompoundDrawablesWithIntrinsicBounds(
													R.drawable.availablegreen,
													0, 0, 0);
									tvAvailable.setText("Free");
									tvAvailable.setTextColor(getResources()
											.getColor(R.color.textcolor_green));
									AppValues.isAvailableSelected = true;
								} else {
									tvAvailable
											.setCompoundDrawablesWithIntrinsicBounds(
													R.drawable.available_red,
													0, 0, 0);
									tvAvailable.setText("Busy");
									tvAvailable.setTextColor(getResources()
											.getColor(R.color.textcolor_red));
									AppValues.isAvailableSelected = false;
								}
							}
						});
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// isCashSelected = toggleTextView(tvCash,
		// R.drawable.cashonlyiconselected, R.drawable.cashonlyicon,
		// isCashSelected);
		toggleTextViewBackgroundFilter();
		applyFilter();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (AppValues.driverSettings == null) {
			fetchSettingsAndAccountData();
		}
	}

	void toggleFilter(RelativeLayout rlFilter) {

		if (rlFilter.getVisibility() == 0) {
			rlFilter.setVisibility(View.GONE);
		} else {
			rlFilter.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		tvAvailable.setOnTouchListener(new TextTouchListener());
		tvCash.setOnTouchListener(new TextTouchListener());
		tvCashCard.setOnTouchListener(new TextTouchListener());
		tvCabPay.setOnTouchListener(new TextTouchListener());
		tvHome.setOnTouchListener(new TextTouchListener());
		tvComingIn.setOnTouchListener(new TextTouchListener());
		tvGoingTo.setOnTouchListener(new TextTouchListener());
		tvCashOnly.setOnTouchListener(new TextTouchListener());
		tvAroundMe.setOnTouchListener(new TextTouchListener());
		tvEditFilters.setOnTouchListener(new TextTouchListener());
		tvCashback.setOnTouchListener(new TextTouchListener());

		/* if bank details added, set to card/cash */
		if (AppValues.driverDetails != null
				&& AppValues.driverDetails.getPaymentType().equals("both")) {
			AppValues.isCashSelected = false;
			AppValues.isCashCardSelected = true;
			toggleTextViewBackgroundFilter();
		}

		mSettingsHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0) {

					try {
						if (fetchSettingsDialog != null) {
							fetchSettingsDialog.dismiss();
							fetchSettingsDialog = null;
						}
					} catch (Exception e) {

					}

					if (Constants.isDebug)
						Log.e("JobsFragment", "DriverSettings is:: "
								+ AppValues.driverSettings
								+ " DriverDetails is:: "
								+ AppValues.driverDetails);

					if (AppValues.driverSettings == null) {
						try {
							fetchSettingsAndAccountData();
						} catch (Exception e) {

						}
					} else {
						setCashBackText();
						toggleAvailableStatus();
					}
				} else if (msg.what == 1) {
					// update tab title with count
					if (!Util.bIsPrebookFragment) {
						setPreBookTabTitle("Pre-book ("
								+ AppValues.nPreBookJbsFilteredCount + ")");
					} else if (!Util.bIsNowFragment) {
						setNowTabTitle("Now ("
								+ AppValues.nNowJobsFilteredCount + ")");
					}
				}
			}

		};

		toggleAvailableStatus();

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
				fetchNowAndPrebookJobs();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		locationBroadcastManager.unregisterReceiver(newJobsReceivedReceiver);

		if (fetchSettingsDialog != null) {
			fetchSettingsDialog.dismiss();
			fetchSettingsDialog = null;
		}
	};

	void fetchNowAndPrebookJobs() {
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FetchJobs fetchJobs = new FetchJobs(getActivity());
				JobsList jobs = null;

				if (!Util.bIsPrebookFragment) { // get pre book list
					jobs = fetchJobs.getJobsList(true, getActivity(),
							RootFragment.currentLocation);
				} else if (!Util.bIsNowFragment) { // get now jobs list
					jobs = fetchJobs.getJobsList(false, getActivity(),
							RootFragment.currentLocation);
				}

				if (jobs != null) {
					List<Job> jobsList = jobs.getJobs();

					if (!Util.bIsPrebookFragment) {
						AppValues.setPreBook(jobsList);
						AppValues.nPreBookJbsFilteredCount = jobsList.size();
						Log.d("JobsFragment", " Prebook JOB count:: "
								+ AppValues.nPreBookJbsFilteredCount);
					} else if (!Util.bIsNowFragment) {
						AppValues.setNowJobs(jobsList);
						AppValues.nNowJobsFilteredCount = jobsList.size();
						Log.d("JobsFragment", " Now JOB count:: "
								+ AppValues.nNowJobsFilteredCount);
					}

					if (mSettingsHandler != null) {
						mSettingsHandler.sendEmptyMessage(1);
					}
					return;
				}
			}
		});

		if (Util.mContext != null && NetworkUtil.isNetworkOn(Util.mContext)) {
			networkThread.start();
		} else {
			// Util.showToastMessage(getActivity(),
			// getResources().getString(R.string.no_network_error),
			// Toast.LENGTH_LONG);
		}
	}

	void fetchSettingsAndAccountData() {
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DriverAccountDetails driverAccount = new DriverAccountDetails(
						Util.mContext);
				driverAccount.retriveAccountDetails(Util.mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						Util.mContext);
				driverSettings.retriveDriverSettings(Util.mContext);

				if (AppValues.countryList == null) {
					GetAllCountryList allCountry = new GetAllCountryList(
							Util.mContext);
					AppValues.countryList = allCountry
							.getCountryList(Util.mContext);
				}
				if (AppValues.cityList == null) {
					GetAllCityList allCity = new GetAllCityList(Util.mContext);
					AppValues.cityList = allCity.getCityList(Util.mContext);
				}
				if (Util.mContext != null) {

					MyJobsCount myJobsCount = new MyJobsCount(Util.mContext);
					AppValues.nJobsCount = myJobsCount.getMyJobsCount();
					((Activity) Util.mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							((MainActivity) Util.mContext)
									.updateAndSetMenuItems(AppValues.nJobsCount);
							updateJobsCountBadge(AppValues.nJobsCount);
						}
					});

					if (mSettingsHandler != null)
						mSettingsHandler.sendEmptyMessage(0);
				}
			}
		});

		if (Util.mContext != null)
			if (Util.getAccessToken(Util.mContext) != null
					&& NetworkUtil.isNetworkOn(Util.mContext)) {
				try {
					if (fetchSettingsDialog == null) {
						fetchSettingsDialog = new ProgressDialog(Util.mContext);
						fetchSettingsDialog.setMessage("Loading...");
						fetchSettingsDialog.setCancelable(false);
						fetchSettingsDialog.setCanceledOnTouchOutside(false);
						if (!Util.getIsOverlaySeen(getActivity(), TAG))
							fetchSettingsDialog.show();
					}
				} catch (Exception e) {

				}
				networkThread.start();
			} else {
				Util.showToastMessage(Util.mContext,
						getResources().getString(R.string.no_network_error),
						Toast.LENGTH_LONG);
				showDialog("No network connection. Please turn on network and click OK to fetch driver settings!");
			}
	}

	void showDialog(String message) {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(Util.mContext)
				.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						argDialog.cancel();
						if (AppValues.driverSettings == null) {
							fetchSettingsAndAccountData();
						}
					}
				}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		mTabManager.handleViewStateRestored(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabManager.handleDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mTabManager.handleSaveInstanceState(outState);
	}

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. DO NOT USE THIS. If
	 * you want tabs in a fragment, use the support v13 library's
	 * FragmentTabHost class, which takes care of all of this for you (in a
	 * simpler way even).
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {
		private final Context mContext;
		private final FragmentManager mManager;
		private final int mContainerId;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		private TabHost mTabHost;
		private TabInfo mLastTab;
		private boolean mInitialized;
		private String mCurrentTabTag;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(Context context, FragmentManager manager,
				int containerId) {
			mContext = context;
			mManager = manager;
			mContainerId = containerId;
		}

		public TabHost handleCreateView(View root) {
			if (mTabHost != null) {
				throw new IllegalStateException("TabHost already set");
			}
			mTabHost = (TabHost) root.findViewById(android.R.id.tabhost);
			mTabHost.setup();
			mTabHost.setOnTabChangedListener(this);
			return mTabHost;
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();
			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
		}

		public void handleViewStateRestored(Bundle savedInstanceState) {
			if (savedInstanceState != null) {
				mCurrentTabTag = savedInstanceState.getString("tab");
			}
			mTabHost.setCurrentTabByTag(mCurrentTabTag);

			String currentTab = mTabHost.getCurrentTabTag();

			// Go through all tabs and make sure their fragments match
			// the correct state.
			FragmentTransaction ft = null;
			for (int i = 0; i < mTabs.size(); i++) {
				TabInfo tab = mTabs.get(i);
				tab.fragment = mManager.findFragmentByTag(tab.tag);
				if (tab.fragment != null && !tab.fragment.isDetached()) {
					if (tab.tag.equals(currentTab)) {
						// The fragment for this tab is already there and
						// active, and it is what we really want to have
						// as the current tab. Nothing to do.
						mLastTab = tab;
					} else {
						// This fragment was restored in the active state,
						// but is not the current tab. Deactivate it.
						if (ft == null) {
							ft = mManager.beginTransaction();
						}
						ft.detach(tab.fragment);
					}
				}
			}

			// We are now ready to go. Make sure we are switched to the
			// correct tab.
			mInitialized = true;
			ft = doTabChanged(currentTab, ft);
			if (ft != null) {
				ft.commit();
				mManager.executePendingTransactions();
			}
		}

		public void handleDestroyView() {
			mCurrentTabTag = mTabHost.getCurrentTabTag();
			mTabHost = null;
			mTabs.clear();
			mInitialized = false;
		}

		public void handleSaveInstanceState(Bundle outState) {
			outState.putString("tab",
					mTabHost != null ? mTabHost.getCurrentTabTag()
							: mCurrentTabTag);
		}

		@Override
		public void onTabChanged(String tabId) {
			if (!mInitialized) {
				return;
			}
			FragmentTransaction ft = doTabChanged(tabId, null);
			if (ft != null) {
				ft.commit();
			}
		}

		private FragmentTransaction doTabChanged(String tabId,
				FragmentTransaction ft) {
			TabInfo newTab = null;
			for (int i = 0; i < mTabs.size(); i++) {
				TabInfo tab = mTabs.get(i);
				if (tab.tag.equals(tabId)) {
					newTab = tab;
				}
			}
			if (newTab == null) {
				throw new IllegalStateException("No tab known for tag " + tabId);
			}
			if (mLastTab != newTab) {
				if (ft == null) {
					ft = mManager.beginTransaction();
				}
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mContext,
								newTab.clss.getName(), newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
					} else {
						ft.attach(newTab.fragment);
					}
				}

				mLastTab = newTab;
			}
			return ft;
		}
	}

	class TextTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				// if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// toggleTextViewBackgroundFilter((TextView) v);
				// }

				switch (v.getId()) {
				case R.id.tvCash:
					// isCashSelected = toggleTextView(tvCash,
					// R.drawable.cashonlyiconselected,
					// R.drawable.cashonlyicon, isCashSelected);
					AppValues.isCashSelected = true;
					AppValues.isCashCardSelected = false;
					break;
				case R.id.tvCashCard:
					// isCashCardSelected = toggleTextView(tvCashCard,
					// R.drawable.cominginiconselected,
					// R.drawable.cominginicon, isCashCardSelected);
					AppValues.isCashSelected = false;
					AppValues.isCashCardSelected = true;
					break;

				// case R.id.tvHome:
				// isHomeSelected = toggleTextView(tvHome,
				// R.drawable.homeiconselected, R.drawable.homeicon,
				// isHomeSelected);
				// break;
				// case R.id.tvComingIn:
				// isComingInSelected = toggleTextView(tvComingIn,
				// R.drawable.cominginiconselected,
				// R.drawable.cominginicon, isComingInSelected);
				// break;
				// case R.id.tvGoingTo:
				// isGoingToSelected = toggleTextView(tvGoingTo,
				// R.drawable.goingtoiconselected,
				// R.drawable.goingtoicon, isGoingToSelected);
				// break;
				// case R.id.tvCashOnly:
				// isCashOnlySelected = toggleTextView(tvCashOnly,
				// R.drawable.cashonlyiconselected,
				// R.drawable.cashonlyicon, isCashOnlySelected);
				// break;
				// case R.id.tvAroundMe:
				// isAroundMeSelected = toggleTextView(tvAroundMe,
				// R.drawable.arroundmeiconselected,
				// R.drawable.arroundmeicon, isAroundMeSelected);
				//
				// break;
				// case R.id.tvEditFilters:
				// isEditFiltersSelected = toggleTextView(tvEditFilters,
				// R.drawable.editfiltersiconselected,
				// R.drawable.editfiltersicon, isEditFiltersSelected);
				// break;

				case R.id.tvAvailable:
					if (AppValues.isCashSelected) {
						tvCash.setCompoundDrawablesWithIntrinsicBounds(0,
								R.drawable.cashonlyiconselected, 0, 0);
						tvCash.setTextColor(getResources().getColor(
								R.color.textview_selected));
					} else if (AppValues.isCashCardSelected) {
						tvCashCard.setCompoundDrawablesWithIntrinsicBounds(0,
								R.drawable.allpayment, 0, 0);
						tvCashCard.setTextColor(getResources().getColor(
								R.color.textview_selected));
					}

					dialogJobFragment = new ProgressDialog(getActivity());
					dialogJobFragment.setMessage("Loading...");
					dialogJobFragment.setCancelable(false);
					if (Util.getIsOverlaySeen(getActivity(), TAG))
						dialogJobFragment.show();

					AvailabilityTask availableTask = new AvailabilityTask();
					availableTask.bIsAvailable = !AppValues.isAvailableSelected;
					// availableTask.txtView = ((TextView) v);
					availableTask.execute();
					break;

				case R.id.tvCabPay:
					Fragment fragment = new com.android.cabapp.fragments.CabPayFragment();
					if (fragment != null) {
						((MainActivity) getActivity())
								.setSlidingMenuPosition(Constants.CAB_PAY_FRAGMENT);
						((MainActivity) getActivity()).replaceFragment(
								fragment, false);
					}
					break;

				case R.id.rlJobsOkyGotIt:
					Util.setIsOverlaySeen(getActivity(), true, TAG);
					rlJobsOverlay.setVisibility(View.GONE);
					tvAvailable.setEnabled(true);
					tvCashback.setEnabled(true);
					tvCabPay.setEnabled(true);
					nowTabsView.setEnabled(true);
					preBookView.setEnabled(true);
					if (MainActivity.slidingMenu != null)
						MainActivity.slidingMenu
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;

				case R.id.tvCashback:
					cashBackClick();
					break;

				}

				toggleTextViewBackgroundFilter();

				applyFilter();
			}

			// if (v == tvCabPay) {
			// Fragment fragment = new
			// com.android.cabapp.fragments.CabPayFragment();
			// if (fragment != null) {
			// // ((MainActivity) getActivity()).replaceFragment(fragment);
			// ((MainActivity) getActivity())
			// .setSlidingMenuPosition(Constants.CAB_PAY_FRAGMENT);
			// FragmentManager fragmentManager = getActivity()
			// .getSupportFragmentManager();
			// fragmentManager.beginTransaction()
			// .replace(R.id.frame_container, fragment).commit();
			// }
			// }
			return true;
		}
	}

	void cashBackClick() {

		String szPromoCode = "";
		if (AppValues.driverSettings != null
				&& AppValues.driverSettings.getPromoCode() != null) {
			szPromoCode = AppValues.driverSettings.getPromoCode();
		}

		Resources resources = getResources();
		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		emailIntent
				.putExtra(Intent.EXTRA_SUBJECT, "Promo Code: " + szPromoCode);
		emailIntent.setType("message/rfc822");

		PackageManager pm = getActivity().getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");

		Intent openInChooser = Intent.createChooser(emailIntent, "Promo code: "
				+ szPromoCode);

		List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;

			Intent intent = new Intent();
			intent.setComponent(new ComponentName(packageName,
					ri.activityInfo.name));
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "Promo code: " + szPromoCode);
			intentList.add(new LabeledIntent(intent, packageName, ri
					.loadLabel(pm), ri.icon));
		}
		// convert intentList to array
		LabeledIntent[] extraIntents = intentList
				.toArray(new LabeledIntent[intentList.size()]);

		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);

	}

	void applyFilter() {
		int nCurrentTabSelected = mTabHost.getCurrentTab();
		if (nCurrentTabSelected == 0) {
			if (AppValues.isCashSelected) {
				NowFragment.filterAdapter("filterbycash");
			}
			if (AppValues.isCashCardSelected) {
				NowFragment.filterAdapter("filterbyall");
			}
			if ((AppValues.isCashSelected && AppValues.isCashCardSelected)
					|| (!AppValues.isCashCardSelected && !AppValues.isCashSelected)) {
				NowFragment.filterAdapter("filterbyall");
			}

		}

		if (nCurrentTabSelected == 1) {
			if (AppValues.isCashSelected) {
				PreBookFragment.filterAdapter("filterbycash");
			}
			if (AppValues.isCashCardSelected) {
				PreBookFragment.filterAdapter("filterbyall");
			}
			if ((AppValues.isCashSelected && AppValues.isCashCardSelected)
					|| (!AppValues.isCashCardSelected && !AppValues.isCashSelected)) {
				PreBookFragment.filterAdapter("filterbyall");
			}
		}
	}

	boolean toggleTextView(TextView tv, int selectedResId, int unselectedResId,
			boolean isSelected) {
		if (!isSelected) {
			isSelected = true;
			tv.setCompoundDrawablesWithIntrinsicBounds(0, selectedResId, 0, 0);
			tv.setTextColor(getResources().getColor(R.color.textview_selected));
		} else {
			isSelected = false;
			tv.setTextColor(getResources().getColor(R.color.textcolor_grey));
			tv.setCompoundDrawablesWithIntrinsicBounds(0, unselectedResId, 0, 0);
		}
		return isSelected;
	}

	void toggleTextViewBackgroundFilter() {
		tvCash.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.cashonlyicon, 0, 0);
		tvCashCard.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.allpaymentunselected, 0, 0);

		tvCash.setTextColor(getResources().getColor(R.color.textcolor_grey));
		tvCashCard
				.setTextColor(getResources().getColor(R.color.textcolor_grey));

		if (AppValues.isCashSelected) {
			tvCash.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.cashonlyiconselected, 0, 0);
			tvCash.setTextColor(getResources().getColor(
					R.color.textview_selected));
			AppValues.isCashSelected = true;
			AppValues.isCashCardSelected = false;
		} else if (AppValues.isCashCardSelected) {
			tvCashCard.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.allpayment, 0, 0);
			tvCashCard.setTextColor(getResources().getColor(
					R.color.textview_selected));
			AppValues.isCashCardSelected = true;
			AppValues.isCashSelected = false;
		}

	}

	void toggleTextViewBackground(TextView textViewSelected) {

		if (textViewSelected.getText().equals("Available")) {
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.unavailable, 0, 0, 0);
			textViewSelected.setText("Unavailable");
			textViewSelected.setTextColor(getResources().getColor(
					R.color.textcolor_grey));
			AppValues.isAvailableSelected = false;
		} else {
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.available, 0, 0, 0);
			textViewSelected.setText("Available");
			textViewSelected.setTextColor(getResources().getColor(
					android.R.color.white));
			AppValues.isAvailableSelected = true;
		}
	}

	public static void setPreBookTabTitle(String title) {
		tvTabsTitlePreBook.setText(title);
	}

	public static void setNowTabTitle(String title) {
		tvTabsTitleNow.setText(title);
	}

	public class AvailabilityTask extends AsyncTask<String, Void, String> {
		public boolean bIsAvailable;

		// public TextView txtView;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("bIsAvailable", "bIsAvailable Toggle To::> " + bIsAvailable);
			Availability availability = new Availability(getActivity(),
					bIsAvailable);
			String response = availability.IsAvailabilty();

			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					DriverSettingDetails driverSettings = new DriverSettingDetails(
							Util.mContext);
					driverSettings.retriveDriverSettings(Util.mContext);
					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				}

				return "error";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogJobFragment != null)
				dialogJobFragment.dismiss();

			if (result.equals("success")) {
				// Util.showToastMessage(getActivity(), "Status changed!",
				// Toast.LENGTH_LONG);
				// toggleTextViewBackground(txtView);
				AppValues.isAvailableSelected = bIsAvailable;
				if (Constants.isDebug) {
					if (AppValues.driverSettings != null)
						Log.e("JobsFragment",
								"onAvailableClicked--isAvailable:: "
										+ AppValues.driverSettings
												.getAvailability());
				}
				toggleAvailableStatus();
			} else {
				if (result.isEmpty()) {
					result = "Oops! Something went wrong.";
				}
				if (getActivity() != null)
					Util.showToastMessage(getActivity(), result,
							Toast.LENGTH_LONG);
			}

		}

	}
}
