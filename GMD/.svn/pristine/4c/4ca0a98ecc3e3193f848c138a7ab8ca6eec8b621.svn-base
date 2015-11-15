package co.uk.pocketapp.gmd.tasks;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Downloader;

public class Download_PDF_Task extends AsyncTask<Void, Integer, String> {
	private static final String TAG = "DOWNLOAD PDF TASK";
	// PDF LINK from website
	// String szDownloadURL =
	// "http://www.nmu.ac.in/ejournals/aspx/courselist.pdfs";

	// // Download PDF
	// http://dev.pocketapp.co.uk/dev/gmd/api.php?access_token=83DF627CB93B8437&action=download_pdf&report_id={report_id}

	// PDF LINK given by sangam
	String szDownloadURL = "http://dev.pocketapp.co.uk/dev/gmd/report1.pdf";
	// File file = new File(Environment.getExternalStorageDirectory() +
	// "/GMD/Archive_Reports");
	// String szResponseXML = "";
	public Context mContext;
	public ProgressBar progressBar;
	File file_Archive_Reports_Folder;
	String szFileName = null;
	int num;
	int i = 0;
	File file;

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		// String extStorageDirectory =
		// Environment.getExternalStorageDirectory()
		// .toString();
		// File folder = new File(extStorageDirectory, "pdf");
		// folder.mkdir();
		// File file = new File(folder, "Read.pdf");
		// try {
		// file.createNewFile();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// Downloader.DownloadFile("http://www.nmu.ac.in/ejournals/aspx/courselist.pdf",
		// file);

		// String extStorageDirectory =
		// Environment.getExternalStorageDirectory()
		// + File.separator + AppValues.APP_NAME;
		String extStorageDirectory = AppValues.getDirectory() + File.separator
				+ AppValues.APP_NAME;
		file_Archive_Reports_Folder = new File(extStorageDirectory
				+ File.separator + "Archive_Reports");

		if (!file_Archive_Reports_Folder.isDirectory()) {
			file_Archive_Reports_Folder.mkdirs();
		}

		// List<File> files = getListFiles(file_Archive_Reports_Folder);
		// num = files.size();
		// Log.v(TAG, "Files SIZE ------>"+files.size());
		// for (i = 0; i < files.size(); i++){

		// String szFilePath = files.get(i).getPath();
		// szFileName = szFilePath.substring(szFilePath.lastIndexOf("/") + 1,
		// szFilePath.lastIndexOf('.'));
		// Log.v(TAG, szFileName);

		// szFileName=szDownloadURL.substring(szDownloadURL.lastIndexOf("/"+1),
		// szDownloadURL.lastIndexOf('.'));

		file = new File(file_Archive_Reports_Folder, "report1.pdf");

		// try {
		// if (!file.exists())
		// file.createNewFile();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		if (!file.exists())
			new Downloader().DownloadFile(szDownloadURL, file);
		Log.v(TAG, "Downloading file NAME " + file.getPath().toString());
		// }

		// Encryption function:
		// Log.v(TAG, "GOINT TO  ENCYPTION");
		// EncodeDecodeAES encrypted_File = new EncodeDecodeAES();
		// encrypted_File.encrypt(file);
		// Log.v(TAG, "Coming BACK FROM ENCYPTION");

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

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
