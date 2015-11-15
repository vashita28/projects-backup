package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

public class Activity_Pressfinger_feeder extends ParentActivity {
	private final String TAG = "ACTIVITY PRESSFINGER FEEDER";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;
	TextView tv_number;

	TextView tv_Numbering_Heading, tv_Numbering_Text;

	EditText et_in_outwards, et_displacement;

	LinearLayout ll_Items_Add_Rows_Pressfinger;
	View view_Pressfinger;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Pressfinger_Feeder;
	int child_Feeder = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressPressfingerFeeder;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pressfinger_feeder);

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
		page_Title_TextView.setText("Pressfinger Feeder");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontText);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Pressfinger_Feeder = new HashMap<String, String>();

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

		ll_Items_Add_Rows_Pressfinger = (LinearLayout) findViewById(R.id.ll_pressfinger_rows);

		if (((LinearLayout) ll_Items_Add_Rows_Pressfinger).getChildCount() > 0)
			((LinearLayout) ll_Items_Add_Rows_Pressfinger).removeAllViews();

		progressPressfingerFeeder = (ProgressBar) findViewById(R.id.progress_pressfinger_feeder);
		progressPressfingerFeeder.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Save_All_Values_Function(false);
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Feeder();

		// Writing in xml
		XML_Creation save_Pressfinger_Feeder = new XML_Creation();
		save_Pressfinger_Feeder.save_Pressfinger_Feeder(
				hm_Pressfinger_Feeder,
				Util.getMillID(Activity_Pressfinger_feeder.this,
						Util.getMillName(Activity_Pressfinger_feeder.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Pressfinger_feeder.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Pressfinger Feeder' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		if (showSaveDialog) {
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					Activity_Pressfinger_feeder.this)
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

	// Adding rows in main layout
	private void layout_Add_Pressfinger_Rows_Function() {
		// TODO Auto-generated method stub
		int rows = 34;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Pressfinger = inflater.inflate(
						R.layout.simplerow_pressfinger_feeder, null);
				String number = null;
				number = String.valueOf(i);
				Log.v("**************NUMBER VALUE****************", number);
				tv_number = (TextView) view_Pressfinger
						.findViewById(R.id.tv_numbers);

				if (number != null)
					tv_number.setText(number);

				et_in_outwards = (EditText) view_Pressfinger
						.findViewById(R.id.et_in_outwards);
				et_in_outwards.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_displacement = (EditText) view_Pressfinger
						.findViewById(R.id.et_displacement);
				et_displacement.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				ll_Items_Add_Rows_Pressfinger.addView(view_Pressfinger);

				if (i == rows) {
					ll_Items_Add_Rows_Pressfinger.removeView(view_Pressfinger);
					ll_Items_Add_Rows_Pressfinger.addView(view_Pressfinger);

				}
				number = "";
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
						Activity_Pressfinger_feeder.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_PressFingerFeeder(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Pressfinger_feeder.this,
											Util.getMillName(Activity_Pressfinger_feeder.this)));
				else
					xmlData = parser
							.parse_PressFingerFeeder(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Pressfinger_feeder.this,
											Util.getMillName(Activity_Pressfinger_feeder.this)));
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
			if (xmlData.containsKey(XML_Values.PRESSFINGER_FEEDER_COUNT))
				nCount_Feeder = Integer.parseInt(xmlData
						.get(XML_Values.PRESSFINGER_FEEDER_COUNT));

			if (nCount_Feeder == 0) {
				layout_Add_Pressfinger_Rows_Function();
			} else {
				populating_Data_Feeder_function(nCount_Feeder);
			}
			if (progressPressfingerFeeder != null)
				progressPressfingerFeeder.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Layout_Feeder() {

		child_Feeder = ll_Items_Add_Rows_Pressfinger.getChildCount();
		int id_Count = child_Feeder;

		for (int i = 0; i < id_Count; i++) {

			View Pressfinger_Feeder_View = ll_Items_Add_Rows_Pressfinger
					.getChildAt(i);

			tv_number = (TextView) Pressfinger_Feeder_View
					.findViewById(R.id.tv_numbers);

			et_in_outwards = (EditText) Pressfinger_Feeder_View
					.findViewById(R.id.et_in_outwards);

			et_displacement = (EditText) Pressfinger_Feeder_View
					.findViewById(R.id.et_displacement);

			hm_Pressfinger_Feeder.put(
					(XML_Values.PRESSFINGER_FEEDER_FINGER_PLATE + i), tv_number
							.getText().toString());
			hm_Pressfinger_Feeder.put(
					(XML_Values.PRESSFINGER_FEEDER_IN_OR_OUTWARDS + i),
					et_in_outwards.getText().toString());
			hm_Pressfinger_Feeder.put(
					(XML_Values.PRESSFINGER_FEEDER_DISPLACEMENT + i),
					et_displacement.getText().toString());

			Log.v("============ id==== data========== ",
					(XML_Values.PRESSFINGER_FEEDER_FINGER_PLATE + i)
							+ "  "
							+ (XML_Values.PRESSFINGER_FEEDER_IN_OR_OUTWARDS + i)
							+ "  "
							+ (XML_Values.PRESSFINGER_FEEDER_DISPLACEMENT + i)
							+ "     &   " + tv_number.getText().toString()
							+ " " + et_in_outwards.getText().toString() + "  "
							+ et_displacement.getText().toString());

		}
		hm_Pressfinger_Feeder.put(XML_Values.PRESSFINGER_FEEDER_COUNT,
				String.valueOf(child_Feeder));
	}

	private void populating_Data_Feeder_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Feeder = inflater.inflate(
						R.layout.simplerow_pressfinger_feeder, null);

				TextView tv_number = (TextView) view_Feeder
						.findViewById(R.id.tv_numbers);

				EditText et_in_outwards = (EditText) view_Feeder
						.findViewById(R.id.et_in_outwards);

				EditText et_displacement = (EditText) view_Feeder
						.findViewById(R.id.et_displacement);

				tv_number.setText(DecodeXML(xmlData
						.get(XML_Values.PRESSFINGER_FEEDER_FINGER_PLATE + i)));
				et_in_outwards
						.setText(DecodeXML(xmlData
								.get(XML_Values.PRESSFINGER_FEEDER_IN_OR_OUTWARDS
										+ i)));
				et_displacement.setText(DecodeXML(xmlData
						.get(XML_Values.PRESSFINGER_FEEDER_DISPLACEMENT + i)));

				ll_Items_Add_Rows_Pressfinger.removeView(view_Feeder);
				ll_Items_Add_Rows_Pressfinger.addView(view_Feeder);

				if (i == rows) {
					ll_Items_Add_Rows_Pressfinger.removeView(view_Feeder);
					ll_Items_Add_Rows_Pressfinger.addView(view_Feeder);
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
