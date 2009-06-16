package org.mini.jingle.xmpp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("content")
public class Content {

    @XStreamAsAttribute
    private String creator, name, senders;

    private Description description;
    private RawUDPTransport transport;

    public Content(String creator, String name, String senders, Description description,RawUDPTransport transport) {
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.transport = transport;
        this.senders = senders;
    }

    public String getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public RawUDPTransport getTransport() {
        return transport;
    }
}
