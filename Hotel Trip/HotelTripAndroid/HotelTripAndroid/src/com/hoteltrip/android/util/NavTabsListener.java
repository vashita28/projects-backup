package com.hoteltrip.android.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class NavTabsListener implements ActionBar.TabListener {

	private int containerResourceId;
	private SherlockFragmentActivity activity;
	private Fragment fragment;

	public NavTabsListener(int containerResourceId,
			SherlockFragmentActivity activity, Fragment fragment) {
		super();
		this.containerResourceId = containerResourceId;
		this.activity = activity;
		this.fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		ft.add(containerResourceId, fragment, null);
		fragmentTrans.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragment != null) {
			ft.remove(fragment);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// do nothing for now
		if (fragment != null) {
			ft.attach(fragment);
		}
	}
}
