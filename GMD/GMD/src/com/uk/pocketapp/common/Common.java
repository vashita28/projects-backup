package com.uk.pocketapp.common;

import java.io.File;

import co.uk.pocketapp.gmd.R;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Common {

	/** show logcat */
	public static void showLogCat(String log) {

		Log.e("GMD", log);
	}

	/** show toast message */
	public static void showToastMsg(Context ctx, String msg) {

		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	/** sdcard path */
	public static String sdPath = "";

	public static String EXTRA_MESSAGE = "PATH";

	/** custom dialog For Open File */
	public static void dialogForOpenFile(final Context context,
			final String title, final File file) {

		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.custom_dialog);

		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);

		TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_title);
		txtTitle.setText(title);

		Button btnOpen = (Button) dialog.findViewById(R.id.btn_open);
		Button btnNo = (Button) dialog.findViewById(R.id.btn_no);

		if (title.toString().contains(".doc")
				|| title.toString().contains(".docx")) {
			// Word document
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".pdf")) {
			// PDF file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".ppt")
				|| title.toString().contains(".pptx")) {
			// Powerpoint file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".xls")
				|| title.toString().contains(".xlsx")) {
			// Excel file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".zip")
				|| title.toString().contains(".rar")) {
			// WAV audio file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".rtf")) {
			// RTF file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".wav")
				|| title.toString().contains(".mp3")) {
			// WAV audio file
			btnOpen.setText(context.getString(R.string.play1));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".gif")) {
			// GIF file

		} else if (title.toString().contains(".jpg")
				|| title.toString().contains(".jpeg")
				|| title.toString().contains(".png")) {
			// JPG file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".txt")) {
			// Text file
			btnOpen.setText(context.getString(R.string.open));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".3gp")
				|| title.toString().contains(".mpg")
				|| title.toString().contains(".mpeg")
				|| title.toString().contains(".mpe")
				|| title.toString().contains(".mp4")
				|| title.toString().contains(".avi")) {
			// Video files
			btnOpen.setText(context.getString(R.string.play1));
			btnNo.setText(context.getString(R.string.no));
		} else if (title.toString().contains(".flv")) {
			btnOpen.setText(context.getString(R.string.play1));
			btnNo.setText(context.getString(R.string.no));
		}

		btnOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

				try {
					// Common.showLogCat("Open clicked: "+title);
					OpenFile.openFile(context, file);
				} catch (Exception ex) {
					Common.showLogCat(ex.toString());
				}

			}
		});

		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();
	}

}
