package org.mini.jingle;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.mini.jingle.xmpp.*;
import org.mini.jingle.media.MediaHandler;

public class CallManager implements PacketListener {

    private final XMPPConnection connection;
    private String sid = null;
    private final Candidate localCandidate;
    private Candidate remoteCandidate;
    private final RawUDPTransport transport;
    private final Description description;
    private final Content content;
    private final MediaHandler mediaHandler;
    private String proxy;

    public CallManager(final XMPPConnection connection, final Candidate localCandidate, final MediaHandler mediaHandler, final String proxy) {
        this.connection = connection;
        connection.addPacketListener(this, new PacketFilter() {
            public boolean accept(Packet packet) {
                return packet instanceof JingleIQ;
            }
        });
        enableJingle(connection);
        this.mediaHandler = mediaHandler;
        this.localCandidate = localCandidate;
        this.transport = new RawUDPTransport(localCandidate);
        this.description = new Description("audio", mediaHandler.getSupportedPayloadTypes());
        this.content = new Content("responder", "media", "both", description, transport);
        this.proxy = proxy;
    }

    public void processPacket(Packet packet) {
        if (packet instanceof JingleIQ) {
            final JingleIQ iq = (JingleIQ) packet;
            final Jingle j = iq.getJingle();
            final JingleIQ reply;

            connection.sendPacket(createResult(iq));

            if (sid == null) {

                if (Jingle.SESSION_INITIATE.equals(iq.getJingle().getAction())) {
                    // Pick The Call
                    this.sid = j.getSid();
                    this.remoteCandidate = j.getContent().getTransport().getCandidate();
                    final Jingle jr = new Jingle(content, j.getSid(), connection.getUser(), j.getInitiator(), Jingle.SESSION_ACCEPT);
                    reply = new JingleIQ(jr);
                    reply.setTo(iq.getFrom());
                    reply.setFrom(iq.getTo());
                    mediaHandler.startMedia(localCandidate, remoteCandidate, j.getContent().getDescription());
                    connection.sendPacket(reply);
                }
                else {
                    // Terminate Incomming Calls when within a call
                    final Jingle jr = new Jingle(null, j.getSid(), j.getResponder(), j.getInitiator(), Jingle.SESSION_TERMINATE);
                    reply = new JingleIQ(jr);
                    reply.setTo(iq.getFrom());
                    reply.setFrom(iq.getTo());
                    connection.sendPacket(reply);
                }

            }
            else {

                if (Jingle.SESSION_ACCEPT.equals(iq.getJingle().getAction()) && j.getSid() != null && j.getSid().equals(sid)) {
                    // Call Picked
                    this.remoteCandidate = j.getContent().getTransport().getCandidate();
                    mediaHandler.startMedia(localCandidate, remoteCandidate, j.getContent().getDescription());
                }
                else if (Jingle.SESSION_TERMINATE.equals(iq.getJingle().getAction()) && j.getSid() != null && j.getSid().equals(sid)) {
                    // Call Terminated
                    sid = null;
                    mediaHandler.stopMedia();
                }

            }
        }
    }

    public void call(final String responder) {

        final Jingle jr = new Jingle(content, String.valueOf(System.currentTimeMillis()), responder, connection.getUser(), Jingle.SESSION_INITIATE);
        JingleIQ reply = new JingleIQ(jr);
        reply.setTo(proxy != null ? proxy : responder);
        reply.setFrom(connection.getUser());
        connection.sendPacket(reply);

    }

    public static void enableJingle(final XMPPConnection connection) {
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Jingle.XMLNS);

        Presence presence = new Presence(Presence.Type.available);
        presence.addExtension(new PacketExtension() {
            public String getElementName() {
                return "c";
            }

            public String getNamespace() {
                return "http://jabber.org/protocol/caps";
            }

            public String toXML() {
                return "<c xmlns=\"http://jabber.org/protocol/caps\" node=\"client:caps\" ver=\"0.1\" ext=\"jingle-v1\"></c>";
            }
        });

        connection.sendPacket(presence);
    }

    public static IQ createResult(final IQ request) {
        IQ iq = new IQ() {
            public String getChildElementXML() {
                return "";
            }
        };
        iq.setType(IQ.Type.RESULT);
        iq.setTo(request.getFrom());
        iq.setFrom(request.getTo());
        return iq;
    }
}
