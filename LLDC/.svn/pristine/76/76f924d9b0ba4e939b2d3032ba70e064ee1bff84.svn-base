package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.EventSocialAdapter;
import co.uk.android.lldc.async.GetMediaListTask;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;

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
		View rootView = inflater.inflate(R.layout.fragment_eventdetails_social,
				container, false);
		mContext = getActivity();

		gridView = (GridView) rootView.findViewById(R.id.gridview);
		tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);

		pagingprogress = (ProgressBar) rootView
				.findViewById(R.id.pagingprogress);

		pagingprogress.setVisibility(View.VISIBLE);

		// selectedModel = LLDCApplication.DBHelper.getSingleData(_id,
		// tableName);
		if (LLDCApplication.selectedModel.getSocialFlag().equals("1")) {
			gridView.setVisibility(View.VISIBLE);
			tvNoData.setVisibility(View.GONE);
			pagingprogress.setVisibility(View.VISIBLE);
			loadingMore = true;
			getMediaListTask = new GetMediaListTask(mContext,
					LLDCApplication.selectedModel.getSocialHandle(), "");
			getMediaListTask.execute();

		} else {
			pagingprogress.setVisibility(View.GONE);
			gridView.setVisibility(View.GONE);
			tvNoData.setVisibility(View.VISIBLE);
			Log.e(TAG, "LLDCApplication.noSocialMessage:: "
					+ LLDCApplication.noSocialMessage.toString());
			tvNoData.setText(LLDCApplication.noSocialMessage.toString());
		}

		// 1 getsociasl handle text //socialHandle if not

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (eventList == null) {
					return;
				}

				ArrayList<MediaModel> socialList = new ArrayList<MediaModel>();

				MediaDialogFragment dialgo = new MediaDialogFragment(eventList,
						socialList, "Social", position);

				dialgo.show(getFragmentManager(), "Dlg");

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
				//what is the bottom iten that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				//is the bottom item visible & not loading more already ? Load more !
				if((lastInScreen == totalItemCount) && !loadingMore){
					pagingprogress.setVisibility(View.VISIBLE);
					if (!eventList.get(totalItemCount-1).getPaginationUrl().equals("")) {
						loadingMore = true;
						getMediaListTask = new GetMediaListTask(
								mContext, LLDCApplication.selectedModel
								.getSocialHandle(), eventList.get(totalItemCount-1)
								.getPaginationUrl());
						getMediaListTask.execute();
					}
				}
				
			}
		});

		return rootView;
	}

}
