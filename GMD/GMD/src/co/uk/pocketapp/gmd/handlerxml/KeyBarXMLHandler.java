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

public class KeyBarXMLHandler extends DefaultHandler {
	// string to track each entry
	private String currParent = "";
	String szCurrentTag = "";
	int nCount = 0;

	// flag to keep track of XML processing
	private boolean isItem = false;

	// context for user interface
	private Context theContext;

	// input stream
	InputStream inputStream;

	HashMap<String, String> mapKeybaryXML;

	boolean bIsKeyBar = false;

	StringBuffer buf;

	String szMillTag = XML_Values.KEYBAR;

	// constructor
	public KeyBarXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap, String millTag) {
		super();
		theContext = cont;
		inputStream = inputS;
		mapKeybaryXML = inputHashMap;
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
			Log.e("KeyBarXMLHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("KeyBarXMLHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("KeyBarXMLHandler", "Unspecified Error " + oe.getMessage());
		}

	}

	// start of the XML document
	public void startDocument() {
		// Log.i("KeyBarXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("KeyBarXMLHandler", "End of XML document");
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("KeyBarXMLHandler", "startElement :: " + qName);
		szCurrentTag = qName;
		buf = new StringBuffer();

		if (qName.equals(szMillTag)) {
			bIsKeyBar = true;
		}

		// the element is a item
		if (qName.equals(XML_Values.KEYBAR_ITEM) && bIsKeyBar) {
			isItem = true;
			nCount++;
		}
	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("KeyBarXMLHandler", "endElement :: " + name);

		if (qName.equals(szMillTag) && bIsKeyBar) {
			mapKeybaryXML.put(XML_Values.KEYBAR_COUNT, String.valueOf(nCount));
			bIsKeyBar = false;
			nCount = 0;
		}
		if (qName.equals(XML_Values.KEYBAR_ITEM) && bIsKeyBar) {
			isItem = false;
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
		if (isItem && szCurrentTag.equals(XML_Values.KEY) && bIsKeyBar) {

			mapKeybaryXML.put(XML_Values.KEY + nCount, buf.toString());

		} else if (isItem && szCurrentTag.equals(XML_Values.KEYBAR_VALUE)
				&& bIsKeyBar) {

			mapKeybaryXML.put(XML_Values.KEYBAR_VALUE + nCount, buf.toString());

		}
	}

}
