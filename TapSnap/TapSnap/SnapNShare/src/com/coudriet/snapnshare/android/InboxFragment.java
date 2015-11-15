package com.coudriet.snapnshare.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.coudriet.snapnshare.android.R;

public class InboxFragment extends ListFragment {

	public static final String TAG = InboxFragment.class.getSimpleName();
	
	protected List<ParseObject> mMessages;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_inbox,
				container, false);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		updateInboxList();
	}
	
	private void updateInboxList() {
		
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			
			query.cancel();
		}
		else
		{
			
			//change this
			getActivity().setProgressBarIndeterminateVisibility(false);
			
			query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> messages, ParseException e) {
					//getActivity().setProgressBarIndeterminateVisibility(false);
					
					if (e == null) {
						// We found messages!
						mMessages = messages;
						
						if (isFragmentUIActive())
						{
							Log.i(TAG, "InboxFragment Visible");
							
							if (getListView().getAdapter() == null) {
								MessageAdapter adapter = new MessageAdapter(
										getListView().getContext(), 
										mMessages);
								setListAdapter(adapter);
							}
							else {
								// refill the adapter!
								((MessageAdapter)getListView().getAdapter()).refill(mMessages);
							}
						}
						else
						{
							Log.i(TAG, "InboxFragment Not Visible");
						}
					}
				}
			});
		}
	}
	
	public boolean isFragmentUIActive() {
	    return isAdded() && isVisible() && !isDetached() && !isRemoving();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject message = mMessages.get(position);
		String messageSender = message.getString(ParseConstants.KEY_SENDER_NAME);
		Date date = message.getCreatedAt();
		
		String timestamp = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm a", Locale.US).format(date);
		
		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
		Uri fileUri = Uri.parse(file.getUrl());
		
		if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
			
			// view the image
			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
			intent.putExtra("messageSender", messageSender);
			intent.putExtra("messageSentAt", timestamp);
			intent.setData(fileUri);
			startActivity(intent);
			
		    // System.out.println(messageSender);
		}
		else {
			// view the video
			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
			intent.setDataAndType(fileUri, "video/*");
			startActivity(intent);
		}
		
		// Delete it!
		List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
		
		if (ids.size() == 1) {
			// last recipient - delete the whole thing!
			message.deleteInBackground();
		}
		else {
			// remove the recipient and save
			ids.remove(ParseUser.getCurrentUser().getObjectId());
			
			ArrayList<String> idsToRemove = new ArrayList<String>();
			idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
			
			message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
			message.saveInBackground();
		}
	}
}








