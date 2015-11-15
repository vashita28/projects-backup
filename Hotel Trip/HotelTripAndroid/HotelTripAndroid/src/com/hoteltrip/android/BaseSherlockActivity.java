package com.hoteltrip.android;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.hoteltrip.android.fragment.MyDialogFragment;
import com.hoteltrip.android.util.DateFetchedListener;

public class BaseSherlockActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void showMyDialog(int id, DateFetchedListener dateFetchedListener) {
		MyDialogFragment dialogFragment = MyDialogFragment.newInstance(id, 0);
		dialogFragment.show(getSupportFragmentManager(), "dialog");

		if (dateFetchedListener != null) {
			dialogFragment.setDateFetcher(dateFetchedListener);
		}

	}
}
