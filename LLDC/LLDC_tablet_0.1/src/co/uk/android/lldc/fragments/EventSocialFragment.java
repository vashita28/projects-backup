package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.EventSocialAdapter;
import co.uk.android.lldc.async.GetMediaListTask;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;
import co.uk.android.lldc.tablet.LLDCApplication;

public class EventSocialFragment extends Fragment {
	private static final String TAG = EventSocialFragment.class.getSimpleName();

	Context mContext;
	GridView gridView;
	TextView tvNoData;
	String _id, tableName;
	EventSocialAdapter eventSocialAdapter = null;
	public static ArrayList<EventMediaModel> eventList = new ArrayList<EventMediaModel>();

	ProgressBar pagingprogress = null;

	public static Handler mHandler;
	boolean loadingMore = false;

	GetMediaListTask getMediaListTask = null;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				if (eventList != null) {
					if (eventSocialAdapter == null) {
						loadingMore = false;
						eventSocialAdapter = new EventSocialAdapter(mContext,
								eventList);
						gridView.setAdapter(eventSocialAdapter);
						pagingprogress.setVisibility(View.GONE);
					} else {
						loadingMore = false;
						eventSocialAdapter.notifyDataSetChanged();
						pagingprogress.setVisibility(View.GONE);
					}
				}

			}
		};
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		Log.e(TAG, "Device is Tablet...");
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(
				R.layout.fragment_eventdetails_social_tablet, container, false);
		mContext = getActivity();

		gridView = (GridView) rootView.findViewById(R.id.gridview);
		tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);

		pagingprogress = (ProgressBar) rootView
				.findViewById(R.id.pagingprogress);

		pagingprogress.setVisibility(View.VISIBLE);

		// selectedModel = LLDCApplication.DBHelper.getSingleData(_id,
		// tableName);
		if (LLDCApplication.selectedModel.getSocialFlag().equals("1")) {// 0
			gridView.setVisibility(View.VISIBLE);
			tvNoData.setVisibility(View.GONE);
			pagingprogress.setVisibility(View.VISIBLE);
			loadingMore = true;
			getMediaListTask = new GetMediaListTask(mContext,
					LLDCApplication.selectedModel.getSocialHandle(), "");
			getMediaListTask.execute();

			// mHandler = new Handler() {
			// @Override
			// public void handleMessage(Message message) {
			// super.handleMessage(message);
			//
			// if (eventList != null) {
			// Log.e(TAG,
			// "mHandler:eventList-size " + eventList.size());
			// eventSocialAdapter = new EventSocialAdapter(mContext,
			// eventList);
			// gridView.setAdapter(eventSocialAdapter);
			// }
			//
			// }
			// };
		} else {
			pagingprogress.setVisibility(View.GONE);
			gridView.setVisibility(View.GONE);
			tvNoData.setVisibility(View.VISIBLE);
			Log.e(TAG, "LLDCApplication.noSocialMessage:: "
					+ LLDCApplication.noSocialMessage.toString());
			tvNoData.setText(LLDCApplication.noSocialMessage.toString());
		}

		// 1 getsocial handle text //socialHandle if not

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (eventList == null) {
					return;
				}
				// TODO Auto-generated method stub
				// final Dialog dialog = new Dialog(mContext);
				// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				// dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				// dialog.setContentView(R.layout.dialog_media);
				// RelativeLayout rlClose = (RelativeLayout) dialog
				// .findViewById(R.id.rlClose);
				// ImageView ivBanner = (ImageView) dialog
				// .findViewById(R.id.ivBanner);
				//
				// final ImageView ivLeft = (ImageView) dialog
				// .findViewById(R.id.ivLeft);
				// final ImageView ivRight = (ImageView) dialog
				// .findViewById(R.id.ivRight);

				ArrayList<MediaModel> socialList = new ArrayList<MediaModel>();
				MediaDialogFragment dialgo = new MediaDialogFragment(eventList,
						socialList, "Social", position);

				dialgo.show(getFragmentManager(), "Dlg");

				// final GalleryAdapter adapter = new GalleryAdapter(
				// getActivity(), socialList, eventList, "Social");
				//
				// final MyGallery gallery = (MyGallery) dialog
				// .findViewById(R.id.mediaGallery);
				//
				// gallery.setAdapter(adapter);
				//
				// gallery.setSelection(position);
				//
				// gallery.setOnItemSelectedListener(new
				// OnItemSelectedListener() {
				//
				// @Override
				// public void onItemSelected(AdapterView<?> arg0, View arg1,
				// int arg2, long arg3) {
				// // TODO Auto-generated method stub
				// ivLeft.setVisibility(View.VISIBLE);
				// ivRight.setVisibility(View.VISIBLE);
				// int count = adapter.getCount();
				// if (arg2 == count - 1) {
				// ivRight.setVisibility(View.GONE);
				// ivLeft.setVisibility(View.VISIBLE);
				// } else if (arg2 == 0) {
				// ivLeft.setVisibility(View.GONE);
				// ivRight.setVisibility(View.VISIBLE);
				// }
				// }
				//
				// @Override
				// public void onNothingSelected(AdapterView<?> arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				// });
				//
				// ivLeft.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// gallery.setSelection(gallery.getSelectedItemPosition() - 1);
				// if (gallery.getSelectedItemPosition() == 0) {
				// ivLeft.setVisibility(View.GONE);
				// ivRight.setVisibility(View.VISIBLE);
				// }
				// }
				// });
				//
				// ivRight.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// gallery.setSelection(gallery.getSelectedItemPosition() + 1);
				// if (gallery.getSelectedItemPosition() == adapter
				// .getCount()) {
				// ivRight.setVisibility(View.GONE);
				// ivLeft.setVisibility(View.VISIBLE);
				// }
				// }
				// });
				//
				// rlClose.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// dialog.dismiss();
				// }
				// });
				// // dialog.setCanceledOnTouchOutside(false);
				// dialog.show();

			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// what is the bottom item that is visible
				try {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					// is the bottom item visible & not loading more already ?
					// Load
					// more !
					if ((lastInScreen == totalItemCount) && !loadingMore
							&& eventList.size() > 0) {

						pagingprogress.setVisibility(View.VISIBLE);

						if (!eventList.get(totalItemCount - 1)
								.getPaginationUrl().equals("")) {
							loadingMore = true;
							getMediaListTask = new GetMediaListTask(mContext,
									LLDCApplication.selectedModel
											.getSocialHandle(), eventList.get(
											totalItemCount - 1)
											.getPaginationUrl());
							getMediaListTask.execute();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		LLDCApplication.removeSimpleProgressDialog();
		return rootView;
	}

}
