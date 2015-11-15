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

public class Activity_Brushwear extends ParentActivity {
	private final String TAG = "ACTIVITY BRUSH WEAR";

	TextView GMD_heading_TextView, page_Title_TextView, page_Main_Heading;

	TextView tv_Numbering_Heading, tv_Numbering_Text,
			tv_item_moments_text_heading, tv_item_moments_text_belowtext;

	LinearLayout ll_Add_Inner_Rings, ll_Add_Outer_Rings, ll_Add_Ground;
	View view_Inner_Ring, view_Outer_Ring, view_Ground;

	Button btn_Cancel, btn_SaveAndComplete;

	HashMap<String, String> hm_Brushwear;
	int child_Inner_ring = 0;
	int child_Outer_ring = 0;
	int child_Ground = 0;
	HashMap<String, String> xmlData;

	ProgressBar progressBrushWear;

	String m_szMillID = "", m_szServicesItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brush_wear);

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
		page_Title_TextView.setText("Brush Wear");

		page_Main_Heading = (TextView) findViewById(R.id.page_heading_middle);
		page_Main_Heading.setTypeface(GMDApplication.fontText);

		tv_Numbering_Heading = (TextView) findViewById(R.id.numbering_Heading);
		tv_Numbering_Heading.setTypeface(GMDApplication.fontHeading);

		tv_Numbering_Text = (TextView) findViewById(R.id.numbering_text);
		tv_Numbering_Text.setTypeface(GMDApplication.fontText);

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);

		btn_Cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_Cancel = (Button) findViewById(R.id.btn_cancel);
		btn_SaveAndComplete = (Button) findViewById(R.id.btn_saveandcomplete);
		hm_Brushwear = new HashMap<String, String>();

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

				progressBrushWear.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});

		ll_Add_Inner_Rings = (LinearLayout) findViewById(R.id.ll_add_inner_ring);

		ll_Add_Outer_Rings = (LinearLayout) findViewById(R.id.ll_add_outer_ring);

		ll_Add_Ground = (LinearLayout) findViewById(R.id.ll_add_ground);

		if (((LinearLayout) ll_Add_Inner_Rings).getChildCount() > 0)
			((LinearLayout) ll_Add_Inner_Rings).removeAllViews();

		if (((LinearLayout) ll_Add_Outer_Rings).getChildCount() > 0)
			((LinearLayout) ll_Add_Outer_Rings).removeAllViews();

		if (((LinearLayout) ll_Add_Ground).getChildCount() > 0)
			((LinearLayout) ll_Add_Ground).removeAllViews();

		progressBrushWear = (ProgressBar) findViewById(R.id.progress_brushwear);
		progressBrushWear.setVisibility(View.VISIBLE);

		new populateValuesTask().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Save_All_Values_Function(false);

		// progressBrushWear.setVisibility(View.VISIBLE);
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
						Activity_Brushwear.this)
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

				if (progressBrushWear != null)
					progressBrushWear.setVisibility(View.GONE);
			}

		}
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {
		get_Data_From_Layout_Inner_Rings();
		get_Data_From_Layout_Outer_Rings();
		get_Data_From_Layout_Ground();

		// Writing in xml
		XML_Creation save_Brushwear = new XML_Creation();
		save_Brushwear.save_BrushWear(
				hm_Brushwear,
				Util.getMillID(Activity_Brushwear.this,
						Util.getMillName(Activity_Brushwear.this)));

		ContentValues taskValues = new ContentValues();
		taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "true");

		getContentResolver().update(
				DataProvider.Tasks.CONTENT_URI,
				taskValues,
				DataProvider.Tasks.MILL_ID + " ='" + m_szMillID + "' AND "
						+ DataProvider.Tasks.REPORT_ID + " ='"
						+ Util.getReportID(Activity_Brushwear.this) + "' AND "
						+ DataProvider.Tasks.TASK_NAME + " ='Brush Wear' AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
						+ m_szServicesItemID + "'", null);
		getContentResolver().notifyChange(DataProvider.Tasks.CONTENT_URI, null);

		// if (showSaveDialog) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Activity_Brushwear.this)
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

	// Adding rows in main layout:Inner Rings
	private void layout_Add_Rows_Inner_Rings() {
		// TODO Auto-generated method stub
		int rows = 10;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Inner_Ring = inflater.inflate(
						R.layout.simplerow_brushwear, null);
				String number = null;
				number = String.valueOf(i);

				EditText et_brush = (EditText) view_Inner_Ring
						.findViewById(R.id.et_brush);
				et_brush.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				if (number != null)
					et_brush.setText(number);

				EditText et_mm = (EditText) view_Inner_Ring
						.findViewById(R.id.et_mm);
				et_mm.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_date = (EditText) view_Inner_Ring
						.findViewById(R.id.et_date);
				et_date.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_time = (EditText) view_Inner_Ring
						.findViewById(R.id.et_time);
				et_time.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_brush.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_mm.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_date.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_time.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Add_Inner_Rings.addView(view_Inner_Ring);

				if (i == rows) {
					ll_Add_Inner_Rings.removeView(view_Inner_Ring);
					ll_Add_Inner_Rings.addView(view_Inner_Ring);

				}
			}
		}
	}

	private void layout_Add_Rows_Outer_Rings() {
		// TODO Auto-generated method stub
		int rows = 10;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Outer_Ring = inflater.inflate(
						R.layout.simplerow_brushwear, null);
				String number = null;
				number = String.valueOf(i);

				EditText et_brush = (EditText) view_Outer_Ring
						.findViewById(R.id.et_brush);
				et_brush.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				if (number != null)
					et_brush.setText(number);

				EditText et_mm = (EditText) view_Outer_Ring
						.findViewById(R.id.et_mm);
				et_mm.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_date = (EditText) view_Outer_Ring
						.findViewById(R.id.et_date);
				et_date.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_time = (EditText) view_Outer_Ring
						.findViewById(R.id.et_time);
				et_time.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_brush.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_mm.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_date.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_time.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Add_Outer_Rings.addView(view_Outer_Ring);

				if (i == rows) {
					ll_Add_Outer_Rings.removeView(view_Outer_Ring);
					ll_Add_Outer_Rings.addView(view_Outer_Ring);

				}
			}
		}
	}

	private void layout_Add_Rows_Grounds() {
		// TODO Auto-generated method stub
		int rows = 2;
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_Ground = inflater.inflate(R.layout.simplerow_brushwear,
						null);
				String number = null;
				number = String.valueOf(i);

				EditText et_brush = (EditText) view_Ground
						.findViewById(R.id.et_brush);
				et_brush.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				if (number != null)
					et_brush.setText(number);

				EditText et_mm = (EditText) view_Ground
						.findViewById(R.id.et_mm);
				et_mm.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_date = (EditText) view_Ground
						.findViewById(R.id.et_date);
				et_date.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText et_time = (EditText) view_Ground
						.findViewById(R.id.et_time);
				et_time.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_brush.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_mm.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_date.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_time.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_Add_Ground.addView(view_Ground);

				if (i == rows) {
					ll_Add_Ground.removeView(view_Ground);
					ll_Add_Ground.addView(view_Ground);

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
						Activity_Brushwear.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser.parse_BrushWear(
							szDecryptedText,
							xmlData,
							Util.getMillID(Activity_Brushwear.this,
									Util.getMillName(Activity_Brushwear.this)));
				else
					xmlData = parser.parse_BrushWear(
							text.toString(),
							xmlData,
							Util.getMillID(Activity_Brushwear.this,
									Util.getMillName(Activity_Brushwear.this)));
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

			int nCount_Inner = 0;
			if (xmlData.containsKey(XML_Values.BRUSH_WEAR_INNER_RING_COUNT))
				nCount_Inner = Integer.parseInt(xmlData
						.get(XML_Values.BRUSH_WEAR_INNER_RING_COUNT));

			int nCount_Outer = 0;
			if (xmlData.containsKey(XML_Values.BRUSH_WEAR_OUTER_RING_COUNT))
				nCount_Outer = Integer.parseInt(xmlData
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_COUNT));

			int nCount_Ground = 0;
			if (xmlData.containsKey(XML_Values.BRUSH_WEAR_GROUND_COUNT))
				nCount_Ground = Integer.parseInt(xmlData
						.get(XML_Values.BRUSH_WEAR_GROUND_COUNT));

			if (nCount_Inner == 0) {
				layout_Add_Rows_Inner_Rings();
			} else {
				populating_Data_InnerRings_function(nCount_Inner);
			}

			if (nCount_Outer == 0) {
				layout_Add_Rows_Outer_Rings();
			} else {
				populating_Data_OuterRings_function(nCount_Outer);
			}

			if (nCount_Ground == 0) {
				layout_Add_Rows_Grounds();
			} else {
				populating_Data_Ground_function(nCount_Ground);
			}

			if (progressBrushWear != null)
				progressBrushWear.setVisibility(View.GONE);
		}
	}

	private void get_Data_From_Layout_Inner_Rings() {

		child_Inner_ring = ll_Add_Inner_Rings.getChildCount();
		int id_Count = child_Inner_ring;

		for (int i = 0; i < id_Count; i++) {

			View Inner_Rings_View = ll_Add_Inner_Rings.getChildAt(i);

			EditText et_brush = (EditText) Inner_Rings_View
					.findViewById(R.id.et_brush);

			EditText et_mm = (EditText) Inner_Rings_View
					.findViewById(R.id.et_mm);

			EditText et_date = (EditText) Inner_Rings_View
					.findViewById(R.id.et_date);

			EditText et_time = (EditText) Inner_Rings_View
					.findViewById(R.id.et_time);

			hm_Brushwear.put((XML_Values.BRUSH_WEAR_INNER_RING_BRUSH + i),
					et_brush.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_INNER_RING_MM + i), et_mm
					.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_INNER_RING_DATE + i),
					et_date.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_INNER_RING_TYPE + i),
					et_time.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.BRUSH_WEAR_INNER_RING_BRUSH + i) + "  "
			// + (XML_Values.BRUSH_WEAR_INNER_RING_MM + i) + "  "
			// + (XML_Values.BRUSH_WEAR_INNER_RING_DATE + i) + " "
			// + (XML_Values.BRUSH_WEAR_INNER_RING_TYPE + i)
			// + "     &   " + et_brush.getText().toString() + " "
			// + et_mm.getText().toString() + "  "
			// + et_date.getText().toString() + " "
			// + et_time.getText().toString());

		}
		hm_Brushwear.put(XML_Values.BRUSH_WEAR_INNER_RING_COUNT,
				String.valueOf(child_Inner_ring));
	}

	private void get_Data_From_Layout_Outer_Rings() {

		child_Outer_ring = ll_Add_Outer_Rings.getChildCount();
		int id_Count = child_Outer_ring;

		for (int i = 0; i < id_Count; i++) {

			View Inner_Rings_View = ll_Add_Outer_Rings.getChildAt(i);

			EditText et_brush = (EditText) Inner_Rings_View
					.findViewById(R.id.et_brush);

			EditText et_mm = (EditText) Inner_Rings_View
					.findViewById(R.id.et_mm);

			EditText et_date = (EditText) Inner_Rings_View
					.findViewById(R.id.et_date);

			EditText et_time = (EditText) Inner_Rings_View
					.findViewById(R.id.et_time);

			hm_Brushwear.put((XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH + i),
					et_brush.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_OUTER_RING_MM + i), et_mm
					.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_OUTER_RING_DATE + i),
					et_date.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_OUTER_RING_TYPE + i),
					et_time.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH + i) + "  "
			// + (XML_Values.BRUSH_WEAR_OUTER_RING_MM + i) + "  "
			// + (XML_Values.BRUSH_WEAR_OUTER_RING_DATE + i) + " "
			// + (XML_Values.BRUSH_WEAR_OUTER_RING_TYPE + i)
			// + "     &   " + et_brush.getText().toString() + " "
			// + et_mm.getText().toString() + "  "
			// + et_date.getText().toString() + " "
			// + et_time.getText().toString());

		}
		hm_Brushwear.put(XML_Values.BRUSH_WEAR_OUTER_RING_COUNT,
				String.valueOf(child_Outer_ring));
	}

	private void get_Data_From_Layout_Ground() {

		child_Ground = ll_Add_Ground.getChildCount();
		int id_Count = child_Ground;

		for (int i = 0; i < id_Count; i++) {

			View Inner_Rings_View = ll_Add_Ground.getChildAt(i);

			EditText et_brush = (EditText) Inner_Rings_View
					.findViewById(R.id.et_brush);

			EditText et_mm = (EditText) Inner_Rings_View
					.findViewById(R.id.et_mm);

			EditText et_date = (EditText) Inner_Rings_View
					.findViewById(R.id.et_date);

			EditText et_time = (EditText) Inner_Rings_View
					.findViewById(R.id.et_time);

			hm_Brushwear.put((XML_Values.BRUSH_WEAR_GROUND_BRUSH + i), et_brush
					.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_GROUND_MM + i), et_mm
					.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_GROUND_DATE + i), et_date
					.getText().toString());
			hm_Brushwear.put((XML_Values.BRUSH_WEAR_GROUND_TYPE + i), et_time
					.getText().toString());

			// Log.v("============ id==== data========== ",
			// (XML_Values.BRUSH_WEAR_GROUND_BRUSH + i) + "  "
			// + (XML_Values.BRUSH_WEAR_GROUND_MM + i) + "  "
			// + (XML_Values.BRUSH_WEAR_GROUND_DATE + i) + " "
			// + (XML_Values.BRUSH_WEAR_GROUND_TYPE + i)
			// + "     &   " + et_brush.getText().toString() + " "
			// + et_mm.getText().toString() + "  "
			// + et_date.getText().toString() + " "
			// + et_time.getText().toString());

		}
		hm_Brushwear.put(XML_Values.BRUSH_WEAR_GROUND_COUNT,
				String.valueOf(child_Ground));
	}

	private void populating_Data_InnerRings_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_IR = inflater.inflate(R.layout.simplerow_brushwear,
						null);

				EditText et_brush = (EditText) view_IR
						.findViewById(R.id.et_brush);

				EditText et_mm = (EditText) view_IR.findViewById(R.id.et_mm);

				EditText et_date = (EditText) view_IR
						.findViewById(R.id.et_date);

				EditText et_time = (EditText) view_IR
						.findViewById(R.id.et_time);

				et_brush.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_INNER_RING_BRUSH + i)));
				et_mm.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_INNER_RING_MM + i)));
				et_date.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_INNER_RING_DATE + i)));
				et_time.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_INNER_RING_TYPE + i)));

				ll_Add_Inner_Rings.removeView(view_IR);
				ll_Add_Inner_Rings.addView(view_IR);

				if (i == rows) {
					ll_Add_Inner_Rings.removeView(view_IR);
					ll_Add_Inner_Rings.addView(view_IR);
				}
			}
		}

	}

	private void populating_Data_OuterRings_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_OR = inflater.inflate(R.layout.simplerow_brushwear,
						null);

				EditText et_brush = (EditText) view_OR
						.findViewById(R.id.et_brush);

				EditText et_mm = (EditText) view_OR.findViewById(R.id.et_mm);

				EditText et_date = (EditText) view_OR
						.findViewById(R.id.et_date);

				EditText et_type = (EditText) view_OR
						.findViewById(R.id.et_time);

				et_brush.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH + i)));
				et_mm.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_MM + i)));
				et_date.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_DATE + i)));
				et_type.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_TYPE + i)));

				ll_Add_Outer_Rings.removeView(view_OR);
				ll_Add_Outer_Rings.addView(view_OR);

				if (i == rows) {
					ll_Add_Outer_Rings.removeView(view_OR);
					ll_Add_Outer_Rings.addView(view_OR);
				}
			}
		}

	}

	private void populating_Data_Ground_function(int rows) {

		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_G = inflater.inflate(R.layout.simplerow_brushwear,
						null);

				EditText et_brush = (EditText) view_G
						.findViewById(R.id.et_brush);

				EditText et_mm = (EditText) view_G.findViewById(R.id.et_mm);

				EditText et_date = (EditText) view_G.findViewById(R.id.et_date);

				EditText et_type = (EditText) view_G.findViewById(R.id.et_time);

				et_brush.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_GROUND_BRUSH + i)));
				et_mm.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_GROUND_MM + i)));
				et_date.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_GROUND_DATE + i)));
				et_type.setText(DecodeXML(xmlData
						.get(XML_Values.BRUSH_WEAR_GROUND_TYPE + i)));

				ll_Add_Ground.removeView(view_G);
				ll_Add_Ground.addView(view_G);

				if (i == rows) {
					ll_Add_Ground.removeView(view_G);
					ll_Add_Ground.addView(view_G);
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
