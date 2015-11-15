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

public class CorePartitionsXMLHandler extends DefaultHandler {
	// string to track each entry
	private String currParent = "";
	String szCurrentTag = "";
	int nCount = 0;

	// flag to keep track of XML processing
	private boolean isFeederItem = false, isDischargeItem = false;

	// context for user interface
	private Context theContext;

	// input stream
	InputStream inputStream;

	HashMap<String, String> mapCorePartitionsXML;

	boolean bIsCorePartitions = false;

	StringBuffer buf;

	String szMillTag = XML_Values.CORE_PARTITIONS;

	// constructor
	public CorePartitionsXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap, String millTag) {
		super();
		theContext = cont;
		inputStream = inputS;
		mapCorePartitionsXML = inputHashMap;
		szMillTag = szMillTag + "_" + millTag;

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
			Log.e("CorePartitionsXMLHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("CorePartitionsXMLHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("CorePartitionsXMLHandler",
					"Unspecified Error " + oe.getMessage());
		}

	}

	// start of the XML document
	public void startDocument() {
		// Log.i("CorePartitionsXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("CorePartitionsXMLHandler", "End of XML document");
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("CorePartitionsXMLHandler", "startElement :: " + qName);
		szCurrentTag = qName;
		buf = new StringBuffer();

		if (qName.equals(szMillTag)) {
			bIsCorePartitions = true;
		}

		// the element is a item
		if (qName.equals(XML_Values.CORE_PARTITIONS_FEEDER_ITEM)
				&& bIsCorePartitions) {
			isFeederItem = true;
			nCount++;
		}

		if (qName.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_ITEM)
				&& bIsCorePartitions) {
			isDischargeItem = true;
			nCount++;
		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("CorePartitionsXMLHandler", "endElement :: " + name);

		if (qName.equals(szMillTag))
			bIsCorePartitions = false;

		if (qName.equals(XML_Values.CORE_PARTITIONS_FEEDER_SIDE)
				&& bIsCorePartitions) {
			mapCorePartitionsXML.put(XML_Values.CORE_PARTITIONS_FEEDER_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_SIDE)
				&& bIsCorePartitions) {
			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_DISCHARGE_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.CORE_PARTITIONS_FEEDER_ITEM)
				&& bIsCorePartitions) {
			isFeederItem = false;
		}
		if (qName.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_ITEM)
				&& bIsCorePartitions) {
			isDischargeItem = false;
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
		if (isFeederItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_FEEDER_POSITION)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(XML_Values.CORE_PARTITIONS_FEEDER_POSITION
					+ nCount, buf.toString());

		} else if (isFeederItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_FEEDER_RADIAL_DISPLACEMENT
							+ nCount, buf.toString());

		} else if (isFeederItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_FEEDER_AXIAL_DISPLACEMENT
							+ nCount, buf.toString());

		} else if (isFeederItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(XML_Values.CORE_PARTITIONS_FEEDER_REMARKS
					+ nCount, buf.toString());

		}

		if (isDischargeItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_DISCHARGE_POSITION + nCount,
					buf.toString());

		} else if (isDischargeItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_DISCHARGE_RADIAL_GAP + nCount,
					buf.toString());

		} else if (isDischargeItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_DISCHARGE_AXIAL_GAP + nCount,
					buf.toString());

		} else if (isDischargeItem
				&& szCurrentTag
						.equals(XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT)
				&& bIsCorePartitions) {

			mapCorePartitionsXML.put(
					XML_Values.CORE_PARTITIONS_DISCHARGE_DISPLACEMENT + nCount,
					buf.toString());

		}

	}

}
