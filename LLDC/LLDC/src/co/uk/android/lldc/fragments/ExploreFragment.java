package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import co.uk.android.lldc.ExploreListDetailsActivity;
import co.uk.android.lldc.ExploreListingActivity;
import co.uk.android.lldc.HomeActivityTablet;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.VenueActivity;
import co.uk.android.lldc.adapters.ExploreAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.fragments.tablet.ExploreListDetailsFragmentTablet;
import co.uk.android.lldc.fragments.tablet.ExploreTabParentFragmentTablet;
import co.uk.android.lldc.fragments.tablet.VenueFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.util.Util;

public class ExploreFragment extends Fragment {
	private static final String TAG = ExploreFragment.class.getSimpleName();

	RelativeLayout rlTrailsAndTours, rlVenuesAndAttractions, rlLondon2012,
			rlFacilities;
	GridView lvExplore;
	ExploreAdapter exploreAdapter;
	ArrayList<ServerModel> exploreList = new ArrayList<ServerModel>();

	Context mcontext;
	boolean bIsTablet = false;
	FragmentManager mFragmentManager;
	Fragment fragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(R.layout.fragment_explore_tablet,
					container, false);
			bIsTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(R.layout.fragment_explore, container,
					false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
		}

		mcontext = getActivity();

		if (bIsTablet) {
			((HomeActivityTablet) getActivity()).hideBackButton();
			((HomeActivityTablet) getActivity()).tvHeaderTitle
					.setText("Explore");
		}

		rlTrailsAndTours = (RelativeLayout) rootView
				.findViewById(R.id.rlTrailsnTours);
		rlVenuesAndAttractions = (RelativeLayout) rootView
				.findViewById(R.id.rlVenuenAttractions);
		rlLondon2012 = (RelativeLayout) rootView
				.findViewById(R.id.rlLondon2012);
		rlFacilities = (RelativeLayout) rootView
				.findViewById(R.id.rlFacilities);
		lvExplore = (GridView) rootView.findViewById(R.id.listViewExplore);

		rlTrailsAndTours.setOnClickListener(new onClickListener());
		rlVenuesAndAttractions.setOnClickListener(new onClickListener());
		rlLondon2012.setOnClickListener(new onClickListener());
		rlFacilities.setOnClickListener(new onClickListener());

		lvExplore.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				Bundle bundle = new Bundle();
				bundle = getArguments();
				LLDCApplication.selectedModel = exploreList.get(arg2);

