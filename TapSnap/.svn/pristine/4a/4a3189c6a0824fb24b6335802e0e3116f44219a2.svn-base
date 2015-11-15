package com.coudriet.tapsnap.fragments;

import java.util.List;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.coudriet.tapsnap.adapters.RequestsAdapter;
import com.coudriet.tapsnap.android.MainActivity;
import com.coudriet.tapsnap.android.ParseConstants;
import com.coudriet.tapsnap.android.R;
import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RequestsFragment extends ListFragment {

	public static final String TAG = RequestsFragment.class.getSimpleName();

	protected List<ParseObject> mRequests;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected List<ParseUser> mRequestedUser;
	protected ParseUser mCurrentUser;
	private ProgressBar mPbInFriendsReq;
	private Context mContext;
	private boolean mFriendsReqStatus;
    private	ParseQuery<ParseUser> mQueryUsers;
    private ParseQuery<ParseObject> mQuery; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_requests, container,
				false);
		mPbInFriendsReq = (ProgressBar) rootView
				.findViewById(R.id.progressBarfriendsReq);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG,
				"onResume");
		Log.e(TAG,
				MainActivity.mPosition+"");
		mFriendsReqStatus = false;
		getActivity().setProgressBarIndeterminateVisibility(false);
		mContext = getActivity();
		if (AppStatus.getInstance(getListView().getContext()).isOnline()) {
			if(MainActivity.mPosition==1)
			updateRequestsList();
		} else {
			Toast.makeText(getListView().getContext(),
					R.string.connectivity_error, Toast.LENGTH_SHORT).show();
		}
	}

	private void updateRequestsList() {

		mPbInFriendsReq.setVisibility(View.VISIBLE);
		// getActivity().setProgressBarIndeterminateVisibility(true);
		getListView().setEnabled(false);

		mQuery = new ParseQuery<ParseObject>(
				ParseConstants.CLASS_REQUESTS);
		mQuery.whereEqualTo(ParseConstants.KEY_RECIPIENT_ID, ParseUser
				.getCurrentUser().getObjectId());
		mQuery.addDescendingOrder(ParseConstants.KEY_CREATED_AT);

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {

			mQuery.cancel();
		} else {
			// getActivity().setProgressBarIndeterminateVisibility(false);

			mQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> requests, ParseException e) {
					// getActivity().setProgressBarIndeterminateVisibility(false);
					mFriendsReqStatus = true;
					mPbInFriendsReq.setVisibility(View.INVISIBLE);
					if (mContext != null)
						getListView().setEnabled(true);

					if (e == null) {
						// We found requests!
						mRequests = requests;

						if (isFragmentUIActive()) {
							Log.i(TAG, "RequestsFragment Visible");

							if (getListView().getAdapter() == null) {
								RequestsAdapter adapter = new RequestsAdapter(
										getListView().getContext(), mRequests);
								setListAdapter(adapter);
							} else {
								// refill the adapter!
								((RequestsAdapter) getListView().getAdapter())
										.refill(mRequests);
							}
						} else {
							Log.i(TAG, "RequestsFragment Not Visible");
						}
					} else {
						Toast.makeText(getActivity(),
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
						if (!mFriendsReqStatus) {
					
						mPbInFriendsReq.setVisibility(View.INVISIBLE);
	/*					Toast.makeText(getActivity(),
								"Connection time out, Try again.",
								Toast.LENGTH_SHORT).show();*/
						Log.i(TAG, "!friendsReqStatus");
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

	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (AppStatus.getInstance(getListView().getContext()).isOnline()) {

			Common.flagBackPress = false;

			final ParseObject request = mRequests.get(position);

			// System.out.println(request.getString(ParseConstants.KEY_SENDER_ID));

			mQueryUsers = ParseUser.getQuery();
			try {
				mQueryUsers.get(request.getString(ParseConstants.KEY_SENDER_ID));
			} catch (ParseException e1) {

				e1.printStackTrace();
			}
			mQueryUsers.findInBackground(new FindCallback<ParseUser>() {

				public void done(List<ParseUser> objects, ParseException e) {

					if (e == null) {
						// Success
						mRequestedUser = objects;
					} else {
						Toast.makeText(getActivity(),
								R.string.connectivity_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			String requestMessage = "Add "
					+ request.getString(ParseConstants.KEY_SENDER_NAME)
					+ " as a confirmed friend to share snap messages?";

			AlertDialog.Builder builder = new AlertDialog.Builder(getListView()
					.getContext());
			builder.setMessage(requestMessage)
					.setTitle("Add Friend?")
					.setNegativeButton("Maybe Later",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							})
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									request.deleteInBackground();

									((RequestsAdapter) getListView()
											.getAdapter()).clear(mRequests);

									mCurrentUser = ParseUser.getCurrentUser();

									if (mCurrentUser != null) {

										try {
											mFriendsRelation = mCurrentUser
													.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
											mFriendsRelation.add(mRequestedUser
													.get(position));
										} catch (Exception e) {
											Log.e("e", e.toString());
										}

										mCurrentUser
												.saveInBackground(new SaveCallback() {

													public void done(
															ParseException e) {

														if (e == null) {

															Common.flagBackPress = true;

															try {
																AlertDialog.Builder builder = new AlertDialog.Builder(
																		getListView()
																				.getContext());
																builder.setMessage(
																		"You can now send and receive snap messages with "
																				+ request
																						.getString(ParseConstants.KEY_SENDER_NAME)
																				+ ".")
																		.setTitle(
																				"Added!")
																		.setPositiveButton(
																				android.R.string.ok,
																				new DialogInterface.OnClickListener() {

																					public void onClick(
																							DialogInterface dialog,
																							int id) {

																						final Handler handler = new Handler();
																						handler.postDelayed(
																								new Runnable() {
																									@Override
																									public void run() {
																										if(MainActivity.mPosition==1)
																										updateRequestsList();
																									}

																								},
																								1000);
																					}
																				});

																AlertDialog dialog = builder
																		.create();
																dialog.show();

															} catch (Exception ee) {
																Log.e("ee",
																		ee.toString());
															}

														} else {
															Toast.makeText(
																	getActivity(),
																	R.string.connectivity_error,
																	Toast.LENGTH_SHORT)
																	.show();
														}
													}
												});

									}
								}
							})
					.setNeutralButton("Delete Request",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									request.deleteInBackground();

									((RequestsAdapter) getListView()
											.getAdapter()).clear(mRequests);

									final Handler handler = new Handler();
									handler.postDelayed(new Runnable() {
										@Override
										public void run() {
											if(MainActivity.mPosition==1)
											updateRequestsList();
										}

									}, 1000);
								}
							});

			AlertDialog dialog = builder.create();
			dialog.show();

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
			mQueryUsers.cancel();
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
