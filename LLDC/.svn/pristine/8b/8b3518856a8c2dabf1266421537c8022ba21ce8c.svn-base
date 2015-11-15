package co.uk.android.lldc.tablet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.fragments.VenueMediaFragment;
import co.uk.android.lldc.fragments.VenueOverviewFragment;
import co.uk.android.lldc.fragments.WelcomeFragment;

@SuppressLint({ "ResourceAsColor", "DefaultLocale" })
public class VenueActivity extends FragmentActivity {
	private static final String TAG = VenueActivity.class.getSimpleName();

	Context mContext;
	RelativeLayout relMenu, relSearch, rlHeadingOfFooter, rlRelaxTab,
			rlEntertainTab, rlActiveTab;
	LinearLayout llElementsOfFooter;
	TextView tvHeaderTitle, tvFooterTitle;
	View viewUnderline, viewFooterBlur;

	public boolean bIsFooterOpen = false;
	FragmentManager mFragmentManager;

	/** For tab host **/
	RelativeLayout relvenueoverview, relvenueevents, relvenuemedia;
	TextView txtvenueoverview, txtvenueevents, txtvenuemedia;
	View viewvenueoverview, viewvenueevents, viewvenuemedia;

	public Handler mVenueDetailsHandler = null;

	ViewPager pager;
	View tabHost;

	private static String[] CONTENT = new String[] { "Overview", "Events",
			"Media" };

