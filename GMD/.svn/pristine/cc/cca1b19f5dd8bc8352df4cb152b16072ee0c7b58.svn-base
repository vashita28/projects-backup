package co.uk.pocketapp.gmd.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import co.uk.pocketapp.gmd.receiver.NetworkBroadcastReceiver;
import co.uk.pocketapp.gmd.ui.MainActivity;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class LoginTask extends AsyncTask<Void, Void, String> {

	private final String TAG = "SYNC WITH SERVER";

	final String szLoginURL = AppValues.SERVER_URL
			+ "access_token=%s&action=login&username=%s&password=%s";

	public String szUserName = "";
	public String szPassword = "";
	public ProgressBar progressBar;
	public Context mContext;

	final int HTTP_TIMEOUT = 60 * 1000;

	boolean bIsNotOfflineLogin = true;

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String szResponse = "";

		// offline check first
		StringBuilder text = null;
		String szDecryptedText = "";
		try {

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
					br.close();

					if (AppValues.bIsEncryptionRequired) {
						szDecryptedText = AES.aesDecrypt(text.toString(),
								AES.SHA256(""));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			HashMap<String, String> xmlData = new HashMap<String, String>();
			ResponseParser parser = new ResponseParser(mContext);

			if (AppValues.bIsEncryptionRequired)
				xmlData = parser.parse_User_Details(szDecryptedText, xmlData);
			else
				xmlData = parser.parse_User_Details(text.toString(), xmlData);

			Log.d("***************", "****************" + xmlData.toString());

			if (szUserName.equals(xmlData.get(XML_Values.USERNAME))
					&& szPassword.equals(xmlData.get(XML_Values.PASSWORD))) {
				bIsNotOfflineLogin = false;
				szResponse = "{\"head\":{\"status\":\"ACTIVE\"},\"body\":{\"user\":{\"engineer_id\":\"27\",\"engineer_name\":\"tom\",\"engineer_username\":\"tomm\",\"engineer_password\":\"tommmm\",\"engineer_password_md5\":\"5872e6f5506ca52fd48e3d242217b7cc\",\"engineer_address\":\"\",\"engineer_telephone\":\"\",\"engineer_mobile\":\"\",\"engineer_email\":\"\",\"engineer_notes\":\"\",\"created_on\":\"2013-06-18 09:23:16\",\"login_status\":\"Active\",\"last_logged_in\":\"2013-06-25 12:40:31\",\"delete_status\":\"0\",\"deleted_on\":\"0000-00-00 00:00:00\",\"is_assigned\":\"1\"}}}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bIsNotOfflineLogin) {

			// if (Util.isAirplaneModeOn(mContext))
			// NetworkBroadcastReceiver.ms_bIsNetworkAvailable = false;

			// Set Timeout for Connection & Socket
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT);

			// Init Http Client
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpGet httpGet = new HttpGet(String.format(szLoginURL,
					AppValues.ACCESS_TOKEN, URLEncoder.encode(szUserName),
					szPassword));

			// Log.d("LoginTask",
			// "URL is :: "
			// + String.format(szLoginURL, AppValues.ACCESS_TOKEN,
			// szUserName, szPassword));

			try {
				// szResponse = new
				// HttpConnect().connect(String.format(szLoginURL,
				// AppValues.ACCESS_TOKEN, szUserName, szPassword));

				HttpResponse response = httpClient.execute(httpGet);
				szResponse = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8).trim();
				Log.v(TAG, "Downloaded task from server done");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return szResponse;
	}

	@Override
	protected void onPostExecute(String szResult) {
		super.onPostExecute(szResult);
		// if (progressBar != null) {
		// progressBar.setVisibility(View.GONE);
		// }
		if (!szResult.equals("")) {
			parseResponse(szResult);
			Log.d("LoginTask-()", "onPostExecute SUCCESS");
			// DOWNLOAD PDF FROM SERVER:
			// Log.v(TAG, "Downloading pdf file");
			// Download_PDF_Task downloadPDFTask = new Download_PDF_Task();
			// downloadPDFTask.execute();

		} else {
			if (!NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				AlertDialog m_AlertDialog = new AlertDialog.Builder(mContext)
						.setTitle("Alert!")
						.setMessage("Network not available")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			} else {
				Toast.makeText(mContext,
						"Unable to process your request. Please try again",
						Toast.LENGTH_LONG).show();
			}
			Log.d("LoginTask-()", "onPostExecute ERROR");
			Util.setLoggedIn(mContext, false);
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
		}
	}

	private void parseResponse(String szResult) {
		try {

			JSONObject jsonObj = new JSONObject(szResult);

			JSONObject jsonHeadObj = jsonObj.getJSONObject("head");
			String szStatus = jsonHeadObj.getString("status");

			HashMap<String, String> mapUserDetails = new HashMap<String, String>();

			if (szStatus != null && szStatus.equalsIgnoreCase("ACTIVE")) {

				if (bIsNotOfflineLogin) {
					JSONObject userObj = jsonObj.getJSONObject("body")
							.getJSONObject("user");
					String szEngineerID = userObj.getString("engineer_id");
					String szLoginStatus = userObj.getString("login_status");
					String szLastLoggedIn = userObj.getString("last_logged_in");
					Log.d("LoginTask : ", "EngineerID :: " + szEngineerID);
					Log.d("LoginTask : ", "LoginStatus :: " + szLoginStatus);
					Log.d("LoginTask : ", "Last Logged In  :: "
							+ szLastLoggedIn);

					Util.setEngineerID(mContext, szEngineerID);
					mapUserDetails.put(XML_Values.ENGINEER_ID, szEngineerID);
				}

				Util.setLoggedIn(mContext, true);

				mapUserDetails.put(XML_Values.USERNAME, szUserName);
				mapUserDetails.put(XML_Values.PASSWORD, szPassword);
				new XML_Creation().save_UserDetails(mapUserDetails);

				// Download_XML_Task downloadXMLTask = new Download_XML_Task();
				// downloadXMLTask.mContext = mContext;
				// downloadXMLTask.progressBar = progressBar;
				// downloadXMLTask.execute();

				Intent mainActivityIntent = new Intent(mContext,
						MainActivity.class);
				mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				((Activity) mContext).finish();
				mContext.startActivity(mainActivityIntent);

			} else {
				if (szStatus != null && szStatus.equalsIgnoreCase("INVALID")) {
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							mContext)
							.setTitle("Invalid credentials!")
							.setMessage(
									"Username and password do not match. If you have forgotten your credentials please contact the admin on 123-123-1234")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {

										}
									}).create();
					m_AlertDialog.setCanceledOnTouchOutside(false);
					m_AlertDialog.show();
				}

				Util.setLoggedIn(mContext, false);

				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
