package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;

public class MainActivity extends TabActivity {
	private TabSpec tspec;
	private TabHost tabHost;
	static String szXMLResponse = "";
	Context context;

	int nReportDetails = 0;
	int nSummary = 1;
	int nServiceChecklist = 2;
	int nPhotographs = 3;
	int nSiteConditions = 4;
	int nArchiveReports = 5;
	int nSyncWithServer = 6;
	StringBuilder text;
	public static Handler mHandler;

	ProgressBar progress_RecreatingDataFromExistingReport;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = MainActivity.this;
		// if (getIntent().getExtras() != null) {
		// szXMLResponse = getIntent().getStringExtra("xmlresponse");
		// Log.d("MainActivity", "XML DATA :: " + szXMLResponse);
		// }

		progress_RecreatingDataFromExistingReport = (ProgressBar) findViewById(R.id.progress_loading_data);
		progress_RecreatingDataFromExistingReport.setVisibility(View.VISIBLE);

		AppValues.AppContext = MainActivity.this;

		new PopulateUITask().execute();

		getTabHost().setup();
		tabHost = getTabHost();

		// getTabWidget().setStripEnabled(false);
		tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

	}

	public static void logout() {
		Util.setLoggedIn(AppValues.AppContext, false);
		Util.setLogoutValues(AppValues.AppContext);
		// if (AppValues.getReportXMLFile().exists())
		// AppValues.getReportXMLFile().delete();

		// AppValues.AppContext.getContentResolver().delete(
		// DataProvider.Material_Log.CONTENT_URI, null, null);
		// AppValues.AppContext.getContentResolver().delete(
		// DataProvider.Mill.CONTENT_URI, null, null);
		// AppValues.AppContext.getContentResolver().delete(
		// DataProvider.Services.CONTENT_URI, null, null);
		// AppValues.AppContext.getContentResolver().delete(
		// DataProvider.Tasks.CONTENT_URI, null, null);
		// AppValues.AppContext.getContentResolver().delete(
		// DataProvider.Child_Leaf.CONTENT_URI, null, null);
	}

	void ToggleTabsSelection(ImageView icon_Reports_Details,
			ImageView icon_Summary, ImageView icon_Checklist,
			ImageView icon_Photographs, ImageView icon_Site_Condition,
			ImageView icon_Reports, ImageView icon_Sync_With_Server) {
		if (!Util.getIsReportXMLDownloaded(MainActivity.this)
				&& !AppValues.getReportXMLFile().exists()) {
			tabHost.getTabWidget().getChildAt(nReportDetails).setEnabled(false);
			// tabHost.getTabWidget()
			// .getChildAt(nReportDetails)
			// .setBackground(
			// getResources().getDrawable(
			// R.drawable.btn_inactive_report_details));
			// icon_Reports_Details.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_reportdetails));
			tabHost.getTabWidget().getChildAt(nSummary).setEnabled(false);
			// icon_Summary.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_summary));

			tabHost.getTabWidget().getChildAt(nServiceChecklist)
					.setEnabled(false);
			// icon_Checklist.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_service_checklist));

			tabHost.getTabWidget().getChildAt(nPhotographs).setEnabled(false);
			// icon_Photographs.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_photographs));

			tabHost.getTabWidget().getChildAt(nSiteConditions)
					.setEnabled(false);
			// icon_Site_Condition.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_site_conditions));
			tabHost.getTabWidget().getChildAt(nArchiveReports)
					.setEnabled(false);
			// icon_Reports.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_archive_reports));
			icon_Sync_With_Server
					.setImageResource(R.drawable.tab_focused_green);
			tabHost.getTabWidget().getChildAt(nSyncWithServer).setEnabled(true);

			tabHost.setCurrentTab(0);

		} else {

			tabHost.getTabWidget().getChildAt(nReportDetails).setEnabled(true);
			// icon_Reports_Details.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_report_details));

			tabHost.getTabWidget().getChildAt(nSummary).setEnabled(true);
			// icon_Summary.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_summary));

			tabHost.getTabWidget().getChildAt(nServiceChecklist)
					.setEnabled(true);
			// icon_Checklist.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_service_checklist));

			tabHost.getTabWidget().getChildAt(nPhotographs).setEnabled(true);
			// icon_Photographs.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_photographs));

			tabHost.getTabWidget().getChildAt(nSiteConditions).setEnabled(true);
			// icon_Site_Condition.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_site_conditions));

			tabHost.getTabWidget().getChildAt(nArchiveReports).setEnabled(true);
			// icon_Reports.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_inactive_archive_reports));
			icon_Sync_With_Server
					.setImageResource(R.drawable.tab_unfocused_purple);
			tabHost.getTabWidget().getChildAt(nSyncWithServer)
					.setEnabled(false);

			// icon_Sync_With_Server.setImageDrawable(getResources().getDrawable(
			// R.drawable.btn_active_sync_with_server));
		}

	}

	class PopulateUITask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (AppValues.getReportXMLFile().exists()) {
				text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							AppValues.getReportXMLFile()));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					szXMLResponse = text.toString();
					new ResponseParser(MainActivity.this)
							.parse_Header_Details(szXMLResponse);

					br.close();
					// if (AppValues.XMLResponse.equals(""))
					// Log.d("MainActivity", " XML IS :: " + szXMLResponse);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			progress_RecreatingDataFromExistingReport.setVisibility(View.GONE);

			// Report Details Tab
			tspec = tabHost.newTabSpec("Report Details");
			Intent intent_Report_Details = new Intent(getBaseContext(),
					Report_Details.class);
			intent_Report_Details.putExtra("parseData", szXMLResponse);
			// startActivity(intent_Report_Details);
			// Intent intent_Report_Details = new Intent().setClass(this,
			// Report_Details.class);

			View tabIndicator = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.tab_indicator, null, false);
			final ImageView icon_Reports_Details = (ImageView) tabIndicator
					.findViewById(R.id.icon);
			TextView txtTitle_ReportDetails = (TextView) tabIndicator
					.findViewById(R.id.textview_title);
			txtTitle_ReportDetails.setText("Report Details");
			txtTitle_ReportDetails.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Reports_Details.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator);
			tspec.setContent(intent_Report_Details);
			tabHost.addTab(tspec);

			// Summary tab
			tspec = tabHost.newTabSpec("Summary");
			// Intent intent_Summary = new Intent().setClass(this,
			// Summary.class);
			Intent intent_Summary = new Intent(getBaseContext(), Summary.class);
			intent_Summary.putExtra("parseData", szXMLResponse);
			View tabIndicator1 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Summary = (ImageView) tabIndicator1
					.findViewById(R.id.icon);
			TextView txtTitle_Summary = (TextView) tabIndicator1
					.findViewById(R.id.textview_title);
			txtTitle_Summary.setText("Summary");
			txtTitle_Summary.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Summary.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator1);
			tspec.setContent(intent_Summary);
			tabHost.addTab(tspec);

			// Service Checklist tab
			tspec = tabHost.newTabSpec("Service Checklist");
			Intent intent_Checklist = new Intent().setClass(MainActivity.this,
					Service_CheckList_Without_Swipe.class);
			View tabIndicator2 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Checklist = (ImageView) tabIndicator2
					.findViewById(R.id.icon);
			TextView txtTitle_ServiceChecklist = (TextView) tabIndicator2
					.findViewById(R.id.textview_title);
			txtTitle_ServiceChecklist.setText("Service Checklist");
			txtTitle_ServiceChecklist.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Checklist.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator2);
			tspec.setContent(intent_Checklist);
			tabHost.addTab(tspec);

			// Photographs tab
			tspec = tabHost.newTabSpec("Photographs");
			Intent intent_Photographs = new Intent().setClass(
					MainActivity.this, Photographs.class);
			View tabIndicator3 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Photographs = (ImageView) tabIndicator3
					.findViewById(R.id.icon);
			TextView txtTitle_Photographs = (TextView) tabIndicator3
					.findViewById(R.id.textview_title);
			txtTitle_Photographs.setText("Photographs");
			txtTitle_Photographs.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Photographs.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator3);
			tspec.setContent(intent_Photographs);
			tabHost.addTab(tspec);

			// Site Conditions tab
			tspec = tabHost.newTabSpec("Site Conditions");
			Intent intent_Site_conditions = new Intent().setClass(
					MainActivity.this, Site_Conditions.class);
			View tabIndicator4 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Site_Condition = (ImageView) tabIndicator4
					.findViewById(R.id.icon);
			TextView txtTitle_SiteConditions = (TextView) tabIndicator4
					.findViewById(R.id.textview_title);
			txtTitle_SiteConditions.setText("Site Conditions");
			txtTitle_SiteConditions.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Site_Condition.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator4);
			tspec.setContent(intent_Site_conditions);
			tabHost.addTab(tspec);

			// Archive Reports tab
			tspec = tabHost.newTabSpec("Archive Reports");
			Intent intent_Archive_Reports = new Intent().setClass(
					MainActivity.this, Archive_Reports.class);
			View tabIndicator5 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Reports = (ImageView) tabIndicator5
					.findViewById(R.id.icon);
			TextView txtTitle_ArchiveReports = (TextView) tabIndicator5
					.findViewById(R.id.textview_title);
			txtTitle_ArchiveReports.setText("Archive Reports");
			txtTitle_ArchiveReports.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			icon_Reports.setImageResource(R.drawable.tab_selector);

			tspec.setIndicator(tabIndicator5);
			tspec.setContent(intent_Archive_Reports);
			tabHost.addTab(tspec);

			// Sync With Server tab
			tspec = tabHost.newTabSpec("Sync with server");
			Intent intent_Sync_with_server = new Intent().setClass(
					MainActivity.this, Sync_With_Server.class);
			View tabIndicator6 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.tab_indicator, null, false);
			final ImageView icon_Sync_With_Server = (ImageView) tabIndicator6
					.findViewById(R.id.icon);
			TextView txtTitle_Syncwithserver = (TextView) tabIndicator6
					.findViewById(R.id.textview_title);
			txtTitle_Syncwithserver.setText("Sync with server");
			txtTitle_Syncwithserver.setTextColor(getResources().getColor(
					R.color.title_bar_data_text_color));
			// icon_Sync_With_Server.setImageResource(R.drawable.tab_selector_sync_with_server);

			tspec.setIndicator(tabIndicator6);
			tspec.setContent(intent_Sync_with_server);
			tabHost.addTab(tspec);

			ToggleTabsSelection(icon_Reports_Details, icon_Summary,
					icon_Checklist, icon_Photographs, icon_Site_Condition,
					icon_Reports, icon_Sync_With_Server);
			// tspec.setIndicator("",
			// ressources.getDrawable(R.layout.sync_with_server_tab));

			// set Windows tab as default (zero based)
			// tabHost.setCurrentTab(0);

			mHandler = new Handler() {
				@Override
				public void handleMessage(Message message) {
					if (message.what == 777) {
						icon_Sync_With_Server
								.setImageResource(R.drawable.tab_focused_green);
						tabHost.getTabWidget().getChildAt(nSyncWithServer)
								.setEnabled(true);
						if (Sync_With_Server.mHandler != null)
							Sync_With_Server.mHandler
									.sendEmptyMessage(Sync_With_Server.ALL_TASKS_COMPLETED);
					} else {
						ToggleTabsSelection(icon_Reports_Details, icon_Summary,
								icon_Checklist, icon_Photographs,
								icon_Site_Condition, icon_Reports,
								icon_Sync_With_Server);
					}
					super.handleMessage(message);
				}
			};

			if (!Util.getReportID(MainActivity.this).equals("")) {
				// Condition to check for all completed tasks
				Cursor cursor = getContentResolver().query(
						DataProvider.Tasks.CONTENT_URI, null, null, null, null);
				cursor.moveToFirst();

				Cursor taskCursor = getContentResolver().query(
						DataProvider.Tasks.CONTENT_URI,
						null,
						DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
								+ DataProvider.Tasks.TASK_COMPLETED + " = ?",
						new String[] { Util.getReportID(MainActivity.this),
								"false" }, null);
				taskCursor.moveToFirst();
				if (cursor.getCount() > 0 && taskCursor.getCount() == 0) {
					if (MainActivity.mHandler != null)
						MainActivity.mHandler.sendEmptyMessage(777);
				}
				taskCursor.close();
				cursor.close();
			}

		}

	}
}
