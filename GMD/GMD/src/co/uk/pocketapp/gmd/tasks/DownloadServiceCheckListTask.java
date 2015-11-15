package co.uk.pocketapp.gmd.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.pocketapp.gmd.handlerxml.ServiceCheckListReadValuesHandler;
import co.uk.pocketapp.gmd.ui.Sync_With_Server;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Values;

public class DownloadServiceCheckListTask extends
		AsyncTask<Void, Integer, String> {

	String TAG = "DownloadServiceCheckListTask";

	// http://dev.pocketapp.co.uk/gmd/api.php?access_token=83DF627CB93B8437&action=download_service_checklists&report_id=1

	String szDownloadURL = AppValues.SERVER_URL
			+ "action=download_service_checklists&access_token=%s&report_id=%s";

	final int readLimit = 16 * 1024;
	final int HTTP_TIMEOUT = 60 * 1000;
	// File file = new File(Environment.getExternalStorageDirectory()
	// + File.separator + AppValues.APP_NAME);
	File file = new File(AppValues.getDirectory() + File.separator
			+ AppValues.APP_NAME);

	String XMLName = "gmd_service_checklist.xml";

	String szResponseXML = "";
	public Context mContext;
	public ProgressBar progressBar;
	public TextView txtview_loadingmessage;
	public String szReporttID = "";
	// ReportXMLHandler reportXMLhandler;
	String m_szResponse = "";

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d("DownloadServiceCheckListTask", "DownloadServiceCheckListTask");
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
					AppValues.ACCESS_TOKEN, szReporttID));

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

				// InputStream mInputStream = new BufferedInputStream(
				// new FileInputStream(file), readLimit);
				// mInputStream.mark(readLimit);

				// reportXMLhandler = new ReportXMLHandler(mContext,
				// mInputStream,
				// "");
				// if (reportXMLhandler.getStatus().equals("0")) // not active
				m_szResponse = "100";

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

		// if (Sync_With_Server.mHandler != null)
		// Sync_With_Server.mHandler
		// .sendEmptyMessage(Sync_With_Server.DOWNLOAD_NOT_GOING_ON);

		if (!m_szResponse.equals("") && !m_szResponse.equals("0")) { // Active
																		// Status
																		// - 1

			try {
				// tasks values from service checklist xml
				StringBuilder text = null;
				if (AppValues.getServiceCheckListtXMLFile().exists()) {
					text = new StringBuilder();
					try {
						BufferedReader br = new BufferedReader(new FileReader(
								AppValues.getServiceCheckListtXMLFile()));
						String line;

						while ((line = br.readLine()) != null) {
							text.append(line);
							text.append('\n');
						}
						br.close();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

				if (text != null)
					new ServiceCheckListReadValuesHandler(
							mContext,
							new ByteArrayInputStream(text.toString().getBytes()));

				Util.EncryptServiceCheckListXML();
				Util.setIsReportXMLDownloaded(mContext, true);
				Util.setIsReportUploaded(mContext, false);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		// else if (m_szResponse.equals("0")) {
		//
		// Bundle bundle = new Bundle();
		// bundle.putString("error_code", reportXMLhandler.getErrorCode());
		// bundle.putString("error_message",
		// reportXMLhandler.getErrorMessage());
		// Message msg = Message.obtain();
		// msg.obj = bundle;
		//
		// if (Sync_With_Server.mHandler != null) {
		// Sync_With_Server.mHandler.sendMessage(msg);
		// }
		//
		// if (AppValues.getServiceCheckListtXMLFile().exists())
		// AppValues.getServiceCheckListtXMLFile().delete();
		//
		// }
		else {
			// Toast.makeText(mContext, "Error syncing report",
			// Toast.LENGTH_LONG)
			// .show();
			// if (Sync_With_Server.mHandler != null)
			// Sync_With_Server.mHandler
			// .sendEmptyMessage(Sync_With_Server.ERROR_SYNCING_REPORT);

			if (AppValues.getServiceCheckListtXMLFile().exists())
				AppValues.getServiceCheckListtXMLFile().delete();
		}
	}
}