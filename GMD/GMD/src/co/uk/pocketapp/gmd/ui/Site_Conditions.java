package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class Site_Conditions extends ParentActivity {
	private static final String TAG = "SITE CONDITIONS";
	TextView page_Title;

	// Button add_New_Material_Button;
	// Button cancel_Button;
	// Button save_Button;
	// ListView material_listview;

	// LinearLayout site_conditions_LL;
	// LinearLayout add_New_Material_Log_LL;

	// EditText material_name_Edittext;
	// Spinner material_Category_Spinner;
	// EditText information_Edittext;
	// private ArrayAdapter<CharSequence> material_Spinner_Adapter;

	// String Material_Name;
	// String Material_Category;
	// String Information;
	// String reportDate;

	// To apply font:
	TextView GMD_heading_TextView;
	TextView heading_Textview;
	TextView existing_Material_Logs;
	TextView new_material_heading_TextView;
	ArrayList<DataModel_Site_Conditions> listArray;
	Context context;

	// ListAdapter material_logs_adapter;
	HashMap<String, String> xmlData_Save = new HashMap<String, String>();

	Boolean bIsUpdate = false;
	String szMaterialID = "";

	Cursor mCursorMateriallogs;

	// NEW ONES::
	TextView tv_ssc_Heading;
	TextView tv_spas_Heading;
	TextView tv_tni_Heading;

	// Site Storage Conditions::
	// LinearLayout ll_SSC_Default_Values;
	private ArrayAdapter<CharSequence> checked_Spinner_Adapter_default;
	private ArrayAdapter<CharSequence> checked_Spinner_Adapter;
	View view_SSC;
	EditText et_Store_SSC;
	Spinner checked_Spinner_SSC;
	EditText et_Date_SSC;
	EditText et_Remarks_SSC;

	// Default first Row::Site Storage Condition
	EditText et_Store_SSC1;
	Spinner checked_Spinner_SSC1;
	EditText et_Date_SSC1;
	EditText et_Remarks_SSC1;
	Button btn_add_site_storage;
	LinearLayout ll_site_storage_conditions;
	EditText et_store_SSC_Added;
	Spinner spinner_checked_SSC;
	EditText et_date_SSC_Added;
	EditText et_remarks_SSC_Added;
	int childcount_SSC_Default = 0;
	int childcount_SSC_Added = 0;

	// Spare Parts At site::
	// LinearLayout ll_SPAS_Default_Values;
	View view_SPAS;

	// Default First Row:: Spare part at site
	EditText et_Material_SPAS1;
	EditText et_Quantity_SPAS1;
	EditText et_Date_SPAS1;
	EditText et_Remarks_SPAS1;

	EditText et_Material_SPAS;
	EditText et_Quantity_SPAS;
	EditText et_Date_SPAS;
	EditText et_Remarks_SPAS;
	Button btn_add_spare_part;
	LinearLayout ll_spare_parts_at_site;
	int childcount_SPAS_Default = 0;
	int childcount_SPAS_Added = 0;

	// Tools and instruments::
	// LinearLayout ll_TNI_Default_Values;
	View view_TNI;

	// Default first row::Tools and Instruments::
	EditText et_Tools_TNI1;
	EditText et_Quantity_TNI1;
	EditText et_Date_TNI1;
	EditText et_Remarks_TNI1;

	EditText et_Tools_TNI;
	EditText et_Quantity_TNI;
	EditText et_Date_TNI;
	EditText et_Remarks_TNI;
	Button btn_add_tools;
	LinearLayout ll_tools_instruments;
	int childcount_TNI_Default = 0;
	int childcount_TNI_Added = 0;

	Button btn_Save;
	HashMap<String, String> hm_Site_Conditions;

	HashMap<String, String> xmlData;
	ProgressBar progressSiteConditions;

	String reportDate;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		System.gc();

		progressSiteConditions = (ProgressBar) findViewById(R.id.progress_siteConditions);
		progressSiteConditions.setVisibility(View.VISIBLE);

		// ll_SSC_Default_Values = (LinearLayout)
		// findViewById(R.id.ll_ssc_default_values);
		// if (((LinearLayout) ll_SSC_Default_Values).getChildCount() > 0)
		// ((LinearLayout) ll_SSC_Default_Values).removeAllViews();
		// layout_Site_Storage_Condition_function();

		ll_site_storage_conditions = (LinearLayout) findViewById(R.id.ll_site_storage_conditions);
		if (((LinearLayout) ll_site_storage_conditions).getChildCount() > 0)
			((LinearLayout) ll_site_storage_conditions).removeAllViews();

		// ll_SPAS_Default_Values = (LinearLayout)
		// findViewById(R.id.ll_spas_default_values);
		// if (((LinearLayout) ll_SPAS_Default_Values).getChildCount() > 0)
		// ((LinearLayout) ll_SPAS_Default_Values).removeAllViews();
		// layout_Spare_Parts_At_Site_function();

		ll_spare_parts_at_site = (LinearLayout) findViewById(R.id.ll_spare_parts_at_site);
		if (((LinearLayout) ll_spare_parts_at_site).getChildCount() > 0)
			((LinearLayout) ll_spare_parts_at_site).removeAllViews();

		// ll_TNI_Default_Values = (LinearLayout)
		// findViewById(R.id.ll_tni_default_values);
		// if (((LinearLayout) ll_TNI_Default_Values).getChildCount() > 0)
		// ((LinearLayout) ll_TNI_Default_Values).removeAllViews();
		// // layout_Tools_And_Instruments_function();

		ll_tools_instruments = (LinearLayout) findViewById(R.id.ll_tools_instruments);
		if (((LinearLayout) ll_tools_instruments).getChildCount() > 0)
			((LinearLayout) ll_tools_instruments).removeAllViews();

		new populateValuesTask().execute();

		mCursorMateriallogs = managedQuery(
				DataProvider.Material_Log.CONTENT_URI,
				null,
				DataProvider.Material_Log.REPORT_ID + " ='"
						+ Util.getReportID(Site_Conditions.this) + "'", null,
				null);

		mCursorMateriallogs.moveToFirst();
		startManagingCursor(mCursorMateriallogs);
		// material_logs_adapter = new
		// Material_Log_Adapter(Site_Conditions.this,
		// mCursorMateriallogs, true);
		// material_listview.setAdapter(material_logs_adapter);

		System.gc();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_conditions);
		context = Site_Conditions.this;

		listArray = new ArrayList<DataModel_Site_Conditions>();

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);
		// font implementation of page heading: textview
		page_Title = (TextView) findViewById(R.id.pageTitle);
		page_Title.setTypeface(GMDApplication.fontText);

		// Setting the headings of the page:
		page_Title.setText("Site Conditions");

		btn_add_site_storage = (Button) findViewById(R.id.btn_add_site_storage);
		btn_add_spare_part = (Button) findViewById(R.id.btn_add_spare_part);
		btn_add_tools = (Button) findViewById(R.id.btn_add_tools);

		et_Store_SSC1 = (EditText) findViewById(R.id.store_cleanliness_edittext);
		checked_Spinner_SSC1 = (Spinner) findViewById(R.id.checked_spinner);
		et_Date_SSC1 = (EditText) findViewById(R.id.date_edittext);
		et_Remarks_SSC1 = (EditText) findViewById(R.id.remarks_edittext);

		et_Store_SSC1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Date_SSC1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Remarks_SSC1.setImeOptions(EditorInfo.IME_ACTION_DONE);

		et_Store_SSC1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Date_SSC1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Remarks_SSC1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Setting spinner data:
		checked_Spinner_Adapter_default = ArrayAdapter.createFromResource(
				Site_Conditions.this, R.array.site_storage_conditions_checked,
				R.layout.custom_spinner_item);

		checked_Spinner_Adapter_default
				.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		checked_Spinner_SSC1.setAdapter(checked_Spinner_Adapter_default);
		checked_Spinner_SSC1.setSelection(0);
		checked_Spinner_SSC1
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		et_Material_SPAS1 = (EditText) findViewById(R.id.material_edittext_SPAS);
		et_Quantity_SPAS1 = (EditText) findViewById(R.id.quantity_edittext_SPAS);
		et_Date_SPAS1 = (EditText) findViewById(R.id.date_edittext_SPAS);
		et_Remarks_SPAS1 = (EditText) findViewById(R.id.remarks_edittext_SPAS);

		et_Material_SPAS1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Quantity_SPAS1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Date_SPAS1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Remarks_SPAS1.setImeOptions(EditorInfo.IME_ACTION_DONE);

		et_Material_SPAS1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Quantity_SPAS1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Date_SPAS1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Remarks_SPAS1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		et_Tools_TNI1 = (EditText) findViewById(R.id.tools_edittext_TNI);
		et_Quantity_TNI1 = (EditText) findViewById(R.id.quantity_edittext_TNI);
		et_Date_TNI1 = (EditText) findViewById(R.id.date_edittext_TNI);
		et_Remarks_TNI1 = (EditText) findViewById(R.id.remarks_edittext_TNI);

		et_Tools_TNI1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Quantity_TNI1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Date_TNI1.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_Remarks_TNI1.setImeOptions(EditorInfo.IME_ACTION_DONE);

		et_Tools_TNI1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Quantity_TNI1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Date_TNI1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_Remarks_TNI1.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		// add_New_Material_Button = (Button) findViewById(R.id.addNew_Button);
		// material_listview = (ListView)
		// findViewById(R.id.siteCondition_list_view);
		// site_conditions_LL = (LinearLayout)
		// findViewById(R.id.ll_site_conditions);
		// add_New_Material_Log_LL = (LinearLayout)
		// findViewById(R.id.ll_add_new_material);
		// cancel_Button = (Button) findViewById(R.id.cancel_button);
		// save_Button = (Button) findViewById(R.id.save_button);
		// material_name_Edittext = (EditText)
		// findViewById(R.id.material_name_text);
		// material_Category_Spinner = (Spinner)
		// findViewById(R.id.material_category_spinner);
		// information_Edittext = (EditText)
		// findViewById(R.id.information_text);

		// // Apply font to material name edit text
		// material_name_Edittext.setHintTextColor(getResources().getColor(
		// R.color.hint_text_color));
		// // Apply font to information edit text
		// information_Edittext.setHintTextColor(getResources().getColor(
		// R.color.hint_text_color));
		//
		// // Remove the default listview divider:
		// material_listview.setDivider(null);

		// // Setting spinner Data:
		// material_Spinner_Adapter = ArrayAdapter.createFromResource(this,
		// R.array.material_array, R.layout.custom_spinner_item);
		// material_Spinner_Adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// material_Category_Spinner.setAdapter(material_Spinner_Adapter);
		// material_Category_Spinner
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// // Font implementation:
		// existing_Material_Logs = (TextView)
		// findViewById(R.id.existing_material_log_textview);
		// existing_Material_Logs.setTypeface(GMDApplication.fontHeading);
		//
		// new_material_heading_TextView = (TextView)
		// findViewById(R.id.new_material_heading);
		// new_material_heading_TextView.setTypeface(GMDApplication.fontText);

		// NEW CODE::
		tv_ssc_Heading = (TextView) findViewById(R.id.site_storage_conditions_text);
		tv_ssc_Heading.setTypeface(GMDApplication.fontHeading);
		tv_spas_Heading = (TextView) findViewById(R.id.spare_parts_at_site_text);
		tv_spas_Heading.setTypeface(GMDApplication.fontHeading);
		tv_tni_Heading = (TextView) findViewById(R.id.tools_instruments_text);
		tv_tni_Heading.setTypeface(GMDApplication.fontHeading);

		btn_Save = (Button) findViewById(R.id.Save_Button);

		// // Add new material button:
		// add_New_Material_Button.setOnClickListener(new View.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // Intent myIntent = new Intent(getBaseContext(),
		// // Log_New_Material.class);
		// // startActivity(myIntent);
		// site_conditions_LL.setVisibility(View.INVISIBLE);
		// add_New_Material_Log_LL.setVisibility(View.VISIBLE);
		// page_Title.setText("Log a Problem");
		//
		// }
		// });

		// material_listview.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View view, int pos,
		// long id) {
		// // TODO Auto-generated method stub
		// site_conditions_LL.setVisibility(View.INVISIBLE);
		// add_New_Material_Log_LL.setVisibility(View.VISIBLE);
		// page_Title.setText("Log a Problem");
		//
		// TextView txt_materialID = (TextView) view
		// .findViewById(R.id.textview_material_rowID);
		// szMaterialID = txt_materialID.getText().toString();
		//
		// TextView txt_dateTime = (TextView) view
		// .findViewById(R.id.date_time);
		// String szDateTime = txt_dateTime.getText().toString();
		//
		// Cursor materialCursor = getContentResolver()
		// .query(DataProvider.Material_Log.CONTENT_URI,
		// null,
		// DataProvider.Material_Log.ID
		// + " = ?"
		// + " AND "
		// + DataProvider.Material_Log.MATERIAL_REPORT_DATE
		// + " = ?",
		// new String[] { szMaterialID, szDateTime }, null);
		// if (materialCursor != null && materialCursor.moveToFirst()) {
		// material_name_Edittext.setText(materialCursor.getString(materialCursor
		// .getColumnIndex(DataProvider.Material_Log.MATERIAL_NAME)));
		//
		// for (int i = 0; i < material_Spinner_Adapter.getCount(); i++) {
		// if (materialCursor
		// .getString(
		// materialCursor
		// .getColumnIndex(DataProvider.Material_Log.MATERIAL_CATEGORY))
		// .equals(material_Spinner_Adapter.getItem(i)
		// .toString())) {
		// material_Category_Spinner.setSelection(i);
		// break;
		// }
		// }
		//
		// information_Edittext.setText(materialCursor.getString(materialCursor
		// .getColumnIndex(DataProvider.Material_Log.MATERIAL_INFO)));
		//
		// bIsUpdate = true;
		// }
		//
		// }
		// });

		// // Cancel Button
		// cancel_Button.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// site_conditions_LL.setVisibility(View.VISIBLE);
		// add_New_Material_Log_LL.setVisibility(View.INVISIBLE);
		// page_Title.setText("Site Conditions");
		//
		// material_name_Edittext.setText("");
		// information_Edittext.setText("");
		// material_Category_Spinner.setSelection(0);
		// bIsUpdate = false;
		// szMaterialID = "";
		// }
		// });

		// // Save Button
		// save_Button.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// // setting data and time:
		// try {
		// Date today = Calendar.getInstance().getTime();
		// reportDate = dateFormat.format(today);
		// // dateAndTime.setText(reportDate);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		// // Validation Part:
		//
		// int pos = material_Category_Spinner.getSelectedItemPosition();
		// Log.v(TAG, "" + pos);
		//
		// String material_Name_Data = material_name_Edittext.getText()
		// .toString().trim();
		// String information_Data = information_Edittext.getText()
		// .toString().trim();
		// // Validation for Material Name EditText empty:
		// if (material_Name_Data.length() == 0) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Site_Conditions.this)
		// .setTitle("Alert!")
		// .setMessage("All fields are mandatory")
		// .setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface argDialog,
		// int argWhich) {
		//
		// }
		// }).create();
		// m_AlertDialog.setCanceledOnTouchOutside(false);
		// m_AlertDialog.show();
		//
		// } // Validation For Material Category Spinner :item not
		// // selected:
		// else if (pos == 0) {
		// // int pos
		// // =material_Category_Spinner.getSelectedItemPosition();
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Site_Conditions.this)
		// .setTitle("Alert!")
		// .setMessage("Please select a material category")
		// .setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface argDialog,
		// int argWhich) {
		//
		// }
		// }).create();
		// m_AlertDialog.setCanceledOnTouchOutside(false);
		// m_AlertDialog.show();
		// } else if (information_Data.length() == 0) {
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Site_Conditions.this)
		// .setTitle("Alert!")
		// .setMessage("All fields are mandatory")
		// .setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface argDialog,
		// int argWhich) {
		//
		// }
		// }).create();
		// m_AlertDialog.setCanceledOnTouchOutside(false);
		// m_AlertDialog.show();
		// } else {
		// site_conditions_LL.setVisibility(View.VISIBLE);
		// add_New_Material_Log_LL.setVisibility(View.INVISIBLE);
		//
		// Material_Name = material_name_Edittext.getText().toString();
		// Material_Category = material_Category_Spinner
		// .getSelectedItem().toString();
		// Information = information_Edittext.getText().toString();
		//
		// reportDate = dateFormat.format(Calendar.getInstance()
		// .getTime());
		//
		// xmlData_Save.put(XML_Values.MATERIAL_NAME, Material_Name);
		// xmlData_Save.put(XML_Values.MATERIAL_CATEGORY,
		// Material_Category);
		// xmlData_Save.put(XML_Values.INFORMATION, Information);
		// xmlData_Save.put(XML_Values.MATERIAL_DATE, reportDate);
		//
		// // Saving data in xml:
		// XML_Creation xml_Creation = new XML_Creation();
		// xml_Creation.save_Site_Conditions(xmlData_Save);
		// Log.v(TAG, "DONE WITH SAVING DATA AT SAME LOCATION");
		//
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Site_Conditions.this)
		// .setMessage("Data saved successfully!")
		// .setPositiveButton("Ok",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface argDialog,
		// int argWhich) {
		//
		// }
		// }).create();
		// m_AlertDialog.setCanceledOnTouchOutside(false);
		// m_AlertDialog.show();
		//
		// ContentValues material_log_values = new ContentValues();
		// material_log_values.put(
		// DataProvider.Material_Log.REPORT_ID,
		// Util.getReportID(Site_Conditions.this));
		// material_log_values.put(
		// DataProvider.Material_Log.MATERIAL_NAME,
		// Material_Name);
		// material_log_values.put(
		// DataProvider.Material_Log.MATERIAL_REPORT_DATE,
		// reportDate.toLowerCase());
		// material_log_values.put(
		// DataProvider.Material_Log.MATERIAL_CATEGORY,
		// Material_Category);
		// material_log_values.put(
		// DataProvider.Material_Log.MATERIAL_INFO,
		// Information);
		//
		// if (bIsUpdate && !szMaterialID.equals("")) {
		// getContentResolver().update(
		// DataProvider.Material_Log.CONTENT_URI,
		// material_log_values,
		// DataProvider.Material_Log.ID + " = ?",
		// new String[] { szMaterialID });
		//
		// } else {
		// getContentResolver().insert(
		// DataProvider.Material_Log.CONTENT_URI,
		// material_log_values);
		// }
		//
		// mCursorMateriallogs.requery();
		// // Cursor materiallogsCursor = managedQuery(
		// // DataProvider.Material_Log.CONTENT_URI, null, null,
		// // null, null);
		// // startManagingCursor(materiallogsCursor);
		// // material_logs_adapter = new Material_Log_Adapter(
		// // Site_Conditions.this, materiallogsCursor, true);
		// // material_listview.setAdapter(material_logs_adapter);
		//
		// // Clear the fields:
		// material_name_Edittext.setText("");
		// material_Category_Spinner.setSelection(0);
		// information_Edittext.setText("");
		// // Changing the page title back to Site Conditions:
		// page_Title.setText("Site Conditions");
		// bIsUpdate = false;
		// szMaterialID = "";
		// }
		// }
		// });

		// Button->Add site storage conditions:
		btn_add_site_storage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(getBaseContext());
				View SSC_view = inflater.inflate(
						R.layout.site_storage_conditions_simplerow, null);

				et_store_SSC_Added = (EditText) SSC_view
						.findViewById(R.id.store_cleanliness_edittext);
				et_store_SSC_Added.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				spinner_checked_SSC = (Spinner) SSC_view
						.findViewById(R.id.checked_spinner);
				et_date_SSC_Added = (EditText) SSC_view
						.findViewById(R.id.date_edittext);
				et_date_SSC_Added.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_remarks_SSC_Added = (EditText) SSC_view
						.findViewById(R.id.remarks_edittext);
				et_remarks_SSC_Added.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				et_store_SSC_Added.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_date_SSC_Added.setImeOptions(EditorInfo.IME_ACTION_DONE);
				et_remarks_SSC_Added.setImeOptions(EditorInfo.IME_ACTION_DONE);

				// Setting spinner Data:for checked
				checked_Spinner_Adapter = ArrayAdapter.createFromResource(
						SSC_view.getRootView().getContext(),
						R.array.site_storage_conditions_checked,
						R.layout.custom_spinner_item);

				checked_Spinner_Adapter
						.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
				spinner_checked_SSC.setAdapter(checked_Spinner_Adapter);
				spinner_checked_SSC.setSelection(0);
				spinner_checked_SSC
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

				ll_site_storage_conditions.removeView(SSC_view);
				ll_site_storage_conditions.addView(SSC_view);
			}
		});
		// Button->Add spare parts and tools:
		btn_add_spare_part.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View SPAS_view = inflater.inflate(
						R.layout.spare_parts_at_site_simplerow, null);

				EditText material_edittext_SPAS = (EditText) SPAS_view
						.findViewById(R.id.material_edittext_SPAS);
				material_edittext_SPAS.setHintTextColor(getResources()
						.getColor(R.color.hint_text_color));
				EditText quantity_edittext_SPAS = (EditText) SPAS_view
						.findViewById(R.id.quantity_edittext_SPAS);
				quantity_edittext_SPAS.setHintTextColor(getResources()
						.getColor(R.color.hint_text_color));
				EditText date_edittext_SPAS = (EditText) SPAS_view
						.findViewById(R.id.date_edittext_SPAS);
				date_edittext_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				EditText remarks_edittext_SPAS = (EditText) SPAS_view
						.findViewById(R.id.remarks_edittext_SPAS);
				remarks_edittext_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				material_edittext_SPAS
						.setImeOptions(EditorInfo.IME_ACTION_DONE);
				quantity_edittext_SPAS
						.setImeOptions(EditorInfo.IME_ACTION_DONE);
				date_edittext_SPAS.setImeOptions(EditorInfo.IME_ACTION_DONE);
				remarks_edittext_SPAS.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_spare_parts_at_site.removeView(SPAS_view);
				ll_spare_parts_at_site.addView(SPAS_view);
			}
		});
		// Button->Add tools and instruments:
		btn_add_tools.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View TNI_view = inflater.inflate(
						R.layout.tools_instruments_simplerow, null);

				EditText tools_edittext_TNI = (EditText) TNI_view
						.findViewById(R.id.tools_edittext_TNI);
				tools_edittext_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText quantity_edittext_TNI = (EditText) TNI_view
						.findViewById(R.id.quantity_edittext_TNI);
				quantity_edittext_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText date_edittext_TNI = (EditText) TNI_view
						.findViewById(R.id.date_edittext_TNI);
				date_edittext_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				EditText remarks_edittext_TNI = (EditText) TNI_view
						.findViewById(R.id.remarks_edittext_TNI);
				remarks_edittext_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				tools_edittext_TNI.setImeOptions(EditorInfo.IME_ACTION_DONE);
				quantity_edittext_TNI.setImeOptions(EditorInfo.IME_ACTION_DONE);
				date_edittext_TNI.setImeOptions(EditorInfo.IME_ACTION_DONE);
				remarks_edittext_TNI.setImeOptions(EditorInfo.IME_ACTION_DONE);

				ll_tools_instruments.removeView(TNI_view);
				ll_tools_instruments.addView(TNI_view);
			}
		});

		// Save Button::
		btn_Save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// saveSiteConditionsData(true);
				progressSiteConditions.setVisibility(View.VISIBLE);
				saveDatatask task = new saveDatatask();
				task.flag = true;
				task.execute();
			}
		});
	}

	void saveSiteConditionsData(boolean bIsShowDialog) {

		// Getting Data in HashMap to send to XML_Serializer Class:
		hm_Site_Conditions = new HashMap<String, String>();
		hm_Site_Conditions.clear();

		// Site Storage Conditions: Saving data in xml
		// get_Site_Storage_Conditions_From_Default();
		if (checked_Spinner_SSC1.getSelectedItemPosition() == 0) {
			// AlertDialog m_AlertDialog = new AlertDialog.Builder(
			// Site_Conditions.this)
			// .setMessage("Select a value for checked")
			// .setPositiveButton("Ok",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface argDialog,
			// int argWhich) {
			//
			// }
			// }).create();
			// m_AlertDialog.show();
		} else {

			hm_Site_Conditions.put((XML_Values.STORE + "0"), et_Store_SSC1
					.getText().toString());
			String spinner_Value = checked_Spinner_SSC1.getSelectedItem()
					.toString();
			hm_Site_Conditions.put((XML_Values.CHECKED + "0"), spinner_Value);
			hm_Site_Conditions.put(
					(XML_Values.DATE_SITE_STORAGE_CONDITIONS + "0"),
					et_Date_SSC1.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + "0"),
					et_Remarks_SSC1.getText().toString());

			Log.v("==========ID========   store====checked==========sdate=======remarks==========  ",
					(XML_Values.STORE + "0")
							+ (XML_Values.CHECKED + "0")
							+ (XML_Values.DATE_SITE_STORAGE_CONDITIONS + "0")
							+ (XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + "0")
							+ "     &   " + et_Store_SSC1.getText().toString()
							+ "  "
							+ checked_Spinner_SSC1.getSelectedItem().toString()
							+ "  " + et_Date_SSC1.getText().toString() + "  "
							+ et_Remarks_SSC1.getText().toString());

			get_Site_Storage_Conditions_From_Added();
			// Spare Parts At Site: Saving data in xml
			// get_Spare_Part_At_Site_From_Default();

			hm_Site_Conditions.put((XML_Values.SPARE_PART_MATERIAL + "0"),
					et_Material_SPAS1.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.QUANTITY_SPARE_PART_AT_SITE + "0"),
					et_Quantity_SPAS1.getText().toString());
			hm_Site_Conditions.put((XML_Values.DATE_SPARE_PART_AT_SITE + "0"),
					et_Date_SPAS1.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.REMARKS_SPARE_PART_AT_SITE + "0"),
					et_Remarks_SPAS1.getText().toString());

			Log.v("============ material==== quantity===============date==========remark========== ",
					(XML_Values.SPARE_PART_MATERIAL + "0")
							+ (XML_Values.QUANTITY_SPARE_PART_AT_SITE + "0")
							+ (XML_Values.DATE_SPARE_PART_AT_SITE + "0")
							+ (XML_Values.REMARKS_SPARE_PART_AT_SITE + "0")
							+ "     &   "
							+ et_Material_SPAS1.getText().toString() + "  "
							+ et_Quantity_SPAS1.getText().toString() + "  "
							+ et_Date_SPAS1.getText().toString() + "  "
							+ et_Remarks_SPAS1.getText().toString());

			get_Spare_Part_At_Site_From_Added();

			// Tools and Instruments:Saing in xml
			// get_Tools_Instruments_From_Default();

			hm_Site_Conditions.put((XML_Values.TOOLS + "0"), et_Tools_TNI1
					.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + "0"),
					et_Quantity_TNI1.getText().toString());
			hm_Site_Conditions.put((XML_Values.DATE_TOOLS_INSTRUMENT + "0"),
					et_Date_TNI1.getText().toString());
			hm_Site_Conditions.put((XML_Values.REMARKS_TOOLS_INSTRUMENT + "0"),
					et_Remarks_TNI1.getText().toString());

			Log.v("============ tool==== quantity===============date==========remark========== ",
					(XML_Values.TOOLS + "0")
							+ (XML_Values.QUANTITY_TOOLS_INSTRUMENTS + "0")
							+ (XML_Values.DATE_TOOLS_INSTRUMENT + "0")
							+ (XML_Values.REMARKS_TOOLS_INSTRUMENT + "0")
							+ "     &   " + et_Tools_TNI1.getText().toString()
							+ et_Quantity_TNI1.getText().toString()
							+ et_Date_TNI1.getText().toString()
							+ et_Remarks_TNI1.getText().toString());

			get_Tools_Instruments_From_Added();

			// Writing in xml
			XML_Creation save_Site_Condition = new XML_Creation();
			save_Site_Condition.save_Site_Conditions_New(hm_Site_Conditions);

			// if (bIsShowDialog) {
			// AlertDialog m_AlertDialog = new AlertDialog.Builder(
			// Site_Conditions.this)
			// .setTitle("Success!")
			// .setMessage("Data saved successfully!")
			// .setPositiveButton("Ok",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(
			// DialogInterface argDialog,
			// int argWhich) {
			//
			// }
			// }).create();
			// m_AlertDialog.setCanceledOnTouchOutside(false);
			// m_AlertDialog.show();
			// }
			// try {
			// if (progressSiteConditions != null)
			// progressSiteConditions.setVisibility(View.GONE);
			// } catch (Exception e) {
			// // TODO: handle exception
			// e.printStackTrace();
			// }
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				Site_Conditions.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						finish();

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}

	// Adding default SSC:: in layout
	// private void layout_Site_Storage_Condition_function() {
	// // TODO Auto-generated method stub
	// int rows = 2;
	// int count = rows;
	// if (count >= 1) {
	// for (int i = 1; i <= count; i++) {
	//
	// LayoutInflater inflater = LayoutInflater.from(getBaseContext());
	// view_SSC = inflater.inflate(
	// R.layout.site_storage_conditions_simplerow, null);
	// et_Store_SSC = (EditText) view_SSC
	// .findViewById(R.id.store_cleanliness_edittext);
	// et_Store_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// checked_Spinner_SSC = (Spinner) view_SSC
	// .findViewById(R.id.checked_spinner);
	// et_Date_SSC = (EditText) view_SSC
	// .findViewById(R.id.date_edittext);
	// et_Date_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_SSC = (EditText) view_SSC
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// // Setting spinner Data:for checked
	// checked_Spinner_Adapter = ArrayAdapter.createFromResource(
	// view_SSC.getRootView().getContext(),
	// R.array.site_storage_conditions_checked,
	// R.layout.custom_spinner_item);
	//
	// checked_Spinner_Adapter
	// .setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
	// checked_Spinner_SSC.setAdapter(checked_Spinner_Adapter);
	// checked_Spinner_SSC.setSelection(1);
	// checked_Spinner_SSC
	// .setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> arg0,
	// View arg1, int arg2, long arg3) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// ll_SSC_Default_Values.addView(view_SSC);
	//
	// // Setting data in EditText :store_Text
	// if (i == 1) {
	// et_Store_SSC.setText("Cooling container for spares");
	// } else if (i == 2) {
	// et_Store_SSC.setText("Work shop for specific tools");
	// }
	//
	// if (i == rows) {
	// ll_SSC_Default_Values.removeView(view_SSC);
	// ll_SSC_Default_Values.addView(view_SSC);
	// }
	// }
	// }
	// }

	private void layout_Site_Storage_Condition_function_fromXML(int rows) {
		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater.from(getBaseContext());
				view_SSC = inflater.inflate(
						R.layout.site_storage_conditions_simplerow, null);
				et_Store_SSC = (EditText) view_SSC
						.findViewById(R.id.store_cleanliness_edittext);
				et_Store_SSC.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				checked_Spinner_SSC = (Spinner) view_SSC
						.findViewById(R.id.checked_spinner);
				et_Date_SSC = (EditText) view_SSC
						.findViewById(R.id.date_edittext);
				et_Date_SSC.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Remarks_SSC = (EditText) view_SSC
						.findViewById(R.id.remarks_edittext);
				et_Remarks_SSC.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				// Setting spinner Data:for checked
				checked_Spinner_Adapter = ArrayAdapter.createFromResource(
						view_SSC.getRootView().getContext(),
						R.array.site_storage_conditions_checked,
						R.layout.custom_spinner_item);

				checked_Spinner_Adapter
						.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
				checked_Spinner_SSC.setAdapter(checked_Spinner_Adapter);
				checked_Spinner_SSC.setSelection(0);
				checked_Spinner_SSC
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				// ll_SSC_Default_Values.addView(view_SSC);
				ll_site_storage_conditions.addView(view_SSC);

				// Setting data in EditText :store_Text
				// STORE
				// CHECKED
				// DATE_SITE_STORAGE_CONDITIONS
				// REMARKS_SITE_STORAGE_CONDITIONS

				// Setting data in EditText :Tools_Text
				et_Store_SSC.setText(DecodeXML(xmlData
						.get(XML_Values.STORE + i)));

				String szChecked = xmlData.get(XML_Values.CHECKED + i);
				if (szChecked != null) {
					for (int j = 0; j < checked_Spinner_Adapter.getCount(); j++) {
						if (szChecked.equals(checked_Spinner_Adapter.getItem(j)
								.toString())) {
							checked_Spinner_SSC.setSelection(j);
							break;
						}
					}
				}

				et_Date_SSC.setText(DecodeXML(xmlData
						.get(XML_Values.DATE_SITE_STORAGE_CONDITIONS + i)));
				et_Remarks_SSC.setText(DecodeXML(xmlData
						.get(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + i)));

				if (i == rows) {
					// ll_SSC_Default_Values.removeView(view_SSC);
					// ll_SSC_Default_Values.addView(view_SSC);
					ll_site_storage_conditions.removeView(view_SSC);
					ll_site_storage_conditions.addView(view_SSC);
				}
			}
		}
	}

	// Adding default SPAS::in layout
	// private void layout_Spare_Parts_At_Site_function() {
	// // TODO Auto-generated method stub
	// int rows = 18;
	// int count = rows;
	// if (count >= 1) {
	// for (int i = 1; i <= count; i++) {
	//
	// LayoutInflater inflater = LayoutInflater
	// .from(getApplicationContext());
	// view_SPAS = inflater.inflate(
	// R.layout.spare_parts_at_site_simplerow, null);
	//
	// et_Material_SPAS = (EditText) view_SPAS
	// .findViewById(R.id.material_edittext_SPAS);
	// et_Material_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Quantity_SPAS = (EditText) view_SPAS
	// .findViewById(R.id.quantity_edittext);
	// et_Quantity_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Date_SPAS = (EditText) view_SPAS
	// .findViewById(R.id.date_edittext);
	// et_Date_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_SPAS = (EditText) view_SPAS
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// ll_SPAS_Default_Values.addView(view_SPAS);
	//
	// // Setting data in EditText :material_Text
	// if (i == 1) {
	// et_Material_SPAS.setText("Counter wedges");
	// } else if (i == 2) {
	// et_Material_SPAS.setText("End wedges Type A");
	// } else if (i == 3) {
	// et_Material_SPAS.setText("End wedges Type B");
	// } else if (i == 4) {
	// et_Material_SPAS.setText("Middle wedges");
	// } else if (i == 5) {
	// et_Material_SPAS.setText("Slot filler 1mm");
	// } else if (i == 6) {
	// et_Material_SPAS.setText("Slot filler 0.5mm");
	// } else if (i == 7) {
	// et_Material_SPAS.setText("Slot filler 0.76mm");
	// } else if (i == 8) {
	// et_Material_SPAS.setText("Distance between bars");
	// } else if (i == 9) {
	// et_Material_SPAS.setText("Fiber glass cord Dia. 40mm");
	// } else if (i == 10) {
	// et_Material_SPAS.setText("Fiber glass cord Dia. 35mm");
	// } else if (i == 11) {
	// et_Material_SPAS.setText("Fiber band 60*3mm");
	// } else if (i == 12) {
	// et_Material_SPAS.setText("Copper H winding connector");
	// } else if (i == 13) {
	// et_Material_SPAS.setText("Shims for H winding connector");
	// } else if (i == 14) {
	// et_Material_SPAS.setText("Silver brazing roads");
	// } else if (i == 15) {
	// et_Material_SPAS.setText("Nomex 0,13m thick");
	// } else if (i == 16) {
	// et_Material_SPAS.setText("Fiber wool");
	// } else if (i == 17) {
	// et_Material_SPAS.setText("Heat exchanger");
	// } else if (i == 18) {
	// et_Material_SPAS
	// .setText("Electronic cards for CCV& other equit");
	// }
	// if (i == rows) {
	// ll_SPAS_Default_Values.removeView(view_SPAS);
	// ll_SPAS_Default_Values.addView(view_SPAS);
	// }
	// }
	// }
	// }

	private void layout_Spare_Parts_At_Site_function_fromXML(int rows) {
		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_SPAS = inflater.inflate(
						R.layout.spare_parts_at_site_simplerow, null);

				et_Material_SPAS = (EditText) view_SPAS
						.findViewById(R.id.material_edittext_SPAS);
				et_Material_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Quantity_SPAS = (EditText) view_SPAS
						.findViewById(R.id.quantity_edittext_SPAS);
				et_Quantity_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Date_SPAS = (EditText) view_SPAS
						.findViewById(R.id.date_edittext_SPAS);
				et_Date_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Remarks_SPAS = (EditText) view_SPAS
						.findViewById(R.id.remarks_edittext_SPAS);
				et_Remarks_SPAS.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				// ll_SPAS_Default_Values.addView(view_SPAS);
				ll_spare_parts_at_site.addView(view_SPAS);

				// SPARE_PART_MATERIAL
				// QUANTITY_SPARE_PART_AT_SITE
				// DATE_SPARE_PART_AT_SITE
				// spare_part_remarks

				// Setting data in EditText :Tools_Text
				et_Material_SPAS.setText(DecodeXML(xmlData
						.get(XML_Values.SPARE_PART_MATERIAL + i)));
				et_Quantity_SPAS.setText(DecodeXML(xmlData
						.get(XML_Values.QUANTITY_SPARE_PART_AT_SITE + i)));
				et_Date_SPAS.setText(DecodeXML(xmlData
						.get(XML_Values.DATE_SPARE_PART_AT_SITE + i)));
				et_Remarks_SPAS.setText(DecodeXML(xmlData
						.get(XML_Values.REMARKS_SPARE_PART_AT_SITE + i)));

				// Setting data in EditText :material_Text
				if (i == rows) {
					// ll_SPAS_Default_Values.removeView(view_SPAS);
					// ll_SPAS_Default_Values.addView(view_SPAS);

					ll_spare_parts_at_site.removeView(view_SPAS);
					ll_spare_parts_at_site.addView(view_SPAS);
				}
			}
		}
	}

	// Adding default TNI::in layout
	// private void layout_Tools_And_Instruments_function() {
	// // TODO Auto-generated method stub
	// int rows = 12;
	// int count = rows;
	// if (count >= 1) {
	// for (int i = 1; i <= count; i++) {
	//
	// LayoutInflater inflater = LayoutInflater
	// .from(getApplicationContext());
	// view_TNI = inflater.inflate(
	// R.layout.tools_instruments_simplerow, null);
	//
	// et_Tools_TNI = (EditText) view_TNI
	// .findViewById(R.id.tools_edittext_TNI);
	// et_Tools_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Quantity_TNI = (EditText) view_TNI
	// .findViewById(R.id.quantity_edittext);
	// et_Quantity_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Date_TNI = (EditText) view_TNI
	// .findViewById(R.id.date_edittext);
	// et_Date_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_TNI = (EditText) view_TNI
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// ll_TNI_Default_Values.addView(view_TNI);
	//
	// // Setting data in EditText :Tools_Text
	// if (i == 1) {
	// et_Tools_TNI.setText("Winding wedges blocs");
	// } else if (i == 2) {
	// et_Tools_TNI.setText("Debouncing big plastic hammer");
	// } else if (i == 3) {
	// et_Tools_TNI.setText("Debouncing middle plastic hammer");
	// } else if (i == 4) {
	// et_Tools_TNI.setText("Copper roll");
	// } else if (i == 5) {
	// et_Tools_TNI.setText("Middle knife palette");
	// } else if (i == 6) {
	// et_Tools_TNI.setText("Resin injection niddle");
	// } else if (i == 7) {
	// et_Tools_TNI.setText("Copper block");
	// } else if (i == 8) {
	// et_Tools_TNI.setText("10T chain blocks");
	// } else if (i == 9) {
	// et_Tools_TNI
	// .setText("Hydraulic stator lifting jack complete set");
	// } else if (i == 10) {
	// et_Tools_TNI
	// .setText("Hytorc equipment for foundation bolts");
	// } else if (i == 11) {
	// et_Tools_TNI.setText("Dollies stator shifting");
	// } else if (i == 12) {
	// et_Tools_TNI.setText("Drivers type of hydraulic jacks");
	// }
	//
	// if (i == rows) {
	// ll_TNI_Default_Values.removeView(view_TNI);
	// ll_TNI_Default_Values.addView(view_TNI);
	// }
	// }
	// }
	// }

	private void layout_Tools_And_Instruments_function_fromXML(int rows) {
		// TODO Auto-generated method stub
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_TNI = inflater.inflate(
						R.layout.tools_instruments_simplerow, null);

				et_Tools_TNI = (EditText) view_TNI
						.findViewById(R.id.tools_edittext_TNI);
				et_Tools_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Quantity_TNI = (EditText) view_TNI
						.findViewById(R.id.quantity_edittext_TNI);
				et_Quantity_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Date_TNI = (EditText) view_TNI
						.findViewById(R.id.date_edittext_TNI);
				et_Date_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				et_Remarks_TNI = (EditText) view_TNI
						.findViewById(R.id.remarks_edittext_TNI);
				et_Remarks_TNI.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				// ll_TNI_Default_Values.addView(view_TNI);
				ll_tools_instruments.addView(view_TNI);

				// TOOLS
				// QUANTITY_TOOLS_INSTRUMENTS
				// DATE_TOOLS_INSTRUMENT
				// REMARKS_TOOLS_INSTRUMENT

				// Setting data in EditText :Tools_Text
				et_Tools_TNI.setText(DecodeXML(xmlData
						.get(XML_Values.TOOLS + i)));
				et_Quantity_TNI.setText(DecodeXML(xmlData
						.get(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + i)));
				et_Date_TNI.setText(DecodeXML(xmlData
						.get(XML_Values.DATE_TOOLS_INSTRUMENT + i)));
				et_Remarks_TNI.setText(DecodeXML(xmlData
						.get(XML_Values.REMARKS_TOOLS_INSTRUMENT + i)));

				if (i == rows) {
					// ll_TNI_Default_Values.removeView(view_TNI);
					// ll_TNI_Default_Values.addView(view_TNI);
					ll_tools_instruments.removeView(view_TNI);
					ll_tools_instruments.addView(view_TNI);
				}
			}
		}
	}

	// Adding default SSC:: in hashmap
	// private void get_Site_Storage_Conditions_From_Default() {
	// childcount_SSC_Default = ll_SSC_Default_Values.getChildCount();
	// for (int i = 0; i < childcount_SSC_Default; i++) {
	//
	// View SSC_view = ll_SSC_Default_Values.getChildAt(i);
	//
	// et_Store_SSC = (EditText) SSC_view
	// .findViewById(R.id.store_cleanliness_edittext);
	// et_Store_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// checked_Spinner_SSC = (Spinner) SSC_view
	// .findViewById(R.id.checked_spinner);
	// et_Date_SSC = (EditText) SSC_view.findViewById(R.id.date_edittext);
	// et_Date_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_SSC = (EditText) SSC_view
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_SSC.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// hm_Site_Conditions.put((XML_Values.STORE + i), et_Store_SSC
	// .getText().toString());
	// String checked_Spinner_Value = checked_Spinner_SSC
	// .getSelectedItem().toString();
	// hm_Site_Conditions.put((XML_Values.CHECKED + i),
	// checked_Spinner_Value);
	// hm_Site_Conditions.put(
	// (XML_Values.DATE_SITE_STORAGE_CONDITIONS + i), et_Date_SSC
	// .getText().toString());
	// hm_Site_Conditions.put(
	// (XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + i),
	// et_Remarks_SSC.getText().toString());
	//
	// //
	// Log.v("==========ID========   store====checked===========date===========remarks==    ",
	// // (XML_Values.STORE + i) + (XML_Values.CHECKED + i)
	// // + (XML_Values.DATE_SITE_STORAGE_CONDITIONS + i)
	// // + "  "
	// // + (XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + i)
	// // + "  " + "     &   "
	// // + et_Store_SSC.getText().toString() + "  "
	// // + checked_Spinner_SSC.getSelectedItem().toString()
	// // + "  " + et_Date_SSC.getText().toString() + "  "
	// // + et_Remarks_SSC.getText().toString());
	//
	// }
	// hm_Site_Conditions.put("Count_SSC",
	// String.valueOf(childcount_SSC_Default));
	//
	// }

	// Adding added layout from button SSC::in hashmap
	private void get_Site_Storage_Conditions_From_Added() {
		childcount_SSC_Added = ll_site_storage_conditions.getChildCount();
		// int id_SSC_Added = childcount_SSC_Default;

		for (int i = 0; i < childcount_SSC_Added; i++) {
			// if (id_SSC_Added < (childcount_SSC_Added +
			// childcount_SSC_Default)) {
			View SSC_View = ll_site_storage_conditions.getChildAt(i);

			et_store_SSC_Added = (EditText) SSC_View
					.findViewById(R.id.store_cleanliness_edittext);
			et_store_SSC_Added.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			spinner_checked_SSC = (Spinner) SSC_View
					.findViewById(R.id.checked_spinner);
			et_date_SSC_Added = (EditText) SSC_View
					.findViewById(R.id.date_edittext);
			et_date_SSC_Added.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_remarks_SSC_Added = (EditText) SSC_View
					.findViewById(R.id.remarks_edittext);
			et_remarks_SSC_Added.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			// hm_Site_Conditions.put((XML_Values.STORE + id_SSC_Added),
			// et_store_SSC_Added.getText().toString());
			// String spinner_Value = spinner_checked_SSC.getSelectedItem()
			// .toString();
			// hm_Site_Conditions.put((XML_Values.CHECKED + id_SSC_Added),
			// spinner_Value);
			// hm_Site_Conditions
			// .put((XML_Values.DATE_SITE_STORAGE_CONDITIONS + id_SSC_Added),
			// et_date_SSC_Added.getText().toString());
			// hm_Site_Conditions
			// .put((XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + id_SSC_Added),
			// et_remarks_SSC_Added.getText().toString());

			// if (checked_Spinner_SSC.getSelectedItemPosition() == 0) {
			// AlertDialog m_AlertDialog = new AlertDialog.Builder(
			// Site_Conditions.this)
			// .setMessage("Select a value for checked")
			// .setPositiveButton("Ok",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(
			// DialogInterface argDialog,
			// int argWhich) {
			//
			// }
			// }).create();
			// m_AlertDialog.setCanceledOnTouchOutside(false);
			// m_AlertDialog.show();
			// } else
			if (!(spinner_checked_SSC.getSelectedItem().toString()
					.equals("Checked")
					&& !et_store_SSC_Added.getText().toString().equals("") && !et_date_SSC_Added
					.getText().toString().equals("")
			// && !et_remarks_SSC_Added.getText().toString().equals("")
			)) {

				hm_Site_Conditions.put((XML_Values.STORE + (i + 1)),
						et_store_SSC_Added.getText().toString());
				String spinner_Value = spinner_checked_SSC.getSelectedItem()
						.toString();
				hm_Site_Conditions.put((XML_Values.CHECKED + (i + 1)),
						spinner_Value);
				hm_Site_Conditions.put(
						(XML_Values.DATE_SITE_STORAGE_CONDITIONS + (i + 1)),
						et_date_SSC_Added.getText().toString());
				hm_Site_Conditions.put(
						(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + (i + 1)),
						et_remarks_SSC_Added.getText().toString());

				Log.v("==========ID========   store====checked==========sdate=======remarks==========  ",
						(XML_Values.STORE + (i + 1))
								+ (XML_Values.CHECKED + (i + 1))
								+ (XML_Values.DATE_SITE_STORAGE_CONDITIONS + (i + 1))
								+ (XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + (i + 1))
								+ "     &   "
								+ et_store_SSC_Added.getText().toString()
								+ "  "
								+ spinner_checked_SSC.getSelectedItem()
										.toString() + "  "
								+ et_date_SSC_Added.getText().toString() + "  "
								+ et_remarks_SSC_Added.getText().toString());
				// id_SSC_Added++;
				// }

				// Log.v("============count SSC========= ", " "
				// + (childcount_SSC_Added + childcount_SSC_Default));

				hm_Site_Conditions.put("Count_SSC",
						String.valueOf(childcount_SSC_Added + 1));
			}

		}
	}

	// Adding default SPAS:: in hashmap
	// private void get_Spare_Part_At_Site_From_Default() {
	// childcount_SPAS_Default = ll_SPAS_Default_Values.getChildCount();
	// for (int i = 0; i < childcount_SPAS_Default; i++) {
	//
	// View SPAS_view = ll_SPAS_Default_Values.getChildAt(i);
	//
	// et_Material_SPAS = (EditText) SPAS_view
	// .findViewById(R.id.material_edittext_SPAS);
	// et_Material_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Quantity_SPAS = (EditText) SPAS_view
	// .findViewById(R.id.quantity_edittext);
	// et_Quantity_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Date_SPAS = (EditText) SPAS_view
	// .findViewById(R.id.date_edittext);
	// et_Date_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_SPAS = (EditText) SPAS_view
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_SPAS.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// hm_Site_Conditions.put((XML_Values.SPARE_PART_MATERIAL + i),
	// et_Material_SPAS.getText().toString());
	// hm_Site_Conditions.put(
	// (XML_Values.QUANTITY_SPARE_PART_AT_SITE + i),
	// et_Quantity_SPAS.getText().toString());
	// hm_Site_Conditions.put((XML_Values.DATE_SPARE_PART_AT_SITE + i),
	// et_Date_SPAS.getText().toString());
	// hm_Site_Conditions.put((XML_Values.REMARKS_SPARE_PART_AT_SITE + i),
	// et_Remarks_SPAS.getText().toString());
	//
	// // Log.v("&&&&&&&&&&&&&    ID  SPAS &&&&&&&&&&&          ",
	// // (XML_Values.SPARE_PART_MATERIAL + i) + "  "
	// // + (XML_Values.QUANTITY_SPARE_PART_AT_SITE + i)
	// // + "   " + (XML_Values.DATE_SPARE_PART_AT_SITE + i)
	// // + "   "
	// // + (XML_Values.REMARKS_SPARE_PART_AT_SITE + i));
	// }
	// hm_Site_Conditions.put("Count_SPAS",
	// String.valueOf(childcount_SPAS_Default));
	// }

	// Adding added layout from button SPAS::in hashmap
	private void get_Spare_Part_At_Site_From_Added() {
		childcount_SPAS_Added = ll_spare_parts_at_site.getChildCount();
		for (int i = 0; i < childcount_SPAS_Added; i++) {
			// if (id_SPAS_Added < (childcount_SPAS_Added +
			// childcount_SPAS_Default)) {

			View SPAS_View = ll_spare_parts_at_site.getChildAt(i);

			et_Material_SPAS = (EditText) SPAS_View
					.findViewById(R.id.material_edittext_SPAS);
			et_Material_SPAS.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Quantity_SPAS = (EditText) SPAS_View
					.findViewById(R.id.quantity_edittext_SPAS);
			et_Quantity_SPAS.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Date_SPAS = (EditText) SPAS_View
					.findViewById(R.id.date_edittext_SPAS);
			et_Date_SPAS.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Remarks_SPAS = (EditText) SPAS_View
					.findViewById(R.id.remarks_edittext_SPAS);
			et_Remarks_SPAS.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			// hm_Site_Conditions.put(
			// (XML_Values.SPARE_PART_MATERIAL + id_SPAS_Added),
			// et_Material_SPAS.getText().toString());
			// hm_Site_Conditions
			// .put((XML_Values.QUANTITY_SPARE_PART_AT_SITE + id_SPAS_Added),
			// et_Quantity_SPAS.getText().toString());
			// hm_Site_Conditions.put(
			// (XML_Values.DATE_SPARE_PART_AT_SITE + id_SPAS_Added),
			// et_Date_SPAS.getText().toString());
			// hm_Site_Conditions
			// .put((XML_Values.REMARKS_SPARE_PART_AT_SITE + id_SPAS_Added),
			// et_Remarks_SPAS.getText().toString());

			hm_Site_Conditions.put((XML_Values.SPARE_PART_MATERIAL + (i + 1)),
					et_Material_SPAS.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.QUANTITY_SPARE_PART_AT_SITE + (i + 1)),
					et_Quantity_SPAS.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.DATE_SPARE_PART_AT_SITE + (i + 1)),
					et_Date_SPAS.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.REMARKS_SPARE_PART_AT_SITE + (i + 1)),
					et_Remarks_SPAS.getText().toString());

			Log.v("============ material==== quantity===============date==========remark========== ",
					(XML_Values.SPARE_PART_MATERIAL + (i + 1))
							+ (XML_Values.QUANTITY_SPARE_PART_AT_SITE + (i + 1))
							+ (XML_Values.DATE_SPARE_PART_AT_SITE + (i + 1))
							+ (XML_Values.REMARKS_SPARE_PART_AT_SITE + (i + 1))
							+ "     &   "
							+ et_Material_SPAS.getText().toString() + "  "
							+ et_Quantity_SPAS.getText().toString() + "  "
							+ et_Date_SPAS.getText().toString() + "  "
							+ et_Remarks_SPAS.getText().toString());

			// id_SPAS_Added++;
			// }

			// Log.v("============count SPAS========= ", " "
			// + (childcount_SPAS_Added + childcount_SPAS_Default));

		}

		hm_Site_Conditions.put("Count_SPAS",
				String.valueOf(childcount_SPAS_Added + 1));
	}

	// Adding default TNI:: in hashmap
	// private void get_Tools_Instruments_From_Default() {
	// childcount_TNI_Default = ll_TNI_Default_Values.getChildCount();
	// for (int i = 0; i < childcount_TNI_Default; i++) {
	//
	// View TNI_view = ll_TNI_Default_Values.getChildAt(i);
	//
	// et_Tools_TNI = (EditText) TNI_view
	// .findViewById(R.id.tools_edittext_TNI);
	// et_Tools_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Quantity_TNI = (EditText) TNI_view
	// .findViewById(R.id.quantity_edittext);
	// et_Quantity_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Date_TNI = (EditText) TNI_view.findViewById(R.id.date_edittext);
	// et_Date_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	// et_Remarks_TNI = (EditText) TNI_view
	// .findViewById(R.id.remarks_edittext);
	// et_Remarks_TNI.setHintTextColor(getResources().getColor(
	// R.color.hint_text_color));
	//
	// hm_Site_Conditions.put((XML_Values.TOOLS + i), et_Tools_TNI
	// .getText().toString());
	// hm_Site_Conditions.put((XML_Values.QUANTITY_TOOLS_INSTRUMENTS + i),
	// et_Quantity_TNI.getText().toString());
	// hm_Site_Conditions.put((XML_Values.DATE_TOOLS_INSTRUMENT + i),
	// et_Date_TNI.getText().toString());
	// hm_Site_Conditions.put((XML_Values.REMARKS_TOOLS_INSTRUMENT + i),
	// et_Remarks_TNI.getText().toString());
	//
	// // Log.v("&&&&&&&&&&&&&    ID  TNI &&&&&&&&&&&          ",
	// // (XML_Values.TOOLS + i) + "  "
	// // + (XML_Values.QUANTITY_SPARE_PART_AT_SITE + i)
	// // + "   " + (XML_Values.DATE_TOOLS_INSTRUMENT + i)
	// // + "   " + (XML_Values.REMARKS_TOOLS_INSTRUMENT + i));
	// }
	// hm_Site_Conditions.put("Count_TNI",
	// String.valueOf(childcount_TNI_Default));
	// }

	// Adding added layout from button TNI::in hashmap
	private void get_Tools_Instruments_From_Added() {
		childcount_TNI_Added = ll_tools_instruments.getChildCount();
		int id_TNI_Added = childcount_TNI_Default;

		for (int i = 0; i < childcount_TNI_Added; i++) {
			// if (id_TNI_Added < (childcount_TNI_Added +
			// childcount_TNI_Default)) {

			View TNI_View = ll_tools_instruments.getChildAt(i);

			et_Tools_TNI = (EditText) TNI_View
					.findViewById(R.id.tools_edittext_TNI);
			et_Tools_TNI.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Quantity_TNI = (EditText) TNI_View
					.findViewById(R.id.quantity_edittext_TNI);
			et_Quantity_TNI.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Date_TNI = (EditText) TNI_View
					.findViewById(R.id.date_edittext_TNI);
			et_Date_TNI.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			et_Remarks_TNI = (EditText) TNI_View
					.findViewById(R.id.remarks_edittext_TNI);
			et_Remarks_TNI.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			// hm_Site_Conditions.put((XML_Values.TOOLS + id_TNI_Added),
			// et_Tools_TNI.getText().toString());
			// hm_Site_Conditions.put(
			// (XML_Values.QUANTITY_TOOLS_INSTRUMENTS + id_TNI_Added),
			// et_Quantity_TNI.getText().toString());
			// hm_Site_Conditions.put(
			// (XML_Values.DATE_TOOLS_INSTRUMENT + id_TNI_Added),
			// et_Date_TNI.getText().toString());
			// hm_Site_Conditions.put(
			// (XML_Values.REMARKS_TOOLS_INSTRUMENT + id_TNI_Added),
			// et_Remarks_TNI.getText().toString());

			hm_Site_Conditions.put((XML_Values.TOOLS + (i + 1)), et_Tools_TNI
					.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + (i + 1)),
					et_Quantity_TNI.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.DATE_TOOLS_INSTRUMENT + (i + 1)), et_Date_TNI
							.getText().toString());
			hm_Site_Conditions.put(
					(XML_Values.REMARKS_TOOLS_INSTRUMENT + (i + 1)),
					et_Remarks_TNI.getText().toString());

			Log.v("============ tool==== quantity===============date==========remark========== ",
					(XML_Values.TOOLS + (i + 1))
							+ (XML_Values.QUANTITY_TOOLS_INSTRUMENTS + (i + 1))
							+ (XML_Values.DATE_TOOLS_INSTRUMENT + (i + 1))
							+ (XML_Values.REMARKS_TOOLS_INSTRUMENT + (i + 1))
							+ "     &   " + et_Tools_TNI.getText().toString()
							+ et_Quantity_TNI.getText().toString()
							+ et_Date_TNI.getText().toString()
							+ et_Remarks_TNI.getText().toString());

			// id_TNI_Added++;
		}
		hm_Site_Conditions.put("Count_TNI",
				String.valueOf(childcount_TNI_Added + 1));
		// Log.v("============count TNI========= ", " "
		// + (childcount_TNI_Added + childcount_TNI_Default));

		// }
	}

	class populateValuesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				if (AppValues.bIsEncryptionRequired)
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
				ResponseParser parser = new ResponseParser(Site_Conditions.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser.parse_Site_Conditions(szDecryptedText,
							xmlData);
				else
					xmlData = parser.parse_Site_Conditions(text.toString(),
							xmlData);

				// Log.d("*SiteConditions-populateValuesTask",
				// "SiteConditions XML DATA HASH MAP ::" + xmlData.size());

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

			// Calling layout_summary_of_inspection_function
			Log.d("", "COUNT OF SSC is :: " + xmlData.get("Count_SSC"));
			Log.d("", "COUNT OF Count_SPAS is :: " + xmlData.get("Count_SPAS"));
			Log.d("", "COUNT OF Count_TNI is :: " + xmlData.get("Count_TNI"));

			int nCountSSC = 0;
			int nCountSPAS = 0;
			int nCountTNI = 0;

			if (xmlData.containsKey(XML_Values.STORE + "0"))
				et_Store_SSC1.setText(xmlData.get(XML_Values.STORE + "0"));

			if (xmlData.containsKey(XML_Values.CHECKED + "0")) {

				String Checked = xmlData.get(XML_Values.CHECKED + "0");
				if (Checked != null) {
					for (int i = 0; i < checked_Spinner_Adapter_default
							.getCount(); i++) {
						if (Checked.equals(checked_Spinner_Adapter_default
								.getItem(i).toString())) {
							checked_Spinner_SSC1.setSelection(i);
							break;
						}
					}
				}
			}
			if (xmlData.containsKey(XML_Values.DATE_SITE_STORAGE_CONDITIONS
					+ "0"))
				et_Date_SSC1.setText(xmlData
						.get(XML_Values.DATE_SITE_STORAGE_CONDITIONS + "0"));

			if (xmlData.containsKey(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS
					+ "0"))
				et_Remarks_SSC1.setText(xmlData
						.get(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS + "0"));

			if (xmlData.containsKey("Count_SSC")) {
				// layout_Site_Storage_Condition_function();
				// else {
				nCountSSC = Integer.parseInt(xmlData.get("Count_SSC"));
				layout_Site_Storage_Condition_function_fromXML(nCountSSC);
				// }
			}

			if (xmlData.containsKey(XML_Values.SPARE_PART_MATERIAL + "0"))
				et_Material_SPAS1.setText(xmlData
						.get(XML_Values.SPARE_PART_MATERIAL + "0"));
			if (xmlData.containsKey(XML_Values.QUANTITY_SPARE_PART_AT_SITE
					+ "0"))
				et_Quantity_SPAS1.setText(xmlData
						.get(XML_Values.QUANTITY_SPARE_PART_AT_SITE + "0"));
			if (xmlData.containsKey(XML_Values.DATE_SPARE_PART_AT_SITE + "0"))
				et_Date_SPAS1.setText(xmlData
						.get(XML_Values.DATE_SPARE_PART_AT_SITE + "0"));
			if (xmlData
					.containsKey(XML_Values.REMARKS_SPARE_PART_AT_SITE + "0"))
				et_Remarks_SPAS1.setText(xmlData
						.get(XML_Values.REMARKS_SPARE_PART_AT_SITE + "0"));

			if (xmlData.containsKey("Count_SPAS")) {
				// layout_Spare_Parts_At_Site_function();
				// else {
				nCountSPAS = Integer.parseInt(xmlData.get("Count_SPAS"));
				layout_Spare_Parts_At_Site_function_fromXML(nCountSPAS);
				// }
			}

			if (xmlData.containsKey(XML_Values.TOOLS + "0"))
				et_Tools_TNI1.setText(xmlData.get(XML_Values.TOOLS + "0"));
			if (xmlData
					.containsKey(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + "0"))
				et_Quantity_TNI1.setText(xmlData
						.get(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + "0"));
			if (xmlData.containsKey(XML_Values.DATE_TOOLS_INSTRUMENT + "0"))
				et_Date_TNI1.setText(xmlData
						.get(XML_Values.DATE_TOOLS_INSTRUMENT + "0"));
			if (xmlData.containsKey(XML_Values.REMARKS_TOOLS_INSTRUMENT + "0"))
				et_Remarks_TNI1.setText(xmlData
						.get(XML_Values.REMARKS_TOOLS_INSTRUMENT + "0"));

			if (xmlData.containsKey("Count_TNI")) {
				// layout_Tools_And_Instruments_function();
				// else {
				nCountTNI = Integer.parseInt(xmlData.get("Count_TNI"));
				layout_Tools_And_Instruments_function_fromXML(nCountTNI);
				// }
			}

			if (progressSiteConditions != null)
				progressSiteConditions.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		progressSiteConditions.setVisibility(View.VISIBLE);
		// saveSiteConditionsData(false);
		new saveDatatask().execute();

		saveDatatask task = new saveDatatask();
		task.flag = false;
		task.execute();
	};

	public class saveDatatask extends AsyncTask<Void, Void, Void> {

		boolean flag = false;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Calling parser function:
			saveSiteConditionsData(flag);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (flag) {
				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Site_Conditions.this)
						.setMessage("Data saved successfully!")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			}

			try {
				if (progressSiteConditions != null)
					progressSiteConditions.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
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
