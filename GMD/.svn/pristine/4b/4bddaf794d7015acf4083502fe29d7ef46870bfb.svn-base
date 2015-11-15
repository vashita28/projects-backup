package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import co.uk.pocketapp.gmd.ui.Activity_Air_Gap.saveDatatask;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Sole_Plate extends ParentActivity {
	private final String TAG = "ACTIVITY SOLE PLATE";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	LinearLayout ll_Rows_Sole_plate;
	View view_Item;
	EditText et_Power, et_kW, et_Measurement1, et_Measurement2,
			et_Measurement3;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Sole_Plate;
	int child_SolePlate = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressSolePlate;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sole_plate);

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
		page_Title_TextView.setText("Sole Plate");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontText);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Sole_Plate = new HashMap<String, String>();

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
				progressSolePlate.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Rows_Sole_plate = (LinearLayout) findViewById(R.id.ll_rows_sole_plate);

		if (((LinearLayout) ll_Rows_Sole_plate).getChildCount() > 0)
			((LinearLayout) ll_Rows_Sole_plate).removeAllViews();

		progressSolePlate = (ProgressBar) findViewById(R.id.progress_soleplate);
		progressSolePlate.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);
		// progressSolePlate.setVisibility(View.VISIBLE);
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
						Activity_Sole_Plate.this)
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

				if (progressSolePlate != null)
					progressSolePlate.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Sole_Plate();

		// Writing in xml
		XML_Creation save_SolePlate = new XML_Creation();
		save_SolePlate.save_SolePlate(
				hm_Sole_Plate,
				Util.getMillID(Activity_Sole_Plate.this,
						Util.getMillName(Activity_Sole_Plate.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Sole_Plate.this) + "' AND "
						+ DataProvider.Tasks.TASK_NAME + " ='Sole Plate' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Sole_Plate.this)
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

	// Adding rows in main layout
	private void layout_Add_Key_Rows_function() {

		// TODO Auto-generated method stub
		int rows = 17;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Item = inflater.inflate(R.layout.simplerow_sole_plate,
						null);

				et_Power = (EditText) view_Item.findViewById(R.id.et_power);
				et_Power.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_kW = (EditText) view_Item.findViewById(R.id.et_kw);
				et_kW.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_Measurement1 = (EditText) view_Item
						.findViewById(R.id.et_measurement_1);
				et_Measurement1.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_Measurement2 = (EditText) view_Item
						.findViewById(R.id.et_measurement_2);
				et_Measurement2.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_Measurement3 = (EditText) view_Item
						.findViewById(R.id.et_measurement_3);
				et_Measurement3.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_Power.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_kW.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_Measurement1.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_Measurement2.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_Measurement3.setImeOptions(EditorInfo.IME_ACTION_DONE);

				if (i == 1) {
					et_Power.setText("Stator Voltage");
					et_kW.setText("V");
				} else if (i == 2) {
					et_Power.setText("Stator Current");
					et_kW.setText("A");
				} else if (i == 3) {
					et_Power.setText("Excitation Voltage");
					et_kW.setText("V");
				} else if (i == 4) {
					et_Power.setText("Excitation Current");
					et_kW.setText("A");
				} else if (i == 5) {
					et_Power.setText("Hot Air Temperature-Left");
					et_kW.setText("\u2103");
				} else if (i == 6) {
					et_Power.setText("Hot Air Temperature-Right");
					et_kW.setText("\u2103");
				} else if (i == 7) {
					et_Power.setText("Winding Temperature U 1(4.00) ");
					et_kW.setText("\u2103");
				} else if (i == 8) {
					et_Power.setText("Winding Temperature U 2(7.00)");
					et_kW.setText("\u2103");
				} else if (i == 9) {
					et_Power.setText("Winding Temperature U 3(11.00)");
					et_kW.setText("\u2103");
				} else if (i == 10) {
					et_Power.setText("Winding Temperature V 1(4.00) ");
					et_kW.setText("\u2103");
				} else if (i == 11) {
					et_Power.setText("Winding Temperature V 2(8.00)");
					et_kW.setText("\u2103");
				} else if (i == 12) {
					et_Power.setText("Winding Temperature V 3(1.00)");
					et_kW.setText("\u2103");
				} else if (i == 13) {
					et_Power.setText("Winding Temperature W 1(6.00)");
					et_kW.setText("\u2103");
				} else if (i == 14) {
					et_Power.setText("Winding Temperature W 2(10.00) ");
					et_kW.setText("\u2103");
				} else if (i == 15) {
					et_Power.setText("Winding Temperature W 3(2.00)");
					et_kW.setText("\u2103");
				} else if (i == 16) {
					et_Power.setText("Clearance at Feeder Side");
					et_kW.setText("mm");
				} else if (i == 17) {
					et_Power.setText("Clearance at Discharge Side)");
					et_kW.setText("mm");
				}

				ll_Rows_Sole_plate.addView(view_Item);

				if (i == rows) {
					ll_Rows_Sole_plate.removeView(view_Item);
					ll_Rows_Sole_plate.addView(view_Item);

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
						Activity_Sole_Plate.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_SolePlate(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Sole_Plate.this,
											Util.getMillName(Activity_Sole_Plate.this)));
				else
					xmlData = parser
							.parse_SolePlate(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Sole_Plate.this,
											Util.getMillName(Activity_Sole_Plate.this)));
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

			int nCount_Sole_plate = 0;
			if (xmlData.containsKey(XML_Values.SOLE_PLATE_COUNT))
				nCount_Sole_plate = Integer.parseInt(xmlData
						.get(XML_Values.SOLE_PLATE_COUNT));

			if (nCount_Sole_plate == 0) {
				layout_Add_Key_Rows_function();
			} else {
				populating_Data_SolePlate_function(nCount_Sole_plate);
			}

			if (progressSolePlate != null)
				progressSolePlate.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Sole_Plate() {

		child_SolePlate = ll_Rows_Sole_plate.getChildCount();
		int id_Count = child_SolePlate;

		for (int i = 0; i < id_Count; i++) {

			View SP_View = ll_Rows_Sole_plate.getChildAt(i);

			et_Power = (EditText) SP_View.findViewById(R.id.et_power);

			et_kW = (EditText) SP_View.findViewById(R.id.et_kw);

			et_Measurement1 = (EditText) SP_View
					.findViewById(R.id.et_measurement_1);

			et_Measurement2 = (EditText) SP_View
					.findViewById(R.id.et_measurement_2);

			et_Measurement3 = (EditText) SP_View
					.findViewById(R.id.et_measurement_3);

			hm_Sole_Plate.put((XML_Values.SOLE_PLATE_POWER + i), et_Power
					.getText().toString());
			hm_Sole_Plate.put((XML_Values.SOLE_PLATE_KW + i), et_kW.getText()
					.toString());
			hm_Sole_Plate.put((XML_Values.SOLE_PLATE_MEASUREMENT1 + i),
					et_Measurement1.getText().toString());
			hm_Sole_Plate.put((XML_Values.SOLE_PLATE_MEASUREMENT2 + i),
					et_Measurement2.getText().toString());
			hm_Sole_Plate.put((XML_Values.SOLE_PLATE_MEASUREMENT3 + i),
					et_Measurement3.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.SOLE_PLATE_POWER + i) + "  "
			// + (XML_Values.SOLE_PLATE_KW + i) + "  "
			// + (XML_Values.SOLE_PLATE_MEASUREMENT1 + i) + "  "
			// + (XML_Values.SOLE_PLATE_MEASUREMENT2 + i) + "  "
			// + (XML_Values.SOLE_PLATE_MEASUREMENT3 + i)
			// + "     &   " + et_Power.getText().toString());

		}
		hm_Sole_Plate.put(XML_Values.SOLE_PLATE_COUNT,
				String.valueOf(child_SolePlate));
	}

	private void populating_Data_SolePlate_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Sole_Plate = inflater.inflate(
						R.layout.simplerow_sole_plate, null);

				et_Power = (EditText) view_Sole_Plate
						.findViewById(R.id.et_power);

				et_kW = (EditText) view_Sole_Plate.findViewById(R.id.et_kw);

				et_Measurement1 = (EditText) view_Sole_Plate
						.findViewById(R.id.et_measurement_1);

				et_Measurement2 = (EditText) view_Sole_Plate
						.findViewById(R.id.et_measurement_2);

				et_Measurement3 = (EditText) view_Sole_Plate
						.findViewById(R.id.et_measurement_3);

				et_Power.setText(DecodeXML(xmlData
						.get(XML_Values.SOLE_PLATE_POWER + i)));
				et_kW.setText(DecodeXML(xmlData.get(XML_Values.SOLE_PLATE_KW
						+ i)));
				et_Measurement1.setText(DecodeXML(xmlData
						.get(XML_Values.SOLE_PLATE_MEASUREMENT1 + i)));
				et_Measurement2.setText(DecodeXML(xmlData
						.get(XML_Values.SOLE_PLATE_MEASUREMENT2 + i)));
				et_Measurement3.setText(DecodeXML(xmlData
						.get(XML_Values.SOLE_PLATE_MEASUREMENT3 + i)));

				ll_Rows_Sole_plate.removeView(view_Sole_Plate);
				ll_Rows_Sole_plate.addView(view_Sole_Plate);

				if (i == rows) {
					ll_Rows_Sole_plate.removeView(view_Sole_Plate);
					ll_Rows_Sole_plate.addView(view_Sole_Plate);
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
