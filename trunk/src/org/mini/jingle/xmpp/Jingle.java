package org.mini.jingle.xmpp;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias(Jingle.NAME)
public class Jingle {

    @XStreamOmitField
    final static XStream stream = new XStream(new DomDriver()) {
        protected MapperWrapper wrapMapper(MapperWrapper next) {
            return new MapperWrapper(next) {
                public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                    return definedIn != Object.class && super.shouldSerializeMember(definedIn, fieldName);
               }
            };
        }
    };

    static {

        stream.autodetectAnnotations(true);
        stream.alias("jingle", Jingle.class);

    }

    public static final String NAME = "jingle";

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    public final String NAMESPACE = "urn:xmpp:tmp:jingle";

    @XStreamOmitField
    public static final String XMLNS = "urn:xmpp:tmp:jingle";

    public static final String SESSION_INITIATE = "session-initiate";
    public static final String SESSION_ACCEPT = "session-accept";
    public static final String SESSION_TERMINATE = "session-terminate";

    @XStreamAsAttribute
    private String action, initiator, responder, sid;

    private Content content;

    public Jingle(Content content, String sid, String responder, String initiator, String action) {
        this.content = content;
        this.sid = sid;
        this.responder = responder;
        this.initiator = initiator;
        this.action = action;
    }

    public String toString() {
        return stream.toXML(this);
    }

    public static XStream getStream() {
        return stream;
    }

    public String getAction() {
        return action;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getResponder() {
        return responder;
    }

    public String getSid() {
        return sid;
    }

    public Content getContent() {
        return content;
    }
}
