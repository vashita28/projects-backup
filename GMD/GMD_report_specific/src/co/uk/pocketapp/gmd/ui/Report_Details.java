package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

public class Report_Details extends ParentActivity {
	private static final String TAG = "Report_Details() ";

	HashMap<String, String> report_Details;

	Button save_Button;
	TextView page_Title_TextView;

	EditText customer_Name;
	EditText site_Name;
	Spinner country_type_Spinner;
	EditText customer_rep;
	EditText plant_Name;
	// Spinner plant_Type_Spinner;;
	EditText unit;
	EditText system_Machine_Name;
	EditText type_Name;
	EditText serial_No;
	EditText year_Of_Delivery;
	EditText operating_Hours;

	private ArrayAdapter<CharSequence> country_Spinner_Adapter;
	// private ArrayAdapter<CharSequence> plant_Type_Spinner_Adapter;

	String file_Data = "";
	String report_id;

	// plant type:multi selection
	CheckBox cb_gmd;
	CheckBox cb_transformer;
	CheckBox cb_ehouse;
	CheckBox cb_drive;
	CheckBox cb_cyclo;
	CheckBox cb_motor;

	// service type:multi selection ---> previously only one selection
	CheckBox new_Plant_RB;
	CheckBox upgrade_RB;
	CheckBox trouble_Shooting_RB;
	CheckBox others_RB;
	CheckBox first_Installation_RB;
	CheckBox minor_RB;
	CheckBox major_RB;

	// service task:multiple selection
	CheckBox installation_CB;
	CheckBox comissioning_CB;
	CheckBox test_Assessment_CB;
	CheckBox others_CB;
	CheckBox overall_Works_CB;
	CheckBox re_Comissioning_CB;
	CheckBox diagnostics_CB;
	CheckBox factory_Insp_CB;
	CheckBox repair_CB;
	CheckBox investigation_CB;
	CheckBox inspection_CB;
	CheckBox motor_Insp_Inside_CB;
	CheckBox motor_Insp_Outside_CB;

	String radio_Button_Data;
	String checkBox_Data;

	EditText stator_Text;
	EditText rotor_poles_Text;
	EditText motor_aux_Text;
	Spinner spinner_cyclo_convertor;
	private ArrayAdapter<CharSequence> cyclo_Spinner_Adapter;
	EditText cyclo_converter_Text;

	String stator;
	String rotor_poles;
	String motor_aux;
	String cyclo_converter;
	String st_cyclo_convertor_heading;

	// To apply font:
	TextView GMD_heading_TextView;
	TextView heading_Textview;
	TextView site_heading_TextView;
	TextView customer_TextView;
	TextView site_TextView;
	TextView country_TextView;
	TextView customer_rep_TextView;
	TextView plant_TextView;
	TextView plant_type_TextView;
	TextView unit_TextView;
	TextView system_TextView;
	TextView type_TextView;
	TextView serial_no_TextView;
	TextView year_of_delivery_TextView;
	TextView operating_hrs_TextView;
	TextView service_Info_textView;
	TextView service_type_textView;
	TextView service_task_textView;
	TextView desc_of_service_performed_TextView;

	TextView list_of_distribution_textView;
	TextView distribution_to_customer_textView;

	TextView personnel_involve_fromABB_Textview;
	TextView personnel_involve_from_client_TextView;
	Button btn_add_personnel_involve_from_ABB,
			btn_add_personnel_involve_from_client;

	LinearLayout ll_personnel_involved_from_abb,
			ll_personnel_involved_from_client;

	HashMap<String, String> xmlData;

	String count_list_of_distribution = "";
	String count_dist_to_customer = "";
	LinearLayout linear_List_Of_Distribution;
	EditText location_Text;
	EditText dept_Loc_EditText;
	EditText surname_EditText;
	EditText remarks_EditText;
	View view_LOD;
	LinearLayout linear_Dist_To_Customer;
	EditText send_By_Text;
	EditText to_EditText_1;
	EditText date_EditText;
	EditText comments_integrated_EditText;
	EditText customer_comments_EditText;
	View view_DOC;
	Context context;

	Button btn_add_desc_service, btn_add_service_info;

	TextView tv_Go_To_Server;
	ScrollView scroll_view_content;
	public static Handler mHandler;

	ProgressBar progressReportDetails;

