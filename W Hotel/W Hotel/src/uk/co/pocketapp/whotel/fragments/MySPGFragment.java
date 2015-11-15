package uk.co.pocketapp.whotel.fragments;

import uk.co.pocketapp.whotel.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MySPGFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_spg, container, false);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// View view = getView();
		// TextView txtHeader = (TextView)
		// view.findViewById(R.id.textview_header);
		// txtHeader.setText(getString(R.string.spg));
	}
}
