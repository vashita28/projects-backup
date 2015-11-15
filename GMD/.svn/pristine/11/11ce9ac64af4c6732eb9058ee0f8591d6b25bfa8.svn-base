package co.uk.pocketapp.gmd.util;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import android.util.Log;
import co.uk.pocketapp.gmd.ui.Report_A_Problem;

public class XML_Creation {
	File file_report;
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;

	File file_checklist;
	DocumentBuilderFactory docFactory_sc;
	DocumentBuilder docBuilder_sc;
	Document doc_sc;

	int count_SOI = 0;
	int count_MD = 0;
	int count_Recomm = 0;
	int count_POGI = 0;
	int count_SI = 0;
	int count_MMD = 0;
	int count_SMD = 0;

	int count_SSC = 0;
	int count_SPAS = 0;
	int count_TNI = 0;

	public void get_File() {
		try {
			Util.DecryptReportXML();

			file_report = AppValues.getReportXMLFile();
			// Log.d("XML_Creation-getFile()", "Report FILE PATH:: "
			// + file_report.getPath().toString());

			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(file_report);

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	public void get_File_for_Service_Checklist() {
		try {
			file_checklist = AppValues.getServiceCheckListtXMLFile();
			// Log.d("XML_Creation-get_File_for_Service_Checklist()",
			// "ServiceCheckList FILE PATH:: "
			// + file_checklist.getPath().toString());

			docFactory_sc = DocumentBuilderFactory.newInstance();
			docBuilder_sc = docFactory_sc.newDocumentBuilder();
			doc_sc = docBuilder_sc.parse(file_checklist);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	public void save_Report_Details(HashMap<String, String> report_Details) {
		try {
			get_File();

			// // Report ID::
			// Node report_ID_node = doc
			// .getElementsByTagName(XML_Values.REPORT_ID).item(0);
			// report_ID_node.setTextContent(report_Details
			// .get(XML_Values.REPORT_ID));
			// Log.v("REPORT ID =====> ", report_ID_node.getNodeValue());

			if (report_Details.containsKey(XML_Values.CUSTOMER)
					&& !report_Details.get(XML_Values.CUSTOMER).equals("")) {

				Node report_Details1 = doc.getElementsByTagName(
						"report_details").item(0);

				// Customer::
				Node customer_node = doc.getElementsByTagName(
						XML_Values.CUSTOMER).item(0);
				customer_node.setTextContent(report_Details
						.get(XML_Values.CUSTOMER));

				// Site
				Node site_node = doc.getElementsByTagName(XML_Values.SITE)
						.item(0);
				site_node.setTextContent(report_Details.get(XML_Values.SITE));

				// Country name
				Node counrty_node = doc.getElementsByTagName(
						XML_Values.COUNTRY_NAME).item(0);
				counrty_node.setTextContent(report_Details
						.get(XML_Values.COUNTRY_NAME));

				// Customer Rep
				Node customer_rep_node = doc.getElementsByTagName(
						XML_Values.CUSTOMER_REP).item(0);
				customer_rep_node.setTextContent(EncodeXML(report_Details
						.get(XML_Values.CUSTOMER_REP)));

				// Descrption of service performed::
				// Stator
				Node stator_node = doc.getElementsByTagName(XML_Values.STATOR)
						.item(0);
				stator_node.setTextContent(EncodeXML(report_Details
						.get(XML_Values.STATOR)));

				// Rotor Poles
				Node rotor_Poles_node = doc.getElementsByTagName(
						XML_Values.ROTOR_POLES).item(0);
				rotor_Poles_node.setTextContent(EncodeXML(report_Details
						.get(XML_Values.ROTOR_POLES)));

				// Motor Auxillaries
				Node motor_aux_node = doc.getElementsByTagName(
						XML_Values.MOTOR_AUXILLARIES).item(0);
				motor_aux_node.setTextContent(EncodeXML(report_Details
						.get(XML_Values.MOTOR_AUXILLARIES)));

				// Desc of sercice performed
				Node desc_of_Service_Performed_Node = doc.getElementsByTagName(
						XML_Values.DESC_OF_SERVICE_PERFORMED).item(0);

				// Cyclo Convertor
				try {

					Node node_Cyclo_Convertor_old = (Node) doc
							.getElementsByTagName(XML_Values.CYCLO_CONVERTER)
							.item(0);

					Node Cyclo_Convertor_Node = (Node) doc
							.createElement(XML_Values.CYCLO_CONVERTER);

					Node Cyclo_Convertor_Heading = (Node) doc
							.createElement("heading");
					Text Cyclo_Convertor_Heading_Text = doc
							.createTextNode(EncodeXML(report_Details
									.get(XML_Values.CYCLO_CONVERTER_HEADING)));
					Cyclo_Convertor_Heading
							.appendChild(Cyclo_Convertor_Heading_Text);
					Cyclo_Convertor_Node.appendChild(Cyclo_Convertor_Heading);

					Node Cyclo_Convertor_Values = (Node) doc
							.createElement("value");
					Text Cyclo_Convertor_Value_Text = doc
							.createTextNode(EncodeXML(report_Details
									.get(XML_Values.CYCLO_CONVERTER)));
					Cyclo_Convertor_Values
							.appendChild(Cyclo_Convertor_Value_Text);
					Cyclo_Convertor_Node.appendChild(Cyclo_Convertor_Values);

					desc_of_Service_Performed_Node.replaceChild(
							Cyclo_Convertor_Node, node_Cyclo_Convertor_old);

				} catch (Exception e) {
					e.printStackTrace();
				}

				// List of distribution
				try {
					if (report_Details.get("Child_LOD") != null) {
						int count_LOD = Integer.parseInt(report_Details
								.get("Child_LOD"));
						if (count_LOD != 0) {

							XPathFactory xpf_LOD = XPathFactory.newInstance();
							XPath xpath_LOD = xpf_LOD.newXPath();
							XPathExpression expression_LOD = xpath_LOD
									.compile("//callback/body/"
											+ XML_Values.PROJECT
											+ "/report_details/"
											+ XML_Values.LIST_OF_DIST);

							Node node_LOD = (Node) expression_LOD.evaluate(doc,
									XPathConstants.NODE);
							try {
								node_LOD.getParentNode().removeChild(node_LOD);
							} catch (Exception e) {
								e.printStackTrace();
							}

							Node itemTag_LOD = (Node) doc
									.createElement(XML_Values.LIST_OF_DIST);

							for (int j = 0; j < count_LOD; j++) {

								if (!report_Details
										.get(XML_Values.LOCATION + j)
										.equals("")
										|| !report_Details.get(
												XML_Values.DEPT_LOC + j)
												.equals("")
										|| !report_Details.get(
												XML_Values.NAME + j).equals("")
										|| !report_Details.get(
												XML_Values.REMARKS_LOD + j)
												.equals("")) {
									Node itemTag_ITEM_LOD = (Node) doc
											.createElement(XML_Values.ITEM);
									itemTag_LOD.appendChild(itemTag_ITEM_LOD);

									Text location_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.LOCATION
															+ j)));
									Node location_Tag = (Node) doc
											.createElement(XML_Values.LOCATION);
									((Node) location_Tag)
											.appendChild(location_Text);

									itemTag_ITEM_LOD.appendChild(location_Tag);

									Text dept_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.DEPT_LOC
															+ j)));
									Node dept_Tag = (Node) doc
											.createElement(XML_Values.DEPT_LOC);
									((Node) dept_Tag).appendChild(dept_Text);

									itemTag_ITEM_LOD.appendChild(dept_Tag);

									Text name_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.NAME + j)));
									Node name_Tag = (Node) doc
											.createElement(XML_Values.NAME);
									((Node) name_Tag).appendChild(name_Text);

									itemTag_ITEM_LOD.appendChild(name_Tag);

									Text remarks_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.REMARKS_LOD
															+ j)));
									Node remarks_Tag = (Node) doc
											.createElement(XML_Values.REMARKS_LOD);
									((Node) remarks_Tag)
											.appendChild(remarks_Text);

