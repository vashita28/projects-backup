package com.coudriet.tapsnap.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coudriet.tapsnap.adapters.FriendsAdapter;
import com.coudriet.tapsnap.android.MainActivity;
import com.coudriet.tapsnap.android.ParseConstants;
import com.coudriet.tapsnap.android.R;
import com.coudriet.tapsnap.android.SignUpActivity;
import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FriendsFragment extends ListFragment {

	public static final String TAG = FriendsFragment.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;

	private ProgressBar pbInFriendsList;
	private Context mContext;
	boolean friendsListStatus;
	private ParseQuery<ParseUser> mQuery;
	// private ProgressDialog progressDialog;
	private HashMap<Long, Integer> mapID = new HashMap<Long, Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);

		pbInFriendsList = (ProgressBar) rootView
				.findViewById(R.id.progressBarfriendslist);

		mFriends = new ArrayList<ParseUser>();

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
		friendsListStatus = false;
		mContext = getActivity();
		if (mContext != null) {
			if (AppStatus.getInstance(getListView().getContext()).isOnline()) {
				if(MainActivity.mPosition==2)
				updateFriendsList();
			} else {

				Toast.makeText(getListView().getContext(),
						R.string.connectivity_error, Toast.LENGTH_SHORT).show();
				// pbInFriendsList.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateFriendsList() {
		
		
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		         /*   while(true) {
		                sleep(1000);
		                handler.post(r);
		            }*/
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		};

		thread.start();

		pbInFriendsList.setVisibility(View.VISIBLE);
		getListView().setEnabled(false);
		// getActivity().setProgressBarIndeterminateVisibility(true);

		mCurrentUser = ParseUser.getCurrentUser();

		if (mCurrentUser != null) {
			mFriendsRelation = mCurrentUser
					.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		}

		mQuery = mFriendsRelation.getQuery();
		mQuery.addAscendingOrder(ParseConstants.KEY_USERNAME);

		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			mQuery.cancel();
		} else {
			// getActivity().setProgressBarIndeterminateVisibility(false);

			mQuery.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					// getActivity().setProgressBarIndeterminateVisibility(false);

					friendsListStatus = true;
					pbInFriendsList.setVisibility(View.INVISIBLE);
					if (mContext != null)
						getListView().setEnabled(true);

					if (e == null) {

						// We found friends!
						mFriends = friends;

						if (isFragmentUIActive()) {
							Log.i(TAG, "FriendsFragment Visible");

							if (getListView().getAdapter() == null) {
								FriendsAdapter adapter = new FriendsAdapter(
										getListView().getContext(), mFriends);
								setListAdapter(adapter);
							} else {
								// refill the adapter!
								((FriendsAdapter) getListView().getAdapter())
										.refill(mFriends);
							}
						} else {
							Log.i(TAG, "FriendsFragment Not Visible");
						}
					} else {
						Toast.makeText(getListView().getContext(),
								R.string.connectivity_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

	 new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					try
					{
						if (!friendsListStatus) {
					
						pbInFriendsList.setVisibility(View.INVISIBLE);
					/*	Toast.makeText(getActivity(),
								"Connection time out, Try again.",
								Toast.LENGTH_SHORT).show();*/
						Log.i(TAG, "!friendsListStatus");
						mQuery.cancel();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onListItemClick(ListView l, View v, final int position,
			final long id) {
		super.onListItemClick(l, v, position, id);
		TextView nameLabel2 = (TextView) v.findViewById(R.id.senderStatus);
		// if (nameLabel2 != null) {
		// if (nameLabel2.getText().toString().equals("CHECKING STATUS...")
		// || nameLabel2.getText().toString()
		// .equals(R.string.friendship_confirmed)) {
		// return;
		// }
		// }
		final ParseUser friendPosition = mFriends.get(position);

		String requestMessage = "Send " + friendPosition.getUsername()
				+ " a friend request so you can share snap messages.";

		AlertDialog.Builder builder = new AlertDialog.Builder(getListView()
				.getContext());
		builder.setMessage(requestMessage)
				.setTitle("Send Friend Request?")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								if (mContext != null) {
									final ParseQuery query = new ParseQuery(
											"Requests");
									query.whereEqualTo(
											ParseConstants.KEY_RECIPIENT_ID,
											friendPosition.getObjectId());
									query.findInBackground(new FindCallback<ParseObject>() {

										@Override
										public void done(
												List<ParseObject> friends,
												ParseException e) {
											// TODO Auto-generated method stub
											if (e == null) {
												for (int i = 0; i < friends
														.size(); i++) {
													Log.e(TAG,
															"********isSent********** "
																	+ friends
																			.get(i)
																			.getString(
																					"isSent"));
												}
											}
										}
									});

									ParseObject message = new ParseObject(
											ParseConstants.CLASS_REQUESTS);
									message.put(
											ParseConstants.KEY_RECIPIENT_ID,
											friendPosition.getObjectId());
									message.put(ParseConstants.KEY_SENDER_ID,
											ParseUser.getCurrentUser()
													.getObjectId());
									message.put(ParseConstants.KEY_SENDER_NAME,
											ParseUser.getCurrentUser()
													.getUsername());

									message.put("isSent", "yes");
									// Added picture
									if (mCurrentUser
											.get(ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE) == null) {
										message.put(
												ParseConstants.KEY_PROFILE_PICTURE,
												mCurrentUser
														.get(ParseConstants.KEY_PROFILE_PICTURE));
									} else {
										message.put(
												ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE,
												mCurrentUser
														.get(ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE));
									}
									message.saveInBackground(new SaveCallback() {
										public void done(ParseException e) {
											if (e == null) {

												String alertMessage = ParseUser
														.getCurrentUser()
														.getUsername()
														+ " sent you a friend request!";

												// Notification for Android &
												// iOS
												// users
												JSONObject data = new JSONObject();
												try {
													data.put("alert",
															alertMessage);
													data.put("badge",
															"Increment");
													data.put("sound",
															"shutterClick.wav");

													ParsePush push = new ParsePush();
													push.setChannel(friendPosition
															.getUsername()); // Notice
																				// we
																				// use
																				// setChannel
																				// not
																				// setChannels
													push.setData(data);
													push.sendInBackground();

												} catch (JSONException e1) {
													// TODO Auto-generated catch
													// block
													e1.printStackTrace();
												}

												AlertDialog.Builder builder = new AlertDialog.Builder(
														getListView()
																.getContext());
												builder.setMessage(
														"Your request has been sent!")
														.setTitle(
																"Request Status!")
														.setPositiveButton(
																android.R.string.ok,
																null);
												AlertDialog dialog = builder
														.create();
												dialog.show();

												((FriendsAdapter) getListView()
														.getAdapter())
														.update(mFriends);

											} else {

												Log.e(TAG, e.getMessage());
												AlertDialog.Builder builder = new AlertDialog.Builder(
														getListView()
																.getContext());
												builder.setMessage(
														R.string.connectivity_error)
														.setTitle(
																R.string.error_title)
														.setPositiveButton(
																android.R.string.ok,
																null);
												AlertDialog dialog = builder
														.create();
												dialog.show();
											}
										}
									});
								}
							}
						});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

	public class MyAlertDialog extends AlertDialog.Builder {

		// add constructor stuff

		public MyAlertDialog(Context arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		private long tag;

		public long getTag() {
			return tag;
		}

		public void setTag(final long tag) {
			Log.e(TAG, "getTag:: " + tag);
			this.tag = tag;
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
}