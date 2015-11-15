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
import co.uk.pocketapp.gmd.ui.Activity_Air_Gap.saveDatatask;
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Bolt_Insulation extends ParentActivity {
	private final String TAG = "ACTIVITY BOLT INSULATION";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	HashMap<String, String> xmlData;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	LinearLayout ll_Addrows_Bolt_Insulation;
	View view_Bolt_Insulation;
	int childcount_Bolt_Insulation = 0;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_BoltInsulation;

	ProgressBar progressBoltInsulation;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bolt_insulation);

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
		page_Title_TextView.setText("Bolt Insulation");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontHeading);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_BoltInsulation = new HashMap<String, String>();

		progressBoltInsulation = (ProgressBar) findViewById(R.id.progress_bolt_insulation);

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
				// Save_All_Values_Function(true);
				progressBoltInsulation.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Addrows_Bolt_Insulation = (LinearLayout) findViewById(R.id.ll_bolt_insulation_rows);

		if (((LinearLayout) ll_Addrows_Bolt_Insulation).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Bolt_Insulation).removeAllViews();

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);

		// progressBoltInsulation.setVisibility(View.VISIBLE);
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
						Activity_Bolt_Insulation.this)
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

				if (progressBoltInsulation != null)
					progressBoltInsulation.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {

		// TODO Auto-generated method stub
		get_Data_From_Layout();
		// Writing in xml
		XML_Creation save_Bolt_Insulation = new XML_Creation();
		save_Bolt_Insulation.save_Bolt_Insulation(
				hm_BoltInsulation,
				Util.getMillID(Activity_Bolt_Insulation.this,
						Util.getMillName(Activity_Bolt_Insulation.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Bolt_Insulation.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Bolt Insulation' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Bolt_Insulation.this)
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

	private void layout_Add_Rows_Bolt_Insulation_Function() {
		// TODO Auto-generated method stub
		int rows = 235;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Bolt_Insulation = inflater.inflate(
						R.layout.simplerow_bolt_insulation, null);

				String number = null;
				number = String.valueOf(i);

				EditText et_numbers = (EditText) view_Bolt_Insulation
						.findViewById(R.id.et_numbers);
				if (number != null)
					et_numbers.setText(number);
				et_numbers.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_mohms = (EditText) view_Bolt_Insulation
						.findViewById(R.id.et_mohms);
				et_mohms.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_numbers.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_mohms.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Addrows_Bolt_Insulation.addView(view_Bolt_Insulation);

				if (i == rows) {
					ll_Addrows_Bolt_Insulation.removeView(view_Bolt_Insulation);
					ll_Addrows_Bolt_Insulation.addView(view_Bolt_Insulation);

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
						Activity_Bolt_Insulation.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_BoltInsulation(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Bolt_Insulation.this,
											Util.getMillName(Activity_Bolt_Insulation.this)));
				else
					xmlData = parser
							.parse_BoltInsulation(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Bolt_Insulation.this,
											Util.getMillName(Activity_Bolt_Insulation.this)));
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
			int nCount_BoltInsulation = 0;
			if (xmlData.containsKey(XML_Values.BOLT_INSULATION_COUNT))
				nCount_BoltInsulation = Integer.parseInt(xmlData
						.get(XML_Values.BOLT_INSULATION_COUNT));

			if (nCount_BoltInsulation == 0) {
				layout_Add_Rows_Bolt_Insulation_Function();
			} else {
				populating_BoltInsulation_function(nCount_BoltInsulation);
			}

			if (progressBoltInsulation != null)
				progressBoltInsulation.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Layout() {

		childcount_Bolt_Insulation = ll_Addrows_Bolt_Insulation.getChildCount();
		int id_Count = childcount_Bolt_Insulation;

		for (int i = 0; i < id_Count; i++) {

			View BI_View = ll_Addrows_Bolt_Insulation.getChildAt(i);

			EditText et_mohms = (EditText) BI_View.findViewById(R.id.et_mohms);

			hm_BoltInsulation.put((XML_Values.BOLT_INSULATION_ITEM + i),
					et_mohms.getText().toString());
			// Log.v("============ id==== data========== ",
			// (XML_Values.BOLT_INSULATION_ITEM + i) + "     &   "
			// + et_mohms.getText().toString());

		}
		hm_BoltInsulation.put(XML_Values.BOLT_INSULATION_COUNT,
				String.valueOf(childcount_Bolt_Insulation));
	}

	private void populating_BoltInsulation_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_BoltInsulation = inflater.inflate(
						R.layout.simplerow_bolt_insulation, null);

				String number = null;
				number = String.valueOf(i);

				EditText et_numbers = (EditText) view_BoltInsulation
						.findViewById(R.id.et_numbers);
				if (number != null)
					et_numbers.setText(number);

				EditText et_mohms = (EditText) view_BoltInsulation
						.findViewById(R.id.et_mohms);

				et_mohms.setText(DecodeXML(xmlData
						.get(XML_Values.BOLT_INSULATION_VALUE + i)));

				ll_Addrows_Bolt_Insulation.removeView(view_BoltInsulation);
				ll_Addrows_Bolt_Insulation.addView(view_BoltInsulation);

				if (i == rows) {
					ll_Addrows_Bolt_Insulation.removeView(view_BoltInsulation);
					ll_Addrows_Bolt_Insulation.addView(view_BoltInsulation);
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
