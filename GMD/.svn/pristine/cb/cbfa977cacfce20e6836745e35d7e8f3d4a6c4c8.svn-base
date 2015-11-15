package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.ui.Summary.saveDatatask;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Air_Gap extends ParentActivity {
	private final String TAG = "ACTIVITY AIR GAP";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	EditText et_winding_temp, et_stator_temp, et_ambient_temp, et_remarks;

	LinearLayout ll_Addrows_Physical_Measurement,
			ll_Addrows_AirGap_Measurement;
	View view_PM, view_AGM;

	Button btn_Cancel, btn_SaveAndComplete;
	int childcount_KeyBar = 0;

	HashMap<String, String> hm_Air_Gap;
	int child_Airgap_P = 0;
	int child_Sensors = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressAirGap;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_gap_measurement);

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
		page_Title_TextView.setText("Air Gap");

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

		et_winding_temp.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_stator_temp.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_ambient_temp.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);

		progressAirGap = (ProgressBar) findViewById(R.id.progress_airgap);

		hm_Air_Gap = new HashMap<String, String>();

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

				// Save_All_Values_Function(true);

				progressAirGap.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Addrows_Physical_Measurement = (LinearLayout) findViewById(R.id.ll_physical_rows);

		ll_Addrows_AirGap_Measurement = (LinearLayout) findViewById(R.id.ll_airgap_rows);

		if (((LinearLayout) ll_Addrows_Physical_Measurement).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Physical_Measurement).removeAllViews();

		if (((LinearLayout) ll_Addrows_AirGap_Measurement).getChildCount() > 0)
			((LinearLayout) ll_Addrows_AirGap_Measurement).removeAllViews();

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		// Save_All_Values_Function(false);

		// progressAirGap.setVisibility(View.VISIBLE);
		// saveDatatask task = new saveDatatask();
		// task.flag = false;
		// task.execute();
	}

	public class saveDatatask extends AsyncTask<Void, Void, Void> {

		boolean flag = false;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Calling parser function:
			Save_All_Values_Function(flag);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (flag) {
				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Activity_Air_Gap.this)
						.setMessage("Data saved successfully!")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {
										finish();
									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();

				if (progressAirGap != null)
					progressAirGap.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Physical_Measurement();
		get_Data_From_Layout_Airgap_Sensors_Measurement();

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Air_Gap.this) + "' AND "
						+ DataProvider.Tasks.TASK_NAME + " ='Air Gap' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		hm_Air_Gap.put((XML_Values.AIRGAP_WINDING_T), et_winding_temp.getText()
				.toString());
		hm_Air_Gap.put((XML_Values.AIRGAP_STATOR_T), et_stator_temp.getText()
				.toString());
		hm_Air_Gap.put((XML_Values.AIRGAP_AMBIENT_T), et_ambient_temp.getText()
				.toString());
		hm_Air_Gap.put((XML_Values.AIRGAP_REMARKS), et_remarks.getText()
				.toString());

		// Writing in xml
		XML_Creation save_Airgap_Data = new XML_Creation();
		save_Airgap_Data.save_AirGap(
				hm_Air_Gap,
				Util.getMillID(Activity_Air_Gap.this,
						Util.getMillName(Activity_Air_Gap.this)));

		// if (progressAirGap != null)
		// progressAirGap.setVisibility(View.GONE);
		//
		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Air_Gap.this)
		// .setTitle("Success!")
		// .setMessage("Data saved successfully!")
		// .setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface argDialog,
		// int argWhich) {
		// finish();
		// }
		// }).create();
		// m_AlertDialog.setCanceledOnTouchOutside(false);
		// m_AlertDialog.show();
		// }
	}

	// Adding rows in main layout:Physical measurement
	private void layout_Add_Rows_Physical_Measurement_Function() {
		// TODO Auto-generated method stub
		int rows = 8;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_PM = inflater.inflate(
						R.layout.simplerow_air_gap_measurement, null);

				EditText et_position = (EditText) view_PM
						.findViewById(R.id.et_position);
				et_position.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_feeder_side = (EditText) view_PM
						.findViewById(R.id.et_feeder_side);
				et_feeder_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_discharge_side = (EditText) view_PM
						.findViewById(R.id.et_discharge_side);
				et_discharge_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_airgap_remarks = (EditText) view_PM
						.findViewById(R.id.et_airgap_remarks);
				et_airgap_remarks.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_position.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_feeder_side.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_discharge_side.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_airgap_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);

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

				ll_Addrows_Physical_Measurement.addView(view_PM);

				if (i == rows) {
					ll_Addrows_Physical_Measurement.removeView(view_PM);
					ll_Addrows_Physical_Measurement.addView(view_PM);

				}
			}
		}
	}

	private void layout_Add_Rows_Airgap_Sensors_Measurement_Function() {
		// TODO Auto-generated method stub
		int rows = 8;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_AGM = inflater.inflate(
						R.layout.simplerow_air_gap_measurement, null);

				EditText et_position = (EditText) view_AGM
						.findViewById(R.id.et_position);
				et_position.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_feeder_side = (EditText) view_AGM
						.findViewById(R.id.et_feeder_side);
				et_feeder_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_discharge_side = (EditText) view_AGM
						.findViewById(R.id.et_discharge_side);
				et_discharge_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_airgap_remarks = (EditText) view_AGM
						.findViewById(R.id.et_airgap_remarks);
				et_airgap_remarks.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_position.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_feeder_side.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_discharge_side.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_airgap_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);

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

				ll_Addrows_AirGap_Measurement.addView(view_AGM);

				if (i == rows) {
					ll_Addrows_AirGap_Measurement.removeView(view_AGM);
					ll_Addrows_AirGap_Measurement.addView(view_AGM);

				}
			}
		}
	}

	class populateValuesTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

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
						Activity_Air_Gap.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser.parse_AirGap(
							szDecryptedText,
							xmlData,
							Util.getMillID(Activity_Air_Gap.this,
									Util.getMillName(Activity_Air_Gap.this)));
				else
					xmlData = parser.parse_AirGap(
							text.toString(),
							xmlData,
							Util.getMillID(Activity_Air_Gap.this,
									Util.getMillName(Activity_Air_Gap.this)));
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

			if (xmlData.containsKey(XML_Values.AIRGAP_WINDING_T)) {
				et_winding_temp.setText(DecodeXML(xmlData
						.get(XML_Values.AIRGAP_WINDING_T)));
			} else
				et_winding_temp.setText("");

			if (xmlData.containsKey(XML_Values.AIRGAP_STATOR_T)) {
				et_stator_temp.setText(DecodeXML(xmlData
						.get(XML_Values.AIRGAP_STATOR_T)));
			} else
				et_stator_temp.setText("");

			if (xmlData.containsKey(XML_Values.AIRGAP_AMBIENT_T)) {
				et_ambient_temp.setText(DecodeXML(xmlData
						.get(XML_Values.AIRGAP_AMBIENT_T)));
			} else
				et_ambient_temp.setText("");

			if (xmlData.containsKey(XML_Values.AIRGAP_REMARKS)) {
				et_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.AIRGAP_REMARKS)));
			} else
				et_remarks.setText("");

			int nCount_Physical = 0;
			if (xmlData
					.containsKey(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_COUNT))
				nCount_Physical = Integer.parseInt(xmlData
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_COUNT));

			int nCount_Sensors = 0;
			if (xmlData
					.containsKey(XML_Values.AIR_GAP_SENSORS_MEASUREMENT_COUNT))
				nCount_Sensors = Integer.parseInt(xmlData
						.get(XML_Values.AIR_GAP_SENSORS_MEASUREMENT_COUNT));

			if (nCount_Physical == 0) {
				layout_Add_Rows_Physical_Measurement_Function();
			} else {
				populating_Data_Physical_function(nCount_Physical);
			}
			if (nCount_Sensors == 0) {
				layout_Add_Rows_Airgap_Sensors_Measurement_Function();
			} else {
				populating_Data_Sensors_function(nCount_Sensors);
			}
			if (progressAirGap != null)
				progressAirGap.setVisibility(View.GONE);
		}

	}

	private void get_Data_From_Layout_Physical_Measurement() {
		child_Airgap_P = ll_Addrows_Physical_Measurement.getChildCount();
		int id_Count = child_Airgap_P;
		for (int i = 0; i < id_Count; i++) {
			View PM_View = ll_Addrows_Physical_Measurement.getChildAt(i);
			EditText et_position = (EditText) PM_View
					.findViewById(R.id.et_position);

			EditText et_feeder_side = (EditText) PM_View
					.findViewById(R.id.et_feeder_side);
			EditText et_discharge_side = (EditText) PM_View
					.findViewById(R.id.et_discharge_side);

			EditText et_airgap_remarks = (EditText) PM_View
					.findViewById(R.id.et_airgap_remarks);
			hm_Air_Gap.put(
					(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION + i),
					et_position.getText().toString());
			hm_Air_Gap.put(
					(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE + i),
					et_feeder_side.getText().toString());
			hm_Air_Gap.put(
					(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE + i),
					et_discharge_side.getText().toString());
			hm_Air_Gap.put(
					(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS + i),
					et_airgap_remarks.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION + i)
			// + "  "
			// + (XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE + i)
			// + "  "
			// + (XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE + i)
			// + "  "
			// + (XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS + i)
			// + "     &   " + et_position.getText().toString());

		}
		hm_Air_Gap.put(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_COUNT,
				String.valueOf(child_Airgap_P));
	}

	private void get_Data_From_Layout_Airgap_Sensors_Measurement() {

		child_Sensors = ll_Addrows_AirGap_Measurement.getChildCount();
		int id_Count = child_Sensors;

		for (int i = 0; i < id_Count; i++) {

			View AGS_View = ll_Addrows_AirGap_Measurement.getChildAt(i);

			EditText et_position = (EditText) AGS_View
					.findViewById(R.id.et_position);

			EditText et_feeder_side = (EditText) AGS_View
					.findViewById(R.id.et_feeder_side);

			EditText et_discharge_side = (EditText) AGS_View
					.findViewById(R.id.et_discharge_side);

			EditText et_airgap_remarks = (EditText) AGS_View
					.findViewById(R.id.et_airgap_remarks);

			hm_Air_Gap.put((XML_Values.AIR_GAP_POSITION + i), et_position
					.getText().toString());
			hm_Air_Gap.put((XML_Values.AIR_GAP_FEEDER_SIDE + i), et_feeder_side
					.getText().toString());
			hm_Air_Gap.put((XML_Values.AIR_GAP_DISCHARGE_SIDE + i),
					et_discharge_side.getText().toString());
			hm_Air_Gap.put((XML_Values.AIR_GAP_REMARKS + i), et_airgap_remarks
					.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.AIR_GAP_POSITION + i) + "  "
			// + (XML_Values.AIR_GAP_FEEDER_SIDE + i) + "  "
			// + (XML_Values.AIR_GAP_DISCHARGE_SIDE + i) + "  "
			// + (XML_Values.AIR_GAP_REMARKS + i) + "     &   "
			// + et_position.getText().toString());

		}
		hm_Air_Gap.put(XML_Values.AIR_GAP_SENSORS_MEASUREMENT_COUNT,
				String.valueOf(child_Sensors));
	}

	private void populating_Data_Physical_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Physical = inflater.inflate(
						R.layout.simplerow_air_gap_measurement, null);

				EditText et_position = (EditText) view_Physical
						.findViewById(R.id.et_position);

				EditText et_feeder_side = (EditText) view_Physical
						.findViewById(R.id.et_feeder_side);

				EditText et_discharge_side = (EditText) view_Physical
						.findViewById(R.id.et_discharge_side);

				EditText et_airgap_remarks = (EditText) view_Physical
						.findViewById(R.id.et_airgap_remarks);

				et_position.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION
								+ i)));
				et_feeder_side
						.setText(DecodeXML(xmlData
								.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE
										+ i)));
				et_discharge_side.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE
								+ i)));
				et_airgap_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS
								+ i)));

				ll_Addrows_Physical_Measurement.removeView(view_Physical);
				ll_Addrows_Physical_Measurement.addView(view_Physical);

				if (i == rows) {
					ll_Addrows_Physical_Measurement.removeView(view_Physical);
					ll_Addrows_Physical_Measurement.addView(view_Physical);
				}
			}
		}

	}

	private void populating_Data_Sensors_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Sensors = inflater.inflate(
						R.layout.simplerow_air_gap_measurement, null);

				EditText et_position = (EditText) view_Sensors
						.findViewById(R.id.et_position);

				EditText et_feeder_side = (EditText) view_Sensors
						.findViewById(R.id.et_feeder_side);

				EditText et_discharge_side = (EditText) view_Sensors
						.findViewById(R.id.et_discharge_side);

				EditText et_airgap_remarks = (EditText) view_Sensors
						.findViewById(R.id.et_airgap_remarks);

				et_position.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_POSITION + i)));
				et_feeder_side.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_FEEDER_SIDE + i)));
				et_discharge_side.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_DISCHARGE_SIDE + i)));
				et_airgap_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.AIR_GAP_REMARKS + i)));

				ll_Addrows_AirGap_Measurement.removeView(view_Sensors);
				ll_Addrows_AirGap_Measurement.addView(view_Sensors);

				if (i == rows) {
					ll_Addrows_AirGap_Measurement.removeView(view_Sensors);
					ll_Addrows_AirGap_Measurement.addView(view_Sensors);
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
