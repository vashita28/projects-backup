package com.coudriet.snapnshare.android;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.coudriet.snapnshare.android.R;

public class RequestsFragment extends ListFragment {

	public static final String TAG = RequestsFragment.class.getSimpleName();
	
	protected List<ParseObject> mRequests;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected List<ParseUser> mRequestedUser;
	protected ParseUser mCurrentUser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_requests,
				container, false);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		updateRequestsList();
	}
	
	private void updateRequestsList() {
		
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_REQUESTS);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_ID, ParseUser.getCurrentUser().getObjectId());
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
				public void done(List<ParseObject> requests, ParseException e) {
					//getActivity().setProgressBarIndeterminateVisibility(false);
					
					if (e == null) {
						// We found requests!
						mRequests = requests;
						
						if (isFragmentUIActive())
						{
							Log.i(TAG, "RequestsFragment Visible");
							
							if (getListView().getAdapter() == null) {
								RequestsAdapter adapter = new RequestsAdapter(
										getListView().getContext(), 
										mRequests);
								setListAdapter(adapter);
							}
							else {
								// refill the adapter!
								((RequestsAdapter)getListView().getAdapter()).refill(mRequests);
							}
						}
						else
						{
							Log.i(TAG, "RequestsFragment Not Visible");
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
	public void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
				final ParseObject request = mRequests.get(position);
				
				// System.out.println(request.getString(ParseConstants.KEY_SENDER_ID));
				
				ParseQuery<ParseUser> query = ParseUser.getQuery();
				try {
					query.get(request.getString(ParseConstants.KEY_SENDER_ID));
				} catch (ParseException e1) {
					
					e1.printStackTrace();
				}
				query.findInBackground(new FindCallback<ParseUser>() {
				  public void done(List<ParseUser> objects, ParseException e) {
				    if (e == null) {
				        // Success
				    	mRequestedUser = objects;
				    	
				    	// System.out.println(mRequestedUser);
				    }
				}
			});
		
		String requestMessage = "Add " + request.getString(ParseConstants.KEY_SENDER_NAME) + " as a confirmed friend to share snap messages?";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
		builder.setMessage(requestMessage)
			.setTitle("Add Friend?")
			.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
	
					dialog.cancel();
				}
			})
			.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					request.deleteInBackground();
					
					((RequestsAdapter)getListView().getAdapter()).clear(mRequests);
					
					mCurrentUser = ParseUser.getCurrentUser();
					
					mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
					
					mFriendsRelation.add(mRequestedUser.get(position));

					mCurrentUser.saveInBackground(new SaveCallback() {
						   public void done(ParseException e) {
						     if (e == null) {
						    	 
						    	 AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
								 builder.setMessage("You can now send and receive snap messages with " + request.getString(ParseConstants.KEY_SENDER_NAME) + ".")
								 .setTitle("Added!")
							     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
										
							    	 public void onClick(DialogInterface dialog,int id) {
							    	 
							    		 final Handler handler = new Handler();
							    		 handler.postDelayed(new Runnable() {
							    		   @Override
							    		   public void run() {
							    		    
							    			   updateRequestsList();
							    		   }
							    		 
							    		 }, 1000);
							     	  }
							     });
								 
								 AlertDialog dialog = builder.create();
								 dialog.show();
									
							  } 
						  }
					});
				}
			})
			.setNeutralButton("Delete Request", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				request.deleteInBackground();
				
				((RequestsAdapter)getListView().getAdapter()).clear(mRequests);
				
					 final Handler handler = new Handler();
		    		 handler.postDelayed(new Runnable() {
		    		   @Override
		    		   public void run() {
		    		    
		    			   updateRequestsList();
		    		   }
		    		 
		    		 }, 1000);
		    	 }
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}