									itemTag_ITEM_LOD.appendChild(remarks_Tag);
								}
							}
							// Description of service performed:
							Node decs_of_service_performed_node = doc
									.getElementsByTagName(
											XML_Values.DESC_OF_SERVICE_PERFORMED)
									.item(0);

							report_Details1.insertBefore(itemTag_LOD,
									decs_of_service_performed_node);
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// Distribution to customer
				XPathFactory xpf_DTC = XPathFactory.newInstance();
				XPath xpath_DTC = xpf_DTC.newXPath();
				XPathExpression expression_DTC = xpath_DTC
						.compile("//callback/body/" + XML_Values.PROJECT
								+ "/report_details/"
								+ XML_Values.DISTRIBUTION_TO_CUSTOMER);

				try {
					if (report_Details.get("Child_DTC") != null) {
						int count_DTC = Integer.parseInt(report_Details
								.get("Child_DTC"));
						if (count_DTC != 0) {
							Node node_DTC = (Node) expression_DTC.evaluate(doc,
									XPathConstants.NODE);
							try {
								node_DTC.getParentNode().removeChild(node_DTC);
							} catch (Exception e) {
								e.printStackTrace();
							}

							Node itemTag = (Node) doc
									.createElement(XML_Values.DISTRIBUTION_TO_CUSTOMER);

							for (int i = 0; i < count_DTC; i++) {

								if (!report_Details.get(XML_Values.SEND_BY + i)
										.equals("")
										|| !report_Details.get(
												XML_Values.TO + i).equals("")
										|| !report_Details.get(
												XML_Values.DATE_DTC + i)
												.equals("")
										|| !report_Details.get(
												XML_Values.CUSTOMER_COMMENTS
														+ i).equals("")
										|| !report_Details.get(
												XML_Values.COMMENTS_INTEGRATED
														+ i).equals("")) {
									Node itemTag_ITEM = (Node) doc
											.createElement(XML_Values.ITEM);
									itemTag.appendChild(itemTag_ITEM);

									Text send_by_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.SEND_BY + i)));
									Node send_by_Tag = (Node) doc
											.createElement(XML_Values.SEND_BY);
									((Node) send_by_Tag)
											.appendChild(send_by_Text);

									itemTag_ITEM.appendChild(send_by_Tag);

									Text to_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.TO + i)));
									Node to_Tag = (Node) doc
											.createElement(XML_Values.TO);
									((Node) to_Tag).appendChild(to_Text);

									itemTag_ITEM.appendChild(to_Tag);

									Text date_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.DATE_DTC
															+ i)));
									Node date_Tag = (Node) doc
											.createElement(XML_Values.DATE_DTC);
									((Node) date_Tag).appendChild(date_Text);

									itemTag_ITEM.appendChild(date_Tag);

									Text customer_comments_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.CUSTOMER_COMMENTS
															+ i)));
									Node customer_comments_Tag = (Node) doc
											.createElement(XML_Values.CUSTOMER_COMMENTS);
									((Node) customer_comments_Tag)
											.appendChild(customer_comments_Text);

									itemTag_ITEM
											.appendChild(customer_comments_Tag);

									Text comments_integrated_Text = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.COMMENTS_INTEGRATED
															+ i)));
									Node comments_integrated_Tag = (Node) doc
											.createElement(XML_Values.COMMENTS_INTEGRATED);
									((Node) comments_integrated_Tag)
											.appendChild(comments_integrated_Text);

									itemTag_ITEM
											.appendChild(comments_integrated_Tag);

								}
							}
							// Description of service performed:
							Node decs_of_service_performed_node = doc
									.getElementsByTagName(
											XML_Values.DESC_OF_SERVICE_PERFORMED)
									.item(0);

							report_Details1.insertBefore(itemTag,
									decs_of_service_performed_node);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// Personnel involved from ABB
				try {
					XPathFactory xpf_PIF_ABB = XPathFactory.newInstance();
					XPath xpath_PIF_ABB = xpf_PIF_ABB.newXPath();
					XPathExpression expression_PIF_ABB = xpath_PIF_ABB
							.compile("//callback/body/" + XML_Values.PROJECT
									+ "/report_details/"
									+ XML_Values.PERSONNEL_INVOLVED_FROM_ABB);
					Node node_PIF_ABB = (Node) expression_PIF_ABB.evaluate(doc,
							XPathConstants.NODE);

					try {
						node_PIF_ABB.getParentNode().removeChild(node_PIF_ABB);
					} catch (Exception e) {
						// TODO: handle exception
					}

					if (report_Details.get(XML_Values.PERSONNEL_ABB_COUNT) != null) {
						int count_Personnel_ABB = Integer
								.parseInt(report_Details
										.get(XML_Values.PERSONNEL_ABB_COUNT));

						if (count_Personnel_ABB != 0) {

							Node Personnel_ABB = (Node) doc
									.createElement(XML_Values.PERSONNEL_INVOLVED_FROM_ABB);

							for (int i = 0; i < count_Personnel_ABB; i++) {

								if (!report_Details.get(
										XML_Values.PERSONNEL_ABB_N + i).equals(
										"")
										|| !report_Details
												.get(XML_Values.PERSONNEL_ABB_SURNAME_NAME
														+ i).equals("")
										|| !report_Details.get(
												XML_Values.PERSONNEL_ABB_CAT
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_ABB_FUNCTION
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_ABB_DEPT_LOC
														+ i).equals("")) {
									Node itemTag_Personnel_ABB_Item = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_ITEM);
									Personnel_ABB
											.appendChild(itemTag_Personnel_ABB_Item);

									// ABB: N
									Node node_N_ABB = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_N);
									Text Text_N_ABB = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_N
															+ i)));
									node_N_ABB.appendChild(Text_N_ABB);
									itemTag_Personnel_ABB_Item
											.appendChild(node_N_ABB);

									// ABB: Surname, name
									Node node_Surname = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_SURNAME_NAME);
									Text Text_Surname = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_SURNAME_NAME
															+ i)));
									node_Surname.appendChild(Text_Surname);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Surname);

									// ABB: Cat
									Node node_Cat = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_CAT);
									Text Text_Cat = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_CAT
															+ i)));
									node_Cat.appendChild(Text_Cat);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Cat);

									// ABB: Function
									Node node_Function = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_FUNCTION);
									Text Text_Function = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_FUNCTION
															+ i)));
									node_Function.appendChild(Text_Function);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Function);

									// ABB: Arrival Date
									Node node_Arrival_date = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE);
									Text Text_Arrival_date = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE
															+ i)));
									node_Arrival_date
											.appendChild(Text_Arrival_date);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Arrival_date);

									// ABB: Departure Date
									Node node_Departure_date = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE);
									Text Text_Departure_date = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE
															+ i)));
									node_Departure_date
											.appendChild(Text_Departure_date);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Departure_date);

									// ABB: Dept Loc
									Node node_Dept_Loc = (Node) doc
											.createElement(XML_Values.PERSONNEL_ABB_DEPT_LOC);
									Text Text_Dept_Loc = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_ABB_DEPT_LOC
															+ i)));
									node_Dept_Loc.appendChild(Text_Dept_Loc);
									itemTag_Personnel_ABB_Item
											.appendChild(node_Dept_Loc);

								}
							}
							// Description of service performed:
							Node decs_of_service_performed_node = doc
									.getElementsByTagName(
											XML_Values.DESC_OF_SERVICE_PERFORMED)
									.item(0);

							report_Details1.insertBefore(Personnel_ABB,
									decs_of_service_performed_node);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// Personnel involved from Client
				try {
					XPathFactory xpf_PIF_Client = XPathFactory.newInstance();
					XPath xpath_PIF_Client = xpf_PIF_Client.newXPath();
					XPathExpression expression_PIF_Client = xpath_PIF_Client
							.compile("//callback/body/" + XML_Values.PROJECT
									+ "/report_details/"
									+ XML_Values.PERSONNEL_INVOLVED_FROM_CLIENT);
					Node node_PIF_Client = (Node) expression_PIF_Client
							.evaluate(doc, XPathConstants.NODE);

					try {
						node_PIF_Client.getParentNode().removeChild(
								node_PIF_Client);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (report_Details.get(XML_Values.PERSONNEL_CLIENT_COUNT) != null) {
						int count_Personnel_Client = Integer
								.parseInt(report_Details
										.get(XML_Values.PERSONNEL_CLIENT_COUNT));

						if (count_Personnel_Client != 0) {

							Node Personnel_Client = (Node) doc
									.createElement(XML_Values.PERSONNEL_INVOLVED_FROM_CLIENT);

							for (int i = 0; i < count_Personnel_Client; i++) {

								if (!report_Details.get(
										XML_Values.PERSONNEL_CLIENT_N + i)
										.equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_CLIENT_SURNAME
														+ i).equals("")
										|| !report_Details.get(
												XML_Values.PERSONNEL_CLIENT_CAT
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_CLIENT_FUNCTION
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_CLIENT_FROM
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_CLIENT_TILL
														+ i).equals("")
										|| !report_Details
												.get(XML_Values.PERSONNEL_CLIENT_DEPT_LOC
														+ i).equals("")) {
									Node itemTag_Personnel_Client_Item = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_ITEM);
									Personnel_Client
											.appendChild(itemTag_Personnel_Client_Item);

									// Client: N
									Node node_N_Client = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_N);
									Text Text_N_Client = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_N
															+ i)));
									node_N_Client.appendChild(Text_N_Client);
									itemTag_Personnel_Client_Item
											.appendChild(node_N_Client);

									// Client: Surname, name
									Node node_Surname = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_SURNAME);
									Text Text_Surname = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_SURNAME
															+ i)));
									node_Surname.appendChild(Text_Surname);
									itemTag_Personnel_Client_Item
											.appendChild(node_Surname);

									// Client: Cat
									Node node_Cat = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_CAT);
									Text Text_Cat = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_CAT
															+ i)));
									node_Cat.appendChild(Text_Cat);
									itemTag_Personnel_Client_Item
											.appendChild(node_Cat);

									// Client: Function
									Node node_Function = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_FUNCTION);
									Text Text_Function = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_FUNCTION
															+ i)));
									node_Function.appendChild(Text_Function);
									itemTag_Personnel_Client_Item
											.appendChild(node_Function);

									// Client: from
									Node node_from = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_FROM);
									Text Text_from = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_FROM
															+ i)));
									node_from.appendChild(Text_from);
									itemTag_Personnel_Client_Item
											.appendChild(node_from);

									// Client: till
									Node node_till = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_TILL);
									Text Text_tille = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_TILL
															+ i)));
									node_till.appendChild(Text_tille);
									itemTag_Personnel_Client_Item
											.appendChild(node_till);

									// Client: Dept Loc
									Node node_Dept_Loc = (Node) doc
											.createElement(XML_Values.PERSONNEL_CLIENT_DEPT_LOC);
									Text Text_Dept_Loc = doc
											.createTextNode(EncodeXML(report_Details
													.get(XML_Values.PERSONNEL_CLIENT_DEPT_LOC
															+ i)));
									node_Dept_Loc.appendChild(Text_Dept_Loc);
									itemTag_Personnel_Client_Item
											.appendChild(node_Dept_Loc);

								}
							}
							// Description of service performed:
							Node decs_of_service_performed_node = doc
									.getElementsByTagName(
											XML_Values.DESC_OF_SERVICE_PERFORMED)
									.item(0);

							report_Details1.insertBefore(Personnel_Client,
									decs_of_service_performed_node);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// mill specific
				{
					String szExpression = "//callback/body/"
							+ XML_Values.PROJECT
							+ "/report_details/"
							+ XML_Values.REPORTS
							+ "/"
							+ XML_Values.MILL
							+ "[@id='"
							+ Util.getMillID(AppValues.AppContext,
									Util.getMillName(AppValues.AppContext))
							+ "']";
					XPathFactory xpf_mill = XPathFactory.newInstance();
					XPath xpath_mill = xpf_mill.newXPath();

					XPathExpression expression_mill;

					// Plant
					if (report_Details.containsKey(XML_Values.PLANT)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT);
						Node node_Plant = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						node_Plant.setTextContent(report_Details
								.get(XML_Values.PLANT));
					}
					// Plant type:Cyclo
					if (report_Details.containsKey(XML_Values.PLANT_TYPE_CYCLO)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_CYCLO);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_CYCLO));
					}
					// Plant type:Drive
					if (report_Details.containsKey(XML_Values.PLANT_TYPE_DRIVE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_DRIVE);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_DRIVE));
					}// Plant type:E-house
					if (report_Details
							.containsKey(XML_Values.PLANT_TYPE_EHOUSE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_EHOUSE);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_EHOUSE));
					}// Plant type:GMD
					if (report_Details.containsKey(XML_Values.PLANT_TYPE_GMD)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_GMD);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_GMD));
					}// Plant type:Motor
					if (report_Details.containsKey(XML_Values.PLANT_TYPE_MOTOR)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_MOTOR);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_MOTOR));
					}// Plant type:Transformer
					if (report_Details
							.containsKey(XML_Values.PLANT_TYPE_TRANSFORMER)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.PLANT_TYPE_TRANSFORMER);
						Node plant_type_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						plant_type_node.setTextContent(report_Details
								.get(XML_Values.PLANT_TYPE_TRANSFORMER));
					}
					// Unit
					if (report_Details.containsKey(XML_Values.UNIT)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.UNIT);
						Node unit_node = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						unit_node.setTextContent(report_Details
								.get(XML_Values.UNIT));
					}
					// System Machine
					if (report_Details.containsKey(XML_Values.SYSTEM_MACHINE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SYSTEM_MACHINE);
						Node system_Machine_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						system_Machine_node.setTextContent(report_Details
								.get(XML_Values.SYSTEM_MACHINE));
					}
					// Type
					if (report_Details.containsKey(XML_Values.TYPE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.TYPE);
						Node type_node = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						type_node.setTextContent(report_Details
								.get(XML_Values.TYPE));
					}
					// Serial number
					if (report_Details.containsKey(XML_Values.SERIAL_NO)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERIAL_NO);
						Node serial_no_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						serial_no_node.setTextContent(report_Details
								.get(XML_Values.SERIAL_NO));
					}
					// Year of delivery
					if (report_Details.containsKey(XML_Values.YEAR_OF_DELIVERY)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.YEAR_OF_DELIVERY);
						Node year_of_Delivery_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						year_of_Delivery_node.setTextContent(report_Details
								.get(XML_Values.YEAR_OF_DELIVERY));
					}
					// Operating hours
					if (report_Details.containsKey(XML_Values.OPERATING_HOURS)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.OPERATING_HOURS);
						Node Operating_Hrs_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Operating_Hrs_node.setTextContent(report_Details
								.get(XML_Values.OPERATING_HOURS));
					}
					// Service Information=> Service type
					// New Plant
					if (report_Details.containsKey(XML_Values.NEW_PLANT)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.NEW_PLANT);
						Node new_Plant_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						new_Plant_node.setTextContent(report_Details
								.get(XML_Values.NEW_PLANT));
					}
					// Upgrade
					if (report_Details.containsKey(XML_Values.UPGRADE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.UPGRADE);
						Node Upgrade_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						Upgrade_node.setTextContent(report_Details
								.get(XML_Values.UPGRADE));
					}
					// Trouble shooting
					if (report_Details.containsKey(XML_Values.TROUBLESHOOTING)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.TROUBLESHOOTING);
						Node Troubleshooting_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Troubleshooting_node.setTextContent(report_Details
								.get(XML_Values.TROUBLESHOOTING));
					}
					// Others
					if (report_Details.containsKey(XML_Values.OTHERS)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.OTHERS);
						Node Others_node = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						Others_node.setTextContent(report_Details
								.get(XML_Values.OTHERS));
					}
					// First Installation
					if (report_Details
							.containsKey(XML_Values.FIRST_INSTALLATION)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.FIRST_INSTALLATION);
						Node First_Installation_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						First_Installation_node.setTextContent(report_Details
								.get(XML_Values.FIRST_INSTALLATION));
					}
					// Minor Inspection
					if (report_Details.containsKey(XML_Values.MINOR_INSPECTION)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.MINOR_INSPECTION);
						Node Minor_Inspection_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Minor_Inspection_node.setTextContent(report_Details
								.get(XML_Values.MINOR_INSPECTION));
					}
					// Major Overhaul
					if (report_Details.containsKey(XML_Values.MAJOR_OVERHAUL)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TYPE + "/"
								+ XML_Values.MAJOR_OVERHAUL);
						Node Major_Overhaul_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Major_Overhaul_node.setTextContent(report_Details
								.get(XML_Values.MAJOR_OVERHAUL));
					}
					// Service Information=> Service task
					// Installation
					if (report_Details.containsKey(XML_Values.INSTALLATION)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.INSTALLATION);
						Node Installation_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Installation_node.setTextContent(report_Details
								.get(XML_Values.INSTALLATION));
					}
					// Comissioning
					if (report_Details.containsKey(XML_Values.COMISSIONING)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.COMISSIONING);
						Node Comissiong_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						Comissiong_node.setTextContent(report_Details
								.get(XML_Values.COMISSIONING));
					}
					// Test assesment
					if (report_Details.containsKey(XML_Values.TEST_ASSESSMENT)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.TEST_ASSESSMENT);
						Node Test_Assessment_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Test_Assessment_node.setTextContent(report_Details
								.get(XML_Values.TEST_ASSESSMENT));
					}
					// Others
					if (report_Details
							.containsKey(XML_Values.OTHERS_SERVICE_TASK)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.OTHERS_SERVICE_TASK);
						Node Other_Service_Task_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Other_Service_Task_node.setTextContent(report_Details
								.get(XML_Values.OTHERS_SERVICE_TASK));
					}
					// Overall works
					if (report_Details.containsKey(XML_Values.OVERHAUL_WORKS)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.OVERHAUL_WORKS);
						Node Overhaul_Works_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Overhaul_Works_node.setTextContent(report_Details
								.get(XML_Values.OVERHAUL_WORKS));
					}
					// Re-Comissioning
					if (report_Details.containsKey(XML_Values.RE_COMISSIONING)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.RE_COMISSIONING);
						Node Re_Commsissioning_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Re_Commsissioning_node.setTextContent(report_Details
								.get(XML_Values.RE_COMISSIONING));
					}
					// Diagnostics
					if (report_Details.containsKey(XML_Values.DIAGNOSTICS)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.DIAGNOSTICS);
						Node Diagnotics_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						Diagnotics_node.setTextContent(report_Details
								.get(XML_Values.DIAGNOSTICS));
					}
					// Factory insp.
					if (report_Details.containsKey(XML_Values.FACTORY_INSP)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.FACTORY_INSP);
						Node Factory_Insp_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Factory_Insp_node.setTextContent(report_Details
								.get(XML_Values.FACTORY_INSP));
					}
					// Repair
					if (report_Details.containsKey(XML_Values.REPAIR)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.REPAIR);
						Node Repair_node = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						Repair_node.setTextContent(report_Details
								.get(XML_Values.REPAIR));
					}
					// Investigation
					if (report_Details.containsKey(XML_Values.INVESTIGATION)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.INVESTIGATION);
						Node Investigation_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Investigation_node.setTextContent(report_Details
								.get(XML_Values.INVESTIGATION));
					}
					// Inspection
					if (report_Details.containsKey(XML_Values.INSPECTION)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.INSPECTION);
						Node Inspection_node = (Node) expression_mill.evaluate(
								doc, XPathConstants.NODE);
						Inspection_node.setTextContent(report_Details
								.get(XML_Values.INSPECTION));
					}
					// Motor Insp. Inside
					if (report_Details
							.containsKey(XML_Values.MOTOR_INSP_INSIDE)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.MOTOR_INSP_INSIDE);
						Node Motor_Insp_Inside_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Motor_Insp_Inside_node.setTextContent(report_Details
								.get(XML_Values.MOTOR_INSP_INSIDE));
					}
					// Motor insp External
					if (report_Details
							.containsKey(XML_Values.MOTOR_INSP_EXTERNAL)) {
						expression_mill = xpath_mill.compile(szExpression + "/"
								+ XML_Values.SERVICE_INFORMATION + "/"
								+ XML_Values.SERVICE_TASKS + "/"
								+ XML_Values.MOTOR_INSP_EXTERNAL);
						Node Motor_Insp_External_node = (Node) expression_mill
								.evaluate(doc, XPathConstants.NODE);
						Motor_Insp_External_node.setTextContent(report_Details
								.get(XML_Values.MOTOR_INSP_EXTERNAL));
					}

					// try {

					// Plant
					// Node plant_node =
					// doc.getElementsByTagName(XML_Values.PLANT)
					// .item(0);
					// plant_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.PLANT)));

					// Plant type
					// if
					// (report_Details.containsValue(XML_Values.PLANT_TYPE_CYCLO))
					// {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_CYCLO).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_CYCLO));
					// }
					// if
					// (report_Details.containsValue(XML_Values.PLANT_TYPE_DRIVE))
					// {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_DRIVE).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_DRIVE));
					// }
					// if
					// (report_Details.containsValue(XML_Values.PLANT_TYPE_EHOUSE))
					// {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_EHOUSE).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_EHOUSE));
					// }
					// if
					// (report_Details.containsValue(XML_Values.PLANT_TYPE_GMD))
					// {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_GMD).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_GMD));
					// }
					// if
					// (report_Details.containsValue(XML_Values.PLANT_TYPE_MOTOR))
					// {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_MOTOR).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_MOTOR));
					// }
					// if (report_Details
					// .containsValue(XML_Values.PLANT_TYPE_TRANSFORMER)) {
					// Node plant_type_node = doc.getElementsByTagName(
					// XML_Values.PLANT_TYPE_TRANSFORMER).item(0);
					//
					// plant_type_node.setTextContent(report_Details
					// .get(XML_Values.PLANT_TYPE_TRANSFORMER));
					// }

					// // Unit
					// Node unit_node =
					// doc.getElementsByTagName(XML_Values.UNIT)
					// .item(0);
					// unit_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.UNIT)));

					// // System_Machine
					// Node system_Machine_node = doc.getElementsByTagName(
					// XML_Values.SYSTEM_MACHINE).item(0);
					// system_Machine_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.SYSTEM_MACHINE)));
					//
					// // Type
					// Node type_node =
					// doc.getElementsByTagName(XML_Values.TYPE)
					// .item(0);
					// type_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.TYPE)));

					// // Serial No
					// Node serial_No_node = doc.getElementsByTagName(
					// XML_Values.SERIAL_NO).item(0);
					// serial_No_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.SERIAL_NO)));

					// Year of Delivery
					// Node year_of_Delivery_node = doc.getElementsByTagName(
					// XML_Values.YEAR_OF_DELIVERY).item(0);
					// year_of_Delivery_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.YEAR_OF_DELIVERY)));

					// Operating hours
					// Node operating_hr_node = doc.getElementsByTagName(
					// XML_Values.OPERATING_HOURS).item(0);
					// operating_hr_node.setTextContent(EncodeXML(report_Details
					// .get(XML_Values.OPERATING_HOURS)));

					// Service Information=> Service type
					// // New Plant
					// Node new_plant_node = doc.getElementsByTagName(
					// XML_Values.NEW_PLANT).item(0);
					// new_plant_node.setTextContent(report_Details
					// .get(XML_Values.NEW_PLANT));

					// // Upgrade
					// Node upgrade_node = doc
					// .getElementsByTagName(XML_Values.UPGRADE).item(0);
					// upgrade_node.setTextContent(report_Details
					// .get(XML_Values.UPGRADE));

					// // Trouble shooting
					// Node trouble_shooting_node = doc.getElementsByTagName(
					// XML_Values.TROUBLESHOOTING).item(0);
					// trouble_shooting_node.setTextContent(report_Details
					// .get(XML_Values.TROUBLESHOOTING));

					// Others
					// Node others_node =
					// doc.getElementsByTagName(XML_Values.OTHERS)
					// .item(0);
					// others_node.setTextContent(report_Details
					// .get(XML_Values.OTHERS));
					//
					// // First Installation
					// Node first_installation_node = doc.getElementsByTagName(
					// XML_Values.FIRST_INSTALLATION).item(0);
					// first_installation_node.setTextContent(report_Details
					// .get(XML_Values.FIRST_INSTALLATION));

					// // Minor Inspection
					// Node minor_Ins_node = doc.getElementsByTagName(
					// XML_Values.MINOR_INSPECTION).item(0);
					// minor_Ins_node.setTextContent(report_Details
					// .get(XML_Values.MINOR_INSPECTION));
					//
					// // Major Overhaul
					// Node major_overhaul_node = doc.getElementsByTagName(
					// XML_Values.MAJOR_OVERHAUL).item(0);
					// major_overhaul_node.setTextContent(report_Details
					// .get(XML_Values.MAJOR_OVERHAUL));

					// Service Information=> Service task
					// // Installation
					// Node installatiom_node = doc.getElementsByTagName(
					// XML_Values.INSTALLATION).item(0);
					// installatiom_node.setTextContent(report_Details
					// .get(XML_Values.INSTALLATION));

					// // Comissioning
					// Node comissioning_node = doc.getElementsByTagName(
					// XML_Values.COMISSIONING).item(0);
					// comissioning_node.setTextContent(report_Details
					// .get(XML_Values.COMISSIONING));
					//
					// // Test Assessment
					// Node test_assessment_node = doc.getElementsByTagName(
					// XML_Values.TEST_ASSESSMENT).item(0);
					// test_assessment_node.setTextContent(report_Details
					// .get(XML_Values.TEST_ASSESSMENT));

					// Others
					// Node others_Servicetask_node = doc.getElementsByTagName(
					// XML_Values.OTHERS_SERVICE_TASK).item(0);
					// others_Servicetask_node.setTextContent(report_Details
					// .get(XML_Values.OTHERS_SERVICE_TASK));

					// Overall works
					// Node overall_node = doc.getElementsByTagName(
					// XML_Values.OVERHAUL_WORKS).item(0);
					// overall_node.setTextContent(report_Details
					// .get(XML_Values.OVERHAUL_WORKS));
					//
					// // Re-Comissioning
					// Node re_Comm_node = doc.getElementsByTagName(
					// XML_Values.RE_COMISSIONING).item(0);
					// re_Comm_node.setTextContent(report_Details
					// .get(XML_Values.RE_COMISSIONING));

					// Diagnostics
					// Node diagnostics_node = doc.getElementsByTagName(
					// XML_Values.DIAGNOSTICS).item(0);
					// diagnostics_node.setTextContent(report_Details
					// .get(XML_Values.DIAGNOSTICS));
					//
					// // Factory Insp.
					// Node factory_insp_node = doc.getElementsByTagName(
					// XML_Values.FACTORY_INSP).item(0);
					// factory_insp_node.setTextContent(report_Details
					// .get(XML_Values.FACTORY_INSP));

					// // Repair
					// Node repair_node =
					// doc.getElementsByTagName(XML_Values.REPAIR)
					// .item(0);
					// repair_node.setTextContent(report_Details
					// .get(XML_Values.REPAIR));
					//
					// // Investigation
					// Node investigation_node = doc.getElementsByTagName(
					// XML_Values.INVESTIGATION).item(0);
					// investigation_node.setTextContent(report_Details
					// .get(XML_Values.INVESTIGATION));

					// Inspection
					// Node inspection_node = doc.getElementsByTagName(
					// XML_Values.INSPECTION).item(0);
					// inspection_node.setTextContent(report_Details
					// .get(XML_Values.INSPECTION));
					//
					// // Motor Insp. Inside
					// Node motor_insp_inside_node = doc.getElementsByTagName(
					// XML_Values.MOTOR_INSP_INSIDE).item(0);
					// motor_insp_inside_node.setTextContent(report_Details
					// .get(XML_Values.MOTOR_INSP_INSIDE));
					//
					// // Motor Insp. External
					// Node motor_insp_external_node = doc.getElementsByTagName(
					// XML_Values.MOTOR_INSP_EXTERNAL).item(0);
					// motor_insp_external_node.setTextContent(report_Details
					// .get(XML_Values.MOTOR_INSP_EXTERNAL));
					// } catch (Exception e) {
					// e.printStackTrace();
					// }

				}
				Transformer transformer = TransformerFactory.newInstance()
						.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				// initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(file_report);
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result);

				Util.EncryptReportXML();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save_Summary(HashMap<String, String> hm_summary) {
		try {

			get_File();

			Node summary = doc.getElementsByTagName("summary").item(0);

			// Summary of Inspection:
			XPathFactory xpf_SOI = XPathFactory.newInstance();
			XPath xpath_SOI = xpf_SOI.newXPath();
			XPathExpression expression_SOI = xpath_SOI
					.compile("//callback/body/" + XML_Values.PROJECT
							+ "/summary/" + XML_Values.SUMMARY_OF_INSPECTION);

			if (hm_summary.get("Child_SOI") != null) {
				count_SOI = Integer.parseInt(hm_summary.get("Child_SOI"));
				// Log.v("***********************SOI****************", "" +
				// count_SOI);

				if (count_SOI != 0) {
					Node node_SOI = (Node) expression_SOI.evaluate(doc,
							XPathConstants.NODE);
					try {
						node_SOI.getParentNode().removeChild(node_SOI);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Node itemTag_SOI = (Node) doc
							.createElement(XML_Values.SUMMARY_OF_INSPECTION);

					for (int j = 0; j < count_SOI; j++) {
						Node itemTag_ITEM_SOI = (Node) doc
								.createElement(XML_Values.ITEM);
						itemTag_SOI.appendChild(itemTag_ITEM_SOI);
						Text item_Text = doc
								.createTextNode(EncodeXML(hm_summary
										.get(XML_Values.SUMMARY_OF_INSPECTION
												+ j)));

						((Node) itemTag_ITEM_SOI).appendChild(item_Text);

					}

					summary.appendChild(itemTag_SOI);
				}
			}

			// Service Information:
			XPathFactory xpf_SI = XPathFactory.newInstance();
			XPath xpath_SI = xpf_SI.newXPath();
			XPathExpression expression_SI = xpath_SI.compile("//callback/body/"
					+ XML_Values.PROJECT + "/summary/"
					+ XML_Values.SERVICE_INFORMATION);

			if (hm_summary.get(XML_Values.SERVICE_INFORMATION_COUNT) != null) {
				count_SI = Integer.parseInt(hm_summary
						.get(XML_Values.SERVICE_INFORMATION_COUNT));
				try {
					if (count_SI != 0) {
						Node node_SI = (Node) expression_SI.evaluate(doc,
								XPathConstants.NODE);
						try {
							node_SI.getParentNode().removeChild(node_SI);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Node Tag_SI = (Node) doc
								.createElement(XML_Values.SERVICE_INFORMATION);

						for (int j = 0; j < count_SI; j++) {

							Node item_SI = (Node) doc
									.createElement(XML_Values.SERVICE_INFORMATION_ITEM);

							Tag_SI.appendChild(item_SI);

							// Number
							Node item_SI_No = (Node) doc
									.createElement(XML_Values.SERVICE_INFORMATION_N);
							Text Text_SI_No = doc
									.createTextNode(EncodeXML(hm_summary
											.get(XML_Values.SERVICE_INFORMATION_N
													+ j)));
							item_SI_No.appendChild(Text_SI_No);
							item_SI.appendChild(item_SI_No);

							// Description
							Node item_SI_Desc = (Node) doc
									.createElement(XML_Values.SERVICE_INFORMATION_DESCRIPTION);
							Text Text_SI_Desc = doc
									.createTextNode(EncodeXML(hm_summary
											.get(XML_Values.SERVICE_INFORMATION_DESCRIPTION
													+ j)));
							item_SI_Desc.appendChild(Text_SI_Desc);
							item_SI.appendChild(item_SI_Desc);

							// From
							Node item_SI_From = (Node) doc
									.createElement(XML_Values.SERVICE_INFORMATION_FROM);
							Text Text_SI_From = doc
									.createTextNode(EncodeXML(hm_summary
											.get(XML_Values.SERVICE_INFORMATION_FROM
													+ j)));
							item_SI_From.appendChild(Text_SI_From);
							item_SI.appendChild(item_SI_From);

							// Till
							Node item_SI_Till = (Node) doc
									.createElement(XML_Values.SERVICE_INFORMATION_TILL);
							Text Text_SI_Till = doc
									.createTextNode(EncodeXML(hm_summary
											.get(XML_Values.SERVICE_INFORMATION_TILL
													+ j)));
							item_SI_Till.appendChild(Text_SI_Till);
							item_SI.appendChild(item_SI_Till);

						}
						summary.appendChild(Tag_SI);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Mill specific::

			{
				String szExpression = "//callback/body/"
						+ XML_Values.PROJECT
						+ "/summary/"
						+ XML_Values.REPORTS
						+ "/"
						+ XML_Values.MILL
						+ "[@id='"
						+ Util.getMillID(AppValues.AppContext,
								Util.getMillName(AppValues.AppContext)) + "']";
				XPathFactory xpf_mill = XPathFactory.newInstance();
				XPath xpath_mill = xpf_mill.newXPath();

				XPathExpression expression_mill;
				Node Mill = null;

				try {
					expression_mill = xpath_mill.compile(szExpression);
					Mill = (Node) expression_mill.evaluate(doc,
							XPathConstants.NODE);
					Log.v("******************* Mill name**************", ""
							+ Mill.getNodeName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				// if (report_Details.containsKey(XML_Values.NEW_PLANT)) {
				// expression_mill = xpath_mill
				// .compile(szExpression + "/" + XML_Values.SERVICE_INFORMATION
				// + "/" + XML_Values.SERVICE_TYPE + "/" +
				// XML_Values.NEW_PLANT);
				// Node new_Plant_node = (Node) expression_mill.evaluate(doc,
				// XPathConstants.NODE);
				// new_Plant_node.setTextContent(report_Details.get(XML_Values.NEW_PLANT));
				// }

				// XPathFactory xpf_MD = XPathFactory.newInstance();
				// XPath xpath_MD = xpf_MD.newXPath();
				// XPathExpression expression_MD = xpath_MD
				// .compile("//callback/body/" + XML_Values.PROJECT +
				// "/summary/"
				// + XML_Values.MAIN_DIFFICULTIES);

				// Main difficulties:
				if (hm_summary.get("Child_MD") != null) {
					count_MD = Integer.parseInt(hm_summary.get("Child_MD"));
					Log.v("***********************MD****************", ""
							+ count_MD);

					expression_mill = xpath_mill.compile(szExpression + "/"
							+ XML_Values.MAIN_DIFFICULTIES);

					if (count_MD != 0) {
						Node node_MD = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						try {
							node_MD.getParentNode().removeChild(node_MD);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Node itemTag_MD = (Node) doc
								.createElement(XML_Values.MAIN_DIFFICULTIES);

						for (int j = 0; j < count_MD; j++) {
							Node itemTag_ITEM_MD = (Node) doc
									.createElement(XML_Values.ITEM);
							itemTag_MD.appendChild(itemTag_ITEM_MD);

							Text item = doc.createTextNode(EncodeXML(hm_summary
									.get(XML_Values.MAIN_DIFFICULTIES + j)));

							((Node) itemTag_ITEM_MD).appendChild(item);

						}

						Mill.appendChild(itemTag_MD);
					}
				}

				// XPathFactory xpf_Recomm = XPathFactory.newInstance();
				// XPath xpath_Recomm = xpf_Recomm.newXPath();
				// XPathExpression expression_Recomm = xpath_Recomm
				// .compile("//callback/body/" + XML_Values.PROJECT +
				// "/summary/"
				// + XML_Values.RECOMMENDATIONS);

				// Recommendations:
				if (hm_summary.get("Child_Recom") != null) {
					count_Recomm = Integer.parseInt(hm_summary
							.get("Child_Recom"));
					expression_mill = xpath_mill.compile(szExpression + "/"
							+ XML_Values.RECOMMENDATIONS);

					if (count_Recomm != 0) {
						Node node_Recomm = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						try {
							node_Recomm.getParentNode()
									.removeChild(node_Recomm);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Node itemTag_Recomm = (Node) doc
								.createElement(XML_Values.RECOMMENDATIONS);

						for (int j = 0; j < count_Recomm; j++) {
							Node itemTag_ITEM_Recomm = (Node) doc
									.createElement(XML_Values.ITEM);
							itemTag_Recomm.appendChild(itemTag_ITEM_Recomm);

							Text item = doc.createTextNode(EncodeXML(hm_summary
									.get(XML_Values.RECOMMENDATIONS + j)));

							((Node) itemTag_ITEM_Recomm).appendChild(item);

						}

						Mill.appendChild(itemTag_Recomm);
					}
				}

				// XPathFactory xpf_POGI = XPathFactory.newInstance();
				// XPath xpath_POGI = xpf_POGI.newXPath();
				// XPathExpression expression_POGI = xpath_POGI
				// .compile("//callback/body/" + XML_Values.PROJECT +
				// "/summary/"
				// + XML_Values.POINT_OF_GENERAL_INTEREST);

				// Point of general interest:
				if (hm_summary.get("Child_POGI") != null) {
					count_POGI = Integer.parseInt(hm_summary.get("Child_POGI"));

					expression_mill = xpath_mill.compile(szExpression + "/"
							+ XML_Values.POINT_OF_GENERAL_INTEREST);

					if (count_POGI != 0) {
						Node node_POGI = (Node) expression_mill.evaluate(doc,
								XPathConstants.NODE);
						try {
							node_POGI.getParentNode().removeChild(node_POGI);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Node itemTag_POGI = (Node) doc
								.createElement(XML_Values.POINT_OF_GENERAL_INTEREST);

						for (int j = 0; j < count_POGI; j++) {
							Node itemTag_ITEM_POGI = (Node) doc
									.createElement(XML_Values.ITEM);
							itemTag_POGI.appendChild(itemTag_ITEM_POGI);

							Text item = doc.createTextNode(EncodeXML(hm_summary
									.get(XML_Values.POINT_OF_GENERAL_INTEREST
											+ j)));

							((Node) itemTag_ITEM_POGI).appendChild(item);

						}

						Mill.appendChild(itemTag_POGI);
					}
				}

				// //Main Motor Data:
				// XPathFactory xpf_MMD = XPathFactory.newInstance();
				// XPath xpath_MMD = xpf_MMD.newXPath();
				// XPathExpression expression_MMD = xpath_MMD
				// .compile("//callback/body/reports/summary/"
				// + XML_Values.MAIN_MOTOR_DATA);
				//
				// if (hm_summary.get(XML_Values.MAIN_MOTOR_DATA_COUNT) != null)
				// {
				// count_MMD =
				// Integer.parseInt(hm_summary.get(XML_Values.MAIN_MOTOR_DATA_COUNT));
				// try {
				// if (count_MMD != 0) {
				// Node node_MMD = (Node) expression_MMD.evaluate(doc,
				// XPathConstants.NODE);
				// try {
				// node_MMD.getParentNode().removeChild(node_MMD);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				// Node Tag_MMD = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA);
				//
				// for (int j = 0; j < count_MMD; j++) {
				// Node item_MMD = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA_ITEM);
				// Tag_MMD.appendChild(item_MMD);
				//
				// //Number
				// Node item_MMD_No = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA_N);
				// Text Text_MMD_No = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.MAIN_MOTOR_DATA_N
				// + j)));
				// item_MMD_No.appendChild(Text_MMD_No);
				// item_MMD.appendChild(item_MMD_No);
				//
				// //Description
				// Node item_MMD_Desc = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA_DESCRIPTION);
				// Text Text_MMD_Desc = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.MAIN_MOTOR_DATA_DESCRIPTION
				// + j)));
				// item_MMD_Desc.appendChild(Text_MMD_Desc);
				// item_MMD.appendChild(item_MMD_Desc);
				//
				// //Data
				// Node item_MMD_Data = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA_DATA);
				// Text Text_MMD_Data = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.MAIN_MOTOR_DATA_DATA
				// + j)));
				// item_MMD_Data.appendChild(Text_MMD_Data);
				// item_MMD.appendChild(item_MMD_Data);
				//
				// //Remarks
				// Node item_MMD_Remarks = (Node) doc
				// .createElement(XML_Values.MAIN_MOTOR_DATA_REMARKS);
				// Text Text_MMD_Remarks = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.MAIN_MOTOR_DATA_REMARKS
				// + j)));
				// item_MMD_Remarks.appendChild(Text_MMD_Remarks);
				// item_MMD.appendChild(item_MMD_Remarks);
				// }
				// summary.appendChild(Tag_MMD);
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }

				// //Specific Motor Data:
				// XPathFactory xpf_SMD = XPathFactory.newInstance();
				// XPath xpath_SMD = xpf_SMD.newXPath();
				// XPathExpression expression_SMD = xpath_SMD
				// .compile("//callback/body/reports/summary/"
				// + XML_Values.SPECIFIC_MOTOR_DATA);
				//
				// if (hm_summary.get(XML_Values.SPECIFIC_MOTOR_DATA_COUNT) !=
				// null) {
				// count_SMD =
				// Integer.parseInt(hm_summary.get(XML_Values.SPECIFIC_MOTOR_DATA_COUNT));
				//
				// try {
				// if (count_SMD != 0) {
				// Node node_SMD = (Node) expression_SMD.evaluate(doc,
				// XPathConstants.NODE);
				// try {
				// node_SMD.getParentNode().removeChild(node_SMD);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// Node Tag_SMD = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA);
				//
				// for (int j = 0; j < count_SMD; j++) {
				// Node item_SMD = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA_ITEM);
				// Tag_SMD.appendChild(item_SMD);
				//
				// //Number
				// Node item_SMD_No = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA_N);
				// Text Text_SMD_No = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.SPECIFIC_MOTOR_DATA_N
				// + j)));
				// item_SMD_No.appendChild(Text_SMD_No);
				// item_SMD.appendChild(item_SMD_No);
				//
				// //Description
				// Node item_SMD_Desc = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA_DESCRIPTION);
				// Text Text_SMD_Desc = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.SPECIFIC_MOTOR_DATA_DESCRIPTION
				// + j)));
				// item_SMD_Desc.appendChild(Text_SMD_Desc);
				// item_SMD.appendChild(item_SMD_Desc);
				//
				// //Data
				// Node item_SMD_Data = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA_DATA);
				// Text Text_SMD_Data = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.SPECIFIC_MOTOR_DATA_DATA
				// + j)));
				// item_SMD_Data.appendChild(Text_SMD_Data);
				// item_SMD.appendChild(item_SMD_Data);
				//
				// //Remarks
				// Node item_SMD_Remarks = (Node) doc
				// .createElement(XML_Values.SPECIFIC_MOTOR_DATA_REMARKS);
				// Text Text_SMD_Remarks = doc
				// .createTextNode(EncodeXML(hm_summary
				// .get(XML_Values.SPECIFIC_MOTOR_DATA_REMARKS
				// + j)));
				// item_SMD_Remarks.appendChild(Text_SMD_Remarks);
				// item_SMD.appendChild(item_SMD_Remarks);
				// }
				// summary.appendChild(Tag_SMD);
				// }
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				// }

			}
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	// public void save_Site_Conditions(HashMap<String, String>
	// hm_siteConditions) {
	// try {
	//
	// get_File();
	//
	// Node item_reports = doc.getElementsByTagName(XML_Values.REPORT)
	// .item(0);
	//
	// Node node_site_Conditions = doc.getElementsByTagName(
	// XML_Values.SITE_CONDITIONS).item(0);
	//
	// if (node_site_Conditions != null) {
	//
	// // Log.v("***************Already exists************::    ",
	// // "YES");
	//
	// Node itemTag_ITEM = (Node) doc.createElement(XML_Values.ITEM);
	// node_site_Conditions.appendChild(itemTag_ITEM);
	//
	// // Material Name:
	// Node item_Material_Name = (Node) doc
	// .createElement(XML_Values.MATERIAL_NAME);
	// Text data_Material_Name = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_NAME));
	// item_Material_Name.appendChild(data_Material_Name);
	//
	// itemTag_ITEM.appendChild(item_Material_Name);
	// // Material Category:
	//
	// Node item_Material_Category = (Node) doc
	// .createElement(XML_Values.MATERIAL_CATEGORY);
	// Text data_Material_Category = doc
	// .createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_CATEGORY));
	// item_Material_Category.appendChild(data_Material_Category);
	//
	// itemTag_ITEM.appendChild(item_Material_Category);
	//
	// // Information
	// Node item_Information = (Node) doc
	// .createElement(XML_Values.INFORMATION);
	// Text data_Information = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.INFORMATION));
	// item_Information.appendChild(data_Information);
	//
	// itemTag_ITEM.appendChild(item_Information);
	//
	// // Date
	// Node item_Date = (Node) doc
	// .createElement(XML_Values.MATERIAL_DATE);
	// Text data_date = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_DATE));
	// item_Date.appendChild(data_date);
	//
	// itemTag_ITEM.appendChild(item_Date);
	//
	// } else {
	// // Log.v("***************ALready exists************::  ", "NO");
	//
	// Node item_site_Conditions = (Node) doc
	// .createElement(XML_Values.SITE_CONDITIONS);
	//
	// item_reports.appendChild(item_site_Conditions);
	//
	// Node itemTag_ITEM = (Node) doc.createElement(XML_Values.ITEM);
	// item_site_Conditions.appendChild(itemTag_ITEM);
	//
	// // Material Name:
	// Node item_Material_Name = (Node) doc
	// .createElement(XML_Values.MATERIAL_NAME);
	// Text data_Material_Name = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_NAME));
	// item_Material_Name.appendChild(data_Material_Name);
	//
	// itemTag_ITEM.appendChild(item_Material_Name);
	//
	// // Material Category:
	// Node item_Material_Category = (Node) doc
	// .createElement(XML_Values.MATERIAL_CATEGORY);
	// Text data_Material_Category = doc
	// .createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_CATEGORY));
	// item_Material_Category.appendChild(data_Material_Category);
	//
	// itemTag_ITEM.appendChild(item_Material_Category);
	//
	// // Information
	// Node item_Information = (Node) doc
	// .createElement(XML_Values.INFORMATION);
	// Text data_Information = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.INFORMATION));
	// item_Information.appendChild(data_Information);
	//
	// itemTag_ITEM.appendChild(item_Information);
	//
	// // Date
	// Node item_Date = (Node) doc
	// .createElement(XML_Values.MATERIAL_DATE);
	// Text data_date = doc.createTextNode(hm_siteConditions
	// .get(XML_Values.MATERIAL_DATE));
	// item_Date.appendChild(data_date);
	//
	// itemTag_ITEM.appendChild(item_Date);
	// }
	//
	// Transformer transformer = TransformerFactory.newInstance()
	// .newTransformer();
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	//
	// // initialize StreamResult with File object to save to file
	// StreamResult result = new StreamResult(file_report);
	// DOMSource source = new DOMSource(doc);
	// transformer.transform(source, result);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // System.out.println(e.getMessage());
	// }
	// }

	public void save_Site_Conditions_New(
			HashMap<String, String> hm_site_conditions) {

		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Site_condition = XPathFactory.newInstance();
			XPath xpath_Site_condition = xpf_Site_condition.newXPath();
			XPathExpression expression_Site_condition = xpath_Site_condition
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.SITE_CONDITIONS);

			Node node_SC = (Node) expression_Site_condition.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_SC.getParentNode().removeChild(node_SC);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// Site conditions::
			Node site_Conditions = doc
					.createElement(XML_Values.SITE_CONDITIONS);

			// Site Storage Condition::
			Node item_Site_Storage_Condition = (Node) doc
					.createElement(XML_Values.SITE_STORAGE_CONDITIONS);
			site_Conditions.appendChild(item_Site_Storage_Condition);

			// int id_SSC_Added = 0;
			// id_SSC_Added = Integer
			// .parseInt(hm_site_conditions.get("Count_SSC"));
			// Log.v("  XML CREATION:: ", " value of SSC count:: " +
			// id_SSC_Added);

			if (hm_site_conditions.get("Count_SSC") != null) {
				count_SSC = Integer.parseInt(hm_site_conditions
						.get("Count_SSC"));

				if (count_SSC != 0) {
					for (int i = 0; i < count_SSC; i++) {

						Node itemTag_ITEM = (Node) doc
								.createElement(XML_Values.ITEM);
						item_Site_Storage_Condition.appendChild(itemTag_ITEM);

						// Store
						Node item_Store_SSC = (Node) doc
								.createElement(XML_Values.STORE);
						Text data_Store_SSC = doc
								.createTextNode(EncodeXML(hm_site_conditions
										.get(XML_Values.STORE + i)));
						item_Store_SSC.appendChild(data_Store_SSC);

						itemTag_ITEM.appendChild(item_Store_SSC);
						// Log.v("================id of store=========== ",
						// hm_site_conditions.get(XML_Values.STORE + i));

						// Checked
						Node item_checked_SSC = (Node) doc
								.createElement(XML_Values.CHECKED);
						Text data_checked_SSC = doc
								.createTextNode(EncodeXML(hm_site_conditions
										.get(XML_Values.CHECKED + i)));
						item_checked_SSC.appendChild(data_checked_SSC);
						// Log.v("================id of checked=========== ",
						// hm_site_conditions.get(XML_Values.CHECKED + i));
						itemTag_ITEM.appendChild(item_checked_SSC);

						// Date
						Node item_Date_SSC = (Node) doc
								.createElement(XML_Values.DATE_SITE_STORAGE_CONDITIONS);
						Text data_date_SSC = doc
								.createTextNode(EncodeXML(hm_site_conditions
										.get(XML_Values.DATE_SITE_STORAGE_CONDITIONS
												+ i)));
						item_Date_SSC.appendChild(data_date_SSC);
						// Log.v("================id of date=========== ",
						// hm_site_conditions
						// .get(XML_Values.DATE_SITE_STORAGE_CONDITIONS
						// + i));

						itemTag_ITEM.appendChild(item_Date_SSC);

						// Remarks
						Node item_remarks_SSC = (Node) doc
								.createElement(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS);
						Text data_remarks_SSC = doc
								.createTextNode(EncodeXML(hm_site_conditions
										.get(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS
												+ i)));
						item_remarks_SSC.appendChild(data_remarks_SSC);
						// Log.v("================id of remarks=========== ",
						// hm_site_conditions
						// .get(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS
						// + i));

						itemTag_ITEM.appendChild(item_remarks_SSC);

					}
				}
			}

			// Spare Parts At site:::
			Node item_Spare_Parts_At_Site = (Node) doc
					.createElement(XML_Values.SPARE_PARTS_AT_SITE);
			site_Conditions.appendChild(item_Spare_Parts_At_Site);

			// int id_SPAS_Added = 0;
			// id_SPAS_Added = Integer.parseInt(hm_site_conditions
			// .get("Count_SPAS"));
			// Log.v("  XML CREATION:: ", " value of SPAS count:: "
			// + id_SPAS_Added);

			if (hm_site_conditions.get("Count_SPAS") != null) {
				count_SPAS = Integer.parseInt(hm_site_conditions
						.get("Count_SPAS"));

				if (count_SPAS != 0) {
					for (int i = 0; i < count_SPAS; i++) {

						if (!hm_site_conditions.get(
								XML_Values.SPARE_PART_MATERIAL + i).equals("")
								|| !hm_site_conditions.get(
										XML_Values.QUANTITY_SPARE_PART_AT_SITE
												+ i).equals("")
								|| !hm_site_conditions.get(
										XML_Values.DATE_SPARE_PART_AT_SITE + i)
										.equals("")
								|| !hm_site_conditions.get(
										XML_Values.REMARKS_SPARE_PART_AT_SITE
												+ i).equals("")) {

							Node itemTag_ITEM = (Node) doc
									.createElement(XML_Values.ITEM);
							item_Spare_Parts_At_Site.appendChild(itemTag_ITEM);

							// Material
							Node item_Material_SPAS = (Node) doc
									.createElement(XML_Values.SPARE_PART_MATERIAL);
							Text data_Material_SPAS = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.SPARE_PART_MATERIAL
													+ i)));
							item_Material_SPAS.appendChild(data_Material_SPAS);

							itemTag_ITEM.appendChild(item_Material_SPAS);
							// Log.v("================id of material=========== ",
							// hm_site_conditions.get(XML_Values.SPARE_PART_MATERIAL
							// + i));

							// Quantity
							Node item_Quantity_SPAS = (Node) doc
									.createElement(XML_Values.QUANTITY_SPARE_PART_AT_SITE);
							Text data_Quantity_SPAS = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.QUANTITY_SPARE_PART_AT_SITE
													+ i)));
							item_Quantity_SPAS.appendChild(data_Quantity_SPAS);
							// Log.v("================id of quantity=========== ",
							// hm_site_conditions
							// .get(XML_Values.QUANTITY_SPARE_PART_AT_SITE +
							// i));
							itemTag_ITEM.appendChild(item_Quantity_SPAS);

							// Date
							Node item_Date_SPAS = (Node) doc
									.createElement(XML_Values.DATE_SPARE_PART_AT_SITE);
							Text data_date_SPAS = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.DATE_SPARE_PART_AT_SITE
													+ i)));
							item_Date_SPAS.appendChild(data_date_SPAS);
							// Log.v("================id of date SPAS=========== ",
							// hm_site_conditions
							// .get(XML_Values.DATE_SPARE_PART_AT_SITE + i));

							itemTag_ITEM.appendChild(item_Date_SPAS);

							// Remarks
							Node item_remarks_SPAS = (Node) doc
									.createElement(XML_Values.REMARKS_SPARE_PART_AT_SITE);
							Text data_remarks_SPAS = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.REMARKS_SPARE_PART_AT_SITE
													+ i)));
							item_remarks_SPAS.appendChild(data_remarks_SPAS);
							// Log.v("================id of remarks spas=========== ",
							// hm_site_conditions
							// .get(XML_Values.REMARKS_SPARE_PART_AT_SITE + i));

							itemTag_ITEM.appendChild(item_remarks_SPAS);
						}
					}
				}
			}

			// Tools And instruments:::
			Node item_Tools_N_Instruments = (Node) doc
					.createElement(XML_Values.TOOLS_INSTRUMENTS);
			site_Conditions.appendChild(item_Tools_N_Instruments);

			int id_TNI_Added = 0;
			// id_TNI_Added = Integer
			// .parseInt(hm_site_conditions.get("Count_TNI"));
			// Log.v("  XML CREATION:: ", " value of TNI count:: " +
			// id_TNI_Added);

			if (hm_site_conditions.get("Count_TNI") != null) {
				count_TNI = Integer.parseInt(hm_site_conditions
						.get("Count_TNI"));

				if (count_TNI != 0) {

					for (int i = 0; i < count_TNI; i++) {
						if (!hm_site_conditions.get(XML_Values.TOOLS + i)
								.equals("")
								|| !hm_site_conditions.get(
										XML_Values.QUANTITY_TOOLS_INSTRUMENTS
												+ i).equals("")
								|| !hm_site_conditions.get(
										XML_Values.DATE_TOOLS_INSTRUMENT + i)
										.equals("")
								|| !hm_site_conditions
										.get(XML_Values.REMARKS_TOOLS_INSTRUMENT
												+ i).equals("")) {

							Node itemTag_ITEM = (Node) doc
									.createElement(XML_Values.ITEM);
							item_Tools_N_Instruments.appendChild(itemTag_ITEM);

							// Material
							Node item_Tools_TNI = (Node) doc
									.createElement(XML_Values.TOOLS);
							Text data_Tools_TNI = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.TOOLS + i)));
							item_Tools_TNI.appendChild(data_Tools_TNI);

							itemTag_ITEM.appendChild(item_Tools_TNI);
							// Log.v("================id of tools TNI=========== ",
							// hm_site_conditions.get(XML_Values.TOOLS + i));

							// Quantity
							Node item_Quantity_TNI = (Node) doc
									.createElement(XML_Values.QUANTITY_TOOLS_INSTRUMENTS);
							Text data_Quantity_TNI = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.QUANTITY_TOOLS_INSTRUMENTS
													+ i)));
							item_Quantity_TNI.appendChild(data_Quantity_TNI);
							// Log.v("================id of quantity TNI=========== ",
							// hm_site_conditions
							// .get(XML_Values.QUANTITY_TOOLS_INSTRUMENTS + i));
							itemTag_ITEM.appendChild(item_Quantity_TNI);

							// Date
							Node item_Date_TNI = (Node) doc
									.createElement(XML_Values.DATE_TOOLS_INSTRUMENT);
							Text data_date_TNI = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.DATE_TOOLS_INSTRUMENT
													+ i)));
							item_Date_TNI.appendChild(data_date_TNI);
							// Log.v("================id of date TNI=========== ",
							// hm_site_conditions.get(XML_Values.DATE_TOOLS_INSTRUMENT
							// + i));

							itemTag_ITEM.appendChild(item_Date_TNI);

							// Remarks
							Node item_remarks_TNI = (Node) doc
									.createElement(XML_Values.REMARKS_TOOLS_INSTRUMENT);
							Text data_remarks_TNI = doc
									.createTextNode(EncodeXML(hm_site_conditions
											.get(XML_Values.REMARKS_TOOLS_INSTRUMENT
													+ i)));
							item_remarks_TNI.appendChild(data_remarks_TNI);
							// Log.v("================id of remarks TNI=========== ",
							// hm_site_conditions
							// .get(XML_Values.REMARKS_TOOLS_INSTRUMENT + i));

							itemTag_ITEM.appendChild(item_remarks_TNI);
						}
					}
				}
			}

			item_reports.appendChild(site_Conditions);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	public void save_Report_A_Problem() {// HashMap<String, String>
		// hm_report_problem// To
		// save
		// xml
		get_File(); // for
		// report
		// a
		// problem
		try {

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			Node report_a_problem = doc
					.createElement(XML_Values.REPORT_A_PROBLEM);

			item_reports.appendChild(report_a_problem);

			// Mill_ID
			Node mill_id = doc.createElement(XML_Values.MILL_ID);
			Text data_mill_id = doc
					.createTextNode(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.MILL_ID));
			mill_id.appendChild(data_mill_id);
			report_a_problem.appendChild(mill_id);

			// Parent_ID
			Node parent_id = doc.createElement(XML_Values.PARENT_ID);
			Text data_parent_id = doc
					.createTextNode(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.PARENT_ID));
			parent_id.appendChild(data_parent_id);
			report_a_problem.appendChild(parent_id);

			// ID
			Node id = doc.createElement(XML_Values.ID);
			Text data_id = doc.createTextNode(Report_A_Problem.hm_Problem_Data
					.get(XML_Values.ID));
			id.appendChild(data_id);
			report_a_problem.appendChild(id);

			// COORDINATES
			Node coordinates = doc.createElement(XML_Values.COORDINATES);
			Text data_coordinates = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.COORDINATES)));
			coordinates.appendChild(data_coordinates);
			report_a_problem.appendChild(coordinates);

			// PICTURE
			Node picture = doc.createElement(XML_Values.PICTURE);
			Text data_picture = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.PICTURE)));
			picture.appendChild(data_picture);
			report_a_problem.appendChild(picture);

			// IMAGE_NAME
			Node image_name = doc.createElement(XML_Values.IMAGE_NAME);
			Text data_image_name = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.IMAGE_NAME)));
			image_name.appendChild(data_image_name);
			report_a_problem.appendChild(image_name);

			// THIRDGENITEMID
			Node parent_pic_id = doc.createElement(XML_Values.THIRDGENITEMID);
			Text data_parent_pic_id;
			try {
				data_parent_pic_id = doc
						.createTextNode(Report_A_Problem.hm_Problem_Data
								.get(XML_Values.THIRDGENITEMID));
			} catch (Exception e) {
				data_parent_pic_id = doc.createTextNode("0");
			}
			parent_pic_id.appendChild(data_parent_pic_id);
			report_a_problem.appendChild(parent_pic_id);

			// PHOTO_DATE
			Node photo_date = doc.createElement(XML_Values.PHOTO_DATE);
			Text data_photo_date = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.PHOTO_DATE)));
			photo_date.appendChild(data_photo_date);
			report_a_problem.appendChild(photo_date);

			// What is the Problem
			Node what_is_the_problem = doc
					.createElement(XML_Values.WHAT_IS_THE_PROBLEM);
			Text data_what_is_the_problem = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.WHAT_IS_THE_PROBLEM)));
			what_is_the_problem.appendChild(data_what_is_the_problem);
			report_a_problem.appendChild(what_is_the_problem);

			// Where is the Problem
			Node where_is_the_problem = doc
					.createElement(XML_Values.WHERE_IS_THE_PROBLEM);
			Text data_where_is_the_problem = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.WHERE_IS_THE_PROBLEM)));
			where_is_the_problem.appendChild(data_where_is_the_problem);
			report_a_problem.appendChild(where_is_the_problem);

			// Why is it a Problem
			Node why_is_it_a_problem = doc
					.createElement(XML_Values.WHY_IS_IT_A_PROBLEM);
			Text data_why_is_it_a_problem = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.WHY_IS_IT_A_PROBLEM)));
			why_is_it_a_problem.appendChild(data_why_is_it_a_problem);
			report_a_problem.appendChild(why_is_it_a_problem);

			// Photo location
			Node photo_location = doc.createElement(XML_Values.PHOTO_LOCATION);
			Text data_photo_location = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.PHOTO_LOCATION)));
			photo_location.appendChild(data_photo_location);
			report_a_problem.appendChild(photo_location);

			// Position
			Node position = doc.createElement(XML_Values.POSITION);
			Text data_position = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.POSITION)));
			position.appendChild(data_position);
			report_a_problem.appendChild(position);

			// Motor Parts
			Node motor_parts = doc.createElement(XML_Values.MOTOR_PART);
			Text data_motor_parts = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.MOTOR_PART)));
			motor_parts.appendChild(data_motor_parts);
			report_a_problem.appendChild(motor_parts);

			// Comments
			Node comments = doc.createElement(XML_Values.COMMENTS);
			Text data_comments = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.COMMENTS)));
			comments.appendChild(data_comments);
			report_a_problem.appendChild(comments);

			// How can this be fixed
			Node how_can_this_be_Fixed = doc
					.createElement(XML_Values.HOW_CAN_THIS_BE_FIXED);
			Text data_how_can_this_be_Fixed = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.HOW_CAN_THIS_BE_FIXED)));
			how_can_this_be_Fixed.appendChild(data_how_can_this_be_Fixed);
			report_a_problem.appendChild(how_can_this_be_Fixed);

			// Why fix it
			Node why_fix_it = doc.createElement(XML_Values.WHY_FIX_IT);
			Text data_why_fix_it = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.WHY_FIX_IT)));
			why_fix_it.appendChild(data_why_fix_it);
			report_a_problem.appendChild(why_fix_it);

			// Time frame
			Node time_frame = doc.createElement(XML_Values.TIME_FRAME);
			Text data_time_frame = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.TIME_FRAME)));
			time_frame.appendChild(data_time_frame);
			report_a_problem.appendChild(time_frame);

			// Required Personnal
			Node required_personnal = doc
					.createElement(XML_Values.REQUIRED_PERSONNAL);
			Text data_required_personnal = doc
					.createTextNode(EncodeXML(Report_A_Problem.hm_Problem_Data
							.get(XML_Values.REQUIRED_PERSONNAL)));
			required_personnal.appendChild(data_required_personnal);
			report_a_problem.appendChild(required_personnal);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save_All_Photos(HashMap<String, String> mapPhotos) {
		get_File();

		try {
			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);
			Node photographs = doc.createElement(XML_Values.PHOTOGRAPH);
			item_reports.appendChild(photographs);

			// PICTURE
			Node picture = doc.createElement(XML_Values.PICTURE);
			Text data_picture = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.PICTURE)));
			picture.appendChild(data_picture);
			photographs.appendChild(picture);

			// MILL_ID
			Node mill_id = doc.createElement(XML_Values.MILL_ID);
			Text data_mill_id = doc.createTextNode(mapPhotos
					.get(XML_Values.MILL_ID));
			mill_id.appendChild(data_mill_id);
			photographs.appendChild(mill_id);

			// IMAGE_NAME
			Node image_name = doc.createElement(XML_Values.IMAGE_NAME);
			Text data_image_name = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.IMAGE_NAME)));
			image_name.appendChild(data_image_name);
			photographs.appendChild(image_name);

			// THIRDGENITEMID
			Node parent_id = doc.createElement(XML_Values.THIRDGENITEMID);
			Text data_parent_id;
			try {
				data_parent_id = doc.createTextNode(mapPhotos
						.get(XML_Values.THIRDGENITEMID));
			} catch (Exception e) {
				data_parent_id = doc.createTextNode("0");
			}
			parent_id.appendChild(data_parent_id);
			photographs.appendChild(parent_id);

			// PHOTO_DATE
			Node photo_date = doc.createElement(XML_Values.PHOTO_DATE);
			Text data_photo_date = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.PHOTO_DATE)));
			photo_date.appendChild(data_photo_date);
			photographs.appendChild(photo_date);

			// Photo location
			Node photo_location = doc.createElement(XML_Values.PHOTO_LOCATION);
			Text data_photo_location = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.PHOTO_LOCATION)));
			photo_location.appendChild(data_photo_location);
			photographs.appendChild(photo_location);

			// Position
			Node position = doc.createElement(XML_Values.POSITION);
			Text data_position = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.POSITION)));
			position.appendChild(data_position);
			photographs.appendChild(position);

			// Motor Parts
			Node motor_parts = doc.createElement(XML_Values.MOTOR_PART);
			Text data_motor_parts = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.MOTOR_PART)));
			motor_parts.appendChild(data_motor_parts);
			photographs.appendChild(motor_parts);

			// Comments
			Node comments = doc.createElement(XML_Values.COMMENTS);
			Text data_comments = doc.createTextNode(EncodeXML(mapPhotos
					.get(XML_Values.COMMENTS)));
			comments.appendChild(data_comments);
			photographs.appendChild(comments);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void delete_Service_Checklist(
	// HashMap<String, String> hm_service_checklist) {
	// try {
	// get_File();
	//
	// // For deleting service checklist::
	// XPathFactory xpf_SC = XPathFactory.newInstance();
	// XPath xpath_SC = xpf_SC.newXPath();
	// XPathExpression expression_SC = xpath_SC
	// .compile("//callback/body/reports/"
	// + XML_Values.SERVICE_CHECKLIST);
	//
	// Node node_SC = (Node) expression_SC.evaluate(doc,
	// XPathConstants.NODE);
	// node_SC.getParentNode().removeChild(node_SC);
	//
	// Transformer transformer = TransformerFactory.newInstance()
	// .newTransformer();
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	//
	// // initialize StreamResult with File object to save to file
	// StreamResult result = new StreamResult(file_report);
	// DOMSource source = new DOMSource(doc);
	// transformer.transform(source, result);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // System.out.println(e.getMessage());
	// }
	// }

	// public void append_Service_Checklist() {
	// // For adding service checklist::
	// try {
	// get_File();
	// get_File_for_Service_Checklist();
	// XPathFactory xpf_SC_ADD = XPathFactory.newInstance();
	// XPath xpath_SC_Add = xpf_SC_ADD.newXPath();
	// XPathExpression expression_SC_ADD = xpath_SC_Add
	// .compile("//callback/" + XML_Values.SERVICE_CHECKLIST);
	//
	// Node node_SC_ADD = (Node) expression_SC_ADD.evaluate(doc,
	// XPathConstants.NODE);
	//
	// Node service_Checklist_new = doc_sc.getElementsByTagName(
	// XML_Values.SERVICE_CHECKLIST).item(0);
	//
	// // Log.v("************______before___________*****************",
	// // node_SC_ADD.toString());
	//
	// Node service_Checklist = doc.getElementsByTagName(
	// XML_Values.SERVICE_CHECKLIST).item(0);
	//
	// // Log.v("^^^^^^^^^^^OLD NODE VALUE^^^^^^^^^^^^^ ", ""
	// // + service_Checklist.getNodeName());
	// // Log.v("^^^^^^^^^^^NEW NODE VALUE^^^^^^^^^^^^^ ",
	// // service_Checklist_new.getNodeName());
	//
	// doc.replaceChild(service_Checklist_new, service_Checklist);
	//
	// // report.appendChild(node_SC_ADD);
	//
	// Transformer transformer = TransformerFactory.newInstance()
	// .newTransformer();
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	//
	// // initialize StreamResult with File object to save to file
	// StreamResult result = new StreamResult(file_report);
	// DOMSource source = new DOMSource(doc);
	// transformer.transform(source, result);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // System.out.println(e.getMessage());
	// }
	// }

	// public void add_Service_Checklist() {
	// // For adding service checklist::
	// try {
	// get_File();
	// get_File_for_Service_Checklist();
	// XPathFactory xpf_SC_ADD = XPathFactory.newInstance();
	// XPath xpath_SC_Add = xpf_SC_ADD.newXPath();
	// XPathExpression expression_SC_ADD = xpath_SC_Add
	// .compile("//callback/" + XML_Values.SERVICE_CHECKLIST);
	//
	// Node node_SC_ADD = (Node) expression_SC_ADD.evaluate(doc,
	// XPathConstants.NODE);
	//
	// Node report = doc.getElementsByTagName(XML_Values.REPORT).item(0);
	// Node service_Checklist = doc
	// .createElement(XML_Values.SERVICE_CHECKLIST);
	//
	// Node service_Checklist_new = doc_sc.getElementsByTagName(
	// XML_Values.SERVICE_CHECKLIST).item(0);
	//
	// // Log.v("^^^^^^^^^^^PARENT^^^^^^^^^^^^ ", "" +
	// // report.getNodeName());
	//
	// // Log.v("^^^^^^^^^^^append ^^^^^^^^^^^^ ",
	// // "" + service_Checklist_new.getNodeName());
	//
	// // report.appendChild(service_Checklist_new);
	//
	// // //filename is filepath string
	// // BufferedReader br1 = new BufferedReader(new FileReader(file));
	// // String line1;
	// // StringBuilder sb1 = new StringBuilder();
	// //
	// // while((line1=br1.readLine())!= null){
	// // sb1.append(line1.trim());
	// // }
	// //
	// // line1 = sb1.toString();
	// //
	// // //filename is filepath string
	// // BufferedReader br2 = new BufferedReader(new
	// // FileReader(file_checklist));
	// // String line2;
	// // StringBuilder sb2 = new StringBuilder();
	// //
	// // while((line2=br2.readLine())!= null){
	// // sb2.append(line2.trim());
	// // }
	// //
	// // line2 = sb2.toString();
	// //
	// // String newstr = line1.concat(line2);
	// //
	// // Log.v("^^^^^^^^^^^newstr ^^^^^^^^^^^^ ",newstr);
	// NodeList nodeList = doc_sc.getDocumentElement()
	// .getElementsByTagName(XML_Values.SERVICE_CHECKLIST);
	//
	// //
	// Log.v("^^^^^^^^^^^nodeList ^^^^^^^^^^^^ ",nodeList.item(0).cloneNode(true).toString());
	//
	// doc.adoptNode(nodeList.item(0));
	//
	// Transformer transformer = TransformerFactory.newInstance()
	// .newTransformer();
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	//
	// // initialize StreamResult with File object to save to file
	// StreamResult result = new StreamResult(file_report);
	// DOMSource source = new DOMSource(doc);
	// transformer.transform(source, result);
	//
	// //
	// // StreamResult result1 = new StreamResult(file_checklist);
	// // DOMSource source1 = new DOMSource(doc_sc);
	// // transformer.transform(source1, result1);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // System.out.println(e.getMessage());
	// }
	// }

	// Special item:: KEY BAR (10)
	public void save_Key_Bars(HashMap<String, String> hm_keyBar, String millTag) {
		try {
			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_keyBar = XPathFactory.newInstance();
			XPath xpath_Keybar = xpf_keyBar.newXPath();
			XPathExpression expression_millTag = xpath_Keybar
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.KEYBAR + "_" + millTag);

			Node node_MillTag = (Node) expression_millTag.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_MillTag.getParentNode().removeChild(node_MillTag);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Keybar::
			Node keybar = doc.createElement(XML_Values.KEYBAR + "_" + millTag);

			int no_keybar = 0;
			no_keybar = Integer
					.parseInt(hm_keyBar.get(XML_Values.KEYBAR_COUNT));
			try {
				for (int i = 0; i < no_keybar; i++) {
					// Item ::
					Node item_Keybar = (Node) doc
							.createElement(XML_Values.KEYBAR_ITEM);
					keybar.appendChild(item_Keybar);

					// key
					Node key = (Node) doc.createElement(XML_Values.KEY);
					Text data_key = doc.createTextNode(String.valueOf(i + 1));
					key.appendChild(data_key);
					item_Keybar.appendChild(key);

					// Value
					Node value = (Node) doc
							.createElement(XML_Values.KEYBAR_VALUE);
					Text data_value = doc.createTextNode(EncodeXML(hm_keyBar
							.get(XML_Values.KEYBAR_ITEM + i)));
					value.appendChild(data_value);
					item_Keybar.appendChild(value);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			item_reports.appendChild(keybar);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	// Special item:: AIR GAP (13)
	public void save_AirGap(HashMap<String, String> hm_airGap, String millTag) {

		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_airgap = XPathFactory.newInstance();
			XPath xpath_airgap = xpf_airgap.newXPath();
			XPathExpression expression_airgap = xpath_airgap
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.AIR_GAP + "_" + millTag);
			Node node_Airgap = (Node) expression_airgap.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_Airgap.getParentNode().removeChild(node_Airgap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// AirGap::
			Node AirGap = doc.createElement(XML_Values.AIR_GAP + "_" + millTag);

			int no_airgap_phy = 0;
			no_airgap_phy = Integer.parseInt(hm_airGap
					.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_COUNT));

			// physical measurement ::
			Node Physical_measurement = (Node) doc
					.createElement(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT);
			AirGap.appendChild(Physical_measurement);

			for (int i = 0; i < no_airgap_phy; i++) {
				// Item ::
				Node Physical_Item = (Node) doc
						.createElement(XML_Values.AIR_GAP_PHYSICAL_ITEM);
				Physical_measurement.appendChild(Physical_Item);

				// physical measurement:Position
				Node pm_Position = (Node) doc
						.createElement(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION);
				Text data_pm_Position = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION
								+ i)));
				pm_Position.appendChild(data_pm_Position);
				Physical_Item.appendChild(pm_Position);

				// physical measurement:feeder
				Node pm_Feeder = (Node) doc
						.createElement(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE);
				Text data_pm_Feeder = doc
						.createTextNode(EncodeXML(hm_airGap
								.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE
										+ i)));
				pm_Feeder.appendChild(data_pm_Feeder);
				Physical_Item.appendChild(pm_Feeder);

				// physical measurement:discharge
				Node pm_Discharge = (Node) doc
						.createElement(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE);
				Text data_pm_Discharge = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE
								+ i)));
				pm_Discharge.appendChild(data_pm_Discharge);
				Physical_Item.appendChild(pm_Discharge);

				// physical measurement:feeder
				Node pm_Remarks = (Node) doc
						.createElement(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS);
				Text data_pm_Remarks = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS
								+ i)));
				pm_Remarks.appendChild(data_pm_Remarks);
				Physical_Item.appendChild(pm_Remarks);
			}

			int no_airgap_sensor = 0;
			no_airgap_sensor = Integer.parseInt(hm_airGap
					.get(XML_Values.AIR_GAP_SENSORS_MEASUREMENT_COUNT));

			// sensor measurement ::
			Node Sensor_measurement = (Node) doc
					.createElement(XML_Values.AIR_GAP_SENSORS_MEASUREMENT);
			AirGap.appendChild(Sensor_measurement);

			for (int i = 0; i < no_airgap_sensor; i++) {
				// Item ::
				Node Sensor_Item = (Node) doc
						.createElement(XML_Values.AIR_GAP_SENSORS_ITEM);
				Sensor_measurement.appendChild(Sensor_Item);

				// sensor measurement:Position
				Node sm_Position = (Node) doc
						.createElement(XML_Values.AIR_GAP_POSITION);
				Text data_sm_Position = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_POSITION + i)));
				sm_Position.appendChild(data_sm_Position);
				Sensor_Item.appendChild(sm_Position);

				// sensor measurement:feeder
				Node sm_Feeder = (Node) doc
						.createElement(XML_Values.AIR_GAP_FEEDER_SIDE);
				Text data_sm_Feeder = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_FEEDER_SIDE + i)));
				sm_Feeder.appendChild(data_sm_Feeder);
				Sensor_Item.appendChild(sm_Feeder);

				// sensor measurement:discharge
				Node sm_Discharge = (Node) doc
						.createElement(XML_Values.AIR_GAP_DISCHARGE_SIDE);
				Text data_sm_Discharge = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_DISCHARGE_SIDE + i)));
				sm_Discharge.appendChild(data_sm_Discharge);
				Sensor_Item.appendChild(sm_Discharge);

				// sensor measurement:feeder
				Node sm_Remarks = (Node) doc
						.createElement(XML_Values.AIR_GAP_REMARKS);
				Text data_sm_Remarks = doc.createTextNode(EncodeXML(hm_airGap
						.get(XML_Values.AIR_GAP_REMARKS + i)));
				sm_Remarks.appendChild(data_sm_Remarks);
				Sensor_Item.appendChild(sm_Remarks);
			}
			// Winding Temp
			Node Winding_Temp = (Node) doc
					.createElement(XML_Values.AIRGAP_WINDING_T);
			Text data_Winding_Temp = doc.createTextNode(EncodeXML(hm_airGap
					.get(XML_Values.AIRGAP_WINDING_T)));
			Winding_Temp.appendChild(data_Winding_Temp);
			AirGap.appendChild(Winding_Temp);

			// Stator Temp
			Node Stator_Temp = (Node) doc
					.createElement(XML_Values.AIRGAP_STATOR_T);
			Text data_Stator_Temp = doc.createTextNode(EncodeXML(hm_airGap
					.get(XML_Values.AIRGAP_STATOR_T)));
			Stator_Temp.appendChild(data_Stator_Temp);
			AirGap.appendChild(Stator_Temp);

			// Ambiant Temp
			Node Ambiant_Temp = (Node) doc
					.createElement(XML_Values.AIRGAP_AMBIENT_T);
			Text data_Ambiant_Temp = doc.createTextNode(EncodeXML(hm_airGap
					.get(XML_Values.AIRGAP_AMBIENT_T)));
			Ambiant_Temp.appendChild(data_Ambiant_Temp);
			AirGap.appendChild(Ambiant_Temp);

			// Remarks
			Node Remarks = (Node) doc.createElement(XML_Values.AIRGAP_REMARKS);
			Text data_Remarks = doc.createTextNode(EncodeXML(hm_airGap
					.get(XML_Values.AIRGAP_REMARKS)));
			Remarks.appendChild(data_Remarks);
			AirGap.appendChild(Remarks);

			item_reports.appendChild(AirGap);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	// Special item:: SOLE PLATE (09)
	public void save_SolePlate(HashMap<String, String> hm_solePlate,
			String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_SolePlate = XPathFactory.newInstance();
			XPath xpath_SolePlate = xpf_SolePlate.newXPath();
			XPathExpression expression_SolePlate = xpath_SolePlate
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.SOLE_PLATE + "_" + millTag);

			Node node_SolePlate = (Node) expression_SolePlate.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_SolePlate.getParentNode().removeChild(node_SolePlate);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// SolePlate::
			Node SolePlate = doc.createElement(XML_Values.SOLE_PLATE + "_"
					+ millTag);

			int no_solePlate = 0;
			no_solePlate = Integer.parseInt(hm_solePlate
					.get(XML_Values.SOLE_PLATE_COUNT));

			for (int i = 0; i < no_solePlate; i++) {

				// Item ::
				Node item_SolePlate = (Node) doc.createElement(XML_Values.ITEM);
				SolePlate.appendChild(item_SolePlate);

				// Power
				Node Power = (Node) doc
						.createElement(XML_Values.SOLE_PLATE_POWER);
				Text data_Power = doc.createTextNode(EncodeXML(hm_solePlate
						.get(XML_Values.SOLE_PLATE_POWER + i)));
				Power.appendChild(data_Power);
				item_SolePlate.appendChild(Power);

				// kW
				Node kW = (Node) doc.createElement(XML_Values.SOLE_PLATE_KW);
				Text data_kW = doc.createTextNode(EncodeXML(hm_solePlate
						.get(XML_Values.SOLE_PLATE_KW + i)));
				kW.appendChild(data_kW);
				item_SolePlate.appendChild(kW);

				// Measurement1
				Node Measurement1 = (Node) doc
						.createElement(XML_Values.SOLE_PLATE_MEASUREMENT1);
				Text data_Measurement1 = doc
						.createTextNode(EncodeXML(hm_solePlate
								.get(XML_Values.SOLE_PLATE_MEASUREMENT1 + i)));
				Measurement1.appendChild(data_Measurement1);
				item_SolePlate.appendChild(Measurement1);

				// Measurement2
				Node Measurement2 = (Node) doc
						.createElement(XML_Values.SOLE_PLATE_MEASUREMENT2);
				Text data_Measurement2 = doc
						.createTextNode(EncodeXML(hm_solePlate
								.get(XML_Values.SOLE_PLATE_MEASUREMENT2 + i)));
				Measurement2.appendChild(data_Measurement2);
				item_SolePlate.appendChild(Measurement2);

				// Measurement2
				Node Measurement3 = (Node) doc
						.createElement(XML_Values.SOLE_PLATE_MEASUREMENT3);
				Text data_Measurement3 = doc
						.createTextNode(EncodeXML(hm_solePlate
								.get(XML_Values.SOLE_PLATE_MEASUREMENT3 + i)));
				Measurement3.appendChild(data_Measurement3);
				item_SolePlate.appendChild(Measurement3);

			}
			item_reports.appendChild(SolePlate);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item::CORE PARTITIONS(06)
	public void save_CorePartitions(HashMap<String, String> hm_corePartitions,
			String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_CorePartitions = XPathFactory.newInstance();
			XPath xpath_CorePartitions = xpf_CorePartitions.newXPath();
			XPathExpression expression_CorePartitions = xpath_CorePartitions
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.CORE_PARTITIONS + "_" + millTag);

			Node node_CorePartitions = (Node) expression_CorePartitions
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_CorePartitions.getParentNode().removeChild(
						node_CorePartitions);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Core Partitions::
			Node CorePartitions = doc.createElement(XML_Values.CORE_PARTITIONS
					+ "_" + millTag);

			int no_core_Feeder = 0;
			no_core_Feeder = Integer.parseInt(hm_corePartitions
					.get(XML_Values.CORE_PARTITIONS_FEEDER_COUNT));

			Node Feeder_side = (Node) doc
					.createElement(XML_Values.CORE_PARTITIONS_FEEDER_SIDE);
			CorePartitions.appendChild(Feeder_side);

			for (int i = 0; i < no_core_Feeder; i++) {
				// Item ::
				Node Feeder_Item = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_FEEDER_ITEM);
				Feeder_side.appendChild(Feeder_Item);

				// Feeder Side:Position
				Node Feeder_Position = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_FEEDER_POSITION);
				Text data_Feeder_Position = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_FEEDER_POSITION
										+ i)));
				Feeder_Position.appendChild(data_Feeder_Position);
				Feeder_Item.appendChild(Feeder_Position);

				// Feeder side:radial
				Node Feeder_Radial = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT);
				Text data_Feeder_Radial = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT
										+ i)));
				Feeder_Radial.appendChild(data_Feeder_Radial);
				Feeder_Item.appendChild(Feeder_Radial);

				// feeder side:Axial
				Node Feeder_Axial = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT);
				Text data_Feeder_Axial = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT
										+ i)));
				Feeder_Axial.appendChild(data_Feeder_Axial);
				Feeder_Item.appendChild(Feeder_Axial);

				// Feeder side:Remarks
				Node Feeder_Remarks = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS);
				Text data_Feeder_Remarks = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS
										+ i)));
				Feeder_Remarks.appendChild(data_Feeder_Remarks);
				Feeder_Item.appendChild(Feeder_Remarks);
			}

			int no_Core_Discharge = 0;
			no_Core_Discharge = Integer.parseInt(hm_corePartitions
					.get(XML_Values.CORE_PARTITIONS_DISCHARGE_COUNT));

			Node Discharge_side = (Node) doc
					.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_SIDE);
			CorePartitions.appendChild(Discharge_side);

			for (int i = 0; i < no_Core_Discharge; i++) {
				// Item ::
				Node Discharge_Item = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_ITEM);
				Discharge_side.appendChild(Discharge_Item);

				// Discharge Side:Position
				Node Discharge_Position = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION);
				Text data_Discharge_Position = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION
										+ i)));
				Discharge_Position.appendChild(data_Discharge_Position);
				Discharge_Item.appendChild(Discharge_Position);

				// Discharge side:radial
				Node Discharge_Radial = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP);
				Text data_Discharge_Radial = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP
										+ i)));
				Discharge_Radial.appendChild(data_Discharge_Radial);
				Discharge_Item.appendChild(Discharge_Radial);

				// Discharge side:Axial
				Node Discharge_Axial = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP);
				Text data_Discharge_Axial = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP
										+ i)));
				Discharge_Axial.appendChild(data_Discharge_Axial);
				Discharge_Item.appendChild(Discharge_Axial);

				// Discharge side:Displacement
				Node Discharge_Displacement = (Node) doc
						.createElement(XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT);
				Text data_Discharge_Displacement = doc
						.createTextNode(EncodeXML(hm_corePartitions
								.get(XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT
										+ i)));
				Discharge_Displacement.appendChild(data_Discharge_Displacement);
				Discharge_Item.appendChild(Discharge_Displacement);
			}

			item_reports.appendChild(CorePartitions);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	// Special item:PRESSFINGER FEEDER(07)
	public void save_Pressfinger_Feeder(
			HashMap<String, String> hm_Pressfinger_Feeder, String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Pressfinger_Feeder = XPathFactory.newInstance();
			XPath xpath_Pressfinger_Feeder = xpf_Pressfinger_Feeder.newXPath();
			XPathExpression expression_Pressfinger_Feeder = xpath_Pressfinger_Feeder
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.PRESSFINGER_FEEDER + "_" + millTag);

			Node node_Pressfinger_Feeder = (Node) expression_Pressfinger_Feeder
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_Pressfinger_Feeder.getParentNode().removeChild(
						node_Pressfinger_Feeder);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Pressfinger Feeder::
			Node Pressfinger_Feeder = doc
					.createElement(XML_Values.PRESSFINGER_FEEDER + "_"
							+ millTag);

			int no_Pressfinger_Feeder = 0;
			no_Pressfinger_Feeder = Integer.parseInt(hm_Pressfinger_Feeder
					.get(XML_Values.PRESSFINGER_FEEDER_COUNT));

			for (int i = 0; i < no_Pressfinger_Feeder; i++) {
				// Item ::
				Node Pressfinger_Feeder_Item = (Node) doc
						.createElement(XML_Values.PRESSFINGER_FEEDER_ITEM);
				Pressfinger_Feeder.appendChild(Pressfinger_Feeder_Item);

				// Finger Plate
				Node Finger_Plate = (Node) doc
						.createElement(XML_Values.PRESSFINGER_FEEDER_FINGER_PLATE);
				Text data_Finger_Plate = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Feeder
								.get(XML_Values.PRESSFINGER_FEEDER_FINGER_PLATE
										+ i)));
				Finger_Plate.appendChild(data_Finger_Plate);
				Pressfinger_Feeder_Item.appendChild(Finger_Plate);

				// In or Outwards
				Node In_Or_Outwards = (Node) doc
						.createElement(XML_Values.PRESSFINGER_FEEDER_IN_OR_OUTWARDS);
				Text data_In_Or_Outwards = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Feeder
								.get(XML_Values.PRESSFINGER_FEEDER_IN_OR_OUTWARDS
										+ i)));
				In_Or_Outwards.appendChild(data_In_Or_Outwards);
				Pressfinger_Feeder_Item.appendChild(In_Or_Outwards);

				// Displacement
				Node Displacement = (Node) doc
						.createElement(XML_Values.PRESSFINGER_FEEDER_DISPLACEMENT);
				Text data_Displacement = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Feeder
								.get(XML_Values.PRESSFINGER_FEEDER_DISPLACEMENT
										+ i)));
				Displacement.appendChild(data_Displacement);
				Pressfinger_Feeder_Item.appendChild(Displacement);

			}
			item_reports.appendChild(Pressfinger_Feeder);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item:PRESSFINGER DISCHARGE(08)
	public void save_Pressfinger_Discharge(
			HashMap<String, String> hm_Pressfinger_Discharge, String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Pressfinger_Discharge = XPathFactory.newInstance();
			XPath xpath_Pressfinger_Discharge = xpf_Pressfinger_Discharge
					.newXPath();
			XPathExpression expression_Pressfinger_Discharge = xpath_Pressfinger_Discharge
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.PRESSFINGER_DISCHARGE + "_" + millTag);

			Node node_Pressfinger_Discharge = (Node) expression_Pressfinger_Discharge
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_Pressfinger_Discharge.getParentNode().removeChild(
						node_Pressfinger_Discharge);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Pressfinger Discharge::
			Node Pressfinger_Discharge = doc
					.createElement(XML_Values.PRESSFINGER_DISCHARGE + "_"
							+ millTag);

			int no_Pressfinger_Discharge = 0;
			no_Pressfinger_Discharge = Integer
					.parseInt(hm_Pressfinger_Discharge
							.get(XML_Values.PRESSFINGER_DISCHARGE_COUNT));

			for (int i = 0; i < no_Pressfinger_Discharge; i++) {

				// Item ::
				Node pressfinger_Discharge_Item = (Node) doc
						.createElement(XML_Values.PRESSFINGER_DISCHARGE_ITEM);
				Pressfinger_Discharge.appendChild(pressfinger_Discharge_Item);

				// Finger Plate
				Node Finger_Plate = (Node) doc
						.createElement(XML_Values.PRESSFINGER_DISCHARGE_FINGER_PLATE);
				Text data_Finger_Plate = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Discharge
								.get(XML_Values.PRESSFINGER_DISCHARGE_FINGER_PLATE
										+ i)));
				Finger_Plate.appendChild(data_Finger_Plate);
				pressfinger_Discharge_Item.appendChild(Finger_Plate);

				// In or Outwards
				Node In_Or_Outwards = (Node) doc
						.createElement(XML_Values.PRESSFINGER_DISCHARGE_IN_OR_OUTWARDS);
				Text data_In_Or_Outwards = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Discharge
								.get(XML_Values.PRESSFINGER_DISCHARGE_IN_OR_OUTWARDS
										+ i)));
				In_Or_Outwards.appendChild(data_In_Or_Outwards);
				pressfinger_Discharge_Item.appendChild(In_Or_Outwards);

				// Displacement
				Node Displacement = (Node) doc
						.createElement(XML_Values.PRESSFINGER_DISCHARGE_DISPLACEMENT);
				Text data_Displacement = doc
						.createTextNode(EncodeXML(hm_Pressfinger_Discharge
								.get(XML_Values.PRESSFINGER_DISCHARGE_DISPLACEMENT
										+ i)));
				Displacement.appendChild(data_Displacement);
				pressfinger_Discharge_Item.appendChild(Displacement);

			}
			item_reports.appendChild(Pressfinger_Discharge);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item:HOLDING PLATES(11)
	public void save_Holding_Plates(HashMap<String, String> hm_Holding_Plates,
			String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Holding_Plates = XPathFactory.newInstance();
			XPath xpath_Holding_Plates = xpf_Holding_Plates.newXPath();
			XPathExpression expression_Holding_Plates = xpath_Holding_Plates
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.HOLDING_PLATES + "_" + millTag);

			Node node_Holding_Plates = (Node) expression_Holding_Plates
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_Holding_Plates.getParentNode().removeChild(
						node_Holding_Plates);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Holding Plates::
			Node Holding_Plates = doc.createElement(XML_Values.HOLDING_PLATES
					+ "_" + millTag);

			int no_Holding_Plates = 0;
			no_Holding_Plates = Integer.parseInt(hm_Holding_Plates
					.get(XML_Values.HOLDING_PLATES_COUNT));

			for (int i = 0; i < no_Holding_Plates; i++) {

				// Item ::
				Node Holding_Plates_Item = (Node) doc
						.createElement(XML_Values.HOLDING_PLATES_ITEM);
				Holding_Plates.appendChild(Holding_Plates_Item);

				// keybar
				Node Keybar = (Node) doc
						.createElement(XML_Values.HOLDING_PLATES_KEYBAR);
				Text data_Keybar = doc
						.createTextNode(EncodeXML(hm_Holding_Plates
								.get(XML_Values.HOLDING_PLATES_KEYBAR + i)));
				Keybar.appendChild(data_Keybar);
				Holding_Plates_Item.appendChild(Keybar);

				// Holding plates1
				Node Holding_plates1 = (Node) doc
						.createElement(XML_Values.HOLDING_PLATE1);
				Text data_holding_plates1 = doc
						.createTextNode(EncodeXML(hm_Holding_Plates
								.get(XML_Values.HOLDING_PLATE1 + i)));
				Holding_plates1.appendChild(data_holding_plates1);
				Holding_Plates_Item.appendChild(Holding_plates1);

				// Holding Plates2
				Node Holding_Plate2 = (Node) doc
						.createElement(XML_Values.HOLDING_PLATE2);
				Text data_Holding_plate2 = doc
						.createTextNode(EncodeXML(hm_Holding_Plates
								.get(XML_Values.HOLDING_PLATE2 + i)));
				Holding_Plate2.appendChild(data_Holding_plate2);
				Holding_Plates_Item.appendChild(Holding_Plate2);

				// Holding Plates3
				Node Holding_Plate3 = (Node) doc
						.createElement(XML_Values.HOLDING_PLATE3);
				Text data_Holding_plate3 = doc
						.createTextNode(EncodeXML(hm_Holding_Plates
								.get(XML_Values.HOLDING_PLATE3 + i)));
				Holding_Plate3.appendChild(data_Holding_plate3);
				Holding_Plates_Item.appendChild(Holding_Plate3);

			}
			item_reports.appendChild(Holding_Plates);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item:MAGNETIC CENTERING(16)
	public void save_Magnetic_Centering(
			HashMap<String, String> hm_Magnetic_Centering, String millTag) {
		try {
			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Magnetic_Centering = XPathFactory.newInstance();
			XPath xpath_Magnetic_Centering = xpf_Magnetic_Centering.newXPath();
			XPathExpression expression_Magnetic_Centering = xpath_Magnetic_Centering
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.MAGNETIC_CENTERING + "_" + millTag);

			Node node_Magnetic_Centering = (Node) expression_Magnetic_Centering
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_Magnetic_Centering.getParentNode().removeChild(
						node_Magnetic_Centering);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Magnetic_Centering::
			Node Magnetic_Centering = doc
					.createElement(XML_Values.MAGNETIC_CENTERING + "_"
							+ millTag);

			int no_Magnetic_Centering = 0;
			no_Magnetic_Centering = Integer.parseInt(hm_Magnetic_Centering
					.get(XML_Values.MAGNETIC_CENTERING_COUNT));

			for (int i = 0; i < no_Magnetic_Centering; i++) {

				// Item ::
				Node Magnetic_Centering_Item = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_ITEM);
				Magnetic_Centering.appendChild(Magnetic_Centering_Item);

				// Position
				Node Position = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_POSITION);
				Text data_Position = doc.createTextNode(hm_Magnetic_Centering
						.get(XML_Values.MAGNETIC_CENTERING_POSITION + i));
				Position.appendChild(data_Position);
				Magnetic_Centering_Item.appendChild(Position);

				// Pole no
				Node Pole_No = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_POLE_NO);
				Text data_Pole_No = doc
						.createTextNode(EncodeXML(hm_Magnetic_Centering
								.get(XML_Values.MAGNETIC_CENTERING_POLE_NO + i)));
				Pole_No.appendChild(data_Pole_No);
				Magnetic_Centering_Item.appendChild(Pole_No);

				// Feeder Side
				Node Feeder_Side = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_FEEDER_SIDE);
				Text data_Feeder_Side = doc
						.createTextNode(EncodeXML(hm_Magnetic_Centering
								.get(XML_Values.MAGNETIC_CENTERING_FEEDER_SIDE
										+ i)));
				Feeder_Side.appendChild(data_Feeder_Side);
				Magnetic_Centering_Item.appendChild(Feeder_Side);

				// Discharge Side
				Node Discharge_Side = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_DISCHARGE_SIDE);
				Text data_Discharge_Side = doc
						.createTextNode(EncodeXML(hm_Magnetic_Centering
								.get(XML_Values.MAGNETIC_CENTERING_DISCHARGE_SIDE
										+ i)));
				Discharge_Side.appendChild(data_Discharge_Side);
				Magnetic_Centering_Item.appendChild(Discharge_Side);

				// Deviation
				Node Deviation = (Node) doc
						.createElement(XML_Values.MAGNETIC_CENTERING_DEVIATION);
				Text data_Deviation = doc
						.createTextNode(EncodeXML(hm_Magnetic_Centering
								.get(XML_Values.MAGNETIC_CENTERING_DEVIATION
										+ i)));
				Deviation.appendChild(data_Deviation);
				Magnetic_Centering_Item.appendChild(Deviation);

			}

			// Winding Temp
			Node Winding_Temp = (Node) doc
					.createElement(XML_Values.MAGNETIC_CENTERING_WINDING_T);
			Text data_Winding_Temp = doc
					.createTextNode(EncodeXML(hm_Magnetic_Centering
							.get(XML_Values.MAGNETIC_CENTERING_WINDING_T)));
			Winding_Temp.appendChild(data_Winding_Temp);
			Magnetic_Centering.appendChild(Winding_Temp);

			// Stator Temp
			Node Stator_Temp = (Node) doc
					.createElement(XML_Values.MAGNETIC_CENTERING_STATOR_T);
			Text data_Stator_Temp = doc
					.createTextNode(EncodeXML(hm_Magnetic_Centering
							.get(XML_Values.MAGNETIC_CENTERING_STATOR_T)));
			Stator_Temp.appendChild(data_Stator_Temp);
			Magnetic_Centering.appendChild(Stator_Temp);

			// Ambiant Temp
			Node Ambiant_Temp = (Node) doc
					.createElement(XML_Values.MAGNETIC_CENTERING_AMBIENT_T);
			Text data_Ambiant_Temp = doc
					.createTextNode(EncodeXML(hm_Magnetic_Centering
							.get(XML_Values.MAGNETIC_CENTERING_AMBIENT_T)));
			Ambiant_Temp.appendChild(data_Ambiant_Temp);
			Magnetic_Centering.appendChild(Ambiant_Temp);

			// Remarks
			Node Remarks = (Node) doc
					.createElement(XML_Values.MAGNETIC_CENTERING_REMARKS);
			Text data_Remarks = doc
					.createTextNode(EncodeXML(hm_Magnetic_Centering
							.get(XML_Values.MAGNETIC_CENTERING_REMARKS)));
			Remarks.appendChild(data_Remarks);
			Magnetic_Centering.appendChild(Remarks);

			item_reports.appendChild(Magnetic_Centering);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item:BRUSHWEAR(14)
	public void save_BrushWear(HashMap<String, String> hm_BrushWear,
			String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_Brushwear = XPathFactory.newInstance();
			XPath xpath_Brushwear = xpf_Brushwear.newXPath();
			XPathExpression expression_Brushwear = xpath_Brushwear
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.BRUSH_WEAR + "_" + millTag);

			Node node_Brushwear = (Node) expression_Brushwear.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_Brushwear.getParentNode().removeChild(node_Brushwear);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Brushwear::
			Node Brushwear = doc.createElement(XML_Values.BRUSH_WEAR + "_"
					+ millTag);

			int no_brushwear_Inner = 0;
			no_brushwear_Inner = Integer.parseInt(hm_BrushWear
					.get(XML_Values.BRUSH_WEAR_INNER_RING_COUNT));

			// Inner Ring ::
			Node Inner_ring = (Node) doc
					.createElement(XML_Values.BRUSH_WEAR_INNER_RING);
			Brushwear.appendChild(Inner_ring);

			for (int i = 0; i < no_brushwear_Inner; i++) {

				// Item ::
				Node Inner_Ring_Item = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_INNER_RING_ITEM);
				Inner_ring.appendChild(Inner_Ring_Item);

				// brush
				Node Brush = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_INNER_RING_BRUSH);
				Text data_Brush = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_INNER_RING_BRUSH + i)));
				Brush.appendChild(data_Brush);
				Inner_Ring_Item.appendChild(Brush);

				// MM
				Node MM = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_INNER_RING_MM);
				Text data_MM = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_INNER_RING_MM + i)));
				MM.appendChild(data_MM);
				Inner_Ring_Item.appendChild(MM);

				// date
				Node Date = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_INNER_RING_DATE);
				Text data_Date = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_INNER_RING_DATE + i)));
				Date.appendChild(data_Date);
				Inner_Ring_Item.appendChild(Date);

				// Type
				Node Type = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_INNER_RING_TYPE);
				Text data_Type = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_INNER_RING_TYPE + i)));
				Type.appendChild(data_Type);
				Inner_Ring_Item.appendChild(Type);

			}

			int no_brushwear_Outer = 0;
			no_brushwear_Outer = Integer.parseInt(hm_BrushWear
					.get(XML_Values.BRUSH_WEAR_OUTER_RING_COUNT));

			// Outer Ring ::
			Node Outer_ring = (Node) doc
					.createElement(XML_Values.BRUSH_WEAR_OUTER_RING);
			Brushwear.appendChild(Outer_ring);

			for (int i = 0; i < no_brushwear_Outer; i++) {

				// Item ::
				Node Outer_Ring_Item = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_OUTER_RING_ITEM);
				Outer_ring.appendChild(Outer_Ring_Item);

				// brush
				Node Brush = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH);
				Text data_Brush = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH + i)));
				Brush.appendChild(data_Brush);
				Outer_Ring_Item.appendChild(Brush);

				// MM
				Node MM = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_OUTER_RING_MM);
				Text data_MM = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_MM + i)));
				MM.appendChild(data_MM);
				Outer_Ring_Item.appendChild(MM);

				// date
				Node Date = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_OUTER_RING_DATE);
				Text data_Date = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_DATE + i)));
				Date.appendChild(data_Date);
				Outer_Ring_Item.appendChild(Date);

				// Type
				Node Type = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_OUTER_RING_TYPE);
				Text data_Type = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_OUTER_RING_TYPE + i)));
				Type.appendChild(data_Type);
				Outer_Ring_Item.appendChild(Type);

			}

			int no_brushwear_Ground = 0;
			no_brushwear_Ground = Integer.parseInt(hm_BrushWear
					.get(XML_Values.BRUSH_WEAR_GROUND_COUNT));

			// Ground ::
			Node Ground = (Node) doc
					.createElement(XML_Values.BRUSH_WEAR_GROUND);
			Brushwear.appendChild(Ground);

			for (int i = 0; i < no_brushwear_Ground; i++) {

				// Item ::
				Node Ground_Item = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_GROUND_ITEM);
				Ground.appendChild(Ground_Item);

				// brush
				Node Brush = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_GROUND_BRUSH);
				Text data_Brush = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_GROUND_BRUSH + i)));
				Brush.appendChild(data_Brush);
				Ground_Item.appendChild(Brush);

				// MM
				Node MM = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_GROUND_MM);
				Text data_MM = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_GROUND_MM + i)));
				MM.appendChild(data_MM);
				Ground_Item.appendChild(MM);

				// date
				Node Date = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_GROUND_DATE);
				Text data_Date = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_GROUND_DATE + i)));
				Date.appendChild(data_Date);
				Ground_Item.appendChild(Date);

				// Type
				Node Type = (Node) doc
						.createElement(XML_Values.BRUSH_WEAR_GROUND_TYPE);
				Text data_Type = doc.createTextNode(EncodeXML(hm_BrushWear
						.get(XML_Values.BRUSH_WEAR_GROUND_TYPE + i)));
				Type.appendChild(data_Type);
				Ground_Item.appendChild(Type);

			}

			item_reports.appendChild(Brushwear);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	// Special item:: BOLT INSULATION (12)
	public void save_Bolt_Insulation(
			HashMap<String, String> hm_bolt_insulation, String millTag) {
		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_bolt_insulation = XPathFactory.newInstance();
			XPath xpath_bolt_insulation = xpf_bolt_insulation.newXPath();
			XPathExpression expression_bolt_insulation = xpath_bolt_insulation
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.BOLT_INSULATION + "_" + millTag);

			Node node_Bolt_Insulation = (Node) expression_bolt_insulation
					.evaluate(doc, XPathConstants.NODE);

			try {
				node_Bolt_Insulation.getParentNode().removeChild(
						node_Bolt_Insulation);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Bolt Insulation::
			Node Bolt_Insulation = doc.createElement(XML_Values.BOLT_INSULATION
					+ "_" + millTag);

			int no_bolt_insulation = 0;
			no_bolt_insulation = Integer.parseInt(hm_bolt_insulation
					.get(XML_Values.BOLT_INSULATION_COUNT));

			for (int i = 0; i < no_bolt_insulation; i++) {
				// Item ::
				Node item_Bolt_Insulation = (Node) doc
						.createElement(XML_Values.BOLT_INSULATION_ITEM);
				Bolt_Insulation.appendChild(item_Bolt_Insulation);

				// key
				Node key = (Node) doc
						.createElement(XML_Values.BOLT_INSULATION_KEY);
				Text data_key = doc.createTextNode(String.valueOf(i + 1));
				key.appendChild(data_key);
				item_Bolt_Insulation.appendChild(key);

				// Value
				Node value = (Node) doc
						.createElement(XML_Values.BOLT_INSULATION_VALUE);
				Text data_value = doc
						.createTextNode(EncodeXML(hm_bolt_insulation
								.get(XML_Values.BOLT_INSULATION_ITEM + i)));
				value.appendChild(data_value);
				item_Bolt_Insulation.appendChild(value);
			}
			item_reports.appendChild(Bolt_Insulation);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	// Special item:: BOLT TORQUE(03)
	public void save_Bolt_Torque(HashMap<String, String> hm_bolt_torque,
			String millTag) {

		try {

			get_File();

			Node item_reports = doc.getElementsByTagName(XML_Values.PROJECT)
					.item(0);

			XPathFactory xpf_bolt_torque = XPathFactory.newInstance();
			XPath xpath_bolt_torque = xpf_bolt_torque.newXPath();
			XPathExpression expression_bolt_torque = xpath_bolt_torque
					.compile("//callback/body/" + XML_Values.PROJECT + "/"
							+ XML_Values.BOLT_TORQUE + "_" + millTag);

			Node node_Bolt_Torque = (Node) expression_bolt_torque.evaluate(doc,
					XPathConstants.NODE);

			try {
				node_Bolt_Torque.getParentNode().removeChild(node_Bolt_Torque);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			// Bolt Torque::
			Node Bolt_Torque = doc.createElement(XML_Values.BOLT_TORQUE + "_"
					+ millTag);

			int no_bolt_torque = 0;
			no_bolt_torque = Integer.parseInt(hm_bolt_torque
					.get(XML_Values.BOLT_TORQUE_COUNT));

			for (int i = 0; i < no_bolt_torque; i++) {
				// Item ::
				Node item_Bolt_Torque = (Node) doc
						.createElement(XML_Values.BOLT_TORQUE_ITEM);
				Bolt_Torque.appendChild(item_Bolt_Torque);

				// Item Bolt Number
				Node Item_Bolt_Number = (Node) doc
						.createElement(XML_Values.BOLT_ITEM_NUMBER);
				Text data_Item_Bolt_Number = doc.createTextNode(hm_bolt_torque
						.get(XML_Values.BOLT_ITEM_NUMBER + i));
				Item_Bolt_Number.appendChild(data_Item_Bolt_Number);
				item_Bolt_Torque.appendChild(Item_Bolt_Number);

				// Item Initial Torque
				Node Item_Initial_torque = (Node) doc
						.createElement(XML_Values.BOLT_INTIAL_TORQUE);
				Text data_Initial_torque = doc.createTextNode(hm_bolt_torque
						.get(XML_Values.BOLT_INTIAL_TORQUE + i));
				Item_Initial_torque.appendChild(data_Initial_torque);
				item_Bolt_Torque.appendChild(Item_Initial_torque);

				// Item Nut Value
				Node Item_Nut_Value = (Node) doc
						.createElement(XML_Values.BOLT_NUT_VALUE);
				Text data_Nut_Value = doc.createTextNode(hm_bolt_torque
						.get(XML_Values.BOLT_NUT_VALUE + i));
				Item_Nut_Value.appendChild(data_Nut_Value);
				item_Bolt_Torque.appendChild(Item_Nut_Value);

				// Item Final Torque
				Node Item_Final_Torque = (Node) doc
						.createElement(XML_Values.BOLT_FINAL_TORQUE);
				Text data_Final_Torque = doc.createTextNode(hm_bolt_torque
						.get(XML_Values.BOLT_FINAL_TORQUE + i));
				Item_Final_Torque.appendChild(data_Final_Torque);
				item_Bolt_Torque.appendChild(Item_Final_Torque);

			}

			// Remarks
			Node Remarks = (Node) doc.createElement(XML_Values.AIRGAP_REMARKS);
			Text data_Remarks = doc.createTextNode(EncodeXML(hm_bolt_torque
					.get(XML_Values.AIRGAP_REMARKS)));
			Remarks.appendChild(data_Remarks);
			Bolt_Torque.appendChild(Remarks);

			item_reports.appendChild(Bolt_Torque);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	public void save_UserDetails(HashMap<String, String> mapUserDetails) {
		try {

			get_File();

			// username::
			Node username_node = doc.getElementsByTagName(XML_Values.USERNAME)
					.item(0);
			username_node.setTextContent(mapUserDetails
					.get(XML_Values.USERNAME));

			// password::
			Node password_node = doc.getElementsByTagName(XML_Values.PASSWORD)
					.item(0);
			password_node.setTextContent(mapUserDetails
					.get(XML_Values.PASSWORD));

			// engineer_id
			if (mapUserDetails.containsKey(XML_Values.ENGINEER_ID)) {
				try {
					Node enginnerid_node = doc.getElementsByTagName(
							XML_Values.ENGINEER_ID).item(0);
					enginnerid_node.setTextContent(mapUserDetails
							.get(XML_Values.ENGINEER_ID));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(file_report);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Util.EncryptReportXML();

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}

	String EncodeXML(String szString) {
		szString = szString.replaceAll("&", "&amp;");
		szString = szString.replaceAll("<", "&lt;");
		szString = szString.replaceAll(">", "&gt;");
		szString = szString.replaceAll("'", "&apos;");
		szString = szString.replaceAll("\"", "&quot;");
		return szString;
	}
}
