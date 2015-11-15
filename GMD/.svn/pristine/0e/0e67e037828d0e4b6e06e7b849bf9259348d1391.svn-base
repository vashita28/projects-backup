package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Photo_Details extends Activity implements OnClickListener {

	View view_photo_details_entry_top_bar;
	TextView textview_title;

	EditText et_Comments, et_location_others;
	Spinner spinner_motorPart;
	Spinner spinner_location;
	Spinner spinner_position;
	private ArrayAdapter<CharSequence> location_Spinner_Adapter;
	private ArrayAdapter<CharSequence> motorPart_Spinner_Adapter;
	private ArrayAdapter<CharSequence> position_Spinner_Adapter;

	Button btn_saveChanges;
	ProgressBar progressPhotoDetailsEntry;

	String m_szFileName = "", m_CapturedImagePath = "", m_szDateTime = "";

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mmaa");
	TextView textview_dateAndTime, textview_GMD_Heading;

	String m_szThirdGenItemID = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_details_entry);

		progressPhotoDetailsEntry = (ProgressBar) findViewById(R.id.progress_photodetailsentry);

		view_photo_details_entry_top_bar = (View) findViewById(R.id.view_photo_details_entry_top_bar);
		textview_title = (TextView) view_photo_details_entry_top_bar
				.findViewById(R.id.pageTitle);

		textview_title.setText("Photo Details Entry");

		// font implementation of main heading "GMD": textview
		textview_GMD_Heading = (TextView) findViewById(R.id.Heading);
		textview_GMD_Heading.setTypeface(GMDApplication.fontHeading);

		textview_dateAndTime = (TextView) view_photo_details_entry_top_bar
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

		et_Comments = (EditText) findViewById(R.id.et_comments);
		spinner_location = (Spinner) findViewById(R.id.spinner_location);
		spinner_motorPart = (Spinner) findViewById(R.id.spinner_motorpart);
		spinner_position = (Spinner) findViewById(R.id.spinner_position);
		et_Comments.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		et_Comments.setImeOptions(EditorInfo.IME_ACTION_DONE);

		// Setting LOCATION spinner Data:
		et_location_others = (EditText) findViewById(R.id.et_location_others);
		et_location_others.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_location_others.setVisibility(View.GONE);
		location_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.location_array, R.layout.custom_spinner_item);
		location_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_location.setAdapter(location_Spinner_Adapter);
		spinner_location
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String szLocation = spinner_location.getSelectedItem()
								.toString();
						if (szLocation.equals("Other")) {
							et_location_others.setVisibility(View.VISIBLE);
						} else {
							et_location_others.setVisibility(View.GONE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Setting POSITION spinner Data:
		position_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.position_array, R.layout.custom_spinner_item);
		position_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_position.setAdapter(position_Spinner_Adapter);
		spinner_position
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Setting MOTOR PART spinner Data:
		motorPart_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.motorpart_array, R.layout.custom_spinner_item);
		motorPart_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_motorPart.setAdapter(motorPart_Spinner_Adapter);
		spinner_motorPart
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		btn_saveChanges = (Button) findViewById(R.id.btn_savechanges);
		btn_saveChanges.setOnClickListener(this);

		if (getIntent().getExtras() != null) {
			m_szFileName = getIntent().getExtras().getString("filename");
			m_CapturedImagePath = getIntent().getExtras().getString(
					"capturedimageuri");
			m_szDateTime = getIntent().getExtras().getString("datetime");
			m_szThirdGenItemID = getIntent().getExtras().getString(
					"thirdgenitemid");
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_savechanges:
			progressPhotoDetailsEntry.setVisibility(View.VISIBLE);

			String szComments = et_Comments.getText().toString();
			String szLocation = spinner_location.getSelectedItem().toString();
			if (szLocation.equals("Other")) {
				szLocation = et_location_others.getText().toString();
			}
			String szMotorPart = spinner_motorPart.getSelectedItem().toString();
			String szPosition = spinner_position.getSelectedItem().toString();

			if (szComments.length() > 0
					&& spinner_location.getSelectedItemPosition() > 0
					&& spinner_motorPart.getSelectedItemPosition() > 0
					&& spinner_position.getSelectedItemPosition() > 0
					&& szLocation.length() > 0) {
				Util.setPhotoEntryDetails(Photo_Details.this, m_szFileName,
						szLocation, szPosition, szComments, szMotorPart,
						m_szDateTime);

				int nNumber = Integer.parseInt(Util
						.getPhotoFileNumber(Photo_Details.this)) + 1;
				String szFileNumber = String.valueOf(nNumber);
				if (szFileNumber.length() == 1)
					szFileNumber = "00" + szFileNumber;
				if (szFileNumber.length() == 2)
					szFileNumber = "0" + szFileNumber;

				Util.setPhotoFileNumber(Photo_Details.this, szFileNumber);

				HashMap<String, String> mapPhotoValues = new HashMap<String, String>();
				mapPhotoValues.put(XML_Values.PICTURE, m_CapturedImagePath); // image
																				// path
				mapPhotoValues.put(XML_Values.IMAGE_NAME, m_szFileName);
				mapPhotoValues.put(XML_Values.PHOTO_DATE, m_szDateTime);
				mapPhotoValues.put(XML_Values.PHOTO_LOCATION, szLocation);
				mapPhotoValues.put(XML_Values.POSITION, szPosition);
				mapPhotoValues.put(XML_Values.MOTOR_PART, szMotorPart);
				mapPhotoValues.put(XML_Values.COMMENTS, szComments);
				mapPhotoValues.put(XML_Values.THIRDGENITEMID,
						m_szThirdGenItemID);
				mapPhotoValues.put(
						XML_Values.MILL_ID,
						Util.getMillID(Photo_Details.this,
								Util.getMillName(Photo_Details.this)));
				new XML_Creation().save_All_Photos(mapPhotoValues);

				Log.d("Photo_Details", "PhotoDetails:: " + m_szFileName + " "
						+ szLocation + " " + szPosition + " " + szComments
						+ " " + szMotorPart + " " + m_szThirdGenItemID);

				if (progressPhotoDetailsEntry != null)
					progressPhotoDetailsEntry.setVisibility(View.GONE);

				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Photo_Details.this)
						.setMessage("Data saved successfully!")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {
										Photographs.bIsCameraOrGalleryOn = true;
										finish();
									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			} else {
				progressPhotoDetailsEntry.setVisibility(View.GONE);

				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Photo_Details.this)
						.setTitle("Alert!")
						.setMessage("All fields are mandatory")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			File file = new File(m_CapturedImagePath);
			file.delete();
			Util.setPhotoEntryDetails(Photo_Details.this, m_szFileName, "", "",
					"", "", "");
			Photographs.bIsCameraOrGalleryOn = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
