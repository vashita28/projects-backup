package com.uk.pocketapp.common;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import co.uk.pocketapp.gmd.ui.VideoPlayer;

public class OpenFile {

	/** Open Any type of file with Intent */
	public static void openFile(Context context, File url) throws IOException {

		// Create URI
		File file = url;
		Uri uri = Uri.fromFile(file);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Check what kind of file you are trying to open, by comparing the url
		// with extensions.
		if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
			// Word document
			intent.setDataAndType(uri, "application/msword");
			context.startActivity(intent);
		} else if (url.toString().contains(".pdf")) {
			// PDF file
			intent.setDataAndType(uri, "application/pdf");
			context.startActivity(intent);
		} else if (url.toString().contains(".ppt")
				|| url.toString().contains(".pptx")) {
			// Powerpoint file
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
			context.startActivity(intent);
		} else if (url.toString().contains(".xls")
				|| url.toString().contains(".xlsx")) {
			// Excel file
			intent.setDataAndType(uri, "application/vnd.ms-excel");
			context.startActivity(intent);
		} else if (url.toString().contains(".zip")
				|| url.toString().contains(".rar")) {
			// WAV audio file
			intent.setDataAndType(uri, "application/x-wav");
			context.startActivity(intent);
		} else if (url.toString().contains(".rtf")) {
			// RTF file
			intent.setDataAndType(uri, "application/rtf");
			context.startActivity(intent);
		} else if (url.toString().contains(".wav")
				|| url.toString().contains(".mp3")) {
			// WAV audio file
			intent.setDataAndType(uri, "audio/x-wav");
			context.startActivity(intent);
		} else if (url.toString().contains(".gif")) {
			// GIF file
			intent.setDataAndType(uri, "image/gif");
			context.startActivity(intent);
		} else if (url.toString().contains(".jpg")
				|| url.toString().contains(".jpeg")
				|| url.toString().contains(".png")) {
			// JPG file
			intent.setDataAndType(uri, "image/jpeg");
			context.startActivity(intent);
		} else if (url.toString().contains(".txt")) {
			// Text file
			intent.setDataAndType(uri, "text/plain");
			context.startActivity(intent);
		} else if (url.toString().contains(".3gp")
				|| url.toString().contains(".mpg")
				|| url.toString().contains(".mpeg")
				|| url.toString().contains(".mpe")
				|| url.toString().contains(".mp4")
				|| url.toString().contains(".avi")) {
			/*
			 * // Video files //Play video using intent
			 * intent.setDataAndType(uri, "video/*");
			 * context.startActivity(intent);
			 */

			// Play video through custom VideoView
			Intent myIntent1 = new Intent(context, VideoPlayer.class);
			myIntent1.putExtra("video_path", file.getAbsolutePath());
			myIntent1.putExtra("text_path",
					file.getAbsolutePath().replace("mp4", "txt"));
			context.startActivity(myIntent1);

		} else if (url.toString().contains(".flv")) {
			intent.setDataAndType(uri, "video/flv");
			context.startActivity(intent);
		} else {
			// if you want you can also define the intent type for any other
			// file
			intent.setDataAndType(uri, "*/*");
			context.startActivity(intent);
		}

	}

}
