package co.uk.android.lldc.async;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.SplashScreen;

public class DownloadFileAsync extends AsyncTask<Void, Void, Boolean> {

	private ProgressDialog mProgressDialog;
	private Context ctx;
	private String fname;
	private String paths, downloadUrls;

	// private boolean show = false;

	public DownloadFileAsync(Context ctx, String paths, String urls) {
		this.ctx = ctx;
		this.paths = paths;
		downloadUrls = urls;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(ctx);
		mProgressDialog.setMessage("Downloading files..");
		// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		// mProgressDialog.show();
	}

	@Override
	@SuppressWarnings("unused")
	protected Boolean doInBackground(Void... aurl) {

		int count;
		try {

			fname = this.paths + ".zip";

			LLDCApplication.onShowLogCat("fname", fname);

			URL url = new URL(this.downloadUrls);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			int lenghtOfFile = conexion.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream(), 2048);
			File file1 = new File(LLDCApplication.mBaseDir);
			file1.mkdir();

			File file = new File(LLDCApplication.mBaseDir + fname);
			if (!file.exists()) {
				file.createNewFile();
			}

			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[2048];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			LLDCApplication.onShowLogCat("file write ex", e.toString());
			return false;
		}

		return true;

	}

	@Override
	protected void onProgressUpdate(Void... progress) {
		// mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}

	@Override
	protected void onPostExecute(Boolean result) {

		try {
			if (SplashScreen.mHandler != null) {
				if (!result) {
					SplashScreen.mHandler.sendEmptyMessage(1007);
					LLDCApplication.onShowLogCat("DownloadFileAsync", "failed");
				} else {
					LLDCApplication.onShowLogCat("DownloadFileAsync",
							"successful");
					SplashScreen.mHandler.sendEmptyMessage(1006);
				}
			}
			if (mProgressDialog != null && mProgressDialog.isShowing())
				mProgressDialog.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
