package co.uk.android.lldc.fragments;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
	EventSocialAdapter eventSocialAdapter;
	public static ArrayList<EventMediaModel> eventList = new ArrayList<EventMediaModel>();

	public static Handler mHandler;

	@SuppressLint("HandlerLeak") @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				if (eventList != null) {
					eventSocialAdapter = new EventSocialAdapter(mContext,
							eventList);
					gridView.setAdapter(eventSocialAdapter);
				}

			}
		};
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler = null;
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

		// selectedModel = LLDCApplication.DBHelper.getSingleData(_id,
		// tableName);
		if (LLDCApplication.selectedModel.getSocialFlag().equals("1")) {
			gridView.setVisibility(View.VISIBLE);
			tvNoData.setVisibility(View.GONE);
			GetMediaListTask getMediaListTask = new GetMediaListTask(mContext,
					LLDCApplication.selectedModel.getSocialHandle());
			getMediaListTask.execute();
			
		} else {
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

				MediaDialogFragment dialgo = new MediaDialogFragment(eventList, socialList, "Social", position);
				
				dialgo.show(getFragmentManager(), "Dlg");

			}
		});

		return rootView;
	}

}
