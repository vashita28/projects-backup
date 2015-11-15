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

public class Activity_Holding_Plates extends ParentActivity {
	private final String TAG = "ACTIVITY HOLDING PLATES";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	LinearLayout ll_Addrows_Holding_Plates;
	View view_HP;

	Button btn_Cancel, btn_SaveAndComplete;
	int childcount_KeyBar = 0;

	HashMap<String, String> hm_Holding_Plates;
	int child_Holding_Plates = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressHoldingPlates;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holding_plates);

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
		page_Title_TextView.setText("Holding Plates");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontHeading);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Holding_Plates = new HashMap<String, String>();

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

				progressHoldingPlates.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Addrows_Holding_Plates = (LinearLayout) findViewById(R.id.ll_holding_plates_rows);

		if (((LinearLayout) ll_Addrows_Holding_Plates).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Holding_Plates).removeAllViews();

		progressHoldingPlates = (ProgressBar) findViewById(R.id.progress_holding_plates);
		progressHoldingPlates.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);

		// progressHoldingPlates.setVisibility(View.VISIBLE);
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
						Activity_Holding_Plates.this)
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

				if (progressHoldingPlates != null)
					progressHoldingPlates.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Holding_Plates();

		// Writing in xml
		XML_Creation save_Holding_Plates = new XML_Creation();
		save_Holding_Plates.save_Holding_Plates(
				hm_Holding_Plates,
				Util.getMillID(Activity_Holding_Plates.this,
						Util.getMillName(Activity_Holding_Plates.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Holding_Plates.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Holding Plates' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Holding_Plates.this)
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

	// Adding rows in main layout:Holding Plates
	private void layout_Add_Rows_Holding_Plates_Function() {
		// TODO Auto-generated method stub
		int rows = 140;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_HP = inflater.inflate(R.layout.simplerow_holding_plates,
						null);

				String number = null;
				number = String.valueOf(i);

				EditText et_keybar = (EditText) view_HP
						.findViewById(R.id.et_keybar);
				if (number != null)
					et_keybar.setText(number);

				et_keybar.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_connection_side = (EditText) view_HP
						.findViewById(R.id.et_connection_side);
				et_connection_side.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_center = (EditText) view_HP
						.findViewById(R.id.et_center);
				et_center.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_non_connection_side = (EditText) view_HP
						.findViewById(R.id.et_non_connection_side);
				et_non_connection_side.setHintTextColor(getResources()
						.getColor(R.color.hint_text_color));

				et_keybar.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_connection_side.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_center.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_non_connection_side
						.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Addrows_Holding_Plates.addView(view_HP);

				if (i == rows) {
					ll_Addrows_Holding_Plates.removeView(view_HP);
					ll_Addrows_Holding_Plates.addView(view_HP);

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
						Activity_Holding_Plates.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_HoldingPlates(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Holding_Plates.this,
											Util.getMillName(Activity_Holding_Plates.this)));
				else
					xmlData = parser
							.parse_HoldingPlates(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Holding_Plates.this,
											Util.getMillName(Activity_Holding_Plates.this)));
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
			int nCount_HoldingPlates = 0;
			if (xmlData.containsKey(XML_Values.HOLDING_PLATES_COUNT))
				nCount_HoldingPlates = Integer.parseInt(xmlData
						.get(XML_Values.HOLDING_PLATES_COUNT));

			if (nCount_HoldingPlates == 0) {
				layout_Add_Rows_Holding_Plates_Function();
			} else {
				populating_Data_HoldingPlates_function(nCount_HoldingPlates);
			}

			if (progressHoldingPlates != null)
				progressHoldingPlates.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Layout_Holding_Plates() {

		child_Holding_Plates = ll_Addrows_Holding_Plates.getChildCount();
		int id_Count = child_Holding_Plates;

		for (int i = 0; i < id_Count; i++) {

			View Holding_Plates_View = ll_Addrows_Holding_Plates.getChildAt(i);

			EditText et_keybar = (EditText) Holding_Plates_View
					.findViewById(R.id.et_keybar);

			EditText et_connection_side = (EditText) Holding_Plates_View
					.findViewById(R.id.et_connection_side);

			EditText et_center = (EditText) Holding_Plates_View
					.findViewById(R.id.et_center);

			EditText et_non_connection_side = (EditText) Holding_Plates_View
					.findViewById(R.id.et_non_connection_side);

			hm_Holding_Plates.put((XML_Values.HOLDING_PLATES_KEYBAR + i),
					et_keybar.getText().toString());
			hm_Holding_Plates.put((XML_Values.HOLDING_PLATE1 + i),
					et_connection_side.getText().toString());
			hm_Holding_Plates.put((XML_Values.HOLDING_PLATE2 + i), et_center
					.getText().toString());
			hm_Holding_Plates.put((XML_Values.HOLDING_PLATE3 + i),
					et_non_connection_side.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.HOLDING_PLATES_KEYBAR + i) + "  "
			// + (XML_Values.HOLDING_PLATE1 + i) + "  "
			// + (XML_Values.HOLDING_PLATE2 + i) + " "
			// + (XML_Values.HOLDING_PLATE3 + i) + "     &   "
			// + et_keybar.getText().toString() + " "
			// + et_connection_side.getText().toString() + "  "
			// + et_center.getText().toString() + " "
			// + et_non_connection_side.getText().toString());

		}
		hm_Holding_Plates.put(XML_Values.HOLDING_PLATES_COUNT,
				String.valueOf(child_Holding_Plates));
	}

	private void populating_Data_HoldingPlates_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_HoldingPlates = inflater.inflate(
						R.layout.simplerow_holding_plates, null);

				EditText et_keybar = (EditText) view_HoldingPlates
						.findViewById(R.id.et_keybar);

				EditText et_connection_side = (EditText) view_HoldingPlates
						.findViewById(R.id.et_connection_side);

				EditText et_center = (EditText) view_HoldingPlates
						.findViewById(R.id.et_center);

				EditText et_non_connection_side = (EditText) view_HoldingPlates
						.findViewById(R.id.et_non_connection_side);

				et_keybar.setText(DecodeXML(xmlData
						.get(XML_Values.HOLDING_PLATES_KEYBAR + i)));
				et_connection_side.setText(DecodeXML(xmlData
						.get(XML_Values.HOLDING_PLATE1 + i)));
				et_center.setText(DecodeXML(xmlData
						.get(XML_Values.HOLDING_PLATE2 + i)));
				et_non_connection_side.setText(DecodeXML(xmlData
						.get(XML_Values.HOLDING_PLATE3 + i)));

				ll_Addrows_Holding_Plates.removeView(view_HoldingPlates);
				ll_Addrows_Holding_Plates.addView(view_HoldingPlates);

				if (i == rows) {
					ll_Addrows_Holding_Plates.removeView(view_HoldingPlates);
					ll_Addrows_Holding_Plates.addView(view_HoldingPlates);
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
