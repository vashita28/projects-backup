package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.adapter.Sc_Leaf_Node_Adapter;
import co.uk.pocketapp.gmd.adapter.Sc_Main_Adapter;
import co.uk.pocketapp.gmd.adapter.Sc_SecondGen_Adapter;
import co.uk.pocketapp.gmd.adapter.Sc_ThirdGen_Adapter;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class Service_Checklist extends Activity {
	Button logout_Button;
	// ListView report_listview;
	// private ArrayAdapter<String> listAdapter;
	// CustomAdaptor_Checklist custom_Adaptor = new CustomAdaptor_Checklist();

	// Button btn_report_a_problem;

	View view_service_checklists_top_bar;
	TextView site_Name;
	TextView mill_Name;

	TextView date_N_Time;
	String reportDate = null;

	ListView listview_main, listview_secondgen, listview_thirdgen,
			listview_leaf;

	LinearLayout ll_secondgen, ll_thirdgen, ll_leaf, ll_secondgen_back,
			ll_thirdgen_back, ll_leaf_back;

	TextView textview_title, textview_GMD_Heading, item_moments_textView;

	String m_szLeafItem, m_szThirdGenItem, m_szSecondGenItem, m_szMainItem;
	String m_szLeafItemID, m_szThirdGenItemID, m_szSecondGenItemID,
			m_szMainItemID;

	View view_sample_task_page, view_sample_task_page_2;
	Boolean bIsSampleTaskPageVisible = false,
			bIsSampleTaskPage2Visible = false;
	LinearLayout ll_listviews;

	EditText m_et1, m_et2, m_et3, m_et4;
	EditText m_et_task2_1, m_et_task2_2, m_et_task2_3;

	Button btn_cancel, btn_saveandcomplete, btn_task2_save, btn_task2_cancel;

	Cursor mCursorMain, mCursorLeafNode, mCursorThirdGen, mCursorSecondGen;
	ContentObserver dataObserver;
	int nPreviousPositionLeaf = -1, nPreviousPositionThirdGen = -1,
			nPreviousPositionSecondGen = -1, nPreviousPositionMain = -1;
	ListAdapter mLeafNodeAdapter, mSecondGenAdapter, mThirdGenAdapter,
			mMainAdapter;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mmaa");

	int nMainId = -1, nSecondGenID = -1, nThirdGenID = -1, nLeafID = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_checklist);
		site_Name = (TextView) findViewById(R.id.siteName);
		site_Name.setTypeface(GMDApplication.fontText);
		mill_Name = (TextView) findViewById(R.id.millName);
		mill_Name.setTypeface(GMDApplication.fontText);
		// Getting data from share preferences and show on site name and mill
		// name textview:
		site_Name.setText(Util.getSiteName(Service_Checklist.this));
		mill_Name.setText(Util.getMillName(Service_Checklist.this));

		date_N_Time = (TextView) findViewById(R.id.DateNtime);
		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			reportDate = dateFormat.format(today);
			date_N_Time.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// sample task page
		{
			view_sample_task_page = (View) findViewById(R.id.view_sample_task_page);
			m_et1 = (EditText) view_sample_task_page.findViewById(R.id.et_1);
			m_et2 = (EditText) view_sample_task_page.findViewById(R.id.et_2);
			m_et3 = (EditText) view_sample_task_page.findViewById(R.id.et_3);
			m_et4 = (EditText) view_sample_task_page.findViewById(R.id.et_4);
			item_moments_textView = (TextView) findViewById(R.id.item_moments_text);
			item_moments_textView.setTypeface(GMDApplication.fontHeading);
			m_et1.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			m_et2.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			m_et3.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));
			m_et4.setHintTextColor(getResources().getColor(
					R.color.hint_text_color));

			m_et1.setImeOptions(EditorInfo.IME_ACTION_DONE);
			m_et2.setImeOptions(EditorInfo.IME_ACTION_DONE);
			m_et3.setImeOptions(EditorInfo.IME_ACTION_DONE);
			m_et4.setImeOptions(EditorInfo.IME_ACTION_DONE);

			btn_cancel = (Button) view_sample_task_page
					.findViewById(R.id.btn_cancel);
			btn_saveandcomplete = (Button) view_sample_task_page
					.findViewById(R.id.btn_saveandcomplete);

			btn_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					view_sample_task_page.setVisibility(View.GONE);
					ll_listviews.setVisibility(View.VISIBLE);
					bIsSampleTaskPageVisible = false;
					// Setting all edittext empty:
					m_et1.setText("");
					m_et2.setText("");
					m_et3.setText("");
					m_et4.setText("");
				}
			});

			btn_saveandcomplete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (m_et1.getText().length() > 0
							|| m_et2.getText().length() > 0
							|| m_et3.getText().length() > 0
							|| m_et4.getText().length() > 0) {
						// Save code goes here-----------------

						String szSelection = DataProvider.Summary_Values.CHILD_LEAF_NODE_ID
								+ " ='"
								+ m_szLeafItemID
								+ "' AND "
								+ DataProvider.Summary_Values.CHILD_MAIN_ID
								+ " ='"
								+ m_szMainItemID
								+ "' AND "
								+ DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
								+ " ='"
								+ m_szSecondGenItemID
								+ "' AND "
								+ DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
								+ " ='"
								+ m_szThirdGenItemID
								+ "' AND "
								+ DataProvider.Summary_Values.REPORT_ID
								+ " ='"
								+ Util.getReportID(Service_Checklist.this)
								+ "'";

						Cursor cursorSummary = getContentResolver().query(
								DataProvider.Summary_Values.CONTENT_URI, null,
								szSelection, null, null);
						cursorSummary.moveToFirst();
						String szID = "";

						ContentValues summaryValues = new ContentValues();
						summaryValues.put(DataProvider.Summary_Values.CHILD,
								m_et1.getText().toString() + ","
										+ m_et2.getText().toString() + ","
										+ m_et3.getText().toString() + ","
										+ m_et4.getText().toString());
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_LEAF_NODE_ID,
								m_szLeafItemID);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_THIRD_GEN_ID,
								m_szThirdGenItemID);
						summaryValues
								.put(DataProvider.Summary_Values.CHILD_SECOND_GEN_ID,
										m_szSecondGenItemID);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_MAIN_ID,
								m_szMainItemID);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_LEAF_NODE,
								m_szLeafItem);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_THIRD_GEN,
								m_szThirdGenItem);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_SECOND_GEN,
								m_szSecondGenItem);
						summaryValues.put(
								DataProvider.Summary_Values.CHILD_MAIN,
								m_szMainItem);
						summaryValues.put(
								DataProvider.Summary_Values.REPORT_ID,
								Util.getReportID(Service_Checklist.this));

						if (cursorSummary.getCount() > 0) {
							szID = cursorSummary.getString(cursorSummary
									.getColumnIndex(DataProvider.Summary_Values.ID));

							getContentResolver().update(
									DataProvider.Summary_Values.CONTENT_URI,
									summaryValues,
									DataProvider.Summary_Values.ID + " ='"
											+ szID + "'", null);

						} else {
							getContentResolver().insert(
									DataProvider.Summary_Values.CONTENT_URI,
									summaryValues);
						}
						cursorSummary.close();
						getContentResolver().notifyChange(
								DataProvider.Summary_Values.CONTENT_URI, null);

						// Setting all edittext empty:
						m_et1.setText("");
						m_et2.setText("");
						m_et3.setText("");
						m_et4.setText("");

						view_sample_task_page.setVisibility(View.GONE);
						ll_listviews.setVisibility(View.VISIBLE);
						bIsSampleTaskPageVisible = false;

					} else {
						AlertDialog m_AlertDialog = new AlertDialog.Builder(
								Service_Checklist.this)
								.setMessage("Please fill atleast one value.")
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

			// sample task page 2
			{

				view_sample_task_page_2 = (View) findViewById(R.id.view_sample_task_page_2);
				m_et_task2_1 = (EditText) view_sample_task_page_2
						.findViewById(R.id.et_1);
				m_et_task2_2 = (EditText) view_sample_task_page_2
						.findViewById(R.id.et_2);
				m_et_task2_3 = (EditText) view_sample_task_page_2
						.findViewById(R.id.et_3);

				m_et_task2_1.setImeOptions(EditorInfo.IME_ACTION_DONE);
				m_et_task2_2.setImeOptions(EditorInfo.IME_ACTION_DONE);
				m_et_task2_3.setImeOptions(EditorInfo.IME_ACTION_DONE);

				btn_task2_cancel = (Button) view_sample_task_page_2
						.findViewById(R.id.btn_cancel);
				btn_task2_save = (Button) view_sample_task_page_2
						.findViewById(R.id.btn_saveandcomplete);

				btn_task2_cancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						view_sample_task_page_2.setVisibility(View.GONE);
						ll_listviews.setVisibility(View.VISIBLE);
						bIsSampleTaskPage2Visible = false;
						// Setting all edittext empty:
						m_et_task2_1.setText("");
						m_et_task2_2.setText("");
						m_et_task2_3.setText("");

						view_sample_task_page_2.setVisibility(View.GONE);
						ll_listviews.setVisibility(View.VISIBLE);
						bIsSampleTaskPage2Visible = false;

						listview_thirdgen.setVisibility(View.VISIBLE);
						ll_thirdgen.setVisibility(View.VISIBLE);
						listview_secondgen.setVisibility(View.VISIBLE);
						ll_secondgen.setVisibility(View.VISIBLE);
					}
				});

				btn_task2_save.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Save code goes here-----------------

						// Setting all edittext empty:
						m_et_task2_1.setText("");
						m_et_task2_2.setText("");
						m_et_task2_3.setText("");

						view_sample_task_page_2.setVisibility(View.GONE);
						ll_listviews.setVisibility(View.VISIBLE);
						bIsSampleTaskPage2Visible = false;

						listview_thirdgen.setVisibility(View.VISIBLE);
						ll_thirdgen.setVisibility(View.VISIBLE);
						listview_secondgen.setVisibility(View.VISIBLE);
						ll_secondgen.setVisibility(View.VISIBLE);
					}
				});

			}

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
		listview_main = (ListView) findViewById(R.id.listview_main);
		listview_secondgen = (ListView) findViewById(R.id.listview_second);
		listview_thirdgen = (ListView) findViewById(R.id.listview_third);
		listview_leaf = (ListView) findViewById(R.id.listview_leaf);

		listview_main.setDivider(null);
		listview_secondgen.setDivider(null);
		listview_thirdgen.setDivider(null);
		listview_leaf.setDivider(null);

		logout_Button = (Button) findViewById(R.id.LogOutButton);
		// report_listview = (ListView) findViewById(R.id.report_list_view);

		// report_listview.setAdapter(custom_Adaptor);
		// btn_report_a_problem = (Button) findViewById(R.id.reportproblemBtn);

		// logout_Button:
		logout_Button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getBaseContext(),
						Activity_login.class);
				startActivity(myIntent);
				MainActivity.logout();
				finish();
			}
		});

		// List view
		// Seeing background color:
		listview_main.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.service_checklist_list_bg));

		listview_main.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				nMainId = pos;
				LinearLayout ll_internal;
				try {
					View prev_View = listview_main
							.getChildAt(nPreviousPositionMain);
					ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bglight));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// listview_main.getChildAt(nPreviousPosition).setBackground(
				// getResources().getDrawable(
				// R.drawable.service_checklist_list_bglight));

				nPreviousPositionMain = pos;
				ll_internal = (LinearLayout) view
						.findViewById(R.id.ll_internal);
				ll_internal.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.service_checklist_list_bgdark));

				// view.setBackground(getResources().getDrawable(
				// R.drawable.service_checklist_list_bgdark));

				m_szMainItem = ((TextView) view
						.findViewById(R.id.textview_report_title)).getText()
						.toString();

				// listview_secondgen.setVisibility(View.VISIBLE);
				show(listview_secondgen);
				ll_secondgen.setVisibility(View.VISIBLE);

				listview_thirdgen.setVisibility(View.GONE);
				ll_thirdgen.setVisibility(View.GONE);
				listview_leaf.setVisibility(View.GONE);
				ll_leaf.setVisibility(View.GONE);

				String szChildRowID = ((TextView) view
						.findViewById(R.id.textview_child_row_id)).getText()
						.toString();
				m_szMainItemID = szChildRowID;

				mCursorSecondGen = managedQuery(
						DataProvider.Second_Generation.CONTENT_URI, null,
						DataProvider.Second_Generation.MAIN_CHILD_ID + " = ?",
						new String[] { szChildRowID }, null);
				mCursorSecondGen.moveToFirst();
				startManagingCursor(mCursorSecondGen);
				mSecondGenAdapter = new Sc_SecondGen_Adapter(
						Service_Checklist.this, mCursorSecondGen, true);
				listview_secondgen.setAdapter(mSecondGenAdapter);
			}
		});

		listview_secondgen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				nSecondGenID = pos;
				LinearLayout ll_internal;
				try {
					View prev_View = listview_secondgen
							.getChildAt(nPreviousPositionSecondGen);
					ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bglight));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// listview_secondgen.getChildAt(nPreviousPosition).setBackground(
				// getResources().getDrawable(
				// R.drawable.service_checklist_list_bglight));

				nPreviousPositionSecondGen = pos;
				ll_internal = (LinearLayout) view
						.findViewById(R.id.ll_internal);
				ll_internal.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.service_checklist_list_bgdark));

				// view.setBackground(getResources().getDrawable(
				// R.drawable.service_checklist_list_bgdark));

				m_szSecondGenItem = ((TextView) view
						.findViewById(R.id.textview_report_title)).getText()
						.toString();

				listview_main.setVisibility(View.GONE);

				// listview_thirdgen.setVisibility(View.VISIBLE);
				show(listview_thirdgen);

				ll_thirdgen.setVisibility(View.VISIBLE);
				listview_leaf.setVisibility(View.GONE);
				ll_leaf.setVisibility(View.GONE);

				String szChildRowID = ((TextView) view
						.findViewById(R.id.textview_child_row_id)).getText()
						.toString();
				m_szSecondGenItemID = szChildRowID;

				mCursorThirdGen = managedQuery(
						DataProvider.Third_Generation.CONTENT_URI, null,
						DataProvider.Third_Generation.CHILD_SECOND_GEN_ID
								+ " = ?", new String[] { szChildRowID }, null);
				mCursorThirdGen.moveToFirst();
				startManagingCursor(mCursorThirdGen);
				mThirdGenAdapter = new Sc_ThirdGen_Adapter(
						Service_Checklist.this, mCursorThirdGen, true);
				listview_thirdgen.setAdapter(mThirdGenAdapter);
			}
		});

		listview_thirdgen.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				nThirdGenID = pos;
				LinearLayout ll_internal;
				try {
					View prev_View = listview_thirdgen
							.getChildAt(nPreviousPositionThirdGen);
					ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bglight));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// listview_thirdgen.getChildAt(nPreviousPosition).setBackground(
				// getResources().getDrawable(
				// R.drawable.service_checklist_list_bglight));

				nPreviousPositionThirdGen = pos;
				ll_internal = (LinearLayout) view
						.findViewById(R.id.ll_internal);
				ll_internal.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.service_checklist_list_bgdark));
				// view.setBackground(getResources().getDrawable(
				// R.drawable.service_checklist_list_bgdark));

				m_szThirdGenItem = ((TextView) view
						.findViewById(R.id.textview_report_title)).getText()
						.toString();

				listview_main.setVisibility(View.GONE);
				listview_secondgen.setVisibility(View.GONE);
				ll_secondgen.setVisibility(View.GONE);

				if (m_szThirdGenItem.contains("Check air gap between stator")
						|| m_szThirdGenItem
								.contains("Check sole plate clearance")
						|| m_szThirdGenItem
								.contains("Check Core bolts & Pressing system")
						|| m_szThirdGenItem.contains("Check Core partition")
						|| m_szThirdGenItem.contains("Check press fingers")
						|| m_szThirdGenItem
								.contains("Check key bars displacement")
						|| m_szThirdGenItem
								.contains("Check key bar holding plates")
						|| m_szThirdGenItem
								.contains("Check core bolt insulation")
						|| m_szThirdGenItem.contains("Fixing bolts")
						|| m_szThirdGenItem.contains("Check brush wear")) {
					listview_thirdgen.setVisibility(View.GONE);
					ll_thirdgen.setVisibility(View.GONE);
					ll_listviews.setVisibility(View.GONE);
					view_sample_task_page_2.setVisibility(View.VISIBLE);
					bIsSampleTaskPage2Visible = true;
				} else {

					// listview_leaf.setVisibility(View.VISIBLE);
					show(listview_leaf);
					ll_leaf.setVisibility(View.VISIBLE);

					String szChildRowID = ((TextView) view
							.findViewById(R.id.textview_child_row_id))
							.getText().toString();
					m_szThirdGenItemID = szChildRowID;

					mCursorLeafNode = managedQuery(
							DataProvider.Leaf_Node.CONTENT_URI, null, null,
							null, null);
					mCursorLeafNode.moveToFirst();
					startManagingCursor(mCursorLeafNode);
					mLeafNodeAdapter = new Sc_Leaf_Node_Adapter(
							Service_Checklist.this, mCursorLeafNode, true,
							m_szMainItemID, m_szSecondGenItemID,
							m_szThirdGenItemID);
					listview_leaf.setAdapter(mLeafNodeAdapter);
				}
			}
		});

		listview_leaf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				nLeafID = pos;
				LinearLayout ll_internal;
				try {
					View prev_View = listview_leaf
							.getChildAt(nPreviousPositionLeaf);
					ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bglight));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// listview_leaf.getChildAt(nPreviousPosition).setBackground(
				// getResources().getDrawable(
				// R.drawable.service_checklist_list_bglight));

				nPreviousPositionLeaf = pos;
				ll_internal = (LinearLayout) view
						.findViewById(R.id.ll_internal);
				ll_internal.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.service_checklist_list_bgdark));
				// view.setBackground(getResources().getDrawable(
				// R.drawable.service_checklist_list_bgdark));

				LinearLayout ll_firstLayout = (LinearLayout) view_sample_task_page
						.findViewById(R.id.ll_first);
				LinearLayout ll_secondLayout = (LinearLayout) view_sample_task_page
						.findViewById(R.id.ll_workscope);
				TextView txtHeading = (TextView) view_sample_task_page
						.findViewById(R.id.page_heading_middle);

				m_szLeafItem = ((TextView) view
						.findViewById(R.id.textview_report_title)).getText()
						.toString();

				String szChildRowID = ((TextView) view
						.findViewById(R.id.textview_child_row_id)).getText()
						.toString();
				m_szLeafItemID = szChildRowID;

				if (m_szLeafItem.equals("Report a problem")) {
					Intent myIntent = new Intent(getBaseContext(),
							Report_A_Problem.class);
					startActivity(myIntent);
				} else if (m_szLeafItem.equals("Help-Support")) {
					return;
				} else {
					view_sample_task_page.setVisibility(View.VISIBLE);
					ll_listviews.setVisibility(View.GONE);
					bIsSampleTaskPageVisible = true;

					Log.d("*********************************",
							" **********************************" + nMainId
									+ " " + nSecondGenID + " " + nThirdGenID
									+ " " + nLeafID);

					if (nMainId == 0 && nSecondGenID == 0 && nThirdGenID == 0) {
						ll_firstLayout.setVisibility(View.GONE);
						ll_secondLayout.setVisibility(View.VISIBLE);

						if (nLeafID == 0) {
							txtHeading.setText("Workscope");
						} else if (nLeafID == 1) {
							txtHeading.setText("Work carried out");
						} else if (nLeafID == 2) {
							txtHeading.setText("Conditions");
						} else if (nLeafID == 3) {
							txtHeading.setText("Recommendations");
						} else if (nLeafID == 4) {
							txtHeading.setText("Comments");
						} else if (nLeafID == 6) {
							Intent myIntent = new Intent(getBaseContext(),
									Report_A_Problem.class);
							startActivity(myIntent);
						}
					} else {

						ll_firstLayout.setVisibility(View.VISIBLE);
						ll_secondLayout.setVisibility(View.GONE);
					}

				}

				String szSelection = DataProvider.Summary_Values.CHILD_LEAF_NODE_ID
						+ " ='"
						+ m_szLeafItemID
						+ "' AND "
						+ DataProvider.Summary_Values.CHILD_MAIN_ID
						+ " ='"
						+ m_szMainItemID
						+ "' AND "
						+ DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
						+ " ='"
						+ m_szSecondGenItemID
						+ "' AND "
						+ DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
						+ " ='"
						+ m_szThirdGenItemID
						+ "' AND "
						+ DataProvider.Summary_Values.REPORT_ID
						+ " ='"
						+ Util.getReportID(Service_Checklist.this) + "'";

				Cursor cursorSummary = getContentResolver().query(
						DataProvider.Summary_Values.CONTENT_URI, null,
						szSelection, null, null);
				if (cursorSummary != null && cursorSummary.moveToFirst()) {

					try {
						String[] szCommentArray = cursorSummary
								.getString(
										cursorSummary
												.getColumnIndex(DataProvider.Summary_Values.CHILD))
								.split(",");
						m_et1.setText(szCommentArray[0]);
						m_et2.setText(szCommentArray[1]);
						m_et3.setText(szCommentArray[2]);
						m_et4.setText(szCommentArray[3]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				cursorSummary.close();

			}
		});

		ll_secondgen = (LinearLayout) findViewById(R.id.ll_secondgen);
		ll_secondgen_back = (LinearLayout) findViewById(R.id.ll_secondgen_back);
		ll_thirdgen = (LinearLayout) findViewById(R.id.ll_thirdgen);
		ll_thirdgen_back = (LinearLayout) findViewById(R.id.ll_thirdgen_back);
		ll_leaf = (LinearLayout) findViewById(R.id.ll_leaf);
		ll_leaf_back = (LinearLayout) findViewById(R.id.ll_leaf_back);

		ll_secondgen_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listview_main.setVisibility(View.VISIBLE);
				ll_secondgen.setVisibility(View.GONE);
				listview_secondgen.setVisibility(View.GONE);
				ll_thirdgen.setVisibility(View.GONE);
				listview_thirdgen.setVisibility(View.GONE);
				ll_leaf.setVisibility(View.GONE);
				listview_leaf.setVisibility(View.GONE);
				showHideListViews();
			}
		});

		ll_thirdgen_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_secondgen.setVisibility(View.VISIBLE);
				listview_secondgen.setVisibility(View.VISIBLE);
				ll_leaf.setVisibility(View.GONE);
				listview_leaf.setVisibility(View.GONE);
				showHideListViews();
			}
		});

		ll_leaf_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_thirdgen.setVisibility(View.VISIBLE);
				listview_thirdgen.setVisibility(View.VISIBLE);
				ll_secondgen.setVisibility(View.VISIBLE);
				listview_secondgen.setVisibility(View.VISIBLE);
				ll_leaf.setVisibility(View.GONE);
				listview_leaf.setVisibility(View.GONE);
				showHideListViews();
			}
		});

		populateData();

	}

	void populateData() {
		mCursorMain = managedQuery(DataProvider.Main.CONTENT_URI, null, null,
				null, null);
		mCursorMain.moveToFirst();
		startManagingCursor(mCursorMain);
		mMainAdapter = new Sc_Main_Adapter(Service_Checklist.this, mCursorMain,
				true);
		listview_main.setAdapter(mMainAdapter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!showHideListViews())
			showQuitAlert();
		else
			showHideListViews();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dataObserver = new DataObserver(new Handler());
		getContentResolver().registerContentObserver(
				DataProvider.Summary_Values.CONTENT_URI, true, dataObserver);
	}

	boolean showHideListViews() {
		if (bIsSampleTaskPageVisible) {
			m_et1.setText("");
			m_et2.setText("");
			m_et3.setText("");
			m_et4.setText("");
			view_sample_task_page.setVisibility(View.GONE);
			ll_listviews.setVisibility(View.VISIBLE);
			bIsSampleTaskPageVisible = false;
			return true;
		} else if (listview_thirdgen.getVisibility() == View.VISIBLE
				&& listview_leaf.getVisibility() == View.VISIBLE) {
			listview_leaf.setVisibility(View.GONE);
			ll_leaf.setVisibility(View.GONE);
			listview_main.setVisibility(View.GONE);
			listview_secondgen.setVisibility(View.VISIBLE);
			ll_secondgen.setVisibility(View.VISIBLE);
			return true;
		} else if (listview_secondgen.getVisibility() == View.VISIBLE
				&& listview_thirdgen.getVisibility() == View.VISIBLE) {
			listview_leaf.setVisibility(View.GONE);
			ll_leaf.setVisibility(View.GONE);
			listview_main.setVisibility(View.VISIBLE);
			listview_thirdgen.setVisibility(View.GONE);
			ll_thirdgen.setVisibility(View.GONE);
			return true;
		} else if (bIsSampleTaskPage2Visible) {
			bIsSampleTaskPage2Visible = false;
			view_sample_task_page_2.setVisibility(View.GONE);
			ll_listviews.setVisibility(View.VISIBLE);
			return true;
		}
		// else if (listview_main.getVisibility() == View.VISIBLE
		// && listview_secondgen.getVisibility() == View.VISIBLE
		// && listview_thirdgen.getVisibility() == View.GONE
		// && listview_leaf.getVisibility() == View.GONE) {
		// listview_secondgen.setVisibility(View.GONE);
		// ll_secondgen.setVisibility(View.GONE);
		// listview_leaf.setVisibility(View.GONE);
		// ll_leaf.setVisibility(View.GONE);
		// listview_thirdgen.setVisibility(View.GONE);
		// ll_thirdgen.setVisibility(View.GONE);
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

			// refresh leaf list
			{
				mLeafNodeAdapter = null;
				listview_leaf.setAdapter(mLeafNodeAdapter);
				mLeafNodeAdapter = new Sc_Leaf_Node_Adapter(
						Service_Checklist.this, mCursorLeafNode, true,
						m_szMainItemID, m_szSecondGenItemID, m_szThirdGenItemID);
				listview_leaf.setAdapter(mLeafNodeAdapter);

				try {
					View prev_View = listview_leaf.getAdapter().getView(
							nPreviousPositionLeaf, null, null);
					// .getChildAt(nPreviousPositionLeaf);
					LinearLayout ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bgdark));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// refresh thirdgen list
			{
				mThirdGenAdapter = null;
				listview_thirdgen.setAdapter(mThirdGenAdapter);
				mThirdGenAdapter = new Sc_ThirdGen_Adapter(
						Service_Checklist.this, mCursorThirdGen, true);
				listview_thirdgen.setAdapter(mThirdGenAdapter);

				try {
					View prev_View = listview_thirdgen.getAdapter().getView(
							nPreviousPositionThirdGen, null, null);
					// .getChildAt(nPreviousPositionThirdGen);
					LinearLayout ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bgdark));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// refresh secondgen list
			{
				mSecondGenAdapter = null;
				listview_secondgen.setAdapter(mSecondGenAdapter);
				mSecondGenAdapter = new Sc_SecondGen_Adapter(
						Service_Checklist.this, mCursorSecondGen, true);
				listview_secondgen.setAdapter(mSecondGenAdapter);

				try {
					View prev_View = listview_secondgen.getAdapter().getView(
							nPreviousPositionSecondGen, null, null);
					// .getChildAt(nPreviousPositionSecondGen);
					LinearLayout ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bgdark));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// refresh main list
			{
				mMainAdapter = null;
				listview_main.setAdapter(mMainAdapter);
				mMainAdapter = new Sc_Main_Adapter(Service_Checklist.this,
						mCursorMain, true);
				listview_main.setAdapter(mMainAdapter);

				try {
					View prev_View = listview_main.getAdapter().getView(
							nPreviousPositionMain, null, null);
					// .getChildAt(nPreviousPositionMain);
					LinearLayout ll_internal = (LinearLayout) prev_View
							.findViewById(R.id.ll_internal);
					ll_internal.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.service_checklist_list_bgdark));
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	public void show(ListView view) {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(250);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(150);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		view.setLayoutAnimation(controller);
		view.setVisibility(View.VISIBLE);
	}

	void showQuitAlert() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				Service_Checklist.this)
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
}
