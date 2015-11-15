package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import com.google.android.gms.internal.bs;

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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.EventDetailsActivity;
import co.uk.android.lldc.ExploreListingActivity;
import co.uk.android.lldc.HomeActivityTablet;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.StaticTrailsActivity;
import co.uk.android.lldc.VenueActivity;
import co.uk.android.lldc.adapters.VenuesAndAttractionsAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.fragments.tablet.EventDetailsFragmentTablet;
import co.uk.android.lldc.fragments.tablet.ExploreTabParentFragmentTablet;
import co.uk.android.lldc.fragments.tablet.StaticTrailsDummyFragmentTablet;
import co.uk.android.lldc.fragments.tablet.VenueFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.util.Util;

public class ExploreListingFragment extends Fragment {
	private static final String TAG = ExploreListingFragment.class
			.getSimpleName();

	ListView listVenuesAttractions;
	VenuesAndAttractionsAdapter listAdapter;
	TextView tvPageTitle;

	RelativeLayout relMenu, relSearch, rlHeadingOfFooter, rlRelaxTab,
			rlEntertainTab, rlActiveTab;
	LinearLayout llElementsOfFooter;
	TextView tvHeaderTitle, tvFooterTitle;
	View viewUnderline, footerblur;
	public boolean bIsFooterOpen = false;
	ArrayList<ServerModel> venueList = new ArrayList<ServerModel>();

	String pageTitle = "";
	LinearLayout bbar_layout;
	RelativeLayout rlGuidedTours, rlBoatTrails;

	public static boolean bIsTablet = false;
	FragmentManager mFragmentManager;
	Fragment fragment = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(
					R.layout.activity_venues_and_attractions_tablet, container,
					false);
			bIsTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(
					R.layout.activity_venues_and_attractions, container, false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
		}
		listVenuesAttractions = (ListView) rootView
				.findViewById(R.id.lvVenuesAndAttractions);

		bbar_layout = (LinearLayout) rootView.findViewById(R.id.bbar_layout);

		rlGuidedTours = (RelativeLayout) rootView
				.findViewById(R.id.rlGuidedTours);
		rlBoatTrails = (RelativeLayout) rootView
				.findViewById(R.id.rlBoatTrails);

		if (!bIsTablet)
			pageTitle = (String) getActivity().getIntent().getExtras()
					.get("PAGETITLE");
		else {
			Bundle bundle = getArguments();
			pageTitle = bundle.getString("PAGETITLE");
		}

