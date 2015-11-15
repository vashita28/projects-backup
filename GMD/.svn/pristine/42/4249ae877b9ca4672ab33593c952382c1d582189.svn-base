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

public class AirGapXMLHandler extends DefaultHandler {
	// string to track each entry
	private String currParent = "";
	String szCurrentTag = "";
	int nCount = 0;

	// flag to keep track of XML processing
	private boolean isPhysicalItem = false;
	private boolean isSensorItem = false;

	// context for user interface
	private Context theContext;

	// input stream
	InputStream inputStream;

	HashMap<String, String> mapAirGapXML;

	boolean bIsAirGap = false;

	StringBuffer buf;
	String szMillTag = XML_Values.AIR_GAP;

	// constructor
	public AirGapXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap,String millTag) {
		super();
		theContext = cont;
		inputStream = inputS;
		mapAirGapXML = inputHashMap;
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
			Log.e("AirGapXMLHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("AirGapXMLHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("AirGapXMLHandler", "Unspecified Error " + oe.getMessage());
		}

	}

	// start of the XML document
	public void startDocument() {
		// Log.i("AirGapXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("AirGapXMLHandler", "End of XML document");
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("AirGapXMLHandler", "startElement :: " + qName);
		szCurrentTag = qName;
		buf = new StringBuffer();

		if (qName.equals(szMillTag)) {
			bIsAirGap = true;
		}

		// the element is a item
		if (qName.equals(XML_Values.AIR_GAP_PHYSICAL_ITEM) && bIsAirGap) {
			isPhysicalItem = true;
			nCount++;
		}
		if (qName.equals(XML_Values.AIR_GAP_SENSORS_ITEM) && bIsAirGap) {
			isSensorItem = true;
			nCount++;
		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("AirGapXMLHandler", "endElement :: " + name);

		if (qName.equals(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT) && bIsAirGap) {
			mapAirGapXML.put(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.AIR_GAP_SENSORS_MEASUREMENT) && bIsAirGap) {
			mapAirGapXML.put(XML_Values.AIR_GAP_SENSORS_MEASUREMENT_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(szMillTag) && bIsAirGap) {
			bIsAirGap = false;
		}
		if (qName.equals(XML_Values.AIR_GAP_PHYSICAL_ITEM) && bIsAirGap) {
			isPhysicalItem = false;
		}
		if (qName.equals(XML_Values.AIR_GAP_SENSORS_ITEM) && bIsAirGap) {
			isSensorItem = false;
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
		if (isPhysicalItem
				&& szCurrentTag
						.equals(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_POSITION
					+ nCount, buf.toString());

		} else if (isPhysicalItem
				&& szCurrentTag
						.equals(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE)
				&& bIsAirGap) {

			mapAirGapXML.put(
					XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_FEEDER_SIDE
							+ nCount, buf.toString());

		} else if (isPhysicalItem
				&& szCurrentTag
						.equals(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_DISCHARGE
					+ nCount, buf.toString());

		} else if (isPhysicalItem
				&& szCurrentTag
						.equals(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_PHYSICAL_MEASUREMENT_REMARKS
					+ nCount, buf.toString());

		}

		if (isSensorItem && szCurrentTag.equals(XML_Values.AIR_GAP_POSITION)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_POSITION + nCount,
					buf.toString());

		} else if (isSensorItem
				&& szCurrentTag.equals(XML_Values.AIR_GAP_FEEDER_SIDE)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_FEEDER_SIDE + nCount,
					buf.toString());

		} else if (isSensorItem
				&& szCurrentTag.equals(XML_Values.AIR_GAP_DISCHARGE_SIDE)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_DISCHARGE_SIDE + nCount,
					buf.toString());

		} else if (isSensorItem
				&& szCurrentTag.equals(XML_Values.AIR_GAP_REMARKS) && bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIR_GAP_REMARKS + nCount,
					buf.toString());

		}

		if (szCurrentTag.equals(XML_Values.AIRGAP_WINDING_T) && bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIRGAP_WINDING_T, buf.toString());
		} else if (szCurrentTag.equals(XML_Values.AIRGAP_STATOR_T) && bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIRGAP_STATOR_T, buf.toString());
		} else if (szCurrentTag.equals(XML_Values.AIRGAP_STATOR_T) && bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIRGAP_STATOR_T, buf.toString());
		} else if (szCurrentTag.equals(XML_Values.AIRGAP_AMBIENT_T)
				&& bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIRGAP_AMBIENT_T, buf.toString());
		} else if (szCurrentTag.equals(XML_Values.AIRGAP_REMARKS) && bIsAirGap) {

			mapAirGapXML.put(XML_Values.AIRGAP_REMARKS, buf.toString());
		}

	}

}
