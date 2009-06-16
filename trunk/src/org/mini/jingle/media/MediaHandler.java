package org.mini.jingle.media;

import org.mini.jingle.xmpp.Description;
import org.mini.jingle.xmpp.Candidate;
import org.mini.jingle.xmpp.PayloadType;

import java.util.List;
import java.util.ArrayList;

public abstract class MediaHandler {

    private final List<PayloadType> supportedPayloadTypes = new ArrayList<PayloadType>();

    public abstract void startMedia(final Candidate localCandidate, final Candidate remoteCandidate, final Description description);

    public abstract void stopMedia();

    public List<PayloadType> getSupportedPayloadTypes() {
        return supportedPayloadTypes;
    }
}
