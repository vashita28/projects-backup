package co.uk.pocketapp.gmd.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.handlerxml.AirGapXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.BoltInsulationXMLHanlder;
import co.uk.pocketapp.gmd.handlerxml.BoltTorqueXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.BrushWearXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.CorePartitionsXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.HoldingPlatesXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.KeyBarXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.MagneticCenteringXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.PhotographsHandler;
import co.uk.pocketapp.gmd.handlerxml.PressFingerDischargeXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.PressFingerFeederXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.ReportDetailsXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.ReportXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.ServiceCheckListReadValuesHandler;
import co.uk.pocketapp.gmd.handlerxml.SiteConditionsXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.SolePlateXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.SummaryXMLHandler;
import co.uk.pocketapp.gmd.handlerxml.UserDetailsXMLHandler;
import co.uk.pocketapp.gmd.tasks.DownloadServiceCheckListTask;
import co.uk.pocketapp.gmd.tasks.Download_XML_Task;
import co.uk.pocketapp.gmd.ui.Sync_With_Server;

public class ResponseParser {
	private static final String TAG = "ResponseParser";
	List<String> list;
	StringBuilder shown_Data = new StringBuilder();
	static int count_list_of_dist = 0;// 0
	static int count_dist_to_customer = 0;// 0
	static int count_summary_inspection = 0;
	static int count_main_difficulties = 0;
	static int count_recommandations = 0;
	static int count_point_of_general_interest = 0;
	static int count_site_storage_conditions = 0;
	static int count_spare_parts_site = 0;
	static int count_tools_instruments = 0;
	int status_int;
	Context mContext;
	public static HashMap<String, String> rejectedCommentsMap = new HashMap<String, String>();

	public ResponseParser(Context context) {
		mContext = context;
	}

	// private static XmlPullParser getXMLPullParserInstance(String xmlData)
	// throws XmlPullParserException, IOException {
	//
	// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	// factory.setNamespaceAware(true);
	// XmlPullParser xmlPullParser = factory.newPullParser();
	//
	// xmlPullParser.setInput(new StringReader(xmlData));
	//
	// return xmlPullParser;
	// }

