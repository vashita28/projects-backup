package co.uk.pocketapp.gmd.ui;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.adapter.SC_Mill_Adapter;
import co.uk.pocketapp.gmd.adapter.SC_Services_Leaf_Adapter;
import co.uk.pocketapp.gmd.adapter.SC_Services_Main_Adapter;
import co.uk.pocketapp.gmd.adapter.SC_Services_SecondGen_Adapter;
import co.uk.pocketapp.gmd.adapter.SC_Services_ThirdGen_Adapter;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.tasks.CreateServiceCheckListXMLTask;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.uk.pocketapp.common.Common;
import com.uk.pocketapp.fragments.FileBrowserActivity;
import com.uk.pocketapp.fragments.HelpSupportFiles;

public class Service_CheckList_Without_Swipe extends ParentActivity {

	private String mLibPath = "";

	// ListView report_listview;
	// private ArrayAdapter<String> listAdapter;
	// CustomAdaptor_Checklist custom_Adaptor = new CustomAdaptor_Checklist();

	// Button btn_report_a_problem;
	private static final int PICKFILE_RESULT_CODE = 1;

	View view_service_checklists_top_bar;

	String reportDate = null;

	ListView listview_mill, listview_services_main_gen,
			listview_services_second_gen, listview_services_third_gen,
			listview_services_leaf;

	LinearLayout ll_servicesmain, ll_servicessecondgen, ll_servicesthirdgen,
			ll_servicesleaf, ll_servicesmain_back, ll_servicessecondgen_back,
			ll_servicesthirdgen_back, ll_servicesleaf_back;

	TextView textview_title, textview_GMD_Heading; // item_moments_textView;

	String m_szServicesLeafItem, m_szServicesThirdGenItem,
			m_szServicesSecondGenItem, m_szServicesMainItem, m_szMillName;
	String m_szServicesLeafItemID, m_szServicesThirdGenItemID,
			m_szServicesSecondGenItemID, m_szServicesMainItemID, m_szMillID,
			m_szReportID;

	View view_sample_task_page;// view_sample_task_page_2;
	Boolean bIsSampleTaskPageVisible = false;
	// bIsSampleTaskPage2Visible = false;
	LinearLayout ll_listviews;

	EditText m_etWorkScope, m_etWorkScopeComments;
	LinearLayout ll_workscope, ll_workscope_comments, ll_work_Carried_out,
			ll_conditions, ll_recommendations, ll_cancel;

	Button btn_workscope_close;
	ImageView imageview_workscope_part;

	Spinner spinner_work_Carried_Out, spinner_conditions,
			spinner_recommendations;

	private ArrayAdapter<CharSequence> work_Carried_Out_Spinner_Adapter;
	private ArrayAdapter<CharSequence> conditions_Spinner_Adapter;
	private ArrayAdapter<CharSequence> recommendations_Spinner_Adapter;

	// EditText m_et1, m_et2, m_et3, m_et4;
	// EditText m_et_task2_1, m_et_task2_2, m_et_task2_3;

	Button btn_cancel, btn_saveandcomplete, btn_Close; // btn_task2_save,
	// btn_task2_cancel;

	Cursor mCursorMill, mCursorServicesThirdGen, mCursorServicesSecondGen,
			mCursorServicesMain, mCursorServicesLeaf;
	ContentObserver dataObserver;
	int nPreviousPositionServicesThirdGen = -1,
			nPreviousPositionServicesSecondGen = -1,
			nPreviousPositionServicesMain = -1, nPreviousPositionMill = -1;
	ListAdapter mServicesLeafAdapter, mServicesThirdGenAdapter,
			mServicesMainAdapter, mServicesSecondGenAdapter, mMillAdapter;

	int nMillId = -1, nServicesMainID = -1, nServicesSecondGenID = -1,
			nServicesThirdGenID = -1, nServicesLeafID = -1;

	String m_szCurrentLeafChild = "";

