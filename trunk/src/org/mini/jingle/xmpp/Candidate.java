package org.mini.jingle.xmpp;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.ArrayList;
import java.util.List;

import org.mini.jingle.network.LocalIPResolver;

@XStreamAlias("candidate")
public class Candidate {

    @XStreamOmitField
    final XStream stream = new XStream(new DomDriver());

    @XStreamAsAttribute
    private final String ip, port, generation;

    public Candidate(String ip, String port, String generation) {
        this.ip = ip;
        this.port = port;
        this.generation = generation;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getGeneration() {
        return generation;
    }

}
