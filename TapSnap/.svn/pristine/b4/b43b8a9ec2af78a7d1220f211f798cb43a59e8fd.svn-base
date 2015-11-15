package com.coudriet.snapnshare.android;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.coudriet.snapnshare.android.R;

public class FriendsAdapter extends ArrayAdapter<ParseUser> {
	
	public static final String TAG = FriendsFragment.class.getSimpleName();
	
	protected Context mContext;
	protected List<ParseUser> mFriends;
	protected List<ParseUser> mOtherFriends;
	protected List<ParseObject> mRequests;
	protected ParseUser mCurrentUser;
	protected ParseRelation<ParseUser> mRecipientsRelation;
	private ArrayList<ParseUser> mFriendsFound;
	private ArrayList<ParseUser> mRequestsFound;
	
	public FriendsAdapter(Context context, List<ParseUser> friends) {
		super(context, R.layout.friends_item, friends);
		mContext = context;
		mFriends = friends;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.friends_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
			holder.iconImageView2 = (ImageView)convertView.findViewById(R.id.requestIcon);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView)convertView.findViewById(R.id.senderStatus);
			convertView.setTag(holder);
		}
		else {
			
			holder = (ViewHolder)convertView.getTag();
		}
		
		mFriendsFound = new ArrayList<ParseUser>();
		mRequestsFound = new ArrayList<ParseUser>();
		
		mCurrentUser = ParseUser.getCurrentUser();
		
		final ParseUser friendPosition = mFriends.get(position);
		
		holder.iconImageView.setImageResource(R.drawable.ic_chatna);
		
		holder.iconImageView2.setImageDrawable(null);
			
		holder.nameLabel.setText(friendPosition.getUsername());
		
		holder.nameLabel2.setText("checking status...");
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_REQUESTS);
		query.findInBackground(new FindCallback<ParseObject>() {
		    
			@Override
			public void done(List<ParseObject> requests, ParseException e) {
		        if (e == null) {
		           
		        	// Success
					mRequests = requests;
					
				    for (final ParseUser user : mFriends) {

		        		mRecipientsRelation = user.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

		        		ParseQuery<ParseUser> query = mRecipientsRelation.getQuery();
		        		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
			
		        		ParseUser currentUser = ParseUser.getCurrentUser();
		        		if (currentUser == null) {
				
		        			query.cancel();
		        		}
		        		else
		        		{
		        			query.findInBackground(new FindCallback<ParseUser>() {
					
		        				@Override
		        				public void done(List<ParseUser> otherFriends, ParseException e) {
						
		        					if (e == null)
		        					{ 
		        						// Success
		        						mOtherFriends = otherFriends;
							
		        						for (ParseUser friend : mOtherFriends) {
								
		        							if (friend.getObjectId().equals(mCurrentUser.getObjectId()))
		        							{	
		        								// Log.i(TAG, "Friend found");
									
		        								mFriendsFound.add(user);
									
		        								// System.out.println(mFriendsFinal);
		        								
		        					        	if (mFriendsFound.contains(friendPosition))
		        								{	
		        									// Log.i(TAG, "Friend found");
		        										
		        									holder.iconImageView.setImageResource(R.drawable.ic_chat);
		        									
		        									holder.iconImageView2.setImageDrawable(null);
		        										
		        									holder.nameLabel2.setText("friendship confirmed");
		        								}
		        							    else if (isPending(friendPosition))
		        								{
		        									// Log.i(TAG, "Friend not found");
		        									
		        									holder.iconImageView.setImageResource(R.drawable.ic_chatna);
		        									
		        									holder.iconImageView2.setImageDrawable(null);
		        									
		        									holder.nameLabel2.setText("friend request pending");
		        								}
		        								else
		        								{
		        									// Log.i(TAG, "Friend not found");
		        										
		        									holder.iconImageView.setImageResource(R.drawable.ic_chatna);
		        									
		        									holder.iconImageView2.setImageResource(R.drawable.ic_sendrequest);
		        									
		        									holder.nameLabel2.setText("waiting for " + friendPosition.getUsername() + " to add you");
		        								}
		        							}
		        						}
		        					}
		        				}
		        			});
		        		}
				    }
		        }
			}	
		});
		        
		return convertView;
	}
	
	@Override
	public boolean isEnabled(int position) {
		   
		   ParseUser friendPosition = mFriends.get(position);
		   
		   if (mRequestsFound.contains(friendPosition) || mFriendsFound.contains(friendPosition)) {
	            
			   return false;
	       }
		         
	    return true;
	}
	
	public boolean isPending(ParseUser user) {
		   
		mCurrentUser = ParseUser.getCurrentUser();
		
		for (ParseObject friend : mRequests) {
    		
    		if (friend.getString(ParseConstants.KEY_RECIPIENT_ID).equals (user.getObjectId()) && friend.getString(ParseConstants.KEY_SENDER_ID).equals (mCurrentUser.getObjectId()))
    		{
    			mRequestsFound.add(user);
    			
    			return true;
    		}
    	}
		   
		return false;
	}
	
	private static class ViewHolder {
		ImageView iconImageView;
		ImageView iconImageView2;
		TextView nameLabel;
		TextView nameLabel2;
	}
	
	public void refill(List<ParseUser> friends) {
		
		// System.out.println("Friends Refill");
		
		mFriends.clear();
		mFriends.addAll(friends);
		notifyDataSetChanged();
	}
	
	public void update(List<ParseUser> friends) {
		
		// System.out.println("Friends Update");
		
		notifyDataSetChanged();
	}
}






