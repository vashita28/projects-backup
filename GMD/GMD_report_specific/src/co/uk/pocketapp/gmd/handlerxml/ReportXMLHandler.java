package co.uk.pocketapp.gmd.handlerxml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.Util;

public class ReportXMLHandler extends DefaultHandler {

	// string to track each entry
	String szCurrentTag = "";

	private boolean isTasks = false;

	boolean bIsServiceCheckList = false;
	private Context mContext;

	InputStream inputStream;

	String m_szReportID = "";
	String m_szSiteName = "";

	String m_szMillID = "";
	String m_szMillName = "";
	String m_szGMDType = "";
	String m_szStatus = "";
	String m_szErrorCode = "";
	String m_szErrorMessage = "";

	int nServiceCount = -1;

	String m_szMainParID = "", m_szMainID = "", m_szSecondGenParentID = "",
			m_szSecondGenID = "", m_szThirdGenParentID = "",
			m_szThirdGenID = "";

	String m_szWork_Scope = "", m_szWork_Scope_Comments = "",
			m_szWork_Carried_Out = "", m_szConditions = "",
			m_szRecommendations = "";

	String m_szSpecialType = "";

	ContentValues leafNodeValues;

	String m_szItemID = "", m_szItemName = "", m_szParentID = "";

	StringBuffer buf;

	// constructor
	public ReportXMLHandler(Context cont, InputStream inputS, String szStatus) {
		super();
		mContext = cont;
		inputStream = inputS;
		m_szStatus = szStatus;

		if (!Util.getIsServiceCheclistValuesInserted(mContext)) {
			// take care of SAX, input and parsing errors
			try {
				// create a parser
				SAXParserFactory parseFactory = SAXParserFactory.newInstance();
				SAXParser xmlParser = parseFactory.newSAXParser();
				// get an XML reader
				XMLReader xmlIn = xmlParser.getXMLReader();

				// instruct the app to use this object as the handler
				xmlIn.setContentHandler(this);
				xmlIn.parse(new InputSource(inputStream));

			} catch (SAXException se) {
				Log.e("MainActivity", "SAX Error " + se.getMessage());
			} catch (IOException ie) {
				Log.e("MainActivity", "Input Error " + ie.getMessage());
			} catch (Exception oe) {
				Log.e("MainActivity", "Unspecified Error " + oe.getMessage());
			}
		}
	}

	// start of the XML document
	public void startDocument() {
		// Log.i("ReportXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("ReportXMLHandler", "End of XML document");
		if (m_szStatus.equals("1")
				&& !Util.getIsServiceCheclistValuesInserted(mContext)) {
			Util.setIsLeafNodeValuesInserted(mContext, true);
			Util.setIsServiceCheclistValuesInserted(mContext, true);
			// new XML_Creation().delete_Service_Checklist(null);
		}
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element
		// Log.i("ReportXMLHandler", "StartElement :: " + qName);
		szCurrentTag = qName;

		buf = new StringBuffer();

		if (m_szStatus.equals("1")) {
			if (qName.equals("service_checklist")) {
				bIsServiceCheckList = true;
				Log.i("ReportXMLHandler", "ServiceCheckList STARTS :: ");
			}
			if (!qName.equals("tasks")) {

				// set item tag to false

				// XMLContent = XMLContent + "<" + qName + ">" + eol;

			}// the element is a item
			else if (qName.equals("tasks"))
				isTasks = true;

			// if (isTasks && !Util.getIsLeafNodeValuesInserted(mContext)
			// && !qName.equals("tasks")) {
			//
			// Cursor leafnodeCursor = mContext.getContentResolver().query(
			// DataProvider.Child_Leaf.CONTENT_URI,
			// null,
			// DataProvider.Child_Leaf.REPORT_ID + " ='" + m_szReportID
			// + "' AND " + DataProvider.Child_Leaf.MILL_ID
			// + " ='" + m_szMillID + "' AND "
			// + DataProvider.Child_Leaf.CHILD_LEAF + " ='"
			// + qName + "'", null, null);
			//
			// if (leafnodeCursor != null && leafnodeCursor.getCount() == 0) {
			// leafNodeValues = new ContentValues();
			// leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
			// m_szReportID);
			// leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID, m_szMillID);
			// leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF, qName);
			// mContext.getContentResolver().insert(
			// DataProvider.Child_Leaf.CONTENT_URI, leafNodeValues);
			// }
			// leafnodeCursor.close();
			// }

			if (qName.equals("services"))
				nServiceCount++;

		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("ReportXMLHandler", "EndElement :: " + qName);
		if (szCurrentTag.equals("status"))
			m_szStatus = buf.toString();

		if (m_szStatus.equals("1")) {

			if (qName.equals("service_checklist")) {
				bIsServiceCheckList = false;
				Log.i("ReportXMLHandler", "ServiceCheckList ENDS :: ");
			}

			if (qName.equals("tasks")) {
				isTasks = false;

				Cursor leafnodeCursor = mContext.getContentResolver().query(
						DataProvider.Child_Leaf.CONTENT_URI,
						null,
						DataProvider.Child_Leaf.REPORT_ID + " ='"
								+ m_szReportID + "' AND "
								+ DataProvider.Child_Leaf.MILL_ID + " ='"
								+ m_szMillID + "' AND "
								+ DataProvider.Child_Leaf.CHILD_LEAF
								+ " ='Comments'", null, null);

				if (leafnodeCursor != null && leafnodeCursor.getCount() == 0) {

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Work scope");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Special Type");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Work carried out");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Conditions");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Recommendations");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues = new ContentValues();
					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Comments");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Photograph");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Help-Support");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);

					leafNodeValues.put(DataProvider.Child_Leaf.REPORT_ID,
							m_szReportID);
					leafNodeValues.put(DataProvider.Child_Leaf.MILL_ID,
							m_szMillID);
					leafNodeValues.put(DataProvider.Child_Leaf.CHILD_LEAF,
							"Report a problem");
					mContext.getContentResolver()
							.insert(DataProvider.Child_Leaf.CONTENT_URI,
									leafNodeValues);
				}
				leafnodeCursor.close();
			}

