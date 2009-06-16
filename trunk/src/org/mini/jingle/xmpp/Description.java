package org.mini.jingle.xmpp;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("description")
public class Description {

    @XStreamOmitField
    final XStream stream = new XStream(new DomDriver());

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    public final String NAMESPACE = "urn:xmpp:tmp:jingle:apps:rtp";

    @XStreamImplicit
    private List<PayloadType> payloadTypes = new ArrayList<PayloadType>();

    @XStreamAsAttribute
    private String media;

    public Description(String media, List<PayloadType> payloadTypes) {
        this.media = media;
        this.payloadTypes = payloadTypes;
    }

}
