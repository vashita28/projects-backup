package co.uk.android.lldc.fragments.tablet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.fragments.VenueMediaFragment;
import co.uk.android.lldc.fragments.VenueOverviewFragment;
import co.uk.android.lldc.fragments.WelcomeFragment;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.SearchMapActivity;
import co.uk.android.lldc.utils.Constants;

public class Search_VenueFragmentTablet extends Fragment {
	private static final String TAG = Search_VenueFragmentTablet.class
			.getSimpleName();

	FragmentManager mFragmentManager;
	public static FrameLayout frameContainerVenue;
	RelativeLayout screen;

	/** For tab host **/
	RelativeLayout relvenueoverview, relvenueevents, relvenuemedia;
	TextView txtvenueoverview, txtvenueevents, txtvenuemedia;
	View viewvenueoverview, viewvenueevents, viewvenuemedia;

	public static ViewPager pager;
	public static View tabHost;
	Bundle bundle = new Bundle();

	private static String[] CONTENT = new String[] { "Overview", "Events",
			"Media" };

	@SuppressLint({ "ResourceAsColor", "HandlerLeak" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.activity_venue_search_tablet,
				container, false);
		bundle = getArguments();

		/* Search should only be wisible is user is inside park */

		/** For tab host **/
		relvenueoverview = (RelativeLayout) rootView
				.findViewById(R.id.relvenueoverview);
		relvenueevents = (RelativeLayout) rootView
				.findViewById(R.id.relvenueevents);
		relvenuemedia = (RelativeLayout) rootView
				.findViewById(R.id.relvenuemedia);
		txtvenueoverview = (TextView) rootView
				.findViewById(R.id.txtvenueoverview);
		txtvenueevents = (TextView) rootView.findViewById(R.id.txtvenueevents);
		txtvenuemedia = (TextView) rootView.findViewById(R.id.txtvenuemedia);
		viewvenueoverview = (View) rootView
				.findViewById(R.id.viewvenueoverview);
		viewvenueevents = (View) rootView.findViewById(R.id.viewvenueevents);
		viewvenuemedia = (View) rootView.findViewById(R.id.viewvenuemedia);
		relvenueoverview.setOnClickListener(new onTabChangeListener());
		relvenueevents.setOnClickListener(new onTabChangeListener());
		relvenuemedia.setOnClickListener(new onTabChangeListener());

		pager = (ViewPager) rootView.findViewById(R.id.pager);
		tabHost = (View) rootView.findViewById(R.id.tabHost);
		frameContainerVenue = (FrameLayout) rootView
				.findViewById(R.id.frame_container_venue);
		screen = (RelativeLayout) rootView.findViewById(R.id.screen);

		((SearchMapActivity) getActivity()).tvPageTitle.setText("Venues");
		// ((SearchMapActivity) getActivity()).rlBackBtn
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Log.e(TAG, TAG + " BackButtonPressed");
		// if (getFragmentManager() != null
		// && getFragmentManager()
		// .getBackStackEntryCount() > 0) {
		// getFragmentManager().popBackStack();
		// ((SearchMapActivity) getActivity()).tvPageTitle
		// .setText("Map");
		// } else {
		// SearchMapActivity.finishActivity();
		// }
		// }
		// });

		// Added logic to hide unwanted tab
		CONTENT = new String[] { "Overview" };
		tabHost.setVisibility(View.GONE);

		if (LLDCApplication.selectedModel.getMediaList().size() > 0
				&& LLDCApplication.selectedModel.getEventCount() != 0) {
			tabHost.setVisibility(View.VISIBLE);
			CONTENT = new String[] { "Overview", "Events", "Media" };
		} else if (LLDCApplication.selectedModel.getMediaList().size() > 0
				&& LLDCApplication.selectedModel.getEventCount() == 0) {
			tabHost.setVisibility(View.VISIBLE);
			CONTENT = new String[] { "Overview", "Media" };
			relvenueevents.setVisibility(View.GONE);
		} else if (LLDCApplication.selectedModel.getEventCount() != 0
				&& LLDCApplication.selectedModel.getMediaList().size() == 0) {
			tabHost.setVisibility(View.VISIBLE);
			CONTENT = new String[] { "Overview", "Events" };
			relvenuemedia.setVisibility(View.GONE);
		}

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 0) {
					txtvenueoverview.setTextColor(Color.parseColor("#41C0F0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.VISIBLE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.GONE);
				} else if (arg0 == 1) {
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#41C0F0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.VISIBLE);
					viewvenuemedia.setVisibility(View.GONE);
				} else {
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#41C0F0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(
				getChildFragmentManager());
		pager.setAdapter(adapter);

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, TAG);
		bundle = getArguments();
	}

	class onTabChangeListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Fragment f = null;
			switch (v.getId()) {
			case R.id.relvenueoverview:
				mFragmentManager = getChildFragmentManager();
				f = mFragmentManager
						.findFragmentById(R.id.frame_container_venue);
				if (f instanceof Search_VenueOverviewFragment) {

				} else {
					txtvenueoverview.setTextColor(Color.parseColor("#41C0F0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.VISIBLE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.GONE);
				}
				pager.setCurrentItem(0);
				break;
			case R.id.relvenueevents:
				mFragmentManager = getChildFragmentManager();
				f = mFragmentManager
						.findFragmentById(R.id.frame_container_venue);
				if (f instanceof Search_EventsFragment) {// EventsFragment

				} else {
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#41C0F0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.VISIBLE);
					viewvenuemedia.setVisibility(View.GONE);
				}
				pager.setCurrentItem(1);
				break;
			case R.id.relvenuemedia:
				mFragmentManager = getChildFragmentManager();
				f = mFragmentManager
						.findFragmentById(R.id.frame_container_venue);
				if (f instanceof WelcomeFragment) {

				} else {
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#41C0F0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.VISIBLE);
				}
				pager.setCurrentItem(2);
				break;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		((SearchMapActivity) getActivity()).tvPageTitle.setText("Map");
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			String pageTitle = getPageTitle(position).toString();
			Fragment fragment = null;
			if (pageTitle.equals("OVERVIEW")) {
				fragment = new Search_VenueOverviewFragment();
			} else if (pageTitle.equals("EVENTS")) {
				Bundle bundle = new Bundle();
				fragment = new Search_EventsFragment();
				// bundle.putBoolean(Constants.IS_COMING_FROM_EXPLORE, true);
				bundle.putString(Constants.IS_FOR_TODAY_EVENT, "VenueActivity");
				fragment.setArguments(bundle);
			} else {
				fragment = new VenueMediaFragment();
			}
			/* Adding fragment to stack */
			// SearchMapActivity.fragmentStack.lastElement().onPause();
			SearchMapActivity.fragmentStack.push(fragment);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}

}
