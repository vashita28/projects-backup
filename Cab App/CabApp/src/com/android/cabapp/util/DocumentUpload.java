package com.android.cabapp.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class DocumentUpload {
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

	public DocumentUpload(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public String httpPostSendFile(String argUrl, File file) throws Exception {

		String pathToOurFile = file.getAbsolutePath();

		urlServer = Constants.driverURL + "document/?driverId="
				+ Util.getDriverID(mContext);

		Log.d("DocumentUpload-httpPostSendFile()", "URL:: " + urlServer);

		// ArrayList<NameValuePair> nameValuePairs = new
		// ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("data[driverLicence]", file
		// .getName()));

		// String serverResponseMessage = "";
		// String szResponse = "";
		// try {
		// FileInputStream fileInputStream = new FileInputStream(new File(
		// pathToOurFile));
		//
		// URL url = new URL(urlServer);
		// connection = (HttpURLConnection) url.openConnection();
		//
		// // Allow Inputs & Outputs
		// connection.setDoInput(true);
		// connection.setDoOutput(true);
		// connection.setUseCaches(false);
		//
		// // Enable POST method
		// connection.setRequestMethod("POST");
		//
		// connection.setRequestProperty("Connection", "Keep-Alive");
		// connection.setRequestProperty("Content-Type",
		// "multipart/form-data;boundary=" + boundary);
		//
		// outputStream = new DataOutputStream(connection.getOutputStream());
		//
		// BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
		// outputStream, "UTF-8"));
		// writer.write(getQuery(nameValuePairs));
		//
		// outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		// outputStream
		// .writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
		// + file.getName() + "\"" + lineEnd);
		// outputStream.writeBytes(lineEnd);
		//
		// bytesAvailable = fileInputStream.available();
		// bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// buffer = new byte[bufferSize];
		//
		// // Read file
		// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		//
		// while (bytesRead > 0) {
		// outputStream.write(buffer, 0, bufferSize);
		// bytesAvailable = fileInputStream.available();
		// bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		// }
		//
		// outputStream.writeBytes(lineEnd);
		// outputStream.writeBytes(twoHyphens + boundary + twoHyphens
		// + lineEnd);
		//
		// // Responses from the server (code and message)
		// int serverResponseCode = connection.getResponseCode();
		// serverResponseMessage = connection.getResponseMessage();
		//
		// String reply = new String();
		// InputStream inStream = connection.getInputStream();
		//
		// try {
		// int chr;
		// while ((chr = inStream.read()) != -1) {
		// reply = reply + (char) chr;
		// }
		// } finally {
		// inStream.close();
		// }
		//
		// Log.d("PhotoUploadTask-httpPostSendFile()", " PHOTO reply : "
		// + reply);
		// szResponse = reply;
		//
		// fileInputStream.close();
		// outputStream.flush();
		// outputStream.close();
		// } catch (Exception ex) {
		// // Exception handling
		// ex.printStackTrace();
		// }
		//
		// return szResponse;

		// new HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		// post header
		HttpPost httpPost = new HttpPost(urlServer);

		FileBody fileBody = new FileBody(file);

		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		reqEntity.addPart("driverLicence", fileBody);
		// reqEntity
		// .addPart("driverLicence", new StringBody(file.getName()));
		httpPost.setEntity(reqEntity);

		// execute HTTP post request
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();

		String responseStr = "";
		if (resEntity != null) {

			responseStr = EntityUtils.toString(resEntity).trim();
			Log.v("DocumentUpload", "Response: " + responseStr);

		}
		return responseStr;
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
