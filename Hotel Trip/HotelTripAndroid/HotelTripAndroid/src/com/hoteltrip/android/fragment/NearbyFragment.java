package com.hoteltrip.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.hoteltrip.android.MainActivity;
import com.hoteltrip.android.R;

public class NearbyFragment extends SherlockFragment {

	private String[] nearbyPlaces = { "Kuala Lumpur", "Bukit Bintang",
			"Starhill Gallery", "Pavilion", "Kuala Terengganu", "Pahang",
			"Negeri Sembilan" };

	private ListView nearbyList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_destination_nearby,
				container, false);

		nearbyList = (ListView) v.findViewById(R.id.nearbyList);
		nearbyList.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, nearbyPlaces));
		nearbyList.setOnItemClickListener(nearbyListItemClick);

		InputMethodManager mgr = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

		return v;
	}

	OnItemClickListener nearbyListItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Intent mainIntent = new Intent(getActivity(), MainActivity.class);
			mainIntent.putExtra("nearbyCriterion", parent.getAdapter()
					.getItem(position).toString());
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
		}
	};
}
