package co.uk.android.lldc.tablet;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.fragments.SearchMapFragment;
import co.uk.android.lldc.fragments.Search_MapFragment;
import co.uk.android.lldc.fragments.tablet.Search_EventOverviewFragment;
import co.uk.android.lldc.fragments.tablet.Search_ExploreDetailsFragment;
import co.uk.android.lldc.fragments.tablet.Search_VenueFragmentTablet;
import co.uk.android.lldc.utils.Constants;

public class SearchMapActivity extends FragmentActivity {
	private static final String TAG = SearchMapActivity.class.getSimpleName();

	static Context mContext;

	public TextView tvPageTitle;
	RelativeLayout rlBackBtn;

	FragmentManager mFragmentManager;
	FragmentTransaction fragmentTransaction;

	// public boolean bIsFooterOpen = false;

	String mPageTitle = "";

	String pageSubTitle = "";
	String selectedId = "";

	LocationManager locationManager;
	String provider;
	ImageView ivSerach, ivNavigation, ivBack;

	Bundle bundle = new Bundle();
	public static Stack<Fragment> fragmentStack;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_map_navigation_tablet);

		mContext = this;
		bundle = getIntent().getExtras();
		fragmentStack = new Stack<Fragment>();

		tvPageTitle = (TextView) findViewById(R.id.tvPageTitle);
		rlBackBtn = (RelativeLayout) findViewById(R.id.rlBackBtn);
		ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
		ivBack = (ImageView) findViewById(R.id.ivBack);

		rlBackBtn.setOnClickListener(new OnitemClickListener());
		ivNavigation.setOnClickListener(new OnitemClickListener());

		if (bundle != null && bundle.containsKey("tagarr")) {
			try {

				String[] tagarr = bundle.getStringArray("tagarr");

				if (LLDCApplication.EVENT == Integer.parseInt(tagarr[1]))
					LLDCApplication.selectedModel = LLDCApplication.DBHelper
							.getSingleData(tagarr[0],
									LLDCDataBaseHelper.TABLE_EVENTS);
				else if (LLDCApplication.VENUE == Integer.parseInt(tagarr[1]))
					LLDCApplication.selectedModel = LLDCApplication.DBHelper
							.getSingleData(tagarr[0],
									LLDCDataBaseHelper.TABLE_VENUES);
				else if (LLDCApplication.FACILITIES == Integer
						.parseInt(tagarr[1]))
					LLDCApplication.selectedModel = LLDCApplication.DBHelper
							.getSingleData(tagarr[0],
									LLDCDataBaseHelper.TABLE_FACILITIES);
				else if (LLDCApplication.TRAILS == Integer.parseInt(tagarr[1]))
					LLDCApplication.selectedModel = LLDCApplication.DBHelper
							.getSingleData(tagarr[0],
									LLDCDataBaseHelper.TABLE_TRAILS);

				if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.EVENT) {
					Log.e(TAG, TAG + " EVENT ");
					displaySearchResultFragment(1);

				} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.VENUE) {
					Log.e(TAG, TAG + " VENUE ");

					displaySearchResultFragment(2);

				} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.FACILITIES) {
					Log.e(TAG, TAG + " FACILITIES ");
					displaySearchResultFragment(3);

				} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
					Log.e(TAG, TAG + " TRAILS ");
					Constants.IS_TRAILS = true;
					displaySearchResultFragment(4);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (getIntent().getExtras().containsKey("PAGETITLE")) {
			mPageTitle = getIntent().getExtras().getString("PAGETITLE");
			tvPageTitle.setText("Map");

			if (mPageTitle.equals("Search")) {
				mPageTitle = "Map";
				pageSubTitle = getIntent().getExtras()
						.getString("PAGESUBTITLE");
				selectedId = getIntent().getExtras().getString("SELID");
				if (pageSubTitle.equals("SHOWME")) {
					displayView(2);
				} else {
					displayView(1);
				}
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, TAG);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		try {
			String mPageTitle = intent.getExtras().getString("PAGETITLE");
			if (mPageTitle.equals("Search")) {
				mPageTitle = "Map";
				pageSubTitle = intent.getExtras().getString("PAGESUBTITLE");
				selectedId = intent.getExtras().getString("SELID");
				if (pageSubTitle.equals("SHOWME")) {
					displayView(2);
				} else {
					displayView(1);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displaySearchResultFragment(int position) {
		if (LLDCApplication.isInsideThePark)
			ivNavigation.setVisibility(View.VISIBLE);
		else
			ivNavigation.setVisibility(View.GONE);

		Fragment fragment = null;
		FragmentManager mFragmentManager = null;
		Bundle bundle = new Bundle();
		mFragmentManager = getSupportFragmentManager();

		switch (position) {
		case 1:
			fragment = (Fragment) new Search_EventOverviewFragment();

			if (fragment != null) {

				fragmentStack.push(fragment);
				bundle.putString("PAGETITLE", "Events");
				fragment.setArguments(bundle);

				mFragmentManager = getSupportFragmentManager();
				fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction
						.replace(R.id.frame_container, fragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.commit();
				// mFragmentManager
				// .beginTransaction()
				// .replace(R.id.frame_container, fragment)
				// .setTransition(
				// FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				// .commit();
			}

			break;

		case 2:
			fragment = (Fragment) new Search_VenueFragmentTablet();
			if (fragment != null) {

				fragmentStack.push(fragment);
				fragment.setArguments(bundle);

				mFragmentManager = getSupportFragmentManager();
				fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction
						.replace(R.id.frame_container, fragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.commit();

				// mFragmentManager = getSupportFragmentManager();
				// mFragmentManager
				// .beginTransaction()
				// .replace(R.id.frame_container, fragment)
				// .setTransition(
				// FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				// .commit();
			}
			break;

		case 3:
			fragment = (Fragment) new Search_ExploreDetailsFragment();

			if (fragment != null) {
				fragmentStack.push(fragment);
				bundle.putString("PAGETITLE", "Facilities");
				fragment.setArguments(bundle);

				mFragmentManager = getSupportFragmentManager();
				fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction
						.replace(R.id.frame_container, fragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.commit();

				// mFragmentManager = getSupportFragmentManager();
				// mFragmentManager
				// .beginTransaction()
				// .replace(R.id.frame_container, fragment)
				// .setTransition(
				// FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				// .commit();
			}
			break;

		case 4:
			fragment = (Fragment) new Search_ExploreDetailsFragment();

			if (fragment != null) {
				fragmentStack.push(fragment);
				bundle.putString("PAGETITLE", "Trails");
				fragment.setArguments(bundle);

				mFragmentManager = getSupportFragmentManager();
				fragmentTransaction = mFragmentManager.beginTransaction();
				fragmentTransaction
						.replace(R.id.frame_container, fragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.commit();

				// mFragmentManager = getSupportFragmentManager();
				// mFragmentManager
				// .beginTransaction()
				// .replace(R.id.frame_container, fragment)
				// .setTransition(
				// FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				// .commit();
			}
			break;
		}

	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = (Fragment) new SearchMapFragment(pageSubTitle,
					selectedId);
			break;
		case 2:
			fragment = (Fragment) new Search_MapFragment(pageSubTitle,
					selectedId);
			break;
		}

		if (fragment != null) {
			/* Adding fragment to stack */
			// fragmentStack.lastElement().onPause();
			fragmentStack.push(fragment);

			mFragmentManager = getSupportFragmentManager();
			fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction
					.replace(R.id.frame_container, fragment)
					// mFragmentManager.beginTransaction()
					// .replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
		}

	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlBackBtn:
				if (fragmentStack.size() > 0) {// mFragmentManager != null &&
					// Toast.makeText(SearchMapActivity.this, "back pressed if",
					// Toast.LENGTH_LONG).show();
					onBackPressed();
				} else {
					// Toast.makeText(SearchMapActivity.this,
					// "back pressed finish", Toast.LENGTH_LONG).show();
					finish();
				}
				break;

			case R.id.ivNavigation:
				Intent intent = new Intent(SearchMapActivity.this,
						MapNavigationActivity.class);
				if (Constants.IS_TRAILS)
					intent.putExtra("PAGETITLE", "Trails");
				else
					intent.putExtra("PAGETITLE", "Map");
				try {
					LLDCApplication
							.showSimpleProgressDialog(SearchMapActivity.this);
					startActivity(intent);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

}