	public HashMap<String, String> parse_Header_Details(String data_To_Parse)
			throws XmlPullParserException, IOException {
		HashMap<String, String> user = new HashMap<String, String>();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xmlPullParser = factory.newPullParser();

		xmlPullParser.setInput(new StringReader(new String(data_To_Parse)));

		int eventType = xmlPullParser.getEventType();

		String millIdRejectedComments = "";
		String currentTag = "";
		String szReportID = "", szReassigned = "";
		for (; eventType != XmlPullParser.END_DOCUMENT; eventType = xmlPullParser
				.next()) {

			switch (eventType) {

			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				currentTag = xmlPullParser.getName();
				// Getting report id:
				// if (currentTag.equals("report")) {
				// String report_id = xmlPullParser.getAttributeValue(0)
				// .toString();
				// Log.d("REPORT ID:TEXT", "" + report_id);
				// user.put("report_id", report_id);
				// Util.setReportID(mContext, report_id);
				// Util.setIsReportXMLDownloaded(mContext,
				// report_id, true);
				// }
				if (currentTag.equals("rejection_comments")) {
					millIdRejectedComments = xmlPullParser.getAttributeValue(
							null, "mill_id");
				}
				break;

			case XmlPullParser.END_TAG:
				currentTag = "";
				break;

			case XmlPullParser.TEXT:
				if (currentTag.equals(XML_Values.REPORT_ID)) {
					szReportID = xmlPullParser.getText().toString();
					if (Util.getReportID(mContext).equals("")
							|| !Util.getReportID(mContext).equals(szReportID)) {

						// report id different in XML
						// delete all values and rebuild all information from
						// existing XML
						Util.setReportID(mContext, szReportID);
						Util.setIsServiceCheclistValuesInserted(mContext, false);
						Util.setIsLeafNodeValuesInserted(mContext, false);

						mContext.getContentResolver().delete(
								DataProvider.Mill.CONTENT_URI, null, null);
						mContext.getContentResolver().delete(
								DataProvider.Services.CONTENT_URI, null, null);
						mContext.getContentResolver()
								.delete(DataProvider.Child_Leaf.CONTENT_URI,
										null, null);
						mContext.getContentResolver().delete(
								DataProvider.Tasks.CONTENT_URI, null, null);
						mContext.getContentResolver().delete(
								DataProvider.Material_Log.CONTENT_URI, null,
								null);

						// service checklist from report xml
						new ReportXMLHandler(mContext,
								new ByteArrayInputStream(
										data_To_Parse.getBytes()), "1"); // status
																			// "1"
																			// for
																			// successful
																			// login

						// tasks values from service checklist xml
						StringBuilder text = null;
						if (AppValues.getServiceCheckListtXMLFile().exists()) {
							text = new StringBuilder();
							try {
								BufferedReader br = new BufferedReader(
										new FileReader(AppValues
												.getServiceCheckListtXMLFile()));
								String line;

								while ((line = br.readLine()) != null) {
									text.append(line);
									text.append('\n');
								}
								br.close();
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

						if (text != null)
							new ServiceCheckListReadValuesHandler(mContext,
									new ByteArrayInputStream(text.toString()
											.getBytes()));

						// all photographs from xml
						new PhotographsHandler(mContext,
								new ByteArrayInputStream(
										data_To_Parse.getBytes()));

						if (szReassigned.equals("true")) {
							DownloadServiceCheckListTask downloadServiceCheckListXMLTask = new DownloadServiceCheckListTask();
							downloadServiceCheckListXMLTask.mContext = mContext;
							downloadServiceCheckListXMLTask.progressBar = null;
							downloadServiceCheckListXMLTask.szReporttID = szReportID;
							downloadServiceCheckListXMLTask.txtview_loadingmessage = null;
							downloadServiceCheckListXMLTask.execute();
						} else {
							Util.setIsReportXMLDownloaded(mContext, true);
							Util.setIsReportUploaded(mContext, false);
						}
					}
					user.put(XML_Values.REPORT_ID, szReportID);
					Util.setReportID(mContext, szReportID);
				} else if (currentTag.equals(XML_Values.SITE_NAME)) {
					String site_name_Data = xmlPullParser.getText().toString();
					user.put(XML_Values.SITE_NAME, site_name_Data);
					Util.setSiteName(mContext, site_name_Data);
				} else if (currentTag.equals(XML_Values.MILL_NAME)) {
					String mill_name_Data = xmlPullParser.getText().toString();
					user.put(XML_Values.MILL_NAME, mill_name_Data);
					Log.d("MILL NAME DATA:TEXT", "" + mill_name_Data);
					// Util.setMillName(mContext, mill_name_Data);
				} else if (currentTag.equals("reassigned")) {
					szReassigned = xmlPullParser.getText().toString();
					Log.d("REASSIGNED", "REASSIGNED:: " + szReassigned);
					if (szReassigned.equals("true"))
						Util.setIsReportReassigned(mContext, true);
					else
						Util.setIsReportReassigned(mContext, false);
				} else if (currentTag.equals("rejection_comments")) {
					String rejectionComments = xmlPullParser.getText()
							.toString();
					rejectedCommentsMap.put(millIdRejectedComments,
							rejectionComments);
				}
			}
		}
		return user;
	}

	// public HashMap<String, String> parse_Report_Details(String data_To_Parse)
	// throws XmlPullParserException, IOException {
	//
	// HashMap<String, String> user = new HashMap<String, String>();
	// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	// factory.setNamespaceAware(true);
	// XmlPullParser xmlPullParser = factory.newPullParser();
	//
	// xmlPullParser.setInput(new StringReader(new String(data_To_Parse)));
	//
	// int eventType = xmlPullParser.getEventType();
	//
	// String currentTag = "";
	// for (; eventType != XmlPullParser.END_DOCUMENT; eventType = xmlPullParser
	// .next()) {
	//
	// switch (eventType) {
	//
	// case XmlPullParser.START_DOCUMENT:
	// break;
	//
	// case XmlPullParser.START_TAG:
	// currentTag = xmlPullParser.getName();
	// // Getting report id:
	// if (currentTag.equals("report")) {
	// String report_id = xmlPullParser.getAttributeValue(0)
	// .toString();
	// Log.d("REPORT ID:TEXT", "" + report_id);
	// user.put("report_id", report_id);
	// }
	// break;
	//
	// case XmlPullParser.END_TAG:
	// currentTag = "";
	// break;
	//
	// case XmlPullParser.TEXT:
	// if (currentTag.equals(XML_Values.CUSTOMER)) {
	// String customer_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.CUSTOMER, customer_Data);
	// } else if (currentTag.equals(XML_Values.SITE)) {
	// String site_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.SITE, site_Data);
	// } else if (currentTag.equals(XML_Values.COUNTRY_NAME)) {
	// String country_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.COUNTRY_NAME, country_Data);
	// } else if (currentTag.equals(XML_Values.CUSTOMER_REP)) {
	// String customer_Rep_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.CUSTOMER_REP, customer_Rep_Data);
	// } else if (currentTag.equals(XML_Values.PLANT)) {
	// String plant_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.PLANT, plant_Data);
	// } else if (currentTag.equals(XML_Values.PLANT_TYPE)) {
	// String plant_Type_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.PLANT_TYPE, plant_Type_Data);
	// } else if (currentTag.equals(XML_Values.UNIT)) {
	// String unit_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.UNIT, unit_Data);
	// } else if (currentTag.equals(XML_Values.SYSTEM_MACHINE)) {
	// String system_Machine_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.SYSTEM_MACHINE, system_Machine_Data);
	// } else if (currentTag.equals(XML_Values.TYPE)) {
	// String type_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.TYPE, type_Data);
	// } else if (currentTag.equals(XML_Values.SERIAL_NO)) {
	// String serial_no_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.SERIAL_NO, serial_no_Data);
	// } else if (currentTag.equals(XML_Values.YEAR_OF_DELIVERY)) {
	// String year_of_delivery_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.YEAR_OF_DELIVERY, year_of_delivery_Data);
	// } else if (currentTag.equals(XML_Values.OPERATING_HOURS)) {
	// String operating_hours_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.OPERATING_HOURS, operating_hours_Data);
	// }// SERVICE INFORMATION:Service TYPE----->
	// else if (currentTag.equals(XML_Values.SERVICE_TYPE)) {
	// String service_Type_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.SERVICE_TYPE, service_Type_Data);
	// }
	// if (currentTag.equals(XML_Values.NEW_PLANT)) {
	// String new_plant_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.NEW_PLANT, new_plant_Data);
	// }
	// if (currentTag.equals(XML_Values.UPGRADE)) {
	// String upgrade_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.UPGRADE, upgrade_Data);
	// }
	// if (currentTag.equals(XML_Values.TROUBLESHOOTING)) {
	// String troubleshooting_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.TROUBLESHOOTING, troubleshooting_Data);
	// }
	// if (currentTag.equals(XML_Values.OTHERS)) {
	// String others_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.OTHERS, others_Data);
	// }
	// if (currentTag.equals(XML_Values.FIRST_INSTALLATION)) {
	// String first_installation_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.FIRST_INSTALLATION,
	// first_installation_Data);
	// }
	// if (currentTag.equals(XML_Values.MINOR_INSPECTION)) {
	// String minor_inspection_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.MINOR_INSPECTION, minor_inspection_Data);
	// }
	// if (currentTag.equals(XML_Values.MAJOR_OVERHAUL)) {
	// String major_overhaul_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.MAJOR_OVERHAUL, major_overhaul_Data);
	// }// SERVICE INFORMATION:Service TASK----->
	// else if (currentTag.equals(XML_Values.SERVICE_TASKS)) {
	// String service_Task_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.SERVICE_TASKS, service_Task_Data);
	// }
	// if (currentTag.equals(XML_Values.INSTALLATION)) {
	// String installation_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.INSTALLATION, installation_Data);
	// }
	// if (currentTag.equals(XML_Values.COMISSIONING)) {
	// String comissioning_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.COMISSIONING, comissioning_Data);
	// }
	// if (currentTag.equals(XML_Values.TEST_ASSESSMENT)) {
	// String test_assessment_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.TEST_ASSESSMENT, test_assessment_Data);
	// }
	// if (currentTag.equals(XML_Values.OTHERS_SERVICE_TASK)) {// OTHERS
	// String others_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.OTHERS_SERVICE_TASK, others_Data);
	// }
	// if (currentTag.equals(XML_Values.OVERHAUL_WORKS)) {
	// String overhaul_works_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.OVERHAUL_WORKS, overhaul_works_Data);
	// }
	// if (currentTag.equals(XML_Values.RE_COMISSIONING)) {
	// String re_comissioning_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.RE_COMISSIONING, re_comissioning_Data);
	// }
	// if (currentTag.equals(XML_Values.DIAGNOSTICS)) {
	// String diagnostics_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.DIAGNOSTICS, diagnostics_Data);
	// }
	// if (currentTag.equals(XML_Values.FACTORY_INSP)) {
	// String factory_insp_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.FACTORY_INSP, factory_insp_Data);
	// }
	// if (currentTag.equals(XML_Values.REPAIR)) {
	// String repair_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.REPAIR, repair_Data);
	// }
	// if (currentTag.equals(XML_Values.INVESTIGATION)) {
	// String investigation_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.INVESTIGATION, investigation_Data);
	// }
	// if (currentTag.equals(XML_Values.INSPECTION)) {
	// String inspection_Data = xmlPullParser.getText().toString();
	// user.put(XML_Values.INSPECTION, inspection_Data);
	// }
	// if (currentTag.equals(XML_Values.MOTOR_INSP_INSIDE)) {
	// String motor_insp_inside_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.MOTOR_INSP_INSIDE,
	// motor_insp_inside_Data);
	// }
	// if (currentTag.equals(XML_Values.MOTOR_INSP_EXTERNAL)) {
	// String motor_insp_external_Data = xmlPullParser.getText()
	// .toString();
	// user.put(XML_Values.MOTOR_INSP_EXTERNAL,
	// motor_insp_external_Data);
	// }
	// // else if (currentTag.equals("service_type")) {
	// // String service_type_Data = xmlPullParser.getText()
	// // .toString();
	// // user.put("service_info_type", service_type_Data);
	// // Log.d(TAG + "SERVICE TYPE DATA:TEXT", ""
	// // + service_type_Data);
	// // } else if (currentTag.equals("service_task")) {
	// // String service_task_Data = xmlPullParser.getText()
	// // .toString();
	// // user.put("service_info_task", service_task_Data);
	// // Log.d(TAG + "SERVICE TASK DATA:TEXT", ""
	// // + service_task_Data);
	// // }
	// else if (currentTag.equals(XML_Values.STATOR)) {
	// String stator_Data = xmlPullParser.getText().toString();
	// if (stator_Data == null)
	// stator_Data = "";
	// user.put(XML_Values.STATOR, stator_Data);
	// } else if (currentTag.equals(XML_Values.ROTOR_POLES)) {
	// String rotor_Poles_Data = xmlPullParser.getText()
	// .toString();
	// if (rotor_Poles_Data == null)
	// rotor_Poles_Data = "";
	// user.put(XML_Values.ROTOR_POLES, rotor_Poles_Data);
	// } else if (currentTag.equals(XML_Values.MOTOR_AUXILLARIES)) {
	// String motor_Aux_Data = xmlPullParser.getText().toString();
	// if (motor_Aux_Data == null)
	// motor_Aux_Data = "";
	// user.put(XML_Values.MOTOR_AUXILLARIES, motor_Aux_Data);
	// } else if (currentTag.equals(XML_Values.CYCLO_CONVERTER)) {
	// String cyclo_Convertor_Data = xmlPullParser.getText()
	// .toString();
	// if (cyclo_Convertor_Data == null)
	// cyclo_Convertor_Data = "";
	// user.put(XML_Values.CYCLO_CONVERTER, cyclo_Convertor_Data);
	// } else if (currentTag.equals(XML_Values.LOCATION)) {
	// count_list_of_dist = count_list_of_dist + 1;
	// String location_Data = xmlPullParser.getText().toString();
	// if (location_Data == null)
	// location_Data = "";
	// if (!user.containsKey(XML_Values.LOCATION
	// + count_list_of_dist))
	// user.put(XML_Values.LOCATION + count_list_of_dist,
	// location_Data);
	// Log.d(TAG + "location_Data", "" + location_Data);
	//
	// } else if (currentTag.equals(XML_Values.DEPT_LOC)) {
	// String dept_loc_Data = xmlPullParser.getText().toString();
	// if (dept_loc_Data == null)
	// dept_loc_Data = "";
	// user.put(XML_Values.DEPT_LOC + count_list_of_dist,
	// dept_loc_Data);
	// Log.d(TAG + "dept_loc_Data", "" + dept_loc_Data);
	// } else if (currentTag.equals(XML_Values.NAME)) {
	// String surname_Data = xmlPullParser.getText().toString();
	// if (surname_Data == null)
	// surname_Data = "";
	// user.put(XML_Values.NAME + count_list_of_dist, surname_Data);
	// Log.d(TAG + "SURNAME:TEXT", "" + surname_Data);
	// } else if (currentTag.equals(XML_Values.REMARKS_LOD)) {
	// String remarks_LOD_Data = xmlPullParser.getText()
	// .toString();
	// if (remarks_LOD_Data == null)
	// remarks_LOD_Data = "";
	// user.put(XML_Values.REMARKS_LOD + count_list_of_dist,
	// remarks_LOD_Data);
	// // Adding count in hashmap
	// user.put("layout_count", String.valueOf(count_list_of_dist));
	// Log.v("LAYOUT COUNT", String.valueOf(count_list_of_dist));
	// Log.d(TAG + "REMARKS:TEXT", "" + remarks_LOD_Data);
	// } else if (currentTag.equals(XML_Values.SEND_BY)) {
	// count_dist_to_customer = count_dist_to_customer + 1;
	// String send_By_Data = xmlPullParser.getText().toString();
	// if (send_By_Data == null)
	// send_By_Data = "";
	// if (!user.containsKey(XML_Values.SEND_BY
	// + count_dist_to_customer))
	// user.put(XML_Values.SEND_BY + count_dist_to_customer,
	// send_By_Data);
	// Log.d(TAG + "send_By_Data", "" + send_By_Data);
	//
	// } else if (currentTag.equals(XML_Values.TO)) {
	// String to_Data = xmlPullParser.getText().toString();
	// if (to_Data == null)
	// to_Data = "";
	// user.put(XML_Values.TO + count_dist_to_customer, to_Data);
	// Log.d(TAG + "to_Data", "" + to_Data);
	// } else if (currentTag.equals(XML_Values.DATE_DTC)) {
	// String date_Data = xmlPullParser.getText().toString();
	// if (date_Data == null)
	// date_Data = "";
	// user.put(XML_Values.DATE_DTC + count_dist_to_customer,
	// date_Data);
	// Log.d(TAG + "date_Data", "" + date_Data);
	// } else if (currentTag.equals(XML_Values.CUSTOMER_COMMENTS)) {
	// String customer_comments_Data = xmlPullParser.getText()
	// .toString();
	// if (customer_comments_Data == null)
	// customer_comments_Data = "";
	// user.put(XML_Values.CUSTOMER_COMMENTS
	// + count_dist_to_customer, customer_comments_Data);
	// Log.d(TAG + "customer_comments_Data", ""
	// + customer_comments_Data);
	// } else if (currentTag.equals(XML_Values.COMMENTS_INTEGRATED)) {
	// String comments_integrated_Data = xmlPullParser.getText()
	// .toString();
	// if (comments_integrated_Data == null)
	// comments_integrated_Data = "";
	// user.put(XML_Values.COMMENTS_INTEGRATED
	// + count_dist_to_customer, comments_integrated_Data);
	// // adding count to hashmap
	// user.put("layout_count_DTC",
	// String.valueOf(count_dist_to_customer));
	// Log.v("LAYOUT COUNT DTC",
	// String.valueOf(count_dist_to_customer));
	// Log.d(TAG + "comments_integrated_Data", ""
	// + comments_integrated_Data);
	// }
	// }
	// }
	// return user;
	//
	// }

	public HashMap<String, String> parse_rep_details(String data_To_Parse,
			HashMap<String, String> xmlData) throws XmlPullParserException,
			IOException {
		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new ReportDetailsXMLHandler(mContext, inputstream, xmlData);

		return xmlData;
	}

	public HashMap<String, String> parse_Summary(String data_To_Parse,
			HashMap<String, String> xmlData) throws XmlPullParserException,
			IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new SummaryXMLHandler(mContext, inputstream, xmlData);

		return xmlData;

	}

	public HashMap<String, String> parse_Site_Conditions(String data_To_Parse,
			HashMap<String, String> xmlData) throws XmlPullParserException,
			IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new SiteConditionsXMLHandler(mContext, inputstream, xmlData);

		return xmlData;

	}

