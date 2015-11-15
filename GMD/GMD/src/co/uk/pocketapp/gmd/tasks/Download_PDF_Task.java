package co.uk.pocketapp.gmd.tasks;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.receiver.NetworkBroadcastReceiver;
import co.uk.pocketapp.gmd.ui.Archive_Reports;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.CustomMill;
import co.uk.pocketapp.gmd.util.Downloader;
import co.uk.pocketapp.gmd.util.Util;

public class Download_PDF_Task extends AsyncTask<Void, Integer, String> {

	ProgressBar progressArchiveReports;
	Archive_Reports archive_reports;

	public Download_PDF_Task(ProgressBar progressArchiveReports,
			Archive_Reports archive_reports) {
		// TODO Auto-generated constructor stub
		this.progressArchiveReports = progressArchiveReports;
		this.archive_reports = archive_reports;
	}

	private static final String TAG = "DOWNLOAD PDF TASK";
	// PDF LINK from website
	// String szDownloadURL =
	// "http://www.nmu.ac.in/ejournals/aspx/courselist.pdfs";

	// // Download PDF
	// http://dev.pocketapp.co.uk/dev/gmd/api.php?access_token=83DF627CB93B8437&action=download_pdf&report_id={report_id}

	// PDF LINK given by sangam
	// String szDownloadURL = "http://dev.pocketapp.co.uk/dev/gmd/report1.pdf";
	// File file = new File(Environment.getExternalStorageDirectory() +
	// "/GMD/Archive_Reports");
	// String szResponseXML = "";

	// String szDownloadURL = AppValues.SERVER_URL +
	// "&action=archive_report&access_token=%s&report_id=%s";

	String szDownloadURL = "http://dev.pocketapp.co.uk/dev/gmd/projects.php?"
			+ "username=admin&password=password&mill_id=%s&report_number=%s&action=download_report";

	public ProgressBar progressBar;
	File file_Archive_Reports_Folder;
	String szFileName = null;
	int num;
	int i = 0;
	File file;
	JSONObject jaobj;
	JSONArray jArray = null;
	String szArchiveReport = null;
	String szStatus, szErrorMessage;

	public static boolean bIsFile1NotFoundInServer = false;
	public static boolean bIsFile2NotFoundInServer = false;

