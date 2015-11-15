package co.uk.pocketapp.gmd.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;

public class ParentActivity extends Activity {
	private Activity mActivity;

	TextView mill_Name, site_Name, dateAndTime;
	Button logout_Button;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;
	}

	protected void initWidgets() {

		// mill name
		mill_Name = (TextView) findViewById(R.id.millName);
		mill_Name.setTypeface(GMDApplication.fontText);

		// Site name
		site_Name = (TextView) findViewById(R.id.siteName);
		site_Name.setTypeface(GMDApplication.fontText);

		// Date Time
		dateAndTime = (TextView) findViewById(R.id.DateNtime);

		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			String reportDate = dateFormat.format(today);
			dateAndTime.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Logout Button
		logout_Button = (Button) findViewById(R.id.LogOutButton);

		logout_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getBaseContext(),
						Activity_login.class);
				startActivity(myIntent);
				MainActivity.logout();
				finish();
			}
		});

		// Getting data from share preferences and show on site name and mill
		// name textview:
		site_Name.setText(Util.getSiteName(mActivity));
		mill_Name.setText(Util.getMillName(mActivity));

		mill_Name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> millList = new ArrayList<String>();
				Cursor millCursor = getContentResolver().query(
						DataProvider.Mill.CONTENT_URI, null, null, null, null);
				if (millCursor != null && millCursor.moveToFirst()) {
					do {
						millList.add(millCursor.getString(millCursor
								.getColumnIndex(DataProvider.Mill.MILL_NAME)));
					} while (millCursor.moveToNext());
				}
				millCursor.close();

				if (millList.size() > 0) {
					final String[] arrayOfStrings;
					arrayOfStrings = millList.toArray(new String[millList
							.size()]);
					final Dialog dialog;

					dialog = new Dialog(ParentActivity.this);

					dialog.setContentView(R.layout.mill_dialog);

					dialog.setTitle("Select Mill");
					final ListView listview_mill_list = (ListView) dialog
							.findViewById(R.id.list_mills);

					listview_mill_list.setAdapter(new ArrayAdapter<String>(
							mActivity, R.layout.row_mill_dialog,
							R.id.textview_millname, arrayOfStrings));

					listview_mill_list
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int item, long arg3) {
									String szMillName = arrayOfStrings[item]
											.toString();
									Util.setMillName(ParentActivity.this,
											szMillName);
									parseAndShowData();
									if (mill_Name != null)
										mill_Name.setText(szMillName);
									dialog.dismiss();
								}

							});

					dialog.show();

				}
			}
		});
	}

	public void parseAndShowData() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		if (!Util.getIsReportReassigned(mActivity))
			menu.removeItem(R.id.rejection_comments);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.killreport:

			Intent killReportIntent = new Intent(ParentActivity.this,
					KillReport.class);
			startActivity(killReportIntent);
			break;

		case R.id.rejection_comments:
			String szMessage = StringEscapeUtils
					.unescapeXml(ResponseParser.rejectedCommentsMap.get(Util
							.getMillID(mActivity, mill_Name.getText()
									.toString())));
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					ParentActivity.this)
					.setTitle("Comments for report rejection: ")
					.setMessage(szMessage)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {

								}
							}).create();
			m_AlertDialog.setCanceledOnTouchOutside(false);
			m_AlertDialog.show();

			// case R.id.choosemill:
			//
			// ArrayList<String> millList = new ArrayList<String>();
			// Cursor millCursor = getContentResolver().query(
			// DataProvider.Mill.CONTENT_URI, null, null, null, null);
			// if (millCursor != null && millCursor.moveToFirst()) {
			// do {
			// millList.add(millCursor.getString(millCursor
			// .getColumnIndex(DataProvider.Mill.MILL_NAME)));
			// } while (millCursor.moveToNext());
			// }
			// millCursor.close();
			//
			// final String[] arrayOfStrings;
			// arrayOfStrings = millList.toArray(new String[millList.size()]);
			// final Dialog dialog;
			//
			// dialog = new Dialog(ParentActivity.this);
			//
			// dialog.setContentView(R.layout.mill_dialog);
			//
			// dialog.setTitle("Select Mill");
			// final ListView listview_mill_list = (ListView) dialog
			// .findViewById(R.id.list_mills);
			//
			// listview_mill_list.setAdapter(new ArrayAdapter<String>(this,
			// R.layout.row_mill_dialog, R.id.textview_millname,
			// arrayOfStrings));
			//
			// listview_mill_list
			// .setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int item, long arg3) {
			// String szMillName = arrayOfStrings[item].toString();
			// Util.setMillName(ParentActivity.this, szMillName);
			// if (mill_Name != null)
			// mill_Name.setText(szMillName);
			// dialog.dismiss();
			// }
			//
			// });
			//
			// dialog.show();
			//
			// break;
		}

		return true;
	}

}
