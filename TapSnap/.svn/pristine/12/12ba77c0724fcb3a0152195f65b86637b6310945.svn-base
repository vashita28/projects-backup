package com.coudriet.tapsnap.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.coudriet.tapsnap.adapters.MessageAdapter;
import com.coudriet.tapsnap.android.MainActivity;
import com.coudriet.tapsnap.android.ParseConstants;
import com.coudriet.tapsnap.android.R;
import com.coudriet.tapsnap.android.RecipientsActivity;
import com.coudriet.tapsnap.android.ViewImageActivity;
import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InboxFragment extends ListFragment {

	public static final String TAG = InboxFragment.class.getSimpleName();

	protected List<ParseObject> mMessages;

	private ProgressBar pbInbox;
	
	boolean inboxListStatus;

	private Context mContext;
	// InboxReceiver receiver = new InboxReceiver();

	public static Handler mUpdateListHandler;
	
	private ParseQuery<ParseObject> mQuery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_inbox, container,
				false);

		pbInbox = (ProgressBar) rootView.findViewById(R.id.progressBarinbox);

		Common.flagBackPress = true;

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG,
				"onResume");
		Log.e(TAG,
				MainActivity.mPosition+"");

		getActivity().setProgressBarIndeterminateVisibility(false);
		inboxListStatus=false;
		mContext = getActivity();

		// Register Inbox Receiver
		// IntentFilter filter = new IntentFilter(
		// "com.coudriet.tapsnap.android.UPDATE_LIST");
		// mContext.registerReceiver(receiver, filter);

		mUpdateListHandler = new Handler() {

			public void handleMessage(Message msg) {

				if (mContext != null)
				{	Log.e(TAG, "Inside Handler:List Updated!!");
				if(MainActivity.mPosition==0)
				updateInboxList();}
			}
		};
		if (AppStatus.getInstance(getListView().getContext()).isOnline()) {
			if(MainActivity.mPosition==0)
			updateInboxList();
		} else {

			Toast.makeText(getListView().getContext(),
					R.string.connectivity_error, Toast.LENGTH_SHORT).show();
			// pbInbox.setVisibility(View.VISIBLE);
		}
	}

	public void updateInboxList() {
		pbInbox.setVisibility(View.VISIBLE);
		if (getListView() != null) {
			getListView().setEnabled(false);
		}

		// getActivity().setProgressBarIndeterminateVisibility(true);

		final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				ParseConstants.CLASS_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser
				.getCurrentUser().getObjectId());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {

			query.cancel();
		} else {
			// getActivity().setProgressBarIndeterminateVisibility(false);

			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> messages, ParseException e) {
					// getActivity().setProgressBarIndeterminateVisibility(false);
					inboxListStatus=true;
					pbInbox.setVisibility(View.INVISIBLE);
					if (mContext != null)
						getListView().setEnabled(true);

					if (e == null) {
						// We found messages!
						mMessages = messages;

						if (isFragmentUIActive()) {
							Log.i(TAG, "InboxFragment Visible");
							if (getListView() != null)
								if (getListView().getAdapter() == null) {
									MessageAdapter adapter = new MessageAdapter(
											getListView().getContext(),
											mMessages);
									setListAdapter(adapter);
								} else {
									// refill the adapter!
									((MessageAdapter) getListView()
											.getAdapter()).refill(mMessages);
								}
						} else {
							Log.i(TAG, "InboxFragment Not Visible");
						}
					}
					else {

						Toast.makeText(getActivity(),
								R.string.connectivity_error, Toast.LENGTH_SHORT).show();
					}
				}
			});
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					try
					{
						if (!inboxListStatus) {
					
						pbInbox.setVisibility(View.INVISIBLE);
						/*Toast.makeText(getActivity(),
								"Connection time out, Try again.",
								Toast.LENGTH_SHORT).show();*/
						Log.i(TAG, "!inboxListStatus");
						query.cancel();
					}
					}
					catch(Exception e)
					{
						Log.e(TAG, "exception in postDelayed");
					}
				}
			}, 12000);
		}
	}

	public boolean isFragmentUIActive() {
		return isAdded() && isVisible() && !isDetached() && !isRemoving();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (AppStatus.getInstance(getListView().getContext()).isOnline()) {

			ParseObject message = mMessages.get(position);
			String messageSender = message
					.getString(ParseConstants.KEY_SENDER_NAME);
			Date date = message.getCreatedAt();

			String timestamp = new SimpleDateFormat("hh:mma   dd MMMM",
					Locale.US).format(date);// EEE MMM dd, yyyy hh:mm a

			String messageType = message
					.getString(ParseConstants.KEY_FILE_TYPE);
			ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
			Uri fileUri = Uri.parse(file.getUrl());

			if (messageType.equals(ParseConstants.TYPE_IMAGE)) {

				// view the image
				Intent intent = new Intent(getActivity(),
						ViewImageActivity.class);
				intent.putExtra("messageSender", messageSender);
				intent.putExtra("messageSentAt", timestamp);
				intent.setData(fileUri);
				startActivity(intent);

				// System.out.println(messageSender);
			} else {
				// view the video
				Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
				intent.setDataAndType(fileUri, "video/*");
				startActivity(intent);
			}

			// Delete it!
			List<String> ids = message
					.getList(ParseConstants.KEY_RECIPIENT_IDS);

			if (ids.size() == 1) {
				// last recipient - delete the whole thing!
				message.deleteInBackground();
			} else {
				// remove the recipient and save
				ids.remove(ParseUser.getCurrentUser().getObjectId());

				ArrayList<String> idsToRemove = new ArrayList<String>();
				idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

				message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
				message.saveInBackground();
			}

		} else {

			Toast.makeText(getListView().getContext(),
					R.string.connectivity_error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDetach() {
		Log.e(TAG,
				"onDetach");
		super.onDetach();
		try
		{
			mQuery.cancel();
		}
		catch(Exception e)
		{
			Log.e(TAG,
					"exception in onDetach");
		}
		mContext = null;
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.e(TAG,
				"onDestroyView");
		mContext = null;
	}

	public class InboxReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "InboxReceiver");
			try {
				if(MainActivity.mPosition==0)
					updateInboxList();
			} catch (Exception os) {

			}
		}
	}

}