	// HashMap<String, String> hmPDF = new HashMap<String, String>();

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		// Http Post
		// String response = null;
		try {
			// HttpParams httpParams = new BasicHttpParams();
			//
			// // Init Http Client
			// HttpClient httpClient = new DefaultHttpClient(httpParams);
			// HttpGet httpGet = new HttpGet(String.format(szDownloadURL,
			// AppValues.ACCESS_TOKEN, Util.getReportID(archive_reports)));
			//
			// // Add User token to the ValuePair
			// HttpResponse responseHttp = httpClient.execute(httpGet);
			//
			// response = EntityUtils.toString(responseHttp.getEntity(),
			// HTTP.UTF_8).trim();
			// // Log.d(TAG, "XML PAGE" + response);
			//
			// if (response != null) {
			//
			// jaobj = new JSONObject(response);
			//
			// if (jaobj != null && jaobj.has("head")) {
			// szStatus = jaobj.getJSONObject("head").getString("status")
			// .toString();
			//
			// if (szStatus.contains("0")) {
			// szErrorMessage = jaobj.getString("status").toString();
			//
			// } else {
			// jaobj = jaobj.getJSONObject("body");
			//
			// // key:"archive_reports"
			// jArray = jaobj.optJSONArray("archive_report");

			ArrayList<CustomMill> customMillList = new ArrayList<CustomMill>();

			// for (int i = 0; i < jArray.length(); i++) {

			// JSONObject jsonChildNode = jArray.getJSONObject(i);
			// String szReport =
			// jsonChildNode.optString("report")
			// .toString();
			// String szMillId = jsonChildNode
			// .optString("mill_id").toString();

			// hmPDF.put(i + "mill_id_" + szMillId, szReport);

			Cursor millCursor = archive_reports.getContentResolver().query(
					DataProvider.Mill.CONTENT_URI,
					null,
					DataProvider.Mill.REPORT_ID + " ='"
							+ Util.getReportID(archive_reports) + "'", null,
					null);
			if (millCursor != null && millCursor.moveToFirst()) {
				do {
					CustomMill objCumstomMill = new CustomMill();
					// objCumstomMill.setMill_id(Integer.parseInt(jsonChildNode
					// .optString("mill_id")));
					// objCumstomMill.setSzPDFLink(jsonChildNode.optString("report")
					// .toString());
					objCumstomMill
							.setMill_id(Integer.parseInt(millCursor.getString(millCursor
									.getColumnIndex(DataProvider.Mill.MILL_ID))));
					objCumstomMill.setPDFLink(String.format(szDownloadURL,
							objCumstomMill.getMill_id(), 0));
					objCumstomMill.setFileName(millCursor.getString(millCursor
							.getColumnIndex(DataProvider.Mill.MILL_NAME))
							+ "Report_1.pdf");
					customMillList.add(objCumstomMill);

					objCumstomMill = new CustomMill();
					objCumstomMill
							.setMill_id(Integer.parseInt(millCursor.getString(millCursor
									.getColumnIndex(DataProvider.Mill.MILL_ID))));
					objCumstomMill.setPDFLink(String.format(szDownloadURL,
							objCumstomMill.getMill_id(), 1));
					objCumstomMill.setFileName(millCursor.getString(millCursor
							.getColumnIndex(DataProvider.Mill.MILL_NAME))
							+ "Report_2.pdf");
					customMillList.add(objCumstomMill);

				} while (millCursor.moveToNext());
			}
			millCursor.close();

			// }
			// Creating archive folder
			String extStorageDirectory = AppValues.getDirectory()
					+ File.separator + AppValues.APP_NAME;
			file_Archive_Reports_Folder = new File(extStorageDirectory
					+ File.separator + "Archive Reports");

			if (!file_Archive_Reports_Folder.isDirectory()) {
				file_Archive_Reports_Folder.mkdirs();
			}

			for (int j = 0; j < customMillList.size(); j++) {

				if (bIsFile1NotFoundInServer)
					bIsFile1NotFoundInServer = false;

				if (bIsFile2NotFoundInServer)
					bIsFile2NotFoundInServer = false;

				String szFilePath = customMillList.get(j).getPDFLink()
						.toString();

				// String szPDFName = szFilePath.substring(szFilePath
				// .lastIndexOf('/') + 1);
				String szPDFName = customMillList.get(j).getFileName()
						.toString();

				file = new File(file_Archive_Reports_Folder, szPDFName);

				if (!file.exists()) {
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						boolean bStatus = false;
						do {
							bStatus = new Downloader().DownloadFile(
									customMillList.get(j).getPDFLink(), file);
							if (bIsFile1NotFoundInServer)
								break;
							if (bIsFile2NotFoundInServer)
								break;
						} while (bStatus == false);
					} else {
						return "failed due to no network";
					}
				}
			}
			return "success";
			// }
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "failed";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (progressArchiveReports != null)
			progressArchiveReports.setVisibility(View.GONE);

		if (result != null && result.equals("success")) {
			Util.setPDFListDownloaded(archive_reports, true);
		}

		if (result != null && result.equals("failed due to no network")) {

			Toast.makeText(
					archive_reports,
					"PDF download could not be completed due to error in network connection. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		archive_reports.setPDFList();
	}
	// private List<File> getListFiles(File parentDir) {
	// ArrayList<File> inFiles = new ArrayList<File>();
	// File[] files = parentDir.listFiles();
	// if (files != null){
	// for (File file : files) {
	// if (file.isDirectory()) {
	// inFiles.addAll(getListFiles(file));
	// } else {
	// // if (file.getName().endsWith(".csv")) {
	// // inFiles.add(file);
	// // }
	// inFiles.add(file);
	// }
	// }
	// }
	//
	//
	// return inFiles;
	// }

}
