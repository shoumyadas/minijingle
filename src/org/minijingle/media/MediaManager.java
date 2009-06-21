package org.minijingle.media;

import org.minijingle.jingle.description.Payload;
import org.minijingle.jingle.transport.Candidate;

import java.util.List;

public interface MediaManager {

    public void startMedia(final Candidate local, final Candidate remote, final Payload payload);

    public void stopMedia();

    public List<Payload> getPayloads();
}
