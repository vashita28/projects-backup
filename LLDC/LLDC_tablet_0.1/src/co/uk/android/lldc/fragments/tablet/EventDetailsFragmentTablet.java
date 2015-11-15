package co.uk.android.lldc.fragments.tablet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventOverviewFragment;
import co.uk.android.lldc.fragments.EventSocialFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.utils.Constants;

public class EventDetailsFragmentTablet extends Fragment {
	private static final String TAG = EventDetailsFragmentTablet.class
			.getSimpleName();

	Bundle bundle = new Bundle();
	public Fragment fragment = null;
	FragmentManager mFragmentManager;
	/** For tab host **/
	RelativeLayout releventoverview, releventsocial;
	TextView txteventoverview, txteventsocial;
	View vieweventoverview, vieweventsocial, tabHost;
	ViewPager pager = null;

	public static Handler mEventDetailsHandler = null;

	private static String[] CONTENT = new String[] { "Overview", "Social" };

	public EventDetailsFragmentTablet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_event_details,
				container, false);

		bundle = getArguments();

		mEventDetailsHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1101) {
					try {
						if (LLDCApplication.isInsideThePark
								|| LLDCApplication.isDebug) {
							// relSearch.setVisibility(View.VISIBLE);
						} else {
							// relSearch.setVisibility(View.GONE);
						}

						mEventDetailsHandler.sendEmptyMessageDelayed(1101,
								300000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		tabHost = (View) rootView.findViewById(R.id.tabHost);
		pager = (ViewPager) rootView.findViewById(R.id.pager);
		if (LLDCApplication.selectedModel.getSocialFlag().equals("1")
				&& !LLDCApplication.selectedModel.getSocialHandle().equals("")) {
			tabHost.setVisibility(View.VISIBLE);
			CONTENT = new String[] { "Overview", "Social" };
		} else {
			CONTENT = new String[] { "Overview" };
			tabHost.setVisibility(View.GONE);
		}

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(
				getChildFragmentManager());

		pager.setAdapter(adapter);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 0) {
					txteventoverview.setTextColor(Color.parseColor("#E81287"));
					txteventsocial.setTextColor(Color.parseColor("#B4B4B4"));
					vieweventoverview.setVisibility(View.VISIBLE);
					vieweventsocial.setVisibility(View.GONE);
				} else {
					txteventoverview.setTextColor(Color.parseColor("#B4B4B4"));
					txteventsocial.setTextColor(Color.parseColor("#E81287"));
					vieweventoverview.setVisibility(View.GONE);
					vieweventsocial.setVisibility(View.VISIBLE);
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

		releventoverview = (RelativeLayout) rootView
				.findViewById(R.id.releventoverview);
		releventsocial = (RelativeLayout) rootView
				.findViewById(R.id.releventsocial);
		txteventoverview = (TextView) rootView
				.findViewById(R.id.txteventoverview);
		txteventsocial = (TextView) rootView.findViewById(R.id.txteventsocial);
		vieweventoverview = (View) rootView
				.findViewById(R.id.vieweventoverview);
		vieweventsocial = (View) rootView.findViewById(R.id.vieweventsocial);
		releventoverview.setOnClickListener(new onTabChangeListener());
		releventsocial.setOnClickListener(new onTabChangeListener());

		if (bundle != null && bundle.containsKey("PAGETITLE")
				&& !bundle.getString("PAGETITLE").isEmpty())
			((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(bundle
					.getString("PAGETITLE"));

		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (bundle != null
								&& bundle
										.containsKey(Constants.IS_FOR_TODAY_EVENT)
								&& bundle.getString(
										Constants.IS_FOR_TODAY_EVENT).equals(
										"TodaysEventFragment")) {
							getParentFragment().getChildFragmentManager()
									.popBackStack();
						} else {
							((HomeActivityTablet) getActivity())
									.hideBackButton();
							getParentFragment().getChildFragmentManager()
									.popBackStack();
						}
					}
				});

		// displayView(1);

		return rootView;
	}

	class onTabChangeListener implements OnClickListener {
		@SuppressWarnings("unused")
		@Override
		public void onClick(View v) {
			Fragment f = null;
			switch (v.getId()) {
			case R.id.releventoverview:
				txteventoverview.setTextColor(Color.parseColor("#E81287"));
				txteventsocial.setTextColor(Color.parseColor("#B4B4B4"));
				vieweventoverview.setVisibility(View.VISIBLE);
				vieweventsocial.setVisibility(View.GONE);
				pager.setCurrentItem(0);
				// displayView(1);
				break;
			case R.id.releventsocial:
				mFragmentManager = getChildFragmentManager();
				mFragmentManager.findFragmentById(R.id.frame_container);
				txteventoverview.setTextColor(Color.parseColor("#B4B4B4"));
				txteventsocial.setTextColor(Color.parseColor("#E81287"));
				vieweventoverview.setVisibility(View.GONE);
				vieweventsocial.setVisibility(View.VISIBLE);
				pager.setCurrentItem(1);
				// displayView(2);
				break;
			}
		}
	}

	private void displayView(int position) {

		Fragment fragment = null;

		String sFragmentName = "";
		switch (position) {
		case 1:
			fragment = new EventOverviewFragment();
			sFragmentName = EventOverviewFragment.class.getName();
			break;

		case 2:
			fragment = new EventSocialFragment();
			sFragmentName = EventSocialFragment.class.getName();
			break;

		}

		if (fragment != null) {
			mFragmentManager = getChildFragmentManager();
			mFragmentManager.beginTransaction()
					.addToBackStack(EventDetailsFragmentTablet.class.getName())
					.replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		fragment = this;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((HomeActivityTablet) getActivity()).rlHeader
				.setVisibility(View.VISIBLE);
		((HomeActivityTablet) getActivity()).showBackButton();
		((HomeActivityTablet) getActivity()).showNavigationIcon();
		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("Events");

		mFragmentManager = getChildFragmentManager();
		Fragment f = mFragmentManager.findFragmentById(R.id.frame_container);
		if (f instanceof EventOverviewFragment) {
			if (EventOverviewFragment.mEventOverviewHandler != null)
				EventOverviewFragment.mEventOverviewHandler
						.sendEmptyMessageDelayed(1010, 500);
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mEventDetailsHandler.removeMessages(1101);
			mEventDetailsHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fragment = null;
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			if (position == 0) {
				fragment = new EventOverviewFragment();
			} else if (position == 1) {
				fragment = new EventSocialFragment();
			}
			return fragment;
		}

		@SuppressLint("DefaultLocale")
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