		if (bIsTablet && !pageTitle.equals("Trails")) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.weight = 2.0f;
			listVenuesAttractions.setLayoutParams(params);
		}

		onGetDataFromDB();

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(pageTitle);
		((HomeActivityTablet) getActivity()).showBackButton();
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// getParentFragment().getChildFragmentManager()
						// .popBackStack();

						// TODO Auto-generated method stub
						ExploreFragment exploreFragment = (ExploreFragment) getParentFragment()
								.getChildFragmentManager().findFragmentByTag(
										ExploreFragment.class.getName());
						if (exploreFragment != null) {
							getParentFragment()
									.getChildFragmentManager()
									.beginTransaction()
									.replace(R.id.frame_container_parent,
											exploreFragment).commit();
						} else {
							Log.e(TAG, "else back");
						}

					}
				});

		listVenuesAttractions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LLDCApplication.selectedModel = venueList.get(arg2);
				Intent intent = null;
				Fragment fragment = null;
				String fragmentName = "";

				if (pageTitle.equals("Venues & Attractions")) {
					if (bIsTablet) {
						fragment = new co.uk.android.lldc.fragments.tablet.VenueFragmentTablet();
						fragmentName = VenueFragmentTablet.class.getName();
					} else {
						intent = new Intent(getActivity(), VenueActivity.class);
						getActivity().startActivity(intent);
					}
				} else if (pageTitle.equals("London 2012")) {
					if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.VENUE) {
						if (bIsTablet) {
							fragment = new co.uk.android.lldc.fragments.tablet.VenueFragmentTablet();
							fragmentName = VenueFragmentTablet.class.getName();
						} else {
							intent = new Intent(getActivity(),
									VenueActivity.class);
							getActivity().startActivity(intent);
						}
					} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.FACILITIES) {
						if (bIsTablet) {
							ExploreListingActivity.selectedModel = venueList
									.get(arg2);
							if (ExploreListingActivity.mExploreListingHandler != null) {
								ExploreListingActivity.mExploreListingHandler
										.sendEmptyMessage(1002);
							}
						} else {
							ExploreListingActivity.selectedModel = venueList
									.get(arg2);
							if (ExploreTabParentFragmentTablet.mExploreListingHandler != null) {
								ExploreTabParentFragmentTablet.mExploreListingHandler
										.sendEmptyMessage(1002);
							}
						}
					} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
						if (bIsTablet) {
							ExploreListingActivity.selectedModel = venueList
									.get(arg2);
							if (ExploreListingActivity.mExploreListingHandler != null) {
								ExploreListingActivity.mExploreListingHandler
										.sendEmptyMessage(1002);
							}
						} else {

							ExploreListingActivity.selectedModel = venueList
									.get(arg2);
							if (ExploreTabParentFragmentTablet.mExploreListingHandler != null) {
								ExploreTabParentFragmentTablet.mExploreListingHandler
										.sendEmptyMessage(1002);
							}
						}
					} else {
						if (!bIsTablet) {
							intent = new Intent(getActivity(),
									EventDetailsActivity.class);
							intent.putExtra("PAGETITLE", "Events");
							getActivity().startActivity(intent);
						} else {
							displayView(2, "Events");
						}
					}

				} else {
					ExploreListingActivity.selectedModel = venueList.get(arg2);
					if (bIsTablet) {
						if (ExploreTabParentFragmentTablet.mExploreListingHandler != null) {
							ExploreTabParentFragmentTablet.mExploreListingHandler
									.sendEmptyMessage(1002);
						}
					} else {
						if (ExploreListingActivity.mExploreListingHandler != null) {
							ExploreListingActivity.mExploreListingHandler
									.sendEmptyMessage(1002);
						}
					}
				}
				if (bIsTablet && fragment != null) {
					// fragment.setArguments(bundle);
					mFragmentManager = getParentFragment()
							.getChildFragmentManager();
					mFragmentManager
							.beginTransaction()
							.addToBackStack(
									ExploreListingFragment.class.getName())
							.replace(R.id.frame_container_parent, fragment,
									fragmentName)
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_FADE)
							.commit();
				}
			}
		});

		rlGuidedTours.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!bIsTablet) {
					Intent intent = new Intent(getActivity(),
							StaticTrailsActivity.class);
					intent.putExtra("PAGETITLE", "Guided Tours");
					startActivity(intent);
				} else {
					String szTitleName = "Guided Tours";
					displayView(1, szTitleName);
				}
			}
		});

		rlBoatTrails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!bIsTablet) {
					Intent intent = new Intent(getActivity(),
							StaticTrailsActivity.class);
					intent.putExtra("PAGETITLE", "Boat Trails");
					startActivity(intent);
				} else {
					String szTitleName = "Boat Trails";
					displayView(1, szTitleName);
				}
			}
		});

		return rootView;
	}

	private void displayView(int position, String szPagetitle) {
		Bundle bundle = new Bundle();
		bundle.putString("PAGETITLE", szPagetitle);

		switch (position) {
		case 1:
			fragment = (Fragment) new StaticTrailsDummyFragmentTablet();
			fragment.setArguments(bundle);
			break;

		case 2:
			fragment = (Fragment) new EventDetailsFragmentTablet();
			fragment.setArguments(bundle);
			break;
		}
		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					.addToBackStack(ExploreListingFragment.class.getName())
					.replace(R.id.frame_container_parent, fragment,
							StaticTrailsDummyFragmentTablet.class.getName())
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

	public void onGetDataFromDB() {
		try {
			if (pageTitle.equals("Venues & Attractions")) {
				venueList = LLDCApplication.DBHelper.onGetVenueData("");
			} else if (pageTitle.equals("Trails")) {
				bbar_layout.setVisibility(View.VISIBLE);
				venueList = LLDCApplication.DBHelper.onGetTrailsData("");
			} else if (pageTitle.equals("Facilities")) {
				venueList = LLDCApplication.DBHelper.onGetFacilitiesData("");
			} else {
				venueList = getLondonTrailsList();
			}
			/* set explore adapter */
			if (venueList != null) {
				listAdapter = new VenuesAndAttractionsAdapter(getActivity(),
						venueList, pageTitle);
				listVenuesAttractions.setAdapter(listAdapter);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