	boolean bIsFirstRun = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_details);
		tv_Go_To_Server = (TextView) findViewById(R.id.go_to_sync_with_server);
		scroll_view_content = (ScrollView) findViewById(R.id.scroll_view_content);

		ToggleReport_Details_Data();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.what == Sync_With_Server.REPORT_UPLOADED) {
					tv_Go_To_Server.setVisibility(View.VISIBLE);
					tv_Go_To_Server
							.setText("Report was successfully uploaded. Please sync with server for new reports");
					scroll_view_content.setVisibility(View.GONE);
				} else {
					ToggleReport_Details_Data();
				}
				super.handleMessage(message);
			}

		};

		if (!AppValues.getReportXMLFile().exists()) {
			Util.setIsReportXMLDownloaded(Report_Details.this, false);
			Util.setReportID(Report_Details.this, "");
			Util.setIsReportUploaded(Report_Details.this, false);

			getContentResolver().delete(DataProvider.Material_Log.CONTENT_URI,
					null, null);
			getContentResolver().delete(DataProvider.Mill.CONTENT_URI, null,
					null);
			getContentResolver().delete(DataProvider.Services.CONTENT_URI,
					null, null);
			getContentResolver().delete(DataProvider.Tasks.CONTENT_URI, null,
					null);
			getContentResolver().delete(DataProvider.Child_Leaf.CONTENT_URI,
					null, null);
		}
	}

	private void ToggleReport_Details_Data() {
		// TODO Auto-generated method stub
		if (!Util.getIsReportXMLDownloaded(Report_Details.this)) {
			tv_Go_To_Server.setVisibility(View.VISIBLE);
			scroll_view_content.setVisibility(View.GONE);

		} else {
			tv_Go_To_Server.setVisibility(View.GONE);
			scroll_view_content.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();
		ToggleReport_Details_Data();
		// DOWNLOAD PDF FROM SERVER:
		// Download_PDF_Task downloadPDFTask = new Download_PDF_Task();
		// downloadPDFTask.execute();

		btn_add_desc_service = (Button) findViewById(R.id.btn_add_desc_service);
		// btn_add_desc_service.setEnabled(false);

		btn_add_service_info = (Button) findViewById(R.id.btn_add_service_info);
		// btn_add_service_info.setEnabled(false);

		// Distribution to customer
		btn_add_desc_service.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View DOC_view = inflater.inflate(
						R.layout.distribution_to_customer_simplerow, null);

				linear_Dist_To_Customer.removeView(DOC_view);
				linear_Dist_To_Customer.addView(DOC_view);

				EditText send_By = (EditText) DOC_view
						.findViewById(R.id.send_by_text);
				EditText to = (EditText) DOC_view.findViewById(R.id.to_text1);
				EditText date = (EditText) DOC_view
						.findViewById(R.id.date_text);
				EditText comments_integrated = (EditText) DOC_view
						.findViewById(R.id.comments_integrated_text);
				EditText customer_comments = (EditText) DOC_view
						.findViewById(R.id.customer_comments_text);

				send_By.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				to.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				date.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				comments_integrated.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				customer_comments.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));

				// btn_add.setEnabled(false);
				//
				// ViewGroup layoutCont = (ViewGroup)
				// findViewById(R.id.ll_distribution_to_customer);
				// final int mCount = layoutCont.getChildCount();
				//
				// // Loop through all of the children.
				// for (int i = 0; i < mCount; ++i) {
				// final View mChild = layoutCont.getChildAt(i);
				//
				// if (i == mCount) {
				// et_1 = (EditText) mChild
				// .findViewById(R.id.customer_comments_text);
				// et_2 = (EditText) mChild
				// .findViewById(R.id.send_by_text);
				// et_3 = (EditText) mChild.findViewById(R.id.to_text1);
				// et_4 = (EditText) mChild.findViewById(R.id.date_text);
				// et_5 = (EditText) mChild
				// .findViewById(R.id.comments_integrated_text);
				// EditTextHAndle();
				// }
				//
				// }

				// int count = linear_Dist_To_Customer.getChildCount();
				// View ll_view = null;
				// for (int i = 0; i < count; i++) {
				// ll_view = linear_Dist_To_Customer.getChildAt(i);
				//
				// if (i == count) {
				// et_1 = (EditText) ll_view
				// .findViewById(R.id.customer_comments_text);
				// et_2 = (EditText) ll_view
				// .findViewById(R.id.send_by_text);
				// et_3 = (EditText) ll_view.findViewById(R.id.to_text1);
				// et_4 = (EditText) ll_view.findViewById(R.id.date_text);
				// et_5 = (EditText) ll_view
				// .findViewById(R.id.comments_integrated_text);
				//
				// EditTextHAndle();
				// }
				// }

			}
		});
		// List of distribution button
		btn_add_service_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View LOD_view = inflater.inflate(
						R.layout.list_of_distribution_simplerow, null);

				linear_List_Of_Distribution.removeView(LOD_view);
				linear_List_Of_Distribution.addView(LOD_view);

				EditText location = (EditText) LOD_view
						.findViewById(R.id.location_Edittext);
				EditText dept = (EditText) LOD_view
						.findViewById(R.id.dept_loc_text1);
				EditText name = (EditText) LOD_view
						.findViewById(R.id.surname_name_text);
				EditText remarks = (EditText) LOD_view
						.findViewById(R.id.remarks_LOD);

				location.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				dept.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				name.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				remarks.setHintTextColor(getResources().getColor(
						R.color.hint_text_color));
				// btn_add_service_info.setEnabled(false);

			}
		});
		linear_List_Of_Distribution = (LinearLayout) findViewById(R.id.ll_list_of_distribution);
		linear_Dist_To_Customer = (LinearLayout) findViewById(R.id.ll_distribution_to_customer);
		context = Report_Details.this;

		if (((LinearLayout) linear_List_Of_Distribution).getChildCount() > 0)
			((LinearLayout) linear_List_Of_Distribution).removeAllViews();

		if (((LinearLayout) linear_Dist_To_Customer).getChildCount() > 0)
			((LinearLayout) linear_Dist_To_Customer).removeAllViews();

		// mTextView.setText("This text will be underlined");

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);
		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);
		// Page title :Set page Heading
		page_Title_TextView.setText("Report Details");

		// font implementation of middle text heading: textview
		heading_Textview = (TextView) findViewById(R.id.heading_text);
		heading_Textview.setTypeface(GMDApplication.fontText);
		// font implementation of Customer: textview
		customer_TextView = (TextView) findViewById(R.id.customer_textview);
		customer_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of Site: textview
		site_TextView = (TextView) findViewById(R.id.site_textview);
		site_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of Site: textview
		country_TextView = (TextView) findViewById(R.id.countryName_textview);
		country_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of customer rep: textview
		customer_rep_TextView = (TextView) findViewById(R.id.customer_Rep_textview);
		customer_rep_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of plant rep: textview
		plant_TextView = (TextView) findViewById(R.id.plant_textview);
		plant_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of plant type rep: textview
		plant_type_TextView = (TextView) findViewById(R.id.plant_type_textview);
		plant_type_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of unit : textview
		unit_TextView = (TextView) findViewById(R.id.unit_textview);
		unit_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of system: textview
		system_TextView = (TextView) findViewById(R.id.system_textview);
		system_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of type_TextView: textview
		type_TextView = (TextView) findViewById(R.id.type_textview);
		type_TextView.setTypeface(GMDApplication.fontText);
		// font implementation of service information:text view
		service_Info_textView = (TextView) findViewById(R.id.service_Info_text);
		service_Info_textView.setTypeface(GMDApplication.fontHeading);
		// font implementation of service type:textview
		service_type_textView = (TextView) findViewById(R.id.service_type_text);
		service_type_textView.setTypeface(GMDApplication.fontText);
		// font implementation of service task:textview
		service_task_textView = (TextView) findViewById(R.id.service_task_text);
		service_task_textView.setTypeface(GMDApplication.fontText);
		// font implementation of desc_of_service_performed:textview
		desc_of_service_performed_TextView = (TextView) findViewById(R.id.desc_of_service_performed_textview);
		desc_of_service_performed_TextView
				.setTypeface(GMDApplication.fontHeading);
		// font implementation of list_of_distribution:textview
		list_of_distribution_textView = (TextView) findViewById(R.id.list_of_distribution_textview);
		list_of_distribution_textView.setTypeface(GMDApplication.fontHeading);
		// font implementation of distribution_to_customer:textview
		distribution_to_customer_textView = (TextView) findViewById(R.id.distribution_to_customer_textview);
		distribution_to_customer_textView
				.setTypeface(GMDApplication.fontHeading);

		personnel_involve_fromABB_Textview = (TextView) findViewById(R.id.personnelinvolve_textview);
		personnel_involve_fromABB_Textview
				.setTypeface(GMDApplication.fontHeading);
		personnel_involve_from_client_TextView = (TextView) findViewById(R.id.personnel_involve_from_client_textview);
		personnel_involve_from_client_TextView
				.setTypeface(GMDApplication.fontHeading);

		// Apply font to customer edit text as hint text color
		customer_Name = (EditText) findViewById(R.id.customer_name);
		customer_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		// Apply font to site edittext
		site_Name = (EditText) findViewById(R.id.site_name);
		site_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to country type spinner
		// -------
		// Apply font to customer rep edit text:
		customer_rep = (EditText) findViewById(R.id.customer_rep);
		customer_rep.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to plant name:
		plant_Name = (EditText) findViewById(R.id.plant_name);
		plant_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to plant type spinner
		// --------------
		// Apply font to unit type edit text
		unit = (EditText) findViewById(R.id.unit);
		unit.setHintTextColor(getResources().getColor(R.color.hint_text_color));
		// Apply font tos ystem machine name edit text
		system_Machine_Name = (EditText) findViewById(R.id.system_machine);
		system_Machine_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to type name edit text
		type_Name = (EditText) findViewById(R.id.type);
		type_Name.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to serial no edit text
		serial_No = (EditText) findViewById(R.id.serial_no);
		serial_No.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to year of delivery edit text
		year_Of_Delivery = (EditText) findViewById(R.id.year_of_delivery);
		year_Of_Delivery.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		// Apply font to operating hours editb text
		operating_Hours = (EditText) findViewById(R.id.operating_hrs);
		operating_Hours.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		// user_Name.setHintTextColor(getResources().getColor(
		// R.color.hint_text_color));

		country_type_Spinner = (Spinner) findViewById(R.id.country_spinner);
		// plant_Type_Spinner = (Spinner) findViewById(R.id.plant_type_spinner);

		// Setting COUNTRY spinner Data:
		country_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.country_arrays, R.layout.custom_spinner_item);
		country_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		country_type_Spinner.setAdapter(country_Spinner_Adapter);
		country_type_Spinner
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

		// Setting PLANT spinner Data:
		// plant_Type_Spinner_Adapter = ArrayAdapter.createFromResource(this,
		// R.array.plant_type_array, R.layout.custom_spinner_item);
		// plant_Type_Spinner_Adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// plant_Type_Spinner.setAdapter(plant_Type_Spinner_Adapter);
		// plant_Type_Spinner
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

		save_Button = (Button) findViewById(R.id.Save_Button);

		customer_Name = (EditText) findViewById(R.id.customer_name);
		site_Name = (EditText) findViewById(R.id.site_name);
		customer_rep = (EditText) findViewById(R.id.customer_rep);
		plant_Name = (EditText) findViewById(R.id.plant_name);
		unit = (EditText) findViewById(R.id.unit);
		system_Machine_Name = (EditText) findViewById(R.id.system_machine);
		type_Name = (EditText) findViewById(R.id.type);
		serial_No = (EditText) findViewById(R.id.serial_no);
		year_Of_Delivery = (EditText) findViewById(R.id.year_of_delivery);
		operating_Hours = (EditText) findViewById(R.id.operating_hrs);

		cb_gmd = (CheckBox) findViewById(R.id.cb_gmd);
		cb_transformer = (CheckBox) findViewById(R.id.cb_transformer);
		cb_ehouse = (CheckBox) findViewById(R.id.cb_ehouse);
		cb_drive = (CheckBox) findViewById(R.id.cb_drive);
		cb_cyclo = (CheckBox) findViewById(R.id.cb_cyclo);
		cb_motor = (CheckBox) findViewById(R.id.cb_motor);

		// radio buttons:
		new_Plant_RB = (CheckBox) findViewById(R.id.new_plant_rb);
		upgrade_RB = (CheckBox) findViewById(R.id.upgrade_rb);
		trouble_Shooting_RB = (CheckBox) findViewById(R.id.troubleshoting_rb);
		others_RB = (CheckBox) findViewById(R.id.others_rb);
		first_Installation_RB = (CheckBox) findViewById(R.id.first_inst_rb);
		minor_RB = (CheckBox) findViewById(R.id.minor_Insp_rb);
		major_RB = (CheckBox) findViewById(R.id.major_overhaul_rb);

		// font implememtation of radio buttons
		new_Plant_RB.setTypeface(GMDApplication.fontText);
		upgrade_RB.setTypeface(GMDApplication.fontText);
		trouble_Shooting_RB.setTypeface(GMDApplication.fontText);
		others_RB.setTypeface(GMDApplication.fontText);
		first_Installation_RB.setTypeface(GMDApplication.fontText);
		minor_RB.setTypeface(GMDApplication.fontText);
		major_RB.setTypeface(GMDApplication.fontText);

		// Checkboxes:
		installation_CB = (CheckBox) findViewById(R.id.ins_cb);
		comissioning_CB = (CheckBox) findViewById(R.id.comm_cb);
		test_Assessment_CB = (CheckBox) findViewById(R.id.test_ass_cb);
		others_CB = (CheckBox) findViewById(R.id.others_cb);
		overall_Works_CB = (CheckBox) findViewById(R.id.overall_works_cb);
		re_Comissioning_CB = (CheckBox) findViewById(R.id.re_com_cb);
		diagnostics_CB = (CheckBox) findViewById(R.id.diagnostics_cb);
		factory_Insp_CB = (CheckBox) findViewById(R.id.fac_ins_cb);
		repair_CB = (CheckBox) findViewById(R.id.repair_cb);
		investigation_CB = (CheckBox) findViewById(R.id.investigation_cb);
		inspection_CB = (CheckBox) findViewById(R.id.inspection_cb);
		motor_Insp_Inside_CB = (CheckBox) findViewById(R.id.motor_insp_inside_cb);
		motor_Insp_Outside_CB = (CheckBox) findViewById(R.id.motor_insp_outside_cb);

		// font implememntation of checkboxes
		installation_CB.setTypeface(GMDApplication.fontText);
		comissioning_CB.setTypeface(GMDApplication.fontText);
		test_Assessment_CB.setTypeface(GMDApplication.fontText);
		others_CB.setTypeface(GMDApplication.fontText);
		overall_Works_CB.setTypeface(GMDApplication.fontText);
		re_Comissioning_CB.setTypeface(GMDApplication.fontText);
		diagnostics_CB.setTypeface(GMDApplication.fontText);
		factory_Insp_CB.setTypeface(GMDApplication.fontText);
		repair_CB.setTypeface(GMDApplication.fontText);
		investigation_CB.setTypeface(GMDApplication.fontText);
		inspection_CB.setTypeface(GMDApplication.fontText);
		motor_Insp_Inside_CB.setTypeface(GMDApplication.fontText);
		motor_Insp_Outside_CB.setTypeface(GMDApplication.fontText);

		// Description of service performed:
		stator_Text = (EditText) findViewById(R.id.stator_edittext);
		stator_Text.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		rotor_poles_Text = (EditText) findViewById(R.id.rotor_poles_edittext);
		rotor_poles_Text.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		motor_aux_Text = (EditText) findViewById(R.id.motor_aux_edittext);
		motor_aux_Text.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		spinner_cyclo_convertor = (Spinner) findViewById(R.id.spinner_cyclo_convertor);

		// Setting CYCLO CONVERTOR spinner Data:
		cyclo_Spinner_Adapter = ArrayAdapter.createFromResource(this,
				R.array.cyclo_convertor_array, R.layout.custom_spinner_item);
		cyclo_Spinner_Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_cyclo_convertor.setAdapter(cyclo_Spinner_Adapter);
		spinner_cyclo_convertor
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
		cyclo_converter_Text = (EditText) findViewById(R.id.cyclo_converter_edittext);
		cyclo_converter_Text.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		btn_add_personnel_involve_from_ABB = (Button) findViewById(R.id.btn_add_personnel_involve_from_ABB);
		btn_add_personnel_involve_from_client = (Button) findViewById(R.id.btn_add_personnel_involve_from_client);
		ll_personnel_involved_from_abb = (LinearLayout) findViewById(R.id.ll_personnel_involve_from_ABB);
		ll_personnel_involved_from_client = (LinearLayout) findViewById(R.id.ll_personnel_involve_from_client);

		// btn_add_personnel_involve_from_ABB
		btn_add_personnel_involve_from_ABB
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = LayoutInflater
								.from(getApplicationContext());
						View PIF_ABB_view = inflater.inflate(
								R.layout.row_personnel_involve_from_abb, null);

						EditText et_N_Degree_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_n_degree_abb);
						EditText et_surname_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_surname_name_abb);
						EditText et_Cat_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_cat_abb);
						EditText et_Function_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_function_abb);
						EditText et_ArrivalDate_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_arrival_date_abb);
						EditText et_DepartureDate_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_departure_date_abb);
						EditText et_DeptLoc_ABB = (EditText) PIF_ABB_view
								.findViewById(R.id.et_dept_loc_abb);

						et_N_Degree_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_surname_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_Cat_ABB.setHintTextColor(getResources().getColor(
								R.color.hint_text_color));
						et_Function_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_ArrivalDate_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_DepartureDate_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_DeptLoc_ABB.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));

						ll_personnel_involved_from_abb.removeView(PIF_ABB_view);
						ll_personnel_involved_from_abb.addView(PIF_ABB_view);
					}
				});

		// btn_add_personnel_involve_from_client
		btn_add_personnel_involve_from_client
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = LayoutInflater
								.from(getApplicationContext());
						View PIF_Client_view = inflater.inflate(
								R.layout.row_personnel_involved_from_client,
								null);

						EditText et_N_Degree_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_n_drgree_client);
						EditText et_surname_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_surname_name_personnel_client);
						EditText et_Cat_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_cat_personnel_client);
						EditText et_Function_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_function_personnel_client);
						EditText et_from_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_from_personnel_client);
						EditText et_till_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_departure_till_personnel_client);
						EditText et_DeptLoc_Client = (EditText) PIF_Client_view
								.findViewById(R.id.et_dept_loc_personnel_client);

						et_N_Degree_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_surname_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_Cat_Client.setHintTextColor(getResources().getColor(
								R.color.hint_text_color));
						et_Function_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_from_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_till_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));
						et_DeptLoc_Client.setHintTextColor(getResources()
								.getColor(R.color.hint_text_color));

						ll_personnel_involved_from_client
								.removeView(PIF_Client_view);
						ll_personnel_involved_from_client
								.addView(PIF_Client_view);
					}
				});

		if (((LinearLayout) ll_personnel_involved_from_abb).getChildCount() > 0)
			((LinearLayout) ll_personnel_involved_from_abb).removeAllViews();

		if (((LinearLayout) ll_personnel_involved_from_client).getChildCount() > 0)
			((LinearLayout) ll_personnel_involved_from_client).removeAllViews();

		// Radio button check conditions:
		// new_Plant_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (new_Plant_RB.isChecked() == true) {
		// upgrade_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// others_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// upgrade_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (upgrade_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// others_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// trouble_Shooting_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (trouble_Shooting_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// upgrade_RB.setChecked(false);
		// others_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// others_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (others_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// upgrade_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// first_Installation_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (first_Installation_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// upgrade_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// others_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// minor_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (minor_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// upgrade_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// others_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// major_RB.setChecked(false);
		// }
		// }
		// });
		// major_RB.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (major_RB.isChecked() == true) {
		// new_Plant_RB.setChecked(false);
		// upgrade_RB.setChecked(false);
		// trouble_Shooting_RB.setChecked(false);
		// others_RB.setChecked(false);
		// first_Installation_RB.setChecked(false);
		// minor_RB.setChecked(false);
		// }
		// }
		// });

		progressReportDetails = (ProgressBar) findViewById(R.id.progress_reportDetails);

		if (scroll_view_content.getVisibility() == View.VISIBLE) {
			progressReportDetails.setVisibility(View.VISIBLE);
			new populateValuesTask().execute();
		} else {
			progressReportDetails.setVisibility(View.GONE);
		}

		// save Button:
		save_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Validation Part:
				String customer_Data = customer_Name.getText().toString()
						.trim();
				String site_Data = site_Name.getText().toString().trim();
				String customer_Rep = customer_rep.getText().toString().trim();
				String plant = plant_Name.getText().toString().trim();
				String unit_no = unit.getText().toString().trim();
				String system_Machine = system_Machine_Name.getText()
						.toString().trim();
				String type = type_Name.getText().toString().trim();
				String serial_No_Text = serial_No.getText().toString().trim();
				String year_Of_Delievry = year_Of_Delivery.getText().toString()
						.trim();
				String operating_Hours_Data = operating_Hours.getText()
						.toString().trim();

				if (customer_Data.length() == 0 || site_Data.length() == 0
						|| customer_Rep.length() == 0 || plant.length() == 0
						|| unit_no.length() == 0
						|| system_Machine.length() == 0 || type.length() == 0
						|| serial_No_Text.length() == 0
						|| year_Of_Delievry.length() == 0
						|| operating_Hours_Data.length() == 0) {
					showAlertDialog();
				} else if (country_type_Spinner.getSelectedItemPosition() == 0) {// ||
																					// plant_Type_Spinner.getSelectedItemPosition()
																					// ==
																					// 0
					showAlertDialog();
				} else if (cb_gmd.isChecked() == false
						&& cb_cyclo.isChecked() == false
						&& cb_drive.isChecked() == false
						&& cb_ehouse.isChecked() == false
						&& cb_motor.isChecked() == false
						&& cb_transformer.isChecked() == false) {
					showAlertDialog();
				} else if (new_Plant_RB.isChecked() == false
						&& upgrade_RB.isChecked() == false
						&& trouble_Shooting_RB.isChecked() == false
						&& others_RB.isChecked() == false
						&& first_Installation_RB.isChecked() == false
						&& minor_RB.isChecked() == false
						&& major_RB.isChecked() == false) {
					showAlertDialog();
				} else if (installation_CB.isChecked() == false
						&& comissioning_CB.isChecked() == false
						&& test_Assessment_CB.isChecked() == false
						&& others_CB.isChecked() == false
						&& overall_Works_CB.isChecked() == false
						&& re_Comissioning_CB.isChecked() == false
						&& diagnostics_CB.isChecked() == false
						&& factory_Insp_CB.isChecked() == false
						&& repair_CB.isChecked() == false
						&& investigation_CB.isChecked() == false
						&& inspection_CB.isChecked() == false
						&& motor_Insp_Inside_CB.isChecked() == false
						&& motor_Insp_Outside_CB.isChecked() == false) {
					showAlertDialog();
				}
				// else if (spinner_cyclo_convertor.getSelectedItemPosition() ==
				// 0) {// ||
				//
				// showAlertDialog();
				// }

				else {
					Save_All_Values_Function(true);
				}
			}
		});

		bIsFirstRun = false;
	}

	void showAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				Report_Details.this);
		alertDialog.setTitle("Alert!");
		alertDialog.setMessage("All fields are mandatory");
		alertDialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		alertDialog.show();
	}

	private void Save_All_Values_Function(boolean showSaveDialog) {

		Log.d("ReportDetails-SaveAllValuesFunction()",
				"************************* Save All Values Report Details");

		// Spinner data:Country & Plant type
		String country_Name = country_type_Spinner.getSelectedItem().toString();
		// String plant_Type_Name = plant_Type_Spinner.getSelectedItem()
		// .toString();

		// Radio Button Data:Service Type
		if (new_Plant_RB.isChecked() == true) {
			radio_Button_Data = new_Plant_RB.getText().toString();
		} else if (upgrade_RB.isChecked() == true) {
			radio_Button_Data = upgrade_RB.getText().toString();
		} else if (trouble_Shooting_RB.isChecked() == true) {
			radio_Button_Data = trouble_Shooting_RB.getText().toString();
		} else if (others_RB.isChecked() == true) {
			radio_Button_Data = others_RB.getText().toString();
		} else if (first_Installation_RB.isChecked() == true) {
			radio_Button_Data = first_Installation_RB.getText().toString();
		} else if (minor_RB.isChecked() == true) {
			radio_Button_Data = minor_RB.getText().toString();
		} else if (major_RB.isChecked() == true) {
			radio_Button_Data = major_RB.getText().toString();
		}

		// Decs of service performed:
		stator = stator_Text.getText().toString();
		rotor_poles = rotor_poles_Text.getText().toString();
		motor_aux = motor_aux_Text.getText().toString();
		st_cyclo_convertor_heading = spinner_cyclo_convertor.getSelectedItem()
				.toString();
		Log.v("*****************************cyclo convertor selection ************",
				st_cyclo_convertor_heading);
		cyclo_converter = cyclo_converter_Text.getText().toString();

		// Getting Data in HashMap to send to XML_Serializer Class:
		report_Details = new HashMap<String, String>();
		report_Details.clear();
		report_Details.put(XML_Values.REPORT_ID, report_id);
		report_Details.put(XML_Values.CUSTOMER, customer_Name.getText()
				.toString());
		report_Details.put(XML_Values.SITE, site_Name.getText().toString());
		report_Details.put(XML_Values.COUNTRY_NAME, country_Name);

		// temp values inserted
		// report_Details.put(XML_Values.CUSTOMER, "Vashita");
		// report_Details.put(XML_Values.SITE, "Plant 1");
		// report_Details.put(XML_Values.COUNTRY_NAME, "India");
		// temp value ends

		report_Details.put(XML_Values.CUSTOMER_REP, customer_rep.getText()
				.toString());
		report_Details.put(XML_Values.PLANT, plant_Name.getText().toString());
		report_Details.put(XML_Values.PLANT_TYPE_GMD,
				new Boolean(cb_gmd.isChecked()).toString());
		report_Details.put(XML_Values.PLANT_TYPE_TRANSFORMER, new Boolean(
				cb_transformer.isChecked()).toString());
		report_Details.put(XML_Values.PLANT_TYPE_EHOUSE,
				new Boolean(cb_ehouse.isChecked()).toString());
		report_Details.put(XML_Values.PLANT_TYPE_MOTOR,
				new Boolean(cb_motor.isChecked()).toString());
		report_Details.put(XML_Values.PLANT_TYPE_DRIVE,
				new Boolean(cb_drive.isChecked()).toString());
		report_Details.put(XML_Values.PLANT_TYPE_CYCLO,
				new Boolean(cb_cyclo.isChecked()).toString());
		report_Details.put(XML_Values.UNIT, unit.getText().toString());
		report_Details.put(XML_Values.SYSTEM_MACHINE, system_Machine_Name
				.getText().toString());
		report_Details.put(XML_Values.TYPE, type_Name.getText().toString());
		report_Details
				.put(XML_Values.SERIAL_NO, serial_No.getText().toString());
		report_Details.put(XML_Values.YEAR_OF_DELIVERY, year_Of_Delivery
				.getText().toString());
		report_Details.put(XML_Values.OPERATING_HOURS, operating_Hours
				.getText().toString());
		// Service Information :Service Type:

		// Changing boolean true/false to string
		// sString new_Plant=new
		// Boolean(new_Plant_RB.isChecked()).toString();
		report_Details.put(XML_Values.NEW_PLANT,
				new Boolean(new_Plant_RB.isChecked()).toString());
		report_Details.put(XML_Values.UPGRADE,
				new Boolean(upgrade_RB.isChecked()).toString());
		report_Details.put(XML_Values.TROUBLESHOOTING, new Boolean(
				trouble_Shooting_RB.isChecked()).toString());
		report_Details.put(XML_Values.OTHERS,
				new Boolean(others_RB.isChecked()).toString());
		report_Details.put(XML_Values.FIRST_INSTALLATION, new Boolean(
				first_Installation_RB.isChecked()).toString());
		report_Details.put(XML_Values.MINOR_INSPECTION,
				new Boolean(minor_RB.isChecked()).toString());
		report_Details.put(XML_Values.MAJOR_OVERHAUL,
				new Boolean(major_RB.isChecked()).toString());
		// Service Information :Service Task:
		report_Details.put(XML_Values.INSTALLATION,
				new Boolean(installation_CB.isChecked()).toString());
		report_Details.put(XML_Values.COMISSIONING,
				new Boolean(comissioning_CB.isChecked()).toString());
		report_Details.put(XML_Values.TEST_ASSESSMENT, new Boolean(
				test_Assessment_CB.isChecked()).toString());
		report_Details.put(XML_Values.OTHERS_SERVICE_TASK, new Boolean(
				others_CB.isChecked()).toString());
		report_Details.put(XML_Values.OVERHAUL_WORKS, new Boolean(
				overall_Works_CB.isChecked()).toString());
		report_Details.put(XML_Values.RE_COMISSIONING, new Boolean(
				re_Comissioning_CB.isChecked()).toString());
		report_Details.put(XML_Values.DIAGNOSTICS,
				new Boolean(diagnostics_CB.isChecked()).toString());
		report_Details.put(XML_Values.FACTORY_INSP,
				new Boolean(factory_Insp_CB.isChecked()).toString());
		report_Details.put(XML_Values.REPAIR,
				new Boolean(repair_CB.isChecked()).toString());
		report_Details.put(XML_Values.INVESTIGATION, new Boolean(
				installation_CB.isChecked()).toString());
		report_Details.put(XML_Values.INSPECTION,
				new Boolean(inspection_CB.isChecked()).toString());
		report_Details.put(XML_Values.MOTOR_INSP_INSIDE, new Boolean(
				motor_Insp_Inside_CB.isChecked()).toString());
		report_Details.put(XML_Values.MOTOR_INSP_EXTERNAL, new Boolean(
				motor_Insp_Outside_CB.isChecked()).toString());
		// Description of service PErformed:
		report_Details.put(XML_Values.STATOR, stator_Text.getText().toString());
		report_Details.put(XML_Values.ROTOR_POLES, rotor_poles_Text.getText()
				.toString());
		report_Details.put(XML_Values.MOTOR_AUXILLARIES, motor_aux_Text
				.getText().toString());
		report_Details.put(XML_Values.CYCLO_CONVERTER, cyclo_converter_Text
				.getText().toString());
		report_Details.put("cycloheading", st_cyclo_convertor_heading);

		Log.v("++++++++++++++++++++cyclo heaadin,   text",
				st_cyclo_convertor_heading + "       "
						+ cyclo_converter_Text.getText().toString());

		// Adding list of distribution data in hashmap i.e report details:
		getDataFromAddedLayouts_LOD();

		// Adding distribution to customer in hashmap i.e report details:
		getDataFromAddedLayouts_DTC();

		// Adding Personnel involved from ABB in hashmap i.e report details:
		getDataFrom_Personnel_involved_from_ABB();

		// Adding Personnel involved from Client in hashmap i.e report details:
		getDataFrom_Personnel_involved_from_Client();

		// List of Distribution: the data will go by function
		// Name:--->layout_List_of_Distribution_function
		// Distribution To Customer:the data will go by function
		// Name:--->layout_Distribution_To_Customer_function

		// Log.v(TAG, "Calling XML SERIALIZER");
		// XML_Serializer_Activity
		// .Report_Details_Xml_Creation(report_Details);
		// Log.v(TAG, "DONE WITH SAVING DATA AT SAME LOCATION");
		//
		// AlertDialog m_AlertDialog = new AlertDialog.Builder(
		// Report_Details.this)
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
		// m_AlertDialog.show();

		XML_Creation xml_Creation = new XML_Creation();
		xml_Creation.save_Report_Details(report_Details);
		// Log.v(TAG, "DONE WITH SAVING DATA AT SAME LOCATION");

		if (showSaveDialog) {
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					Report_Details.this)
					.setMessage("Data saved successfully!")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {

								}
							}).create();
			m_AlertDialog.show();
		}

		try {
			if (progressReportDetails != null)
				progressReportDetails.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Adding dynamically rows acc as given in fetch xml file for LIST OF
	// DISTRIBUTION
	private void layout_List_of_Distribution_function(int rows) {
		// TODO Auto-generated method stub
		// Log.d("layout_List_of_Distribution_function()", " COUNT :: " + rows);
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_LOD = inflater.inflate(
						R.layout.list_of_distribution_simplerow, null);
				location_Text = (EditText) view_LOD
						.findViewById(R.id.location_Edittext);
				dept_Loc_EditText = (EditText) view_LOD
						.findViewById(R.id.dept_loc_text1);
				surname_EditText = (EditText) view_LOD
						.findViewById(R.id.surname_name_text);
				remarks_EditText = (EditText) view_LOD
						.findViewById(R.id.remarks_LOD);

				if (xmlData.get(XML_Values.LOCATION + i) != null) {
					location_Text.setText(DecodeXML(xmlData
							.get(XML_Values.LOCATION + i)));
				}

				if (xmlData.get(XML_Values.DEPT_LOC + i) != null)
					dept_Loc_EditText.setText(DecodeXML(xmlData
							.get(XML_Values.DEPT_LOC + i)));
				if (xmlData.get(XML_Values.NAME + i) != null)
					surname_EditText.setText(DecodeXML(xmlData
							.get(XML_Values.NAME + i)));
				if (xmlData.get(XML_Values.REMARKS_LOD + i) != null)
					remarks_EditText.setText(DecodeXML(xmlData
							.get(XML_Values.REMARKS_LOD + i)));

				if (location_Text.getText().toString().equals(""))
					location_Text.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (dept_Loc_EditText.getText().toString().equals(""))
					dept_Loc_EditText.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (surname_EditText.getText().toString().equals(""))
					surname_EditText.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (remarks_EditText.getText().toString().equals(""))
					remarks_EditText.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				linear_List_Of_Distribution.addView(view_LOD);

				// if ((i == count) && (remarks_EditText.isFocused() ==
				// true)) {
				// linear_List_Of_Distribution.removeView(view_LOD);
				// // location_Text.setText("");
				// // dept_Loc1_EditText.setText("");
				// // surname_EditText.setText("");
				// // dept_loc2_EditText.setText("");
				// linear_List_Of_Distribution.addView(view_LOD);
				// }

				if (i == rows) {
					linear_List_Of_Distribution.removeView(view_LOD);
					linear_List_Of_Distribution.addView(view_LOD);
				}

				// if (i > count) {
				// linear_List_Of_Distribution.removeView(view_LOD);
				// // location_Text.setText("");
				// // dept_Loc1_EditText.setText("");
				// // surname_EditText.setText("");
				// // dept_loc2_EditText.setText("");
				// linear_List_Of_Distribution.addView(view_LOD);
				// }

				// Putting LIST OF DISTRIBUTION data in hashMap
				// (report_Details)
				// for passing in XML_Serializer Class
				// report_Details.put("Child_LOD", String.valueOf(rows));
				// report_Details.put(XML_Values.LOCATION + i, location_Text
				// .getText().toString());
				// report_Details.put(XML_Values.DEPT_LOC + i,
				// dept_Loc_EditText.getText().toString());
				// report_Details.put(XML_Values.NAME + i, surname_EditText
				// .getText().toString());
				// report_Details.put(XML_Values.REMARKS_LOD + i,
				// remarks_EditText.getText().toString());
			}
		}// Add new lod view
	}

	// Adding dynamically rows acc as given in fetch xml file for DISTRIBUTION
	// TO CUSTOMER
	private void layout_Distribution_To_Customer_function(int rows) {
		// TODO Auto-generated method stub
		// Log.d("layout_Distribution_To_Customer_function()", " COUNT :: " +
		// rows);
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view_DOC = inflater.inflate(
						R.layout.distribution_to_customer_simplerow, null);
				send_By_Text = (EditText) view_DOC
						.findViewById(R.id.send_by_text);
				to_EditText_1 = (EditText) view_DOC.findViewById(R.id.to_text1);
				date_EditText = (EditText) view_DOC
						.findViewById(R.id.date_text);
				comments_integrated_EditText = (EditText) view_DOC
						.findViewById(R.id.comments_integrated_text);
				customer_comments_EditText = (EditText) view_DOC
						.findViewById(R.id.customer_comments_text);

				send_By_Text.setText(DecodeXML(xmlData.get(XML_Values.SEND_BY
						+ i)));
				to_EditText_1
						.setText(DecodeXML(xmlData.get(XML_Values.TO + i)));
				date_EditText.setText(DecodeXML(xmlData.get(XML_Values.DATE_DTC
						+ i)));
				comments_integrated_EditText.setText(DecodeXML(xmlData
						.get(XML_Values.COMMENTS_INTEGRATED + i)));
				customer_comments_EditText.setText(DecodeXML(xmlData
						.get(XML_Values.CUSTOMER_COMMENTS + i)));
				linear_Dist_To_Customer.addView(view_DOC);

				if (send_By_Text.getText().toString().equals(""))
					send_By_Text.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (to_EditText_1.getText().toString().equals(""))
					to_EditText_1.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (date_EditText.getText().toString().equals(""))
					date_EditText.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (comments_integrated_EditText.getText().toString()
						.equals(""))
					comments_integrated_EditText
							.setHintTextColor(getResources().getColor(
									R.color.hint_text_color));
				if (customer_comments_EditText.getText().toString().equals(""))
					customer_comments_EditText.setHintTextColor(getResources()
							.getColor(R.color.hint_text_color));

				if (i == count) {
					linear_Dist_To_Customer.removeView(view_DOC);
					linear_Dist_To_Customer.addView(view_DOC);
				}

				// if (i >count) {
				// Log.v(TAG, "value of i---->" + i +
				// "   value of count---->"+
				// count);
				// linear_Dist_To_Customer.removeView(view_DOC);
				// // send_By_Text.setText("");
				// // to_EditText_1.setText("");
				// // date_EditText.setText("");
				// // comments_integrated_EditText.setText("");
				// // customer_comments_EditText.setText("");
				// linear_Dist_To_Customer.addView(view_DOC);
				// }

				// Putting Distribution to Customer Data data in hashMap
				// (report_Details) for passing in XML_Serializer Class
				// report_Details.put("Child_DTC", String.valueOf(rows));
				// report_Details.put(XML_Values.SEND_BY + i, send_By_Text
				// .getText().toString());
				// report_Details.put(XML_Values.TO + i, to_EditText_1
				// .getText().toString());
				// report_Details.put(XML_Values.DATE_DTC + i, date_EditText
				// .getText().toString());
				// report_Details.put(XML_Values.CUSTOMER_COMMENTS + i,
				// customer_comments_EditText.getText().toString());
				// report_Details.put(XML_Values.COMMENTS_INTEGRATED + i,
				// comments_integrated_EditText.getText().toString());

			}
		}// Add new dtc view
	}

	private void getDataFromAddedLayouts_LOD() {

		int childcount = linear_List_Of_Distribution.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View view_LOD1 = linear_List_Of_Distribution.getChildAt(i);

			EditText location = (EditText) view_LOD1
					.findViewById(R.id.location_Edittext);
			EditText dept = (EditText) view_LOD1
					.findViewById(R.id.dept_loc_text1);
			EditText name = (EditText) view_LOD1
					.findViewById(R.id.surname_name_text);
			EditText remarks = (EditText) view_LOD1
					.findViewById(R.id.remarks_LOD);
			// Log.d("****************************************************",
			// "******************************* "
			// + location.getText().toString() + "   "
			// + dept.getText().toString() + "   "
			// + name.getText().toString() + "   "
			// + remarks.getText().toString());

			report_Details.put(XML_Values.LOCATION + i, location.getText()
					.toString());
			report_Details.put(XML_Values.DEPT_LOC + i, dept.getText()
					.toString());
			report_Details.put(XML_Values.NAME + i, name.getText().toString());
			report_Details.put(XML_Values.REMARKS_LOD + i, remarks.getText()
					.toString());
			report_Details.put("Child_LOD", String.valueOf(childcount));

		}

	}

	private void getDataFromAddedLayouts_DTC() {

		int childcount = linear_Dist_To_Customer.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View view_DOC1 = linear_Dist_To_Customer.getChildAt(i);

			EditText send_By = (EditText) view_DOC1
					.findViewById(R.id.send_by_text);
			EditText to = (EditText) view_DOC1.findViewById(R.id.to_text1);
			EditText date = (EditText) view_DOC1.findViewById(R.id.date_text);
			EditText comments_integrated = (EditText) view_DOC1
					.findViewById(R.id.comments_integrated_text);
			EditText customer_comments = (EditText) view_DOC1
					.findViewById(R.id.customer_comments_text);
			// Log.d("****************************************************",
			// "******************************* "
			// + send_By.getText().toString() + "   "
			// + to.getText().toString() + "   "
			// + comments_integrated.getText().toString() + "   "
			// + date.getText().toString());

			report_Details.put(XML_Values.SEND_BY + i, send_By.getText()
					.toString());
			report_Details.put(XML_Values.TO + i, to.getText().toString());
			report_Details.put(XML_Values.DATE + i, date.getText().toString());
			report_Details.put(XML_Values.COMMENTS_INTEGRATED + i,
					comments_integrated.getText().toString());
			report_Details.put(XML_Values.CUSTOMER_COMMENTS + i,
					customer_comments.getText().toString());
			report_Details.put("Child_DTC", String.valueOf(childcount));

		}

	}

	private void getDataFrom_Personnel_involved_from_ABB() {

		int childcount = ll_personnel_involved_from_abb.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View view_PIF_ABB = ll_personnel_involved_from_abb.getChildAt(i);

			EditText et_N_Degree_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_n_degree_abb);
			EditText et_surname_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_surname_name_abb);
			EditText et_Cat_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_cat_abb);
			EditText et_Function_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_function_abb);
			EditText et_ArrivalDate_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_arrival_date_abb);
			EditText et_DepartureDate_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_departure_date_abb);
			EditText et_DeptLoc_ABB = (EditText) view_PIF_ABB
					.findViewById(R.id.et_dept_loc_abb);

			Log.d("*************************DATA FROM PERSONNEL***************************",
					"******************************* "
							+ et_N_Degree_ABB.getText().toString() + "   "
							+ et_surname_ABB.getText().toString() + "   "
							+ et_Cat_ABB.getText().toString() + "   "
							+ et_Function_ABB.getText().toString() + "  "
							+ et_ArrivalDate_ABB.getText().toString() + " "
							+ et_DepartureDate_ABB.getText().toString() + "  "
							+ et_DeptLoc_ABB.getText().toString());

			report_Details.put(XML_Values.PERSONNEL_ABB_N + i, et_N_Degree_ABB
					.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_SURNAME_NAME + i,
					et_surname_ABB.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_CAT + i, et_Cat_ABB
					.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_FUNCTION + i,
					et_Function_ABB.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE + i,
					et_ArrivalDate_ABB.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE + i,
					et_DepartureDate_ABB.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_ABB_DEPT_LOC + i,
					et_DeptLoc_ABB.getText().toString());
		}
		report_Details.put(XML_Values.PERSONNEL_ABB_COUNT,
				String.valueOf(childcount));
		Log.v("*************abb count**********", "" + childcount);

	}

	private void getDataFrom_Personnel_involved_from_Client() {

		int childcount = ll_personnel_involved_from_client.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View view_PIF_Client = ll_personnel_involved_from_client
					.getChildAt(i);

			EditText et_N_Degree_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_n_drgree_client);
			EditText et_surname_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_surname_name_personnel_client);
			EditText et_Cat_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_cat_personnel_client);
			EditText et_Function_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_function_personnel_client);
			EditText et_ArrivalDate_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_from_personnel_client);
			EditText et_DepartureDate_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_departure_till_personnel_client);
			EditText et_DeptLoc_Client = (EditText) view_PIF_Client
					.findViewById(R.id.et_dept_loc_personnel_client);

			Log.d("*************************DATA FROM PERSONNEL***************************",
					"******************************* "
							+ et_N_Degree_Client.getText().toString() + "   "
							+ et_surname_Client.getText().toString() + "   "
							+ et_Cat_Client.getText().toString() + "   "
							+ et_Function_Client.getText().toString() + "  "
							+ et_ArrivalDate_Client.getText().toString() + " "
							+ et_DepartureDate_Client.getText().toString()
							+ "  " + et_DeptLoc_Client.getText().toString());

			report_Details.put(XML_Values.PERSONNEL_CLIENT_N + i,
					et_N_Degree_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_SURNAME + i,
					et_surname_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_CAT + i,
					et_Cat_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_FUNCTION + i,
					et_Function_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_FROM + i,
					et_ArrivalDate_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_TILL + i,
					et_DepartureDate_Client.getText().toString());
			report_Details.put(XML_Values.PERSONNEL_CLIENT_DEPT_LOC + i,
					et_DeptLoc_Client.getText().toString());
		}
		report_Details.put(XML_Values.PERSONNEL_CLIENT_COUNT,
				String.valueOf(childcount));
		Log.v("*************abb count**********", "" + childcount);

	}

	private void show_Personnel_involved_from_ABB_function(int rows) {
		// TODO Auto-generated method stub
		Log.d("show_Personnel_involved_from_ABB_function()", " COUNT :: "
				+ rows);
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Personnel_ABB = inflater.inflate(
						R.layout.row_personnel_involve_from_abb, null);

				EditText et_N_Degree_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_n_degree_abb);
				EditText et_surname_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_surname_name_abb);
				EditText et_Cat_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_cat_abb);
				EditText et_Function_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_function_abb);
				EditText et_ArrivalDate_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_arrival_date_abb);
				EditText et_DepartureDate_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_departure_date_abb);
				EditText et_DeptLoc_ABB = (EditText) view_Personnel_ABB
						.findViewById(R.id.et_dept_loc_abb);

				if (xmlData.get(XML_Values.PERSONNEL_ABB_N + i) != null)
					et_N_Degree_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_N + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_SURNAME_NAME + i) != null)
					et_surname_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_SURNAME_NAME + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_CAT + i) != null)
					et_Cat_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_CAT + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_FUNCTION + i) != null)
					et_Function_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_FUNCTION + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE + i) != null)
					et_ArrivalDate_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE + i) != null)
					et_DepartureDate_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE + i)));

				if (xmlData.get(XML_Values.PERSONNEL_ABB_DEPT_LOC + i) != null)
					et_DeptLoc_ABB.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_ABB_DEPT_LOC + i)));

				// Setting hint color is field is empty
				if (et_N_Degree_ABB.getText().toString().equals(""))
					et_N_Degree_ABB.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_surname_ABB.getText().toString().equals(""))
					et_surname_ABB.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_Cat_ABB.getText().toString().equals(""))
					et_Cat_ABB.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_Function_ABB.getText().toString().equals(""))
					et_Function_ABB.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_ArrivalDate_ABB.getText().toString().equals(""))
					et_ArrivalDate_ABB.setHintTextColor(getResources()
							.getColor(R.color.hint_text_color));
				if (et_DepartureDate_ABB.getText().toString().equals(""))
					et_DepartureDate_ABB.setHintTextColor(getResources()
							.getColor(R.color.hint_text_color));
				if (et_DeptLoc_ABB.getText().toString().equals(""))
					et_DeptLoc_ABB.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));

				ll_personnel_involved_from_abb.addView(view_Personnel_ABB);

				if (i == rows) {
					ll_personnel_involved_from_abb
							.removeView(view_Personnel_ABB);
					ll_personnel_involved_from_abb.addView(view_Personnel_ABB);
				}
			}
		}// Add new lod view
	}

	private void show_Personnel_involved_from_Client_function(int rows) {
		// TODO Auto-generated method stub
		Log.d("show_Personnel_involved_from_Client_function()", " COUNT :: "
				+ rows);
		int count = rows;
		if (count >= 1) {
			for (int i = 1; i <= count; i++) { // count +1

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_Personnel_Client = inflater.inflate(
						R.layout.row_personnel_involved_from_client, null);

				EditText et_N_Degree_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_n_drgree_client);
				EditText et_surname_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_surname_name_personnel_client);
				EditText et_Cat_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_cat_personnel_client);
				EditText et_Function_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_function_personnel_client);
				EditText et_from_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_from_personnel_client);
				EditText et_till_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_departure_till_personnel_client);
				EditText et_DeptLoc_Client = (EditText) view_Personnel_Client
						.findViewById(R.id.et_dept_loc_personnel_client);

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_N + i) != null)
					et_N_Degree_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_N + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_SURNAME + i) != null)
					et_surname_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_SURNAME + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_CAT + i) != null)
					et_Cat_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_CAT + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_FUNCTION + i) != null)
					et_Function_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_FUNCTION + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_FROM + i) != null)
					et_from_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_FROM + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_TILL + i) != null)
					et_till_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_TILL + i)));

				if (xmlData.get(XML_Values.PERSONNEL_CLIENT_DEPT_LOC + i) != null)
					et_DeptLoc_Client.setText(DecodeXML(xmlData
							.get(XML_Values.PERSONNEL_CLIENT_DEPT_LOC + i)));

				// Setting hint color is field is empty
				if (et_N_Degree_Client.getText().toString().equals(""))
					et_N_Degree_Client.setHintTextColor(getResources()
							.getColor(R.color.hint_text_color));
				if (et_surname_Client.getText().toString().equals(""))
					et_surname_Client.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_Cat_Client.getText().toString().equals(""))
					et_Cat_Client.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_Function_Client.getText().toString().equals(""))
					et_Function_Client.setHintTextColor(getResources()
							.getColor(R.color.hint_text_color));
				if (et_from_Client.getText().toString().equals(""))
					et_from_Client.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_till_Client.getText().toString().equals(""))
					et_till_Client.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));
				if (et_DeptLoc_Client.getText().toString().equals(""))
					et_DeptLoc_Client.setHintTextColor(getResources().getColor(
							R.color.hint_text_color));

				ll_personnel_involved_from_client
						.addView(view_Personnel_Client);

				if (i == rows) {
					ll_personnel_involved_from_client
							.removeView(view_Personnel_Client);
					ll_personnel_involved_from_client
							.addView(view_Personnel_Client);
				}
			}
		}// Add new lod view
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (!bIsFirstRun) {
			progressReportDetails.setVisibility(View.VISIBLE);
			// Save_All_Values_Function(false);
			new saveDatatask().execute();
		}

	}

	class saveDatatask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Calling parser function:
			Save_All_Values_Function(false);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (progressReportDetails != null)
				progressReportDetails.setVisibility(View.GONE);
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				Report_Details.this)
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
		quitAlertDialog.show();
		return;
	}

	class populateValuesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Calling parser function:
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
				} else
					Log.d("Report Details",
							" FILE MISSINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG *************");
				xmlData = new HashMap<String, String>();
				ResponseParser parser = new ResponseParser(Report_Details.this);

				if (AppValues.bIsEncryptionRequired)
					xmlData = parser
							.parse_rep_details(szDecryptedText, xmlData);
				else
					xmlData = parser
							.parse_rep_details(text.toString(), xmlData);

				// Log.d("*ReportDetails-onResume",
				// "ReportDetails XML DATA HASH MAP ::" + xmlData.size());

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

			// Giving details of site name and mill name to header activity
			// Intent myintent1=new
			// Intent(Report_Details.this,Header_Activity.class);
			// myintent1.putExtra("Site_Name", xmlData.get("site_name"));
			// myintent1.putExtra("Mill_Name", xmlData.get("mill_name"));
			// startActivity(myintent1);

			if (xmlData.containsKey(XML_Values.CUSTOMER)) {
				if (xmlData.get(XML_Values.CUSTOMER).equals(XML_Values.NULL)) {
					customer_Name.setText("");
				} else {
					customer_Name.setText(xmlData.get(XML_Values.CUSTOMER));
				}
			}

			if (xmlData.containsKey(XML_Values.SITE)) {
				if (xmlData.get(XML_Values.SITE).equals(XML_Values.NULL))
					site_Name.setText("");
				else
					site_Name.setText(xmlData.get(XML_Values.SITE));
			}

			if (xmlData.containsKey(XML_Values.COUNTRY_NAME)) {
				String country = xmlData.get(XML_Values.COUNTRY_NAME);
				if (country != null) {
					for (int i = 0; i < country_Spinner_Adapter.getCount(); i++) {
						if (country.equals(country_Spinner_Adapter.getItem(i)
								.toString())) {
							country_type_Spinner.setSelection(i);
							break;
						}
					}
				}
			}

			if (xmlData.containsKey(XML_Values.CUSTOMER_REP)) {
				if (xmlData.get(XML_Values.CUSTOMER_REP)
						.equals(XML_Values.NULL))
					customer_rep.setText("");
				else
					customer_rep.setText(DecodeXML(xmlData
							.get(XML_Values.CUSTOMER_REP)));
			}

			if (xmlData.containsKey(XML_Values.PLANT)) {
				if (xmlData.get(XML_Values.PLANT).equals(XML_Values.NULL))
					plant_Name.setText("");
				else
					plant_Name.setText(xmlData.get(XML_Values.PLANT));
			}

			// if (xmlData.containsKey(XML_Values.PLANT_TYPE)) {
			// String plant_type = xmlData.get(XML_Values.PLANT_TYPE);
			// if (plant_type != null) {
			// for (int i = 0; i < plant_Type_Spinner_Adapter.getCount(); i++) {
			// if (plant_type.equals(plant_Type_Spinner_Adapter
			// .getItem(i).toString())) {
			// plant_Type_Spinner.setSelection(i);
			// break;
			// }
			// }
			// }
			// }

			// Plant Type
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_CYCLO)) {
				String szPlantTypeCyclo = xmlData
						.get(XML_Values.PLANT_TYPE_CYCLO);
				if (szPlantTypeCyclo.equals(XML_Values.TRUE)) {
					cb_cyclo.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_DRIVE)) {
				String szPlantTypeDrive = xmlData
						.get(XML_Values.PLANT_TYPE_DRIVE);
				if (szPlantTypeDrive.equals(XML_Values.TRUE)) {
					cb_drive.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_EHOUSE)) {
				String szPlantTypeEHouse = xmlData
						.get(XML_Values.PLANT_TYPE_EHOUSE);
				if (szPlantTypeEHouse.equals(XML_Values.TRUE)) {
					cb_ehouse.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_GMD)) {
				String szPlantTypeGMD = xmlData.get(XML_Values.PLANT_TYPE_GMD);
				if (szPlantTypeGMD.equals(XML_Values.TRUE)) {
					cb_gmd.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_MOTOR)) {
				String szPlantTypeMotor = xmlData
						.get(XML_Values.PLANT_TYPE_MOTOR);
				if (szPlantTypeMotor.equals(XML_Values.TRUE)) {
					cb_motor.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.PLANT_TYPE_TRANSFORMER)) {
				String szPlantTypeTransformer = xmlData
						.get(XML_Values.PLANT_TYPE_TRANSFORMER);
				if (szPlantTypeTransformer.equals(XML_Values.TRUE)) {
					cb_transformer.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.UNIT)) {
				if (xmlData.get(XML_Values.UNIT).equals(XML_Values.NULL))
					unit.setText("");
				else
					unit.setText(DecodeXML(xmlData.get(XML_Values.UNIT)));
			}

			if (xmlData.containsKey(XML_Values.SYSTEM_MACHINE)) {
				if (xmlData.get(XML_Values.SYSTEM_MACHINE).equals(
						XML_Values.NULL))
					system_Machine_Name.setText("");
				else
					system_Machine_Name.setText(DecodeXML(xmlData
							.get(XML_Values.SYSTEM_MACHINE)));
			}

			if (xmlData.containsKey(XML_Values.TYPE))
				if (xmlData.get(XML_Values.TYPE).equals(XML_Values.NULL))
					type_Name.setText("");
				else
					type_Name.setText(DecodeXML(xmlData.get(XML_Values.TYPE)));

			if (xmlData.containsKey(XML_Values.SERIAL_NO))
				if (xmlData.get(XML_Values.SERIAL_NO).equals(XML_Values.NULL))
					serial_No.setText("");
				else
					serial_No.setText(DecodeXML(xmlData
							.get(XML_Values.SERIAL_NO)));

			if (xmlData.containsKey(XML_Values.YEAR_OF_DELIVERY))
				if (xmlData.get(XML_Values.YEAR_OF_DELIVERY).equals(
						XML_Values.NULL))
					year_Of_Delivery.setText("");
				else
					year_Of_Delivery.setText(DecodeXML(xmlData
							.get(XML_Values.YEAR_OF_DELIVERY)));

			if (xmlData.containsKey(XML_Values.OPERATING_HOURS))
				if (xmlData.get(XML_Values.OPERATING_HOURS).equals(
						XML_Values.NULL))
					operating_Hours.setText("");
				else
					operating_Hours.setText(DecodeXML(xmlData
							.get(XML_Values.OPERATING_HOURS)));

			// Setting xml data to SERVICE INFORMATION: Service Type
			if (xmlData.containsKey(XML_Values.NEW_PLANT)) {
				String new_Plant_Data = xmlData.get(XML_Values.NEW_PLANT);
				if (new_Plant_Data.equals(XML_Values.TRUE)) {
					new_Plant_RB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.UPGRADE)) {
				String upgrade_Data = xmlData.get(XML_Values.UPGRADE);
				if (upgrade_Data.equals(XML_Values.TRUE)) {
					upgrade_RB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.TROUBLESHOOTING)) {
				String troubleshooting_Data = xmlData
						.get(XML_Values.TROUBLESHOOTING);
				if (troubleshooting_Data.equals(XML_Values.TRUE)) {
					trouble_Shooting_RB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.OTHERS)) {
				String others_Data = xmlData.get(XML_Values.OTHERS);
				if (others_Data.equals("true")) {
					others_RB.setChecked(true);

				}
			}

			if (xmlData.containsKey(XML_Values.FIRST_INSTALLATION)) {
				String first_installation_Data = xmlData
						.get(XML_Values.FIRST_INSTALLATION);
				if (first_installation_Data.equals(XML_Values.TRUE)) {
					first_Installation_RB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.MINOR_INSPECTION)) {
				String minor_inspection_Data = xmlData
						.get(XML_Values.MINOR_INSPECTION);
				if (minor_inspection_Data.equals(XML_Values.TRUE)) {
					minor_RB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.MAJOR_OVERHAUL)) {
				String major_overhaul_Data = xmlData
						.get(XML_Values.MAJOR_OVERHAUL);
				if (major_overhaul_Data.equals(XML_Values.TRUE)) {
					major_RB.setChecked(true);
				}
			}

			// Setting xml data to SERVICE INFORMATION: Service Task--->
			if (xmlData.containsKey(XML_Values.INSTALLATION)) {
				String installation_Data = xmlData.get(XML_Values.INSTALLATION);
				if (installation_Data.equals(XML_Values.TRUE)) {
					installation_CB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.COMISSIONING)) {
				String comissioning_Data = xmlData.get(XML_Values.COMISSIONING);
				if (comissioning_Data.equals(XML_Values.TRUE)) {
					comissioning_CB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.TEST_ASSESSMENT)) {
				String test_assessment_Data = xmlData
						.get(XML_Values.TEST_ASSESSMENT);
				if (test_assessment_Data.equals(XML_Values.TRUE)) {
					test_Assessment_CB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.OTHERS_SERVICE_TASK)) {
				String others_task_Data = xmlData
						.get(XML_Values.OTHERS_SERVICE_TASK);

				if (others_task_Data.equals(XML_Values.TRUE)) {
					others_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.OVERHAUL_WORKS)) {
				String overhaul_works_Data = xmlData
						.get(XML_Values.OVERHAUL_WORKS);
				if (overhaul_works_Data.equals(XML_Values.TRUE)) {
					overall_Works_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.RE_COMISSIONING)) {
				String re_comissioning_Data = xmlData
						.get(XML_Values.RE_COMISSIONING);
				if (re_comissioning_Data.equals(XML_Values.TRUE)) {
					re_Comissioning_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.DIAGNOSTICS)) {
				String diagnostics_Data = xmlData.get(XML_Values.DIAGNOSTICS);
				if (diagnostics_Data.equals(XML_Values.TRUE)) {
					diagnostics_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.FACTORY_INSP)) {
				String factory_insp_Data = xmlData.get(XML_Values.FACTORY_INSP);
				if (factory_insp_Data.equals(XML_Values.TRUE)) {
					factory_Insp_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.REPAIR)) {
				String repair_Data = xmlData.get(XML_Values.REPAIR);
				if (repair_Data.equals(XML_Values.TRUE)) {
					repair_CB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.INVESTIGATION)) {
				String investigation_Data = xmlData
						.get(XML_Values.INVESTIGATION);
				if (investigation_Data.equals(XML_Values.TRUE)) {
					investigation_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.INSPECTION)) {
				String inspection_Data = xmlData.get(XML_Values.INSPECTION);
				if (inspection_Data.equals(XML_Values.TRUE)) {
					inspection_CB.setChecked(true);
				}
			}

			if (xmlData.containsKey(XML_Values.MOTOR_INSP_INSIDE)) {
				String motor_insp_inside_Data = xmlData
						.get(XML_Values.MOTOR_INSP_INSIDE);
				if (motor_insp_inside_Data.equals(XML_Values.TRUE)) {
					motor_Insp_Inside_CB.setChecked(true);
				}
			}
			if (xmlData.containsKey(XML_Values.MOTOR_INSP_EXTERNAL)) {
				String motor_insp_external_Data = xmlData
						.get(XML_Values.MOTOR_INSP_EXTERNAL);
				if (motor_insp_external_Data.equals(XML_Values.TRUE)) {
					motor_Insp_Outside_CB.setChecked(true);
				}
			}

			// Description of service performed
			if (xmlData.containsKey(XML_Values.STATOR))
				if (xmlData.get(XML_Values.STATOR).equals(XML_Values.NULL))
					stator_Text.setText("");
				else
					stator_Text.setText(DecodeXML(xmlData
							.get(XML_Values.STATOR)));
			if (xmlData.containsKey(XML_Values.ROTOR_POLES))
				if (xmlData.get(XML_Values.ROTOR_POLES).equals(XML_Values.NULL))
					rotor_poles_Text.setText("");
				else
					rotor_poles_Text.setText(DecodeXML(xmlData
							.get(XML_Values.ROTOR_POLES)));
			if (xmlData.containsKey(XML_Values.MOTOR_AUXILLARIES))
				if (xmlData.get(XML_Values.MOTOR_AUXILLARIES).equals(
						XML_Values.NULL))
					motor_aux_Text.setText("");
				else
					motor_aux_Text.setText(DecodeXML(xmlData
							.get(XML_Values.MOTOR_AUXILLARIES)));
			if (xmlData.containsKey(XML_Values.CYCLO_CONVERTER))
				if (xmlData.get(XML_Values.CYCLO_CONVERTER).equals(
						XML_Values.NULL))
					cyclo_converter_Text.setText("");
				else
					cyclo_converter_Text.setText(DecodeXML(xmlData
							.get(XML_Values.CYCLO_CONVERTER)));

			if (xmlData.containsKey(XML_Values.LOD_COUNT)) {
				count_list_of_distribution = xmlData.get(XML_Values.LOD_COUNT);
				int count_rows_for_LOD = Integer
						.parseInt(count_list_of_distribution);
				// Log.d("========COUNT============ ",
				// "COUNT List of Distribution :: " + count_rows_for_LOD);

				// Seting xml data to lists :LIST OF DISTRIBUTION:
				layout_List_of_Distribution_function(count_rows_for_LOD);
			}

			if (xmlData.containsKey(XML_Values.DTC_COUNT)) {
				count_dist_to_customer = xmlData.get(XML_Values.DTC_COUNT);
				int count_rows_for_DTC = Integer
						.parseInt(count_dist_to_customer);
				// Log.d("ReportDetails-() ",
				// "COUNT Distribution to customer :: "
				// + count_rows_for_DTC);

				// Seting xml data to lists :DISTRIBUTION TO CUSTOMER:
				layout_Distribution_To_Customer_function(count_rows_for_DTC);
			}

			if (xmlData.containsKey(XML_Values.PERSONNEL_ABB_COUNT)) {
				String count_Personnel_ABB = xmlData
						.get(XML_Values.PERSONNEL_ABB_COUNT);
				int count_Personnel_From_ABB = Integer
						.parseInt(count_Personnel_ABB);

				// Seting xml data to lists :PERSONNEL INVOLVED FROM ABB:
				show_Personnel_involved_from_ABB_function(count_Personnel_From_ABB);
			}

			if (xmlData.containsKey(XML_Values.PERSONNEL_CLIENT_COUNT)) {
				String count_Personnel_Client = xmlData
						.get(XML_Values.PERSONNEL_CLIENT_COUNT);
				int count_Personnel_From_Client = Integer
						.parseInt(count_Personnel_Client);

				// Seting xml data to lists :PERSONNEL INVOLVED FROM CLIENT:
				show_Personnel_involved_from_Client_function(count_Personnel_From_Client);
			}
			report_id = Util.getReportID(Report_Details.this);

			try {
				if (progressReportDetails != null)
					progressReportDetails.setVisibility(View.GONE);
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
