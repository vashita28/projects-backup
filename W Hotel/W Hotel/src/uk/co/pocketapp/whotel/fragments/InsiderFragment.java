package uk.co.pocketapp.whotel.fragments;

import java.util.ArrayList;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.adapters.InsiderAdapter;
import uk.co.pocketapp.whotel.customview.RecyclingImageView;
import uk.co.pocketapp.whotel.receivers.NetworkBroadcastReceiver;
import uk.co.pocketapp.whotel.tasks.JsonParserTask;
import uk.co.pocketapp.whotel.util.CustomInsiderData;
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
public class InsiderFragment extends SherlockFragment implements
		OnClickListener {

	Fragment mFragment_webview;

	FragmentLoad fragmentLoad;

	String TAG = "InsiderFragment";

	View insiderHeaderView;
	ImageView imageviewInsiderHeader;
	TextView textview_insider_banner_header, textview_insider_banner_desc,
			textview_read_more_banner;

	// ProgressBar progress_banner;

	InsiderAdapter insiderAdapter;
	RelativeLayout rel_banner;

	DisplayImageOptions options;

	PullToRefreshListView mListView;
	ProgressBar progress_insider;

	boolean pauseOnScroll = false; // or true
	boolean pauseOnFling = true; // or false

	private int index = -1;
	private int top = 0;

	boolean bIsViewLoaded = false;

	public static ArrayList<CustomInsiderData> insiderDataList;

	OnRefreshListener<ListView> listRefreshListener;
	public boolean bIsSearchOngoing = false;

	EasyTracker easyTracker;

	public InsiderFragment(FragmentLoad fragmentLoad) {
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

		insiderHeaderView = getActivity().getLayoutInflater().inflate(
				R.layout.header_deals, null);
		imageviewInsiderHeader = (RecyclingImageView) insiderHeaderView
				.findViewById(R.id.imageview_deals_header);

		textview_insider_banner_header = (TextView) insiderHeaderView
				.findViewById(R.id.textview_deals_banner_header);
		textview_insider_banner_desc = (TextView) insiderHeaderView
				.findViewById(R.id.textview_deals_banner_desc);
		textview_read_more_banner = (TextView) insiderHeaderView
				.findViewById(R.id.textview_readmore_banner);
		// progress_banner = (ProgressBar) insiderHeaderView
		// .findViewById(R.id.progress_banner);

		rel_banner = (RelativeLayout) insiderHeaderView
				.findViewById(R.id.rel_header_row);

		easyTracker = EasyTracker.getInstance(getActivity());

		if (easyTracker != null) {
			// This screen name value will remain set on the tracker and sent
			// with
			// hits until it is set to a new value or to null.
			easyTracker.set(Fields.SCREEN_NAME, "Insider");

			easyTracker.send(MapBuilder.createAppView().build());
		}

	}

	public InsiderFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_insider, container,
				false);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_insider);
		mListView.getLoadingLayoutProxy().setLastUpdatedLabel("");
		mListView.getRefreshableView().addHeaderView(insiderHeaderView);
		mListView.getRefreshableView().setEmptyView(
				view.findViewById(android.R.id.empty));

		progress_insider = (ProgressBar) view
				.findViewById(R.id.progress_insider);
		progress_insider.setVisibility(View.VISIBLE);
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		textview_read_more_banner.setText(getActivity().getString(
				R.string.read_more)
				+ " ");

		fragmentLoad.onInsiderFragmentLoad(this);
		// dealsUpdateView();

		// Set a listener to be invoked when the list should be refreshed.
		initRefreshListener();
		// ((PullToRefreshListView) mListView)
		// .setOnRefreshListener(listRefreshListener);

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

		if (insiderDataList != null) {
			progress_insider.setVisibility(View.GONE);
			onTaskCompleted(insiderDataList, false);
		} else {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				progress_insider.setVisibility(View.VISIBLE);
				mListView.getRefreshableView().getEmptyView()
						.setVisibility(View.GONE);
				JsonParserTask jsonParserTask = new JsonParserTask(
						getActivity(), this);
				jsonParserTask.execute();
			} else {
				progress_insider.setVisibility(View.GONE);
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
	}

	void initRefreshListener() {

		listRefreshListener = new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					JsonParserTask jsonParserTask = new JsonParserTask(
							getActivity(), InsiderFragment.this);
					jsonParserTask.execute();
				} else {
					progress_insider.setVisibility(View.GONE);
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

	public void onTaskCompleted(ArrayList<CustomInsiderData> insiderDataList,
			boolean isViewToBeRefreshed) {

		if (progress_insider != null) {

			Handler handler = new Handler();
			Runnable r = new Runnable() {
				public void run() {
					progress_insider.setVisibility(View.GONE);
				}
			};
			handler.postDelayed(r, 100);
		}

		if (isViewToBeRefreshed)
			bIsViewLoaded = false;

		try {
			listRefreshComplete();

			if (insiderDataList != null && insiderDataList.size() > 0) {
				InsiderFragment.insiderDataList = insiderDataList;
				rel_banner.setVisibility(View.VISIBLE);
				// textview_read_more_banner.setOnClickListener(this);
				imageviewInsiderHeader.setOnClickListener(this);

				String imgUrl = insiderDataList.get(0).getInsiderRectLarge()
						.toString();

				ImageLoader.getInstance().displayImage(imgUrl,
						imageviewInsiderHeader,
						new SimpleImageLoadingListener() {
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

				textview_insider_banner_header.setText(insiderDataList.get(0)
						.getInsiderName());
				textview_insider_banner_desc.setText(insiderDataList.get(0)
						.getInsiderDesc());

				Log.d(TAG, "onTaskCompleted()");
				if (!bIsViewLoaded) {
					if (!bIsSearchOngoing)
						insiderAdapter = new InsiderAdapter(insiderDataList,
								getActivity(), this, fragmentLoad);
					// mListView.setAdapter(insiderAdapter);

					if (insiderAdapter != null && insiderAdapter.getCount() > 0)
						bIsViewLoaded = true;
				}
			} else if (insiderAdapter != null && insiderAdapter.getCount() == 0) {
				mListView.getRefreshableView().setAdapter(null);
				rel_banner.setVisibility(View.GONE);
			}
			if (!bIsSearchOngoing) {
				progress_insider.setVisibility(View.GONE);
				mListView.setAdapter(insiderAdapter);
				rel_banner.setVisibility(View.VISIBLE);
			} else {
				rel_banner.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insiderListSearch(CharSequence s) {
		// Log.d(TAG, "insiderListSearch()*** SEARCH TEXT " + s);
		if (insiderAdapter != null)
			insiderAdapter.getFilter().filter(s);

		if (s == null || s.length() == 0) {
			insiderHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.VISIBLE);
			bIsSearchOngoing = false;
			// ((PullToRefreshListView) mListView)
			// .setOnRefreshListener(listRefreshListener);
			// PullToRefreshListView.mRefreshViewText.setVisibility(View.VISIBLE);

		} else {
			insiderHeaderView.findViewById(R.id.rel_header_row).setVisibility(
					View.GONE);
			bIsSearchOngoing = true;
			// ((PullToRefreshListView) mListView).setOnRefreshListener(null);
			// PullToRefreshListView.mRefreshViewText.setVisibility(View.GONE);
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
			mFragment_webview = new WebViewFragment();
			WebViewFragment.fragmentLoad = fragmentLoad;
			bundleArgs = new Bundle();
			bundleArgs.putString("webURL", insiderDataList.get(0)
					.getInsiderPageURL().toString());
			bundleArgs.putString("messageid", insiderDataList.get(0).getId()
					.toString());
			bundleArgs
					.putString("lat", insiderDataList.get(0).getInsiderLati());
			bundleArgs
					.putString("lng", insiderDataList.get(0).getInsiderLong());
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
			insiderListSearch("");
		}
	}

	public void showNoMatchFoundDialog() {

		mListView.getRefreshableView().getEmptyView()
				.setVisibility(View.INVISIBLE);

		new AlertDialog.Builder(getActivity()).setMessage("No match found")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						insiderListSearch("");
						fragmentLoad.emptySearchText();
						dialog.cancel();
					}
				}).create().show();
	}

}
