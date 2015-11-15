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

import android.content.Context;
import android.util.Log;
import co.uk.pocketapp.gmd.util.Util;

public class PhotographsHandler extends DefaultHandler {
	private Context mContext;
	InputStream inputStream;

	String szCurrentTag = "";
	StringBuffer buf;

	// flag to keep track of XML processing
	private boolean bIsPhotograph = false;
	String m_szPhotoName = "", m_szPhotoDate = "", m_szLocation = "",
			m_szPosition = "", m_szMotorPart = "", m_szComments = "";

	// constructor
	public PhotographsHandler(Context cont, InputStream inputS) {
		super();
		// Log.d("PhotographsHandler", "PhotographsHandler");

		mContext = cont;
		inputStream = inputS;

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
			Log.e("PhotographsHandler", "SAX Error " + se.getMessage());
		} catch (IOException ie) {
			Log.e("PhotographsHandler", "Input Error " + ie.getMessage());
		} catch (Exception oe) {
			Log.e("PhotographsHandler", "Unspecified Error " + oe.getMessage());
		}
	}

	// start of the XML document
	public void startDocument() {
		// Log.i("PhotographsHandler", "Start of XML document");
	}

	// end of the XML document
	public void endDocument() {
		// Log.i("PhotographsHandler", "End of XML document");

		if (!m_szPhotoName.equals("")) {
			String szFileNumber = m_szPhotoName.substring(
					m_szPhotoName.indexOf("_") + 1, m_szPhotoName.indexOf("."));
			if (szFileNumber.length() == 1)
				szFileNumber = "00" + szFileNumber;
			if (szFileNumber.length() == 2)
				szFileNumber = "0" + szFileNumber;

			Util.setPhotoFileNumber(mContext, szFileNumber);
		}
	}

	// opening element tag
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// handle the start of an element

		// Log.i("PhotographsHandler", "startElement :: " + qName);
		buf = new StringBuffer();

		if (qName.equals("photograph")) {
			bIsPhotograph = true;
		}

	}

	// closing element tag
	public void endElement(String uri, String name, String qName) {
		// handle the end of an element

		// Log.i("PhotographsHandler", "endElement :: " + name);
		if (qName.equals("photograph")) {
			bIsPhotograph = false;
			Util.setPhotoEntryDetails(mContext, m_szPhotoName, m_szLocation,
					m_szPosition, m_szComments, m_szMotorPart, m_szPhotoDate);
		}

		if (bIsPhotograph && qName.equals("image_name")) {
			m_szPhotoName = DecodeXML(buf.toString());
		} else if (bIsPhotograph && qName.equals("photo_date")) {
			m_szPhotoDate = DecodeXML(buf.toString());
		} else if (bIsPhotograph && qName.equals("photo_location")) {
			m_szLocation = DecodeXML(buf.toString());
		} else if (bIsPhotograph && qName.equals("position")) {
			m_szPosition = DecodeXML(buf.toString());
		} else if (bIsPhotograph && qName.equals("motor_part")) {
			m_szMotorPart = DecodeXML(buf.toString());
		} else if (bIsPhotograph && qName.equals("comments")) {
			m_szComments = DecodeXML(buf.toString());
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
