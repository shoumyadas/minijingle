package org.mini.jingle.xmpp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("payload-type")
public class PayloadType {

    @XStreamAsAttribute
    private String id, name, clockrate;

    public PayloadType(String clockrate, String id, String name) {
        this.clockrate = clockrate;
        this.id = id;
        this.name = name;
    }
}
