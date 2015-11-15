package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.RecommendationListingAdapter;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.EventDetailsActivity;
import co.uk.android.lldc.tablet.ExploreListDetailsActivity;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.VenueActivity;

public class Recommendations_ListingFragment extends Fragment {

	ArrayList<ServerModel> recommandList = new ArrayList<ServerModel>();

	ListView events_list;

	TextView list_empty_text;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.fragment_recommendationlisings, container, false);
		events_list = (ListView) rootView
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
				Intent intent = new Intent(getActivity(),
						ExploreListDetailsActivity.class);
				int i = LLDCApplication.selectedModel.getModelType();
				if (i == LLDCApplication.VENUE) {
					intent = new Intent(getActivity(), VenueActivity.class);
					intent.putExtra("PAGETITLE", "Venues");
				} else if (i == LLDCApplication.FACILITIES) {
					intent.putExtra("PAGETITLE", "Facilities");
				} else if (i == LLDCApplication.TRAILS) {
					intent.putExtra("PAGETITLE", "Trails");
				} else if (i == LLDCApplication.EVENT) {
					intent = new Intent(getActivity(),
							EventDetailsActivity.class);
					intent.putExtra("PAGETITLE", "Events");
					// intent.putExtra("EVENTID",LLDCApplication.selectedModel.get_id());
				}
				startActivity(intent);
				// getActivity().finish();
			}
		});

		onLoadDataFromDB();
		LLDCApplication.removeSimpleProgressDialog();
		return rootView;

	}

	public void onLoadDataFromDB() {
		String pageTitle = getActivity().getIntent().getExtras()
				.getString("PAGETITLE");
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

			/* set explore adapter */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
