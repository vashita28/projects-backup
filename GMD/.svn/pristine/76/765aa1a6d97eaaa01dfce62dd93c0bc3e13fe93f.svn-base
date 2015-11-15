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

public class Activity_Key_Bars extends ParentActivity {
	private final String TAG = "ACTIVITY KEY BARS";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;
	TextView tv_number;

	TextView tv_Numbering_Heading, tv_Numbering_Text,
			tv_item_moments_text_heading, tv_item_moments_text_belowtext;

	LinearLayout ll_Items_Add_Rows;
	View view_Item;

	Button btn_Cancel, btn_SaveAndComplete;
	int childcount_KeyBar = 0;

	HashMap<String, String> hm_Key_Bars;
	HashMap<String, String> xmlData;

	ProgressBar progressKeyBar;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.key_bars);

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
		page_Title_TextView.setText("Key Bars");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontText);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Key_Bars = new HashMap<String, String>();
		xmlData = new HashMap<String, String>();

		btn_Cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tv_item_moments_text_heading = (TextView) findViewById(R.id.item_moments_text_heading);
		tv_item_moments_text_heading.setTypeface(GMDApplication.fontHeading);

		tv_item_moments_text_belowtext = (TextView) findViewById(R.id.item_moments_text_belowtext);
		tv_item_moments_text_belowtext.setTypeface(GMDApplication.fontHeading);

		btn_SaveAndComplete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Save_All_Values_Function(true);
			}
		});

		ll_Items_Add_Rows = (LinearLayout) findViewById(R.id.ll_items);

		if (((LinearLayout) ll_Items_Add_Rows).getChildCount() > 0)
			((LinearLayout) ll_Items_Add_Rows).removeAllViews();

		progressKeyBar = (ProgressBar) findViewById(R.id.progress_keybar);
		progressKeyBar.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Save_All_Values_Function(false);
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout();
		// Writing in xml
		XML_Creation save_Key_Bar = new XML_Creation();
		save_Key_Bar.save_Key_Bars(
				hm_Key_Bars,
				Util.getMillID(Activity_Key_Bars.this,
						Util.getMillName(Activity_Key_Bars.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Key_Bars.this) + "' AND "
						+ DataProvider.Tasks.TASK_NAME + " ='Key Bars' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		if (showSaveDialog) {
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					Activity_Key_Bars.this)
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
	private void layout_Add_Key_Rows_function() {
		// TODO Auto-generated method stub
		int rows = 108;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Item = inflater.inflate(R.layout.simple_row_key_bar, null);
				String number = null;
				number = String.valueOf(i);
				Log.v("**************NUMBER VALUE****************", number);
				tv_number = (TextView) view_Item.findViewById(R.id.tv_numbers);

				if (number != null)
					tv_number.setText(number);

				EditText et_movement = (EditText) view_Item
						.findViewById(R.id.et_movement);
				et_movement.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				ll_Items_Add_Rows.addView(view_Item);

				if (i == rows) {
					ll_Items_Add_Rows.removeView(view_Item);
					ll_Items_Add_Rows.addView(view_Item);

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
						Activity_Key_Bars.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser.parse_KeyBar(
							szDecryptedText,
							xmlData,
							Util.getMillID(Activity_Key_Bars.this,
									Util.getMillName(Activity_Key_Bars.this)));
				else
					xmlData = parser.parse_KeyBar(
							text.toString(),
							xmlData,
							Util.getMillID(Activity_Key_Bars.this,
									Util.getMillName(Activity_Key_Bars.this)));
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
			int nCount_kerbars = 0;
			if (xmlData.containsKey(XML_Values.KEYBAR_COUNT))
				nCount_kerbars = Integer.parseInt(xmlData
						.get(XML_Values.KEYBAR_COUNT));

			if (nCount_kerbars == 0) {
				layout_Add_Key_Rows_function();
			} else {
				populating_Keybars_function(nCount_kerbars);
			}

			if (progressKeyBar != null)
				progressKeyBar.setVisibility(View.GONE);

		}
	}

	private void get_Data_From_Layout() {

		childcount_KeyBar = ll_Items_Add_Rows.getChildCount();
		int id_Count = childcount_KeyBar;

		for (int i = 0; i < id_Count; i++) {

			View moment_View = ll_Items_Add_Rows.getChildAt(i);

			EditText et_movement = (EditText) moment_View
					.findViewById(R.id.et_movement);
			et_movement.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			hm_Key_Bars.put((XML_Values.KEYBAR_ITEM + i), et_movement.getText()
					.toString());

			Log.v("============ id==== data========== ", (XML_Values.ITEM + i)
					+ "     &   " + et_movement.getText().toString());

		}
		hm_Key_Bars.put(XML_Values.KEYBAR_COUNT,
				String.valueOf(childcount_KeyBar));
		Log.v("============count keybar========= ", " " + (childcount_KeyBar));

	}

	private void populating_Keybars_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Keybars = inflater.inflate(
						R.layout.simple_row_key_bar, null);

				String number = null;
				number = String.valueOf(i);
				tv_number = (TextView) view_Keybars
						.findViewById(R.id.tv_numbers);

				if (number != null)
					tv_number.setText(number);

				EditText et_movement = (EditText) view_Keybars
						.findViewById(R.id.et_movement);
				et_movement.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_movement.setText(DecodeXML(xmlData
						.get(XML_Values.KEYBAR_VALUE + i)));

				ll_Items_Add_Rows.removeView(view_Keybars);
				ll_Items_Add_Rows.addView(view_Keybars);

				if (i == rows) {
					ll_Items_Add_Rows.removeView(view_Keybars);
					ll_Items_Add_Rows.addView(view_Keybars);
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
