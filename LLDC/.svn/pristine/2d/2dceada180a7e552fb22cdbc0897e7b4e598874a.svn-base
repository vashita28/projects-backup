package co.uk.android.lldc.fragments;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.RecommendationListingAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.fragments.tablet.RecommendationExploreDetailsFragment;
import co.uk.android.lldc.fragments.tablet.RecommendationsEventDetailsFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;

public class RecommendationsListingFragment extends Fragment {
	private static final String TAG = RecommendationsListingFragment.class
			.getSimpleName();

	ArrayList<ServerModel> recommandList = new ArrayList<ServerModel>();

	GridView events_list;

	TextView list_empty_text;

	FragmentManager mFragmentManager;
	Fragment fragment = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;
		Log.e(TAG, "Device is Tablet...");
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(
				R.layout.fragment_recommendationlisitings_tablet, container,
				false);

		// ((HomeActivityTablet) getActivity()).showBackButton();
		// ((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(true);
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getParentFragment().getChildFragmentManager()
								.popBackStack();
						((HomeActivityTablet) getActivity()).hideBackButton();

						// returnBackStackImmediate(getChildFragmentManager());
						// Log.e(TAG, "RecommendationsListingFragment back   "
						// + Constants.PageTitle);

						// boolean fragmentPopped = getParentFragment()
						// .getChildFragmentManager().popBackStackImmediate();

						// if(fragmentPopped)
						// getParentFragment()
						// .getChildFragmentManager().popBackStack();

						// if (Constants.bIfRecommendationAdded) {
						// Log.e(TAG,
						// "Back parent fragmet: "+getParentFragment().getClass().toString());
						// Log.e(TAG,
						// "Back child fragmet manager: "+getChildFragmentManager().getClass().toString());
						// Log.e(TAG,
						// "Back parentfragment->child fragmet manager: "+getParentFragment().getChildFragmentManager().getClass().toString());
						// Log.e(TAG,
						// "Back child fragmet manager: "+returnBackStackImmediate(getFragmentManager()));

						// if(getParentFragment().getChildFragmentManager().getBackStackEntryCount()>0){
						// Log.e(TAG,
						// "Back Added fragment yet: "+getParentFragment().getChildFragmentManager().getBackStackEntryCount());
						// for(int
						// i=0;i<getParentFragment().getChildFragmentManager().getBackStackEntryCount();i++)
						// Log.e(TAG,
						// "Back Added fragment NAMEs: "+i+"  :   "+getParentFragment().getChildFragmentManager().getBackStackEntryAt(i).getName());
						// }else{
						// Log.e(TAG,
						// "Back No fragment added yet: "+getParentFragment().getChildFragmentManager().getBackStackEntryCount());
						// }

						// Log.e(TAG,
						// "parent fragmet: "+getParentFragment().getChildFragmentManager().getFragments());
						// replaceFragment(getParentFragment());
						// returnBackStackImmediate(getChildFragmentManager());
						// getParentFragment().getChildFragmentManager()
						// .popBackStack();
						// Constants.bIfRecommendationAdded = false;
						// }
						// else {
						// Constants.bIfRecommendationAdded = false;
						// replaceFragment(getParentFragment());
						// }

						// getParentFragment().getChildFragmentManager()
						// .popBackStack();
						//
						// if (Constants.FragmentRecommendationBack
						// .equals(TheParkFragment.class.getSimpleName())) {
						// ((HomeActivityTablet) getActivity())
						// .hideBackButton();
						// ((HomeActivityTablet) getActivity()).tvHeaderTitle
						// .setText(Constants.PageTitle);
						// } else if (Constants.FragmentRecommendationBack
						// .equals(StaticTrailsFragment.class
						// .getSimpleName())) {
						// try {
						// if (((HomeActivityTablet)
						// getActivity()).tvHeaderTitle == null) {
						// ((HomeActivityTablet) getActivity()).tvHeaderTitle =
						// (TextView) ((HomeActivityTablet) getActivity())
						// .findViewById(R.id.tvPageTitle);
						// }
						// if (((HomeActivityTablet)
						// getActivity()).tvHeaderTitle != null) {
						// ((HomeActivityTablet) getActivity()).tvHeaderTitle
						// .setText(Constants.PageTitle);
						// }
						// } catch (Exception e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						//
						// }
						// if (Constants.FragmentRecommendationBack
						// .equals(StaticTrailsFragment.class
						// .getSimpleName())) {
						//
						// StaticTrailsFragment exploreListingFragment =
						// (StaticTrailsFragment) getParentFragment()
						// .getFragmentManager()
						// .findFragmentByTag(
						// StaticTrailsFragment.class
						// .getName());
						// getParentFragment()
						// .getFragmentManager()
						// .beginTransaction()
						// .replace(R.id.frame_container_parent,
						// exploreListingFragment).commit();