			if (qName.equals("services"))
				nServiceCount--;

			if (szCurrentTag.equals("id"))
				m_szItemID = buf.toString();
			else if (szCurrentTag.equals("parent_id"))
				m_szParentID = buf.toString();
			else if (szCurrentTag.equals("name") && bIsServiceCheckList) {
				m_szItemName = DecodeXML(buf.toString());

				ContentValues servicesValues = new ContentValues();
				servicesValues.put(DataProvider.Services.REPORT_ID,
						m_szReportID);
				servicesValues.put(DataProvider.Services.MILL_ID, m_szMillID);
				servicesValues.put(DataProvider.Services.ITEM_NAME,
						m_szItemName);
				servicesValues.put(DataProvider.Services.PARENT_ID,
						m_szParentID);
				servicesValues.put(DataProvider.Services.ITEM_ID, m_szItemID);
				mContext.getContentResolver().insert(
						DataProvider.Services.CONTENT_URI, servicesValues);
			}

			{
				if (szCurrentTag.equals("parent_id") && nServiceCount == 0)
					m_szMainParID = buf.toString();
				else if (szCurrentTag.equals("id") && nServiceCount == 0)
					m_szMainID = buf.toString();

				else if (szCurrentTag.equals("parent_id") && nServiceCount == 1)
					m_szSecondGenParentID = buf.toString();
				else if (szCurrentTag.equals("id") && nServiceCount == 1)
					m_szSecondGenID = buf.toString();

				else if (szCurrentTag.equals("parent_id") && nServiceCount == 2)
					m_szThirdGenParentID = buf.toString();
				else if (szCurrentTag.equals("id") && nServiceCount == 2)
					m_szThirdGenID = buf.toString();

				if (isTasks && szCurrentTag.equals("work_scope")) {
					m_szWork_Scope = buf.toString();
					if (m_szWork_Scope.equals("null"))
						m_szWork_Scope = "";
				} else if (isTasks
						&& szCurrentTag.equals("work_scope_comments")) {
					m_szWork_Scope_Comments = buf.toString();
					if (m_szWork_Scope_Comments.equals("null"))
						m_szWork_Scope_Comments = "";
				} else if (isTasks && szCurrentTag.equals("work_carried_out")) {
					m_szWork_Carried_Out = buf.toString();
					if (m_szWork_Carried_Out.equals("null"))
						m_szWork_Carried_Out = "";
				} else if (isTasks && szCurrentTag.equals("conditions")) {
					m_szConditions = buf.toString();
					if (m_szConditions.equals("null"))
						m_szConditions = "";
				} else if (isTasks && szCurrentTag.equals("recommendations")) {
					m_szRecommendations = buf.toString();
					if (m_szRecommendations.equals("null"))
						m_szRecommendations = "";
				} else if (isTasks && szCurrentTag.equals("special_type")) {
					m_szSpecialType = buf.toString();
					if (m_szSpecialType.equals("null"))
						m_szSpecialType = "";

					ContentValues taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME, "Work scope");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT,
							m_szWork_Scope);
					taskValues.put(DataProvider.Tasks.TASK_COMMENT,
							m_szWork_Scope_Comments);
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME,
							m_szSpecialType);
					if (!m_szSpecialType.equals(""))
						taskValues.put(DataProvider.Tasks.TASK_CONTENT,
								"Special Type");
					else
						taskValues.put(DataProvider.Tasks.TASK_CONTENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");

					if (!m_szSpecialType.equals(""))
						taskValues.put(DataProvider.Tasks.TASK_COMPLETED,
								"false");
					else
						taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "");

					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME,
							"Work carried out");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT,
							m_szWork_Carried_Out);
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME, "Conditions");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT,
							m_szConditions);
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME,
							"Recommendations");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT,
							m_szRecommendations);
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME, "Comments");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME, "Photograph");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues
							.put(DataProvider.Tasks.TASK_NAME, "Help-Support");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, "false");
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					taskValues = new ContentValues();
					taskValues.put(DataProvider.Tasks.REPORT_ID, m_szReportID);
					taskValues.put(DataProvider.Tasks.MILL_ID, m_szMillID);
					taskValues.put(DataProvider.Tasks.SERVICES_ITEM_ID,
							m_szItemID);
					taskValues.put(DataProvider.Tasks.SERVICES_PARENT_ID,
							m_szParentID);
					taskValues.put(DataProvider.Tasks.TASK_NAME,
							"Report a problem");
					taskValues.put(DataProvider.Tasks.TASK_CONTENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMMENT, "");
					taskValues.put(DataProvider.Tasks.TASK_COMPLETED, ""); // non-mandatory
					taskValues.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
							m_szMainID);
					taskValues.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
							m_szSecondGenID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
							m_szMainParID);
					taskValues.put(
							DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
							m_szSecondGenParentID);
					mContext.getContentResolver().insert(
							DataProvider.Tasks.CONTENT_URI, taskValues);

					m_szSpecialType = "";
				}
			}
		} else if (m_szStatus.equals("0")) {

			if (szCurrentTag.equals("error_code"))
				m_szErrorCode = buf.toString();
			else if (szCurrentTag.equals("error_msg"))
				m_szErrorMessage = buf.toString();
		}

	}

	// element content
	public void characters(char ch[], int start, int length) {
		// process the element content

		// string to store the character content
		String currText = "";
		// loop through the character array
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '\\':
				break;
			case '"':
				break;
			case '\n':
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				currText += ch[i];
				if (buf != null)
					buf.append(ch[i]);
				break;
			}
		}

		// prepare for the next item
		if (szCurrentTag.equals("report_id") && currText.length() > 0)
			m_szReportID = currText;
		else if (szCurrentTag.equals("site_name") && currText.length() > 0)
			m_szSiteName = currText;
		else if (szCurrentTag.equals("mill_id") && currText.length() > 0)
			m_szMillID = currText;
		else if (szCurrentTag.equals("mill_name") && currText.length() > 0) {
			m_szMillName = currText;

			boolean bIsFirst = true;
			if (bIsFirst) {
				Util.setMillName(mContext, m_szMillName);
				bIsFirst = false;
			}
		} else if (szCurrentTag.equals("gmd_type") && currText.length() > 0) {
			m_szGMDType = currText;

			ContentValues millValues = new ContentValues();
			millValues.put(DataProvider.Mill.MILL_ID, m_szMillID);
			millValues.put(DataProvider.Mill.MILL_NAME, m_szMillName);
			millValues.put(DataProvider.Mill.REPORT_ID, m_szReportID);
			millValues.put(DataProvider.Mill.GMD_TYPE, m_szGMDType);
			mContext.getContentResolver().insert(DataProvider.Mill.CONTENT_URI,
					millValues);

			Util.setMillID(mContext, m_szMillName, m_szMillID);
		}

	}

	public String getStatus() {
		return m_szStatus;
	}

	public String getErrorCode() {
		return m_szErrorCode;
	}

	public String getErrorMessage() {
		return m_szErrorMessage;
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
