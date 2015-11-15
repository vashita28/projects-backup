package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.adapter.Custom_Adapter_Archive_Reports;
import co.uk.pocketapp.gmd.receiver.NetworkBroadcastReceiver;
import co.uk.pocketapp.gmd.tasks.Download_PDF_Task;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class Archive_Reports extends ParentActivity {
	private static final String TAG = "ARCHIVE REPORTS";
	ListView archive_Reports_Listview;
	Custom_Adapter_Archive_Reports custom_Adaptor;

	View view_archive_reports_top_bar;
	TextView textview_title;
	TextView textview_dateAndTime, textview_GMD_Heading;

	File file_Archive_Reports;
	int num = 0;
	int i = 0;
	ArrayList<String> fileArrayList;
	ArrayList<Data_Model_Archive_Reports> listArray;
	TextView report_Title_TextView;

	String streport_Title;
	String stdate;
	String szFileName;
	String reportDate;

	LinearLayout ll_Archive_Reports;

	// LinearLayout ll_Webview;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	ProgressBar progressArchiveReports;

	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.archive_reports);

		mContext = Archive_Reports.this;
		progressArchiveReports = (ProgressBar) findViewById(R.id.progress_archive_reports);
		ll_Archive_Reports = (LinearLayout) findViewById(R.id.ll_listview_archivereports);
		// ll_Webview = (LinearLayout) findViewById(R.id.ll_webview);

		view_archive_reports_top_bar = (View) findViewById(R.id.view_archive_reports_top_bar);
		textview_title = (TextView) view_archive_reports_top_bar
				.findViewById(R.id.pageTitle);
		textview_title.setText("Archive Reports");

		// Intent sole_plate_Intent = new Intent(getApplicationContext(),
		// Activity_Magnetic_Centering.class);
		// startActivity(sole_plate_Intent);

		// font implementation of main heading "GMD": textview
		textview_GMD_Heading = (TextView) findViewById(R.id.Heading);
		textview_GMD_Heading.setTypeface(GMDApplication.fontHeading);

		textview_dateAndTime = (TextView) view_archive_reports_top_bar
				.findViewById(R.id.DateNtime);

		archive_Reports_Listview = (ListView) findViewById(R.id.archive_Reports_listview);
		// Remove the default listview divider:
		archive_Reports_Listview.setEmptyView(findViewById(android.R.id.empty));
		archive_Reports_Listview.setDivider(null);

		// report_Title_TextView=(TextView)findViewById(R.id.report_title_TextView);
		// date_N_Time_TextView=(TextView)findViewById(R.id.dateNtime_TextView);

		// archive_Reports_Listview.setAdapter(custom_Adaptor);

		archive_Reports_Listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						textview_title.setText("PDF View");

						TextView txtReportTitle = (TextView) arg1
								.findViewById(R.id.report_title_TextView);

						File file = new File(AppValues.getDirectory()
								+ File.separator + AppValues.APP_NAME
								+ "/Archive Reports/"
								+ txtReportTitle.getText().toString() + ".pdf");

						// File file = new File(AppValues.getDirectory()
						// + File.separator + AppValues.APP_NAME
						// + "/Archive_Reports/report1.pdf");

						// EncodeDecodeAES decrypted_File = new
						// EncodeDecodeAES();
						Log.v(TAG, "FILE PATH_---->"
								+ file.getPath().toString());
						// decrypted_File.decrypt(file);
						// String file_Path = Environment
						// .getExternalStorageDirectory()
						// + File.separator
						// + AppValues.APP_NAME
						// + "/Archive_Reports/report1.pdf";

						// String file_Path = AppValues.getDirectory()
						// + File.separator + AppValues.APP_NAME
						// + "/Archive_Reports/report1.pdf";
						// Log.v(TAG, file.getPath().toString());
						// Log.v(TAG, file_Path);
						//
						if (!Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							Log.d("", "No SDCARD");
						} else {

							final Uri uri = Uri.fromFile(file);
							Log.v(TAG, "File URI :: " + uri.toString());

							if (uri.toString().contains(".pdf")) {
								// Uri path = Uri.parse(url);
								Intent pdfIntent = new Intent(
										Intent.ACTION_VIEW);
								pdfIntent
										.setDataAndType(uri, "application/pdf");
								pdfIntent
										.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								try {
									startActivity(pdfIntent);
								} catch (ActivityNotFoundException e) {
									Toast.makeText(Archive_Reports.this,
											"No PDF application found",
											Toast.LENGTH_SHORT).show();
								} catch (Exception otherException) {
									Toast.makeText(Archive_Reports.this,
											"Unknown error", Toast.LENGTH_SHORT)
											.show();
								}

							}

						}
					}
				});
	}

	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					inFiles.addAll(getListFiles(file));
				} else {
					// if (file.getName().endsWith(".csv")) {
					// inFiles.add(file);
					// }
					inFiles.add(file);
				}
			}
		}

		return inFiles;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			reportDate = dateFormat.format(today);
			textview_dateAndTime.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (!Util.getPDFListDownloaded(Archive_Reports.this)) {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				progressArchiveReports.setVisibility(View.VISIBLE);
				archive_Reports_Listview.getEmptyView().setVisibility(
						View.INVISIBLE);
				Download_PDF_Task pdf_Task = new Download_PDF_Task(
						progressArchiveReports, Archive_Reports.this);
				pdf_Task.execute();
			} else {
				progressArchiveReports.setVisibility(View.GONE);
				Toast.makeText(Archive_Reports.this, "No network connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			progressArchiveReports.setVisibility(View.GONE);
			setPDFList();
		}

	}

	public void setPDFList() {
		if (mContext != null) {
			listArray = new ArrayList<Data_Model_Archive_Reports>();

			// Displaying files from ARCHIVE FOLDER in listview
			// file_Archive_Reports = new File(
			// Environment.getExternalStorageDirectory() + File.separator
			// + AppValues.APP_NAME + "/Archive_Reports/");
			file_Archive_Reports = new File(AppValues.getDirectory()
					+ File.separator + AppValues.APP_NAME + "/Archive Reports/");

			if (!file_Archive_Reports.isDirectory())
				file_Archive_Reports.mkdirs();

			// String filePath = Environment.getExternalStorageDirectory()
			// + File.separator + AppValues.APP_NAME + "/Archive_Reports/";

			// String filePath = AppValues.getDirectory() + File.separator
			// + AppValues.APP_NAME + "/Archive Reports/";

			Log.v(TAG, "PATH" + file_Archive_Reports);
			List<File> files = getListFiles(file_Archive_Reports);
			num = files.size();
			Log.v(TAG, "Files SIZE ------>" + files.size());
			if (num > 0) {
				fileArrayList = new ArrayList<String>();

				for (i = 0; i < num; i++) {
					Log.v(TAG, "INSIDE GETTING FILE DATAAA");
					String szFilePath = files.get(i).getPath();
					Log.v(TAG, szFilePath);
					szFileName = szFilePath.substring(
							szFilePath.lastIndexOf("/") + 1,
							szFilePath.lastIndexOf('.'));

					listArray.add(new Data_Model_Archive_Reports(szFileName,
							reportDate));
					Log.v(TAG, "File NAme :------>" + szFileName);
				}
				custom_Adaptor = new Custom_Adapter_Archive_Reports(listArray);
				archive_Reports_Listview.setAdapter(custom_Adaptor);

			} else {
				archive_Reports_Listview.setAdapter(null);
				archive_Reports_Listview.getEmptyView().setVisibility(
						View.VISIBLE);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog m_AlertDialog = new AlertDialog.Builder(
				Archive_Reports.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						finish();

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		m_AlertDialog.setCanceledOnTouchOutside(false);
		m_AlertDialog.show();
		return;
	}
}
