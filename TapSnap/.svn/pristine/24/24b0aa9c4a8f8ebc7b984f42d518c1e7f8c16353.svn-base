package com.coudriet.snapnshare.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.snapshare.utility.AppStatus;
import com.snapshare.utility.Common;

public class RecipientsActivity extends ListActivity {

	public static final String TAG = RecipientsActivity.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseRelation<ParseUser> mRecipientsRelation;
	protected ParseUser mCurrentUser;
	protected List<ParseUser> mFriends;
	protected List<ParseUser> mOtherFriends;
	protected MenuItem mSendMenuItem;
	protected Uri mMediaUri;
	protected String mFileType;
	private ArrayList<ParseUser> mFriendsFound;
	private ArrayList<ParseUser> mFriendsFinal;
	private byte[] fileBytes;

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recipients);
		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		if (!Common.flag) {
			mMediaUri = getIntent().getData();
			mFileType = getIntent().getExtras().getString(
					ParseConstants.KEY_FILE_TYPE);
		} else {
			mFileType = ParseConstants.TYPE_VIDEO;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (AppStatus.getInstance(RecipientsActivity.this).isOnline()) {

			mFriendsRelation = ParseUser.getCurrentUser().getRelation(
					ParseConstants.KEY_FRIENDS_RELATION);

			mFriendsFound = new ArrayList<ParseUser>();

			setProgressBarIndeterminateVisibility(true);

			ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
			query.addAscendingOrder(ParseConstants.KEY_USERNAME);
			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> friends, ParseException e) {
					setProgressBarIndeterminateVisibility(false);

					if (e == null) {
						// Success
						mFriends = friends;

						for (final ParseUser user : mFriends) {

							mRecipientsRelation = user
									.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

							ParseQuery<ParseUser> query = mRecipientsRelation
									.getQuery();
							query.addAscendingOrder(ParseConstants.KEY_USERNAME);
							query.findInBackground(new FindCallback<ParseUser>() {

								@Override
								public void done(List<ParseUser> otherFriends,
										ParseException e) {

									if (e == null) {
										// Success
										mOtherFriends = otherFriends;

										mCurrentUser = ParseUser
												.getCurrentUser();

										for (ParseUser friend : mOtherFriends) {

											if (ParseUser.getCurrentUser() != null) {
												if (friend.getObjectId()
														.equals(mCurrentUser
																.getObjectId())) {
													// Log.i(TAG,
													// "Friend found");

													mFriendsFound.add(user);

													HashSet<ParseUser> listWithoutDuplicates = new HashSet<ParseUser>(
															mFriendsFound);

													mFriendsFinal = new ArrayList<ParseUser>(
															listWithoutDuplicates);

													Log.e(TAG,
															"*******mFriendsFinal*********"
																	+ mFriendsFinal);

													// for (int i = 0; i <
													// mFriendsFinal
													// .size(); i++) {
													// Log.e("mFriendsFinal",
													// "BEFORE:: "
													// + mFriendsFinal
													// .get(i)
													// .getUsername());
													// }

													String currentUserID = null;
													if (ParseUser
															.getCurrentUser() != null) {
														currentUserID = ParseUser
																.getCurrentUser()
																.getObjectId();
													}
													if (mFriendsFinal != null
															&& currentUserID != null) {
														for (int i = 0; i < mFriendsFinal
																.size(); i++) {
															if (mFriendsFinal
																	.get(i)
																	.getObjectId()
																	.equals(currentUserID))
																mFriendsFinal
																		.remove(i);
														}
													}

													// for (int i = 0; i <
													// mFriendsFinal
													// .size(); i++) {
													// Log.e("mFriendsFinal",
													// "AFTER:: "
													// + mFriendsFinal
													// .get(i)
													// .getUsername());
													// }

													String[] usernames = new String[mFriendsFinal
															.size()];
													int i = 0;
													for (ParseUser user : mFriendsFinal) {
														usernames[i] = user
																.getUsername();
														i++;
													}

													// System.out.println(usernames);

													ArrayAdapter<String> adapter = new ArrayAdapter<String>(
															getListView()
																	.getContext(),
															android.R.layout.simple_list_item_checked,
															usernames);
													setListAdapter(adapter);
												}
											}
										}
									}
								}
							});
						}
					}
				}
			});

		} else {

			Toast.makeText(RecipientsActivity.this,
					R.string.connectivity_error, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		mSendMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_send:
			ParseObject message = createMessage();
			if (message == null) {
				// error
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.error_selecting_file)
						.setTitle(R.string.error_selecting_file_title)
						.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				send(message);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (l.getCheckedItemCount() > 0) {
			mSendMenuItem.setVisible(true);
		} else {
			mSendMenuItem.setVisible(false);
		}
	}

	protected ParseObject createMessage() {

		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
		message.put(ParseConstants.KEY_FILE_TYPE, mFileType);
		message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser()
				.getObjectId());
		message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser()
				.getUsername());

		if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
			fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		} else {
			String filePathVideo = Environment.getExternalStorageDirectory()
					+ File.separator + "VID_.3gp";

			try {
				fileBytes = Common.readBytes(filePathVideo);
			} catch (Exception e) {
				Log.e("Exception", e.toString());
			}

		}

		if (fileBytes == null) {
			return null;
		} else {
			if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);

				String fileName = FileHelper.getFileName(this, mMediaUri,
						mFileType);
				ParseFile file = new ParseFile(fileName, fileBytes);
				message.put(ParseConstants.KEY_FILE, file);
			} else {

				// String fileName = FileHelper.getFileName(this, mMediaUri,
				// mFileType);
				ParseFile file = new ParseFile("Video", fileBytes);
				message.put(ParseConstants.KEY_FILE, file);
			}

			return message;
		}
	}

	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		for (int i = 0; i < getListView().getCount(); i++) {
			if (getListView().isItemChecked(i)) {
				recipientIds.add(mFriendsFinal.get(i).getObjectId());
			}
		}

		return recipientIds;
	}

	protected ArrayList<String> getRecipientNames() {
		ArrayList<String> recipientNames = new ArrayList<String>();
		for (int i = 0; i < getListView().getCount(); i++) {
			if (getListView().isItemChecked(i)) {
				recipientNames.add(mFriendsFinal.get(i).getUsername());
			}
		}

		return recipientNames;
	}

	protected void send(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {

				if (e == null) {
					// success!
					Toast.makeText(RecipientsActivity.this,
							R.string.success_message, Toast.LENGTH_LONG).show();

					if (ParseUser.getCurrentUser() != null) {
						String alertMessage = ParseUser.getCurrentUser()
								.getUsername()
								+ " sent you a snap "
								+ mFileType + "!";

						// Notification for Android & iOS users
						JSONObject data = new JSONObject();
						try {
							data.put("alert", alertMessage);
							data.put("badge", "Increment");
							data.put("sound", "shutterClick.wav");

							ParsePush push = new ParsePush();
							push.setChannels(getRecipientNames()); // Notice we
																	// use
																	// setChannels
																	// not
																	// setChannel
							push.setData(data);
							push.sendInBackground();

						} catch (Exception e1) {
							Log.e("Exception: ", e1.toString());
						}
					}
					// System.out.println(getRecipientNames());
				} else {

					/*
					 * AlertDialog.Builder builder = new
					 * AlertDialog.Builder(RecipientsActivity.this);
					 * builder.setMessage(R.string.error_sending_message)
					 * .setTitle(R.string.error_selecting_file_title)
					 * .setPositiveButton(android.R.string.ok, null);
					 * AlertDialog dialog = builder.create(); dialog.show();
					 */

					// Toast.makeText(RecipientsActivity.this,
					// R.string.error_sending_message,
					// Toast.LENGTH_SHORT).show();

					String msg = getString(R.string.error_sending_message);
					String tit = getString(R.string.error_selecting_file_title);

					Common.callDialog(RecipientsActivity.this, msg, tit);
				}
			}
		});
	}
}