						// getParentFragment().getFragmentManager()
						// .popBackStack();
						// }else if(Constants.FragmentRecommendationBack
						// .equals(TheParkFragment.class
						// .getSimpleName())){
						// getParentFragment().getFragmentManager()
						// .popBackStack();
						// }
					}
				});

		events_list = (GridView) rootView
				.findViewById(R.id.recommendations_list);
		list_empty_text = (TextView) rootView
				.findViewById(R.id.list_empty_text);

		events_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				LLDCApplication.showSimpleProgressDialog(getActivity());
				LLDCApplication.selectedModel = recommandList.get(arg2);
				int i = LLDCApplication.selectedModel.getModelType();

				if (i == LLDCApplication.VENUE) {
					displayView(1, "Venues & Attractions");
				} else if (i == LLDCApplication.FACILITIES) {
					displayView(1, "Facilities");
				} else if (i == LLDCApplication.TRAILS) {
					displayView(1, "Trails");
				} else if (i == LLDCApplication.EVENT) {
					displayView(2, "Events");
				}
			}
		});

		onLoadDataFromDB();
		LLDCApplication.removeSimpleProgressDialog();

		return rootView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (((HomeActivityTablet) getActivity()).tvHeaderTitle == null)
			((HomeActivityTablet) getActivity()).tvHeaderTitle = (TextView) ((HomeActivityTablet) getActivity())
					.findViewById(R.id.tvPageTitle);

		((HomeActivityTablet) getActivity()).tvHeaderTitle
				.setText("Recommendations");
		((HomeActivityTablet) getActivity()).hideFooterData();
		((HomeActivityTablet) getActivity()).ivBack.setVisibility(View.GONE);
		((HomeActivityTablet) getActivity()).showCommonSearchIcon();
	}

	private void displayView(int position, String szPagetitle) {
		Bundle bundle = new Bundle();
		String fragmentName = "";

		switch (position) {

		case 1:
			fragment = (Fragment) new RecommendationExploreDetailsFragment();
			fragmentName = RecommendationExploreDetailsFragment.class.getName();
			break;

		case 2:
			fragment = (Fragment) new RecommendationsEventDetailsFragmentTablet();
			fragmentName = RecommendationsEventDetailsFragmentTablet.class
					.getName();
			break;

		}

		if (fragment != null) {
			fragment.setArguments(bundle);
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					.addToBackStack(TAG)
					.replace(R.id.frame_container_parent, fragment,
							fragmentName)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}
	}

	public void onLoadDataFromDB() {
		String pageTitle = "";
		Bundle bundle = new Bundle();
		bundle = getArguments();
		pageTitle = bundle.getString("RECOMM_PAGETITLE");
		Log.e(TAG, "pageTitle:> " + pageTitle);
		try {
			String where = "";
			if (pageTitle.equals("relax")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('1','3','5','7')";
			} else if (pageTitle.equals("entertain")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('2','3','6','7')";
			} else if (pageTitle.equals("active")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('4','5','6','7')";
			}
			// Venue, trails , facilities, events
			ArrayList<ServerModel> venueList1 = LLDCApplication.DBHelper
					.onGetVenueData(where);
			ArrayList<ServerModel> venueList2 = LLDCApplication.DBHelper
					.onGetTrailsData(where);
			ArrayList<ServerModel> venueList3 = LLDCApplication.DBHelper
					.onGetFacilitiesData(where);
			ArrayList<ServerModel> venueList4 = LLDCApplication.DBHelper
					.getEventData(where);
			recommandList.clear();
			for (int i = 0; i < venueList1.size(); i++) {
				recommandList.add(venueList1.get(i));
			}
			for (int i = 0; i < venueList2.size(); i++) {
				recommandList.add(venueList2.get(i));
			}
			for (int i = 0; i < venueList3.size(); i++) {
				recommandList.add(venueList3.get(i));
			}
			for (int i = 0; i < venueList4.size(); i++) {
				recommandList.add(venueList4.get(i));
			}

			if (recommandList.size() > 0) {
				events_list.setAdapter(new RecommendationListingAdapter(
						getActivity(), R.layout.recommendationlist_row,
						recommandList));
				list_empty_text.setVisibility(View.GONE);
			} else {
				list_empty_text.setVisibility(View.VISIBLE);
				events_list.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void replaceFragment(Fragment fragment) {
		try {
			String backStateName = fragment.getClass().getName();

			// FragmentManager manager =
			// getParentFragment().getChildFragmentManager();
			boolean fragmentPopped = getParentFragment()
					.getChildFragmentManager().popBackStackImmediate();// popBackStackImmediate(backStateName,
																		// 0);

			if (!fragmentPopped) { // fragment not in back stack, create it.

				getParentFragment().getChildFragmentManager()
						.beginTransaction()
						.replace(R.id.frame_container_parent, fragment)
						.commit();

				try {
					if (((HomeActivityTablet) getActivity()).tvHeaderTitle == null) {
						((HomeActivityTablet) getActivity()).tvHeaderTitle = (TextView) ((HomeActivityTablet) getActivity())
								.findViewById(R.id.tvPageTitle);
					}
					// if (((HomeActivityTablet) getActivity()).tvHeaderTitle !=
					// null) {
					// ((HomeActivityTablet) getActivity()).tvHeaderTitle
					// .setText(Constants.PageTitle);
					// }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// nBundle.putString("PAGETITLE", theParkList.get(index)
				// .getTitle());

				// FragmentTransaction ft = manager.beginTransaction();
				// ft.replace(R.id.frame_container_parent, fragment);
				// ft.addToBackStack(backStateName);
				// ft.commit();
			} else {
				getParentFragment().getChildFragmentManager()
						.popBackStackImmediate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean returnBackStackImmediate(FragmentManager fm) {
		List<Fragment> fragments = fm.getFragments();
		if (fragments != null && fragments.size() > 0) {
			for (Fragment fragment : fragments) {
				if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
					if (fragment.getChildFragmentManager()
							.popBackStackImmediate()) {
						return true;
					} else {
						return returnBackStackImmediate(fragment
								.getChildFragmentManager());
					}
				}
			}
		}
		return false;
	}
}
