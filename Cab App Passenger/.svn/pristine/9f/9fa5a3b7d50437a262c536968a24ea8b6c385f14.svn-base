package com.example.cabapppassenger.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.CityListAdpater;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.CityListTask;

public class CabappLocation_fragment extends RootFragment implements
		OnCustomBackPressedListener {

	Context mContext;
	ImageView ivBack, iv_airport;
	Handler mHandler;
	ListView lv_citylist;
	View rootView;
	public static TextView tv_nocities;
	ArrayList<String> arr_citylist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_cabapplocation,
				container, false);
		mContext = getActivity();
		iv_airport = (ImageView) rootView.findViewById(R.id.iv_airport);
		iv_airport.setVisibility(View.GONE);
		arr_citylist = new ArrayList<String>();
		lv_citylist = (ListView) rootView.findViewById(R.id.mycityList);
		tv_nocities = (TextView) rootView.findViewById(R.id.tv_nofavadded);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					if (bundleData.containsKey("CityList")) {
						arr_citylist = bundleData
								.getStringArrayList("CityList");
						if (arr_citylist != null && arr_citylist.size() > 0)
							setCity_List(arr_citylist);
					} else {
						tv_nocities = (TextView) rootView
								.findViewById(R.id.tv_nofavadded);
						tv_nocities.setVisibility(View.VISIBLE);
					}
				}
			}
		};

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	public void setCity_List(ArrayList<String> arr_citylist) {
		// TODO Auto-generated method stub
		if (arr_citylist != null && arr_citylist.size() > 0) {
			lv_citylist.setAdapter(new CityListAdpater(mContext, arr_citylist,
					mHandler));
		} else {
			lv_citylist.setAdapter(null);
			tv_nocities = (TextView) rootView.findViewById(R.id.tv_nofavadded);
			tv_nocities.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable && mHandler != null
				&& arr_citylist.size() <= 0) {
			CityListTask task = new CityListTask(mContext);
			task.mHandler = mHandler;
			task.execute();
		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onPause() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		if (((MainFragmentActivity) getActivity()).nLastSelected == 4) {
			if (MainFragmentActivity.slidingMenu.isMenuShowing())
				MainFragmentActivity.slidingMenu.toggle();
			else
				((MainFragmentActivity) getActivity()).showQuitDialog();
		} else {
			getParentFragment().getChildFragmentManager().popBackStack();
		}
	}
}