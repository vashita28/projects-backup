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
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Values;

public class UserDetailsXMLHandler extends DefaultHandler {
	private Context mContext;
	InputStream inputStream;

	String szCurrentTag = "";
	StringBuffer buf;
	HashMap<String, String> userDetailsXML;

	// flag to keep track of XML processing
	private boolean isUserDetails = false;

	// constructor
	public UserDetailsXMLHandler(Context cont, InputStream inputS,
			HashMap<String, String> inputHashMap) {
		super();
		// Log.d("UserDetailsXMLHandler", "UserDetailsXMLHandler");

		mContext = cont;
		inputStream = inputS;
		userDetailsXML = inputHashMap;

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
			Log.e("UserDetailsXMLHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("UserDetailsXMLHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("UserDetailsXMLHandler",
					"Unspecified Error " + oe.getMessage());
		}
	}

	// start of the XML document
	public void startDocument() {
		// Log.i("UserDetailsXMLHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("UserDetailsXMLHandler", "End of XML document");

	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("UserDetailsXMLHandler", "startElement :: " + qName);
		buf = new StringBuffer();

		if (qName.equals("user_details")) {
			isUserDetails = true;
		}

	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("UserDetailsXMLHandler", "endElement :: " + name);
		if (qName.equals("user_details")) {
			isUserDetails = false;
		}

		if (isUserDetails && qName.equals("username")) {
			userDetailsXML.put(XML_Values.USERNAME, DecodeXML(buf.toString()));
		} else if (isUserDetails && qName.equals("password")) {
			userDetailsXML.put(XML_Values.PASSWORD, DecodeXML(buf.toString()));
		} else if (isUserDetails && qName.equals("engineer_id")) {
			Util.setEngineerID(mContext, buf.toString());
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