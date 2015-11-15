package co.uk.pocketapp.gmd.handlerxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;
import co.uk.pocketapp.gmd.util.XML_Values;

public class SiteConditionsXMLHandler extends DefaultHandler {
	// string to track each entry
	private String currParent = "";
	String szCurrentTag = "";
	int nCount = -1;

	// flag to keep track of XML processing
	private boolean isItem = false;

	// context for user interface
	private Context mContext;

	// input stream
	InputStream inputStream;

	HashMap<String, String> siteConditionsXML;

	boolean bIsSiteConditions = false;

	StringBuffer buf;

	// constructor
	public SiteConditionsXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap) {
		super();
		mContext = cont;
		inputStream = inputS;
		siteConditionsXML = inputHashMap;

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

	// start of the XML document
	public void startDocument() {
		// Log.i("XMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("XMLHandler", "End of XML document");
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("XMLHandler", "startElement :: " + qName);

		szCurrentTag = qName;
		buf = new StringBuffer();

		if (qName.equals(XML_Values.SITE_CONDITIONS)) {
			bIsSiteConditions = true;
		}

		// the element is a item
		if (qName.equals("item") && bIsSiteConditions) {
			isItem = true;
			nCount++;
		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("XMLHandler", "endElement :: " + name);

		if (qName.equals(XML_Values.SITE_CONDITIONS)) {
			bIsSiteConditions = false;
		}

		// set item tag to false
		if (qName.equals("item")) {
			isItem = false;
		}

		if (qName.equals(XML_Values.SITE_STORAGE_CONDITIONS)) {
			siteConditionsXML.put("Count_SSC", String.valueOf(nCount));
			resetCounter();
		} else if (qName.equals(XML_Values.SPARE_PARTS_AT_SITE)) {
			siteConditionsXML.put("Count_SPAS", String.valueOf(nCount));
			resetCounter();
		} else if (qName.equals(XML_Values.TOOLS_INSTRUMENTS)) {
			siteConditionsXML.put("Count_TNI", String.valueOf(nCount));
			resetCounter();
		}

		// prepare for the next item
		if (isItem && szCurrentTag.equals(XML_Values.STORE)) {

			siteConditionsXML.put(XML_Values.STORE + nCount, buf.toString());

		} else if (isItem && szCurrentTag.equals(XML_Values.CHECKED)) {

			siteConditionsXML.put(XML_Values.CHECKED + nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.DATE_SITE_STORAGE_CONDITIONS)
				&& bIsSiteConditions) {

			siteConditionsXML.put(XML_Values.DATE_SITE_STORAGE_CONDITIONS
					+ nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag
						.equals(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS)) {

			siteConditionsXML.put(XML_Values.REMARKS_SITE_STORAGE_CONDITIONS
					+ nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.SPARE_PART_MATERIAL)) {

			siteConditionsXML.put(XML_Values.SPARE_PART_MATERIAL + nCount,
					buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.QUANTITY_SPARE_PART_AT_SITE)) {

			siteConditionsXML.put(XML_Values.QUANTITY_SPARE_PART_AT_SITE
					+ nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.DATE_SPARE_PART_AT_SITE)) {

			siteConditionsXML.put(XML_Values.DATE_SPARE_PART_AT_SITE + nCount,
					buf.toString());
		} else if (isItem
				&& szCurrentTag.equals(XML_Values.REMARKS_SPARE_PART_AT_SITE)) {

			siteConditionsXML.put(XML_Values.REMARKS_SPARE_PART_AT_SITE
					+ nCount, buf.toString());
		} else if (isItem && szCurrentTag.equals(XML_Values.TOOLS)) {

			siteConditionsXML.put(XML_Values.TOOLS + nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.QUANTITY_TOOLS_INSTRUMENTS)) {

			siteConditionsXML.put(XML_Values.QUANTITY_TOOLS_INSTRUMENTS
					+ nCount, buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.DATE_TOOLS_INSTRUMENT)) {

			siteConditionsXML.put(XML_Values.DATE_TOOLS_INSTRUMENT + nCount,
					buf.toString());

		} else if (isItem
				&& szCurrentTag.equals(XML_Values.REMARKS_TOOLS_INSTRUMENT)) {

			siteConditionsXML.put(XML_Values.REMARKS_TOOLS_INSTRUMENT + nCount,
					buf.toString());
		}

	}

	void resetCounter() {
		// reset the variable for future items
		currParent = "";
		szCurrentTag = "";
		nCount = -1;
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
	}

}
