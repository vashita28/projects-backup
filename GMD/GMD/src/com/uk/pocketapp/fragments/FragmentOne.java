package com.uk.pocketapp.fragments;

import java.util.ArrayList;
import co.uk.pocketapp.gmd.R;
import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class FragmentOne extends Fragment {
	
	private static String mPath;
	private ArrayList<String> mDir = new ArrayList<String>();
	private TextView mTxt;

	/** fragment newInstance method */
	public static FragmentOne newInstance(String path) {

		mPath = path;
		FragmentOne f = new FragmentOne();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, it would just never be used.
			return null;
		}

		String abtpath = mPath;
		String[] pa = abtpath.split("/");

		for (int i = 1; i < pa.length; i++) {

			mDir.add(pa[i] + "/");
			//Log.e("paa", pa[i]);
		}

		ScrollView scroller = new ScrollView(getActivity());

		int ii = 10;
		final LinearLayout lay = new LinearLayout(getActivity());
		lay.setOrientation(LinearLayout.VERTICAL);
		lay.setPadding(0, 10, 0, 10);

		for (int i = 0; i < mDir.size(); i++) {

			Drawable myDrawable1 = getResources().getDrawable(
					R.drawable.directory_icon);
			Drawable myDrawable2 = getResources()
					.getDrawable(R.drawable.sdicon);

			mTxt = new TextView(getActivity());
			mTxt.setText(mDir.get(i));
			if (i == 0 || i == 1) {
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				llp.setMargins(ii, 10, 0, 0);
				mTxt.setCompoundDrawablesWithIntrinsicBounds(myDrawable2, null,
						null, null);
				mTxt.setLayoutParams(llp);
				mTxt.setCompoundDrawablePadding(10);
			} else {
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				llp.setMargins(ii, 0, 0, 0);
				mTxt.setCompoundDrawablesWithIntrinsicBounds(myDrawable1, null,
						null, null);
				mTxt.setLayoutParams(llp);
				mTxt.setCompoundDrawablePadding(5);
			}

			mTxt.setTextSize(16);
			mTxt.setTypeface(Typeface.DEFAULT_BOLD);
			mTxt.setTextColor(getResources().getColor(R.color.dcolor));
			mTxt.setId(i);

			lay.addView(mTxt);

			ii = ii + 20;

		}

		scroller.addView(lay);

		return scroller;
	}
}
