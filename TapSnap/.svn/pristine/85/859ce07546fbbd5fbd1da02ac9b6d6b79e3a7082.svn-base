package com.coudriet.snapnshare.android;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.coudriet.snapnshare.android.R;

public class FriendsFragment extends ListFragment {
	
	public static final String TAG = FriendsFragment.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends,
				container, false);
		
	
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		updateFriendsList();
	}
	
	private void updateFriendsList() {
	
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			
			query.cancel();
		}
		else
		{
			//change this
			getActivity().setProgressBarIndeterminateVisibility(false);
			
			query.findInBackground(new FindCallback<ParseUser>() {
				
				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					//getActivity().setProgressBarIndeterminateVisibility(false);
					
					if (e == null) {
						
						// We found friends!
						mFriends = friends;
						
						if (isFragmentUIActive())
						{
							Log.i(TAG, "FriendsFragment Visible");
							
							if (getListView().getAdapter() == null) {
								FriendsAdapter adapter = new FriendsAdapter(
										getListView().getContext(), 
										mFriends);
								setListAdapter(adapter);
							}
							else {
								// refill the adapter!
								((FriendsAdapter)getListView().getAdapter()).refill(mFriends);
							}
						}
						else
						{
							Log.i(TAG, "FriendsFragment Not Visible");
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
		
		final ParseUser friendPosition = mFriends.get(position);
		
		String requestMessage = "Send " + friendPosition.getUsername() + " a friend request so you can share snap messages.";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
		builder.setMessage(requestMessage)
			.setTitle("Send Friend Request?")
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
	
					dialog.cancel();
				}
			})
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					ParseObject message = new ParseObject(ParseConstants.CLASS_REQUESTS);
					message.put(ParseConstants.KEY_RECIPIENT_ID, friendPosition.getObjectId());
					message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
					message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
					message.saveInBackground(new SaveCallback() {
						   public void done(ParseException e) {
						     if (e == null) {
						    	 
									String alertMessage = ParseUser.getCurrentUser().getUsername() + " sent you a friend request!";

									// Notification for Android & iOS users
									JSONObject data = new JSONObject();
									try {
										data.put("alert", alertMessage);
				                    	data.put("badge", "Increment");
				                    	data.put("sound", "shutterClick.wav");
				                       
				                    	ParsePush push = new ParsePush();
				                    	push.setChannel(friendPosition.getUsername()); // Notice we use setChannel not setChannels
				                    	push.setData(data);
				                    	push.sendInBackground();
				                    	
					                } catch (JSONException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
					                }
									
									AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
									builder.setMessage("Your request has been sent!")
										.setTitle("Request Status!")
										.setPositiveButton(android.R.string.ok, null);
									AlertDialog dialog = builder.create();
									dialog.show();
									
									((FriendsAdapter)getListView().getAdapter()).update(mFriends);
									
						     } else {
						    	 
							    	Log.e(TAG, e.getMessage());
									AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
									builder.setMessage(R.string.connectivity_error)
										.setTitle(R.string.error_title)
										.setPositiveButton(android.R.string.ok, null);
									AlertDialog dialog = builder.create();
									dialog.show();
						     	}
						     }
						});
					}
			  });
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}