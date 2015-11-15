package co.uk.android.lldc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.HomeActivityTablet;

public class SearchMapFragmentTablet extends Fragment {

	FragmentManager mFragmentManager;

	Bundle bundle = new Bundle();

	String mPageTitle = "";

	String pageSubTitle = "";
	String selectedId = "";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_map_navigation,
				container, false);

		bundle = getArguments();

		if (bundle != null && bundle.containsKey("PAGETITLE"))
			mPageTitle = bundle.getString("PAGETITLE");

		try {
			String mPageTitle = bundle.getString("PAGETITLE");
			if (mPageTitle.equals("Search")) {
				mPageTitle = "Map";
				pageSubTitle = bundle.getString("PAGESUBTITLE");
				selectedId = bundle.getString("SELID");
				if (pageSubTitle.equals("SHOWME")) {
					displayView(2);
				} else
					displayView(1);
			}

			((HomeActivityTablet) getActivity()).tvHeaderTitle
					.setText(mPageTitle);
			// tvHeaderTitle.setText(mPageTitle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rootView;
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = (Fragment) new SearchMapFragment(pageSubTitle,
					selectedId);
			break;
		case 2:
			fragment = (Fragment) new MapFragment(pageSubTitle, selectedId);
			break;
		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();// getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container_parent, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
		}

	}

}
