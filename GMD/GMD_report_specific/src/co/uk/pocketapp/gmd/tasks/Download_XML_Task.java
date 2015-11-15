package co.uk.pocketapp.gmd.tasks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.gmd.handlerxml.ReportXMLHandler;
import co.uk.pocketapp.gmd.ui.MainActivity;
import co.uk.pocketapp.gmd.ui.Report_Details;
import co.uk.pocketapp.gmd.ui.Service_CheckList_Without_Swipe;
import co.uk.pocketapp.gmd.ui.Sync_With_Server;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;

public class Download_XML_Task extends AsyncTask<Void, Integer, String> {

	String TAG = "Download_XML_Task";

	String szDownloadURL = AppValues.SERVER_URL
			+ "action=download_xml&access_token=%s&engineer_id=%s";
	final int readLimit = 16 * 1024;
	final int HTTP_TIMEOUT = 60 * 1000;
	// File file = new File(Environment.getExternalStorageDirectory()
	// + File.separator + AppValues.APP_NAME);
	File file = new File(AppValues.getDirectory() + File.separator
			+ AppValues.APP_NAME);

	String XMLName = "gmd_report.xml";

	String szResponseXML = "";
	public Context mContext;
	public ProgressBar progressBar;
	public TextView txtview_loadingmessage;
	ReportXMLHandler reportXMLhandler;
	String m_szResponse = "";

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		// Log.d("Download_XML_Task", "Download XML Report");
		try {

			// "http://dev.pocketapp.co.uk/dev/GMD/gmd_trial.xml"
			// m_szResponse = new HttpConnect().connect(String.format(
			// szDownloadURL, AppValues.ACCESS_TOKEN,
			// Util.getEngineerID(mContext)));

			// Set Timeout for Connection & Socket
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT);

			// Init Http Client
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpGet httpGet = new HttpGet(String.format(szDownloadURL,
					AppValues.ACCESS_TOKEN, Util.getEngineerID(mContext)));

			// Log.d("Download_XML_Task",
			// "Download XML Report URL :: "
			// + String.format(szDownloadURL,
			// AppValues.ACCESS_TOKEN,
			// Util.getEngineerID(mContext)));

			// Add User token to the ValuePair
			HttpResponse response = httpClient.execute(httpGet);

			m_szResponse = EntityUtils.toString(response.getEntity(),
					HTTP.UTF_8).trim();
			Log.d(TAG, "XML PAGE" + m_szResponse);

			// Save xml data into file
			if (!file.isDirectory()) {
				file.mkdir();
			}
			try {
				file = new File(file, XMLName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(m_szResponse.getBytes());
				fos.close();

				InputStream mInputStream = new BufferedInputStream(
						new FileInputStream(file), readLimit);
				mInputStream.mark(readLimit);
				reportXMLhandler = new ReportXMLHandler(mContext, mInputStream,
						"");
				if (reportXMLhandler.getStatus().equals("0")) // not active
					m_szResponse = "0";

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m_szResponse = "";
		}
		return m_szResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
			txtview_loadingmessage.setVisibility(View.GONE);
		}

		if (Sync_With_Server.mHandler != null)
			Sync_With_Server.mHandler
					.sendEmptyMessage(Sync_With_Server.DOWNLOAD_NOT_GOING_ON);

		if (!m_szResponse.equals("") && !m_szResponse.equals("0")) { // Active
																		// Status
																		// - 1

			try {
				ResponseParser parser = new ResponseParser(AppValues.AppContext);
				parser.parse_Header_Details(result);

				if (MainActivity.mHandler != null)
					MainActivity.mHandler
							.sendEmptyMessage(Sync_With_Server.SUCCESS);
				if (Report_Details.mHandler != null)
					Report_Details.mHandler
							.sendEmptyMessage(Sync_With_Server.SUCCESS);
				if (Sync_With_Server.mHandler != null)
					Sync_With_Server.mHandler
							.sendEmptyMessage(Sync_With_Server.SUCCESS);
				if (Service_CheckList_Without_Swipe.mHandler != null)
					Service_CheckList_Without_Swipe.mHandler.sendEmptyMessage(0);

				Util.EncryptReportXML();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} else if (m_szResponse.equals("0")) {

			Bundle bundle = new Bundle();
			bundle.putString("error_code", reportXMLhandler.getErrorCode());
			bundle.putString("error_message",
					reportXMLhandler.getErrorMessage());
			Message msg = Message.obtain();
			msg.obj = bundle;

			if (Sync_With_Server.mHandler != null) {
				Sync_With_Server.mHandler.sendMessage(msg);
			}

			if (AppValues.getReportXMLFile().exists())
				AppValues.getReportXMLFile().delete();

		} else {
			Toast.makeText(mContext, "Error syncing report", Toast.LENGTH_LONG)
					.show();
			if (Sync_With_Server.mHandler != null)
				Sync_With_Server.mHandler
						.sendEmptyMessage(Sync_With_Server.ERROR_SYNCING_REPORT);

			if (AppValues.getReportXMLFile().exists())
				AppValues.getReportXMLFile().delete();
		}
	}
}
