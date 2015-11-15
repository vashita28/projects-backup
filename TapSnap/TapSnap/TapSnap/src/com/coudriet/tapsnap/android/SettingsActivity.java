package com.coudriet.tapsnap.android;

import java.util.ArrayList;
import java.util.TreeSet;

import com.coudriet.tapsnap.utility.Common;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnItemClickListener {
	public static String TAG = "SettingsActivity";

	private ActionBar actionBar;
	private ListView mListviewSettings;
	private ParseUser currentUser;

	public static int MY_REQUEST_CODE = 1;

	SharedPreferences sharedpreferences;
	boolean isFblogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		sharedpreferences = getSharedPreferences(SignUpActivity.MyPREFERENCES,
				Context.MODE_PRIVATE);

		mListviewSettings = (ListView) findViewById(R.id.settingsListView);

		if (ParseUser.getCurrentUser() != null)
			currentUser = ParseUser.getCurrentUser();

		MyCustomAdapter mAdapter;

		mAdapter = new MyCustomAdapter();

		mAdapter.addSeparatorItem("CURRENT ACCOUNT:");
		mAdapter.addItem("Username: " + currentUser.getUsername());

		if (currentUser.getEmail() == null) {
			mAdapter.addItem("Email: " + "Change Email Id via FB.");
		} else
			mAdapter.addItem("Email: " + currentUser.getEmail());
		mAdapter.addItem("Change Profile Picture ");

		mAdapter.addSeparatorItem("SPREAD THE WORD:");
		mAdapter.addItem("Invite Your Friends");

		mAdapter.addSeparatorItem("MORE INFO:");
		mAdapter.addItem("Terms of Use");
		mAdapter.addItem("Privacy Policy");
		mAdapter.addItem("Getting Started");

		mListviewSettings.setAdapter(mAdapter);
		// setListAdapter(mAdapter);

		mListviewSettings.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (position == 1) {
			isFblogin = sharedpreferences.getBoolean(
					ParseConstants.KEY_LOGIN_VIA_FACEBOOK, false);
			if (isFblogin) {

				String msg = "Username can be changed via facebook.";
				String tit = getString(R.string.signup_error_title);
				Common.callDialog(SettingsActivity.this, msg, tit);
			} else {

				Intent intent = new Intent(SettingsActivity.this,
						EditUsernameActivity.class);
				intent.putExtra("USERNAME", currentUser.getUsername());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}

		if (position == 2) {
			isFblogin = sharedpreferences.getBoolean(
					ParseConstants.KEY_LOGIN_VIA_FACEBOOK, false);
			if (isFblogin) {

				String msg = "Email can be changed via facebook.";
				String tit = getString(R.string.signup_error_title);
				Common.callDialog(SettingsActivity.this, msg, tit);

			} else {

				Intent intent = new Intent(SettingsActivity.this,
						EditEmailActivity.class);
				if (currentUser.getEmail() != null) {
					intent.putExtra("EMAIL", currentUser.getEmail());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
			}
		}

		if (position == 3) {
			Intent intent = new Intent(SettingsActivity.this,
					EditProfilePictureActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

		if (position == 5) {
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.putExtra(Intent.EXTRA_SUBJECT,
					"Hey! Found this awesome app!");
			intent.putExtra(android.content.Intent.EXTRA_TEXT, Html
					.fromHtml(getResources().getString(R.string.email_text)));
			intent.setType("plain/text");
			startActivity(Intent.createChooser(intent, ""));

			// startActivityForResult(Intent.createChooser(intent, "a"),
			// MY_REQUEST_CODE);

		}

		if (position == 7) {
			Intent intent = new Intent(SettingsActivity.this,
					TermsOfUseActivity.class);
			startActivity(intent);
		}

		if (position == 8) {
			Intent intent = new Intent(SettingsActivity.this,
					PrivacyPolicyActivity.class);
			startActivity(intent);
		}

		if (position == 9) {
			Intent intent = new Intent(SettingsActivity.this,
					GettingStartedActivity.class);
			startActivity(intent);
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// Log.e("onActivityResult", "onActivityResult: requestCode "
	// + requestCode + "  resultCode, " + resultCode + ", "
	// + (data != null ? data.toString() : "empty intent"));
	//
	// if (requestCode == 1 && resultCode == Activity.RESULT_OK) {//
	// Activity.RESULT_OK
	// Toast.makeText(SettingsActivity.this, "Email sent successfully.",
	// Toast.LENGTH_SHORT).show();
	//
	// } else if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
	// Toast.makeText(SettingsActivity.this, "Email canceled.",
	// Toast.LENGTH_SHORT).show();
	//
	// } else {
	// Toast.makeText(SettingsActivity.this, "Please try again.",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// // finish(); // to end your activity when toast is shown
	// }

	// Adapter Class
	private class MyCustomAdapter extends BaseAdapter {

		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
		private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

		private ArrayList<String> mData = new ArrayList<String>();
		private LayoutInflater mInflater;

		private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

		public MyCustomAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addItem(final String item) {
			mData.add(item);
			notifyDataSetChanged();
		}

		public void addSeparatorItem(final String item) {
			mData.add(item);
			// save separator position
			mSeparatorsSet.add(mData.size() - 1);
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR
					: TYPE_ITEM;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_MAX_COUNT;
		}

		public int getCount() {
			return mData.size();
		}

		public String getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int type = getItemViewType(position);
			System.out.println("getView " + position + " " + convertView
					+ " type = " + type);
			if (convertView == null) {
				holder = new ViewHolder();
				switch (type) {
				case TYPE_ITEM:
					convertView = mInflater.inflate(R.layout.settings_item,
							null);
					holder.textView = (TextView) convertView
							.findViewById(R.id.text);
					holder.textView
							.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
					break;
				case TYPE_SEPARATOR:
					convertView = mInflater.inflate(R.layout.settings_header,
							null);
					holder.textView = (TextView) convertView
							.findViewById(R.id.textSeparator);
					holder.textView
							.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
					break;
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(mData.get(position));
			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView textView;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	private void setupActionBar() {
		Log.e("SettingsActvity", "setupActionBar");
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

}
