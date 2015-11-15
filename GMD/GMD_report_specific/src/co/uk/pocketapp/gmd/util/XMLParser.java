package co.uk.pocketapp.gmd.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;
import co.uk.pocketapp.gmd.handlerxml.SummaryXMLHandler;

public class XMLParser {

	Context mContext;

	public void parseXML(String inputXML, Context context) {

		mContext = context;

		XmlPullParserFactory factory;
		XmlPullParser xpp = null;
		int eventType = 0;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			xpp = factory.newPullParser();
			xpp.setInput(new StringReader(inputXML));
			eventType = xpp.getEventType();
		} catch (XmlPullParserException e) {
			Log.d("XMLParser : parseXML",
					"XmlPullParserException : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("XMLParser : parseXML", "Exception : " + e.getMessage());
			e.printStackTrace();
		}

		String szCurrentTag = "";

		while (eventType != XmlPullParser.END_DOCUMENT) {
			try {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {

					szCurrentTag = xpp.getName();

					if (xpp.getName().equals(XML_Values.STATUS)) {
						xpp.next();
						String szStatus = xpp.getText().toString();
						Log.d("XMLParser-parseXML():status", "status is : "
								+ szStatus);
					} else if (xpp.getName().equals(
							XML_Values.SUMMARY_OF_INSPECTION)) {

						// convert String into InputStream
						InputStream inputstream = new ByteArrayInputStream(
								inputXML.getBytes());
						SummaryXMLHandler handler = new SummaryXMLHandler(mContext,
								inputstream, new HashMap<String, String>());

					} else if (xpp.getName().equals(
							XML_Values.MAIN_DIFFICULTIES)) {

					}

					// else if (xpp.getName().equals(
					// XML_Values.SUMMARY_OF_INSPECTION)) {
					// int iInsideDepth = xpp.getDepth();
					// xpp.nextTag();
					// int i = 0;
					// String tag = xpp.getName();
					// if (iInsideDepth > 0)
					// while (i < (iInsideDepth - 1) * 2) {
					// if (tag.equals(XML_Values.ITEM)) {
					// xpp.next();
					// if (i % 2 == 0) {
					// String szSummarInspectionItem = xpp
					// .getText().toString();
					// Log.d("XMLParser-parseXML():summary_of_inspection",
					// "Item is : "
					// + szSummarInspectionItem);
					// }
					// i++;
					// xpp.next();
					// }
					// }// end of while
					// }// end of summary of inspection

				} else if (eventType == XmlPullParser.END_TAG) {

				} else if (eventType == XmlPullParser.TEXT) {
				}

				eventType = xpp.next();
			} catch (Exception e) {
				Log.d("XMLParser-parseXML()",
						"Exception Common : " + e.getMessage());
				e.printStackTrace();
				try {
					eventType = xpp.next();
				} catch (XmlPullParserException e1) {
					Log.d("XMLParser-parseXML()",
							"XmlPullParserException : xpp.next() - "
									+ e1.getMessage());
					e1.printStackTrace();
				} catch (IOException e1) {
					Log.d("XMLParser-parseXML()", "IOException : xpp.next() - "
							+ e1.getMessage());
					e1.printStackTrace();
				} catch (Exception e1) {
					Log.d("XMLParser-parseXML()", "Exception : xpp.next() - "
							+ e1.getMessage());
					e1.printStackTrace();
				}
			}
		}

	}
}
