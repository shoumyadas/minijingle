package org.mini.jingle.media.jmf;

import javax.media.*;
import javax.media.protocol.DataSource;
import javax.media.rtp.*;
import javax.media.rtp.event.*;

/**
 * This class implements receive methods and listeners to be used in AudioChannel
 *
 * @author Thiago Camargo
 */
public class AudioReceiver implements ReceiveStreamListener, SessionListener,
        ControllerListener {

	boolean dataReceived = false;

    Object dataSync;

    public AudioReceiver(final Object dataSync) {
        this.dataSync = dataSync;
    }

    /**
     * JingleSessionListener.
     */
    public synchronized void update(SessionEvent evt) {
        if (evt instanceof NewParticipantEvent) {
            Participant p = ((NewParticipantEvent) evt).getParticipant();
        }
    }

    /**
     * ReceiveStreamListener
     */
    public synchronized void update(ReceiveStreamEvent evt) {

        Participant participant = evt.getParticipant();    // could be null.
        ReceiveStream stream = evt.getReceiveStream();  // could be null.

        if (evt instanceof RemotePayloadChangeEvent) {
        }
        else if (evt instanceof NewReceiveStreamEvent) {

            try {
                stream = evt.getReceiveStream();
                DataSource ds = stream.getDataSource();

                // Find out the formats.
                RTPControl ctl = (RTPControl) ds.getControl("javax.jmf.rtp.RTPControl");

                // create a player by passing datasource to the Media Manager
                Player p = javax.media.Manager.createPlayer(ds);
                if (p == null)
                    return;

                p.addControllerListener(this);
                p.realize();

                // Notify intialize() that a new stream had arrived.
                synchronized (dataSync) {
                    dataReceived = true;
                    dataSync.notifyAll();
                }

            }
            catch (Exception e) {
                return;
            }

        }
        else if (evt instanceof StreamMappedEvent) {

            if (stream != null && stream.getDataSource() != null) {
                DataSource ds = stream.getDataSource();
                // Find out the formats.
                RTPControl ctl = (RTPControl) ds.getControl("javax.jmf.rtp.RTPControl");
            }
        }
        else if (evt instanceof ByeEvent) {

        }

    }

    /**
     * ControllerListener for the Players.
     */
    public synchronized void controllerUpdate(ControllerEvent ce) {

        Player p = (Player) ce.getSourceController();

        if (p == null)
            return;

        // Get this when the internal players are realized.
        if (ce instanceof RealizeCompleteEvent) {
            p.start();
        }

        if (ce instanceof ControllerErrorEvent) {
            p.removeControllerListener(this);
        }

    }
}
