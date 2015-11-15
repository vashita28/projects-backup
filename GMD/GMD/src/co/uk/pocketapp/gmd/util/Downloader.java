package co.uk.pocketapp.gmd.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import co.uk.pocketapp.gmd.tasks.Download_PDF_Task;
import co.uk.pocketapp.gmd.ui.Archive_Reports;

import android.util.Log;

public class Downloader {
	public boolean DownloadFile(String fileURL, File file) {
		try {
			Log.d("Downloader", "DownloadFile::" + fileURL);
			FileOutputStream f = new FileOutputStream(file);
			URL u = new URL(fileURL);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) > 0) {
				f.write(buffer, 0, len1);
			}
			f.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			file.delete();
			if (file.getName().contains("1"))
				Download_PDF_Task.bIsFile1NotFoundInServer = true;
			if (file.getName().contains("2"))
				Download_PDF_Task.bIsFile2NotFoundInServer = true;
			return false;
		}

	}
}
