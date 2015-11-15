package co.uk.android.lldc.fragments.tablet;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.EventHighLightPagerAdapter;
import co.uk.android.lldc.adapters.EventListAdapter;
import co.uk.android.lldc.adapters.VenueEventListAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.fragments.Search_MapFragment;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.SearchMapActivity;
import co.uk.android.lldc.utils.Constants;

import com.viewpagerindicator.UnderlinePageIndicator;

public class Search_EventsFragment extends Fragment {
	private static final String TAG = Search_EventsFragment.class
			.getSimpleName();

	ViewPager viewPager;
	Context context;
	GridView events_list;

	EventListAdapter listAdapter = null;
	VenueEventListAdapter venuelistAdapter = null;
	EventHighLightPagerAdapter highlightListAdapter = null;

	ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
	ArrayList<ServerModel> eventHighliteList = null;
	RelativeLayout highLiteList, screen;

	// View headerView;

	TextView tvNoEvent, tvWhatsOnThePark;

	UnderlinePageIndicator mIndicator;
	LinearLayout llIndicator;
	Bundle bundle = new Bundle();
	boolean bIsFromExplore = false;
	String showDataOf = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;

		Log.e(TAG, "Device is Tablet..." + TAG);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_events_tablet, container,
				false);

		bundle = getArguments();
		if (bundle != null
				&& bundle.containsKey(Constants.IS_COMING_FROM_EXPLORE)
				&& bundle.getBoolean(Constants.IS_COMING_FROM_EXPLORE))
			bIsFromExplore = bundle
					.getBoolean(Constants.IS_COMING_FROM_EXPLORE);

		if (bundle != null && bundle.containsKey(Constants.IS_FOR_TODAY_EVENT)) {
			showDataOf = bundle.getString(Constants.IS_FOR_TODAY_EVENT);
			if (showDataOf.equals("TodaysEventFragment")) {
				bundle.putString("PAGETITLE", "Events");
				// ((HomeActivityTablet) getActivity()).showBackButton();
			}
		}

		screen = (RelativeLayout) rootView.findViewById(R.id.screen);
		viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
		events_list = (GridView) rootView.findViewById(R.id.events_list);
		highLiteList = (RelativeLayout) rootView
				.findViewById(R.id.highLiteList);
		mIndicator = (UnderlinePageIndicator) rootView
				.findViewById(R.id.indicator);
		llIndicator = (LinearLayout) rootView.findViewById(R.id.llIndicator);
		tvNoEvent = (TextView) rootView.findViewById(R.id.tvNoEvent);

		tvWhatsOnThePark = (TextView) rootView
				.findViewById(R.id.tvWhatsOnThePark);

		events_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LLDCApplication.selectedModel = eventList.get(arg2);
				/* For checking social */
				LLDCApplication.selectedModel.setSocialFlag("1");
				LLDCApplication.selectedModel.setSocialHandle("Park");
				callEventDetailsFragment();
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub

			}
		});

		// ((SearchMapActivity) getActivity()).rlBackBtn
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (getFragmentManager() != null
		// && getFragmentManager()
		// .getBackStackEntryCount() > 0) {
		// getFragmentManager().popBackStack();
		// ((SearchMapActivity) getActivity()).tvPageTitle
		// .setText("Map");
		// } else if (getChildFragmentManager() != null &&
		// getChildFragmentManager().getBackStackEntryCount()>0) {
		//
		// getChildFragmentManager().popBackStack();
		//
		// } else {
		// SearchMapActivity.finishActivity();
		// }
		// }
		// });

		getDataFromDB();
		LLDCApplication.removeSimpleProgressDialog();
		return rootView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, TAG);
	}

	@SuppressLint("NewApi")
	public void getDataFromDB() {
		try {
			eventList.clear();
			if (bIsFromExplore) {
				tvWhatsOnThePark.setVisibility(View.GONE);
			} else {
				tvWhatsOnThePark.setVisibility(View.VISIBLE);
			}
			if (showDataOf.contains("TodaysEventFragment")) {
				tvWhatsOnThePark.setVisibility(View.GONE);
				((HomeActivityTablet) getActivity()).tvHeaderTitle
						.setText("Today's Events");
				events_list.setPadding(0, 30, 0, 0);
				eventList = LLDCApplication.DBHelper.getTodaysEventData();
			} else if (showDataOf.contains("VenueActivity")) {
				eventList = LLDCApplication.DBHelper
						.getEventData(LLDCDataBaseHelper.COLUMN_VENUE_ID
								+ " ='"
								+ LLDCApplication.selectedModel.getVenueId()
								+ "'");
			} else {
				// ((HomeActivityTablet) getActivity()).tvHeaderTitle
				// .setText("Events");
				eventList = LLDCApplication.DBHelper.getEventData("");
			}

			if (showDataOf.contains("VenueActivity")) {
				Log.e(TAG, "Setting VenuelistAdapter");
				venuelistAdapter = new VenueEventListAdapter(getActivity(),
						eventList);
				events_list.setAdapter(venuelistAdapter);
			} else {
				listAdapter = new EventListAdapter(getActivity(), eventList);
				events_list.setAdapter(listAdapter);
			}

			eventHighliteList = new ArrayList<ServerModel>();

			if (!showDataOf.contains("TodaysEventFragment")
					&& !showDataOf.contains("VenueActivity")) {

				for (int i = 0; i < eventList.size(); i++) {
					if (eventList.get(i).getFlag().toString().trim()
							.equals(LLDCApplication.FLAG_HIGHLIGHT)) {

						eventHighliteList.add(eventList.get(i));
					}
				}
			}

			if (eventHighliteList.size() > 0) {
				llIndicator.setVisibility(View.VISIBLE);
				// highlightListAdapter = new EventHighLightPagerAdapter(
				// EventsFragment.this, getActivity(), eventHighliteList,
				// viewPager);
				// viewPager.setAdapter(highlightListAdapter);
				highlightListAdapter.notifyDataSetChanged();
				highLiteList.setVisibility(View.VISIBLE);

				// ViewPager Indicator
				mIndicator.setFades(false);
				mIndicator.setViewPager(viewPager);
				mIndicator.setSelectedColor(getResources().getColor(
						R.color.highlight_thumb));
			} else {
				llIndicator.setVisibility(View.GONE);
				highLiteList.setVisibility(View.GONE);
			}

			if (eventList.size() == 0) {
				tvNoEvent.setVisibility(View.VISIBLE);
				tvWhatsOnThePark.setVisibility(View.GONE);
				if (showDataOf.contains("VenueActivity")) {
					tvNoEvent.setText("No events found for this venue");
				} else {
					tvNoEvent.setText("No events available");
				}
			}
			// if(eventHighliteList.size())
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void callEventDetailsFragment() {
		if (showDataOf != null && !showDataOf.isEmpty()
				&& showDataOf.equals("TodaysEventFragment")) {
			bundle.putString("PAGETITLE", "Events");
		}
		// Search_VenueFragmentTablet.pager.setVisibility(View.GONE);
		// Search_VenueFragmentTablet.tabHost.setVisibility(View.GONE);
		// Search_VenueFragmentTablet.frameContainerVenue
		// .setVisibility(View.VISIBLE);

		Fragment fragment = null;
		FragmentManager mFragmentManager;
		FragmentTransaction fragmentTransaction;

		/* Adding fragment to stack */
		// SearchMapActivity.fragmentStack.lastElement().onPause();
		SearchMapActivity.fragmentStack.push(fragment);

		fragment = new Search_EventOverviewFragment();
		fragment.setArguments(bundle);
		mFragmentManager = getParentFragment().getFragmentManager();// getFragmentManager();
		fragmentTransaction = mFragmentManager.beginTransaction();
		// mFragmentManager
		// .beginTransaction()
		fragmentTransaction
				.addToBackStack(Search_EventsFragment.class.getName())
				.add(R.id.frame_container, fragment,
						Search_EventOverviewFragment.class.getName())
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				// .commitAllowingStateLoss();
				.commit();

	}
}
