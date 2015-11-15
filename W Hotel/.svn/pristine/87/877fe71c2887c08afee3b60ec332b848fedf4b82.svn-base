package uk.co.pocketapp.whotel.fragments;

import java.util.ArrayList;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.adapters.FilterAdapter;
import uk.co.pocketapp.whotel.adapters.HotelAdapter;
import uk.co.pocketapp.whotel.customview.RecyclingImageView;
import uk.co.pocketapp.whotel.receivers.NetworkBroadcastReceiver;
import uk.co.pocketapp.whotel.tasks.JsonParserTaskGetHotel;
import uk.co.pocketapp.whotel.util.CustomHotelData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("ValidFragment")
public class HotelFragment extends SherlockFragment implements OnClickListener {

	Fragment mFragment_webview;

	FragmentLoad fragmentLoad;

	String TAG = "HotelFragment";

	View hotelHeaderView;
	ImageView imageviewhotelHeader;
	TextView textview_hotel_banner_header, textview_hotel_banner_desc,
			textview_read_more_banner;

	// ProgressBar progress_banner;

	HotelAdapter hotelAdapter;
	RelativeLayout rel_banner;

	DisplayImageOptions options;

	PullToRefreshListView mListView;
	ProgressBar progress_hotel;

	boolean pauseOnScroll = false; // or true
	boolean pauseOnFling = true; // or false

	private int index = -1;
	private int top = 0;

	boolean bIsViewLoaded = false;

	public static ArrayList<CustomHotelData> hotelDataList;

	OnRefreshListener<ListView> listRefreshListener;
	public boolean bIsSearchOngoing = false;

	RelativeLayout relative_filterHeader;
	ListView listFilterItems;
	// ImageView imageview_filterHeader;
	TextView textview_filterHeader;

	EasyTracker easyTracker;

