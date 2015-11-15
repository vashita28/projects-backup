package com.android.cabapp.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class ReportIncident {

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

	public ReportIncident(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public String httpPostReportIncident(String argUrl, File file1, File file2, File file3, File file4, File file5,
			File videoFile, String incidentText) throws Exception {

		urlServer = Constants.driverURL + "reportincident/?accessToken=" + Util.getAccessToken(mContext);

		Log.d("ReportIncident-httpPostSendFile()", "URL:: " + urlServer);

		// ArrayList<NameValuePair> nameValuePairs = new
		// ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("data[text]",
		// incidentText));
		// Log.d("ReportIncident-httpPostSendFile()", "nameValuePairs:: "
		// + nameValuePairs);

		// new HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		// post header
		HttpPost httpPost = new HttpPost(urlServer);

		// String pathToOurFile = file1.getAbsolutePath();

		FileBody fileBody1 = null, fileBody2 = null, fileBody3 = null, fileBody4 = null, fileBody5 = null, videoFileBody = null;

		if (file1 != null) {
			fileBody1 = new FileBody(file1);
			Log.e("ReportIncident-httpPostSendFile()", "File 1:: " + file1.getName());
		}

		if (file2 != null) {
			fileBody2 = new FileBody(file2);
			Log.e("ReportIncident-httpPostSendFile()", "File 2:: " + file2.getName());
		}

		if (file3 != null) {
			fileBody3 = new FileBody(file3);
			Log.e("ReportIncident-httpPostSendFile()", "File 3:: " + file3.getName());
		}

		if (file4 != null) {
			fileBody4 = new FileBody(file4);
			Log.e("ReportIncident-httpPostSendFile()", "File 4:: " + file4.getName());
		}

		if (file5 != null) {
			fileBody5 = new FileBody(file5);
			Log.e("ReportIncident-httpPostSendFile()", "File 5:: " + file5.getName());
		}
		if (videoFile != null) {
			videoFileBody = new FileBody(videoFile);
			Log.e("ReportIncident-httpPostSendFile()", "VideoFile:: " + videoFile.getName());
		}

		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (fileBody1 != null)
			reqEntity.addPart("image1", fileBody1);
		if (fileBody2 != null)
			reqEntity.addPart("image2", fileBody2);
		if (fileBody3 != null)
			reqEntity.addPart("image3", fileBody3);
		if (fileBody4 != null)
			reqEntity.addPart("image4", fileBody4);
		if (fileBody5 != null)
			reqEntity.addPart("image5", fileBody5);
		if (videoFileBody != null)
			reqEntity.addPart("video", videoFileBody);

		reqEntity.addPart("data[text]", new StringBody(incidentText));
		httpPost.setEntity(reqEntity);

		// execute HTTP post request
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();

		String responseStr = "";
		if (resEntity != null) {

			responseStr = EntityUtils.toString(resEntity).trim();
			Log.v("ReportIncident", "Response: " + responseStr);

		}
		return responseStr;
	}
}
