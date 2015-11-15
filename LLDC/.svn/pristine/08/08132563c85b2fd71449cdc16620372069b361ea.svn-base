package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import co.uk.android.lldc.EventDetailsActivity;
import co.uk.android.lldc.HomeActivityTablet;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.EventHighLightPagerAdapter;
import co.uk.android.lldc.adapters.EventListAdapter;
import co.uk.android.lldc.adapters.VenueEventListAdapter;
import co.uk.android.lldc.fragments.tablet.EventDetailsFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.util.Util;

import com.viewpagerindicator.UnderlinePageIndicator;

public class EventsFragment extends Fragment {
	private static final String TAG = EventsFragment.class.getSimpleName();

	ViewPager viewPager;
	Context context;
	GridView events_list;

	EventListAdapter listAdapter = null;
	VenueEventListAdapter venuelistAdapter = null;
	EventHighLightPagerAdapter highlightListAdapter = null;

	ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
	ArrayList<ServerModel> eventHighliteList = null;
	RelativeLayout highLiteList;

	View headerView;
	UnderlinePageIndicator mIndicator;

	public static boolean bIsTablet = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(R.layout.fragment_events_tablet,
					container, false);
			bIsTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(R.layout.fragment_events, container,
					false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
		}

		viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
		events_list = (GridView) rootView.findViewById(R.id.events_list);
		highLiteList = (RelativeLayout) rootView
				.findViewById(R.id.highLiteList);

		mIndicator = (UnderlinePageIndicator) rootView
				.findViewById(R.id.indicator);

		headerView = (View) rootView.findViewById(R.id.viewHeader);

		events_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LLDCApplication.selectedModel = eventList.get(arg2);
				if (!bIsTablet) {
					Intent intent = new Intent(getActivity(),
							EventDetailsActivity.class);
					intent.putExtra("PAGETITLE", "Events");
					getActivity().startActivity(intent);
				} else {
					callEventDetailsFragment();
				}
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

		getDataFromDB();

		return rootView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("NewApi")
	public void getDataFromDB() {
		try {
			eventList.clear();
			if ((getActivity().getClass().getName().toString()
					.contains("TodaysEventActivity"))) {
				eventList = LLDCApplication.DBHelper.getTodaysEventData();
			} else if ((getActivity().getClass().getName().toString()
					.contains("VenueActivity"))) {
				headerView.setVisibility(View.GONE);
				eventList = LLDCApplication.DBHelper.getEventData("");
			} else {
				eventList = LLDCApplication.DBHelper.getEventData("");
			}

			if ((getActivity().getClass().getName().toString()
					.contains("VenueActivity"))) {
				venuelistAdapter = new VenueEventListAdapter(getActivity(),
						eventList);
				events_list.setAdapter(venuelistAdapter);
			} else {
				listAdapter = new EventListAdapter(getActivity(), eventList);
				events_list.setAdapter(listAdapter);
			}

			eventHighliteList = new ArrayList<ServerModel>();

			if (!(getActivity().getClass().getName().toString()
					.contains("TodaysEventActivity"))) {
				for (int i = 0; i < eventList.size(); i++) {
					if (eventList.get(i).getFlag().toString().trim()
							.equals(LLDCApplication.FLAG_HIGHLIGHT)) {
						eventHighliteList.add(eventList.get(i));
					}
				}
			}

			if (eventHighliteList.size() > 0) {
				if (!bIsTablet) {
					highlightListAdapter = new EventHighLightPagerAdapter(
							getActivity(), eventHighliteList, viewPager);
				} else {
					highlightListAdapter = new EventHighLightPagerAdapter(
							EventsFragment.this, getActivity(),
							eventHighliteList, viewPager);
				}
				viewPager.setAdapter(highlightListAdapter);
				highlightListAdapter.notifyDataSetChanged();
				highLiteList.setVisibility(View.VISIBLE);

				// ViewPager Indicator
				mIndicator.setFades(false);
				mIndicator.setViewPager(viewPager);
				mIndicator.setSelectedColor(getResources().getColor(
						R.color.highlight_thumb));

			} else {
				highLiteList.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void callEventDetailsFragment() {
		Fragment fragment = null;
		FragmentManager mFragmentManager;
		fragment = new EventDetailsFragmentTablet();
		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					.addToBackStack(EventsFragment.class.getName())
					.replace(R.id.frame_container_parent, fragment,
							EventDetailsFragmentTablet.class.getName())
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}
	}
}
