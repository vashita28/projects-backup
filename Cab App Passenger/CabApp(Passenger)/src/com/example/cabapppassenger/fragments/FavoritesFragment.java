package com.example.cabapppassenger.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.FavouritesAdapter;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetFavouritesTask;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class FavoritesFragment extends RootFragment implements
		OnCustomBackPressedListener {

	ListView listview_favourites;
	FavouritesAdapter adapter;
	public static TextView tv_nofavadded;
	Handler handler;

	Context mContext;
	RelativeLayout rel_delete;
	PreBookParcelable pbParcelable;

	View view_sliddingmenu_topbar, view_backbtn;
	ImageView iv_back, iv_airport;
	Bundle from;
	ArrayList<Boolean> arr_edit_addresslist;

	ArrayList<String> arr_addresslist;

	Fragment previousFragment;

	public FavoritesFragment(PreBookParcelable pbParcelable) {
		super();
		this.pbParcelable = pbParcelable;

	}

	public FavoritesFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		from = getArguments();
		final View rootView = inflater.inflate(R.layout.fragements_favourites,
				container, false);
		mContext = getActivity();
		rel_delete = (RelativeLayout) rootView.findViewById(R.id.rel_delete);
		tv_nofavadded = (TextView) rootView.findViewById(R.id.tv_nofavadded);
		listview_favourites = (ListView) rootView.findViewById(R.id.myfavList);
		arr_addresslist = new ArrayList<String>();

		view_sliddingmenu_topbar = (View) rootView
				.findViewById(R.id.inc_topbar);
		view_backbtn = (View) rootView.findViewById(R.id.inc_header);
		if (((MainFragmentActivity) getActivity()).nLastSelected == 3) {
			view_backbtn.setVisibility(View.GONE);
			view_sliddingmenu_topbar.setVisibility(View.VISIBLE);
			iv_airport = (ImageView) rootView.findViewById(R.id.iv_airport);
			iv_airport.setVisibility(View.GONE);

		} else {
			view_backbtn.setVisibility(View.VISIBLE);
			view_sliddingmenu_topbar.setVisibility(View.GONE);
			if (MainFragmentActivity.slidingMenu != null)
				MainFragmentActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			iv_back = (ImageView) rootView.findViewById(R.id.ivBack);
			iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

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
							.popBackStack();
				}
			});
		}
		tv_nofavadded.setVisibility(View.VISIBLE);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;
					arr_addresslist.clear();
					if (bundleData.containsKey("Addresslist")) {
						arr_addresslist = bundleData
								.getStringArrayList("Addresslist");
						// SAndip Edited
						arr_edit_addresslist = new ArrayList<Boolean>();
						arr_edit_addresslist.clear();
						for (int i = 0; i < arr_addresslist.size(); i++) {
							arr_edit_addresslist.add(false);
						}

						if (arr_addresslist != null
								&& arr_addresslist.size() > 0) {
							adapter = new FavouritesAdapter(mContext,
									arr_addresslist, arr_edit_addresslist,
									handler);
							listview_favourites.setAdapter(adapter);
							Log.i("Array List Size",
									"" + arr_addresslist.size());
						}

						else {
							tv_nofavadded = (TextView) rootView
									.findViewById(R.id.tv_nofavadded);
							tv_nofavadded.setVisibility(View.VISIBLE);
						}

					}

				}
			}
		};

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

			GetFavouritesTask recent = new GetFavouritesTask(mContext);
			recent.mHandler = handler;
			recent.execute("");

		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
		// }

		listview_favourites
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub

						adapter.onLongPress(arg2);

						return true;
					}

				});

		if (((MainFragmentActivity) getActivity()).nLastSelected != 3) {

			listview_favourites
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long arg3) {
							// TODO Auto-generated method stub
							String address = listview_favourites
									.getItemAtPosition(position).toString();
							String newaddress = address.substring(0,
									address.lastIndexOf(",")).trim();
							Log.i("Clicked Item", newaddress);
							Log.i("Clicked Item LatLng",
									""
											+ Constant.hash_latng_favourites
													.get(newaddress));
							if (newaddress != null) {
								LatLng laatlng = Constant.hash_latng_favourites
										.get(newaddress);
								if (laatlng != null) {

									if (from.containsKey("PFavourites")) {
										pbParcelable
												.setDefaultPickUpLocation(new LocationParcelable());
										pbParcelable
												.setePickUpSelected(EnumLocations.FAVOURITE);
										pbParcelable
												.getFavouritePickUpLocation()
												.setAddress1(
														address.substring(
																0,
																address.lastIndexOf(","))
																.trim());
										pbParcelable
												.getFavouritePickUpLocation()
												.setPinCode(
														address.substring(
																address.lastIndexOf(",") + 1,
																address.length())
																.trim());
										Log.e("Favourite Postcode",
												address.substring(
														address.lastIndexOf(",") + 1,
														address.length())
														.trim());
										pbParcelable
												.getFavouritePickUpLocation()
												.setLatitude(laatlng.latitude);
										pbParcelable
												.getFavouritePickUpLocation()
												.setLongitude(laatlng.longitude);
										/*
										 * pbParcelable
										 * .setStateSelected("Favourites");
										 */

										try {
											String backStackName = getParentFragment()
													.getChildFragmentManager()
													.getBackStackEntryAt(
															getParentFragment()
																	.getChildFragmentManager()
																	.getBackStackEntryCount() - 1)
													.getName();
											previousFragment = getParentFragment()
													.getChildFragmentManager()
													.findFragmentByTag(
															backStackName);

										} catch (Exception e) {
											Log.i(getClass().getSimpleName(),
													e.getMessage() == null ? "exception"
															: e.getMessage());
										}

										getParentFragment()
												.getChildFragmentManager()
												.popBackStackImmediate();
									} else if (from.containsKey("DFavourites")) {
										pbParcelable
												.setDefaultDropOffLocation(new LocationParcelable());

										pbParcelable
												.seteDropOffSelected(EnumLocations.FAVOURITE);

										pbParcelable
												.getFavouriteDropOffLocation()
												.setAddress1(
														address.substring(
																0,
																address.lastIndexOf(","))
																.trim());
										/*
										 * pbParcelable
										 * .setStateSelected("Favourites");
										 */
										pbParcelable
												.getFavouriteDropOffLocation()
												.setPinCode(
														address.substring(
																address.lastIndexOf(",") + 1,
																address.length())
																.trim());
										Log.e("Favourite Postcode",
												address.substring(
														address.lastIndexOf(",") + 1,
														address.length())
														.trim());
										Log.e("Favourite Address",
												address.substring(
														0,
														address.lastIndexOf(","))
														.trim());
										pbParcelable
												.getFavouriteDropOffLocation()
												.setLatitude(laatlng.latitude);
										pbParcelable
												.getFavouriteDropOffLocation()
												.setLongitude(laatlng.longitude);

										try {
											String backStackName = getParentFragment()
													.getChildFragmentManager()
													.getBackStackEntryAt(
															getParentFragment()
																	.getChildFragmentManager()
																	.getBackStackEntryCount() - 1)
													.getName();
											previousFragment = getParentFragment()
													.getChildFragmentManager()
													.findFragmentByTag(
															backStackName);

										} catch (Exception e) {
											Log.i(getClass().getSimpleName(),
													e.getMessage() == null ? "exception"
															: e.getMessage());
										}

										getParentFragment()
												.getChildFragmentManager()
												.popBackStackImmediate();
									}
								} else {
									// Need to write code chk with iOS
									Toast.makeText(
											mContext,
											"Error fetching details for this location.",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});

		}

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

		// pbParcelable.setHailNowPopUpVisible(true);
		if (!Constant.flag_fromslider_favourites) {
			/*
			 * if (MainFragmentActivity.iFromScreen == 0) fPreBook =
			 * getParentFragment().getChildFragmentManager()
			 * .findFragmentByTag("preBookFragment1"); else fPreBook =
			 * getParentFragment().getChildFragmentManager()
			 * .findFragmentByTag("preBookFragment"); try {
			 * 
			 * ((Pre_BookFragment)
			 * previousFragment).updateOnMapValues(pbParcelable); } catch
			 * (Exception e) { try { ((Hail_NowFragment) previousFragment)
			 * .updateHailNowData(pbParcelable); } catch (Exception b) {
			 * 
			 * } e.printStackTrace(); }
			 */
		}
		super.onDetach();
	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		if (((MainFragmentActivity) getActivity()).nLastSelected == 3) {
			if (MainFragmentActivity.slidingMenu.isMenuShowing())
				MainFragmentActivity.slidingMenu.toggle();
			else
				((MainFragmentActivity) getActivity()).showQuitDialog();
		} else {
			getParentFragment().getChildFragmentManager().popBackStack();
		}
	}

}