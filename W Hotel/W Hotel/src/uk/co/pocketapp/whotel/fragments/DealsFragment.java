package uk.co.pocketapp.whotel.fragments;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.adapters.DealsAdapter;
import uk.co.pocketapp.whotel.customview.RecyclingImageView;
import uk.co.pocketapp.whotel.receivers.NetworkBroadcastReceiver;
import uk.co.pocketapp.whotel.util.Util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.mblox.engage.MbloxManager;
import com.mblox.engage.Message;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("ValidFragment")
public class DealsFragment extends SherlockFragment implements OnClickListener {

	Fragment mFragment_webview;
	FragmentLoad fragmentLoad;
	PullToRefreshListView mListView;
	ImageLoader imageLoader;

	String TAG = "DealsFragment";

	View dealsHeaderView;

	public static List<Message> messageDealsList;

	DealsAdapter dealsAdapter;
	RelativeLayout rel_banner;
	DisplayImageOptions options;

	boolean pauseOnScroll = false; // or true
	boolean pauseOnFling = true; // or false

	private int index = -1;
	private int top = 0;

	boolean bIsViewLoaded = false;
	ProgressBar progressDeals;

	OnRefreshListener<ListView> listRefreshListener;
	public boolean bIsSearchOngoing = false;

	ImageView imageviewDealsHeader;
	TextView textview_deals_banner_header, textview_deals_banner_desc,
			textview_read_more_banner;

	// ProgressBar progress_banner;

	public static Handler mDealsRefreshHandler;

	EasyTracker easyTracker;

	public DealsFragment(FragmentLoad fragmentLoad) {
		// TODO Auto-generated constructor stub
		this.fragmentLoad = fragmentLoad;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageLoader = ImageLoader.getInstance();

		// imageLoader.displayImage("drawable://" + R.drawable.deals1,
		// imageviewDealsHeader);

		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.placeholderlandscape)
				.showImageForEmptyUri(R.drawable.placeholderlandscape)
				.showImageOnFail(R.drawable.placeholderlandscape)
				.bitmapConfig(Bitmap.Config.RGB_565).build(); // ARGB_8888

		dealsHeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.header_deals, null);
		imageviewDealsHeader = (RecyclingImageView) dealsHeaderView
				.findViewById(R.id.imageview_deals_header);

		textview_deals_banner_header = (TextView) dealsHeaderView
				.findViewById(R.id.textview_deals_banner_header);
		textview_deals_banner_desc = (TextView) dealsHeaderView
				.findViewById(R.id.textview_deals_banner_desc);
		textview_read_more_banner = (TextView) dealsHeaderView
				.findViewById(R.id.textview_readmore_banner);
		// progress_banner = (ProgressBar) dealsHeaderView
		// .findViewById(R.id.progress_banner);

		rel_banner = (RelativeLayout) dealsHeaderView
				.findViewById(R.id.rel_header_row);

		try {
			messageDealsList = getSortedPriorityList(MbloxManager
					.getInstance(getActivity()).getInbox().getMessages());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDealsRefreshHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				bIsViewLoaded = false;
				super.handleMessage(msg);
			}
		};

		easyTracker = EasyTracker.getInstance(getActivity());

		if (easyTracker != null) {
			// This screen name value will remain set on the tracker and sent
			// with
			// hits until it is set to a new value or to null.
			easyTracker.set(Fields.SCREEN_NAME, "Deals");

			easyTracker.send(MapBuilder.createAppView().build());
		}
	}

	void setBanner() {
		try {
			if (messageDealsList != null && messageDealsList.size() > 0) {
				imageLoader.displayImage(messageDealsList.get(0)
						.getMessageBody().get("banner_image").toString(),
						imageviewDealsHeader, new SimpleImageLoadingListener() {
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

				textview_deals_banner_header.setText(messageDealsList.get(0)
						.getMessageBody().get("title").toString());
				textview_deals_banner_desc.setText(messageDealsList.get(0)
						.getMessageBody().get("desc").toString());
				// textview_read_more_banner.setOnClickListener(this);
				imageviewDealsHeader.setOnClickListener(this);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_deals, container, false);

		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_deals);
		mListView.getLoadingLayoutProxy().setLastUpdatedLabel("");
		mListView.getRefreshableView().addHeaderView(dealsHeaderView);

		mListView.getRefreshableView().setEmptyView(
				view.findViewById(android.R.id.empty));
		// W_Hotel_Application myApplication = (W_Hotel_Application)
		// getActivity()
		// .getApplication();

		progressDeals = (ProgressBar) view.findViewById(R.id.progress_deals);

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
			View v = mListView.getRefreshableView().getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		textview_read_more_banner.setText(getActivity().getString(
				R.string.read_more)
				+ " ");

		fragmentLoad.onDealsFragmentLoad(this);

		if (index != -1) {
			mListView.getRefreshableView().setSelectionFromTop(index, top);
		}

		if (messageDealsList != null && messageDealsList.size() > 0)
			rel_banner.setVisibility(View.VISIBLE);
		else
			rel_banner.setVisibility(View.GONE);

		applyScrollListener();

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

		if (messageDealsList != null && messageDealsList.size() == 0) {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				progressDeals.setVisibility(View.VISIBLE);
				mListView.getRefreshableView().getEmptyView()
						.setVisibility(View.GONE);
				fragmentLoad.refreshSyncMBlox();
			} else {
				progressDeals.setVisibility(View.GONE);
				mListView.getRefreshableView().setAdapter(null);
				Toast.makeText(getActivity(),
						"No network, please try after some time",
						Toast.LENGTH_LONG).show();
			}
			return;
		}

		dealsUpdateView();

	}

	void initRefreshListener() {

		listRefreshListener = new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// Do work to refresh the list here.
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					bIsViewLoaded = false;
					fragmentLoad.refreshSyncMBlox();
				} else {
					progressDeals.setVisibility(View.GONE);
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
		mListView.getRefreshableView().setOnScrollListener(
				new PauseOnScrollListener(imageLoader, pauseOnScroll,
						pauseOnFling));
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
			// bundleArgs.putString("webURL", "http://google.co.in");
			String szReadMoreURL = "http://dev.pocketapp.co.uk/dev/whotel_wifi/administrator/read_content?id=%s&table_type=deals";
			// bundleArgs.putString("webURL",
			// szReadMoreURL.format(szReadMoreURL,
			// messages.get(0).getId()));
			try {
				bundleArgs.putString("webURL", messageDealsList.get(0)
						.getMessageBody().getString("click_url"));
			} catch (Exception e) {
				bundleArgs.putString("webURL", "");
			}
			bundleArgs.putString("messageid", messageDealsList.get(0).getId()
					.toString());

			try {
				String szLat = "";
				if (messageDealsList.get(0).getMessageBody()
						.getJSONObject("app_data").has("lat"))
					szLat = messageDealsList.get(0).getMessageBody()
							.getJSONObject("app_data").getString("lat");

				String szLng = "";
				if (messageDealsList.get(0).getMessageBody()
						.getJSONObject("app_data").has("lng"))
					szLng = messageDealsList.get(0).getMessageBody()
							.getJSONObject("app_data").getString("lng");

				String szIsWHotel = "NO";
				if (messageDealsList.get(0).getMessageBody()
						.getJSONObject("app_data").has("is_whotel"))
					szIsWHotel = messageDealsList.get(0).getMessageBody()
							.getJSONObject("app_data").getString("is_whotel");

				bundleArgs.putString("lat", szLat);
				bundleArgs.putString("lng", szLng);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			dealsListSearch("");
		}
	}

	public void dealsUpdateView() {
		Log.d(TAG, "dealsUpdateView()");

		List<Message> sortedDeals = null;
		try {
			sortedDeals = getSortedPriorityList(MbloxManager
					.getInstance(getActivity()).getInbox().getMessages());
			Log.d("DealsFragment", "dealsUpdateView - SORTEDD LIST SIZE:: "
					+ sortedDeals.size());

			if (sortedDeals != null && messageDealsList != null
					&& sortedDeals.size() != messageDealsList.size()) {
				bIsViewLoaded = false;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			bIsViewLoaded = false;
		}

		try {
			listRefreshComplete();
		} catch (Exception e) {

		}

		if (!bIsViewLoaded) {
			if (!bIsSearchOngoing && sortedDeals != null
					&& sortedDeals.size() > 0) {
				dealsAdapter = new DealsAdapter(sortedDeals, getActivity(),
						this, fragmentLoad);
				mListView.setAdapter(dealsAdapter);
				messageDealsList = sortedDeals;
				setBanner();
			} else {
				mListView.setAdapter(null);
			}
			// mListView.setAdapter(dealsAdapter);

			if (mListView.getRefreshableView() != null
					&& mListView.getRefreshableView().getCount() > 0) {
				bIsViewLoaded = true;
			} else
				mListView.getRefreshableView().getEmptyView()
						.setVisibility(View.VISIBLE);
		}
		if (!bIsSearchOngoing) {
			if (progressDeals != null) {
				Handler handler = new Handler();
				Runnable r = new Runnable() {
					public void run() {
						progressDeals.setVisibility(View.GONE);
					}
				};
				handler.postDelayed(r, 100);
			}
			rel_banner.setVisibility(View.VISIBLE);
		} else {
			rel_banner.setVisibility(View.GONE);
		}
	}

	private List<Message> getSortedPriorityList(Collection<Message> entries)
			throws Exception {
		LinkedList<Message> list = new LinkedList<Message>(entries);
		// Log.v("","Sorting::Before   "+list.toString());
		Collections.sort(list, new DealComparator());
		// Log.v("","Sorting::After   "+list.toString());
		return list;
	}

	class DealComparator implements Comparator<Message> {

		@Override
		public int compare(Message lhs, Message rhs) {

			try {

				Double lhsLat = Double.valueOf(lhs.getMessageBody()
						.getJSONObject("app_data").getString("lat"));
				Double lhsLng = Double.valueOf(lhs.getMessageBody()
						.getJSONObject("app_data").getString("lng"));

				Location currentLocation = Util.getLocation();

				if (currentLocation == null) {
					currentLocation = new Location(
							LocationManager.NETWORK_PROVIDER);
					currentLocation.setLatitude(33.44563834);
					currentLocation.setLongitude(-84.23167366);
				}

				Location first_locationB = new Location(currentLocation);

				first_locationB.setLatitude(lhsLat);
				first_locationB.setLongitude(lhsLng);

				float distance = currentLocation.distanceTo(first_locationB);

				Double rhsLat = Double.valueOf(rhs.getMessageBody()
						.getJSONObject("app_data").getString("lat"));
				Double rhsLng = Double.valueOf(rhs.getMessageBody()
						.getJSONObject("app_data").getString("lng"));

				Location second_locationB = new Location(currentLocation);

				second_locationB.setLatitude(rhsLat);
				second_locationB.setLongitude(rhsLng);

				float second_distance = currentLocation
						.distanceTo(second_locationB);

				if (distance > second_distance) {
					return 1;
				} else if (distance < second_distance) {
					return -1;
				} else {
					return 0;
				}

			} catch (Exception e) {
				return -1;
			}

		}
	}

	// private List<Message> getSortedPriorityList(Collection<Message> entries)
	// throws Exception {
	// LinkedList<Message> list = new LinkedList<Message>(entries);
	// Collections.sort(list, DESCENDING_PRIORITY);
	//
	// List<Message> duplicateListWithSort = new ArrayList<Message>();
	// duplicateListWithSort.add(list.get(0));
	//
	// list.remove(0);
	// Collections.sort(list, DESCENDING_MESSAGE_ID);
	// duplicateListWithSort.addAll(list);
	//
	// return duplicateListWithSort;
	// }
	//
	// private final Comparator<Message> DESCENDING_MESSAGE_ID = Collections
	// .reverseOrder(new Comparator<Message>() {
	//
	// public int compare(Message lhs, Message rhs) {
	// return lhs.getReceivedDate().compareTo(
	// rhs.getReceivedDate());
	// }
	//
	// });
	//
	// private final Comparator<Message> DESCENDING_PRIORITY = Collections
	// .reverseOrder(new Comparator<Message>() {
	//
	// public int compare(Message lhs, Message rhs) {
	// int LHS = 0, RHS = 0;
	// Date LHSDate, RHSDate;
	// LHSDate = lhs.getReceivedDate();
	// RHSDate = rhs.getReceivedDate();
	// try {
	// LHS = lhs.getMessageBody().getInt("priority");
	// } catch (Exception e) {
	//
	// }
	// try {
	// RHS = rhs.getMessageBody().getInt("priority");
	// } catch (Exception e) {
	//
	// }
	//
	// if (LHS == RHS) {
	// if (LHSDate.after(RHSDate))
	// return 1;
	// if (LHSDate.before(RHSDate))
	// return -1;
	// return 0;
	// }
	//
	// if (LHS > RHS)
	// return 1;
	// if (LHS < RHS)
	// return -1;
	// return 0;
	// }
	//
	// });

	public void dealsListSearch(CharSequence s) {
		// Log.d(TAG, "dealsListSearch()*** SEARCH TEXT " + s);
		if (dealsAdapter != null)
			dealsAdapter.getFilter().filter(s);

		if (s == null || s.length() == 0) {
			dealsHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.VISIBLE);
			bIsSearchOngoing = false;
		} else {
			dealsHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.GONE);
			bIsSearchOngoing = true;
		}

	}

	public void showNoMatchFoundDialog() {

		mListView.getRefreshableView().getEmptyView()
				.setVisibility(View.INVISIBLE);

		new AlertDialog.Builder(getActivity()).setMessage("No match found")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dealsListSearch("");
						fragmentLoad.emptySearchText();
						dialog.cancel();
					}
				}).create().show();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		bIsViewLoaded = false;
	}

}
