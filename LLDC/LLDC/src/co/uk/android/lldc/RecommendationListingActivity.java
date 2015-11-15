package co.uk.android.lldc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import co.uk.android.lldc.fragments.RecommendationsListingFragment;
import co.uk.android.lldc.fragments.tablet.EventDetailsFragmentTablet;

public class RecommendationListingActivity extends FragmentActivity {
	private static final String TAG = RecommendationListingActivity.class
			.getSimpleName();
	
	Context mContext;
	RelativeLayout relMenu, relSearch, rlHeadingOfFooter, rlRelaxTab,
			rlEntertainTab, rlActiveTab;
	LinearLayout llElementsOfFooter;
	TextView tvHeaderTitle, tvFooterTitle;
	View viewUnderline, viewFooterBlur;

	FragmentManager mFragmentManager;

	public boolean bIsFooterOpen = false;

	String mPageTitle = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_home);

		mContext = this;

		mPageTitle = getIntent().getExtras().getString("PAGETITLE");

		relMenu = (RelativeLayout) findViewById(R.id.relMenu);
		ImageView ivBack = (ImageView) findViewById(R.id.ivBack);

		ivBack.setVisibility(View.VISIBLE);

		relSearch = (RelativeLayout) findViewById(R.id.relSearch);

		ImageView ivSerach = (ImageView) findViewById(R.id.ivSearch);
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

		hideFooterData();
		tvHeaderTitle.setText("Recommendations");
		displayView(1);
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = (Fragment) new RecommendationsListingFragment();
			break;
		}

		if (fragment != null) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (v.getId()) {

			case R.id.relMenu:
				RecommendationListingActivity.this.finish();
				break;

			case R.id.relSearch:
//				if (!isMenuActivityCalled) {
//					isMenuActivityCalled = true;
//					mHomeActivityHandler.sendEmptyMessageDelayed(1100, 100);
					intent = new Intent(RecommendationListingActivity.this, SearchActivity.class);
					startActivityForResult(intent, 1);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
//				}
				break;

			case R.id.rlHeadingOfFooter:
				switchFooter(bIsFooterOpen);
				break;

			case R.id.rlRelaxTab:
				rlHeadingOfFooter.performClick();
				intent = new Intent(RecommendationListingActivity.this,
						RecommendationListingActivity.class);
				intent.putExtra("PAGETITLE", "relax");
				RecommendationListingActivity.this.startActivity(intent);
				break;

			case R.id.rlEntertainTab:
				rlHeadingOfFooter.performClick();
				intent = new Intent(RecommendationListingActivity.this,
						RecommendationListingActivity.class);
				intent.putExtra("PAGETITLE", "entertain");
				RecommendationListingActivity.this.startActivity(intent);
				break;

			case R.id.rlActiveTab:
				rlHeadingOfFooter.performClick();
				intent = new Intent(RecommendationListingActivity.this,
						RecommendationListingActivity.class);
				intent.putExtra("PAGETITLE", "active");
				RecommendationListingActivity.this.startActivity(intent);
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

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		this.setIntent(intent);
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

}
