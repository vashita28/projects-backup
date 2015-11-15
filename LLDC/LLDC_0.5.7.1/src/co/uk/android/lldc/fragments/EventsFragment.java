package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.EventDetailsActivity;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.EventHighLightPagerAdapter;
import co.uk.android.lldc.adapters.EventListAdapter;
import co.uk.android.lldc.adapters.VenueEventListAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.models.ServerModel;

import com.viewpagerindicator.UnderlinePageIndicator;

public class EventsFragment extends Fragment {

	ViewPager viewPager;
	Context context;
	ListView events_list;

	EventListAdapter listAdapter = null;
	VenueEventListAdapter venuelistAdapter = null;
	EventHighLightPagerAdapter highlightListAdapter = null;

	ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
	ArrayList<ServerModel> eventHighliteList = null;
	RelativeLayout highLiteList;

	View headerView;
	
	TextView tvNoEvent;

	UnderlinePageIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container,
				false);
		viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
		events_list = (ListView) rootView.findViewById(R.id.events_list);
		highLiteList = (RelativeLayout) rootView
				.findViewById(R.id.highLiteList);

		mIndicator = (UnderlinePageIndicator) rootView
				.findViewById(R.id.indicator);

		tvNoEvent = (TextView) rootView.findViewById(R.id.tvNoEvent);
		
		headerView = (View) rootView.findViewById(R.id.viewHeader);

		events_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LLDCApplication.selectedModel = eventList.get(arg2);
				Intent intent = new Intent(getActivity(),
						EventDetailsActivity.class);
				intent.putExtra("PAGETITLE", "Events");
				getActivity().startActivity(intent);
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
				eventList = LLDCApplication.DBHelper
						.getEventData(LLDCDataBaseHelper.COLUMN_VENUE_ID
								+ " ='"
								+ LLDCApplication.selectedModel.getVenueId()
								+ "'");
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
				highlightListAdapter = new EventHighLightPagerAdapter(
						getActivity(), eventHighliteList, viewPager);
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
			
			if(eventList.size() == 0){
				tvNoEvent.setVisibility(View.VISIBLE);
				if ((getActivity().getClass().getName().toString()
						.contains("VenueActivity"))) {
					tvNoEvent.setText("No events found for this venue");
				}else{
					tvNoEvent.setText("No events available");
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
