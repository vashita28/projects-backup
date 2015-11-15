package co.uk.android.lldc.tablet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.ExploreListingFragment;
import co.uk.android.lldc.models.ServerModel;

@SuppressLint("ResourceAsColor")
public class ExploreListingActivity extends FragmentActivity {
	private static final String TAG = ExploreListingActivity.class
			.getSimpleName();

	Context mContext;
	RelativeLayout relMenu, relSearch, rlHeadingOfFooter, rlRelaxTab,
			rlEntertainTab, rlActiveTab;
	LinearLayout llElementsOfFooter;
	TextView tvHeaderTitle, tvFooterTitle;
	View viewUnderline, viewFooterBlur;
	public boolean bIsFooterOpen = false;

	FragmentManager mFragmentManager;

	public static ServerModel selectedModel = new ServerModel();

	public static Handler mExploreListingHandler;
	String pageTitle = "";

	ImageView ivSerach, ivNavigation;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_home);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mContext = this;

		mExploreListingHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (msg.what == 1002) {
					if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
						tvHeaderTitle.setText("Trails");
					}
					displayView(2);
				} else if (msg.what == 1001) {
					displayView(1);
				} else if (msg.what == 1101) {
					try {
						mFragmentManager = getSupportFragmentManager();
						Fragment f = mFragmentManager
								.findFragmentById(R.id.frame_container);
						if (f instanceof ExploreListingFragment) {
							ivNavigation.setVisibility(View.GONE);
							ivSerach.setVisibility(View.VISIBLE);
						} else {
							if (LLDCApplication.isInsideThePark
									|| LLDCApplication.isDebug) {
								ivNavigation.setVisibility(View.VISIBLE);
							} else {
								ivNavigation.setVisibility(View.GONE);
							}
							ivSerach.setVisibility(View.GONE);
						}

						mExploreListingHandler.sendEmptyMessageDelayed(1101,
								300000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		pageTitle = (String) getIntent().getExtras().get("PAGETITLE");

		relMenu = (RelativeLayout) findViewById(R.id.relMenu);

		ImageView ivBack = (ImageView) findViewById(R.id.ivBack);

		ivBack.setVisibility(View.VISIBLE);

		relSearch = (RelativeLayout) findViewById(R.id.relSearch);

		ivSerach = (ImageView) findViewById(R.id.ivSearch);
		ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
		ivSerach.setVisibility(View.VISIBLE);

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
		tvHeaderTitle.setText(pageTitle);

		hideFooterData();
		displayView(1);

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
	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			tvHeaderTitle.setText(pageTitle);
			fragment = new co.uk.android.lldc.fragments.ExploreListingFragment();
			ivNavigation.setVisibility(View.GONE);
			ivSerach.setVisibility(View.VISIBLE);
			break;
		case 2:
			if (pageTitle.equals("Facilities")) {
				tvHeaderTitle.setText("Food & Drink");
			}
			ivNavigation.setVisibility(View.VISIBLE);
			ivSerach.setVisibility(View.GONE);
			fragment = new co.uk.android.lldc.fragments.ExploreDetailsFragment();
			break;
		}

		if (fragment != null) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			mExploreListingHandler.sendEmptyMessage(1101);
		}
	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = null;

			switch (v.getId()) {

			case R.id.relMenu:
				mFragmentManager = getSupportFragmentManager();
				Fragment f = mFragmentManager
						.findFragmentById(R.id.frame_container);
						
							if (f instanceof ExploreListingFragment) {
					ExploreListingActivity.this.finish();
				} else {
					mFragmentManager
							.beginTransaction()
							.remove(f)
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_FADE)
							.commit();
					mExploreListingHandler.sendEmptyMessage(1101);
				}			
						
						
						
						
						
						
						
			//	if (f instanceof ExploreDetailsFragment) {
			//		if (ExploreListingActivity.mExploreListingHandler != null) {
			//			ExploreListingActivity.mExploreListingHandler
			//					.sendEmptyMessage(1001);
			//		}
			//	} else
			//		ExploreListingActivity.this.finish();
				break;

			case R.id.relSearch:
				LLDCApplication.showSimpleProgressDialog(ExploreListingActivity.this);
				if (ivNavigation.getVisibility() == View.VISIBLE) {
					intent = new Intent(ExploreListingActivity.this,
							MapNavigationActivity.class);
					if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
						intent.putExtra("PAGETITLE", "Trails");
						if (LLDCApplication.selectedModel.getWyapoitnList()
								.size() == 0) {
							LLDCApplication.onShowToastMesssage(
									getApplicationContext(),
									"Trails data is not present...");
							return;
						}
					} else {
						intent.putExtra("PAGETITLE", pageTitle);
					}
					ExploreListingActivity.this.startActivity(intent);
				} else if (ivSerach.getVisibility() == View.VISIBLE) {
					intent = new Intent(ExploreListingActivity.this,
							SearchActivity.class);
					startActivityForResult(intent, 1);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
					// }
				}
				break;

			case R.id.rlHeadingOfFooter:
				switchFooter(bIsFooterOpen);
				break;

			case R.id.rlRelaxTab:
				try {
					LLDCApplication.showSimpleProgressDialog(ExploreListingActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(ExploreListingActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "relax");
					ExploreListingActivity.this.startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlEntertainTab:
				try {
					LLDCApplication.showSimpleProgressDialog(ExploreListingActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(ExploreListingActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "entertain");
					ExploreListingActivity.this.startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlActiveTab:
				try {
					LLDCApplication.showSimpleProgressDialog(ExploreListingActivity.this);
					rlHeadingOfFooter.performClick();
					intent = new Intent(ExploreListingActivity.this,
							RecommendationListingActivity.class);
					intent.putExtra("PAGETITLE", "active");
					ExploreListingActivity.this.startActivity(intent);
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
		} else {
			mFragmentManager = getSupportFragmentManager();
			Fragment f = mFragmentManager
					.findFragmentById(R.id.frame_container);
			if (f instanceof ExploreListingFragment) {
				super.onBackPressed();
			} else {
			//	displayView(1);
	mFragmentManager
						.beginTransaction()
						.remove(f)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
			mExploreListingHandler.sendEmptyMessage(1101);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mExploreListingHandler.removeMessages(1101);
			mExploreListingHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
