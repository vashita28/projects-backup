package co.uk.pocketapp.gmd.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Define_The_Solution extends ParentActivity {
	private static final String TAG = "DEFINE THE SOLUTIONS";

	Button back_Button;
	Button btn_save_and_complete;
	TextView page_Title_TextView;

	EditText editText_how_can_this_be_fixed;
	EditText editText_why_fix_it;
	Spinner spinner_Time_Frame;
	private ArrayAdapter<CharSequence> time_Frame_Spinner_Adapter;
	EditText editText_required_pesonnel;

	String et_how_can_This_Be_fixed = "";
	String et_Why_Fixed_It = "";
	String et_Required_Pesonnel = "";
	String sp_Time_frame = "";
	// To apply font:
	TextView GMD_heading_TextView;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.define_the_solution);
		context = Define_The_Solution.this;

		back_Button = (Button) findViewById(R.id.back_button);
		btn_save_and_complete = (Button) findViewById(R.id.btn_save_and_complete);

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);
		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);
		// Page title :Set page Heading
		page_Title_TextView.setText("Report Details");

		editText_how_can_this_be_fixed = (EditText) findViewById(R.id.et_how_can_this_be_fixed);
		editText_how_can_this_be_fixed.setHintTextColor(getResources()
				.getColor(R.color.hint_text_color));
		editText_how_can_this_be_fixed.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editText_why_fix_it = (EditText) findViewById(R.id.et_why_fix_it);
		editText_why_fix_it.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		editText_why_fix_it.setImeOptions(EditorInfo.IME_ACTION_DONE);
		editText_required_pesonnel = (EditText) findViewById(R.id.et_required_pesonnel);
		editText_required_pesonnel.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		editText_required_pesonnel.setImeOptions(EditorInfo.IME_ACTION_DONE);
		spinner_Time_Frame = (Spinner) findViewById(R.id.spinner_time_frame);
		// Setting spinner Data:
		time_Frame_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.time_frame_array, R.layout.custom_spinner_item);
		time_Frame_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_Time_Frame.setAdapter(time_Frame_Spinner_Adapter);
		spinner_Time_Frame
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

		// Next button
		btn_save_and_complete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				et_how_can_This_Be_fixed = editText_how_can_this_be_fixed
						.getText().toString().trim();
				et_Why_Fixed_It = editText_why_fix_it.getText().toString()
						.trim();
				et_Required_Pesonnel = editText_required_pesonnel.getText()
						.toString().trim();
				sp_Time_frame = spinner_Time_Frame.getSelectedItem().toString();

				if (et_how_can_This_Be_fixed.length() == 0
						|| et_Why_Fixed_It.length() == 0
						|| et_Required_Pesonnel.length() == 0
						|| spinner_Time_Frame.getSelectedItemPosition() == 0) {
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							Define_The_Solution.this)
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

				} else {

					Util.setPhotoEntryDetails(context,
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.IMAGE_NAME),
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.PHOTO_LOCATION),
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.POSITION),
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.COMMENTS),
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.MOTOR_PART),
							Report_A_Problem.hm_Problem_Data
									.get(XML_Values.PHOTO_DATE));

					int nNumber = Integer.parseInt(Util
							.getPhotoFileNumber(Define_The_Solution.this)) + 1;
					String szFileNumber = String.valueOf(nNumber);
					if (szFileNumber.length() == 1)
						szFileNumber = "00" + szFileNumber;
					if (szFileNumber.length() == 2)
						szFileNumber = "0" + szFileNumber;

					Util.setPhotoFileNumber(context, szFileNumber);

					Save_Values_In_XML();
					finish();
					if (Report_A_Problem.mContext != null) {
						try {
							((Activity) Report_A_Problem.mContext).finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		});

		back_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}

	private void Save_Values_In_XML() {

		Report_A_Problem.hm_Problem_Data.put(XML_Values.HOW_CAN_THIS_BE_FIXED,
				et_how_can_This_Be_fixed);
		Report_A_Problem.hm_Problem_Data.put(XML_Values.WHY_FIX_IT,
				et_Why_Fixed_It);
		Report_A_Problem.hm_Problem_Data.put(XML_Values.TIME_FRAME,
				sp_Time_frame);
		Report_A_Problem.hm_Problem_Data.put(XML_Values.REQUIRED_PERSONNAL,
				et_Required_Pesonnel);

		// call xml write function
		new XML_Creation().save_Report_A_Problem();
		// clear his hash map
		Report_A_Problem.hm_Problem_Data.clear();
	}
}
