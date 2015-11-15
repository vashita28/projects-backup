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

public class SummaryXMLHandler extends DefaultHandler {

    // string to track each entry
    String szCurrentTag = "";
    int nCount = -1;

    // flag to keep track of XML processing
    private boolean isItem = false, isMotorDataItem = false, isSpecficMotorDataItem = false;

    // context for user interface
    private Context mContext;

    // input stream
    InputStream inputStream;

    HashMap<String, String> summaryXML;

    boolean bIsSummary = false, bIsServiceInformation = false,
            isCurrentSelectedMill = false;

    StringBuffer buf;

    // constructor
    public SummaryXMLHandler(Context cont, InputStream inputS,
                             HashMap<String, String> inputHashMap) {
        super();
        mContext = cont;
        inputStream = inputS;
        summaryXML = inputHashMap;

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
            Log.e("SummaryXMLHandler", "SAX Error " + se.getMessage());
        } catch (IOException ie) {
            Log.e("SummaryXMLHandler", "Input Error " + ie.getMessage());
        } catch (Exception oe) {
            Log.e("SummaryXMLHandler", "Unspecified Error " + oe.getMessage());
        }

    }

    // start of the XML document
    public void startDocument() {
        // Log.i("SummaryXMLHandler", "Start of XML document");
    }

    // end of the XML document
    public void endDocument() {
        // Log.i("SummaryXMLHandler", "End of XML document");
    }

    // opening element tag
    public void startElement(String uri, String name, String qName,
                             Attributes atts) {
        // handle the start of an element

        // Log.i("SummaryXMLHandler", "startElement :: " + qName);
        buf = new StringBuffer();

        if (qName.equals("summary")) {
            bIsSummary = true;
        }
        if (bIsSummary && qName.equals(XML_Values.MILL)) {
            String szMillIDInXML = atts.getValue("id");
            String szMIllIDCurrentSelected = Util.getMillID(mContext,
                    Util.getMillName(mContext));

            if (szMillIDInXML.equals(szMIllIDCurrentSelected)) {
                isCurrentSelectedMill = true;
            }
        }

        if (bIsSummary && qName.equals(XML_Values.SERVICE_INFORMATION)) {
            bIsServiceInformation = true;
        }

        if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION_ITEM)) {
            nCount++;
        }

        if (qName.equals(XML_Values.SUMMARY_OF_INSPECTION)
                || qName.equals(XML_Values.MAIN_DIFFICULTIES)
                || qName.equals(XML_Values.RECOMMENDATIONS)
                || qName.equals(XML_Values.POINT_OF_GENERAL_INTEREST)) {

            szCurrentTag = qName;
        }

        // the element is a item
        if (bIsSummary && qName.equals("item"))
            isItem = true;

        if (isCurrentSelectedMill
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_ITEM)) {
            isMotorDataItem = true;
            nCount++;
        }

        if (isCurrentSelectedMill
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_ITEM)) {
            isSpecficMotorDataItem = true;
            nCount++;
        }
    }

    // closing element tag
    public void endElement(String uri, String name, String qName) {
        // handle the end of an element

        // Log.i("SummaryXMLHandler", "endElement :: " + name);

        if (bIsSummary && qName.equals(XML_Values.MILL)) {
            isCurrentSelectedMill = false;
        }
        if (qName.equals("summary")) {
            bIsSummary = false;
        }

        if (bIsSummary && qName.equals("item")) {
            isItem = false;
            // prepare for the next item
            if (szCurrentTag.equals(XML_Values.SUMMARY_OF_INSPECTION)
                    && bIsSummary) {
                nCount++;
                summaryXML.put(XML_Values.SUMMARY_OF_INSPECTION + nCount,
                        buf.toString());
            }

            if (isCurrentSelectedMill
                    && szCurrentTag.equals(XML_Values.MAIN_DIFFICULTIES)) {
                nCount++;
                summaryXML.put(XML_Values.MAIN_DIFFICULTIES + nCount,
                        buf.toString());
            } else if (isCurrentSelectedMill
                    && szCurrentTag.equals(XML_Values.RECOMMENDATIONS)) {
                nCount++;
                summaryXML.put(XML_Values.RECOMMENDATIONS + nCount,
                        buf.toString());
            } else if (isCurrentSelectedMill
                    && szCurrentTag
                    .equals(XML_Values.POINT_OF_GENERAL_INTEREST)) {
                nCount++;
                summaryXML.put(XML_Values.POINT_OF_GENERAL_INTEREST + nCount,
                        buf.toString());
            }

        }

        if (isCurrentSelectedMill
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_ITEM))
            isMotorDataItem = false;

        if (isCurrentSelectedMill
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_ITEM))
            isSpecficMotorDataItem = false;

        if (qName.equals(XML_Values.SUMMARY_OF_INSPECTION) && bIsSummary) {
            summaryXML.put("count_items_SOI", String.valueOf(nCount));
            resetCounter();
        }

        if (isCurrentSelectedMill && qName.equals(XML_Values.MAIN_DIFFICULTIES)) {
            summaryXML.put("count_items_MD", String.valueOf(nCount));
            resetCounter();
        } else if (isCurrentSelectedMill
                && qName.equals(XML_Values.RECOMMENDATIONS)) {
            summaryXML.put("count_items_Recomm", String.valueOf(nCount));
            resetCounter();
        } else if (isCurrentSelectedMill
                && qName.equals(XML_Values.POINT_OF_GENERAL_INTEREST)) {
            summaryXML.put("count_items_POGI", String.valueOf(nCount));
            resetCounter();
        }

        if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION_N)) {
            summaryXML.put(XML_Values.SERVICE_INFORMATION_N + nCount,
                    buf.toString());
        } else if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION_DESCRIPTION)) {
            summaryXML.put(XML_Values.SERVICE_INFORMATION_DESCRIPTION + nCount,
                    buf.toString());
        } else if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION_FROM)) {
            summaryXML.put(XML_Values.SERVICE_INFORMATION_FROM + nCount,
                    buf.toString());
        } else if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION_TILL)) {
            summaryXML.put(XML_Values.SERVICE_INFORMATION_TILL + nCount,
                    buf.toString());
        } else if (bIsServiceInformation
                && qName.equals(XML_Values.SERVICE_INFORMATION)) {
            summaryXML.put(XML_Values.SERVICE_INFORMATION_COUNT,
                    String.valueOf(nCount));
            bIsServiceInformation = false;
            resetCounter();
        }

        if (isCurrentSelectedMill && isMotorDataItem
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_N)) {
            summaryXML.put(XML_Values.MAIN_MOTOR_DATA_N + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isMotorDataItem
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_DESCRIPTION)) {
            summaryXML.put(XML_Values.MAIN_MOTOR_DATA_DESCRIPTION + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isMotorDataItem
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_DATA)) {
            summaryXML.put(XML_Values.MAIN_MOTOR_DATA_DATA + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isMotorDataItem
                && qName.equals(XML_Values.MAIN_MOTOR_DATA_REMARKS)) {
            summaryXML.put(XML_Values.MAIN_MOTOR_DATA_REMARKS + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && qName.equals(XML_Values.MAIN_MOTOR_DATA)) {
            summaryXML.put(XML_Values.MAIN_MOTOR_DATA_COUNT,
                    String.valueOf(nCount));
            resetCounter();
        }


        if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_N)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_N + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_DESCRIPTION)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_DESCRIPTION + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_DATA)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_DATA + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_QTY)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_QTY + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_SIZE)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_SIZE + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_NM)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_NM + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_TYPE)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_TYPE + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && isSpecficMotorDataItem
                && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA_REMARKS)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_REMARKS + nCount,
                    buf.toString());
        } else if (isCurrentSelectedMill && qName.equals(XML_Values.SPECIFIC_MOTOR_DATA)) {
            summaryXML.put(XML_Values.SPECIFIC_MOTOR_DATA_COUNT,
                    String.valueOf(nCount));
            resetCounter();
        }


    }

    void resetCounter() {
        // reset the variable for future items
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
