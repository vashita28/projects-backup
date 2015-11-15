package com.hoteltrip.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.hoteltrip.android.MainActivity;
import com.hoteltrip.android.R;
import com.hoteltrip.android.util.SearchTask;

public class PlaceSearchFragment extends SherlockFragment {

	private String[] places = { "Kuala Lumpur", "Bukit Bintang",
			"Starhill Gallery", "Pavilion", "Kuala Terengganu", "Pahang",
			"Negeri Sembilan" };

	private ArrayAdapter<String> placesAdapter;
	private ListView placeList;

	private InputMethodManager mgr;
	private static EditText searchTextBox;
	private static SearchTask searchTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_destination_placesearch,
				container, false);

		placeList = (ListView) v.findViewById(R.id.placeList);

		placesAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, places);
		placeList.setAdapter(placesAdapter);
		placeList.setOnItemClickListener(onPlaceItemClick);
		placeList.setVisibility(View.GONE);
		searchTextBox = (EditText) v.findViewById(R.id.searchTextBox);
		searchTextBox.addTextChangedListener(textWatcher);
		
		
		boolean checkFocus = searchTextBox.requestFocus();
		mgr = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(checkFocus){
			mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
		return v;
	}

	OnItemClickListener onPlaceItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			
			mgr.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			
			Intent mainIntent = new Intent(getActivity(), MainActivity.class);
			mainIntent.putExtra("placeCriterion", parent.getAdapter()
					.getItem(position).toString());
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
		}
	};

	// TODO: personal note, after the task returns a list of results, hook it up
	// with an array adapter, then display

	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (!s.equals(null) && s.length() >= 3) {
				placeList.setVisibility(View.VISIBLE);
				// TODO: if task is not null and is not cancelled,
				if (searchTask != null && !searchTask.isCancelled()) {
					searchTask.cancel(false);
				}

				searchTask = new SearchTask(getActivity(), searchTextBox
						.getText().toString());

			}

		}
	};

}
