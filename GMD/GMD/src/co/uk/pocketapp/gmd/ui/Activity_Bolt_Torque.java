package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import co.uk.pocketapp.gmd.util.AES;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.ResponseParser;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Creation;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Activity_Bolt_Torque extends ParentActivity {
	private final String TAG = "ACTIVITY BOLT TORQUE";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text, tv_Info, tv_Info_text;

	LinearLayout ll_Addrows_Bolt_Torque;
	View view_Bolt_Torque;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Bolt_torque;
	int child_BoltTorque = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressBoltTorque;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bolt_torque_check);

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
		page_Title_TextView.setText("Bolt Torque");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontHeading);

		tv_Info = (TextView) findViewById(R.id.information);
		tv_Info.setTypeface(GMDApplication.fontHeading);

		tv_Info_text = (TextView) findViewById(R.id.information_text);
		tv_Info_text.setTypeface(GMDApplication.fontHeading);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Bolt_torque = new HashMap<String, String>();

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

				progressBoltTorque.setVisibility(View.VISIBLE);
				btn_SaveAndComplete.setEnabled(false);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Addrows_Bolt_Torque = (LinearLayout) findViewById(R.id.ll_row_bolt_torque);

		if (((LinearLayout) ll_Addrows_Bolt_Torque).getChildCount() > 0)
			((LinearLayout) ll_Addrows_Bolt_Torque).removeAllViews();

		progressBoltTorque = (ProgressBar) findViewById(R.id.progress_bolttorque);
		progressBoltTorque.setVisibility(View.VISIBLE);
		btn_SaveAndComplete.setEnabled(false);
		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);

		// progressBoltTorque.setVisibility(View.VISIBLE);
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
						Activity_Bolt_Torque.this)
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

				if (progressBoltTorque != null) {
					btn_SaveAndComplete.setEnabled(true);
					progressBoltTorque.setVisibility(View.GONE);
				}
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		progressBoltTorque.setVisibility(View.VISIBLE);

		get_Data_From_Layout();
		// Writing in xml
		XML_Creation save_Bolt_Torque1 = new XML_Creation();
		save_Bolt_Torque1.save_Bolt_Torque(
				hm_Bolt_torque,
				Util.getMillID(Activity_Bolt_Torque.this,
						Util.getMillName(Activity_Bolt_Torque.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Bolt_Torque.this)
						+ "' AND " + DataProvider.Tasks.TASK_NAME
						+ " ='Bolt Torque' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// if (progressBoltTorque != null)
		// progressBoltTorque.setVisibility(View.GONE);
		//
		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Bolt_Torque.this)
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

	private void layout_Add_Rows_Bolt_Torque_Function() {
		// TODO Auto-generated method stub
		int rows = 240;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Bolt_Torque = inflater.inflate(
						R.layout.simplerow_bolt_torque, null);

				String number = null;
				number = String.valueOf(i);

				EditText et_item = (EditText) view_Bolt_Torque
						.findViewById(R.id.et_item);
				if (number != null)
					et_item.setText(number);
				et_item.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_initial_torque = (EditText) view_Bolt_Torque
						.findViewById(R.id.et_initial_torque);
				et_initial_torque.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_nut_angle = (EditText) view_Bolt_Torque
						.findViewById(R.id.et_nut_angle);
				et_nut_angle.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_final_torque = (EditText) view_Bolt_Torque
						.findViewById(R.id.et_final_torque);
				et_final_torque.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_item.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_initial_torque.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_nut_angle.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_final_torque.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Addrows_Bolt_Torque.addView(view_Bolt_Torque);

				if (i == rows) {
					ll_Addrows_Bolt_Torque.removeView(view_Bolt_Torque);
					ll_Addrows_Bolt_Torque.addView(view_Bolt_Torque);

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
						Activity_Bolt_Torque.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_BoltTorque(
									szDecryptedText,
									xmlData,
									Util.getMillID(
											Activity_Bolt_Torque.this,
											Util.getMillName(Activity_Bolt_Torque.this)));
				else
					xmlData = parser
							.parse_BoltTorque(
									text.toString(),
									xmlData,
									Util.getMillID(
											Activity_Bolt_Torque.this,
											Util.getMillName(Activity_Bolt_Torque.this)));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			Save_All_Values_Function(true);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			int nCount_boltTorque = 0;
			if (xmlData.containsKey(XML_Values.BOLT_TORQUE_COUNT)) {
				nCount_boltTorque = Integer.parseInt(xmlData
						.get(XML_Values.BOLT_TORQUE_COUNT));
			}

			if (nCount_boltTorque == 0) {
				layout_Add_Rows_Bolt_Torque_Function();
			} else {
				populating_Data_BoltTorque_function(nCount_boltTorque);
			}

			if (progressBoltTorque != null) {
				btn_SaveAndComplete.setEnabled(true);
				progressBoltTorque.setVisibility(View.GONE);
			}

		}
	}

	private void get_Data_From_Layout() {

		child_BoltTorque = ll_Addrows_Bolt_Torque.getChildCount();
		int id_Count = child_BoltTorque;

		for (int i = 0; i < id_Count; i++) {

			View BT_View = ll_Addrows_Bolt_Torque.getChildAt(i);

			EditText et_item = (EditText) BT_View.findViewById(R.id.et_item);

			EditText et_initial_torque = (EditText) BT_View
					.findViewById(R.id.et_initial_torque);

			EditText et_nut_angle = (EditText) BT_View
					.findViewById(R.id.et_nut_angle);

			EditText et_final_torque = (EditText) BT_View
					.findViewById(R.id.et_final_torque);

			hm_Bolt_torque.put((XML_Values.BOLT_ITEM_NUMBER + i), et_item
					.getText().toString());
			hm_Bolt_torque.put((XML_Values.BOLT_INTIAL_TORQUE + i),
					et_initial_torque.getText().toString());
			hm_Bolt_torque.put((XML_Values.BOLT_NUT_VALUE + i), et_nut_angle
					.getText().toString());
			hm_Bolt_torque.put((XML_Values.BOLT_FINAL_TORQUE + i),
					et_final_torque.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.BOLT_ITEM_NUMBER + i) + "  "
			// + (XML_Values.BOLT_INTIAL_TORQUE + i) + "  "
			// + (XML_Values.BOLT_NUT_VALUE + i) + "  "
			// + (XML_Values.BOLT_FINAL_TORQUE + i) + "     &   "
			// + et_item.getText().toString());

		}
		hm_Bolt_torque.put(XML_Values.BOLT_TORQUE_COUNT,
				String.valueOf(child_BoltTorque));
	}

	private void populating_Data_BoltTorque_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_BoltTorque = inflater.inflate(
						R.layout.simplerow_bolt_torque, null);

				EditText et_item = (EditText) view_BoltTorque
						.findViewById(R.id.et_item);

				EditText et_initial_torque = (EditText) view_BoltTorque
						.findViewById(R.id.et_initial_torque);

				EditText et_nut_angle = (EditText) view_BoltTorque
						.findViewById(R.id.et_nut_angle);

				EditText et_final_torque = (EditText) view_BoltTorque
						.findViewById(R.id.et_final_torque);

				et_item.setText(DecodeXML(xmlData
						.get(XML_Values.BOLT_ITEM_NUMBER + i)));
				et_initial_torque.setText(DecodeXML(xmlData
						.get(XML_Values.BOLT_INTIAL_TORQUE + i)));
				et_nut_angle.setText(DecodeXML(xmlData
						.get(XML_Values.BOLT_NUT_VALUE + i)));
				et_final_torque.setText(DecodeXML(xmlData
						.get(XML_Values.BOLT_FINAL_TORQUE + i)));

				ll_Addrows_Bolt_Torque.removeView(view_BoltTorque);
				ll_Addrows_Bolt_Torque.addView(view_BoltTorque);

				if (i == rows) {
					ll_Addrows_Bolt_Torque.removeView(view_BoltTorque);
					ll_Addrows_Bolt_Torque.addView(view_BoltTorque);
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
