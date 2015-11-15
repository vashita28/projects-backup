/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.task;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.adapter.CustomPlacesSimpleAdapter;
import com.example.cabapppassenger.util.PlaceJSONParser;

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
		CustomPlacesSimpleAdapter adapter = new CustomPlacesSimpleAdapter(
				context, result, android.R.layout.simple_list_item_1, from, to);
		// Pre_BookFragment.atvAdapterResult = adapter;
		AutoCompleteTextView atvPickUp = null;
		AutoCompleteTextView atvDropOff = null;
		AutoCompleteTextView atvCity_Address = null;
		try {
			atvPickUp = ((AutoCompleteTextView) (((Activity) (context))
					.findViewById(R.id.atv_pick_up_address)));
			atvCity_Address = ((AutoCompleteTextView) (((Activity) (context))
					.findViewById(R.id.et_city)));
			atvDropOff = ((AutoCompleteTextView) (((Activity) (context))
					.findViewById(R.id.atv_drop_off_address)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (atvCity_Address != null)
			atvCity_Address.setAdapter(adapter);

		if (atvPickUp != null)
			if (atvPickUp.hasFocus())
				atvPickUp.setAdapter(adapter);
			else if (atvDropOff != null)
				atvDropOff.setAdapter(adapter);
		// else if (atvCity_Address != null)
		// atvCity_Address.setAdapter(adapter);
		// } else
		// Pre_BookFragment.atvAdapterResult.updateList(result);

		// ((AutoCompleteTextView)(((Activity)(context)).findViewById(R.id.atv_pick_up_address))).notifyDataSetChanged();

	}
}
