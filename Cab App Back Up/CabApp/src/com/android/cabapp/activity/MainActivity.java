package com.android.cabapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.adapter.NavDrawerListAdapter;
import com.android.cabapp.datastruct.NavDrawerItem;
import com.android.cabapp.fragments.MyAccountFragment;
import com.android.cabapp.fragments.RootFragment;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.BackKeyPressedCallbackListener;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.flurry.android.FlurryAgent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.urbanairship.UAirship;

public class MainActivity extends FragmentActivity implements
		BackKeyPressedCallbackListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	public static Context mMainActivityContext;
	public static SlidingMenu slidingMenu;
	private ListView mDrawerList;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter navdrawerAdapter;
	// slide menu items
	private String[] navMenuTitles, navBadgeCount = { "", "", "", "", "", "",
			"" }; // 7
	private TypedArray navMenuIcons;

	FragmentManager mFragmentManager;

	int nLastSelected = -1;

	boolean bIsQuitDialogToBeShown = true;
	int nSlidingMenuItemPosition = -1;
	TextView textUserName, tvCreditNumbers, textAddCredits, tvPromoCode,
			tvYourPromoCode, tvShare;// textMyAccount
	RelativeLayout rllistTopbar;

	public static Handler creditsRefreshHandler;
	public static boolean ms_bIsInBackground = false;

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.d(getClass().getName(), "onNewIntent()-Push");

		if (!handlePush(intent)) {
			Fragment fragment = new com.android.cabapp.fragments.JobsFragment();
			replaceFragment(fragment, false);
			setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// MyJob count
		updateAndSetMenuItems(AppValues.nJobsCount);

		FlurryAgent.onStartSession(MainActivity.this, AppValues.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(MainActivity.this);
		ms_bIsInBackground = true;
		if (Constants.isDebug)
			Log.e("MainActivity", "MainActivity-onStop()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Util.mContext = mMainActivityContext = this;
		ms_bIsInBackground = false;
		if (Constants.isDebug)
			Log.e("MainActivity", "MainActivity-onResume()");

	}

	boolean handlePush(Intent intent) {

		boolean bIsChatMessage = false;
		if (intent.getExtras() != null) {
			if (intent.getExtras().containsKey("isChatMessage"))
				bIsChatMessage = intent.getExtras().getBoolean("isChatMessage");

			Log.e("MainActivity", "isChatMessage:: " + bIsChatMessage);
			if (bIsChatMessage) {

				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager nMgr = (NotificationManager) UAirship
						.shared().getApplicationContext().getSystemService(ns);
				nMgr.cancelAll();

				if (ChatActivity.mChatActivityContext != null) {
					// ChatActivity.finishMe();
					// ChatActivity.szJobID = "";
					ChatActivity.szJobID = intent.getExtras().getString(
							Constants.JOB_ID);
					Intent chatIntent = new Intent();
					chatIntent.setAction("NewMessage");
					LocalBroadcastManager.getInstance(MainActivity.this)
							.sendBroadcast(intent);
					return bIsChatMessage;
				}

				Intent chatIntent = new Intent(MainActivity.this,
						ChatActivity.class);
				ChatActivity.szJobID = intent.getExtras().getString(
						Constants.JOB_ID);
				chatIntent.putExtra(Constants.JOB_ID, intent.getExtras()
						.getString(Constants.JOB_ID));
				startActivity(chatIntent);
			}
		}
		return bIsChatMessage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// set the content view
		setContentView(R.layout.activity_main);
		// configure the SlidingMenu
		Util.mContext = mMainActivityContext = this;
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		// slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu);

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		textUserName = (TextView) findViewById(R.id.txtUserName);

		rllistTopbar = (RelativeLayout) findViewById(R.id.list_topbar);
		// textMyAccount = (TextView) findViewById(R.id.txtMyAccount);
		tvCreditNumbers = (TextView) findViewById(R.id.tvCreditNumbers);
		textAddCredits = (TextView) findViewById(R.id.tvAddCredits);
		tvCreditNumbers.setText(String.valueOf(Util
				.getNumberOfCredits(MainActivity.this)));

		tvPromoCode = (TextView) findViewById(R.id.tvPromoCode);
		tvYourPromoCode = (TextView) findViewById(R.id.tvYourPromoCode);
		tvShare = (TextView) findViewById(R.id.tvShare);
		String szYourPromoCodeText = " <font color=#a1a1a1>Your </font><font color=#fd6f01> Promo Code </font>";
		tvYourPromoCode.setText(Html.fromHtml(szYourPromoCodeText));
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cashBackClick();
			}
		});

		rllistTopbar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nSlidingMenuItemPosition = Constants.MY_ACCOUNT_FRAGMENT;
				slidingMenu.toggle();
			}
		});
		textAddCredits.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nSlidingMenuItemPosition = Constants.BUY_ADD_CREDITS_FRAGMENT;
				slidingMenu.toggle();
			}
		});

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navBadgeCount.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1), !navBadgeCount[i].equals(""),
					navBadgeCount[i]));
		}
		// adding nav drawer items to array

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		navdrawerAdapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(navdrawerAdapter);
		mDrawerList.setItemChecked(0, true);
		mDrawerList.setSelection(0);

		displayView(0);

		slidingMenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				// TODO Auto-generated method stub
				if (!Util.getIsOverlaySeen(mMainActivityContext, TAG)) {
					Intent intent = new Intent(MainActivity.this,
							MyJobsOverlayActivity.class);
					startActivity(intent);
				}

				if (AppValues.driverDetails != null) {

					if (Constants.isDebug)
						Log.e("MainActivity", "FirstName:"
								+ AppValues.driverDetails.getFirstname());
					textUserName.setText(AppValues.driverDetails.getFirstname());
				}

				Util.hideSoftKeyBoard(Util.mContext, textUserName);
				if (AppValues.driverSettings != null
						&& AppValues.driverSettings.getPromoCode() != null)
					tvPromoCode.setText(AppValues.driverSettings.getPromoCode());
			}
		});

		slidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
				if (nSlidingMenuItemPosition != -1) {
					displayView(nSlidingMenuItemPosition);
					nSlidingMenuItemPosition = -1;
				}
			}
		});

		creditsRefreshHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				if (message.what == 0) {
					if (tvCreditNumbers != null) {
						tvCreditNumbers.setText(String.valueOf(Util
								.getNumberOfCredits(MainActivity.this)));
					}
				}
			}
		};

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("appislaunchingfresh")) {
				if (!handlePush(getIntent())) {
					Fragment fragment = new com.android.cabapp.fragments.JobsFragment();
					replaceFragment(fragment, false);
					setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
				}
			}
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

		PackageManager pm = mMainActivityContext.getPackageManager();
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

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// Capture current selected List and if the click is a change of
			// position only then do displayView ELSE just toggle
			// display view for selected nav drawer item
			nSlidingMenuItemPosition = position;
			slidingMenu.toggle();
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		// if (bToggle)
		// slidingMenu.toggle();

		Fragment fragment = null;
		bIsQuitDialogToBeShown = true;
		switch (position) {
		case Constants.JOBS_FRAGMENT:
			fragment = new com.android.cabapp.fragments.JobsFragment();
			break;
		case Constants.INBOX_FRAGMENT:
			fragment = new com.android.cabapp.fragments.InboxFragment();
			break;
		// case Constants.MY_FILTERS_FRAGMENT:
		// fragment = new com.android.cabapp.fragments.MyFiltersFragment();
		// break;
		case Constants.MY_JOBS_FRAGMENT:
			fragment = new com.android.cabapp.fragments.MyJobsFragment();
			break;
		case Constants.CAB_PAY_FRAGMENT:
			fragment = new com.android.cabapp.fragments.CabPayFragment();
			break;
		case Constants.FARECALCULATOR_FRAGMENT:
			fragment = new com.android.cabapp.fragments.FareCalculatorFragment();
			break;
		case Constants.PROMOTIONS_FRAGMENT:
			fragment = new com.android.cabapp.fragments.PromotionsFragment();
			break;
		case Constants.CONTACT_SUPPORT_FRAGMENT:
			fragment = new com.android.cabapp.fragments.ContactSupportFragment();
			break;

		case Constants.MY_ACCOUNT_FRAGMENT:
			fragment = new com.android.cabapp.fragments.MyAccountFragment();
			break;

		case Constants.BUY_ADD_CREDITS_FRAGMENT:
			fragment = new com.android.cabapp.fragments.BuyAddCreditsFragment();
			break;

		default:
			break;
		}

		if (fragment != null && nLastSelected != position) {
			mFragmentManager = getSupportFragmentManager();

			// if (position == 0)
			// mFragmentManager.beginTransaction()
			// .replace(R.id.frame_container, fragment)
			// .addToBackStack("JobsFragment").commit();
			// else
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			nLastSelected = position;
			// setTitle(navMenuTitles[position]);
			// mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (slidingMenu.isMenuShowing()) {
			slidingMenu.toggle();
			return;
		}

		if (bIsQuitDialogToBeShown)
			showQuitDialog();
		else
			RootFragment.backkeyPressListener.onBackKeyPressedInFragments();
		return;
	}

	public void replaceFragment(Fragment fragment,
			boolean bCommitAllowingStateLoss) {
		String backStateName = fragment.getClass().getName();

		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopped = false;

		try {
			fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
		} catch (Exception e) {

		}

		if (!fragmentPopped) { // fragment not in back stack, create it.
			FragmentTransaction ft = manager.beginTransaction();
			ft.replace(R.id.frame_container, fragment);
			// ft.addToBackStack(backStateName);
			if (bCommitAllowingStateLoss)
				ft.commitAllowingStateLoss();
			else
				ft.commit();
		}

	}

	public void setSlidingMenuPosition(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		nLastSelected = position;
	}

	void showQuitDialog() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						finish();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}

	public void setDontShowQuitDialog() {
		bIsQuitDialogToBeShown = false;
	}

	public void updateAndSetMenuItems(int nJobsCount) {
		if (navdrawerAdapter != null)
			navdrawerAdapter.updateItems(nJobsCount);
	}

	public static void finishActivity() {
		if (mMainActivityContext != null)
			((Activity) mMainActivityContext).finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Util.mContext = mMainActivityContext = null;
	}

	@Override
	public void onBackKeyPressedInFragments() {
		// TODO Auto-generated method stub

	}

}