				if (!bIsTablet) {
					intent = new Intent(getActivity(),
							ExploreListDetailsActivity.class);
				}
				int i = LLDCApplication.selectedModel.getModelType();
				if (i == LLDCApplication.VENUE) {
					if (bIsTablet)
						displayView(1, "Venues");
					else {
						intent = new Intent(getActivity(), VenueActivity.class);
						startActivity(intent);
						// getActivity().startActivity(intent);
						// intent.putExtra("PAGETITLE", "Venues & Attractions");
					}

				} else if (i == LLDCApplication.FACILITIES) {
					if (bIsTablet)
						displayView(2, "Facilities");
					else {
						intent.putExtra("PAGETITLE", "Facilities");
						startActivity(intent);
					}
				} else if (i == LLDCApplication.TRAILS) {
					if (bIsTablet)
						displayView(2, "Trails");
					else {
						intent.putExtra("PAGETITLE", "Trails");
						startActivity(intent);
					}
				}

			}
		});

		return rootView;

	}

	private void displayView(int position, String szPagetitle) {
		Bundle bundle = new Bundle();
		bundle.putString("PAGETITLE", szPagetitle);
		String fragmentName = "";

		switch (position) {
		case 1:
			fragment = (Fragment) new VenueFragmentTablet();
			fragmentName = VenueFragmentTablet.class.getName();
			break;

		case 2:
			Log.e(TAG, "ExploreListDetailsFragmentTablet");
			fragment = (Fragment) new ExploreListDetailsFragmentTablet();
			fragmentName = ExploreListDetailsFragmentTablet.class.getName();
			break;

		}

		if (fragment != null) {
			fragment.setArguments(bundle);
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					.addToBackStack(ExploreFragment.class.getName())
					.replace(R.id.frame_container_parent, fragment,
							fragmentName)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDataFromDB();
	}

	public void getDataFromDB() {
		try {
			// exploreList
			exploreList.clear();
			ArrayList<ServerModel> facilitiesList = LLDCApplication.DBHelper
					.onGetFacilitiesData("");
			ArrayList<ServerModel> trailsList = LLDCApplication.DBHelper
					.onGetTrailsData("");
			ArrayList<ServerModel> venueList = LLDCApplication.DBHelper
					.onGetVenueData("");
			int i = 0;
			int j = 0;
			for (; i < trailsList.size(); i++) {
				exploreList.add(trailsList.get(i));
			}
			for (; j < venueList.size(); j++, i++) {
				exploreList.add(venueList.get(j));
			}
			for (j = 0; j < facilitiesList.size(); j++, i++) {
				exploreList.add(facilitiesList.get(j));
			}

			/* set explore adapter */
			if (exploreList != null) {
				exploreAdapter = new ExploreAdapter(getActivity(), exploreList);
				lvExplore.setAdapter(exploreAdapter);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class onClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			Fragment fragment = null;
			Bundle bundle = new Bundle();
			if (!bIsTablet) {
				intent = new Intent(getActivity(), ExploreListingActivity.class);
			} else {
				fragment = new co.uk.android.lldc.fragments.tablet.ExploreTabParentFragmentTablet();
			}
			switch (v.getId()) {
			case R.id.rlTrailsnTours:
				if (!bIsTablet) {
					intent.putExtra("PAGETITLE", "Trails");
					startActivity(intent);
				} else {
					bundle.putString("PAGETITLE", "Trails");
				}
				break;

			case R.id.rlVenuenAttractions:
				if (!bIsTablet) {
					intent.putExtra("PAGETITLE", "Venues & Attractions");
					startActivity(intent);
				} else {
					bundle.putString("PAGETITLE", "Venues & Attractions");
				}
				break;

			case R.id.rlLondon2012:
				ArrayList<ServerModel> londonTrailList = getLondonTrailsList();

				if (londonTrailList.size() > 0) {
					if (!bIsTablet) {
						intent.putExtra("PAGETITLE", "London 2012");
						startActivity(intent);
					} else {
						bundle.putString("PAGETITLE", "London 2012");
					}
				} else {
					LLDCApplication.onShowToastMesssage(getActivity(),
							"London 2012 trail data not available...");
				}
				break;

			case R.id.rlFacilities:
				if (!bIsTablet) {
					intent.putExtra("PAGETITLE", "Facilities");
					startActivity(intent);
				} else {
					bundle.putString("PAGETITLE", "Facilities");
				}
				break;

			default:
				break;
			}

			if (bIsTablet) {

				if (fragment != null) {
					fragment.setArguments(bundle);
					mFragmentManager = getParentFragment()
							.getChildFragmentManager();
					mFragmentManager
							.beginTransaction()
							.addToBackStack(ExploreFragment.class.getName())
							.replace(
									R.id.frame_container_parent,
									fragment,
									ExploreTabParentFragmentTablet.class
											.getName())
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_FADE)
							.commit();
				}
			}
		}

	}

	public ArrayList<ServerModel> getLondonTrailsList() {
		ArrayList<ServerModel> londonTrailList = new ArrayList<ServerModel>();
		ArrayList<ServerModel> temp1 = LLDCApplication.DBHelper.getLondonTrail(
				LLDCDataBaseHelper.TABLE_EVENTS, LLDCApplication.EVENT);
		ArrayList<ServerModel> temp2 = LLDCApplication.DBHelper.getLondonTrail(
				LLDCDataBaseHelper.TABLE_VENUES, LLDCApplication.VENUE);
		ArrayList<ServerModel> temp3 = LLDCApplication.DBHelper
				.getLondonTrail(LLDCDataBaseHelper.TABLE_FACILITIES,
						LLDCApplication.FACILITIES);
		ArrayList<ServerModel> temp4 = LLDCApplication.DBHelper.getLondonTrail(
				LLDCDataBaseHelper.TABLE_TRAILS, LLDCApplication.TRAILS);

		londonTrailList = temp1;

		for (int i = 0; i < temp2.size(); i++) {
			londonTrailList.add(temp2.get(i));
		}
		for (int i = 0; i < temp3.size(); i++) {
			londonTrailList.add(temp3.get(i));
		}
		for (int i = 0; i < temp4.size(); i++) {
			londonTrailList.add(temp4.get(i));
		}

		return londonTrailList;
	}

}
