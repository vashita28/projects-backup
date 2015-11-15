package com.coudriet.snapnshare.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coudriet.snapnshare.adapters.FriendsAdapter;
import com.coudriet.snapnshare.android.ParseConstants;
import com.coudriet.snapnshare.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.snapshare.utility.AppStatus;
import com.snapshare.utility.Common;

public class FriendsFragment extends ListFragment {

	public static final String TAG = FriendsFragment.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;

	private ProgressBar pbInFriendsList;
	private Context mContext;

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
		getActivity().setProgressBarIndeterminateVisibility(false);

		mContext = getActivity();
		if (mContext != null) {
			if (AppStatus.getInstance(getListView().getContext()).isOnline()) {
				updateFriendsList();
			} else {

				Toast.makeText(getListView().getContext(),
						R.string.connectivity_error, Toast.LENGTH_SHORT).show();
				// pbInFriendsList.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateFriendsList() {

		pbInFriendsList.setVisibility(View.VISIBLE);
		getListView().setEnabled(false);
		// getActivity().setProgressBarIndeterminateVisibility(true);

		mCurrentUser = ParseUser.getCurrentUser();

		if (mCurrentUser != null) {
			mFriendsRelation = mCurrentUser
					.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		}

		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);

		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			query.cancel();
		} else {
			// getActivity().setProgressBarIndeterminateVisibility(false);

			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					// getActivity().setProgressBarIndeterminateVisibility(false);

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
					}
				}
			});
		}
	}

	public boolean isFragmentUIActive() {
		return isAdded() && isVisible() && !isDetached() && !isRemoving();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView nameLabel2 = (TextView) v.findViewById(R.id.senderStatus);
		// if (nameLabel2 != null) {
		// if (nameLabel2.getText().toString().equals("CHECKING STATUS...")) {
		// return;
		// }
		// }

		if (mContext != null) {
			if (AppStatus.getInstance(getListView().getContext()).isOnline()) {

				final ParseUser friendPosition = mFriends.get(position);

				final String requestMessage = "Send "
						+ friendPosition.getUsername()
						+ " a friend request so you can share snap messages.";

				final ParseQuery query = new ParseQuery("Requests");
				query.whereEqualTo(ParseConstants.KEY_RECIPIENT_ID,
						friendPosition.getObjectId());
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> friends, ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {

							// error
							if (mContext != null) {

								AlertDialog.Builder builder = new AlertDialog.Builder(
										getListView().getContext());
								builder.setMessage(requestMessage)
										.setTitle("Send Friend Request?")
										.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {

														dialog.cancel();
													}
												})
										.setPositiveButton(
												android.R.string.ok,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {

														ParseObject message = new ParseObject(
																ParseConstants.CLASS_REQUESTS);

														message.put(
																ParseConstants.KEY_RECIPIENT_ID,
																friendPosition
																		.getObjectId());
														message.put(
																ParseConstants.KEY_SENDER_ID,
																ParseUser
																		.getCurrentUser()
																		.getObjectId());
														message.put(
																ParseConstants.KEY_SENDER_NAME,
																ParseUser
																		.getCurrentUser()
																		.getUsername());

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

															public void done(
																	ParseException e) {
																if (e == null) {

																	// Notification
																	// for
																	// Android
																	// users
																	JSONObject data = new JSONObject();
																	try {

																		String alertMessage = ParseUser
																				.getCurrentUser()
																				.getUsername()
																				+ " sent you a friend request!";

																		data.put(
																				"alert",
																				alertMessage);
																		data.put(
																				"badge",
																				"Increment");
																		data.put(
																				"sound",
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

																		String msg = "Your request has been sent!";
																		String tit = "Request Status!";

																		Common.callDialog(
																				getListView()
																						.getContext(),
																				msg,
																				tit);

																		((FriendsAdapter) getListView()
																				.getAdapter())
																				.update(mFriends);

																	} catch (Exception e1) {
																		Log.e("e1",
																				e1.toString());
																	}

																} else {

																	String msg = getString(R.string.user_not_found);
																	String tit = getString(R.string.error_title);

																	Common.callDialog(
																			getListView()
																					.getContext(),
																			msg,
																			tit);
																}
															}
														});

													}
												});

								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
						// }
						else {

							String msg = getString(R.string.user_not_found);
							String tit = getString(R.string.error_title);

							Common.callDialog(getListView().getContext(), msg,
									tit);
						}
					}
				});
			}
		} else {

			Toast.makeText(getListView().getContext(),
					R.string.connectivity_error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mContext = null;
	}
}