package com.example.cabapppassenger.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.RecentAdapter;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetRecentTask;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.maps.model.LatLng;

public class RecentFragment extends RootFragment implements
		OnCustomBackPressedListener {

	ListView listview_recent;
	RecentAdapter adapter;
	public static TextView tv_norecentadded;
	Handler handler;
	Context mContext;
	PreBookParcelable pbParcelable;
	ImageView iv_back;
	Bundle from;
	Fragment previousFragment;

	ArrayList<String> arr_addresslist;

	public RecentFragment() {
		super();
	}

	public RecentFragment(PreBookParcelable pbParcelable) {
		super();
		this.pbParcelable = pbParcelable;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		from = getArguments();
		final View rootView = inflater.inflate(R.layout.fragment_recent,
				container, false);
		mContext = getActivity();
		tv_norecentadded = (TextView) rootView
				.findViewById(R.id.tv_norecentadded);
		listview_recent = (ListView) rootView.findViewById(R.id.recentList);

		arr_addresslist = new ArrayList<String>();
		iv_back = (ImageView) rootView.findViewById(R.id.ivBack);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String backStackName = getParentFragment()
							.getChildFragmentManager()
							.getBackStackEntryAt(
									getParentFragment()
											.getChildFragmentManager()
											.getBackStackEntryCount() - 1)
							.getName();
					previousFragment = getParentFragment()
							.getChildFragmentManager().findFragmentByTag(
									backStackName);

				} catch (Exception e) {
					Log.i(getClass().getSimpleName(),
							e.getMessage() == null ? "exception" : e
									.getMessage());
				}

				getParentFragment().getChildFragmentManager().popBackStack();
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;
					if (bundleData.containsKey("Addresslist")) {
						arr_addresslist = bundleData
								.getStringArrayList("Addresslist");
						if (arr_addresslist != null
								&& arr_addresslist.size() > 0) {
							listview_recent.setAdapter(new RecentAdapter(
									mContext, arr_addresslist, handler));
							Log.i("Array List Size",
									"" + arr_addresslist.size());
						}

						else {
							tv_norecentadded = (TextView) rootView
									.findViewById(R.id.tv_norecentadded);
							tv_norecentadded.setVisibility(View.VISIBLE);
						}

					}

				}
			}
		};

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

			GetRecentTask recent = new GetRecentTask(mContext);
			recent.mHandler = handler;
			recent.execute("");

		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}

		// listview_recent = (ListView) rootView.findViewById(R.id.recentList);

		// recentList = new ArrayList<GetterSetter>();
		// recentList.clear();

		// getAllRecent();

		// if (recentList != null && recentList.size() > 0) {
		// // if list is not null then iterate
		// adapter = new RecentAdapter(mContext, recentList, handler);
		// listview_recent.setAdapter(adapter);
		// } else {
		// // if list is null show no fav added text
		// listview_recent.setAdapter(null);
		// tv_norecentadded = (TextView) rootView
		// .findViewById(R.id.tv_nofavadded);
		// tv_norecentadded.setVisibility(View.VISIBLE);
		// }

		listview_recent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				String address = listview_recent.getItemAtPosition(position)
						.toString();
				Log.i("Clicked Item", address);
				Log.i("Clicked Item LatLng",
						"" + Constant.hash_latng_recent.get(address));

				LatLng laatlng = Constant.hash_latng_recent.get(address);
				Double lat = laatlng.latitude;
				if (from.containsKey("PRecent")) {
					pbParcelable.setePickUpSelected(EnumLocations.RECENT);
					pbParcelable
							.setDefaultPickUpLocation(new LocationParcelable());
					pbParcelable.getRecentPickUpLocation().setAddress1(
							address.substring(0, address.lastIndexOf(","))
									.trim());
					pbParcelable.getRecentPickUpLocation().setPinCode(
							address.substring(address.lastIndexOf(",") + 1,
									address.length()).trim());
					pbParcelable.getRecentPickUpLocation().setLatitude(
							laatlng.latitude);
					pbParcelable.getRecentPickUpLocation().setLongitude(
							laatlng.longitude);
					/* pbParcelable.setStateSelected("Recent"); */
					try {
						String backStackName = getParentFragment()
								.getChildFragmentManager()
								.getBackStackEntryAt(
										getParentFragment()
												.getChildFragmentManager()
												.getBackStackEntryCount() - 1)
								.getName();
						previousFragment = getParentFragment()
								.getChildFragmentManager().findFragmentByTag(
										backStackName);

					} catch (Exception e) {
						Log.i(getClass().getSimpleName(),
								e.getMessage() == null ? "exception" : e
										.getMessage());
					}

					getParentFragment().getChildFragmentManager()
							.popBackStackImmediate();
				} else if (from.containsKey("DRecent")) {
					pbParcelable
							.setDefaultDropOffLocation(new LocationParcelable());
					pbParcelable.seteDropOffSelected(EnumLocations.RECENT);
					/* pbParcelable.setStateSelected("Recent"); */
					pbParcelable.getRecentDropOffLocation().setAddress1(
							address.substring(0, address.lastIndexOf(","))
									.trim());
					pbParcelable.getRecentDropOffLocation().setPinCode(
							address.substring(address.lastIndexOf(",") + 1,
									address.length()).trim());
					pbParcelable.getRecentDropOffLocation().setLatitude(
							laatlng.latitude);
					pbParcelable.getRecentDropOffLocation().setLongitude(
							laatlng.longitude);
					try {
						String backStackName = getParentFragment()
								.getChildFragmentManager()
								.getBackStackEntryAt(
										getParentFragment()
												.getChildFragmentManager()
												.getBackStackEntryCount() - 1)
								.getName();
						previousFragment = getParentFragment()
								.getChildFragmentManager().findFragmentByTag(
										backStackName);

					} catch (Exception e) {
						Log.i(getClass().getSimpleName(),
								e.getMessage() == null ? "exception" : e
										.getMessage());
					}
					getParentFragment().getChildFragmentManager()
							.popBackStackImmediate();
				}

			}
		});

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		super.onResume();
	}

	@Override
	public void onPause() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onDetach() {

		/*
		 * Fragment fPreBook = getParentFragment().getChildFragmentManager()
		 * .findFragmentByTag("preBookFragment");
		 */
		/*
		 * try {
		 * 
		 * ((Pre_BookFragment)
		 * previousFragment).updateOnMapValues(pbParcelable); } catch (Exception
		 * e) { try { ((Hail_NowFragment)
		 * previousFragment).updateHailNowData(pbParcelable); } catch (Exception
		 * b) {
		 * 
		 * } e.printStackTrace(); }
		 */
		// pbParcelable.setHailNowPopUpVisible(true);

		super.onDetach();
	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		getParentFragment().getChildFragmentManager().popBackStack();

	}

}