	public HotelFragment(FragmentLoad fragmentLoad) {
		// TODO Auto-generated constructor stub
		this.fragmentLoad = fragmentLoad;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.placeholderlandscape)
				.showImageForEmptyUri(R.drawable.placeholderlandscape)
				.showImageOnFail(R.drawable.placeholderlandscape)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		hotelHeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.header_deals, null);
		imageviewhotelHeader = (RecyclingImageView) hotelHeaderView
				.findViewById(R.id.imageview_deals_header);

		textview_hotel_banner_header = (TextView) hotelHeaderView
				.findViewById(R.id.textview_deals_banner_header);
		textview_hotel_banner_desc = (TextView) hotelHeaderView
				.findViewById(R.id.textview_deals_banner_desc);
		textview_read_more_banner = (TextView) hotelHeaderView
				.findViewById(R.id.textview_readmore_banner);
		// progress_banner = (ProgressBar) hotelHeaderView
		// .findViewById(R.id.progress_banner);

		rel_banner = (RelativeLayout) hotelHeaderView
				.findViewById(R.id.rel_header_row);

		easyTracker = EasyTracker.getInstance(getActivity());

		if (easyTracker != null) {
			// This screen name value will remain set on the tracker and sent
			// with
			// hits until it is set to a new value or to null.
			easyTracker.set(Fields.SCREEN_NAME, "Hotel");

			easyTracker.send(MapBuilder.createAppView().build());
		}
	}

	public HotelFragment() {
		// TODO Auto-generated constructor stub
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hotel, container, false);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_hotel);
		mListView.getLoadingLayoutProxy().setLastUpdatedLabel("");
		mListView.getRefreshableView().addHeaderView(hotelHeaderView);

		mListView.getRefreshableView().setEmptyView(
				view.findViewById(android.R.id.empty));

		progress_hotel = (ProgressBar) view.findViewById(R.id.progress_insider);
		progress_hotel.setVisibility(View.VISIBLE);

		listFilterItems = (ListView) view.findViewById(R.id.filterList);
		listFilterItems.setVisibility(View.GONE);
		relative_filterHeader = (RelativeLayout) view
				.findViewById(R.id.rel_filter_header);
		// imageview_filterHeader = (ImageView) view
		// .findViewById(R.id.imageview_filter_header);
		textview_filterHeader = (TextView) view
				.findViewById(R.id.textview_filter_header);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			bIsSearchOngoing = false;
			index = mListView.getRefreshableView().getFirstVisiblePosition();
			View v = mListView.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		textview_read_more_banner.setText(getActivity().getString(
				R.string.read_more)
				+ " ");

		fragmentLoad.onHotelFragmentLoad(this);
		// dealsUpdateView();

		// Set a listener to be invoked when the list should be refreshed.
		initRefreshListener();

		mListView
				.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {

					@Override
					public void onPullEvent(
							PullToRefreshBase<ListView> refreshView,
							State state, Mode direction) {
						if (state.equals(State.PULL_TO_REFRESH)) {
						}
					}

				});
		mListView.setOnRefreshListener(listRefreshListener);

		if (hotelDataList != null) {
			progress_hotel.setVisibility(View.GONE);
			onTaskCompleted(hotelDataList, false);
		} else {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				progress_hotel.setVisibility(View.VISIBLE);
				mListView.getRefreshableView().getEmptyView()
						.setVisibility(View.GONE);
				JsonParserTaskGetHotel jsonParserHotspottask = new JsonParserTaskGetHotel(
						getActivity(), HotelFragment.this);
				jsonParserHotspottask.execute();

			} else {
				progress_hotel.setVisibility(View.GONE);
				// mListView.getRefreshableView().getEmptyView()
				// .setVisibility(View.VISIBLE);
				mListView.getRefreshableView().setAdapter(null);
				Toast.makeText(getActivity(),
						"No network, please try after some time",
						Toast.LENGTH_LONG).show();
			}
		}

		if (index != -1) {
			mListView.getRefreshableView().setSelectionFromTop(index, top);
		}

		// if (insiderDataList != null && insiderDataList.size() > 0)
		// rel_banner.setVisibility(View.VISIBLE);
		// else
		// rel_banner.setVisibility(View.GONE);

		applyScrollListener();

		relative_filterHeader.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listFilterItems.getVisibility() == View.GONE) {
					listFilterItems.setVisibility(View.VISIBLE);
					ArrayList<String> items = new ArrayList<String>();
					String[] filter_list = (getResources()
							.getStringArray(R.array.hotel_filter_data));
					for (int i = 0; i < filter_list.length; i++) {
						items.add(filter_list[i]);
					}

					String szDeviceModel = android.os.Build.MODEL;
					// if (szDeviceModel != null
					// && szDeviceModel.equals("GT-I9300")) {
					// imageview_filterHeader.getLayoutParams().width = 470;
					// listFilterItems.getLayoutParams().width = 470;
					// } else if (szDeviceModel != null
					// && szDeviceModel.equals("Nexus 4")) {
					// imageview_filterHeader.getLayoutParams().width = 460;
					// listFilterItems.getLayoutParams().width = 460;
					// } else {
					// imageview_filterHeader.getLayoutParams().width = 350;
					// listFilterItems.getLayoutParams().width =
					// imageview_filterHeader.getWidth();
					listFilterItems.getLayoutParams().width = textview_filterHeader
							.getWidth();

					// }

					// try {
					// imageview_filterHeader
					// .setBackground(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_dropdown_top_bg));
					// } catch (Exception e) {
					// imageview_filterHeader
					// .setBackgroundDrawable(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_dropdown_top_bg));
					// }

					listFilterItems.setAdapter(new FilterAdapter(getActivity(),
							items));
				} else {
					if (textview_filterHeader.getText().toString()
							.equalsIgnoreCase("all")) {
						// imageview_filterHeader.getLayoutParams().width =
						// LayoutParams.WRAP_CONTENT;
						// try {
						// imageview_filterHeader
						// .setBackground(getResources()
						// .getDrawable(
						// R.drawable.whotel_search_list_white));
						// } catch (Exception e) {
						// imageview_filterHeader
						// .setBackgroundDrawable(getResources()
						// .getDrawable(
						// R.drawable.whotel_search_list_white));
						// }
					}
					listFilterItems.setVisibility(View.GONE);
				}
			}
		});

		listFilterItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				listFilterItems.setVisibility(View.GONE);

				TextView txtItem = (TextView) view
						.findViewById(R.id.textview_item);
				String szFilterValue = txtItem.getText().toString();

				if (hotelDataList != null)
					szFilterValue = txtItem.getText().toString()
							.replaceAll(" ", "");

				textview_filterHeader.setText(szFilterValue);
				locationSearchByFilter(szFilterValue);

				if (szFilterValue.equalsIgnoreCase("all")) {
					// imageview_filterHeader.getLayoutParams().width =
					// LayoutParams.WRAP_CONTENT;

					// try {
					// imageview_filterHeader.setBackground(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_white));
					// } catch (Exception e) {
					// imageview_filterHeader.setBackgroundDrawable(getResources()
					// .getDrawable(R.drawable.whotel_search_list_white));
					// }
					rel_banner.setVisibility(View.VISIBLE);
				} else
					rel_banner.setVisibility(View.GONE);
			}
		});
	}

	void initRefreshListener() {
		// Do work to refresh the list here.
		listRefreshListener = new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					JsonParserTaskGetHotel jsonParserHoteltask = new JsonParserTaskGetHotel(
							getActivity(), HotelFragment.this);
					jsonParserHoteltask.execute();
				} else {
					progress_hotel.setVisibility(View.GONE);
					listRefreshComplete();
					Toast.makeText(getActivity(),
							"No network, please try after some time",
							Toast.LENGTH_LONG).show();
				}
			}
		};
	}

	void listRefreshComplete() {
		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				mListView.onRefreshComplete();
			}
		};
		handler.postDelayed(r, 100);
	}

	private void applyScrollListener() {
		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), pauseOnScroll, pauseOnFling));
	}

	public void onTaskCompleted(ArrayList<CustomHotelData> hoteldata,
			boolean isViewToBeRefreshed) {
		if (progress_hotel != null) {

			Handler handler = new Handler();
			Runnable r = new Runnable() {
				public void run() {
					progress_hotel.setVisibility(View.GONE);
				}
			};
			handler.postDelayed(r, 100);
		}

		if (isViewToBeRefreshed)
			bIsViewLoaded = false;

		try {
			Log.d(TAG, "onTaskCompleted()");

			listRefreshComplete();

			if (hoteldata != null && hoteldata.size() > 0) {
				HotelFragment.hotelDataList = hoteldata;
				rel_banner.setVisibility(View.VISIBLE);
				// textview_read_more_banner.setOnClickListener(this);
				imageviewhotelHeader.setOnClickListener(this);

				String imgUrl = hotelDataList.get(0).getRectlarge().toString();

				ImageLoader.getInstance().displayImage(imgUrl,
						imageviewhotelHeader, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// progress_banner.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// progress_banner.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// progress_banner.setVisibility(View.GONE);
							}
						});

				textview_hotel_banner_header.setText(hotelDataList.get(0)
						.getH_name());
				textview_hotel_banner_desc.setText(hotelDataList.get(0)
						.getH_desc());

				if (!bIsViewLoaded) {
					if (!bIsSearchOngoing)
						hotelAdapter = new HotelAdapter(hotelDataList,
								getActivity(), this, fragmentLoad);
					// mListView.setAdapter(hotelAdapter);

					if (hotelAdapter != null && hotelAdapter.getCount() > 0)
						bIsViewLoaded = true;
				}
			} else if (hotelAdapter != null && hotelAdapter.getCount() == 0) {
				mListView.getRefreshableView().setAdapter(null);
				rel_banner.setVisibility(View.GONE);
			}
			if (!bIsSearchOngoing) {
				progress_hotel.setVisibility(View.GONE);
				mListView.setAdapter(hotelAdapter);
				rel_banner.setVisibility(View.VISIBLE);
			} else {
				rel_banner.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void locationSearchByFilter(CharSequence szFilterValue) {
		if (hotelAdapter != null) {
			HotelAdapter.isCategoryFilterSearch = true;
			hotelAdapter.getFilter().filter(szFilterValue);
		}
	}

	public void hotelListSearch(CharSequence s) {
		// Log.d(TAG, "hotelListSearch()*** SEARCH TEXT " + s);
		HotelAdapter.isCategoryFilterSearch = false;

		if (hotelAdapter != null)
			hotelAdapter.getFilter().filter(s);

		if (s == null || s.length() == 0) {
			hotelHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.VISIBLE);
			bIsSearchOngoing = false;
		} else {
			hotelHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.GONE);
			bIsSearchOngoing = true;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		bIsViewLoaded = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle bundleArgs;
		switch (v.getId()) {
		case R.id.imageview_deals_header:
			mFragment_webview = WebViewFragment.newInstance();
			WebViewFragment.fragmentLoad = fragmentLoad;
			bundleArgs = new Bundle();
			bundleArgs.putString("webURL", hotelDataList.get(0).getPage_url()
					.toString());
			bundleArgs.putString("messageid", hotelDataList.get(0).getId()
					.toString());
			bundleArgs.putString("lat", hotelDataList.get(0).getH_lati());
			bundleArgs.putString("lng", hotelDataList.get(0).getH_long());
			mFragment_webview.setArguments(bundleArgs);
			switchFragment(mFragment_webview);
			break;
		default:
			break;
		}
	}

	public void switchFragment(Fragment fragment) {
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_frame, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.addToBackStack(null).commit();
	}

	public void onBackPressed() {
		if (mFragment_webview != null)
			((WebViewFragment) mFragment_webview).onBackPressed();
	}

	public void resetList() {
		if (bIsSearchOngoing) {
			hotelListSearch("");
		}
	}

	public void showNoMatchFoundDialog() {

		mListView.getRefreshableView().getEmptyView()
				.setVisibility(View.INVISIBLE);

		new AlertDialog.Builder(getActivity()).setMessage("No match found")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						hotelListSearch("");
						fragmentLoad.emptySearchText();
						dialog.cancel();

						textview_filterHeader.setText("ALL");
						// imageview_filterHeader.getLayoutParams().width =
						// LayoutParams.WRAP_CONTENT;
						// imageview_filterHeader
						// .setBackgroundDrawable(getResources()
						// .getDrawable(
						// R.drawable.whotel_search_list_white));
						listFilterItems.setVisibility(View.GONE);
					}
				}).create().show();
	}

}
