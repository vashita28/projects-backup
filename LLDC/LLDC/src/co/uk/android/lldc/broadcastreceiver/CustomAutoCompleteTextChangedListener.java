package co.uk.android.lldc.broadcastreceiver;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.SearchActivity;
import co.uk.android.lldc.adapters.AutocompleteCustomArrayAdapter;
import co.uk.android.lldc.models.ServerModel;

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
	Context context;

	public CustomAutoCompleteTextChangedListener(Context context) {
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence userInput, int start, int before,
			int count) {

		try {

			// if you want to see in the logcat what the user types
			Log.e(TAG, "User input: " + userInput);
			
			if(userInput.length() < 3)
				return;

			SearchActivity mainActivity = ((SearchActivity) context);

			// update the adapater
			mainActivity.myAdapter.notifyDataSetChanged();

			ArrayList<String> filterarr = mainActivity.getFilterArray();

			ArrayList<String> showMeFilter = mainActivity
					.getShowMeFilterArray();

			// get suggestions from the database
			ArrayList<ServerModel> myObjs = LLDCApplication.DBHelper.search(
					userInput.toString(), filterarr, showMeFilter);

			if (myObjs.size() == 0) {
				ServerModel serverModel = new ServerModel();
				serverModel.set_id("-1");
				serverModel.setName("No result found.");
				myObjs.add(0, serverModel);
			}

			mainActivity.myAdapter = new AutocompleteCustomArrayAdapter(
					mainActivity, R.layout.autocomplete_row, myObjs,
					userInput.toString());

			mainActivity.etSearch.setAdapter(mainActivity.myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