	public HashMap<String, String> parse_User_Details(String data_To_Parse,
			HashMap<String, String> xmlData) throws XmlPullParserException,
			IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new UserDetailsXMLHandler(mContext, inputstream, xmlData);

		return xmlData;

	}

	public HashMap<String, String> parse_KeyBar(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new KeyBarXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_BoltTorque(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new BoltTorqueXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_SolePlate(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new SolePlateXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_CorePartitions(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new CorePartitionsXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_PressFingerFeeder(
			String data_To_Parse, HashMap<String, String> xmlData,
			String millTag) throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new PressFingerFeederXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_PressFingerDischarge(
			String data_To_Parse, HashMap<String, String> xmlData,
			String millTag) throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new PressFingerDischargeXMLHandler(mContext, inputstream, xmlData,
				millTag);

		Log.d("***************", "************************Discharge" + xmlData);

		return xmlData;

	}

	public HashMap<String, String> parse_AirGap(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new AirGapXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_HoldingPlates(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new HoldingPlatesXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_BoltInsulation(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new BoltInsulationXMLHanlder(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_MagneticCentering(
			String data_To_Parse, HashMap<String, String> xmlData,
			String millTag) throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new MagneticCenteringXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

	public HashMap<String, String> parse_BrushWear(String data_To_Parse,
			HashMap<String, String> xmlData, String millTag)
			throws XmlPullParserException, IOException {

		// convert String into InputStream
		InputStream inputstream = new ByteArrayInputStream(
				data_To_Parse.getBytes());
		new BrushWearXMLHandler(mContext, inputstream, xmlData, millTag);

		return xmlData;

	}

}
