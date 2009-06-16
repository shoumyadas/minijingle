package org.dom4j.io;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author Thiago Rocha Camargo (barata7@gmail.com) - barata7@gmail.com
 *         Date: Aug 13, 2008
 *         Time: 6:03:12 PM
 */
public class NParser extends XPP3Reader {

    XmlPullParser parser;

    public NParser(XmlPullParser parser) {
        this.parser = parser;
    }

    public XmlPullParser getXPPParser() throws XmlPullParserException {
        return parser;
    }

    public String getString() {

        StringBuilder str = new StringBuilder();
        int ll = -1, lc = -1;

        final String name = parser.getName(); 

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG || parser.getEventType() == XmlPullParser.END_TAG) {
                    int cl = parser.getLineNumber();
                    int cc = parser.getColumnNumber();
                    if (ll != cl || lc != cc) {
                        str.append(parser.getText());
                        if (parser.getEventType() == XmlPullParser.END_TAG && name.equals(parser.getName())) {
                            break;
                        }else if(parser.getEventType() != XmlPullParser.END_TAG&&parser.isEmptyElementTag()&&name.equals(parser.getName())){
                            break;
                        }
                    }
                }
                ll = parser.getLineNumber();
                lc = parser.getColumnNumber();
                parser.next();
            }

            System.out.println("Received STR: " + str.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }

}