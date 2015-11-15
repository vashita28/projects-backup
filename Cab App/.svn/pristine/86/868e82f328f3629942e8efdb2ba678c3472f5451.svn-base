/*
 * Coded By Manpreet
 */

package com.android.cabapp.async;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.android.cabapp.R;
import com.android.cabapp.adapter.CustomPlacesSimpleAdapter;
import com.android.cabapp.util.PlaceJSONParser;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/** A class to parse the Google Places in JSON format */
public class AutoPlacesParserTask extends
		AsyncTask<String, Integer, List<HashMap<String, String>>> {

	JSONObject jObject;
	Context context;
	Activity activity;

	public AutoPlacesParserTask(Context context) {
		super();
		this.context = context;

	}

	@Override
	protected List<HashMap<String, String>> doInBackground(String... jsonData) {

		List<HashMap<String, String>> places = null;

		PlaceJSONParser placeJsonParser = new PlaceJSONParser();

		try {
			jObject = new JSONObject(jsonData[0]);

			// Getting the parsed data as a List construct
			places = placeJsonParser.parse(jObject);

		} catch (Exception e) {
			Log.d("Exception", e.toString());
		}
		Log.d("", "Places" + places);
		return places;
	}

	@Override
	protected void onPostExecute(List<HashMap<String, String>> result) {

		String[] from = new String[] { "description" };
		int[] to = new int[] { android.R.id.text1 };

		// Creating a SimpleAdapter for the AutoCompleteTextView
		// if (Pre_BookFragment.atvAdapterResult == null) {
		CustomPlacesSimpleAdapter adapter = new CustomPlacesSimpleAdapter(
				context, result, android.R.layout.simple_list_item_1, from, to);
		// Pre_BookFragment.atvAdapterResult = adapter;
		AutoCompleteTextView atvPickUp = null;
		AutoCompleteTextView atvDropOff = null;
		try {
			atvPickUp = ((AutoCompleteTextView) (((Activity) (context))
					.findViewById(R.id.autoCompleteTextPickUpAddress)));
			atvDropOff = ((AutoCompleteTextView) (((Activity) (context))
					.findViewById(R.id.autoCompleteTextDropOffAddress)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (atvPickUp != null && atvDropOff != null)
			if (atvPickUp.hasFocus())
				atvPickUp.setAdapter(adapter);
			else
				atvDropOff.setAdapter(adapter);
		// } else
		// Pre_BookFragment.atvAdapterResult.updateList(result);

		// ((AutoCompleteTextView)(((Activity)(context)).findViewById(R.id.atv_pick_up_address))).notifyDataSetChanged();

	}
}
