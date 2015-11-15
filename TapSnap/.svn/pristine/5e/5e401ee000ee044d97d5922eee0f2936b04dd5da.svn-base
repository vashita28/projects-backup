package com.coudriet.tapsnap.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends ListActivity {

	public static final String TAG = EditFriendsActivity.class.getSimpleName();

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<ParseUser> mUsers;
	EditText etSearch;
	ArrayAdapter<String> adapter;

	private ActionBar actionBar;
	String currentUserID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);

		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));
		// actionBar.setDisplayShowTitleEnabled(false);

		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.setText("");
		mUsers = new ArrayList<ParseUser>();

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		etSearch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				etSearch.setCursorVisible(true);
				// etSearch.setText("");
				return false;
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (adapter != null) {
					adapter.getFilter().filter(s);
				}
				if (s == null || s.length() == 0) {
				} else {
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (AppStatus.getInstance(EditFriendsActivity.this).isOnline()) {

			mCurrentUser = ParseUser.getCurrentUser();
			mFriendsRelation = mCurrentUser
					.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

			setProgressBarIndeterminateVisibility(true);

			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.orderByAscending(ParseConstants.KEY_USERNAME);
			query.setLimit(1000);
			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> users, ParseException e) {
					setProgressBarIndeterminateVisibility(false);

					if (e == null) {
						// Success
						mUsers = users;
						if (ParseUser.getCurrentUser() != null) {
							currentUserID = ParseUser.getCurrentUser()
									.getObjectId();
						}
						if (mUsers != null && currentUserID != null) {
							for (int i = 0; i < mUsers.size(); i++) {
								if (mUsers.get(i).getObjectId()
										.equals(currentUserID))
									mUsers.remove(i);
							}
						}
						String[] usernames = new String[mUsers.size()];
						int i = 0;
						for (ParseUser user : mUsers) {
							usernames[i] = user.getUsername();
							i++;
						}
						adapter = new ArrayAdapter<String>(
								EditFriendsActivity.this,
								android.R.layout.simple_list_item_checked,
								usernames);
						setListAdapter(adapter);

						addFriendCheckmarks();
					} else {

						/*
						 * Log.e(TAG, e.getMessage()); AlertDialog.Builder
						 * builder = new
						 * AlertDialog.Builder(EditFriendsActivity.this);
						 * builder.setMessage(R.string.user_not_found)
						 * .setTitle(R.string.error_title)
						 * .setPositiveButton(android.R.string.ok, null);
						 * AlertDialog dialog = builder.create(); dialog.show();
						 */

						String msg = getString(R.string.user_not_found);
						String tit = getString(R.string.error_title);

						Common.callDialog(getListView().getContext(), msg, tit);
					}
				}
			});

		} else {

			Toast.makeText(EditFriendsActivity.this,
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
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (getListView().isItemChecked(position)) {
			// add the friend
			mFriendsRelation.add(mUsers.get(position));
		} else {
			// remove the friend
			mFriendsRelation.remove(mUsers.get(position));
		}

		mCurrentUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getMessage());
				}
			}
		});
	}

	private void addFriendCheckmarks() {
		mFriendsRelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						if (e == null) {
							// list returned - look for a match
							for (int i = 0; i < mUsers.size(); i++) {
								ParseUser user = mUsers.get(i);

								for (ParseUser friend : friends) {
									if (friend.getObjectId().equals(
											user.getObjectId())) {
										getListView().setItemChecked(i, true);
									}
								}
							}
						} else {
							Toast.makeText(EditFriendsActivity.this,
									R.string.connectivity_error, Toast.LENGTH_SHORT).show();
							Log.e(TAG, e.getMessage());
						}
					}
				});
	}

	void hideSoftKeyBoard() {
		try {
			InputMethodManager imm = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(), 0);
			// imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
		} catch (Exception e) {

		}
	}
}
