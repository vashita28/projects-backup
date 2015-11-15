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

public class PhotoUpload {

	int maxBufferSize = 1 * 1024 * 1024;

	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;

	File galleryFile;

	// String pathToOurFile = AppValues.getDirectory()
	// + "/Camera/1368770477898_GMD.png";

	// File dir = new File("sdcard", "Pictures");
	// String pathToOurFile = dir.getAbsolutePath() + "/H-01.jpg";

	String urlServer;
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";
	byte[] buffer;

	int bytesRead, bytesAvailable, bufferSize;
	public Context mContext;

	public PhotoUpload(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public String httpPostSendFile(String argUrl, File file) throws Exception {

		// galleryFile = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME);
		// galleryFile = new File(file, "Gallery");
		// String pathToOurFile = galleryFile.getAbsolutePath() + "/H-01.jpg";

		String pathToOurFile = file.getAbsolutePath();

		// URL url;
		// url = new URL(argUrl);
		// URLConnection connection = null;
		// connection = url.openConnection();
		// HttpURLConnection httppost = (HttpURLConnection) connection;
		// httppost.setDoInput(true);
		// httppost.setDoOutput(true);

		urlServer = AppValues.SERVER_URL
				+ "access_token=%s&action=upload_image&report_id=%s";

		String serverResponseMessage = "";
		String szResponse = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			urlServer = String.format(urlServer, AppValues.ACCESS_TOKEN,
					Util.getReportID(mContext));

			Log.d("PhotoUploadTask-httpPostSendFile()", "PHOTO URL : "
					+ urlServer);

			URL url = new URL(urlServer);
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
					.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
							+ file.getName() + "\"" + lineEnd);
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

			Log.d("PhotoUploadTask-httpPostSendFile()", " PHOTO reply : "
					+ reply);
			szResponse = reply;

			if (file.exists())
				file.delete();

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
