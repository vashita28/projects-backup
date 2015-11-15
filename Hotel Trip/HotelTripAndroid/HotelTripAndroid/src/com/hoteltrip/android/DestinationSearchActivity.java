package com.hoteltrip.android;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.hoteltrip.android.fragment.NearbyFragment;
import com.hoteltrip.android.fragment.PlaceSearchFragment;
import com.hoteltrip.android.util.NavTabsListener;

public class DestinationSearchActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination_search);

		actionBar = getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab placeSearchTab = actionBar
				.newTab()
				.setText(R.string.title_place_search)
				.setTabListener(
						new NavTabsListener(R.id.destinationSearchContainer,
								this, new PlaceSearchFragment()));
		Tab nearbyTab = actionBar
				.newTab()
				.setText(R.string.title_nearby)
				.setTabListener(
						new NavTabsListener(R.id.destinationSearchContainer,
								this, new NearbyFragment()));

		actionBar.addTab(placeSearchTab);
		actionBar.addTab(nearbyTab);

		int tabIndex = getIntent().getExtras().getInt("tabIndex");
		actionBar.setSelectedNavigationItem(tabIndex);

		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// TODO: another useless hackery to dismiss the keyboard when the index
		// of the fragment is nearby's
		if (actionBar.getSelectedNavigationIndex() == 1) {
			mgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

}
