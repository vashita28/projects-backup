package co.uk.pocketapp.gmd.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.Util;

public class Photo_Page extends Activity {
	Button btn_back;
	Button btn_delete;

	String szFileName;
	String szFilePath;
	ImageView imageview_preview;

	View view_photo_page_top_bar;
	TextView textview_title;

	TextView txtPosition;
	TextView txtLocation;
	TextView txtExtra;
	TextView txtDateTime;
	TextView textview_dateAndTime, textview_GMD_Heading;

	TextView site_Name;
	TextView mill_Name;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_page);

		// ImageLoader Options
		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(800)).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888).build();

		view_photo_page_top_bar = (View) findViewById(R.id.view_photo_page_top_bar);
		textview_title = (TextView) view_photo_page_top_bar
				.findViewById(R.id.pageTitle);
		textview_title.setText("Log a Problem");

		// font implementation of main heading "GMD": textview
		textview_GMD_Heading = (TextView) findViewById(R.id.Heading);
		textview_GMD_Heading.setTypeface(GMDApplication.fontHeading);

		textview_dateAndTime = (TextView) view_photo_page_top_bar
				.findViewById(R.id.DateNtime);
		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			String reportDate = dateFormat.format(today);
			textview_dateAndTime.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		txtPosition = (TextView) findViewById(R.id.textview_position);
		txtLocation = (TextView) findViewById(R.id.textview_location);
		txtExtra = (TextView) findViewById(R.id.textview_extra);
		txtDateTime = (TextView) findViewById(R.id.textview_dateTime);

		txtPosition.setTypeface(GMDApplication.fontText);
		txtLocation.setTypeface(GMDApplication.fontText);
		txtExtra.setTypeface(GMDApplication.fontText);
		txtDateTime.setTypeface(GMDApplication.fontText);

		site_Name = (TextView) findViewById(R.id.siteName);
		site_Name.setTypeface(GMDApplication.fontText);
		mill_Name = (TextView) findViewById(R.id.millName);
		mill_Name.setTypeface(GMDApplication.fontText);
		// Getting data from share preferences and show on site name and mill
		// name textview:
		site_Name.setText(Util.getSiteName(Photo_Page.this));
		mill_Name.setText(Util.getMillName(Photo_Page.this));

		if (getIntent().getExtras() != null) {
			szFileName = getIntent().getStringExtra("filename");
			szFilePath = getIntent().getStringExtra("capturedimageuri");
		}

		String szPhotoEntryDetails = Util.getPhotoEntryDetails(Photo_Page.this,
				szFileName).trim();
		if (!szPhotoEntryDetails.equals("")) {
			String[] photoDetails = szPhotoEntryDetails.split(",");
			String szLocation = "";
			String szPosition = "";
			String szComments = "";
			String szMotorPart = "";
			String szDateTime = "";
			try {
				szLocation = photoDetails[0];
				szPosition = photoDetails[1];
				szComments = photoDetails[2];
				szMotorPart = photoDetails[3];
				szDateTime = photoDetails[4];
			} catch (Exception e) {
				e.printStackTrace();
			}

			txtLocation.setText("Location: " + szLocation);
			txtPosition.setText("Position: " + szPosition);
			txtExtra.setText(szComments);
			txtDateTime.setText(szDateTime);
		}

		imageview_preview = (ImageView) findViewById(R.id.imageview_preview);
		// imageview_preview.setImageURI(Uri.parse(szFilePath));
		imageLoader.displayImage("file://" + szFilePath, imageview_preview,
				options);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_delete = (Button) findViewById(R.id.btn_delete);

		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Photo_Page.this)
						.setTitle("Delete")
						.setMessage("Confirm delete?")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {
										Util.setPhotoEntryDetails(
												Photo_Page.this, szFileName,
												"", "", "", "", "");
										finish();

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		imageview_preview.setImageBitmap(null);
		imageview_preview = null;
		System.gc();
		System.gc();
	}

}
