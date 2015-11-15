package co.uk.pocketapp.gmd.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class UploadXML {
	int maxBufferSize = 1 * 1024 * 1024;

	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;

	// // XML Upload
	// http://dev.pocketapp.co.uk/dev/gmd/api.php?access_token=83DF627CB93B8437&action=upload_xml&report_id={report_id}

	// Report Status
	// http://dev.pocketapp.co.uk/dev/gmd/api.php?access_token=83DF627CB93B8437&action=report_status&report_id={report_id}

	// latest
	// http://dev.pocketapp.co.uk/dev/gmd/api.php?access_token=83DF627CB93B8437&action=upload_xml&report_id={report_id}&file_type={file_type}

	String urlServer;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	byte[] buffer;

	int bytesRead, bytesAvailable, bufferSize;
	public Context mContext;

	public UploadXML(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public String httpPostSendFile(String argUrl, File file, String szFileType)
			throws Exception {

		// String pathToOurFile =
		// AppValues.getReportXMLFile().getAbsolutePath();
		// URL url;
		// url = new URL(argUrl);
		// URLConnection connection = null;
		// connection = url.openConnection();
		// HttpURLConnection httppost = (HttpURLConnection) connection;
		// httppost.setDoInput(true);
		// httppost.setDoOutput(true);

		// file_type = "report_details" and "service_checklists"

		urlServer = AppValues.SERVER_URL
				+ "access_token=%s&action=upload_xml&report_id=%s&file_type=%s";

		String serverResponseMessage = "";
		String szResponse = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(file);

			urlServer = String.format(urlServer, AppValues.ACCESS_TOKEN,
					Util.getReportID(mContext), szFileType);
			URL url = new URL(urlServer);
			Log.d("UploadXMLTask-httpPostSendFile()", "XML UPLOAD URL : "
					+ urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"xml\";filename=\""
							+ file.getName().toString() + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			serverResponseMessage = connection.getResponseMessage();

			String reply = new String();
			InputStream inStream = connection.getInputStream();

			try {
				int chr;
				while ((chr = inStream.read()) != -1) {
					reply = reply + (char) chr;
				}
			} finally {
				inStream.close();
			}

			Log.d("UploadXMLTask-httpPostSendFile()", "XML reply : " + reply);
			szResponse = reply;

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception ex) {
			// Exception handling
			ex.printStackTrace();
		}

		return szResponse;

	}
}
