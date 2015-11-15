package co.uk.android.lldc.async;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.SplashScreen;

public class ExtractAsyntsk extends AsyncTask<Void, Long, Boolean> {

	private ProgressDialog mProgressDialog;
	private Context mCtx;
	private String fname;

	public ExtractAsyntsk(Context ctx, String fileName) {
		this.mCtx = ctx;
		this.fname = fileName;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		mProgressDialog = new ProgressDialog(mCtx);
		// Set progressdialog title
		mProgressDialog.setTitle("Please Wait");
		// Set progressdialog message
		mProgressDialog.setMessage("Extracting files...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		// Show progressdialog
//		mProgressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {
//			for (int i = 0; i < fname.size(); i++) {
				LLDCApplication.unzip(LLDCApplication.mBaseDir + fname + ".zip",
						LLDCApplication.mBaseDir + fname);
//			}

			return true;

		} catch (Exception e) {
			LLDCApplication.onShowLogCat("unzip_ex", e.toString());
		}

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		try {
			if (mProgressDialog != null && mProgressDialog.isShowing())
				mProgressDialog.dismiss();
			if (!result) {
				LLDCApplication.onShowLogCat("ExtractAsyntsk", "false: ");
				if (SplashScreen.mHandler != null) {
					SplashScreen.mHandler.sendEmptyMessage(1007);
				}
			} else {
				
				LLDCApplication.setMapTileFileName(mCtx, LLDCApplication.getMapTileNameFromCMS(mCtx));
				LLDCApplication.onShowLogCat("ExtractAsyntsk", "true: ");
				if (SplashScreen.mHandler != null) {
					SplashScreen.mHandler.sendEmptyMessage(1005);
				}
				try {
					File file = new File(LLDCApplication.mBaseDir + LLDCApplication.getMapTileNameFromCMS(mCtx) + ".zip");
					boolean deleted = file.delete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
