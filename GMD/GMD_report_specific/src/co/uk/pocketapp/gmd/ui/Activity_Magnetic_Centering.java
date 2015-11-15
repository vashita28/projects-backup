package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.ui.Activity_Air_Gap.populateValuesTask;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Magnetic_Centering extends ParentActivity {
	private final String TAG = "ACTIVITY MAGNETIC CENTERING";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	EditText et_winding_temp, et_stator_temp, et_ambient_temp, et_remarks;

	LinearLayout ll_Addrows_Magnetic_Centering;
	View view_MC;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Magnetic_Centering;
	int child_Magnetic_Centering = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressMagneticCentering;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.magnetic_centering);

		if (getIntent().getExtras() != null) {
			m_szMillID = getIntent().getStringExtra("millid");
			m_szServicesItemID = getIntent().getStringExtra("parent_id");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);

		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);
		page_Title_TextView.setText("Magnetic Centering");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontHeading);

		et_winding_temp = (EditText) findViewById(R.id.et_winding_text);
		et_stator_temp = (EditText) findViewById(R.id.et_stator_text);
		et_ambient_temp = (EditText) findViewById(R.id.et_Ambiante_text);
		et_remarks = (EditText) findViewById(R.id.et_remarks);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);

		btn_Cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_SaveAndComplete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Save_All_Values_Function(true);
			}
		});

		ll_Addrows_Magnetic_Centering = (LinearLayout) findViewById(R.id.ll_magnetic_centering_rows);

		if (((LinearLayout) ll_Addrows_Magnetic_Centering).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Magnetic_Centering).removeAllViews();

		progressMagneticCentering = (ProgressBar) findViewById(R.id.progress_magnetic_centering);
		progressMagneticCentering.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Save_All_Values_Function(false);
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Magnetic_Centering();

		hm_Magnetic_Centering.put((XML_Values.MAGNETIC_CENTERING_WINDING_T),
				et_winding_temp.getText().toString());
		hm_Magnetic_Centering.put((XML_Values.MAGNETIC_CENTERING_STATOR_T),
				et_stator_temp.getText().toString());
		hm_Magnetic_Centering.put((XML_Values.MAGNETIC_CENTERING_AMBIENT_T),
				et_ambient_temp.getText().toString());
		hm_Magnetic_Centering.put((XML_Values.MAGNETIC_CENTERING_REMARKS),
				et_remarks.getText().toString());

		// Writing in xml
		XML_Creation save_Magnetic_Centering = new XML_Creation();
		save_Magnetic_Centering.save_Magnetic_Centering(
				hm_Magnetic_Centering,
				Util.getMillID(Activity_Magnetic_Centering.this,
						Util.getMillName(Activity_Magnetic_Centering.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Magnetic_Centering.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Magnetic Centering' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		if (showSaveDialog) {
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					Activity_Magnetic_Centering.this)
					.setTitle("Success!")
					.setMessage("Data saved successfully!")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									finish();
								}
							}).create();
			m_AlertDialog.setCanceledOnTouchOutside(false);
			m_AlertDialog.show();
		}
	}

	// Adding rows in main layout:Physical measurement
	private void layout_Add_Rows_Magnetic_Centering_Function() {
		// TODO Auto-generated method stub
		int rows = 8;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_MC = inflater.inflate(
						R.layout.simplerow_magnetic_centering, null);

				EditText et_position = (EditText) view_MC
						.findViewById(R.id.et_position);
				et_position.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_pole_no = (EditText) view_MC
						.findViewById(R.id.et_pole_no);
				et_pole_no.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_feeder_side = (EditText) view_MC
						.findViewById(R.id.et_feeder_side);
				et_feeder_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_discharge_side = (EditText) view_MC
						.findViewById(R.id.et_discharge_side);
				et_discharge_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_deviation = (EditText) view_MC
						.findViewById(R.id.et_deviation);
				et_deviation.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				if (i == 1) {
					et_position.setText("1.30");
				} else if (i == 2) {
					et_position.setText("3.00");
				} else if (i == 3) {
					et_position.setText("4.30");
				} else if (i == 4) {
					et_position.setText("6.00");
				} else if (i == 5) {
					et_position.setText("7.30");
				} else if (i == 6) {
					et_position.setText("9.00");
				} else if (i == 7) {
					et_position.setText("9.30");
				} else if (i == 8) {
					et_position.setText("12.00");
				}

				ll_Addrows_Magnetic_Centering.addView(view_MC);

				if (i == rows) {
					ll_Addrows_Magnetic_Centering.removeView(view_MC);
					ll_Addrows_Magnetic_Centering.addView(view_MC);

				}
			}
		}
	}

	class populateValuesTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			StringBuilder text = null;
			String szDecryptedText = "";
			try {
				if (AppValues.getReportXMLFile().exists()) {
					text = new StringBuilder();
					try {
						BufferedReader br = new BufferedReader(new FileReader(
								AppValues.getReportXMLFile()));
						String line;

						while ((line = br.readLine()) != null) {
							text.append(line);
							text.append('\n');
						}
						br.close();
						if (AppValues.bIsEncryptionRequired) {
							szDecryptedText = AES.aesDecrypt(text.toString(),
									AES.SHA256(""));
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				xmlData = new HashMap<String, String>();
				ResponseParser parser = new ResponseParser(
						Activity_Magnetic_Centering.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_MagneticCentering(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Magnetic_Centering.this,
											Util.getMillName(Activity_Magnetic_Centering.this)));
				else
					xmlData = parser
							.parse_MagneticCentering(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Magnetic_Centering.this,
											Util.getMillName(Activity_Magnetic_Centering.this)));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (xmlData.containsKey(XML_Values.MAGNETIC_CENTERING_WINDING_T)) {
				et_winding_temp.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_WINDING_T)));
			} else
				et_winding_temp.setText("");

			if (xmlData.containsKey(XML_Values.MAGNETIC_CENTERING_STATOR_T)) {
				et_stator_temp.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_STATOR_T)));
			} else
				et_stator_temp.setText("");

			if (xmlData.containsKey(XML_Values.MAGNETIC_CENTERING_AMBIENT_T)) {
				et_ambient_temp.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_AMBIENT_T)));
			} else
				et_ambient_temp.setText("");

			if (xmlData.containsKey(XML_Values.MAGNETIC_CENTERING_REMARKS)) {
				et_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_REMARKS)));
			} else
				et_remarks.setText("");
			int nCount_MC = 0;
			if (xmlData.containsKey(XML_Values.MAGNETIC_CENTERING_COUNT))
				nCount_MC = Integer.parseInt(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_COUNT));

			if (nCount_MC == 0) {
				layout_Add_Rows_Magnetic_Centering_Function();
			} else {
				populating_Data_Magnetic_Centering_function(nCount_MC);
			}

			if (progressMagneticCentering != null)
				progressMagneticCentering.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Layout_Magnetic_Centering() {

		hm_Magnetic_Centering = new HashMap<String, String>();

		child_Magnetic_Centering = ll_Addrows_Magnetic_Centering
				.getChildCount();
		int id_Count = child_Magnetic_Centering;

		for (int i = 0; i < id_Count; i++) {

			View Magnetic_View = ll_Addrows_Magnetic_Centering.getChildAt(i);

			EditText et_position = (EditText) Magnetic_View
					.findViewById(R.id.et_position);

			EditText et_pole_no = (EditText) Magnetic_View
					.findViewById(R.id.et_pole_no);

			EditText et_feeder_side = (EditText) Magnetic_View
					.findViewById(R.id.et_feeder_side);

			EditText et_discharge_side = (EditText) Magnetic_View
					.findViewById(R.id.et_discharge_side);

			EditText et_deviation = (EditText) Magnetic_View
					.findViewById(R.id.et_deviation);

			hm_Magnetic_Centering.put(
					(XML_Values.MAGNETIC_CENTERING_POSITION + i), et_position
							.getText().toString());
			hm_Magnetic_Centering.put(
					(XML_Values.MAGNETIC_CENTERING_POLE_NO + i), et_pole_no
							.getText().toString());
			hm_Magnetic_Centering.put(
					(XML_Values.MAGNETIC_CENTERING_FEEDER_SIDE + i),
					et_feeder_side.getText().toString());
			hm_Magnetic_Centering.put(
					(XML_Values.MAGNETIC_CENTERING_DISCHARGE_SIDE + i),
					et_discharge_side.getText().toString());
			hm_Magnetic_Centering.put(
					(XML_Values.MAGNETIC_CENTERING_DEVIATION + i), et_deviation
							.getText().toString());

			Log.v("============ id==== data========== ",
					(XML_Values.MAGNETIC_CENTERING_POSITION + i)
							+ "  "
							+ (XML_Values.MAGNETIC_CENTERING_POLE_NO + i)
							+ "  "
							+ (XML_Values.MAGNETIC_CENTERING_FEEDER_SIDE + i)
							+ " "
							+ (XML_Values.MAGNETIC_CENTERING_DISCHARGE_SIDE + i)
							+ " "
							+ (XML_Values.MAGNETIC_CENTERING_DEVIATION + i)
							+ "     &   " + et_position.getText().toString()
							+ " " + et_pole_no.getText().toString() + "  "
							+ et_feeder_side.getText().toString() + " "
							+ et_discharge_side.getText().toString() + " "
							+ et_deviation.getText().toString());

		}
		hm_Magnetic_Centering.put(XML_Values.MAGNETIC_CENTERING_COUNT,
				String.valueOf(child_Magnetic_Centering));
	}

	private void populating_Data_Magnetic_Centering_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_MC = inflater.inflate(
						R.layout.simplerow_magnetic_centering, null);

				EditText et_position = (EditText) view_MC
						.findViewById(R.id.et_position);

				EditText et_pole_no = (EditText) view_MC
						.findViewById(R.id.et_pole_no);

				EditText et_feeder_side = (EditText) view_MC
						.findViewById(R.id.et_feeder_side);

				EditText et_discharge_side = (EditText) view_MC
						.findViewById(R.id.et_discharge_side);

				EditText et_deviation = (EditText) view_MC
						.findViewById(R.id.et_deviation);

				et_position.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_POSITION + i)));
				et_pole_no.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_POLE_NO + i)));
				et_feeder_side.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_FEEDER_SIDE + i)));
				et_discharge_side
						.setText(DecodeXML(xmlData
								.get(XML_Values.MAGNETIC_CENTERING_DISCHARGE_SIDE
										+ i)));
				et_deviation.setText(DecodeXML(xmlData
						.get(XML_Values.MAGNETIC_CENTERING_DEVIATION + i)));

				ll_Addrows_Magnetic_Centering.removeView(view_MC);
				ll_Addrows_Magnetic_Centering.addView(view_MC);

				if (i == rows) {
					ll_Addrows_Magnetic_Centering.removeView(view_MC);
					ll_Addrows_Magnetic_Centering.addView(view_MC);
				}
			}
		}

	}

	String DecodeXML(String szString) {
		szString = szString.replaceAll("&amp;", "&");
		szString = szString.replaceAll("&lt;", "<");
		szString = szString.replaceAll("&gt;", ">");
		szString = szString.replaceAll("&apos;", "'");
		szString = szString.replaceAll("&quot;", "\"");
		return szString;
	}
}
