package org.mini.jingle;

import org.dom4j.io.NParser;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.mini.jingle.xmpp.Jingle;

public class JingleProvider implements IQProvider {

    /**
     * Parse a iq/jingle element.
     */
    public IQ parseIQ(final XmlPullParser parser) throws Exception {

        NParser p = new NParser(parser);

        Jingle j = (Jingle) Jingle.getStream().fromXML(p.getString());

        return new JingleIQ(j);

    }
}