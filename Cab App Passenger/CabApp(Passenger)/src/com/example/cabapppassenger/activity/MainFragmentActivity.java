package com.example.cabapppassenger.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.adapter.NavDrawerListAdapter;
import com.example.cabapppassenger.fragments.MyAccountParentFragment;
import com.example.cabapppassenger.model.NavDrawerItem;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetUserDetailsTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

public class MainFragmentActivity extends FragmentActivity {
	public static SlidingMenu slidingMenu;
	public static ListView mDrawerList;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	// slide menu items
	private String[] navMenuTitles;
	TextView tv_username, tv_preferred_method;
	private TypedArray navMenuIcons;
	static Context mContext;
	Editor editor;
	View view_account_details;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	FragmentManager mFragmentManager;
	OnCustomBackPressedListener mBackPressListener;

	public int nLastSelected = -1;
	int nSlidingMenuItemPosition = -1;

	private static MainFragmentActivity instance = null;
	public static boolean isVisible = false;

	public static Handler mTextCountUpdateHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// configure the SlidingMenu
		mContext = this;
		this.instance = this;
		isVisible = true;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		Log.i("Email", shared_pref.getString("Email", ""));
		Log.i("AccessToken", shared_pref.getString("AccessToken", ""));

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu);
		tv_username = (TextView) slidingMenu.findViewById(R.id.txtUserName);
		tv_preferred_method = (TextView) slidingMenu
				.findViewById(R.id.tv_preferred_method);

		// if (shared_pref.getString("Email", "") == null
		// || shared_pref.getString("Email", "").matches("null")
		// || shared_pref.getString("Email", "").matches("")) {
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			GetUserDetailsTask task = new GetUserDetailsTask(mContext);
			task.textview_username = tv_username;
			task.textview_payingmethod = tv_preferred_method;
			task.execute("");

		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
		// }
		view_account_details = (View) slidingMenu
				.findViewById(R.id.view_acount_details);
		view_account_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MyAccountParentFragment edit = new MyAccountParentFragment();
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, edit,
						"myAccountParentFragment");
				ft.commit();
				slidingMenu.toggle();
				mDrawerList.setItemChecked(-1, true);
				mDrawerList.setSelection(-1);

				nLastSelected = -1;
				nSlidingMenuItemPosition = -1;
			}
		});

		if (shared_pref.getString("FirstName", "") != null
				&& !shared_pref.getString("FirstName", "").matches("null")
				&& shared_pref.getString("LastName", "") != null
				&& !shared_pref.getString("LastName", "").matches("null")) {

			tv_username.setText(shared_pref.getString("FirstName", "") + " "
					+ shared_pref.getString("LastName", ""));
		}

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {
			if (i == 2) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1), true));
			} else {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1), false));
			}
		}
		// adding nav drawer items to array

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setItemChecked(0, true);
		mDrawerList.setSelection(0);

		slidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
				Log.e("on close", "Sliding menu on close listner");
				if (nSlidingMenuItemPosition != -1) {
					displayView(nSlidingMenuItemPosition);
					nSlidingMenuItemPosition = -1;

				}
			}
		});
		displayView(0);

		mTextCountUpdateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (adapter != null) {
					adapter.updateTextCount((String) msg.obj);
				}
			}
		};

	}

	public void setCustomBackPressListener(
			OnCustomBackPressedListener eventListener) {
		mBackPressListener = eventListener;
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
			Log.e("on click", "Sliding menu on click listner");
			hideKeybord(view);
			nSlidingMenuItemPosition = position;
			slidingMenu.toggle();
			if (nSlidingMenuItemPosition != -1) {
				displayView(nSlidingMenuItemPosition);
				nSlidingMenuItemPosition = -1;
			}
		}
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Log.d("", "Last Position =" + nLastSelected + " Position " + position);
		Fragment fragment = null;

		switch (position) {
		case Constant.HAILNOW_FRAGMENT:

			fragment = new com.example.cabapppassenger.fragments.HailNowParentFragment();
			break;
		case Constant.PRE_BOOK_FRAGMENT:

			fragment = new com.example.cabapppassenger.fragments.PreBookParentFragment();
			break;
		case Constant.MY_BOOKINGS_FRAGMENT:

			fragment = new com.example.cabapppassenger.fragments.MyBookingsParentFragment();
			break;
		case Constant.FAVOURITES_FRAGMENT:
			// {
			// Constant.flag_fromslider_favourites = true;
			fragment = new com.example.cabapppassenger.fragments.FavoritesParentFragment();
			// }
			break;

		case Constant.CabApp_Location_FRAGMENT:
			// {
			// Constant.flag_fromslider_favourites = true;
			fragment = new com.example.cabapppassenger.fragments.CabApp_Location_ParentFragment();
			// }
			break;
		case Constant.MORE_FRAGMENT:

			fragment = new com.example.cabapppassenger.fragments.MoreParentFragment();
			break;

		default:
			break;
		}

		if (fragment != null && nLastSelected != position) {
			mFragmentManager = this.getSupportFragmentManager();

			// if (position == 0)
			// mFragmentManager.beginTransaction()
			// .replace(R.id.frame_container, fragment)
			// .addToBackStack("JobsFragment").commit();
			// else
			FragmentTransaction fragtrans = mFragmentManager.beginTransaction();

			if (position == 0) {
				// fragtrans.addToBackStack(null);

				fragtrans.replace(R.id.frame_container, fragment,
						"hailNowParentFragment");
			}

			else if (position == 1) {
				fragtrans.replace(R.id.frame_container, fragment,
						"preBookParentFragment");

			} else if (position == 4) {
				fragtrans.replace(R.id.frame_container, fragment,
						"moreParentFragment");
			} else
				fragtrans.replace(R.id.frame_container, fragment);

			fragtrans.commit();

			// update selected item and title, then close the drawer

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			nLastSelected = position;
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isVisible = true;
		this.instance = this;

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mBackPressListener != null)
			mBackPressListener.onCustomBackPressed();

		return;
	}

	/*
	 * public void replaceFragment(Fragment fragment) { String backStateName =
	 * fragment.getClass().getName();
	 * 
	 * FragmentManager manager = getSupportFragmentManager(); boolean
	 * fragmentPopped = false;
	 * 
	 * try { fragmentPopped = manager.popBackStackImmediate(backStateName, 0); }
	 * catch (Exception e) {
	 * 
	 * }
	 * 
	 * if (!fragmentPopped) { // fragment not in back stack, create it.
	 * FragmentTransaction ft = manager.beginTransaction();
	 * ft.replace(R.id.frame_container, fragment); //
	 * ft.addToBackStack(backStateName); ft.commit(); }
	 * 
	 * }
	 */

	public void setSlidingMenuPosition(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		nLastSelected = position;
	}

	public void showQuitDialog() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				MainFragmentActivity.this)
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

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public void onsetdata() {
		if (shared_pref.getString("FirstName", "") != null
				&& !shared_pref.getString("FirstName", "").matches("null")
				&& shared_pref.getString("LastName", "") != null
				&& !shared_pref.getString("LastName", "").matches("null")) {

			tv_username.setText(shared_pref.getString("FirstName", "") + " "
					+ shared_pref.getString("LastName", ""));
		}
		if (shared_pref.getString("PayingMethod", "") != null
				&& !shared_pref.getString("PayingMethod", "").matches("null")) {

			if (shared_pref.getString("PayingMethod", "").matches("both")
					|| shared_pref.getString("PayingMethod", "")
							.matches("Card")
					|| shared_pref.getString("PayingMethod", "")
							.matches("card")
					|| shared_pref.getString("PayingMethod", "").matches(
							"cash and card"))

				tv_preferred_method.setText("Card");

			else if (shared_pref.getString("PayingMethod", "").equals("Cash")
					|| shared_pref.getString("PayingMethod", "").equals("cash")) {
				tv_preferred_method.setText("Cash");
			}
		} else if (shared_pref.getString("PayingMethod", "").matches("")) {
			tv_preferred_method.setText("Cash");

		}

	}

	@Override
	protected void onPause() {
		isVisible = false;
		super.onPause();
	}

	public static MainFragmentActivity getInstance() {
		return instance;
	}

	public void refresh_menu(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);

		nLastSelected = position;
		nSlidingMenuItemPosition = position;
	}

	public interface OnCustomBackPressedListener {
		public void onCustomBackPressed();
	}

}