	@SuppressLint({ "ResourceAsColor", "HandlerLeak" })
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_venue);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext = this;

		mVenueDetailsHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1101) {
					try {
						if (LLDCApplication.isInsideThePark
								|| LLDCApplication.isDebug) {
							relSearch.setVisibility(View.VISIBLE);
						} else {
							relSearch.setVisibility(View.GONE);
						}
						mVenueDetailsHandler.sendEmptyMessageDelayed(1101,
								300000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		relMenu = (RelativeLayout) findViewById(R.id.relMenu);

		ImageView ivBack = (ImageView) findViewById(R.id.ivBack);

		ivBack.setVisibility(View.VISIBLE);

		relSearch = (RelativeLayout) findViewById(R.id.relSearch);

		ImageView ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
		ivNavigation.setVisibility(View.VISIBLE);

		rlHeadingOfFooter = (RelativeLayout) findViewById(R.id.rlHeadingOfFooter);
		llElementsOfFooter = (LinearLayout) findViewById(R.id.llElementsOfFooter);
		tvHeaderTitle = (TextView) findViewById(R.id.tvPageTitle);
		tvFooterTitle = (TextView) findViewById(R.id.tvFooterTitle);
		viewUnderline = (View) findViewById(R.id.viewUnderline);
		rlRelaxTab = (RelativeLayout) findViewById(R.id.rlRelaxTab);
		rlEntertainTab = (RelativeLayout) findViewById(R.id.rlEntertainTab);
		rlActiveTab = (RelativeLayout) findViewById(R.id.rlActiveTab);
		viewFooterBlur = (View) findViewById(R.id.footerblur);
		relMenu.setOnClickListener(new OnitemClickListener());
		relSearch.setOnClickListener(new OnitemClickListener());
		rlHeadingOfFooter.setOnClickListener(new OnitemClickListener());
		rlRelaxTab.setOnClickListener(new OnitemClickListener());
		rlEntertainTab.setOnClickListener(new OnitemClickListener());
		rlActiveTab.setOnClickListener(new OnitemClickListener());
		viewFooterBlur.setOnClickListener(new OnitemClickListener());

		/** For tab host **/
		relvenueoverview = (RelativeLayout) findViewById(R.id.relvenueoverview);
		relvenueevents = (RelativeLayout) findViewById(R.id.relvenueevents);
		relvenuemedia = (RelativeLayout) findViewById(R.id.relvenuemedia);
		txtvenueoverview = (TextView) findViewById(R.id.txtvenueoverview);
		txtvenueevents = (TextView) findViewById(R.id.txtvenueevents);
		txtvenuemedia = (TextView) findViewById(R.id.txtvenuemedia);
		viewvenueoverview = (View) findViewById(R.id.viewvenueoverview);
		viewvenueevents = (View) findViewById(R.id.viewvenueevents);
		viewvenuemedia = (View) findViewById(R.id.viewvenuemedia);
		relvenueoverview.setOnClickListener(new onTabChangeListener());
		relvenueevents.setOnClickListener(new onTabChangeListener());
		relvenuemedia.setOnClickListener(new onTabChangeListener());
		// txtvenueoverview.setOnClickListener(new OnitemClickListener());
		// txtvenueevents.setOnClickListener(new OnitemClickListener());
		// txtvenuemedia.setOnClickListener(new OnitemClickListener());
		// viewvenueoverview.setOnClickListener(new OnitemClickListener());
		// viewvenueevents.setOnClickListener(new OnitemClickListener());
		// viewvenuemedia.setOnClickListener(new OnitemClickListener());
		tvHeaderTitle.setText("Venues");
		hideFooterData();
		// displayView(1);

		pager = (ViewPager) findViewById(R.id.pager);
		tabHost = (View) findViewById(R.id.tabHost);

		/**
		 * Added logic to hide unwanted tab CONTENT = new String[] { "Overview"
		 * }; tabHost.setVisibility(View.GONE);
		 * 
		 * if (LLDCApplication.selectedModel.getEventCount() != 0) {
		 * tabHost.setVisibility(View.VISIBLE); CONTENT = new String[] {
		 * "Overview", "Events" }; }else{
		 * relvenueevents.setVisibility(View.GONE); }
		 * 
		 * if (LLDCApplication.selectedModel.getMediaList().size() > 0 &&
		 * LLDCApplication.selectedModel.getEventCount() != 0) {
		 * tabHost.setVisibility(View.VISIBLE); CONTENT = new String[] {
		 * "Overview", "Events", "Media" }; } else if
		 * (LLDCApplication.selectedModel.getMediaList().size() > 0 &&
		 * LLDCApplication.selectedModel.getEventCount() == 0) {
		 * tabHost.setVisibility(View.VISIBLE); CONTENT = new String[] {
		 * "Overview", "Media" }; relvenueevents.setVisibility(View.GONE);
		 * }else{ relvenuemedia.setVisibility(View.GONE); }
		 **/
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
				getSupportFragmentManager());

		pager.setAdapter(adapter);

		mVenueDetailsHandler.sendEmptyMessage(1101);

	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (v.getId()) {

			case R.id.relMenu:
				VenueActivity.this.finish();
				break;

			case R.id.relSearch:
				try {
					LLDCApplication
							.showSimpleProgressDialog(VenueActivity.this);
					intent = new Intent(VenueActivity.this,
							MapNavigationActivity.class);
					intent.putExtra("PAGETITLE", "Map");
					startActivity(intent);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;

			case R.id.rlHeadingOfFooter:
				try {
					mFragmentManager = getSupportFragmentManager();
					Fragment f = mFragmentManager
							.findFragmentById(R.id.frame_container);
					if (f instanceof WelcomeFragment) {
						viewFooterBlur.setVisibility(View.GONE);
					} else {
						switchFooter(bIsFooterOpen);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlRelaxTab:
				try {
					LLDCApplication
							.showSimpleProgressDialog(VenueActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(VenueActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "relax");
					VenueActivity.this.startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlEntertainTab:
				try {
					LLDCApplication
							.showSimpleProgressDialog(VenueActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(VenueActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "entertain");
					VenueActivity.this.startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlActiveTab:
				try {
					LLDCApplication
							.showSimpleProgressDialog(VenueActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(VenueActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "active");
					VenueActivity.this.startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.footerblur:
				switchFooter(bIsFooterOpen);
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bIsFooterOpen) {
			switchFooter(bIsFooterOpen);
		} else
			super.onBackPressed();
	}

	class onTabChangeListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Fragment f = null;
			switch (v.getId()) {
			case R.id.relvenueoverview:
				mFragmentManager = getSupportFragmentManager();
				f = mFragmentManager.findFragmentById(R.id.frame_container);
				if (f instanceof VenueOverviewFragment) {

				} else {
					// relvenueoverview.setBackgroundColor(Color.WHITE);
					// relvenueevents.setBackgroundColor(Color.GRAY);
					// relvenuemedia.setBackgroundColor(Color.GRAY);
					txtvenueoverview.setTextColor(Color.parseColor("#41C0F0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.VISIBLE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.GONE);
				}
				pager.setCurrentItem(0);
				// displayView(1);
				break;
			case R.id.relvenueevents:
				mFragmentManager = getSupportFragmentManager();
				f = mFragmentManager.findFragmentById(R.id.frame_container);
				if (f instanceof EventsFragment) {

				} else {
					// relvenueoverview.setBackgroundColor(Color.WHITE);
					// relvenueevents.setBackgroundColor(Color.GRAY);
					// relvenuemedia.setBackgroundColor(Color.GRAY);
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#41C0F0"));
					txtvenuemedia.setTextColor(Color.parseColor("#A0A0A0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.VISIBLE);
					viewvenuemedia.setVisibility(View.GONE);
				}
				pager.setCurrentItem(1);
				// displayView(2);
				break;
			case R.id.relvenuemedia:
				mFragmentManager = getSupportFragmentManager();
				f = mFragmentManager.findFragmentById(R.id.frame_container);
				if (f instanceof WelcomeFragment) {

				} else {
					// relvenueoverview.setBackgroundColor(Color.WHITE);
					// relvenueevents.setBackgroundColor(Color.GRAY);
					// relvenuemedia.setBackgroundColor(Color.GRAY);
					txtvenueoverview.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenueevents.setTextColor(Color.parseColor("#A0A0A0"));
					txtvenuemedia.setTextColor(Color.parseColor("#41C0F0"));
					viewvenueoverview.setVisibility(View.GONE);
					viewvenueevents.setVisibility(View.GONE);
					viewvenuemedia.setVisibility(View.VISIBLE);
				}
				pager.setCurrentItem(2);
				// displayView(3);
				break;
			}
		}
	}

	void hideFooterData() {
		viewUnderline.setVisibility(View.GONE);
		llElementsOfFooter.setVisibility(View.GONE);
		tvFooterTitle.setText("WHAT ARE YOU HERE TO DO?");
		tvFooterTitle.setTypeface(null, Typeface.BOLD);
		tvFooterTitle.setTextColor(getResources().getColor(
				R.color.black_heading));
		rlHeadingOfFooter.setBackgroundColor(getResources().getColor(
				R.color.common_footer));
	}

	@SuppressLint("ResourceAsColor")
	void showFooterData() {
		viewUnderline.setVisibility(View.VISIBLE);
		llElementsOfFooter.setVisibility(View.VISIBLE);
		tvFooterTitle.setTypeface(null, Typeface.NORMAL);
		tvFooterTitle.setText("WHAT ARE YOU LOOKING TO DO?");
		tvFooterTitle.setTextColor(R.color.textcolor_grey);
		rlHeadingOfFooter.setBackgroundColor(getResources().getColor(
				android.R.color.white));
	}

	void switchFooter(boolean isFooterOpen) {
		if (isFooterOpen) {
			bIsFooterOpen = false;
			llElementsOfFooter.setVisibility(View.GONE);
			viewFooterBlur.setVisibility(View.GONE);
			rlHeadingOfFooter.setBackgroundColor(getResources().getColor(
					R.color.common_footer));
		} else {
			bIsFooterOpen = true;
			llElementsOfFooter.setVisibility(View.VISIBLE);
			viewFooterBlur.setVisibility(View.VISIBLE);
			rlHeadingOfFooter.setBackgroundColor(getResources().getColor(
					android.R.color.white));
		}
	}

	/**
	 * Diplaying fragment view for selected tab in MenuActivity
	 * */
	@SuppressWarnings("unused")
	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = new co.uk.android.lldc.fragments.VenueOverviewFragment();
			break;

		case 2:
			fragment = new co.uk.android.lldc.fragments.EventsFragment();
			break;

		case 3:
			fragment = new co.uk.android.lldc.fragments.VenueMediaFragment();
			break;

		}

		if (fragment != null) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();

			// update selected item and title, then close the drawer
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mVenueDetailsHandler.removeMessages(1101);
			mVenueDetailsHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			if (position == 0) {
				fragment = new VenueOverviewFragment();
			} else if (position == 1) {
				fragment = new EventsFragment();
			} else if (position == 2) {
				fragment = new VenueMediaFragment();
			}
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
