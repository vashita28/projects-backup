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

public class BrushWearXMLHandler extends DefaultHandler {
	// string to track each entry
	private String currParent = "";
	String szCurrentTag = "";
	int nCount = 0;

	// flag to keep track of XML processing
	private boolean bisInnerRingItem = false, bIsOuterRingItem = false,
			bIsGroundItem = false;

	// context for user interface
	private Context theContext;

	// input stream
	InputStream inputStream;

	HashMap<String, String> mapBrushWearXML;

	boolean bIsBrushWear = false;

	StringBuffer buf;

	String szMillTag = XML_Values.BRUSH_WEAR;

	// constructor
	public BrushWearXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap, String millTag) {
		super();
		theContext = cont;
		inputStream = inputS;
		mapBrushWearXML = inputHashMap;
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
			Log.e("BrushWearXMLHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("BrushWearXMLHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("BrushWearXMLHandler", "Unspecified Error " + oe.getMessage());
		}

	}

	// start of the XML document
	public void startDocument() {
		// Log.i("BrushWearXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("BrushWearXMLHandler", "End of XML document");
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("BrushWearXMLHandler", "startElement :: " + qName);
		szCurrentTag = qName;
		buf = new StringBuffer();

		if (qName.equals(szMillTag)) {
			bIsBrushWear = true;
		}

		// the element is a item
		if (qName.equals(XML_Values.BRUSH_WEAR_INNER_RING_ITEM) && bIsBrushWear) {
			bisInnerRingItem = true;
			nCount++;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_OUTER_RING_ITEM) && bIsBrushWear) {
			bIsOuterRingItem = true;
			nCount++;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_GROUND_ITEM) && bIsBrushWear) {
			bIsGroundItem = true;
			nCount++;
		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("BrushWearXMLHandler", "endElement :: " + name);

		if (qName.equals(XML_Values.BRUSH_WEAR_INNER_RING) && bIsBrushWear) {
			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_INNER_RING_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_OUTER_RING) && bIsBrushWear) {
			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_OUTER_RING_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_GROUND) && bIsBrushWear) {
			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_GROUND_COUNT,
					String.valueOf(nCount));
			nCount = 0;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_INNER_RING_ITEM) && bIsBrushWear) {
			bisInnerRingItem = false;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_OUTER_RING_ITEM) && bIsBrushWear) {
			bIsOuterRingItem = false;
		}
		if (qName.equals(XML_Values.BRUSH_WEAR_GROUND_ITEM) && bIsBrushWear) {
			bIsGroundItem = false;
		}

		if (qName.equals(szMillTag) && bIsBrushWear) {
			bIsBrushWear = false;
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
		if (bisInnerRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_INNER_RING_BRUSH)
				&& bIsBrushWear) {

			mapBrushWearXML.put(
					XML_Values.BRUSH_WEAR_INNER_RING_BRUSH + nCount,
					buf.toString());

		} else if (bisInnerRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_INNER_RING_MM)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_INNER_RING_MM + nCount,
					buf.toString());

		} else if (bisInnerRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_INNER_RING_DATE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_INNER_RING_DATE + nCount,
					buf.toString());

		} else if (bisInnerRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_INNER_RING_TYPE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_INNER_RING_TYPE + nCount,
					buf.toString());

		}

		if (bIsOuterRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH)
				&& bIsBrushWear) {

			mapBrushWearXML.put(
					XML_Values.BRUSH_WEAR_OUTER_RING_BRUSH + nCount,
					buf.toString());

		} else if (bIsOuterRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_OUTER_RING_MM)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_OUTER_RING_MM + nCount,
					buf.toString());

		} else if (bIsOuterRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_OUTER_RING_DATE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_OUTER_RING_DATE + nCount,
					buf.toString());

		} else if (bIsOuterRingItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_OUTER_RING_TYPE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_OUTER_RING_TYPE + nCount,
					buf.toString());

		}

		if (bIsGroundItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_GROUND_BRUSH)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_GROUND_BRUSH + nCount,
					buf.toString());

		} else if (bIsGroundItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_GROUND_MM)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_GROUND_MM + nCount,
					buf.toString());

		} else if (bIsGroundItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_GROUND_DATE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_GROUND_DATE + nCount,
					buf.toString());

		} else if (bIsGroundItem
				&& szCurrentTag.equals(XML_Values.BRUSH_WEAR_GROUND_TYPE)
				&& bIsBrushWear) {

			mapBrushWearXML.put(XML_Values.BRUSH_WEAR_GROUND_TYPE + nCount,
					buf.toString());

		}

	}

}
