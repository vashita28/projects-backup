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
import android.util.Log;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.Util;

public class ServiceCheckListReadValuesHandler extends DefaultHandler {

	// string to track each entry
	String szCurrentTag = "";

	private boolean isTasks = false;

	boolean bIsServiceCheckList = false;
	private Context mContext;

	InputStream inputStream;

	String m_szMillID = "";
	String m_szMillName = "";
	String m_szGMDType = "";

	String m_szTaskCompleted = "", m_szTaskContent = "";

	String m_szItemID = "", m_szItemName = "", m_szParentID = "";

	String m_szCurrentNodeAsPerDB = "";

	StringBuffer buf;

	// constructor
	public ServiceCheckListReadValuesHandler(Context cont, InputStream inputS) {
		super();
		// Log.d("ServiceCheckListReadValuesHandler",
		// "ServiceCheckListReadValuesHandler");

		mContext = cont;
		inputStream = inputS;

		if (Util.getIsServiceCheclistValuesInserted(mContext)) {

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
				Log.e("ServiceCheckListReadValuesHandler",
						"SAX Error " + se.getMessage());
			} catch (IOException ie) {
				Log.e("ServiceCheckListReadValuesHandler",
						"Input Error " + ie.getMessage());
			} catch (Exception oe) {
				Log.e("ServiceCheckListReadValuesHandler", "Unspecified Error "
						+ oe.getMessage());
			}
		}
	}

	// start of the XML document
	public void startDocument() {
		// Log.i("ServiceCheckListReadValuesHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("ServiceCheckListReadValuesHandler", "End of XML document");
		Util.setIsLeafNodeValuesInserted(mContext, true);
		Util.setIsServiceCheclistValuesInserted(mContext, true);
		// mContext.getContentResolver().notifyChange(
		// DataProvider.Tasks.CONTENT_URI, null);
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element
		// Log.i("ServiceCheckListReadValuesHandler", "StartElement :: " +
		// qName);
		szCurrentTag = qName;

		if (isTasks) {
			m_szTaskCompleted = atts.getValue("task_completed");
			if (m_szTaskCompleted == null || m_szTaskCompleted.equals("")) {
				m_szTaskCompleted = "false";
			}
		}

		buf = new StringBuffer();

		if (qName.equals("service_checklist")) {
			bIsServiceCheckList = true;
			Log.i("ServiceCheckListReadValuesHandler",
					"ServiceCheckList STARTS :: ");
		}
		if (!qName.equals("tasks")) {

			// set item tag to false

		}// the element is a item
		else if (qName.equals("tasks"))
			isTasks = true;

		if (qName.equals("work_scope")) {
			m_szCurrentNodeAsPerDB = "Work scope";
		} else if (qName.equals("special_type")) {
			m_szCurrentNodeAsPerDB = "Special Type";
		} else if (qName.equals("work_carried_out")) {
			m_szCurrentNodeAsPerDB = "Work carried out";
		} else if (qName.equals("conditions")) {
			m_szCurrentNodeAsPerDB = "Conditions";
		} else if (qName.equals("recommendations")) {
			m_szCurrentNodeAsPerDB = "Recommendations";
		} else if (qName.equals("comments")) {
			m_szCurrentNodeAsPerDB = "Comments";
		} else if (qName.equals("photograph")) {
			m_szCurrentNodeAsPerDB = "Photograph";
		} else if (qName.equals("helpsupport")) {
			m_szCurrentNodeAsPerDB = "Help-Support";
		}

	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("ServiceCheckListReadValuesHandler", "EndElement :: " + qName);

		if (qName.equals("service_checklist")) {
			bIsServiceCheckList = false;
			Log.i("ServiceCheckListReadValuesHandler",
					"ServiceCheckList ENDS :: ");
		}

		if (qName.equals("tasks")) {
			isTasks = false;
		}

		if (szCurrentTag.equals("id"))
			m_szItemID = buf.toString();
		else if (szCurrentTag.equals("parent_id"))
			m_szParentID = buf.toString();
		else if (szCurrentTag.equals("name") && bIsServiceCheckList) {
			m_szItemName = DecodeXML(buf.toString());
		}

		if (isTasks) {

			// String emptyString = "";
			// if (buf.toString().equals("null")) {
			// buf.delete(0, buf.length());
			// buf.append(emptyString);
			// }

			m_szTaskContent = buf.toString();
			Log.d("ServiceCheckListReadValuesHandler",
					"Handler Tag Name is :: " + name + " Value is :: "
							+ m_szTaskContent + " Task Completed :: "
							+ m_szTaskCompleted);
			ContentValues taskValues = new ContentValues();
			taskValues.put(DataProvider.Tasks.TASK_CONTENT,
					DecodeXML(m_szTaskContent));
			taskValues
					.put(DataProvider.Tasks.TASK_COMPLETED, m_szTaskCompleted);

			String szWhere = DataProvider.Tasks.MILL_ID + " ='" + m_szMillID
					+ "' AND " + DataProvider.Tasks.SERVICES_ITEM_ID + " ='"
					+ m_szItemID + "' AND "
					+ DataProvider.Tasks.SERVICES_PARENT_ID + " ='"
					+ m_szParentID + "' AND " + DataProvider.Tasks.TASK_NAME
					+ " !='Report a problem' AND ";

			if (m_szCurrentNodeAsPerDB.equals("Special Type")) {
				taskValues.put(DataProvider.Tasks.TASK_CONTENT,
						DecodeXML(m_szCurrentNodeAsPerDB));
				szWhere = szWhere + DataProvider.Tasks.TASK_CONTENT + " ='"
						+ m_szCurrentNodeAsPerDB + "'";
			} else {
				szWhere = szWhere + DataProvider.Tasks.TASK_NAME + " ='"
						+ m_szCurrentNodeAsPerDB + "'";
			}

			Log.d("ServiceCheckListReadValuesHandler", "Handler Where is:: "
					+ szWhere);

			mContext.getContentResolver().update(
					DataProvider.Tasks.CONTENT_URI, taskValues, szWhere, null);

			m_szTaskContent = "";
			m_szTaskCompleted = "";
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
		if (szCurrentTag.equals("mill_id") && currText.length() > 0)
			m_szMillID = currText;
		else if (szCurrentTag.equals("mill_name") && currText.length() > 0)
			m_szMillName = currText;
		else if (szCurrentTag.equals("gmd_type") && currText.length() > 0) {
			m_szGMDType = currText;

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