	public static Handler mHandler;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_checklist_without_swipe);

		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(800))
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();

		m_szReportID = Util.getReportID(Service_CheckList_Without_Swipe.this);

		// sample task page
		{
			spinner_work_Carried_Out = (Spinner) findViewById(R.id.spinner_work_carried_out);
			spinner_conditions = (Spinner) findViewById(R.id.spinner_condition);
			spinner_recommendations = (Spinner) findViewById(R.id.spinner_recommendation);
			ll_workscope = (LinearLayout) findViewById(R.id.ll_workscope);
			ll_workscope_comments = (LinearLayout) findViewById(R.id.ll_workscope_comments);
			btn_workscope_close = (Button) findViewById(R.id.btn_close);
			imageview_workscope_part = (ImageView) findViewById(R.id.imageview_workscope_part);

			ll_work_Carried_out = (LinearLayout) findViewById(R.id.ll_spinner_Work_Carried_Out);
			ll_conditions = (LinearLayout) findViewById(R.id.ll_spinner_condition);
			ll_recommendations = (LinearLayout) findViewById(R.id.ll_spinner_recommendation);
			ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);

			// Setting work_Carried out spinner Data:
			work_Carried_Out_Spinner_Adapter = ArrayAdapter.createFromResource(
					this, R.array.work_carried_out,
					R.layout.custom_spinner_item);
			work_Carried_Out_Spinner_Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_work_Carried_Out
					.setAdapter(work_Carried_Out_Spinner_Adapter);
			spinner_work_Carried_Out
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

			// Setting conditions spinner Data:
			conditions_Spinner_Adapter = ArrayAdapter.createFromResource(this,
					R.array.conditions, R.layout.custom_spinner_item);
			conditions_Spinner_Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_conditions.setAdapter(conditions_Spinner_Adapter);
			spinner_conditions
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

			// Setting recommndations spinner Data:
			recommendations_Spinner_Adapter = ArrayAdapter
					.createFromResource(this, R.array.recommendations,
							R.layout.custom_spinner_item);
			recommendations_Spinner_Adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_recommendations.setAdapter(recommendations_Spinner_Adapter);
			spinner_recommendations
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

			view_sample_task_page = (View) findViewById(R.id.view_sample_task_page);
			m_etWorkScope = (EditText) view_sample_task_page
					.findViewById(R.id.et_workscope);
			m_etWorkScope.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			m_etWorkScopeComments = (EditText) view_sample_task_page
					.findViewById(R.id.et_workscope_comments);
			m_etWorkScopeComments.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			m_etWorkScope.setImeOptions(EditorInfo.IME_ACTION_DONE);
			m_etWorkScopeComments.setImeOptions(EditorInfo.IME_ACTION_DONE);

			// m_et1 = (EditText) view_sample_task_page.findViewById(R.id.et_1);
			// m_et2 = (EditText) view_sample_task_page.findViewById(R.id.et_2);
			// m_et3 = (EditText) view_sample_task_page.findViewById(R.id.et_3);
			// m_et4 = (EditText) view_sample_task_page.findViewById(R.id.et_4);
			// item_moments_textView = (TextView)
			// findViewById(R.id.item_moments_text);
			// item_moments_textView.setTypeface(GMDApplication.fontHeading);
			// m_et1.setHintTextColor(getResources().getColor(
			// R.color.hint_text_color));
			// m_et2.setHintTextColor(getResources().getColor(
			// R.color.hint_text_color));
			// m_et3.setHintTextColor(getResources().getColor(
			// R.color.hint_text_color));
			// m_et4.setHintTextColor(getResources().getColor(
			// R.color.hint_text_color));

			btn_cancel = (Button) view_sample_task_page
					.findViewById(R.id.btn_cancel);
			btn_saveandcomplete = (Button) view_sample_task_page
					.findViewById(R.id.btn_saveandcomplete);
			btn_Close = (Button) findViewById(R.id.btn_close);

			btn_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					view_sample_task_page.setVisibility(View.GONE);
					ll_listviews.setVisibility(View.VISIBLE);
					bIsSampleTaskPageVisible = false;
					// Setting all edittext empty:
					// m_et1.setText("");
					// m_et2.setText("");
					// m_et3.setText("");
					// m_et4.setText("");
					m_etWorkScope.setText("");
					m_etWorkScopeComments.setText("");
					spinner_conditions.setSelection(0);
					spinner_recommendations.setSelection(0);
					spinner_work_Carried_Out.setSelection(0);
				}
			});

			btn_saveandcomplete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					// if (m_et1.getText().length() > 0||
					// m_et2.getText().length() > 0|| m_et3.getText().length() >
					// 0|| m_et4.getText().length() > 0)

					if (m_etWorkScope.getText().length() > 0
							|| spinner_work_Carried_Out
									.getSelectedItemPosition() > 0
							|| spinner_conditions.getSelectedItemPosition() > 0
							|| spinner_recommendations
									.getSelectedItemPosition() > 0) {
						// Save code goes here-----------------

						String work_Carried_Out;
						String conditions;
						String recommendations;

						String szValue = null;

						if (m_szCurrentLeafChild.equalsIgnoreCase("Work scope")) {
							szValue = m_etWorkScope.getText().toString();
						} else if (m_szCurrentLeafChild
								.equalsIgnoreCase("Work carried out")) { // Work
							// carried
							// out
							work_Carried_Out = spinner_work_Carried_Out
									.getSelectedItem().toString();
							szValue = work_Carried_Out;
						} else if (m_szCurrentLeafChild
								.equalsIgnoreCase("Conditions")) { // Conditions
							conditions = spinner_conditions.getSelectedItem()
									.toString();
							szValue = conditions;
						} else if (m_szCurrentLeafChild
								.equalsIgnoreCase("Recommendations")) { // Recommendations
							recommendations = spinner_recommendations
									.getSelectedItem().toString();
							szValue = recommendations;
						} else if (m_szCurrentLeafChild
								.equalsIgnoreCase("Comments")) { // Comments
							szValue = m_etWorkScope.getText().toString();
						}

						if (szValue != null
								&& szValue.equalsIgnoreCase("select an option")) {
							showFieldMandatoryDialog();
							return;
						}

						ContentValues taskValues = new ContentValues();
						taskValues.put(DataProvider.Tasks.TASK_COMPLETED,
								"true");
						taskValues
								.put(DataProvider.Tasks.TASK_CONTENT, szValue);

						String selectionArgs[] = new String[] { m_szMillID,
								m_szReportID, m_szServicesThirdGenItemID,
								m_szCurrentLeafChild };
						String szSelection = DataProvider.Tasks.MILL_ID
								+ " = ?" + " AND "
								+ DataProvider.Tasks.REPORT_ID + " = ?"
								+ " AND " + DataProvider.Tasks.SERVICES_ITEM_ID
								+ " = ?" + " AND "
								+ DataProvider.Tasks.TASK_NAME + " = ?";

						getContentResolver().update(
								DataProvider.Tasks.CONTENT_URI, taskValues,
								szSelection, selectionArgs);
						getContentResolver().notifyChange(
								DataProvider.Tasks.CONTENT_URI, null);

						if (szValue.equalsIgnoreCase("Not Inspected")
								&& m_szCurrentLeafChild
										.equalsIgnoreCase("Work carried out")) {
							String selectionArgs1[] = new String[] {
									m_szMillID, m_szReportID,
									m_szServicesThirdGenItemID,
									"Report a problem", "Comments" };
							String szSelection1 = DataProvider.Tasks.MILL_ID
									+ " = ?" + " AND "
									+ DataProvider.Tasks.REPORT_ID + " = ?"
									+ " AND "
									+ DataProvider.Tasks.SERVICES_ITEM_ID
									+ " = ?" + " AND "
									+ DataProvider.Tasks.TASK_NAME
									+ " != ? AND "
									+ DataProvider.Tasks.TASK_NAME + " != ?";

							ContentValues taskValues1 = new ContentValues();
							taskValues1.put(DataProvider.Tasks.TASK_COMPLETED,
									"true");

							getContentResolver().update(
									DataProvider.Tasks.CONTENT_URI,
									taskValues1, szSelection1, selectionArgs1);
							getContentResolver().notifyChange(
									DataProvider.Tasks.CONTENT_URI, null);
						}

						// String szSelection =
						// DataProvider.Summary_Values.CHILD_LEAF_NODE_ID
						// + " ='"
						// + m_szServicesThirdGenItemID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_MAIN_ID
						// + " ='"
						// + m_szMillID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
						// + " ='"
						// + m_szServicesMainItemID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
						// + " ='"
						// + m_szServicesSecondGenItemID
						// + "' AND "
						// + DataProvider.Summary_Values.REPORT_ID
						// + " ='"
						// + Util.getReportID(Service_CheckList_New.this)
						// + "'";
						//
						// Cursor cursorSummary = getContentResolver().query(
						// DataProvider.Summary_Values.CONTENT_URI, null,
						// szSelection, null, null);
						// cursorSummary.moveToFirst();
						// String szID = "";
						//
						// ContentValues summaryValues = new ContentValues();
						// summaryValues.put(DataProvider.Summary_Values.CHILD,
						// m_et1.getText().toString() + ","
						// + m_et2.getText().toString() + ","
						// + m_et3.getText().toString() + ","
						// + m_et4.getText().toString());
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_LEAF_NODE_ID,
						// m_szServicesThirdGenItemID);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_THIRD_GEN_ID,
						// m_szServicesSecondGenItemID);
						// summaryValues
						// .put(DataProvider.Summary_Values.CHILD_SECOND_GEN_ID,
						// m_szServicesMainItemID);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_MAIN_ID,
						// m_szMillID);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_LEAF_NODE,
						// m_szServicesThirdGenItem);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_THIRD_GEN,
						// m_szServicesSecondGenItem);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_SECOND_GEN,
						// m_szServicesMainItem);
						// summaryValues.put(
						// DataProvider.Summary_Values.CHILD_MAIN,
						// m_szMillName);
						// summaryValues.put(
						// DataProvider.Summary_Values.REPORT_ID,
						// Util.getReportID(Service_CheckList_New.this));
						//
						// if (cursorSummary.getCount() > 0) {
						// szID = cursorSummary.getString(cursorSummary
						// .getColumnIndex(DataProvider.Summary_Values.ID));
						//
						// getContentResolver().update(
						// DataProvider.Summary_Values.CONTENT_URI,
						// summaryValues,
						// DataProvider.Summary_Values.ID + " ='"
						// + szID + "'", null);
						//
						// } else {
						// getContentResolver().insert(
						// DataProvider.Summary_Values.CONTENT_URI,
						// summaryValues);
						// }
						// cursorSummary.close();
						// getContentResolver().notifyChange(
						// DataProvider.Summary_Values.CONTENT_URI, null);

						// Setting all edittext empty:
						// m_et1.setText("");
						// m_et2.setText("");
						// m_et3.setText("");
						// m_et4.setText("");

						m_etWorkScope.setText("");
						m_etWorkScopeComments.setText("");
						spinner_conditions.setSelection(0);
						spinner_recommendations.setSelection(0);
						spinner_work_Carried_Out.setSelection(0);

						view_sample_task_page.setVisibility(View.GONE);
						ll_listviews.setVisibility(View.VISIBLE);
						bIsSampleTaskPageVisible = false;

					} else {
						showFieldMandatoryDialog();
					}
				}
			});

			btn_Close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					view_sample_task_page.setVisibility(View.GONE);
					ll_listviews.setVisibility(View.VISIBLE);
					bIsSampleTaskPageVisible = false;
					// Setting all edittext empty:
					// m_et1.setText("");
					// m_et2.setText("");
					// m_et3.setText("");
					// m_et4.setText("");
					m_etWorkScope.setText("");
					m_etWorkScopeComments.setText("");
					spinner_conditions.setSelection(0);
					spinner_recommendations.setSelection(0);
					spinner_work_Carried_Out.setSelection(0);
				}
			});
			// sample task page 2
			// {
			//
			// view_sample_task_page_2 = (View)
			// findViewById(R.id.view_sample_task_page_2);
			// m_et_task2_1 = (EditText) view_sample_task_page_2
			// .findViewById(R.id.et_1);
			// m_et_task2_2 = (EditText) view_sample_task_page_2
			// .findViewById(R.id.et_2);
			// m_et_task2_3 = (EditText) view_sample_task_page_2
			// .findViewById(R.id.et_3);
			//
			// btn_task2_cancel = (Button) view_sample_task_page_2
			// .findViewById(R.id.btn_cancel);
			// btn_task2_save = (Button) view_sample_task_page_2
			// .findViewById(R.id.btn_saveandcomplete);
			//
			// btn_task2_cancel.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// view_sample_task_page_2.setVisibility(View.GONE);
			// ll_listviews.setVisibility(View.VISIBLE);
			// bIsSampleTaskPage2Visible = false;
			// // Setting all edittext empty:
			// m_et_task2_1.setText("");
			// m_et_task2_2.setText("");
			// m_et_task2_3.setText("");
			//
			// view_sample_task_page_2.setVisibility(View.GONE);
			// ll_listviews.setVisibility(View.VISIBLE);
			// bIsSampleTaskPage2Visible = false;
			//
			// listview_services_second_gen
			// .setVisibility(View.VISIBLE);
			// ll_servicessecondgen.setVisibility(View.VISIBLE);
			// listview_services_main_gen.setVisibility(View.VISIBLE);
			// ll_servicesmain.setVisibility(View.VISIBLE);
			// }
			// });
			//
			// btn_task2_save.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// // Save code goes here-----------------
			//
			// // Setting all edittext empty:
			// m_et_task2_1.setText("");
			// m_et_task2_2.setText("");
			// m_et_task2_3.setText("");
			//
			// view_sample_task_page_2.setVisibility(View.GONE);
			// ll_listviews.setVisibility(View.VISIBLE);
			// bIsSampleTaskPage2Visible = false;
			//
			// listview_services_second_gen
			// .setVisibility(View.VISIBLE);
			// ll_servicessecondgen.setVisibility(View.VISIBLE);
			// listview_services_main_gen.setVisibility(View.VISIBLE);
			// ll_servicesmain.setVisibility(View.VISIBLE);
			// }
			// });
			//
			// }

		}

		ll_listviews = (LinearLayout) findViewById(R.id.ll_listviews);

		view_service_checklists_top_bar = (View) findViewById(R.id.view_service_checklists_top_bar);
		textview_title = (TextView) view_service_checklists_top_bar
				.findViewById(R.id.pageTitle);
		textview_title.setText("Service Checklist");

		// font implementation of main heading "GMD": textview
		textview_GMD_Heading = (TextView) findViewById(R.id.Heading);
		textview_GMD_Heading.setTypeface(GMDApplication.fontHeading);

		// ListView:
		listview_mill = (ListView) findViewById(R.id.listview_mill);
		listview_services_main_gen = (ListView) findViewById(R.id.services_maingen);
		listview_services_second_gen = (ListView) findViewById(R.id.listview_services_secondgen);
		listview_services_third_gen = (ListView) findViewById(R.id.listview_servies_thirdgen);
		listview_services_leaf = (ListView) findViewById(R.id.listview_servies_leaf);

		listview_mill.setDivider(null);
		listview_services_main_gen.setDivider(null);
		listview_services_second_gen.setDivider(null);
		listview_services_third_gen.setDivider(null);
		listview_services_leaf.setDivider(null);

		// report_listview = (ListView) findViewById(R.id.report_list_view);

		// report_listview.setAdapter(custom_Adaptor);
		// btn_report_a_problem = (Button) findViewById(R.id.reportproblemBtn);

		// List view
		// Seeing background color:
		listview_mill.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.service_checklist_list_bg));

		listview_mill.setOnItemClickListener(new OnItemClickListener() {

			private View lastSelectedView = null;

			public void clearSelection() {
				if (lastSelectedView != null) {
					LinearLayout linear_internal = (LinearLayout) lastSelectedView
							.findViewById(R.id.ll_internal);
					linear_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bglight));
				}
			}

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				clearSelection();
				lastSelectedView = view;

				nMillId = pos;
				LinearLayout ll_internal;
				ll_internal = (LinearLayout) view
						.findViewById(R.id.ll_internal);
				ll_internal.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.service_checklist_list_bgdark));

				// view.setBackground(getResources().getDrawable(
				// R.drawable.service_checklist_list_bgdark));

				m_szMillName = ((TextView) view
						.findViewById(R.id.textview_report_title)).getText()
						.toString();
				Util.setMillName(AppValues.AppContext, m_szMillName);
				mill_Name.setText(Util
						.getMillName(Service_CheckList_Without_Swipe.this));

				// listview_secondgen.setVisibility(View.VISIBLE);
				showListView(listview_services_main_gen);
				ll_servicesmain.setVisibility(View.VISIBLE);

				listview_services_second_gen.setVisibility(View.GONE);
				ll_servicessecondgen.setVisibility(View.GONE);
				listview_services_third_gen.setVisibility(View.GONE);
				ll_servicesthirdgen.setVisibility(View.GONE);
				listview_services_leaf.setVisibility(View.GONE);
				ll_servicesleaf.setVisibility(View.GONE);

				String szChildID = ((TextView) view
						.findViewById(R.id.textview_child_row_id)).getText()
						.toString();
				m_szMillID = szChildID;

				mCursorServicesMain = managedQuery(
						DataProvider.Services.CONTENT_URI, null,
						DataProvider.Services.MILL_ID + " = ?" + " AND "
								+ DataProvider.Services.REPORT_ID + " = ?"
								+ " AND " + DataProvider.Services.PARENT_ID
								+ " = ?", new String[] { m_szMillID,
								m_szReportID, "0" }, null);
				mCursorServicesMain.moveToFirst();
				startManagingCursor(mCursorServicesMain);
				mServicesMainAdapter = new SC_Services_Main_Adapter(
						Service_CheckList_Without_Swipe.this,
						mCursorServicesMain, true, m_szMillID);
				listview_services_main_gen.setAdapter(mServicesMainAdapter);
			}
		});

		listview_services_main_gen
				.setOnItemClickListener(new OnItemClickListener() {

					private View lastSelectedView = null;

					public void clearSelection() {
						if (lastSelectedView != null) {
							LinearLayout linear_internal = (LinearLayout) lastSelectedView
									.findViewById(R.id.ll_internal);
							linear_internal
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.service_checklist_list_bglight));
						}
					}

					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int pos, long id) {
						// TODO Auto-generated method stub

						clearSelection();
						lastSelectedView = view;

						nServicesMainID = pos;
						LinearLayout ll_internal;
						ll_internal = (LinearLayout) view
								.findViewById(R.id.ll_internal);
						ll_internal
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.service_checklist_list_bgdark));

						// view.setBackground(getResources().getDrawable(
						// R.drawable.service_checklist_list_bgdark));

						m_szServicesMainItem = ((TextView) view
								.findViewById(R.id.textview_report_title))
								.getText().toString();

						listview_mill.setVisibility(View.GONE);

						// listview_services_main_gen
						// .setAnimation(outToLeftAnimation());
						// listview_thirdgen.setVisibility(View.VISIBLE);
						showListView(listview_services_second_gen);
						ll_servicessecondgen.setVisibility(View.VISIBLE);
						listview_services_third_gen.setVisibility(View.GONE);
						ll_servicesthirdgen.setVisibility(View.GONE);
						listview_services_leaf.setVisibility(View.GONE);
						ll_servicesleaf.setVisibility(View.GONE);

						String szChildRowID = ((TextView) view
								.findViewById(R.id.textview_child_row_id))
								.getText().toString();
						m_szServicesMainItemID = szChildRowID;

						mCursorServicesSecondGen = managedQuery(
								DataProvider.Services.CONTENT_URI, null,
								DataProvider.Services.MILL_ID + " = ?"
										+ " AND "
										+ DataProvider.Services.REPORT_ID
										+ " = ?" + " AND "
										+ DataProvider.Services.PARENT_ID
										+ " = ?", new String[] { m_szMillID,
										m_szReportID, m_szServicesMainItemID },
								null);
						mCursorServicesSecondGen.moveToFirst();
						startManagingCursor(mCursorServicesSecondGen);
						mServicesSecondGenAdapter = new SC_Services_SecondGen_Adapter(
								Service_CheckList_Without_Swipe.this,
								mCursorServicesSecondGen, true, m_szMillID);
						listview_services_second_gen
								.setAdapter(mServicesSecondGenAdapter);
					}
				});

		listview_services_second_gen
				.setOnItemClickListener(new OnItemClickListener() {

					private View lastSelectedView = null;

					public void clearSelection() {
						if (lastSelectedView != null) {
							LinearLayout linear_internal = (LinearLayout) lastSelectedView
									.findViewById(R.id.ll_internal);
							linear_internal
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.service_checklist_list_bglight));
						}
					}

					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int pos, long id) {
						// TODO Auto-generated method stub

						clearSelection();
						lastSelectedView = view;

						nServicesSecondGenID = pos;
						LinearLayout ll_internal;
						ll_internal = (LinearLayout) view
								.findViewById(R.id.ll_internal);
						ll_internal
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.service_checklist_list_bgdark));
						// view.setBackground(getResources().getDrawable(
						// R.drawable.service_checklist_list_bgdark));

						m_szServicesSecondGenItem = ((TextView) view
								.findViewById(R.id.textview_report_title))
								.getText().toString();

						listview_mill.setVisibility(View.GONE);
						listview_services_main_gen.setVisibility(View.GONE);
						ll_servicesmain.setVisibility(View.GONE);

						// listview_services_second_gen
						// .setAnimation(outToLeftAnimation());
						// listview_leaf.setVisibility(View.VISIBLE);
						showListView(listview_services_third_gen);
						ll_servicesthirdgen.setVisibility(View.VISIBLE);
						listview_services_leaf.setVisibility(View.GONE);
						ll_servicesleaf.setVisibility(View.GONE);

						String szChildRowID = ((TextView) view
								.findViewById(R.id.textview_child_row_id))
								.getText().toString();
						m_szServicesSecondGenItemID = szChildRowID;

						mCursorServicesThirdGen = managedQuery(
								DataProvider.Services.CONTENT_URI, null,
								DataProvider.Services.MILL_ID + " = ?"
										+ " AND "
										+ DataProvider.Services.REPORT_ID
										+ " = ?" + " AND "
										+ DataProvider.Services.PARENT_ID
										+ " = ?", new String[] { m_szMillID,
										m_szReportID,
										m_szServicesSecondGenItemID }, null);
						mCursorServicesThirdGen.moveToFirst();
						startManagingCursor(mCursorServicesThirdGen);
						mServicesThirdGenAdapter = new SC_Services_ThirdGen_Adapter(
								Service_CheckList_Without_Swipe.this,
								mCursorServicesThirdGen, true, m_szMillID);
						listview_services_third_gen
								.setAdapter(mServicesThirdGenAdapter);

					}
				});

		listview_services_third_gen
				.setOnItemClickListener(new OnItemClickListener() {

					private View lastSelectedView = null;

					public void clearSelection() {
						if (lastSelectedView != null) {
							LinearLayout linear_internal = (LinearLayout) lastSelectedView
									.findViewById(R.id.ll_internal);
							linear_internal
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.service_checklist_list_bglight));
						}
					}

					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int pos, long id) {
						// TODO Auto-generated method stub

						clearSelection();
						lastSelectedView = view;

						nServicesThirdGenID = pos;
						LinearLayout ll_internal;
						ll_internal = (LinearLayout) view
								.findViewById(R.id.ll_internal);
						ll_internal
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.service_checklist_list_bgdark));
						// view.setBackground(getResources().getDrawable(
						// R.drawable.service_checklist_list_bgdark));

						m_szServicesThirdGenItem = ((TextView) view
								.findViewById(R.id.textview_report_title))
								.getText().toString();

						listview_mill.setVisibility(View.GONE);
						listview_services_main_gen.setVisibility(View.GONE);
						ll_servicesmain.setVisibility(View.GONE);
						listview_services_second_gen.setVisibility(View.GONE);
						ll_servicessecondgen.setVisibility(View.GONE);

						// listview_services_third_gen
						// .setAnimation(outToLeftAnimation());
						// listview_leaf.setVisibility(View.VISIBLE);
						showListView(listview_services_leaf);
						ll_servicesleaf.setVisibility(View.VISIBLE);

						String szChildRowID = ((TextView) view
								.findViewById(R.id.textview_child_row_id))
								.getText().toString();
						m_szServicesThirdGenItemID = szChildRowID;

						Cursor taskCursor = getContentResolver().query(
								DataProvider.Tasks.CONTENT_URI,
								null,
								DataProvider.Tasks.MILL_ID + " = ?" + " AND "
										+ DataProvider.Tasks.REPORT_ID + " = ?"
										+ " AND "
										+ DataProvider.Tasks.SERVICES_ITEM_ID
										+ " = ? AND "
										+ DataProvider.Tasks.TASK_CONTENT
										+ " = ?",
								new String[] { m_szMillID, m_szReportID,
										m_szServicesThirdGenItemID,
										"Special Type" }, null);

						boolean bIsContainSpecialType = false;
						String szSpecialTypeName = "";
						if (taskCursor.moveToFirst()) {
							bIsContainSpecialType = true;
							szSpecialTypeName = taskCursor.getString(taskCursor
									.getColumnIndex(DataProvider.Tasks.TASK_NAME));
						} else {
							bIsContainSpecialType = false;
							szSpecialTypeName = "";
						}
						taskCursor.close();

						String[] selectionargs;
						String szSelection = null;

						if (!bIsContainSpecialType) {
							szSelection = DataProvider.Child_Leaf.MILL_ID
									+ " = ?" + " AND "
									+ DataProvider.Child_Leaf.REPORT_ID
									+ " = ? AND "
									+ DataProvider.Child_Leaf.CHILD_LEAF
									+ " != ?";
							selectionargs = new String[] { m_szMillID,
									m_szReportID, "Special Type" };
						} else {
							szSelection = DataProvider.Child_Leaf.MILL_ID
									+ " = ?" + " AND "
									+ DataProvider.Child_Leaf.REPORT_ID
									+ " = ?";
							selectionargs = new String[] { m_szMillID,
									m_szReportID };
						}

						mCursorServicesLeaf = managedQuery(
								DataProvider.Child_Leaf.CONTENT_URI, null,
								szSelection, selectionargs, null);

						mCursorServicesLeaf.moveToFirst();
						startManagingCursor(mCursorServicesLeaf);
						mServicesLeafAdapter = new SC_Services_Leaf_Adapter(
								Service_CheckList_Without_Swipe.this,
								mCursorServicesLeaf, true,
								m_szServicesThirdGenItemID, m_szMillID,
								szSpecialTypeName);
						listview_services_leaf.setAdapter(mServicesLeafAdapter);
					}
				});

		listview_services_leaf
				.setOnItemClickListener(new OnItemClickListener() {

					private View lastSelectedView = null;

					public void clearSelection() {
						if (lastSelectedView != null) {
							LinearLayout linear_internal = (LinearLayout) lastSelectedView
									.findViewById(R.id.ll_internal);
							linear_internal
									.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.service_checklist_list_bglight));
						}
					}

					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int pos, long id) {
						// TODO Auto-generated method stub

						clearSelection();
						lastSelectedView = view;

						nServicesLeafID = pos;
						LinearLayout ll_internal;
						ll_internal = (LinearLayout) view
								.findViewById(R.id.ll_internal);
						ll_internal
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.service_checklist_list_bgdark));

						TextView textview_child = (TextView) view
								.findViewById(R.id.textview_report_title);
						m_szCurrentLeafChild = textview_child.getText()
								.toString();
						ImageView imageview_color = (ImageView) view
								.findViewById(R.id.imageview_color_id);
						if (m_szCurrentLeafChild.equalsIgnoreCase("Work scope")
								|| m_szCurrentLeafChild
										.equalsIgnoreCase("Photograph")
								|| m_szCurrentLeafChild
										.equalsIgnoreCase("Help-Support")) {
							// Help/Support - 6
							// Workscope - 0
							// Photograph - 5
							imageview_color
									.setBackgroundDrawable(getResources()
											.getDrawable(R.drawable.green));

							ContentValues taskValues = new ContentValues();
							taskValues.put(DataProvider.Tasks.TASK_COMPLETED,
									"true");
							getContentResolver()
									.update(DataProvider.Tasks.CONTENT_URI,
											taskValues,
											DataProvider.Tasks.MILL_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.REPORT_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.SERVICES_ITEM_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.TASK_NAME
													+ " = ?",
											new String[] { m_szMillID,
													m_szReportID,
													m_szServicesThirdGenItemID,
													m_szCurrentLeafChild });

							getContentResolver().notifyChange(
									DataProvider.Tasks.CONTENT_URI, null);

						}

						// view.setBackground(getResources().getDrawable(
						// R.drawable.service_checklist_list_bgdark));

						TextView txtHeading = (TextView) view_sample_task_page
								.findViewById(R.id.page_heading_middle);

						m_szServicesLeafItem = ((TextView) view
								.findViewById(R.id.textview_report_title))
								.getText().toString();

						String szChildRowID = ((TextView) view
								.findViewById(R.id.textview_child_row_id))
								.getText().toString();
						m_szServicesLeafItemID = szChildRowID;

						if (m_szServicesLeafItem.equals("Report a problem")) {
							// Intent myIntent = new Intent(getBaseContext(),
							// Report_A_Problem.class);
							Intent myIntent = new Intent(getBaseContext(),
									Report_A_Problem_ReviewImage.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesMainItemID);
							myIntent.putExtra("thirdgenitemid",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem.equals("Help-Support")) {
							HelpandSupport();
						} else if (m_szServicesLeafItem.equals("Photograph")) {
							// open activity to choose from gallery or camera
							Intent photoIntent = new Intent(getBaseContext(),
									LeafPhotograph.class);
							photoIntent.putExtra("thirdgenitemid",
									m_szServicesThirdGenItemID);
							startActivity(photoIntent);

						} else if (m_szServicesLeafItem.equals("Air Gap")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Air_Gap.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem.equals("Sole Plate")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Sole_Plate.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem.equals("Bolt Torque")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Bolt_Torque.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Core Partitions")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Core_Partitions.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Pressfinger Feeder")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Pressfinger_feeder.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Pressfinger Discharge")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Pressfinger_Discharge.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem.equals("Key Bars")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Key_Bars.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Holding Plates")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Holding_Plates.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Bolt Insulation")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Bolt_Insulation.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem
								.equals("Magnetic Centering")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Magnetic_Centering.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else if (m_szServicesLeafItem.equals("Brush Wear")) {
							Intent myIntent = new Intent(getBaseContext(),
									Activity_Brushwear.class);
							myIntent.putExtra("millid", m_szMillID);
							myIntent.putExtra("parent_id",
									m_szServicesThirdGenItemID);
							myIntent.putExtra("id", m_szServicesSecondGenItemID);
							startActivity(myIntent);
						} else {
							view_sample_task_page.setVisibility(View.VISIBLE);
							ll_listviews.setVisibility(View.GONE);
							bIsSampleTaskPageVisible = true;

							Cursor taskCursor = getContentResolver()
									.query(DataProvider.Tasks.CONTENT_URI,
											null,
											DataProvider.Tasks.MILL_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.REPORT_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.SERVICES_ITEM_ID
													+ " = ?"
													+ " AND "
													+ DataProvider.Tasks.TASK_NAME
													+ " = ?",
											new String[] { m_szMillID,
													m_szReportID,
													m_szServicesThirdGenItemID,
													m_szCurrentLeafChild },
											null);
							String szTaskContent = "";
							String szTaskComment = "";
							if (taskCursor != null && taskCursor.moveToFirst()) {
								szTaskContent = taskCursor.getString(taskCursor
										.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
								szTaskComment = taskCursor.getString(taskCursor
										.getColumnIndex(DataProvider.Tasks.TASK_COMMENT));
							}
							taskCursor.close();

							m_etWorkScope.setText(szTaskContent);
							m_etWorkScopeComments.setText(szTaskComment);
							m_etWorkScope.setEnabled(true);
							btn_saveandcomplete.setEnabled(true);

							if (m_szCurrentLeafChild
									.equalsIgnoreCase("Work scope")) {
								txtHeading.setText(m_szServicesSecondGenItem
										+ ": " + m_szServicesThirdGenItem);
								ll_workscope.setVisibility(View.VISIBLE);
								imageLoader.displayImage("file://"
										+ AppValues
												.getServiceCheckListDirectory()
												.getAbsolutePath()
										+ File.separator + m_szServicesMainItem
										+ File.separator
										+ m_szServicesSecondGenItem
										+ File.separator
										+ m_szServicesThirdGenItem
										+ File.separator + m_szServicesLeafItem
										+ File.separator
										+ m_szServicesThirdGenItem + ".jpg",
										imageview_workscope_part, options);
								ll_workscope_comments
										.setVisibility(View.VISIBLE);
								btn_workscope_close.setVisibility(View.VISIBLE);
								imageview_workscope_part
										.setVisibility(View.VISIBLE);
								ll_cancel.setVisibility(View.GONE);
								ll_work_Carried_out.setVisibility(View.GONE);
								ll_recommendations.setVisibility(View.GONE);
								ll_conditions.setVisibility(View.GONE);
								m_etWorkScope.setEnabled(false);
								btn_saveandcomplete.setEnabled(false);
							} else if (m_szCurrentLeafChild
									.equalsIgnoreCase("Work carried out")) {
								ll_cancel.setVisibility(View.VISIBLE);
								txtHeading.setText("Work carried out");
								if (szTaskContent != null) {
									for (int i = 0; i < work_Carried_Out_Spinner_Adapter
											.getCount(); i++) {
										if (szTaskContent
												.equals(work_Carried_Out_Spinner_Adapter
														.getItem(i).toString())) {
											spinner_work_Carried_Out
													.setSelection(i);
											break;
										}
									}
								}
								ll_workscope.setVisibility(View.GONE);
								ll_workscope_comments.setVisibility(View.GONE);
								ll_work_Carried_out.setVisibility(View.VISIBLE);
								ll_recommendations.setVisibility(View.GONE);
								ll_conditions.setVisibility(View.GONE);
							} else if (m_szCurrentLeafChild
									.equalsIgnoreCase("Conditions")) {
								ll_cancel.setVisibility(View.VISIBLE);
								txtHeading.setText("Conditions");
								if (szTaskContent != null) {
									for (int i = 0; i < conditions_Spinner_Adapter
											.getCount(); i++) {
										if (szTaskContent
												.equals(conditions_Spinner_Adapter
														.getItem(i).toString())) {
											spinner_conditions.setSelection(i);
											break;
										}
									}
								}
								ll_workscope.setVisibility(View.GONE);
								ll_workscope_comments.setVisibility(View.GONE);
								ll_work_Carried_out.setVisibility(View.GONE);
								ll_recommendations.setVisibility(View.GONE);
								ll_conditions.setVisibility(View.VISIBLE);
							} else if (m_szCurrentLeafChild
									.equalsIgnoreCase("Recommendations")) {
								ll_cancel.setVisibility(View.VISIBLE);
								txtHeading.setText("Recommendations");
								if (szTaskContent != null) {
									for (int i = 0; i < recommendations_Spinner_Adapter
											.getCount(); i++) {
										if (szTaskContent
												.equals(recommendations_Spinner_Adapter
														.getItem(i).toString())) {
											spinner_recommendations
													.setSelection(i);
											break;
										}
									}
								}
								ll_workscope.setVisibility(View.GONE);
								ll_workscope_comments.setVisibility(View.GONE);
								ll_work_Carried_out.setVisibility(View.GONE);
								ll_recommendations.setVisibility(View.VISIBLE);
								ll_conditions.setVisibility(View.GONE);
							} else if (m_szCurrentLeafChild.equals("Comments")) {
								view_sample_task_page
										.setVisibility(View.VISIBLE);
								ll_listviews.setVisibility(View.GONE);
								bIsSampleTaskPageVisible = true;
								ll_cancel.setVisibility(View.VISIBLE);
								txtHeading.setText("Comments");
								ll_workscope.setVisibility(View.VISIBLE);
								ll_workscope_comments.setVisibility(View.GONE);
								btn_workscope_close.setVisibility(View.GONE);
								imageview_workscope_part
										.setVisibility(View.GONE);
								ll_work_Carried_out.setVisibility(View.GONE);
								ll_recommendations.setVisibility(View.GONE);
								ll_conditions.setVisibility(View.GONE);
							}
							// else if (nServicesLeafID == 4) { // Comments
							// ll_cancel.setVisibility(View.VISIBLE);
							// txtHeading.setText("Comments");
							// ll_workscope.setVisibility(View.VISIBLE);
							// btn_workscope_close.setVisibility(View.GONE);
							// imageview_workscope_part
							// .setVisibility(View.GONE);
							// ll_work_Carried_out.setVisibility(View.GONE);
							// ll_recommendations.setVisibility(View.GONE);
							// ll_conditions.setVisibility(View.GONE);
							// }
						}

						// String szSelection =
						// DataProvider.Summary_Values.CHILD_LEAF_NODE_ID
						// + " ='"
						// + m_szServicesThirdGenItemID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_MAIN_ID
						// + " ='"
						// + m_szMillID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
						// + " ='"
						// + m_szServicesMainItemID
						// + "' AND "
						// + DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
						// + " ='"
						// + m_szServicesSecondGenItemID
						// + "' AND "
						// + DataProvider.Summary_Values.REPORT_ID
						// + " ='"
						// + Util.getReportID(Service_CheckList_New.this)
						// + "'";
						//
						// Cursor cursorSummary = getContentResolver().query(
						// DataProvider.Summary_Values.CONTENT_URI, null,
						// szSelection, null, null);
						// if (cursorSummary != null
						// && cursorSummary.moveToFirst()) {
						//
						// try {
						// String[] szCommentArray = cursorSummary
						// .getString(
						// cursorSummary
						// .getColumnIndex(DataProvider.Summary_Values.CHILD))
						// .split(",");
						// m_et1.setText(szCommentArray[0]);
						// m_et2.setText(szCommentArray[1]);
						// m_et3.setText(szCommentArray[2]);
						// m_et4.setText(szCommentArray[3]);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						// cursorSummary.close();

					}
				});

		ll_servicesmain = (LinearLayout) findViewById(R.id.ll_services_maingen);
		ll_servicesmain_back = (LinearLayout) findViewById(R.id.ll_services_maingen_back);
		ll_servicessecondgen = (LinearLayout) findViewById(R.id.ll_services_secondgen);
		ll_servicessecondgen_back = (LinearLayout) findViewById(R.id.ll_services_secondgen_back);
		ll_servicesthirdgen = (LinearLayout) findViewById(R.id.ll_services_thirdgen);
		ll_servicesthirdgen_back = (LinearLayout) findViewById(R.id.ll_services_thirdgen_back);
		ll_servicesleaf = (LinearLayout) findViewById(R.id.ll_services_leaf);
		ll_servicesleaf_back = (LinearLayout) findViewById(R.id.ll_services_leaf_back);

		ll_servicesmain_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHideListViews();
				// listview_mill.setVisibility(View.VISIBLE);
				// if (ll_servicessecondgen.getVisibility() == View.VISIBLE)
				// ll_servicesmain.setVisibility(View.VISIBLE);
				// else
				// ll_servicesmain.setVisibility(View.GONE);
				// ll_servicessecondgen.setVisibility(View.GONE);
				// ll_servicesthirdgen.setVisibility(View.GONE);
				// ll_servicesleaf.setVisibility(View.GONE);
				// // listview_mill.setVisibility(View.VISIBLE);
				// // ll_servicesmain.setVisibility(View.GONE);
				// // listview_services_main_gen.setVisibility(View.GONE);
				// // ll_servicessecondgen.setVisibility(View.GONE);
				// // listview_services_second_gen.setVisibility(View.GONE);
				// // ll_servicesthirdgen.setVisibility(View.GONE);
				// // listview_services_third_gen.setVisibility(View.GONE);
				// // ll_servicesleaf.setVisibility(View.GONE);
				// // listview_services_leaf.setVisibility(View.GONE);
			}
		});

		ll_servicessecondgen_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showHideListViews();
				// listview_mill.setVisibility(View.VISIBLE);
				// ll_servicesmain.setVisibility(View.VISIBLE);
				// listview_services_main_gen.setVisibility(View.VISIBLE);
				// ll_servicessecondgen.setVisibility(View.GONE);
				// ll_servicesthirdgen.setVisibility(View.GONE);
				// ll_servicesleaf.setVisibility(View.GONE);
				// // TODO Auto-generated method stub
				// // listview_mill.setVisibility(View.VISIBLE);
				// // ll_servicesmain.setVisibility(View.VISIBLE);
				// // listview_services_main_gen.setVisibility(View.VISIBLE);
				// // ll_servicessecondgen.setVisibility(View.GONE);
				// // listview_services_second_gen.setVisibility(View.GONE);
				// // ll_servicesthirdgen.setVisibility(View.GONE);
				// // listview_services_third_gen.setVisibility(View.GONE);
				// // ll_servicesleaf.setVisibility(View.GONE);
				// // listview_services_leaf.setVisibility(View.GONE);
			}
		});

		ll_servicesthirdgen_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHideListViews();
				// listview_mill.setVisibility(View.VISIBLE);
				// ll_servicesmain.setVisibility(View.VISIBLE);
				// listview_services_main_gen.setVisibility(View.VISIBLE);
				// ll_servicessecondgen.setVisibility(View.GONE);
				// ll_servicesthirdgen.setVisibility(View.GONE);
				// ll_servicesleaf.setVisibility(View.GONE);
				// // listview_mill.setVisibility(View.GONE);
				// // ll_servicessecondgen.setVisibility(View.VISIBLE);
				// // listview_services_second_gen.setVisibility(View.VISIBLE);
				// // ll_servicesmain.setVisibility(View.VISIBLE);
				// // listview_services_main_gen.setVisibility(View.VISIBLE);
				// // ll_servicesthirdgen.setVisibility(View.GONE);
				// // listview_services_third_gen.setVisibility(View.GONE);
				// // ll_servicesleaf.setVisibility(View.GONE);
				// // listview_services_leaf.setVisibility(View.GONE);
			}
		});

		ll_servicesleaf_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHideListViews();
				// listview_mill.setVisibility(View.VISIBLE);
				// ll_servicesmain.setVisibility(View.VISIBLE);
				// listview_services_main_gen.setVisibility(View.VISIBLE);
				// ll_servicessecondgen.setVisibility(View.GONE);
				// ll_servicesthirdgen.setVisibility(View.GONE);
				// ll_servicesleaf.setVisibility(View.GONE);
				// // listview_mill.setVisibility(View.GONE);
				// // ll_servicesmain.setVisibility(View.GONE);
				// // listview_services_main_gen.setVisibility(View.GONE);
				// // ll_servicessecondgen.setVisibility(View.VISIBLE);
				// // listview_services_second_gen.setVisibility(View.VISIBLE);
				// // ll_servicesthirdgen.setVisibility(View.VISIBLE);
				// // listview_services_third_gen.setVisibility(View.VISIBLE);
				// // ll_servicesleaf.setVisibility(View.GONE);
				// // listview_services_leaf.setVisibility(View.GONE);
			}
		});

		populateData();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				m_szReportID = Util
						.getReportID(Service_CheckList_Without_Swipe.this);

				mCursorMill.requery();
				listview_mill.setVisibility(View.VISIBLE);
				// populateData();

				listview_services_leaf.setVisibility(View.GONE);
				ll_servicesleaf.setVisibility(View.GONE);
				listview_services_third_gen.setVisibility(View.GONE);
				ll_servicesthirdgen.setVisibility(View.GONE);
				listview_services_second_gen.setVisibility(View.GONE);
				ll_servicessecondgen.setVisibility(View.GONE);
				listview_services_main_gen.setVisibility(View.GONE);
				ll_servicesmain.setVisibility(View.GONE);
			}
		};

	}

	void populateData() {
		mCursorMill = managedQuery(DataProvider.Mill.CONTENT_URI, null, null,
				null, null);
		mCursorMill.moveToFirst();
		startManagingCursor(mCursorMill);
		mMillAdapter = new SC_Mill_Adapter(
				Service_CheckList_Without_Swipe.this, mCursorMill, true);
		listview_mill.setAdapter(mMillAdapter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!showHideListViews())
			showQuitAlert();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		// Condition to check for all completed tasks
		Cursor taskCursor = getContentResolver().query(
				DataProvider.Tasks.CONTENT_URI,
				null,
				DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
						+ DataProvider.Tasks.TASK_COMPLETED + " = ?",
				new String[] { m_szReportID, "false" }, null);
		taskCursor.moveToFirst();
		if (taskCursor.getCount() == 0
				&& Util.getIsReportXMLDownloaded(Service_CheckList_Without_Swipe.this)) {
			// Toast.makeText(
			// Service_CheckList_Without_Swipe.this,
			// "All tasks completed. You can sync with server to upload report",
			// Toast.LENGTH_LONG).show();
			if (MainActivity.mHandler != null)
				MainActivity.mHandler.sendEmptyMessage(777);

			// CreateServiceCheckListXMLTask task = new
			// CreateServiceCheckListXMLTask();
			// task.mContext = Service_CheckList_Without_Swipe.this;
			// task.execute();
		}
		taskCursor.close();

		dataObserver = new DataObserver(new Handler());
		getContentResolver().registerContentObserver(
				DataProvider.Tasks.CONTENT_URI, true, dataObserver);
	}

	boolean showHideListViews() {
		if (bIsSampleTaskPageVisible) {
			// m_et1.setText("");
			// m_et2.setText("");
			// m_et3.setText("");
			// m_et4.setText("");
			m_etWorkScope.setText("");
			view_sample_task_page.setVisibility(View.GONE);
			ll_listviews.setVisibility(View.VISIBLE);
			bIsSampleTaskPageVisible = false;
			return true;
		} else if (listview_services_third_gen.getVisibility() == View.VISIBLE
				&& listview_services_leaf.getVisibility() == View.VISIBLE) {
			listview_services_third_gen.setVisibility(View.VISIBLE);
			ll_servicesthirdgen.setVisibility(View.VISIBLE);
			listview_services_second_gen.setVisibility(View.VISIBLE);
			ll_servicessecondgen.setVisibility(View.VISIBLE);
			listview_services_leaf.setVisibility(View.GONE);
			ll_servicesleaf.setVisibility(View.GONE);
			listview_services_main_gen.setVisibility(View.GONE);
			ll_servicesmain.setVisibility(View.GONE);
			listview_mill.setVisibility(View.GONE);
			return true;
		} else if (listview_services_second_gen.getVisibility() == View.VISIBLE
				&& listview_services_third_gen.getVisibility() == View.VISIBLE) {
			listview_services_third_gen.setVisibility(View.GONE);
			ll_servicesthirdgen.setVisibility(View.GONE);
			listview_services_leaf.setVisibility(View.GONE);
			ll_servicesleaf.setVisibility(View.GONE);
			listview_mill.setVisibility(View.GONE);
			listview_services_main_gen.setVisibility(View.VISIBLE);
			ll_servicesmain.setVisibility(View.VISIBLE);
			listview_services_second_gen.setVisibility(View.VISIBLE);
			ll_servicessecondgen.setVisibility(View.VISIBLE);
			return true;
		} else if (listview_services_main_gen.getVisibility() == View.VISIBLE
				&& listview_services_second_gen.getVisibility() == View.VISIBLE) {
			listview_mill.setVisibility(View.VISIBLE);
			listview_services_main_gen.setVisibility(View.VISIBLE);
			ll_servicesmain.setVisibility(View.VISIBLE);
			listview_services_second_gen.setVisibility(View.GONE);
			ll_servicessecondgen.setVisibility(View.GONE);
			listview_services_third_gen.setVisibility(View.GONE);
			ll_servicesthirdgen.setVisibility(View.GONE);
			listview_services_leaf.setVisibility(View.GONE);
			ll_servicesleaf.setVisibility(View.GONE);
			return true;
		} else if (listview_mill.getVisibility() == View.VISIBLE
				&& listview_services_main_gen.getVisibility() == View.VISIBLE) {
			listview_services_main_gen.setVisibility(View.GONE);
			ll_servicesmain.setVisibility(View.GONE);
			listview_services_second_gen.setVisibility(View.GONE);
			ll_servicessecondgen.setVisibility(View.GONE);
			listview_services_third_gen.setVisibility(View.GONE);
			ll_servicesthirdgen.setVisibility(View.GONE);
			listview_services_leaf.setVisibility(View.GONE);
			ll_servicesleaf.setVisibility(View.GONE);
			listview_mill.setVisibility(View.VISIBLE);
			return true;
		}
		// else if (bIsSampleTaskPage2Visible) {
		// bIsSampleTaskPage2Visible = false;
		// view_sample_task_page_2.setVisibility(View.GONE);
		// ll_listviews.setVisibility(View.VISIBLE);
		// return true;
		// }
		return false;
	}

	class DataObserver extends ContentObserver {
		public DataObserver(Handler handler) {
			super(handler);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return false;
		}

		@Override
		public void onChange(boolean arg0) {
			super.onChange(arg0);
			Log.d("DataObserver-OnChange()", "DataObserver OnChange");

			Util.setServiceCheckListCreated(
					Service_CheckList_Without_Swipe.this, false);

			CreateServiceCheckListXMLTask task = new CreateServiceCheckListXMLTask();
			task.mContext = Service_CheckList_Without_Swipe.this;
			task.execute();

			// Condition to check for all completed tasks
			Cursor taskCursor = getContentResolver().query(
					DataProvider.Tasks.CONTENT_URI,
					null,
					DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
							+ DataProvider.Tasks.TASK_COMPLETED + " = ?",
					new String[] { m_szReportID, "false" }, null);
			taskCursor.moveToFirst();
			if (taskCursor.getCount() == 0
					&& Util.getIsReportXMLDownloaded(Service_CheckList_Without_Swipe.this)) {
				// Toast.makeText(
				// Service_CheckList_Without_Swipe.this,
				// "All tasks completed. You can sync with server to upload report",
				// Toast.LENGTH_LONG).show();
				if (MainActivity.mHandler != null)
					MainActivity.mHandler.sendEmptyMessage(777);

				// CreateServiceCheckListXMLTask task = new
				// CreateServiceCheckListXMLTask();
				// task.mContext = Service_CheckList_Without_Swipe.this;
				// task.execute();
			}
			taskCursor.close();

			try {
				mCursorMill.requery();
				mCursorServicesMain.requery();
				mCursorServicesSecondGen.requery();
				mCursorServicesThirdGen.requery();
				mCursorServicesLeaf.requery();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.gc();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dataObserver != null)
			getContentResolver().unregisterContentObserver(dataObserver);
	}

	void HelpandSupport() {
		final Dialog dialog = new Dialog(Service_CheckList_Without_Swipe.this);
		dialog.setContentView(R.layout.help_popup);
		dialog.setTitle("Please select an option below");
		dialog.setCancelable(true);

		// set up button::PDF Button
		Button helpPopUpPDFButton = (Button) dialog
				.findViewById(R.id.helpPopUpPDFButton);
		helpPopUpPDFButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// File file = new File(AppValues.getDirectory(),
				// "GMD/Help/Pdf/gmd.pdf");

				File file = new File(AppValues.getServiceCheckListDirectory()
						.getAbsolutePath()
						+ File.separator
						+ m_szServicesMainItem
						+ File.separator
						+ m_szServicesSecondGenItem
						+ File.separator
						+ m_szServicesThirdGenItem
						+ File.separator
						+ m_szServicesLeafItem
						+ File.separator
						+ m_szServicesThirdGenItem + ".pdf");

				if (file.exists()) {
					// Do action
					dialog.dismiss();
					readPDFFromSDCard(file.getAbsolutePath());
				} else {
					dialog.dismiss();
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							Service_CheckList_Without_Swipe.this)
							.setMessage(
									"The required file is not present on the SD card, please contact the system admin")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {

										}
									}).create();
					m_AlertDialog.show();
				}
				// CopyReadAssets();

			}
		});

		// // set up button::Video Button
		Button helpTutorialVideoButton = (Button) dialog
				.findViewById(R.id.helpTutorialVideoButton);
		helpTutorialVideoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// File file = new File(AppValues.getDirectory(),
				// "GMD/Help/Video/gmd.mp4");
				// File fileTxt = new File(AppValues.getDirectory(),
				// "GMD/Help/Video/pausePoints.txt");

				File fileVideo = new File(AppValues
						.getServiceCheckListDirectory().getAbsolutePath()
						+ File.separator
						+ m_szServicesMainItem
						+ File.separator
						+ m_szServicesSecondGenItem
						+ File.separator
						+ m_szServicesThirdGenItem
						+ File.separator
						+ m_szServicesLeafItem
						+ File.separator + m_szServicesThirdGenItem + ".mp4");

				File fileTxt = new File(AppValues
						.getServiceCheckListDirectory().getAbsolutePath()
						+ File.separator
						+ m_szServicesMainItem
						+ File.separator
						+ m_szServicesSecondGenItem
						+ File.separator
						+ m_szServicesThirdGenItem
						+ File.separator
						+ m_szServicesLeafItem
						+ File.separator + m_szServicesThirdGenItem + ".txt");

				if (fileVideo.exists() && fileTxt.exists()) {
					// Do action
					dialog.dismiss();
					playVideoFromSDCard(fileVideo.getAbsolutePath(),
							fileTxt.getAbsolutePath());
				} else {
					dialog.dismiss();
					AlertDialog m_AlertDialog = new AlertDialog.Builder(
							Service_CheckList_Without_Swipe.this)
							.setMessage(
									"The required file is not present on the SD card, please contact the system admin")
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {

										}
									}).create();
					m_AlertDialog.show();
				}

			}
		});

		// // set up button:Library Button
		Button helpLibraryButton = (Button) dialog
				.findViewById(R.id.helpLibraryButton);
		helpLibraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// File filePath = new File(Environment
				// .getExternalStorageDirectory().getAbsolutePath() + "/");
				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent.setDataAndType(
				// Uri.parse("file:/" + filePath.getAbsolutePath()),
				// "file/*");
				// startActivityForResult(intent, PICKFILE_RESULT_CODE);
				// dialog.dismiss();

				mLibPath = new File(AppValues.getServiceCheckListDirectory()
						.getAbsolutePath()
						+ File.separator
						+ m_szServicesMainItem
						+ File.separator
						+ m_szServicesSecondGenItem
						+ File.separator
						+ m_szServicesThirdGenItem
						+ File.separator
						+ m_szServicesLeafItem).getAbsolutePath();

				// Log.e("mLibPath", mLibPath);

				Intent intent = new Intent(
						Service_CheckList_Without_Swipe.this,
						HelpSupportFiles.class);
				intent.putExtra(Common.EXTRA_MESSAGE, mLibPath);
				startActivity(intent);

				dialog.dismiss();

			}
		});
		// now that the dialog is set up, it's time to show it
		dialog.show();
	}

	private void playVideoFromSDCard(String videoPath, String textPath) {
		Intent myIntent1 = new Intent(Service_CheckList_Without_Swipe.this,
				VideoPlayer.class);
		myIntent1.putExtra("video_path", videoPath);
		myIntent1.putExtra("text_path", textPath);
		startActivity(myIntent1);
	}

	private void readPDFFromSDCard(String pdfpath) {

		// File file = new File(AppValues.getDirectory() + "/" + pdfpath);
		File file = new File(pdfpath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);

	}

	// private void CopyReadAssets() {
	// AssetManager assetManager = getAssets();
	//
	// InputStream in = null;
	// OutputStream out = null;
	// File file = new File(getFilesDir(), "gmd.pdf");
	// try {
	// in = assetManager.open("gmd.pdf");
	// out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
	//
	// copyFile(in, out);
	// in.close();
	// in = null;
	// out.flush();
	// out.close();
	// out = null;
	// } catch (Exception e) {
	// Log.e("tag", e.getMessage());
	// }
	//
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setDataAndType(
	// Uri.parse("file://" + getFilesDir() + "/gmd.pdf"),
	// "application/pdf");
	//
	// startActivity(intent);
	// }

	// private void copyFile(InputStream in, OutputStream out) throws
	// IOException {
	// byte[] buffer = new byte[1024];
	// int read;
	// while ((read = in.read(buffer)) != -1) {
	// out.write(buffer, 0, read);
	// }
	// }

	public void showListView(ListView view) {
		// AnimationSet set = new AnimationSet(true);
		//
		// Animation animation = new AlphaAnimation(0.0f, 1.0f);
		// animation.setDuration(250);
		// set.addAnimation(animation);
		//
		// animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
		// 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		// animation.setDuration(150);
		// set.addAnimation(animation);
		//
		// LayoutAnimationController controller = new LayoutAnimationController(
		// set, 0.25f);
		// view.setLayoutAnimation(controller);

		// view.setAnimation(inFromRightAnimation());
		view.setVisibility(View.VISIBLE);
	}

	void showQuitAlert() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				Service_CheckList_Without_Swipe.this)
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

	private Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(800);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(800);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	void showFieldMandatoryDialog() {
		AlertDialog m_AlertDialog = new AlertDialog.Builder(
				Service_CheckList_Without_Swipe.this)
				.setMessage("Field is mandatory")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {

					}
				}).create();
		m_AlertDialog.setCanceledOnTouchOutside(false);
		m_AlertDialog.show();
	}
}
