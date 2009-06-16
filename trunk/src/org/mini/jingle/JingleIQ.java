package org.mini.jingle;

import org.jivesoftware.smack.packet.IQ;
import org.mini.jingle.xmpp.Jingle;

public class JingleIQ extends IQ {

    private Jingle jingle;

    public JingleIQ(Jingle jingle) {
        this.jingle = jingle;
        this.setType(IQ.Type.SET);
    }

    public String getChildElementXML() {
        return jingle.toString();
    }

    public Jingle getJingle() {
        return jingle;
    }
}
