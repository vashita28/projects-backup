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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.ui.Activity_Air_Gap.populateValuesTask;
import co.uk.pocketapp.gmd.ui.Activity_Air_Gap.saveDatatask;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Core_Partitions extends ParentActivity {
	private final String TAG = "ACTIVITY CORE PARTITIONS";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	LinearLayout ll_Addrows_Feederside, ll_Addrows_Dischargeside;
	View view_FS, view_DS;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Core_Partitions;
	int child_Feeder = 0;
	int child_Discharge = 0;
	HashMap<String, String> xmlData;
	ProgressBar progressCorePartitions;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.core_partitions);

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
		page_Title_TextView.setText("Core Partitions");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontHeading);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Core_Partitions = new HashMap<String, String>();

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

				progressCorePartitions.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Addrows_Feederside = (LinearLayout) findViewById(R.id.ll_feeder_side);

		ll_Addrows_Dischargeside = (LinearLayout) findViewById(R.id.ll_discharge);

		if (((LinearLayout) ll_Addrows_Feederside).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Feederside).removeAllViews();

		if (((LinearLayout) ll_Addrows_Dischargeside).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Dischargeside).removeAllViews();

		progressCorePartitions = (ProgressBar) findViewById(R.id.progress_corepartitions);
		progressCorePartitions.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);

		// progressCorePartitions.setVisibility(View.VISIBLE);
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
						Activity_Core_Partitions.this)
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

				if (progressCorePartitions != null)
					progressCorePartitions.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Feeder();
		get_Data_From_Layout_Discharge();

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Core_Partitions.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Core Partitions' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// Writing in xml
		XML_Creation save_Airgap_Data = new XML_Creation();
		save_Airgap_Data.save_CorePartitions(
				hm_Core_Partitions,
				Util.getMillID(Activity_Core_Partitions.this,
						Util.getMillName(Activity_Core_Partitions.this)));

		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Core_Partitions.this)
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

	// Adding rows in main layout:Feeder side
	private void layout_Add_Rows_Feeder_Side_Function() {
		// TODO Auto-generated method stub
		int rows = 8;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_FS = inflater.inflate(R.layout.simplerow_core_partitions,
						null);

				EditText et_position = (EditText) view_FS
						.findViewById(R.id.et_position);
				et_position.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_radial = (EditText) view_FS
						.findViewById(R.id.et_radial);
				et_radial.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_axial = (EditText) view_FS
						.findViewById(R.id.et_axial);
				et_axial.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_remarks = (EditText) view_FS
						.findViewById(R.id.et_remarks);
				et_remarks.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_position.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_radial.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_axial.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);

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

				ll_Addrows_Feederside.addView(view_FS);

				if (i == rows) {
					ll_Addrows_Feederside.removeView(view_FS);
					ll_Addrows_Feederside.addView(view_FS);

				}
			}
		}
	}

	// Adding rows in main layout:Discharge side
	private void layout_Add_Rows_Discharge_Side_Function() {
		// TODO Auto-generated method stub
		int rows = 8;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_DS = inflater.inflate(R.layout.simplerow_core_partitions,
						null);

				EditText et_position = (EditText) view_DS
						.findViewById(R.id.et_position);
				et_position.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_radial = (EditText) view_DS
						.findViewById(R.id.et_radial);
				et_radial.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_axial = (EditText) view_DS
						.findViewById(R.id.et_axial);
				et_axial.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_remarks = (EditText) view_DS
						.findViewById(R.id.et_remarks);
				et_remarks.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_position.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_radial.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_axial.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);

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

				ll_Addrows_Dischargeside.addView(view_DS);

				if (i == rows) {
					ll_Addrows_Dischargeside.removeView(view_DS);
					ll_Addrows_Dischargeside.addView(view_DS);

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
						Activity_Core_Partitions.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_CorePartitions(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Core_Partitions.this,
											Util.getMillName(Activity_Core_Partitions.this)));
				else
					xmlData = parser
							.parse_CorePartitions(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Core_Partitions.this,
											Util.getMillName(Activity_Core_Partitions.this)));
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
			int nCount_Feeder = 0;
			if (xmlData.containsKey(XML_Values.CORE_PARTITIONS_FEEDER_COUNT))
				nCount_Feeder = Integer.parseInt(xmlData
						.get(XML_Values.CORE_PARTITIONS_FEEDER_COUNT));

			int nCount_discharge = 0;
			if (xmlData.containsKey(XML_Values.CORE_PARTITIONS_DISCHARGE_COUNT))
				nCount_discharge = Integer.parseInt(xmlData
						.get(XML_Values.CORE_PARTITIONS_DISCHARGE_COUNT));

			if (nCount_Feeder == 0) {
				layout_Add_Rows_Feeder_Side_Function();
			} else {
				populating_Data_Feeder_function(nCount_Feeder);
			}

			if (nCount_discharge == 0) {
				layout_Add_Rows_Discharge_Side_Function();
			} else {
				populating_Data_Discharge_function(nCount_discharge);
			}

			if (progressCorePartitions != null)
				progressCorePartitions.setVisibility(View.GONE);
		}
	}

	private void get_Data_From_Layout_Feeder() {

		child_Feeder = ll_Addrows_Feederside.getChildCount();
		int id_Count = child_Feeder;

		for (int i = 0; i < id_Count; i++) {

			View Feeder_View = ll_Addrows_Feederside.getChildAt(i);

			EditText et_position = (EditText) Feeder_View
					.findViewById(R.id.et_position);

			EditText et_radial = (EditText) Feeder_View
					.findViewById(R.id.et_radial);

			EditText et_axial = (EditText) Feeder_View
					.findViewById(R.id.et_axial);

			EditText et_remarks = (EditText) Feeder_View
					.findViewById(R.id.et_remarks);

			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_FEEDER_POSITION + i),
					et_position.getText().toString());
			hm_Core_Partitions
					.put((XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT + i),
							et_radial.getText().toString());
			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT + i),
					et_axial.getText().toString());
			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS + i), et_remarks
							.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.CORE_PARTITIONS_FEEDER_POSITION + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_FEEDER_REMARKS + i)
			// + "     &   " + et_position.getText().toString()
			// + " " + et_radial.getText().toString() + "  "
			// + et_axial.getText().toString() + "  "
			// + et_remarks.getText().toString());

		}
		hm_Core_Partitions.put(XML_Values.CORE_PARTITIONS_FEEDER_COUNT,
				String.valueOf(child_Feeder));
	}

	private void get_Data_From_Layout_Discharge() {

		child_Discharge = ll_Addrows_Dischargeside.getChildCount();
		int id_Count = child_Discharge;

		for (int i = 0; i < id_Count; i++) {

			View Discharge_View = ll_Addrows_Dischargeside.getChildAt(i);

			EditText et_position = (EditText) Discharge_View
					.findViewById(R.id.et_position);

			EditText et_radial = (EditText) Discharge_View
					.findViewById(R.id.et_radial);

			EditText et_axial = (EditText) Discharge_View
					.findViewById(R.id.et_axial);

			EditText et_remarks = (EditText) Discharge_View
					.findViewById(R.id.et_remarks);

			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION + i),
					et_position.getText().toString());
			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP + i),
					et_radial.getText().toString());
			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP + i),
					et_axial.getText().toString());
			hm_Core_Partitions.put(
					(XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT + i),
					et_remarks.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP + i)
			// + "  "
			// + (XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT + i)
			// + "     &   " + et_position.getText().toString()
			// + " " + et_radial.getText().toString() + "  "
			// + et_axial.getText().toString() + "  "
			// + et_remarks.getText().toString());
		}
		hm_Core_Partitions.put(XML_Values.CORE_PARTITIONS_DISCHARGE_COUNT,
				String.valueOf(child_Discharge));
	}

	private void populating_Data_Feeder_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Feeder = inflater.inflate(
						R.layout.simplerow_core_partitions, null);

				EditText et_position = (EditText) view_Feeder
						.findViewById(R.id.et_position);

				EditText et_radial = (EditText) view_Feeder
						.findViewById(R.id.et_radial);

				EditText et_axial = (EditText) view_Feeder
						.findViewById(R.id.et_axial);

				EditText et_remarks = (EditText) view_Feeder
						.findViewById(R.id.et_remarks);

				et_position.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_FEEDER_POSITION + i)));
				et_radial
						.setText(DecodeXML(xmlData
								.get(XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT
										+ i)));
				et_axial.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT
								+ i)));
				et_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS + i)));

				ll_Addrows_Feederside.removeView(view_Feeder);
				ll_Addrows_Feederside.addView(view_Feeder);

				if (i == rows) {
					ll_Addrows_Feederside.removeView(view_Feeder);
					ll_Addrows_Feederside.addView(view_Feeder);
				}
			}
		}

	}

	private void populating_Data_Discharge_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Discharge = inflater.inflate(
						R.layout.simplerow_core_partitions, null);

				EditText et_position = (EditText) view_Discharge
						.findViewById(R.id.et_position);

				EditText et_radial = (EditText) view_Discharge
						.findViewById(R.id.et_radial);

				EditText et_axial = (EditText) view_Discharge
						.findViewById(R.id.et_axial);

				EditText et_remarks = (EditText) view_Discharge
						.findViewById(R.id.et_remarks);

				et_position
						.setText(DecodeXML(xmlData
								.get(XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION
										+ i)));
				et_radial.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP
								+ i)));
				et_axial.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP + i)));
				et_remarks.setText(DecodeXML(xmlData
						.get(XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT
								+ i)));

				ll_Addrows_Dischargeside.removeView(view_Discharge);
				ll_Addrows_Dischargeside.addView(view_Discharge);

				if (i == rows) {
					ll_Addrows_Dischargeside.removeView(view_Discharge);
					ll_Addrows_Dischargeside.addView(view_Discharge);
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
