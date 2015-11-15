package co.uk.pocketapp.gmd.handlerxml;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Values;

public class ReportDetailsXMLHandler extends DefaultHandler {

    // string to track each entry
    String szCurrentTag = "";
    int nCountItem = 0;

    // flag to keep track of XML processing
    private boolean isItem = false, isCurrentSelectedMill = false,
            isReportDetails = false;

    // context for user interface
    private Context mContext;

    // input stream
    InputStream inputStream;

    StringBuffer buf;
    HashMap<String, String> repDetailsXML;

    // constructor
    public ReportDetailsXMLHandler(Context cont, InputStream inputS,
                                   HashMap<String, String> inputHashMap) {
        super();
        mContext = cont;
        inputStream = inputS;
        repDetailsXML = inputHashMap;

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
            Log.e("RepDetailsHandler", "SAX Error " + se.getMessage());
        } catch (IOException ie) {
            Log.e("RepDetailsHandler", "Input Error " + ie.getMessage());
        } catch (Exception oe) {
            Log.e("RepDetailsHandler", "Unspecified Error " + oe.getMessage());
            oe.printStackTrace();
        }

    }

    // start of the XML document
    public void startDocument() {
        // Log.i("DataHandler", "Start of XML document");
    }

    // end of the XML document
    public void endDocument() {
        // Log.i("DataHandler", "End of XML document");
    }

    // opening element tag
    public void startElement(String uri, String name, String qName,
                             Attributes atts) {
        // handle the start of an element
        // Log.i("RepDetailsHandler", "startElement :: " + qName);
        szCurrentTag = qName;
        buf = new StringBuffer();

        if (qName.equals("item")) {
            isItem = true;
            nCountItem++;
        }

        if (qName.equals(XML_Values.PERSONNEL_ABB_ITEM)) {
            isItem = true;
            nCountItem++;
        }

        if (qName.equals(XML_Values.PERSONNEL_CLIENT_ITEM)) {
            isItem = true;
            nCountItem++;
        }

        if (isReportDetails && qName.equals(XML_Values.MILL)) {
            String szMillIDInXML = atts.getValue("id");
            String szMIllIDCurrentSelected = Util.getMillID(mContext,
                    Util.getMillName(mContext));

            if (szMillIDInXML.equals(szMIllIDCurrentSelected)) {
                isCurrentSelectedMill = true;
            }
        }

        if (qName.equals("report_details")) {
            isReportDetails = true;
        }
    }

    // closing element tag
    public void endElement(String uri, String name, String qName) {
        // handle the end of an element

        // Log.i("RepDetailsHandler", "endElement :: " + name);
        if (qName.equals(XML_Values.LIST_OF_DIST)) {
//            Log.i("RepDetailsHandler", "ITEM LOD COUNT :: " + nCountItem);
            repDetailsXML.put(XML_Values.LOD_COUNT, String.valueOf(nCountItem));
            nCountItem = 0;
        } else if (qName.equals("item")) {// set item tag to false
            isItem = false;
        } else if (qName.equals(XML_Values.PERSONNEL_ABB_ITEM)) {
            isItem = false;
        } else if (qName.equals(XML_Values.PERSONNEL_CLIENT_ITEM)) {
            isItem = false;
        } else if (qName.equals(XML_Values.DISTRIBUTION_TO_CUSTOMER)) {
//            Log.i("RepDetailsHandler", "ITEM DTC COUNT :: " + nCountItem);
            repDetailsXML.put(XML_Values.DTC_COUNT, String.valueOf(nCountItem));
            nCountItem = 0;
        } else if (qName.equals(XML_Values.PERSONNEL_INVOLVED_FROM_ABB)) {
//            Log.i("RepDetailsHandler", "ABB ITEM COUNT :: " + nCountItem);
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_COUNT,
                    String.valueOf(nCountItem));
            nCountItem = 0;
        } else if (qName.equals(XML_Values.PERSONNEL_INVOLVED_FROM_CLIENT)) {
//            Log.i("RepDetailsHandler", "CLIENT ITEM COUNT :: " + nCountItem);
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_COUNT,
                    String.valueOf(nCountItem));
            nCountItem = 0;
        } else if (isReportDetails && qName.equals(XML_Values.MILL)) {
            isCurrentSelectedMill = false;
        } else if (qName.equals("report_details")) {
            isReportDetails = false;
        }

        String emptyString = "";

        if (buf.toString().equals("null")) {
            buf.delete(0, buf.length());
            buf.append(emptyString);
        }

        if (szCurrentTag.equals(XML_Values.CUSTOMER)) {
            repDetailsXML.put(XML_Values.CUSTOMER, buf.toString());
        } else if (szCurrentTag.equals(XML_Values.SITE))
            repDetailsXML.put(XML_Values.SITE, buf.toString());
        else if (szCurrentTag.equals(XML_Values.COUNTRY_NAME))
            repDetailsXML.put(XML_Values.COUNTRY_NAME, buf.toString());
        else if (szCurrentTag.equals(XML_Values.CUSTOMER_REP))
            repDetailsXML.put(XML_Values.CUSTOMER_REP, buf.toString());
        else if (szCurrentTag.equals(XML_Values.STATOR))
            repDetailsXML.put(XML_Values.STATOR, buf.toString());
        else if (szCurrentTag.equals(XML_Values.ROTOR_POLES))
            repDetailsXML.put(XML_Values.ROTOR_POLES, buf.toString());
        else if (szCurrentTag.equals(XML_Values.MOTOR_AUXILLARIES))
            repDetailsXML.put(XML_Values.MOTOR_AUXILLARIES, buf.toString());
        else if (szCurrentTag.equals("heading"))
            repDetailsXML.put(
                    XML_Values.CYCLO_CONVERTER_HEADING,
                    buf.toString());
        else if (szCurrentTag.equals("value"))
            repDetailsXML.put(XML_Values.CYCLO_CONVERTER, buf.toString());
        else if (szCurrentTag.equals(XML_Values.LOCATION))
            repDetailsXML.put(XML_Values.LOCATION + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.DEPT_LOC))
            repDetailsXML.put(XML_Values.DEPT_LOC + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.NAME))
            repDetailsXML.put(XML_Values.NAME + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.REMARKS))
            repDetailsXML.put(XML_Values.REMARKS + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.SEND_BY))
            repDetailsXML.put(XML_Values.SEND_BY + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.TO))
            repDetailsXML.put(XML_Values.TO + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.DATE))
            repDetailsXML.put(XML_Values.DATE + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.CUSTOMER_COMMENTS))
            repDetailsXML.put(XML_Values.CUSTOMER_COMMENTS + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.COMMENTS_INTEGRATED))
            repDetailsXML.put(XML_Values.COMMENTS_INTEGRATED + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_N))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_N + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_SURNAME_NAME))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_SURNAME_NAME
                    + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_CAT))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_CAT + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_FUNCTION))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_FUNCTION + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_ARRIVAL_DATE
                    + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_DEPARTURE_DATE
                    + nCountItem, buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_ABB_DEPT_LOC))
            repDetailsXML.put(XML_Values.PERSONNEL_ABB_DEPT_LOC + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_N))
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_N + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_SURNAME))
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_SURNAME + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_CAT))
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_CAT + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_FUNCTION))
            repDetailsXML.put(
                    XML_Values.PERSONNEL_CLIENT_FUNCTION + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_FROM))
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_FROM + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_TILL))
            repDetailsXML.put(XML_Values.PERSONNEL_CLIENT_TILL + nCountItem,
                    buf.toString());
        else if (szCurrentTag.equals(XML_Values.PERSONNEL_CLIENT_DEPT_LOC))
            repDetailsXML.put(
                    XML_Values.PERSONNEL_CLIENT_DEPT_LOC + nCountItem,
                    buf.toString());

        // mill specific
        if (isCurrentSelectedMill && szCurrentTag.equals(XML_Values.PLANT))
            repDetailsXML.put(XML_Values.PLANT, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_GMD))
            repDetailsXML.put(XML_Values.PLANT_TYPE_GMD, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_CYCLO))
            repDetailsXML.put(XML_Values.PLANT_TYPE_CYCLO, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_DRIVE))
            repDetailsXML.put(XML_Values.PLANT_TYPE_DRIVE, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_EHOUSE))
            repDetailsXML.put(XML_Values.PLANT_TYPE_EHOUSE, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_MOTOR))
            repDetailsXML.put(XML_Values.PLANT_TYPE_MOTOR, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.PLANT_TYPE_TRANSFORMER))
            repDetailsXML
                    .put(XML_Values.PLANT_TYPE_TRANSFORMER, buf.toString());
        else if (isCurrentSelectedMill && szCurrentTag.equals(XML_Values.UNIT))
            repDetailsXML.put(XML_Values.UNIT, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.SYSTEM_MACHINE))
            repDetailsXML.put(XML_Values.SYSTEM_MACHINE, buf.toString());
        else if (isCurrentSelectedMill && szCurrentTag.equals(XML_Values.TYPE))
            repDetailsXML.put(XML_Values.TYPE, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.SERIAL_NO))
            repDetailsXML.put(XML_Values.SERIAL_NO, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.YEAR_OF_DELIVERY))
            repDetailsXML.put(XML_Values.YEAR_OF_DELIVERY, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.OPERATING_HOURS))
            repDetailsXML.put(XML_Values.OPERATING_HOURS, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.NEW_PLANT))
            repDetailsXML.put(XML_Values.NEW_PLANT, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.UPGRADE))
            repDetailsXML.put(XML_Values.UPGRADE, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.TROUBLESHOOTING))
            repDetailsXML.put(XML_Values.TROUBLESHOOTING, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.OTHERS))
            repDetailsXML.put(XML_Values.OTHERS, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.FIRST_INSTALLATION))
            repDetailsXML.put(XML_Values.FIRST_INSTALLATION, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.MINOR_INSPECTION))
            repDetailsXML.put(XML_Values.MINOR_INSPECTION, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.MAJOR_OVERHAUL))
            repDetailsXML.put(XML_Values.MAJOR_OVERHAUL, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.INSTALLATION))
            repDetailsXML.put(XML_Values.INSTALLATION, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.COMISSIONING))
            repDetailsXML.put(XML_Values.COMISSIONING, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.TEST_ASSESSMENT))
            repDetailsXML.put(XML_Values.TEST_ASSESSMENT, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.OTHERS_SERVICE_TASK))
            repDetailsXML.put(XML_Values.OTHERS_SERVICE_TASK, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.OVERHAUL_WORKS))
            repDetailsXML.put(XML_Values.OVERHAUL_WORKS, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.RE_COMISSIONING))
            repDetailsXML.put(XML_Values.RE_COMISSIONING, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.DIAGNOSTICS))
            repDetailsXML.put(XML_Values.DIAGNOSTICS, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.FACTORY_INSP))
            repDetailsXML.put(XML_Values.FACTORY_INSP, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.REPAIR))
            repDetailsXML.put(XML_Values.REPAIR, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.INVESTIGATION))
            repDetailsXML.put(XML_Values.INVESTIGATION, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.INSPECTION))
            repDetailsXML.put(XML_Values.INSPECTION, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.MOTOR_INSP_INSIDE))
            repDetailsXML.put(XML_Values.MOTOR_INSP_INSIDE, buf.toString());
        else if (isCurrentSelectedMill
                && szCurrentTag.equals(XML_Values.MOTOR_INSP_EXTERNAL))
            repDetailsXML.put(XML_Values.MOTOR_INSP_EXTERNAL, buf.toString());

    }

    // element content
    public void characters(char ch[], int start, int length) {
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
                    if (buf != null)
                        buf.append(ch[i]);
                    break;
            }
        }

	}
}
