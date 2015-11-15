package com.example.cabapppassenger.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cabapppassenger.R;

public class FavoritesParentFragment extends RootFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
	}

	private ViewGroup mHailNowFrame;
	private View mView;
	private FragmentManager mFragmentManager;
	public static Fragment fragment = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.activity_main, container, false);
		mHailNowFrame = (ViewGroup) mView.findViewById(R.id.frame_container);
		showSubfragments(0, "favoritesFragment");
		super.initWidgets(mView, this.getClass().getName());
		return mView;

	}

	/**
	 * 
	 * @param position
	 *            0- Hail Now Screen 1- Booking Details 2 -Prebook 3 -Driver
	 *            Details 4 -Chat Fragment
	 * 
	 * @param backStackName
	 */
	public void showSubfragments(int position, String tagName) {
		Fragment fragment = null;
		mFragmentManager = this.getChildFragmentManager();
		FragmentTransaction fragtrans = mFragmentManager.beginTransaction();
		switch (position) {
		case 0:
			fragment = new com.example.cabapppassenger.fragments.FavoritesFragment();
			break;

		}

		if (fragment != null) {
			fragtrans.replace(R.id.frame_container, fragment, tagName);
			fragtrans.commit();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		fragment = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		fragment = null;
		super.onDetach();
	}
}
