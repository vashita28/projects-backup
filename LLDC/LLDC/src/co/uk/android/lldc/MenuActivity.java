package co.uk.android.lldc;

import co.uk.android.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {

	RelativeLayout rlMenuTab, rlWelcomeTab, rlMapTab, rlExploreTab,
			rlEventsTab, rlTheParkTab;
	ImageView ivWelcomeHome, ivMap, ivExplore, ivEvent, ivThePark;
	TextView tvWelcome, tvMap, tvExplore, tvEvent, tvThePark;
	View viewPinkWelcome, viewPinkMap, viewPinkExplore, viewPinkEvent,
			viewPinkPark;
	int nLastSelectedFragment = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		rlMenuTab = (RelativeLayout) findViewById(R.id.rlMenuTab);
		rlWelcomeTab = (RelativeLayout) findViewById(R.id.rlWelcomeTab);
		rlMapTab = (RelativeLayout) findViewById(R.id.rlMapTab);
		rlExploreTab = (RelativeLayout) findViewById(R.id.rlExploreTab);
		rlEventsTab = (RelativeLayout) findViewById(R.id.rlEventsTab);
		rlTheParkTab = (RelativeLayout) findViewById(R.id.rlTheParkTab);
		ivWelcomeHome = (ImageView) findViewById(R.id.ivWelcomeHome);
		ivMap = (ImageView) findViewById(R.id.ivMap);
		ivExplore = (ImageView) findViewById(R.id.ivExplore);
		ivEvent = (ImageView) findViewById(R.id.ivEvent);
		ivThePark = (ImageView) findViewById(R.id.ivThePark);
		tvWelcome = (TextView) findViewById(R.id.tvWelcome);
		tvMap = (TextView) findViewById(R.id.tvMap);
		tvExplore = (TextView) findViewById(R.id.tvExplore);
		tvEvent = (TextView) findViewById(R.id.tvEvent);
		tvThePark = (TextView) findViewById(R.id.tvThePark);
		viewPinkWelcome = (View) findViewById(R.id.viewPinkWelcome);
		viewPinkMap = (View) findViewById(R.id.viewPinkMap);
		viewPinkExplore = (View) findViewById(R.id.viewPinkExplore);
		viewPinkEvent = (View) findViewById(R.id.viewPinkEvent);
		viewPinkPark = (View) findViewById(R.id.viewPinkPark);

		rlMenuTab.setOnClickListener(new onClickListener());
		rlWelcomeTab.setOnClickListener(new onClickListener());
		rlMapTab.setOnClickListener(new onClickListener());
		rlExploreTab.setOnClickListener(new onClickListener());
		rlEventsTab.setOnClickListener(new onClickListener());
		rlTheParkTab.setOnClickListener(new onClickListener());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Bundle bundle = getIntent().getExtras();
		nLastSelectedFragment = bundle
				.getInt(Constants.SELECTED_FRAGMENT_NUMBER);
		changeBg(nLastSelectedFragment);

	}

	class onClickListener implements OnClickListener {
		String message = "";
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlMenuTab:

				break;

			case R.id.rlWelcomeTab:
				intent.putExtra(Constants.MESSAGE, message);
				setResult(Constants.WELCOME_FRAGMENT, intent);
				break;

			case R.id.rlMapTab:
				intent.putExtra(Constants.MESSAGE, message);
				setResult(Constants.MAP_FRAGMENT, intent);
				break;

			case R.id.rlExploreTab:
				intent.putExtra(Constants.MESSAGE, message);
				setResult(Constants.EXPLORE_FRAGMENT, intent);
				break;

			case R.id.rlEventsTab:
				intent.putExtra(Constants.MESSAGE, message);
				setResult(Constants.EVENTS_FRAGMENT, intent);
				break;

			case R.id.rlTheParkTab:
				intent.putExtra(Constants.MESSAGE, message);
				setResult(Constants.THE_PARK_FRAGMENT, intent);
				break;

			default:
				break;

			}
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
	}

	void changeBg(int nSelected) {

		tvWelcome.setTextColor(getResources().getColor(R.color.black_heading));
		tvMap.setTextColor(getResources().getColor(R.color.black_heading));
		tvExplore.setTextColor(getResources().getColor(R.color.black_heading));
		tvEvent.setTextColor(getResources().getColor(R.color.black_heading));
		tvThePark.setTextColor(getResources().getColor(R.color.black_heading));

		ivWelcomeHome.setImageResource(R.drawable.welcome);
		ivMap.setImageResource(R.drawable.map);
		ivExplore.setImageResource(R.drawable.explore_icon);
		ivEvent.setImageResource(R.drawable.events);
		ivThePark.setImageResource(R.drawable.thepark);

		viewPinkWelcome.setVisibility(View.GONE);
		viewPinkMap.setVisibility(View.GONE);
		viewPinkExplore.setVisibility(View.GONE);
		viewPinkEvent.setVisibility(View.GONE);
		viewPinkPark.setVisibility(View.GONE);

		if (nSelected == Constants.WELCOME_FRAGMENT) {

			// rlWelcomeTab.setBackgroundColor(getResources().getColor(
			// R.color.selected_tab));
			viewPinkWelcome.setVisibility(View.VISIBLE);
			tvWelcome.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivWelcomeHome.setImageResource(R.drawable.welcome_selc);
		} else if (nSelected == Constants.MAP_FRAGMENT) {

			// rlMapTab.setBackgroundColor(getResources().getColor(
			// R.color.selected_tab));
			viewPinkMap.setVisibility(View.VISIBLE);
			tvMap.setTextColor(getResources().getColor(R.color.textcolor_pink));
			ivMap.setImageResource(R.drawable.map_selc);
		} else if (nSelected == Constants.EXPLORE_FRAGMENT) {

			// rlExploreTab.setBackgroundColor(getResources().getColor(
			// R.color.selected_tab));
			viewPinkExplore.setVisibility(View.VISIBLE);
			tvExplore.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivExplore.setImageResource(R.drawable.explore_icon_selc);
		} else if (nSelected == Constants.EVENTS_FRAGMENT) {

			// rlEventsTab.setBackgroundColor(getResources().getColor(
			// R.color.selected_tab));
			viewPinkEvent.setVisibility(View.VISIBLE);
			tvEvent.setTextColor(getResources()
					.getColor(R.color.textcolor_pink));
			ivEvent.setImageResource(R.drawable.events_selc);
		} else if (nSelected == Constants.THE_PARK_FRAGMENT) {

			// rlTheParkTab.setBackgroundColor(getResources().getColor(
			// R.color.selected_tab));
			viewPinkPark.setVisibility(View.VISIBLE);
			tvThePark.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivThePark.setImageResource(R.drawable.thepark_selc);
		}
	}

	public void onRootViewClick(View v) {
		Intent intent = new Intent();
		intent.putExtra(Constants.MESSAGE, nLastSelectedFragment);
		setResult(nLastSelectedFragment, intent);
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

}
