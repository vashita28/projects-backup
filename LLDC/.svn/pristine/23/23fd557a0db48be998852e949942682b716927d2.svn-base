package co.uk.android.lldc.fragments.tablet;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.MapFragment;
import co.uk.android.lldc.fragments.MapNavFragment;
import co.uk.android.lldc.fragments.MapTrailsFragment;
import co.uk.android.lldc.fragments.SearchMapFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;

public class MapNavigationFragment extends Fragment {

	Context mContext;

	FragmentManager mFragmentManager;

	public boolean bIsFooterOpen = false;

	String mPageTitle = "";

	String pageSubTitle = "";
	String selectedId = "";

	LocationManager locationManager;
	String provider;

	Bundle bundle = new Bundle();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_map_navigation,
				container, false);

		bundle = getArguments();

		if (mPageTitle.equals("Search")) {
			mPageTitle = "Map";
			pageSubTitle = bundle.getString("PAGESUBTITLE");
			selectedId = bundle.getString("SELID");
			if (pageSubTitle.equals("SHOWME")) {
				displayView(3);
			} else
				displayView(2);
		} else
			displayView(1);

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("Map");
		((HomeActivityTablet) getActivity()).showBackButton();
		((HomeActivityTablet) getActivity()).showCommonSearchIcon();
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getParentFragment().getChildFragmentManager()
								.popBackStack();
					}
				});

		return rootView;
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			if (mPageTitle.equals("Trails"))
				fragment = (Fragment) new MapTrailsFragment();
			else
				fragment = (Fragment) new MapNavFragment();
			break;
		case 2:
			fragment = (Fragment) new SearchMapFragment(pageSubTitle,
					selectedId);
			break;
		case 3:
			fragment = (Fragment) new MapFragment(pageSubTitle, selectedId);
			break;
		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager.beginTransaction()
					.add(R.id.frame_container_parent, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
		}

	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// // TODO Auto-generated method stub
	// super.onNewIntent(intent);
	// try {
	// String mPageTitle = intent.getExtras().getString("PAGETITLE");
	// if (mPageTitle.equals("Search")) {
	// mPageTitle = "Map";
	// pageSubTitle = getIntent().getExtras()
	// .getString("PAGESUBTITLE");
	// selectedId = getIntent().getExtras().getString("SELID");
	// if (pageSubTitle.equals("SHOWME")) {
	// displayView(3);
	// } else
	// displayView(2);
	// } else
	// displayView(1);
	// //tvHeaderTitle.setText(mPageTitle);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
